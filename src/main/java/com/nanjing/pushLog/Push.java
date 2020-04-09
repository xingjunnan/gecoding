package com.nanjing.pushLog;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;

/**
 * @ClassName:  Push   
 * @Description: 向前端页面推送信息  
 * @author: Junnan
 * @date:   2019年8月23日 下午5:06:12
 */
public class Push {

	/**
	 * 前端页面socket监听端口
	 */
	private static String listenPort;

	@Value("${websocket.front.listenport}")
	public void setUrl(String listenPort) {
		Push.listenPort = listenPort;
	}

	/**
	 * @Title: pushLog   
	 * @Description: 向监听端口推送日志信息
	 * @param:  @param message
	 * @param:  @return
	 * @return: String      
	 */
	public static String pushLog(String message) {
		try {
			WebSocketServer.sendInfo(message, listenPort);
		} catch (IOException e) {
			e.printStackTrace();
			return listenPort + "#" + e.getMessage();
		}
		return listenPort;
	}
}
