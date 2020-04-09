package com.nanjing.utils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.nanjing.entity.BaiduMap;
import com.nanjing.httpclient.HttpAPIService;
import com.nanjing.httpclient.HttpResult;
import com.nanjing.pushLog.Push;

/**
 * 
 * @ClassName:  GeocodeUtil   
 * @Description:根据地址访问百度地图API获取该地址的经纬度信息。
 * @author: Junnan
 * @date:   2019年8月8日 上午11:28:55  
 */
@Component
public class GeocodeUtil { 
	
	private static Logger logger = (Logger) LoggerFactory.getLogger(GeocodeUtil.class);
	
	
	private static BaiduMap baiduMap;
	
	@Autowired 
	public GeocodeUtil(BaiduMap baiduMap) {
		GeocodeUtil.baiduMap = baiduMap;
	}
	
	private static HttpAPIService httpAPIService;
	
	@Resource
	public void setHttpAPIService(HttpAPIService httpAPIService) {
		GeocodeUtil.httpAPIService = httpAPIService;
	}

	/**
	 * 
	 * @Title: getGeocode   
	 * @Description:  根据地址参数返回经纬度信息，不存在则返回null。
	 * @param:  addressParam 地址
	 * @return: String[]      
	 */
	@SuppressWarnings("all")
	public static String[] getGeocode(String addressParam) {
		String address = CommonUtil.cleanBlank(addressParam);
		if (StringUtils.isEmpty(address)) {
			return null;
		}
		if (address.length() > 40) {
			address = address.substring(0, 40);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("output", "json");
		map.put("ak", baiduMap.getAk());
		map.put("address", address);
		HttpResult hs = null;
		try {
			hs = httpAPIService.doPost(baiduMap.getUrl(), map);
		} catch (Exception e) {
			logger.error("获取百度地图API",e.getStackTrace());
			Push.pushLog("请检查网络连接是否正常 或 联系管理员" + e.getStackTrace());
			e.printStackTrace();
		}
		String result = hs.getBody();
		JSONObject jsonObj = new JSONObject(result);
		String status = jsonObj.get("status") + "";
		if ("0".equals(status)) {
			JSONObject result_jsonObj = (JSONObject) jsonObj.get("result");
			JSONObject location_jsonObj = (JSONObject) result_jsonObj.get("location");
			String lng = location_jsonObj.get("lng") + "";
			String lat = location_jsonObj.get("lat") + "";
			System.out.println("经度：" + lng + " " + "纬度：" + lat);
			String[] geocodeArr = new String[2];
			geocodeArr[0] = lng;
			geocodeArr[1] = lat;
			return geocodeArr;
		} else {
				logger.info(result);
				if (!"1".equals(status)) {
					String[] geocodeArr = new String[2];
					geocodeArr[0] = "Exception";
					geocodeArr[1] = result;
					return geocodeArr;
				}
				return null;

		}
	}

}
