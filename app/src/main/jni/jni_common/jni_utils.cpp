/*
 *     Author : Darren
 * Created on : 06/20 2016
 *
 * Copyright (c) 2016 Darren. All rights reserved.
 */
#include <cstring>


#include <sstream>
#include <unistd.h>
#include "jni_primitives.h"
#include "jni_utils.h"
// Java Integer/Float

JNI_Integer* g_pJniInteger;
JNI_Float* g_pJniFloat;
JNI_PointF* g_pJniPointF;

JNI_VisionDetRet* g_pJNI_VisionDetRet;

JavaVM* g_javaVM = NULL;

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {

  g_javaVM = vm;
  JNIEnv* env;
  vm->GetEnv((void**)&env, JNI_VERSION_1_6);

  // Java Integer/Float
  g_pJniInteger = new JNI_Integer(env);
  g_pJniFloat = new JNI_Float(env);
  g_pJniPointF = new JNI_PointF(env);
  g_pJNI_VisionDetRet = new JNI_VisionDetRet(env);


  return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {

  g_javaVM = NULL;

  // Java Integer/Float
  delete g_pJniInteger;
  delete g_pJniFloat;
  delete g_pJniPointF;
  delete g_pJNI_VisionDetRet;

}

namespace jniutils {

char* convertJStrToCStr(JNIEnv* env, jstring lString) {
  const char* lStringTmp;
  char* pstring;

  lStringTmp = env->GetStringUTFChars(lString, NULL);
  if (lStringTmp == NULL)
    return NULL;

  jsize lStringLength = env->GetStringUTFLength(lString);

  pstring = (char*)malloc(sizeof(char) * (lStringLength + 1));
  strcpy(pstring, lStringTmp);

  env->ReleaseStringUTFChars(lString, lStringTmp);

  return pstring;
}

std::string convertJStrToString(JNIEnv* env, jstring lString) {
  const char* lStringTmp;
  std::string str;

  lStringTmp = env->GetStringUTFChars(lString, NULL);
  if (lStringTmp == NULL)
    return NULL;

  str = lStringTmp;

  env->ReleaseStringUTFChars(lString, lStringTmp);

  return str;
}

JNIEnv* vm2env(JavaVM* vm) {
  JNIEnv* env = NULL;
  if (vm->GetEnv((void**)&env, JNI_VERSION_1_6) != JNI_OK) {

    env = NULL;
  }
  if (env == NULL) {

  }
  return env;
}
}
