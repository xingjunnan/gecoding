package com.nanjing.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName:  ThreadCount   
 * @Description:application.properties文件内线程数量实体
 * @author: Junnan
 * @date:   2019年8月30日 下午2:29:32
 */
@Component
@ConfigurationProperties(prefix="thread")
public class ThreadCount {

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
