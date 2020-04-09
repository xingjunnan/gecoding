package com.nanjing.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName:  BaiduMap   
 * @Description:application.properties文件内百度地图API实体
 * @author: Junnan
 * @date:   2019年8月30日 下午2:27:52
 */
@Component
@ConfigurationProperties(prefix="api.map.baidu")
public class BaiduMap {
	
	private String url;
	
	private String ak;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}
	
}
