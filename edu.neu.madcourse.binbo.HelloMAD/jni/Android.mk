LOCAL_PATH := $(call my-dir)

LOCAL_CFLAGS += -O3 -D_FILE_OFFSET_BITS=64 -D_LARGEFILE_SOURCE

ARMV6_OPTION := -DHAVE_NEON=0 -march=armv6 -mfpu=vfp -mfloat-abi=softfp

ARMV7_OPTION := -DHAVE_NEON=0 -march=armv7-a -mfpu=vfp -mfloat-abi=softfp

ARMV7NEON_OPTION := -DHAVE_NEON=1 -march=armv7-a -mfpu=neon -mfloat-abi=softfp \
					-ftree-vectorize -fvect-cost-model -pipe -mvectorize-with-neon-quad

X86ATOM_OPTION := -march=atom -mfpmath=sse -ffast-math

include $(CLEAR_VARS)
include $(call all-makefiles-under, $(LOCAL_PATH))
