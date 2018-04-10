#include "queue.h"
#include <pthread.h>
#include <malloc.h>

/**
 * 队列结构体，用于存放AVPacket指针
 * 这里使用生产者消费真模式来使用队列，至少需要两个队列实例，分别用于存放音频AVPacket和视频AVPacket
 *
 * 1.生产者：read_stream线程负责不断的读取视频文件中的AVPacket，分别放入两个队列中
 * 2.消费者：
 *  a:视频解码，从视频AVPacket Queue中获取元素，解码，绘制
 *  b:音频解码，从音频AVPacket Queue中获取元素，解码，播放
 */
struct _Queue{
	//长度
	int size;

	//任意类型的指针数组。这里每一个元素都是AVPacket指针，共有size个
	//AVPacket **packets;
	void **tab;

	//push或者pop元素时按照先后顺序，依次进行
	int next_to_write;
	int next_to_read;

	int *ready;
};

/**
 * 初始化队列
 */
Queue* queue_init(int size,queue_fill_fun fill_func){
	//开辟空间存放队列
	Queue *queue = (Queue *)malloc(sizeof(Queue));
	//初始化队列的值
	queue->size = size;
	queue->next_to_write = 0;
	queue->next_to_read = 0;
	//为队列中的数组tab申请开辟空间
	//TODO *queue->tab和queue->tab区别何在
	queue->tab = malloc(sizeof(*queue->tab)*size);

	//为数组中的每一个元素开辟空间
	int i;
	for(i = 0 ; i < size ; i ++){
		queue->tab[i]=fill_func();
	}

	return queue;
}

/**
 * 销毁队列
 */
void queue_free(Queue *queue,queue_free_fun free_fun){
	int i;
	for(i = 0 ; i < queue->size;i++){
		//通过使用回调函数销毁队列中的元素
		free_fun((void *)queue->tab[i]);
	}
	free(queue->tab);
	free(queue);
}

/**
 * 获取下一个索引位置
 */
int queue_get_next(Queue *queue,int current){
	//%是为了当位置到达末尾时，重新从第一个开始返回
	return (current+1) % queue->size;
}

/**
 * 入队
 * 当有元素需要存入队列的时候，获取到队列中下一个位置的指针，将这个指针指向将要入队的元素
 */
void *queue_push(Queue *queue,pthread_mutex_t *mutex,pthread_cond_t *cond){
	int current = queue->next_to_write;
	int next_to_write;
	for(;;){
		//下一个要读的位置等于下一个要写的，等待写入完成再读，不等于就继续，不等于就继续
		next_to_write = queue_get_next(queue,current);
		if(queue->next_to_read != next_to_write){
			break;
		}

		//相等，就等待
		//int pthread_cond_wait(pthread_cond_t *cond, pthread_mutex_t *mutex);
		pthread_cond_wait(cond,mutex);
	}

	queue->next_to_write = queue_get_next(queue,current);
	LOGI("queue_push queue:%#x, %d",queue,current);

	//通知所有等待条件变量的线程int pthread_cond_broadcast(pthread_cond_t *cond);
	pthread_cond_broadcast(cond);
	return queue->tab[current];
}

/**
 * 出队
 * 当要读取下一个元素的时候，获取到队列中下一个位置的指针，将这个指针返回
 */
void* queue_pop(Queue *queue,pthread_mutex_t *mutex, pthread_cond_t *cond){
	int current = queue->next_to_read;

	for(;;){
		if(queue->next_to_read != queue->next_to_write){
			break;
		}
		pthread_cond_wait(cond,mutex);
	}
	queue->next_to_read = queue_get_next(queue,current);
	LOGI("queue_pop queue:%#x, %d",queue,current);

	pthread_cond_broadcast(cond);
	return queue->tab[current];
}

























