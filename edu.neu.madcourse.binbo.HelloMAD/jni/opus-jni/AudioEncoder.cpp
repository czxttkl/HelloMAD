#include "AudioEncoder.h"


CAudioEncoder::CAudioEncoder(IDependency* pDepend)
	: CAudioMuxer(pDepend)
{
}

CAudioEncoder::~CAudioEncoder()
{
	DestroyOutputContext();
}

int CAudioEncoder::Reset()
{
	return CAudioMuxer::Reset();
}

int CAudioEncoder::Create(int nBitrate, int nSampleRate, int nChannels, int nApplication, const char* pszPath)
{
	int nResult = CreateOutputContext(pszPath, nBitrate, nSampleRate, nChannels);
	if (nResult == S_OK) {
		WriteFileHeader();
	}

	return nResult;
}

int CAudioEncoder::Destroy()
{
	int nResult = S_OK;

	WriteFileTrailer();
	nResult = DestroyOutputContext();

	return S_OK;
}

int CAudioEncoder::Encode(BYTE* pInPCM, int nSize)
{
	int nResult = WriteFileData(pInPCM, nSize);

	return nResult;
}


