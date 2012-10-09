/*
 * dictionary.h
 *
 *  Created on: Oct 5, 2012
 *      Author: bigbug
 */

#ifndef DICTIONARY_H_
#define DICTIONARY_H_

#include "Utinities/Lock.h"
#include "DictionaryInterf.h"
#include "BaseObject.h"

#include <string>
using std::string;
#include <vector>
using std::vector;

struct IDictionary
{
	virtual int Load(LOADINFO* pInfo) = 0;
	virtual int Unload(const char* pszSubDictName) = 0;
	virtual int IsLoaded(const char* pszSubDictName) = 0;
	virtual int LookupWord(const char* pszWord) = 0;
};

const int LETTER_COUNT = 26;
const int NAME_LENGTH = 16;

const int WORD_COUNT[LETTER_COUNT] = {
	27498, 21624, 36687, 22157, 16156, 14117, 12968, 15660,
	14747, 3254, 4964, 11555, 23264, 14614, 14543, 39446,
	2139, 20075, 46052, 21724, 23850, 6279, 8034, 572, 1432, 886
};

class CSubDictionary : public CBaseObject
{
public:
	CSubDictionary(const char* pszName);
	~CSubDictionary(); // no virtual here to save memory

	int Load(LOADINFO* pInfo);
	int Unload();
	int IsLoaded();
	int LookupWord(const char* pszWord);

	char* GetName();
	char* GetContent();

protected:
	void SetName(const char* pszName);
	int  Search(const char* pszWord, int nBeg, int nEnd, int nStep);
	void SearchRange(char c, int nStep, int* pBeg, int* pEnd);

	int    m_nSize;
	char   m_szName[NAME_LENGTH + 1];
	char*  m_pContent;

	vector<char*> m_vecWords;
};

class CDictionary : public CBaseObject,
					public CLock,
					public IDictionary
{
public:
	CDictionary();
	~CDictionary(); // no virtual here to save memory

	static CDictionary* GetInstance();

	// IDictionary
	virtual int Load(LOADINFO* pInfo);
	virtual int Unload(const char* pszSubDictName);
	virtual int IsLoaded(const char* pszSubDictName);
	virtual int LookupWord(const char* pszWord);

protected:
	CSubDictionary* GetSubDictionary(const char *pszSubDictName);

	vector<CSubDictionary*> m_vecDicts;
};

#endif /* DICTIONARY_H_ */
