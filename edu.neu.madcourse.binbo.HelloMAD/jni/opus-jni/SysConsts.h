#ifndef SYS_CONSTS_H_
#define SYS_CONSTS_H_

#define TRUE	1
#define FALSE	0

#define S_OK                 0
#define E_FAIL              -1  
#define E_NOIMPL            -2 
#define E_OUTOFMEMORY       -3   
#define E_IO                -4
#define E_EOF				-5
#define E_HANDLED			-6

#define PATH_LENGTH			256

#ifndef OPUS_CODEC
#define OPUS_CODEC
#define OPUS_ENCODER 0L
#define OPUS_DECODER 1L
#endif

#define CALLBACK_ENCOUNTER_ERROR			0
#define CALLBACK_UPDATE_CONVERT_PROGRESS	1

#define PARAM_ENCODER_SAMPLERATE     0L
#define PARAM_ENCODER_CHANNELCONFIG  1L
#define PARAM_ENCODER_RECORDFILEPATH 2L
#define PARAM_ENCODER_BITRATE_BPS    3L

#define PARAM_DECODER_SAMPLERATE     50L
#define PARAM_DECODER_CHANNELS		 51L
#define PARAM_DECODER_SAMPLEFORMAT   52L
#define PARAM_DECODER_BITRATE		 53L

const int EVENT_ENCOUNTER_ERROR			= 0;
const int EVENT_UPDATE_CONVERT_PROGRESS = 1;

#endif
