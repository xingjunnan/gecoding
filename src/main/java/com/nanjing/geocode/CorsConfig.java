package com.nanjing.geocode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 
 * @ClassName:  CorsConfig   
 * @Description:跨域   
 * @author: Junnan
 * @date:   2019年8月30日 下午2:30:08
 */
@Configuration
public class CorsConfig {

	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		//允许任何域名访问
		corsConfiguration.addAllowedOrigin("*");
		//允许任何header访问
		corsConfiguration.addAllowedHeader("*");
		//允许任何方法访问
		corsConfiguration.addAllowedMethod("*");
		return corsConfiguration;
	}
	
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig());
		return new CorsFilter(source);
	}
}
