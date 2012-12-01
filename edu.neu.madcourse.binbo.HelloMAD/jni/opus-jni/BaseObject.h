#ifndef BASE_OBJECT_H_
#define BASE_OBJECT_H_

#include "Config.h"
#include "SysConsts.h"

#ifdef DEBUG
    #include <assert.h>
    #ifdef ANDROID
        #include <android/log.h>
		#define LOG_TAG "opus-driver"
    #endif
#endif

class CBaseObject
{
#ifdef DEBUG
    #ifdef ANDROID
        #define Log(format, args...) \
        { \
            __android_log_print(ANDROID_LOG_INFO, LOG_TAG, format, ##args);\
        }
    #else
        #ifdef LOG_TO_FILE
            #define Log(format, args...) \
            { \
                extern std::string strPathLog; \
                FILE* fp = fopen(strPathLog.c_str(), "a+"); \
                fprintf(fp, format, ##args); \
                fclose(fp); \
            }
        #else
            #define Log(format, args...) \
            { \
                printf(format, ##args); \
            }
        #endif
    #endif \

    #define AssertValid(condition) \
    { \
        assert(condition); \
    }    
#else
    #define Log(format, args...)
    #define AssertValid(bCondition)
#endif
};

#endif
