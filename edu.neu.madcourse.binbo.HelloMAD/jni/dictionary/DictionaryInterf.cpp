/*
 * DictionaryInterf.cpp
 *
 *  Created on: Oct 5, 2012
 *      Author: bigbug
 */

#include "DictionaryInterf.h"
#include "Dictionary.h"

static IDictionary* g_pDict = NULL;

int Create()
{
	g_pDict = CDictionary::GetInstance();
	if (!g_pDict) {
		return E_FAIL;
	}
	return S_OK;
}

int Destroy()
{
	return S_OK;
}

int Load(LOADINFO* pInfo)
{
    if (!g_pDict) {
    	return E_FAIL;
    }

    int nResult = g_pDict->Load(pInfo);

    return nResult;
}

int Unload(const char* pszDictName)
{
    if (!g_pDict) {
        return E_FAIL;
    }

    int nResult = g_pDict->Unload(pszDictName);

    return nResult;
}

int IsLoaded(const char* pszDictName)
{
	if (!g_pDict) {
		return 0;
	}

	return g_pDict->IsLoaded(pszDictName);
}

int LookupWord(const char* pszWord)
{
	if (!g_pDict) {
		return 0;
	}

	return g_pDict->LookupWord(pszWord);
}

