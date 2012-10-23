/*
 * Dictionary.cpp
 *
 *  Created on: Oct 5, 2012
 *      Author: bigbug
 */
#include <iostream>
#include <string.h>
#include "Dictionary.h"
#include "Huffman.h"

CSubDictionary::CSubDictionary(const char* pszName)
{
	memset(this, 0, sizeof(CSubDictionary));

	SetName(pszName);
}

CSubDictionary::~CSubDictionary()
{
	m_vecWords.clear();

	if (m_pContent) {
		delete[] m_pContent;
		m_pContent = NULL;
	}
}

void CSubDictionary::SetName(const char* pszName)
{
	strncpy(m_szName, pszName, NAME_LENGTH);
}

int CSubDictionary::Load(LOADINFO* pInfo)
{
	FILE* fp = NULL;

	// try to open the apk file which holds the assets
	if ((fp = fdopen(pInfo->nFileDescriptor, "rb")) == NULL) {
		return E_FAIL;
	}
	// get the file size before compressing
	LONGLONG llFileSize = 0; // The worlist is encoded on a 64bit PC
	int nFileNameSize = 0;
	fseek(fp, pInfo->nOffsetInAPK + sizeof(UINT), SEEK_SET);
	fread(&nFileNameSize, sizeof(BYTE), 1, fp);
	fseek(fp, nFileNameSize, SEEK_CUR);
	fread(&llFileSize, sizeof(LONGLONG), 1, fp);

	// allocate the memory according to the original file size
	m_pContent = new char[llFileSize];
	if (!m_pContent) {
		fclose(fp);
		return E_FAIL;
	}
	// decompress the file and save the content
	decompress_v2(fp, pInfo->nOffsetInAPK, m_pContent);
	// split words from the content
	int nCount = WORD_COUNT[m_szName[0] - 'a'];
	m_vecWords.reserve(nCount);
	char* pWord = strtok(m_pContent, "\n");
	for (int i = 0; i < nCount; ++i) {
		m_vecWords[i] = pWord;
		pWord = strtok(NULL, "\n");
		//Log("%s", m_vecWords[i]);
	}
	// finally, close the apk file descriptor
	fclose(fp);

	return S_OK;
}

int CSubDictionary::IsLoaded()
{
	if (m_pContent) {
		return TRUE;
	}

	return FALSE;
}

int CSubDictionary::Unload()
{
	return S_OK;
}

int CSubDictionary::LookupWord(const char* pszWord)
{
	return Search(pszWord + 1, 0, WORD_COUNT[pszWord[0] - 'a'] - 1, 0);
}

void CSubDictionary::SearchRange(char c, int nStep, int* pBeg, int* pEnd)
{
	int nBeg = *pBeg;
	int nMid = (*pEnd + *pBeg) >> 1;
	int nEnd = *pEnd;
	// search for the first word beginning with cRange
	while (nEnd - nBeg > 1) {
		char cMid = m_vecWords[nMid][nStep];
		if (cMid < c) {
			nBeg = nMid;
		} else if (cMid > c) {
			nEnd = nMid;
		} else {
			nEnd = nMid;
		}
		nMid = (nBeg + nEnd) >> 1;
	}
	if (m_vecWords[nBeg][nStep] != c && m_vecWords[nEnd][nStep] == c) {
		nBeg += 1;
	} else if (m_vecWords[nBeg][nStep] != c && m_vecWords[nEnd][nStep] != c) {
		*pBeg = *pEnd = -1;
		return;
	}
	int nTmp = nBeg;
	// search for the last word beginning with cRange
	nBeg = *pBeg;
	nMid = (*pEnd + *pBeg) >> 1;
	nEnd = *pEnd;
	while (nEnd - nBeg > 1) {
		char cMid = m_vecWords[nMid][nStep];
		if (cMid < c) {
			nBeg = nMid;
		} else if (cMid > c) {
			nEnd = nMid;
		} else {
			nBeg = nMid;
		}
		nMid = (nBeg + nEnd) >> 1;
	}
	if (m_vecWords[nBeg][nStep] == c && m_vecWords[nEnd][nStep] != c) {
		nEnd -= 1;
	}

	*pBeg = nTmp;
	*pEnd = nEnd;
}

int CSubDictionary::Search(const char* pszWord, int nBeg, int nEnd, int nStep)
{
	SearchRange(pszWord[nStep], nStep, &nBeg, &nEnd);

	if (nBeg == nEnd) {
		if (nBeg == -1) { // haven't found
			return FALSE;
		}
		if (!strcmp(pszWord, m_vecWords[nBeg])) {
			return TRUE;
		}
	}

	if (pszWord[nStep + 1] == '\0') {
		if (!strcmp(pszWord, m_vecWords[nBeg])) {
			return TRUE;
		}
		return FALSE;
	}

	return Search(pszWord, nBeg, nEnd, nStep + 1);
}

char* CSubDictionary::GetName()
{
	return m_szName;
}

char* CSubDictionary::GetContent()
{
	return m_pContent;
}

///////////////////////////////////////////////////////////////////////////////

CDictionary::CDictionary()
{
}

CDictionary::~CDictionary()
{
}

CDictionary* CDictionary::GetInstance()
{
	static CDictionary s_dict;

	return &s_dict;
}

int CDictionary::Load(LOADINFO* pInfo)
{
	CAutoLock cObjectLock(this);

	if (!pInfo) {
		return E_FAIL;
	}

	if (IsLoaded(pInfo->pDictionaryName)) {
		return S_OK;
	}

	CSubDictionary* pSD = new CSubDictionary(pInfo->pDictionaryName);
	AssertValid(pSD);
	if (pSD->Load(pInfo) != S_OK) {
		delete pSD;
		Log("Can't load %s\n", pInfo->pDictionaryName);
		return E_FAIL;
	}

	m_vecDicts.push_back(pSD);
	Log("Load %s successful!\n", pInfo->pDictionaryName);

	return S_OK;
}

int CDictionary::Unload(const char* pszDictName)
{
	CAutoLock cObjectLock(this);

	return S_OK;
}

int CDictionary::IsLoaded(const char* pszSubDictName)
{
	CAutoLock cObjectLock(this);

	for (int i = 0; i < m_vecDicts.size(); ++i) {
		CSubDictionary* pSubDict = m_vecDicts[i];
		if (!strcmp(pSubDict->GetName(), pszSubDictName)) {
			return pSubDict->IsLoaded();
		}
	}

	return FALSE;
}

int CDictionary::LookupWord(const char* pszWord)
{
	CAutoLock cObjectLock(this);

	for (int i = 0; i < m_vecDicts.size(); ++i) {
		CSubDictionary* pSubDict = m_vecDicts[i];

		AssertValid(pSubDict);
		if (!strncmp(pSubDict->GetName(), pszWord, 1)) {
			return pSubDict->LookupWord(pszWord);
		}
	}

	return FALSE;
}

CSubDictionary* CDictionary::GetSubDictionary(const char* pszName)
{
	CAutoLock cObjectLock(this);

	for (int i = 0; i < m_vecDicts.size(); ++i) {
		CSubDictionary* pSubDict = m_vecDicts[i];

		AssertValid(pSubDict);
		if (!strcmp(pSubDict->GetName(), pszName)) {
			return pSubDict;
		}
	}

	return NULL;
}

