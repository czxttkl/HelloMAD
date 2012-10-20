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

#define WORD_LENGTH 15

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
//
//const char* MoveToStartOfTheWord(const char* pszCur, const char* pszDictStart)
//{
//	while (pszCur >= pszDictStart) {
//		if (*pszCur == '\n') {
//			if (pszCur[1] == '\0') {
//				;
//			} else {
//				return &pszCur[1];
//			}
//		}
//		--pszCur;
//	}
//	// here we get the start of the dictionary
//	return pszDictStart;
//}
//
///*
// * find the first word starting with cLetter in the given dictionary of nLength.
// * the starting position is increased one after each search step.
// * pszStart[nLength - 1] should be '\n'
// */
//const char* FindFirstWordStartWith(char cLetter, const char* pszStart, int nLength, int nStep)
//{
//	char cBeg, cMid, cEnd;
//	const char* pszEnd = MoveToStartOfTheWord(pszStart + nLength - 2, pszStart);
//	const char* pszMid = MoveToStartOfTheWord(pszStart + nLength / 2, pszStart);
//
//	cBeg = pszStart[nStep]; // pszStart is at the beginning of the dictionary
//	cMid = pszMid[nStep];
//	cEnd = pszEnd[nStep];
//	nLength /= 2;
//
//	// binary search for cLetter
//	char* pLastMid = pszMid;
//	while (nLength) {
//		if (cMid < cLetter) {
//			pszStart = pszMid;
//		} else if (cMid > cLetter) {
//			pszEnd = pszMid;
//		} else { // cMid == cLetter
//			pszEnd = pszMid;
//		}
//		nLength /= 2;
//		pszMid = MoveToStartOfTheWord(pszStart + nLength, pszStart);
//		if (pLastMid == pszMid) {
//
//		}
//		pLastMid = pszMid;
//
//		cBeg = pszStart[nStep];
//		cMid = pszMid[nStep];
//		cEnd = pszEnd[nStep];
//	}
//
//	return pszMid;
//}
//
//const char* FindLastWordStartWith(char cLetter, const char* pszStart, int nLength, int nStep)
//{
//	char cBeg, cEnd, cMid;
//	const char* pszEnd = MoveToStartOfTheWord(pszStart + nLength - 1, pszStart);
//	const char* pszMid = MoveToStartOfTheWord(pszStart + nLength / 2, pszStart);
//
//	cBeg = pszStart[nStep]; // pszStart is at the beginning of the dictionary
//	cMid = pszMid[nStep];
//	cEnd = pszEnd[nStep];
//	nLength /= 2;
//
//	// binary search for cLetter
//	while (nLength) {
//		if (cMid < cLetter) {
//			pszStart = pszMid;
//		} else if (cMid > cLetter) {
//			pszEnd = pszMid;
//		} else { // cMid == cLetter
//			pszStart = pszMid;
//		}
//		nLength /= 2;
//		pszMid = MoveToStartOfTheWord(pszStart + nLength, pszStart);
//
//		cBeg = pszStart[nStep];
//		cMid = pszMid[nStep];
//		cEnd = pszEnd[nStep];
//	}
//
//	return pszMid;
//}
//
///*
// * pszStartOfDict	the start of the dictionary
// * nLength			the length of the dictionary in byte
// * pszWord			the word to look for
// * nStep			the current search step, plus one for each search
// * note: pszStartOfDict[nLength - 1] should be '\n'
// * This function search the word from its first letter. Binary search is used to
// * locate the first letter of the word each time.
// */
//int CDictionary::SearchWord(const char* pszStartOfDict, int nLength, const char* pszWord, int nStep)
//{
//	char cLetter = pszWord[nStep];
//	const char* pszEndOfDict = pszStartOfDict + nLength;
//
//	if (pszStartOfDict[1] == '\n') {
//		pszStartOfDict += 2;
//		nLength -= 2;
//		if (nLength <= 0) {
//			return 0;
//		}
//	}
//
//	const char* pszFirst = FindFirstWordStartWith(cLetter, pszStartOfDict, nLength, nStep);
//	const char* pszLast  = FindLastWordStartWith(cLetter, pszStartOfDict, nLength, nStep);
//
//	if (pszFirst == NULL) { // not found
//		assert(pszLast == NULL);
//		return 0;
//	}
//	assert(pszLast != NULL);
//	if (pszFirst == pszLast) {
//		// retrieve the word
//		char szWord[WORD_LENGTH + 1];
//		for (int i = 0; i < WORD_LENGTH; ++i) {
//			if (pszFirst[i] == '\n') {
//				szWord[i] = 0;
//				break;
//			}
//			szWord[i] = pszFirst[i];
//		}
//		return !strcmp(szWord, pszWord);
//	}
//
//	while (*pszLast != '\n') {
//		++pszLast;
//	}
//
//	return SearchWord(pszFirst, pszLast - pszFirst + 1, pszWord, nStep + 1);
//}
