package com.nanjing.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nanjing.entity.BaiduMap;
import com.nanjing.entity.Excel;
import com.nanjing.entity.ThreadCount;


/**
 * @ClassName:  SetController   
 * @Description: 动态获取、修改配置文件信息 
 * @author: Junnan
 * @date:   2019年8月23日 下午20:20:20
 */
@RestController
@RequestMapping("/set")
public class SetController {

	private static Logger logger = (Logger) LoggerFactory.getLogger(SetController.class);
	
	@Autowired 
	private Excel excel;
	
	@Autowired 
	private BaiduMap baiduMap;
	
	@Autowired 
	private ThreadCount threadCount;
	
	/**
	 * @Title: getProperties   
	 * @Description: 获取配置文件信息  
	 * @param:  @return
	 * @return: Map<String,Object>      
	 */
	@RequestMapping("/getProperties")
	public Map<String,Object> getProperties() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("exportPath", excel.getExportPath().charAt(0));
		map.put("excelColumns", excel.getColumns());
		map.put("fieldname", excel.getFieldname());
		map.put("sheet", excel.getSheet());
		map.put("threadCount", threadCount.getCount());
		map.put("baiduAk", baiduMap.getAk());
		logger.info("设置信息查询成功");
		System.out.println(map);
		return map;
	}
	

	/**
	 * @Title: setProperties   
	 * @Description: 修改配置文件信息     
	 * @param:  @param map
	 * @param:  @return
	 * @return: Map<String,Object>      
	 */
	@RequestMapping("/setProperties")
	public Map<String,Object> setProperties(@RequestBody Map<String,Object> map) {
		Map<String,Object> successMap = new HashMap<String,Object>();
		try {
			excel.setColumns(Integer.parseInt(map.get("excelColumns")+""));
			excel.setFieldname(map.get("fieldname")+"");
			excel.setSheet(map.get("sheet")+"");
			excel.setExportPath(map.get("exportPath") + ":\\");
			threadCount.setCount(Integer.parseInt(map.get("threadCount")+""));
			baiduMap.setAk(map.get("baiduAk")+"");
			successMap.put("success", "true");
			return successMap;
		} catch (Exception e) {
			logger.info("设置信息保存失败");
		}
		logger.info("设置信息保存成功");
		successMap.put("success", "false");
		return successMap;
	}
}
