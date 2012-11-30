LOCAL_PATH := $(call my-dir)

ifeq ($(TARGET_ARCH_ABI), armeabi)

	include $(CLEAR_VARS)
	LOCAL_MODULE := libhuffman
	LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libhuffman.so
	include $(PREBUILT_SHARED_LIBRARY)
	
	include $(CLEAR_VARS)
	LOCAL_MODULE := libdictionary
	LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libdictionary.so
	include $(PREBUILT_SHARED_LIBRARY)
	
	include $(CLEAR_VARS)
	LOCAL_MODULE := libffmpeg-6
	LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libffmpeg-6.so
	include $(PREBUILT_SHARED_LIBRARY)
	
	include $(CLEAR_VARS)
	LOCAL_MODULE := libopus-driver-6
	LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libopus-driver-6.so
	include $(PREBUILT_SHARED_LIBRARY)
	
else ifeq ($(TARGET_ARCH_ABI), armeabi-v7a)
	
	include $(CLEAR_VARS)
	LOCAL_MODULE := libhuffman
	LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libhuffman.so
	include $(PREBUILT_SHARED_LIBRARY)
	
	include $(CLEAR_VARS)
	LOCAL_MODULE := libdictionary
	LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libdictionary.so
	include $(PREBUILT_SHARED_LIBRARY)
	
	include $(CLEAR_VARS)
	LOCAL_MODULE := libffmpeg-7
	LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libffmpeg-7.so
	include $(PREBUILT_SHARED_LIBRARY)

	include $(CLEAR_VARS)
	LOCAL_MODULE := libffmpeg-7neon
	LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libffmpeg-7neon.so
	include $(PREBUILT_SHARED_LIBRARY)
	
else ifeq ($(TARGET_ARCH_ABI), x86)

	include $(CLEAR_VARS)
	LOCAL_MODULE := libffmpeg-x86atom
	LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libffmpeg-x86atom.so
	include $(PREBUILT_SHARED_LIBRARY)
	
endif





