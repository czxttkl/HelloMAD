/*
 * DictionaryInterf.cpp
 *
 *  Created on: Oct 5, 2012
 *      Author: bigbug
 */

#include "OpusInterf.h"
#include "Opus.h"

static IOpus* g_pOpus = COpus::GetInstance();

int CreateEncoder(int nBitrate, int nSampleRate, int nChannels, int nApplication, const char* pszPath)
{
	int nResult = g_pOpus->CreateEncoder(nBitrate, nSampleRate, nChannels, nApplication, pszPath);

	return nResult;
}

int CreateDecoder(const char* pszPath)
{
	int nResult = g_pOpus->CreateDecoder(pszPath);

	return nResult;
}

int DestroyEncoder()
{
	int nResult = g_pOpus->DestroyEncoder();

	return nResult;
}

int DestroyDecoder()
{
	int nResult = g_pOpus->DestroyDecoder();

	return nResult;
}

int Encode(unsigned char* pInPCM, int nSize)
{
	int nResult = g_pOpus->Encode(pInPCM, nSize);

	return nResult;
}

int Decode(unsigned char* pOutPCM, int nSize)
{
	int nResult = g_pOpus->Decode(pOutPCM, nSize);

	return nResult;
}

int ConvertFile(const char* pszFrom, const char* pszTo)
{
	int nResult = g_pOpus->ConvertFile(pszFrom, pszTo);

	return nResult;
}

int CancelConverting()
{
	int nResult = g_pOpus->CancelConverting();

	return nResult;
}

int SetCallback(int nCallbackType, PCallback pfnCallback, void* pUserData, void* pReserved)
{
    int nResult = g_pOpus->SetCallback(nCallbackType, pfnCallback, pUserData, pReserved);

    return nResult;
}

int SetParameter(int nParam, void* pValue)
{
	int nResult = g_pOpus->SetParameter(nParam, pValue);

	return nResult;
}

int GetParameter(int nParam, void* pValue)
{
	int nResult = g_pOpus->GetParameter(nParam, pValue);

	return nResult;
}


