//
// Created by renzhenming on 2017/9/22.
//
#include "jni_lib.h"


#define JNI_AN_MainActivity "com/example/renzhenming/appmarket/TestJniActivity"
#define METHOD_NUM 1
JNINativeMethod g_nativeMethod[METHOD_NUM]={
        {"GetStrFromJNI","()Ljava/lang/String;",(void*)GetStrFromJNI}
};
/* * 被虚拟机自动调用 */
jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv *env;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK)
        return JNI_ERR;
    jclass jClass = env->FindClass(JNI_AN_MainActivity);
    env->RegisterNatives(jClass,g_nativeMethod,METHOD_NUM);
    env->DeleteLocalRef(jClass);
    return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM* vm, void* reserved)
{
    JNIEnv *env;
    int nJNIVersionOK = vm->GetEnv((void **)&env, JNI_VERSION_1_6) ;
    jclass jClass = env->FindClass(JNI_AN_MainActivity);
    env->UnregisterNatives(jClass);
    env->DeleteLocalRef(jClass);
}

/**
 * 光有这些是不够的，java虚拟机是无法直接找到GetStrFromJNI这个函数的，
 * 需要通过调用JNI_OnLoad函数，实现JNI函数和java native声明的对接,所以就有了上边的代码
 * @param env
 * @param callObj
 * @return
 */
jstring GetStrFromJNI(JNIEnv* env,jobject callObj)
{
    return env->NewStringUTF("String From Jni With c++");
}

