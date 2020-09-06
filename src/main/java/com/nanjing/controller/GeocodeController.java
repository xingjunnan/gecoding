package com.nanjing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName:  GeocodeController   
 * @Description: 首页界面访问路径#
 * @author: Junnan
 * @date:   2019年8月23日 下午5:55:55
 */
@Controller
public class GeocodeController {
	
	@RequestMapping("/")
	public String index(){
		return "index";
	}


}
