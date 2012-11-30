//
//  CallbackManager.cpp
//  QVOD
//
//  Created by bigbug on 11-11-21.
//  Copyright (c) 2011年 qvod. All rights reserved.
//

#include "SysConsts.h"
#include "CallbackManager.h"
using namespace::std;


CCallbackManager::CCallbackManager()
{
	for (int i = 0; i < MAP_SIZE; ++i) {
		m_mapCallbacks[i].nID = -1;
	}
}

CCallbackManager::~CCallbackManager()
{
    
}

CCallbackManager* CCallbackManager::GetInstance()
{
    static CCallbackManager s_CallbackManager;
    
    return &s_CallbackManager;
}

int CCallbackManager::SetCallback(int nType, PCallback pfnCallback, void* pUserData, void* pReserved)
{
    CallbackData cbd;
    
    cbd.nType       = nType;
    cbd.pfnCallback = pfnCallback;
    cbd.pUserData   = pUserData;
    cbd.pReserved   = pReserved;
    AssertValid(pfnCallback);
    
    for (int i = 0; i < MAP_SIZE; ++i) {
    	if (m_mapCallbacks[i].nID != -1) {
    		if (m_mapCallbacks[i].data.nType == nType) {
    			m_mapCallbacks[i].data = cbd;
    			return S_OK;
    		}
    	}
    }

    for (int i = 0; i < MAP_SIZE; ++i) {
    	if (m_mapCallbacks[i].nID == -1) {
    		m_mapCallbacks[i].nID  = i;
    		m_mapCallbacks[i].data = cbd;
    		break;
    	}
    }

   // m_mapCallbacks[nType] = cbd;
    
    return S_OK;
}

CallbackData& CCallbackManager::GetCallbackData(int nType)
{
    //return m_mapCallbacks[nType];
	int i = 0;
	for (; i < MAP_SIZE; ++i) {
		if (m_mapCallbacks[i].data.nType == nType) {
			break;
		}
	}
	return m_mapCallbacks[i].data;
}
