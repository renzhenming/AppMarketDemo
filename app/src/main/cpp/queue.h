#include <android/log.h>
#include <pthread.h>
#define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"renzhenming",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"renzhenming",FORMAT,##__VA_ARGS__);
typedef struct _Queue Queue;

//分配队列元素内存的函数
typedef void* (*queue_fill_fun)();

//释放队列中元素所占用的内存,定义一个回调函数
typedef void* (*queue_free_fun)(void* elem);


/**
 * 初始化队列
 */
Queue* queue_init(int size,queue_fill_fun fill_func);

/**
 * 销毁队列
 */
void queue_free(Queue *queue,queue_free_fun free_fun);

/**
 * 获取下一个索引位置
 */
int queue_get_next(Queue *queue,int current);

/**
 * 入队
 * 当有元素需要存入队列的时候，获取到队列中下一个位置的指针，将这个指针指向将要入队的元素
 */
void *queue_push(Queue *queue,pthread_mutex_t *mutex,pthread_cond_t *cond);

/**
 * 出队
 * 当要读取下一个元素的时候，获取到队列中下一个位置的指针，将这个指针返回
 */
void* queue_pop(Queue *queue,pthread_mutex_t *mutex, pthread_cond_t *cond);
