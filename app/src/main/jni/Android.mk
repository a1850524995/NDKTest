LOCAL_PATH := $(call my-dir)
SUB_MK_FILES := $(call all-subdir-makefiles)

include $(CLEAR_VARS)
LOCAL_MODULE := dlib
LOCAL_C_INCLUDES := $(LOCAL_PATH)/dlib
LOCAL_SRC_FILES += \
                ../$(LOCAL_PATH)/dlib/threads/threads_kernel_shared.cpp \
                ../$(LOCAL_PATH)/dlib/entropy_decoder/entropy_decoder_kernel_2.cpp \
                ../$(LOCAL_PATH)/dlib/base64/base64_kernel_1.cpp \
                ../$(LOCAL_PATH)/dlib/threads/threads_kernel_1.cpp \
                ../$(LOCAL_PATH)/dlib/threads/threads_kernel_2.cpp
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_C_INCLUDES)
include $(BUILD_STATIC_LIBRARY)
include $(SUB_MK_FILES)