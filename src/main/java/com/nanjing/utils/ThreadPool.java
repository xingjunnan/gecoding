package com.nanjing.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nanjing.controller.UploadController;

/**
 * 
 * @ClassName:  ThreadPool   
 * @Description:线程池  
 * @author: Junnan
 * @date:   2019年8月19日 下午5:11:50
 */
public class ThreadPool {
	
	/**
	 * 
	 * @Title: execute   
	 * @Description: 创建线程池 
	 * @param:  threadCount 线程个数
	 * @return: void      
	 */
	public static void execute(int threadCount) {
		// 创建一个可重用固定线程数的线程池
		ExecutorService pool = Executors.newFixedThreadPool(threadCount);
		// 创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口。
		for (int i=0;i<threadCount;i++) {
			Thread t = new UploadController();
			pool.execute(t);
		}
		// 关闭线程池
		pool.shutdown();
	}
}
