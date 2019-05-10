

cmake_minimum_required(VERSION 3.4.1)
#set指定系统变量
set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
#set指令自定义变量
set(JNI_DETECTION_SRC ${CMAKE_SOURCE_DIR}/jni_detections )
set(JNI_COMMON_SRC ${CMAKE_SOURCE_DIR}/jni_common)
set(DLIB_DIR ${CMAKE_SOURCE_DIR}/dlib)
set(pathToOpenCv E:/txs/Android/OpenCV-android-sdk)

SET(CMAKE_BUILD_TYPE "Release")

# 设置dlib
#include(${CMAKE_SOURCE_DIR}/dlib/cmake)

#配置opencv的头文件
include_directories(${pathToOpenCv}/sdk/native/jni/include)
include_directories(${CMAKE_SOURCE_DIR}/hello_dlib_header)
include_directories(${DLIB_DIR}/threads)
include_directories( ${DLIB_DIR}/entropy_decoder)
include_directories(${DLIB_DIR}/base64
        )




#引入lib_opencv包，这里只是自己取了个名字
add_library(lib_opencv STATIC IMPORTED)
#设置lib_opencv的指向
set_target_properties(lib_opencv
        PROPERTIES
        IMPORTED_LOCATION E:/test/NDKTest/app/src/main/jniLibs/armeabi-v7a/${}/libopencv_java3.so#这里替换成你自己的在项目中jniLibs中的so库绝对路径地址
        )


AUX_SOURCE_DIRECTORY(${CMAKE_SOURCE_DIR}/jni_common SRC_COM)
AUX_SOURCE_DIRECTORY( ${CMAKE_SOURCE_DIR}/jni_detections SRC_DET )


add_library( # Sets the name of the library.
        hello_dlib
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        ${SRC_COM}
        ${SRC_DET}
        ${DLIB_DIR}/threads/threads_kernel_shared.cpp
        ${DLIB_DIR}/entropy_decoder/entropy_decoder_kernel_2.cpp
        ${DLIB_DIR}/base64/base64_kernel_1.cpp
        ${DLIB_DIR}/threads/threads_kernel_1.cpp
        ${DLIB_DIR}/threads/threads_kernel_2.cpp
        JNIHello.cpp)


#[[add_library( # Sets the name of the library.
        detection_based_tracker
        # Sets the library as a shared library.
        STATIC
        # Provides a relative path to your source file(s).
        ${CMAKE_SOURCE_DIR}/com_chinaso_ndktest_DetectionBasedTracker.cpp
        ${CMAKE_SOURCE_DIR}/JNIHello.cpp )]]

#加入系统log库
find_library( # Defines the name of the path variable that stores the
        # location of the NDK library.
        log-lib
        # Specifies the name of the NDK library that
        # CMake needs to locate.
        log )
# 连接的时候连接哪些库
target_link_libraries( # Specifies the target library.

        hello_dlib

        # Links the log library to the target library.
        ${log-lib}
        lib_opencv
        jnigraphics
        #[[detection_based_tracker]]
       )

