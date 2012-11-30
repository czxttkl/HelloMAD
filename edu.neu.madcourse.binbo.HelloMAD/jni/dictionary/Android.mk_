LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := dictionary
LOCAL_ARM_MODE := arm
# for logging
LOCAL_LDLIBS += -llog
# for native asset manager
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -lm -landroid -Wl
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../huffman
# show the dependance
LOCAL_SHARED_LIBRARIES += libhuffman

LOCAL_SRC_FILES += \
	DictionaryJNI.c \
	DictionaryInterf.cpp \
	Dictionary.cpp \
	Utinities/BaseTypes.cpp \
	Utinities/Error.cpp \
    Utinities/Event.cpp \
    Utinities/Lock.cpp \
    Utinities/QTime.cpp \
    Utinities/QString.cpp \
    Utinities/Thread.cpp \

include $(BUILD_SHARED_LIBRARY)

