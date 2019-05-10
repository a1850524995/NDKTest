/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_chinaso_ndktest_DetectionBasedTracker */

#ifndef _Included_com_chinaso_ndktest_DetectionBasedTracker
#define _Included_com_chinaso_ndktest_DetectionBasedTracker
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_chinaso_ndktest_DetectionBasedTracker
 * Method:    nativeDestroyObject
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_chinaso_ndktest_DetectionBasedTracker_nativeDestroyObject
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_chinaso_ndktest_DetectionBasedTracker
 * Method:    nativeDetect
 * Signature: (JJJ)V
 */
JNIEXPORT void JNICALL Java_com_chinaso_ndktest_DetectionBasedTracker_nativeDetect
  (JNIEnv *, jclass, jlong, jlong, jlong);

/*
 * Class:     com_chinaso_ndktest_DetectionBasedTracker
 * Method:    nativeStop
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_chinaso_ndktest_DetectionBasedTracker_nativeStop
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_chinaso_ndktest_DetectionBasedTracker
 * Method:    nativeStart
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_chinaso_ndktest_DetectionBasedTracker_nativeStart
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_chinaso_ndktest_DetectionBasedTracker
 * Method:    nativeCreateObject
 * Signature: (Ljava/lang/String;I)J
 */
JNIEXPORT jlong JNICALL Java_com_chinaso_ndktest_DetectionBasedTracker_nativeCreateObject
  (JNIEnv *, jclass, jstring, jint);

#ifdef __cplusplus
}
#endif
#endif
extern "C"
JNIEXPORT void JNICALL
Java_com_chinaso_ndktest_DetectionBasedTracker_nativeSetFaceSize(JNIEnv *env, jclass type,
                                                                 jlong nativeObj, jint size) ;
extern "C"
JNIEXPORT void JNICALL
Java_com_chinaso_ndktest_dlib_FaceDet_jniNativeClassInit(JNIEnv *env, jclass type) ;

extern "C" JNIEXPORT jint JNICALL Java_com_chinaso_ndktest_dlib_FaceDet_jniDeInit(JNIEnv *env, jobject instance) ;
extern "C" JNIEXPORT jint JNICALL Java_com_chinaso_ndktest_dlib_FaceDet_jniInit(JNIEnv *env, jobject instance) ;extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_chinaso_ndktest_dlib_FaceDet_jniBitmapDet(JNIEnv *env, jobject instance, jobject bitmap) ;