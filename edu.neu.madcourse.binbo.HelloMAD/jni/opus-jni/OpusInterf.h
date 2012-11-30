/*
 * DictionaryInterf.h
 *
 *  Created on: Oct 5, 2012
 *      Author: bigbug
 */

#ifndef DICTIONARY_INTERF_H_
#define DICTIONARY_INTERF_H_

#define PARAM_ENCODER_SAMPLERATE     0L
#define PARAM_ENCODER_CHANNELCONFIG  1L
#define PARAM_ENCODER_RECORDFILEPATH 2L
#define PARAM_ENCODER_BITRATE_BPS    3L
#define PARAM_DECODER_SAMPLERATE     50L
#define PARAM_DECODER_CHANNELS		 51L
#define PARAM_DECODER_SAMPLEFORMAT   52L
#define PARAM_DECODER_BITRATE		 53L

#ifdef __cplusplus
extern "C" {
#endif

typedef int (*PCallback)(void* pUserData, void* pReserved);

int CreateEncoder(int nBitrate, int nSampleRate, int nChannels, int nApplication, const char* pszPath);
int CreateDecoder(const char* pszPath);
int DestroyEncoder();
int DestroyDecoder();
int Encode(unsigned char* pInPCM, int nSize);
int Decode(unsigned char* pOutPCM, int nSize);
int ConvertFile(const char* pszFrom, const char* pszTo);
int CancelConverting();
int SetCallback(int nType, PCallback pfnCallback, void* pUserData, void* pReserved);
int SetParameter(int nParam, void* pValue);
int GetParameter(int nParam, void* pValue);

#ifdef __cplusplus
}
#endif

#endif /* DICTIONARY_INTERF_H_ */
