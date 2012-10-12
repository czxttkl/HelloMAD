/*
 * DictionaryInterf.h
 *
 *  Created on: Oct 5, 2012
 *      Author: bigbug
 */

#ifndef DICTIONARY_INTERF_H_
#define DICTIONARY_INTERF_H_

#ifdef __cplusplus
extern "C" {
#endif

typedef struct _LOADINFO
{
	int 	nFileDescriptor;
	int 	nOffsetInAPK;
	char*	pDictionaryName;
} LOADINFO;

int Create();
int Destroy();
int Load(LOADINFO* pInfo);
int Unload(const char* pszDictName);
int IsLoaded(const char* pszDictName);
int LookupWord(const char* pszWord);

#ifdef __cplusplus
}
#endif

#endif /* DICTIONARY_INTERF_H_ */
