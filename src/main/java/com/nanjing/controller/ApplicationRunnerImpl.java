package com.nanjing.controller;

import java.io.IOException;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName:  ApplicationRunnerImpl   
 * @Description: 项目启动成功后，自定义提示信息
 * @author: JunnanXXX
 * @date:   2019年8月30日 上午20:20:20YYY
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("#################################################################################################################");
        System.out.println("##启动成功！！！ ");
        System.out.println("##浏览器访问该地址登录页面：    http://localhost:2019/");
        System.out.println("#################################################################################################################");
        try {
			 Runtime.getRuntime().exec(
			 "cmd   /c   start   http://localhost:2019/ ");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
