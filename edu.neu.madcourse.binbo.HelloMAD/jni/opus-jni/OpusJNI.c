/*
 * OpusJNI.c
 *
 *  Created on: Oct 24, 2012
 *      Author: bigbug
 */

#include <jni.h>
#include <assert.h>
#include "SysConsts.h"
#include "OpusInterf.h"
#include <unistd.h>
#include <stdio.h>
//#define DEBUG

#ifndef NULL
#define NULL	0
#endif

#define LOG_TAG "Opus-jni"

#ifdef DEBUG
	#include <android/log.h>
	#define D(x...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, x)
#else
	#define D(...)  do {} while (0)
#endif

const char* CLASS_NAME = "edu/neu/madcourse/binbo/rocketrush/speech/NativeOpus";

///////////////////////////////////////////////////////////////////////////////

#define OPUS_ENCODER 0
#define OPUS_DECODER 1

static void OnEncounterError(void* pUserData, void* pReserved);
static void OnUpdateConvertProgress(void* pUserData, void* pReserved);

struct UserData
{
	JNIEnv* env;
	jobject thiz;
};

static jint _CreateEncoder(JNIEnv *env, jobject thiz,
						   jint nBitrate, jint nSampleRate, jint nChannels, jint nApplication, jstring strPath)
{
	jboolean bCopy;
	const char* pszPath = (*env)->GetStringUTFChars(env, strPath, &bCopy);
	jint nResult = CreateEncoder(nBitrate, nSampleRate, nChannels, nApplication, pszPath);
	(*env)->ReleaseStringUTFChars(env, strPath, pszPath);

	return nResult;
}

static jint _CreateDecoder(JNIEnv *env, jobject thiz, jstring strPath)
{
	jboolean bCopy;
	const char* pszPath = (*env)->GetStringUTFChars(env, strPath, &bCopy);
	jint nResult = CreateDecoder(pszPath);
	(*env)->ReleaseStringUTFChars(env, strPath, pszPath);

	return nResult;
}

static jint _DestroyEncoder(JNIEnv *env, jobject thiz)
{
	jint nResult = DestroyEncoder();

	return nResult;
}

static jint _DestroyDecoder(JNIEnv *env, jobject thiz)
{
	jint nResult = DestroyDecoder();

	return nResult;
}

static jint _Encode(JNIEnv *env, jobject thiz, jbyteArray baBuffer, jint nSize)
{
	jbyte* pBuffer = (*env)->GetShortArrayElements(env, baBuffer, 0);
	jint nResult = Encode(pBuffer, nSize);
	(*env)->ReleaseByteArrayElements(env, baBuffer, pBuffer, 0);

	return nResult;
}

static jint _Decode(JNIEnv *env, jobject thiz, jbyteArray baBuffer, jint nSize)
{
	jbyte* pBuffer = (*env)->GetByteArrayElements(env, baBuffer, 0);
	jint nResult = Decode(pBuffer, nSize);
	(*env)->ReleaseByteArrayElements(env, baBuffer, pBuffer, 0);

	return nResult;
}

static jint _ConvertFile(JNIEnv *env, jobject thiz, jstring strFrom, jstring strTo)
{
	jboolean bCopy1, bCopy2;
	const char* pszFrom = (*env)->GetStringUTFChars(env, strFrom, &bCopy1);
	const char* pszTo   = (*env)->GetStringUTFChars(env, strTo,   &bCopy2);
	struct UserData udata = { env, thiz };
	SetCallback(CALLBACK_UPDATE_CONVERT_PROGRESS, OnUpdateConvertProgress, &udata, NULL);
	jint nResult = ConvertFile(pszFrom, pszTo);
	(*env)->ReleaseStringUTFChars(env, strFrom, pszFrom);
	(*env)->ReleaseStringUTFChars(env, strTo, pszTo);

	return nResult;
}

static jint _CancelConverting(JNIEnv* env, jobject thiz)
{
	jint nResult = CancelConverting();

	return nResult;
}

static jint _SetParameter(JNIEnv *env, jobject thiz, jint nID, jobject oParam)
{
	jint nResult = JNI_OK;
//	jclass cls = (*env)->GetObjectClass(env, oParam);
//
//	if (nID == PARAM_ENCODER_SAMPLERATE) {
//		jmethodID mid = (*env)->GetMethodID(env, cls, "intValue", "()I");
//		int nSampleRate = (*env)->CallIntMethod(env, oParam, mid);
//		SetParameter(nID, &nSampleRate);
//	} else if (nID == PARAM_ENCODER_CHANNELCONFIG) {
//		jmethodID mid = (*env)->GetMethodID(env, cls, "intValue", "()I");
//		int nChannels = (*env)->CallIntMethod(env, oParam, mid);
//		SetParameter(nID, &nChannels);
//	} else if (nID == PARAM_ENCODER_BITRATE_BPS) {
//		jmethodID mid = (*env)->GetMethodID(env, cls, "intValue", "()I");
//		int nBitrateBPS = (*env)->CallIntMethod(env, oParam, mid);
//		SetParameter(nID, &nBitrateBPS);
//	} else if (nID == PARAM_DECODER_DECODEFILEPATH) {
//		const char* pszPath = (*env)->GetStringUTFChars(env, oParam, 0);
//		SetParameter(nID, pszPath);
//		(*env)->ReleaseStringUTFChars(env, oParam, pszPath);
//	}

	return nResult;
}

static jobject _GetParameter(JNIEnv *env, jobject thiz, jint nID)
{
	jobject obj;
	jclass cls = (*env)->FindClass(env, "java/lang/Integer");
	jmethodID mid = (*env)->GetStaticMethodID(env, cls, "valueOf", "(I)Ljava/lang/Integer;");

	if (nID == PARAM_DECODER_SAMPLERATE) {
		int nSampleRate = 0;
		GetParameter(nID, &nSampleRate);
		obj = (*env)->CallStaticObjectMethod(env, cls, mid, nSampleRate);
	} else if (nID == PARAM_DECODER_CHANNELS) {
		int nChannels = 0;
		GetParameter(nID, &nChannels);
		obj = (*env)->CallStaticObjectMethod(env, cls, mid, nChannels);
	} else if (nID == PARAM_DECODER_SAMPLEFORMAT) {
		int nSampleFormat = 0;
		GetParameter(nID, &nSampleFormat);
		obj = (*env)->CallStaticObjectMethod(env, cls, mid, nSampleFormat);
	} else if (nID == PARAM_DECODER_BITRATE) {
		int nBitrate = 0;
		GetParameter(nID, &nBitrate);
		obj = (*env)->CallStaticObjectMethod(env, cls, mid, nBitrate);
	}

	return obj;
}

static JavaVM *jvm;
static jmethodID method_encounter_error = NULL;
static jmethodID method_update_convert_progress = NULL;

static int NativeInit(JNIEnv* env, jclass clazz)
{
	if ((*env)->GetJavaVM(env, &jvm) < 0) {
		D("classInitNative failed");
    }

	method_encounter_error = (*env)->GetMethodID(env, clazz, "encounterError", "(I)V");
	method_update_convert_progress = (*env)->GetMethodID(env, clazz, "updateConvertProgress", "(I)V");

	return JNI_OK;
}

///////////////////////////////////////////////////////////////////////////////

static JNINativeMethod methods[] = {
	{"nativeInit", "()I", (void*)NativeInit },
	{"createEncoder", "(IIIILjava/lang/String;)I", (void*)_CreateEncoder },
	{"createDecoder", "(Ljava/lang/String;)I", (void*)_CreateDecoder },
	{"destroyEncoder", "()I", (void*)_DestroyEncoder },
	{"destroyDecoder", "()I", (void*)_DestroyDecoder },
	{"encode", "([BI)I", (void*)_Encode },
	{"decode", "([BI)I", (void*)_Decode },
	{"convertFile", "(Ljava/lang/String;Ljava/lang/String;)I", (void*)_ConvertFile },
	{"cancelConverting", "()I", (void*)_CancelConverting },
	{"setParameter", "(ILjava/lang/Object;)I", (void*)_SetParameter },
	{"getParameter", "(I)Ljava/lang/Object;", (void*)_GetParameter },
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* pVM, void* pReserved)
{
	JNIEnv* pEnv;

	if ((*pVM)->GetEnv(pVM, (void **)&pEnv, JNI_VERSION_1_4) != JNI_OK) {
		return JNI_ERR;
	}
	jclass clazz = (*pEnv)->FindClass(pEnv, CLASS_NAME);
	if (clazz == NULL) {
		D("Can't find class in JNI_OnLoad");
	    return JNI_ERR;
	}
	D("JNI_OnLoad ok");
	(*pEnv)->RegisterNatives(pEnv, clazz, methods, sizeof(methods) / sizeof(JNINativeMethod));

	return JNI_VERSION_1_4;
}

///////////////////////////////////////////////////////////////////////////////
// callbacks

static void OnUpdateConvertProgress(void* pUserData, void* pReserved)
{
	struct UserData udata = *(struct UserData*)pUserData;
	jint nProgress = (jint)pReserved;

	JNIEnv* env   = udata.env;
	jobject thiz  = udata.thiz;

	(*env)->CallIntMethod(env, thiz, method_update_convert_progress, nProgress);
}

static void OnEncounterError(void* pUserData, void* pReserved)
{
	struct UserData udata = *(struct UserData*)pUserData;
	jint nErrCode = (jint)pReserved;

	JNIEnv* env   = udata.env;
	jobject thiz  = udata.thiz;

	(*env)->CallIntMethod(env, thiz, method_encounter_error, nErrCode);
}


