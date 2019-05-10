//
// Created by Administrator on 2019-03-12.
//


#include <jni.h>
//#include "native-lib.h"
//#include "opencv2/opencv.hpp"
#include <jni.h>
#include <string>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <opencv2/opencv.hpp>

using namespace cv;
using namespace std;



extern "C"
JNIEXPORT jintArray JNICALL
Java_com_chinaso_ndktest_JNIUtils_getGrayImage(JNIEnv *env, jclass instance, jintArray  buf_, jint w,
                                               jint h) {


   jint *buf = env->GetIntArrayElements(buf_, NULL);

       if (buf == NULL)return 0;
       Mat imgData(h, w, CV_8UC4, (unsigned char *) buf);//创建Mat矩阵对象

       uchar *ptr = imgData.ptr(0);
       for (int i = 0; i < w * h; i++) {
           //计算公式：Y(亮度) = 0.299*R + 0.587*G + 0.114*B
           //对于一个int四字节，其彩色值存储方式为：BGRA,注意这是opencv对像素的存储
           int grayScale = (int) (ptr[4 * i + 2] * 0.299 + ptr[4 * i + 1] * 0.587 +
                                  ptr[4 * i + 0] * 0.114);
           ptr[4 * i + 1] = grayScale;
           ptr[4 * i + 2] = grayScale;
           ptr[4 * i + 0] = grayScale;
       }
       int size = w * h;
       jintArray result = env->NewIntArray(size);
       env->SetIntArrayRegion(result, 0, size, buf);
       env->ReleaseIntArrayElements(buf_, buf, 0);
       return result;
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_chinaso_ndktest_JNIUtils_sayHelloFromJNI(JNIEnv *env, jclass type) {

    // TODO


    return env->NewStringUTF("hello world");
}