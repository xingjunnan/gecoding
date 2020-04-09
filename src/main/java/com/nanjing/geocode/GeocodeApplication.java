package com.nanjing.geocode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @ClassName:  GeocodeApplication   
 * @Description: 项目启动类
 * @author: Junnan
 * @date:   2019年8月30日 下午2:30:24
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = { "com.nanjing", "org.springframework.boot.env"})
public class GeocodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeocodeApplication.class, args);
	}

}
