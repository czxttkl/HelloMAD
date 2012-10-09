/*
 * DictionaryJNI.c
 *
 *  Created on: Oct 5, 2012
 *      Author: bigbug
 */

#include <jni.h>
#include <assert.h>
#include "DictionaryInterf.h"

// for native asset manager
#include <sys/types.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

#define DEBUG

#ifndef NULL
#define NULL	0
#endif

#define LOG_TAG "dictionary"

#ifdef DEBUG
	#include <android/log.h>
	#define D(x...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, x)
#else
	#define D(...)  do {} while (0)
#endif

const char* CLASS_NAME = "edu/neu/madcourse/binbo/NativeDictionary";


static jint create(JNIEnv *env, jobject thiz)
{
	D("create");
	jint nResult = Create();

	return nResult;
}

static jint destroy(JNIEnv *env, jobject thiz)
{
	jint nResult = Destroy();

	return nResult;
}

static jint load(JNIEnv *env, jobject thiz, jstring path, jstring name, jobject assetManager)
{
	D("load");
	int nResult = JNI_OK;
	jboolean bCopy;
	const char* pszPath = (*env)->GetStringUTFChars(env, path, &bCopy);
	const char* pszName = (*env)->GetStringUTFChars(env, name, &bCopy);

	// use asset manager to open asset by filename
	AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
	assert(NULL != mgr);
	AAsset* asset = AAssetManager_open(mgr, pszPath, AASSET_MODE_UNKNOWN);

	// the asset might not be found
	if (NULL == asset) {
		return JNI_ERR;
	}

	// open asset as file descriptor
	off_t start, length;
	int fd = AAsset_openFileDescriptor(asset, &start, &length);
	assert(0 <= fd);
	AAsset_close(asset);
	//D("fd = %d, start = %d, length = %d", fd, start, length);

	LOADINFO li;
	li.nFileDescriptor = fd;
	li.nOffsetInAPK    = start;
	li.pDictionaryName = pszName;
	nResult = Load(&li);

	// I have the file descriptor closed in Load, so I won't call fdclose here.
	(*env)->ReleaseStringUTFChars(env, path, pszPath);
	(*env)->ReleaseStringUTFChars(env, name, pszName);

	return nResult;
}

static jint unload(JNIEnv *env, jobject thiz, jstring name)
{
	D("unload");
    int nResult = JNI_OK;
	jboolean bCopy;
	const char* pszName = (*env)->GetStringUTFChars(env, name, &bCopy);

	nResult = Unload(pszName);
	(*env)->ReleaseStringUTFChars(env, name, pszName);

	return nResult;
}

static jboolean isLoaded(JNIEnv *env, jobject thiz, jstring name)
{
	D("isLoaded");
	jboolean bResult = JNI_TRUE;
	jboolean bCopy;
	const char* pszName = (*env)->GetStringUTFChars(env, name, &bCopy);

	bResult = IsLoaded(pszName);
	(*env)->ReleaseStringUTFChars(env, name, pszName);

	return bResult;
}

static jboolean lookupWord(JNIEnv *env, jobject thiz, jstring word)
{
	D("lookupword");
	jboolean bResult = JNI_TRUE;
	jboolean bCopy;
	const char* pszWord = (*env)->GetStringUTFChars(env, word, &bCopy);

	bResult = LookupWord(pszWord);
	(*env)->ReleaseStringUTFChars(env, word, pszWord);

	return bResult;
}

static JNINativeMethod methods[] = {
	{"create", "()I", (void*)create },
	{"destroy", "()I", (void*)destroy },
	{"load", "(Ljava/lang/String;Ljava/lang/String;Landroid/content/res/AssetManager;)I", (void*)load },
	{"unload", "(Ljava/lang/String;)I", (void*)unload },
	{"isLoaded", "(Ljava/lang/String;)Z", (void*)isLoaded },
	{"lookupWord", "(Ljava/lang/String;)Z", (void*)lookupWord }
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
	JNIEnv *env;

	if ((*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_4) != JNI_OK) {
		return JNI_ERR;
	}
	jclass clazz = (*env)->FindClass(env, CLASS_NAME);
	if (clazz == NULL) {
		D("Can't find the class");
	    return JNI_ERR;
	}
	D("OnLoad ok");
	(*env)->RegisterNatives(env, clazz, methods,
			sizeof(methods) / sizeof(JNINativeMethod));

	return JNI_VERSION_1_4;
}
