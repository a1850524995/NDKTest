LOCAL_PATH := $(call my-dir)


include $(CLEAR_VARS)
OpenCV_INSTALL_MODULES := on
OPENCV_CAMERA_MODULES := on
OPENCV_LIB_TYPE := STATIC
include E:/txs/Android/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE := hello_dlib
LOCAL_C_INCLUDES +=  E:\txs\Android\OpenCV-android-sdk\sdk\native\jni\include
LOCAL_SRC_FILES += \
           jni_imageutils.cpp \
	       jni_pedestrian_det.cpp \
	       jni_face_det.cpp

 LOCAL_LDLIBS += -lm -llog -ldl -lz -ljnigraphics
 LOCAL_CPPFLAGS += -fexceptions -frtti -std=c++11

 LOCAL_STATIC_LIBRARIES += dlib \
                           jni_common
 ifeq ($(TARGET_ARCH_ABI), armeabi-v7a)
     LOCAL_ARM_MODE := arm
 	 LOCAL_ARM_NEON := true
 endif

 include $(BUILD_SHARED_LIBRARY)
