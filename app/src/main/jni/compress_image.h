//
// Created by renzhenming on 2017/9/22.
//

#ifndef APPMARKETDEMO_JNI_LIB_H
#define APPMARKETDEMO_JNI_LIB_H

#include "jni.h"
//jstring GetStrFromJNI(JNIEnv* env,jobject callObj);
jstring compressBitmap(JNIEnv*,jclass, jobject , int , jstring);
#endif //APPMARKETDEMO_JNI_LIB_H
