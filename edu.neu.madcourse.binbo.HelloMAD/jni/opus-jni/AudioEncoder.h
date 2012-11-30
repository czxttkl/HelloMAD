#ifndef AUDIO_ENCODER_H_
#define AUDIO_ENCODER_H_

#include "DependencyObject.h"
#include "AudioMuxer.h"
#include "Codecs.h"


class CAudioEncoder : public IAudioEncoder,
					  public CAudioMuxer
{
public:
	CAudioEncoder();
	CAudioEncoder(IDependency* pDepend);
	virtual ~CAudioEncoder();

	// IAudioEncoder
	virtual int Create(int nBitrate, int nSampleRate, int nChannels, int nApplication, const char* pszPath);
	virtual int Destroy();
	virtual int Reset();
	virtual int Encode(BYTE* pInPCM, int nSize);

protected:
	int	m_nApplication;
};

#endif
