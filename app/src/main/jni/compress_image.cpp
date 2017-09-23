//
// Created by renzhenming on 2017/9/22.
//
#include "compress_image.h"

#include <android/bitmap.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>
#include <setjmp.h>

#define JNI_AN_MainActivity "com/example/renzhenming/appmarket/TestJniActivity"
#define METHOD_NUM 1
typedef uint8_t BYTE;

// log打印
#define LOG_TAG "jni"
#define LOGW(...)  __android_log_write(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
//统一编译方式
extern "C" {
    #include "jpeg/jpeglib.h"
    #include "jpeg/cdjpeg.h"
    #include "jpeg/jversion.h"
    #include "jpeg/jconfig.h"
}
#define true 1
#define false 0
/**
 * 结构体，分别是java层的函数名称，签名，对应的函数指针
 * 定义批量注册的数组，是注册的关键部分
 * "()Ljava/lang/String;"是函数的签名，可以通过javah获取,用字符串描述了函数的参数和返回值
 * 签名不正确无法运行
 * 函数指针，指向native函数。前面都要接 (void *)
 */
JNINativeMethod g_nativeMethod[METHOD_NUM]={
        /*{"GetStrFromJNI","()Ljava/lang/String;",(void*)GetStrFromJNI}*/
        {"compressBitmap","(Landroid/graphics/Bitmap;ILjava/lang/String;)Ljava/lang/String;",(void*)compressBitmap}
};


/* * 被虚拟机自动调用
 *  JNI_OnLoad()函数。
它的用途有二：
 * 告诉VM此C组件使用那一个JNI版本。
 * 如果你的*.so档没有提供JNI_OnLoad()函数，VM会默认该*.so档是使用最老的JNI 1.1版本。
 * 由于新版的JNI做了许多扩充，如果需要使用JNI的新版功能，
 * 例如JNI 1.4的java.nio.ByteBuffer,就必须藉由JNI_OnLoad()函数来告知VM。
 * 由于VM执行到System.loadLibrary()函数时，就会立即先呼叫JNI_OnLoad()，
 * 所以C组件的开发者可以藉由JNI_OnLoad()来进行C组件内的初期值之设定(Initialization) 。
 * 其实Android中的so文件就像是Windows下的DLL一样，JNI_OnLoad和JNI_OnUnLoad函数
 * 就像是DLL中的PROCESS ATTATCH和DEATTATCH的过程一样，可以同样做一些初始化和反初始化的动作。
 * */
jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    //注册时在JNIEnv中实现的，所以必须首先获取它
    JNIEnv *env;

    //从JavaVM获取JNIEnv，一般使用1.4的版本
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK)
        return JNI_ERR;

    /*
     * 如果要注册，只需要两步，
     *    首先FindClass，
     *    然后RegisterNatives
     *
     *    这里可以找到要注册的类，前提是这个类已经加载到java虚拟机中。
     *    这里说明，动态库和有native方法的类之间，没有任何对应关系。
     */
    jclass jClass = env->FindClass(JNI_AN_MainActivity);
    if (jClass == NULL){
        return -1;
    }

    /*
    * 这里就是关键了，把本地函数和一个java类方法关联起来。
    * 不管之前是否关联过，一律把之前的替换掉！
    */
    if (env->RegisterNatives(jClass,g_nativeMethod,METHOD_NUM)!=JNI_OK){
        return -1;
    };
    env->DeleteLocalRef(jClass);

    //一定要返回版本号，否则会出错。
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

/*
typedef struct my_error_mgr *my_error_ptr;

// error 结构体
char *error;
struct my_error_mgr {
    struct jpeg_error_mgr pub;
    jmp_buf setjmp_buffer;
};
METHODDEF(void)
my_error_exit(j_common_ptr cinfo) {
    my_error_ptr myerr = (my_error_ptr) cinfo->err;
    (*cinfo->err->output_message)(cinfo);
    error = (char *) myerr->pub.jpeg_message_table[myerr->pub.msg_code];
    LOGE("jpeg_message_table[%d]:%s", myerr->pub.msg_code,
         myerr->pub.jpeg_message_table[myerr->pub.msg_code]);
    longjmp(myerr->setjmp_buffer, 1);
}
*/
/**
 * 光有这些是不够的，java虚拟机是无法直接找到GetStrFromJNI这个函数的，
 * 需要通过调用JNI_OnLoad函数，实现JNI函数和java native声明的对接,所以就有了上边的代码
 * @param env
 * @param callObj
 * @return
 * 函数名字可以随便取，不过参数一定要和javah生成的函数的参数一致，包括返回值
 *//*


int generateJPEG(BYTE *data, int w, int h, int quality,
                 const char *outfilename, jboolean optimize) {

    // 结构体相当于Java类
    struct jpeg_compress_struct jcs;

    //当读完整个文件的时候就会回调my_error_exit这个退出方法。
    struct my_error_mgr jem;
    jcs.err = jpeg_std_error(&jem.pub);
    jem.pub.error_exit = my_error_exit;
    // setjmp是一个系统级函数，是一个回调。
    if (setjmp(jem.setjmp_buffer)) {
        return 0;
    }

    //初始化jsc结构体
    jpeg_create_compress(&jcs);
    //打开输出文件 wb 可写  rb 可读
    FILE *f = fopen(outfilename, "wb");
    if (f == NULL) {
        return 0;
    }
    //设置结构体的文件路径，以及宽高
    jpeg_stdio_dest(&jcs, f);
    jcs.image_width = w;
    jcs.image_height = h;

    // */
/* TRUE=arithmetic coding, FALSE=Huffman *//*

    jcs.arith_code = false;
    int nComponent = 3;
    */
/* 颜色的组成 rgb，三个 # of color components in input image *//*

    jcs.input_components = nComponent;
    //设置颜色空间为rgb
    jcs.in_color_space = JCS_RGB;
    //*/
/* Default parameter setup for compression *//*

    jpeg_set_defaults(&jcs);
    //是否采用哈弗曼
    jcs.optimize_coding = optimize;
    //设置质量
    jpeg_set_quality(&jcs, quality, true);
    //开始压缩
    jpeg_start_compress(&jcs, TRUE);

    JSAMPROW row_pointer[1];
    int row_stride;
    row_stride = jcs.image_width * nComponent;
    while (jcs.next_scanline < jcs.image_height) {
        //得到一行的首地址
        row_pointer[0] = &data[jcs.next_scanline * row_stride];
        jpeg_write_scanlines(&jcs, row_pointer, 1);
    }
    // 压缩结束
    jpeg_finish_compress(&jcs);
    // 销毁回收内存
    jpeg_destroy_compress(&jcs);
    //关闭文件
    fclose(f);
    return 1;
}
*/

jstring compressBitmap(JNIEnv *env, jclass thiz, jobject bitmap, int quality, jstring fileNameStr)
{
    return env->NewStringUTF("C层内容返回");
    /*//解析RGB

    //获取bitmap信息
    AndroidBitmapInfo* info;
    //java调用完返回的往往是对象，将数据存储在对象中 c往往是将数据存入传入的一个参数中，所以执行完成后
    //bitmap信息就存到了info中
    AndroidBitmap_getInfo(env,bitmap,info);  //<android/bitmap.h>

    //获取bitmap信息
    int bitmap_height = info->height;
    int bitmap_width = info->width;
    int bitmap_format = info->format;

    if(bitmap_format != ANDROID_BITMAP_FORMAT_RGBA_8888){
        return -1;
    }
    //把bitmap解析到数组中，数组中保存的是rgb -> YCbCr

    //锁定画布
    BYTE* pixel_color;
    AndroidBitmap_lockPixels(env,bitmap,(void**)&pixel_color);

    //解析数据，定义变量
    BYTE *data;
    BYTE r,g,b;
    //申请一块内存 = 宽*高*3  malloc stdlib.h中
    data = (BYTE *)malloc(bitmap_width*bitmap_height*3);
    //数组指针指向的是数组首地址，因为这块内存要释放所以先保存一下
    BYTE *tempData;
    tempData = data;

    int i;
    int j;
    int color;
    for (i = 0; i < bitmap_width; ++i) {
        for (j = 0; j < bitmap_height; ++j) {
            //获取二维数组的每一个像素信息首地址
            color =  *((int *)pixel_color);
            //把rgb取出来
            r = (color & 0x00FF0000) >> 16;
            g = (color & 0x0000FF00) >> 8;
            b = (color & 0x000000FF);

            //保存到data中
            *data = b;
            *(data+1)=g;
            *(data+2)=r;
            //在一个循环中走过一次之后，将data的地址右移3个位置，继续下次循环
            data = data+3;
            //一个像素点包括argb四个值，每+4一次就是取下一个像素点
            pixel_color += 4;

        }
    }
    //解锁画布
    AndroidBitmap_unlockPixels(env,bitmap);

    //还差一个参数 jstring -> char*
    char * file_name = (char*)env->GetStringUTFChars(fileNameStr,NULL);

    //调用第三方提供好的方法
    int result = generateJPEG(tempData,bitmap_width,bitmap_height,quality,file_name, true);
    //回收内存
    free(tempData);
    env->ReleaseStringUTFChars(fileNameStr,file_name);

    //返回结果
    if (result == 0)
    {
        return -1;
    }
    return 1;*/
}

