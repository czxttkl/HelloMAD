#ifndef OPUS_H_
#define OPUS_H_

#include "Utinities/Lock.h"
#include "Codecs.h"
#include "DependencyObject.h"


typedef int (*PCallback)(void* pUserData, void* pReserved);

struct IOpus
{
	virtual int CreateEncoder(int nBitrate, int nSampleRate, int nChannels, int nApplication, const char* pszPath) = 0;
	virtual int CreateDecoder(const char* pszPath) = 0;
	virtual int DestroyEncoder() = 0;
	virtual int DestroyDecoder() = 0;
	virtual int Encode(BYTE* pInPCM, int nSize) = 0;
	virtual int Decode(BYTE* pOutPCM, int nSize) = 0;
	virtual int ConvertFile(const char* pszFrom, const char* pszTo) = 0;
	virtual int CancelConverting() = 0;
	virtual int SetCallback(int nType, PCallback pfnCallback, void* pUserData, void* pReserved) = 0;
	virtual int SetParameter(int nParam, void* pValue) = 0;
	virtual int GetParameter(int nParam, void* pValue) = 0;
};

class COpus : public CBaseObject,
			  public CLock,
		      public IOpus,
		      public IDependency
{
public:
	COpus();
	virtual ~COpus(); // no virtual here to save memory

	static COpus* GetInstance();

	// IOpus
	virtual int CreateEncoder(int nBitrate, int nSampleRate, int nChannels, int nApplication, const char* pszPath);
	virtual int CreateDecoder(const char* pszPath);
	virtual int DestroyEncoder();
	virtual int DestroyDecoder();
	virtual int Encode(BYTE* pInPCM,  int nSize);
	virtual int Decode(BYTE* pOutPCM, int nSize);
	virtual int ConvertFile(const char* pszFrom, const char* pszTo);
	virtual int CancelConverting();
	virtual int SetCallback(int nType, PCallback pfnCallback, void* pUserData, void* pReserved);
	virtual int SetParameter(int nParam, void* pValue);
	virtual int GetParameter(int nParam, void* pValue);

	// IDependency
	virtual int ReceiveEvent(void* pSender, int nEvent, DWORD dwParam1, DWORD dwParam2, void* pUserData);

protected:
	// Structure to hold event parameters
	struct EventParam
	{
		EventParam();
		EventParam(int nEvent, DWORD dwParam1, DWORD dwParam2, void* pUserData, void* pReserved);

		int    nEvent;
		DWORD  dwParam1;
		DWORD  dwParam2;
		void*  pUserData;
		void*  pReserved;
	};

	virtual int FilterEvent(void* pSender, UINT nEvent, DWORD dwParam1, DWORD dwParam2, void* pUserData);

	// Event Handlers
	void OnEncounterError(void* pSender, EventParam& param);
	void OnUpdateConvertProgress(void* pSender, EventParam& param);

	BOOL	m_bCanceled;

	IAudioEncoder*   m_pEncoder;
	IAudioDecoder*  m_pDecoder;
	IAudioMuxer*	m_pMuxer;
};

#endif /* OPUS_H_ */
