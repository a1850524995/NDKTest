LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
OpenCV_INSTALL_MODULES := on
OPENCV_CAMERA_MODULES := on
OPENCV_LIB_TYPE := STATIC
include E:/txs/Android/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk
LOCAL_MODULE := jni_common
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/..
LOCAL_C_INCLUDES :=  $(LOCAL_PATH)/..
LOCAL_C_INCLUDES +=  E:\txs\Android\OpenCV-android-sdk\sdk\native\jni\include


all_cpp_files_recursively = \
 $(eval src_files = $(wildcard $1/*.cpp)) \
 $(eval src_files = $(src_files:$(LOCAL_PATH)/%=%))$(src_files) \
 $(eval item_all = $(wildcard $1/*)) \
 $(foreach item, $(item_all) $(),\
  $(eval item := $(item:%.cpp=%)) \
  $(call all_cpp_files_recursively, $(item))\
 )

LOCAL_SRC_FILES  := $(call all_cpp_files_recursively, $(LOCAL_PATH))

include $(BUILD_STATIC_LIBRARY)