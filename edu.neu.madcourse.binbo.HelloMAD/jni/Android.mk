LOCAL_PATH := $(call my-dir)

LOCAL_CFLAGS += -O3 -D_FILE_OFFSET_BITS=64 -D_LARGEFILE_SOURCE

include $(CLEAR_VARS)
include $(call all-makefiles-under, $(LOCAL_PATH))
