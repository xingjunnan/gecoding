package com.nanjing.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.nanjing.entity.Excel;
import com.nanjing.entity.ThreadCount;
import com.nanjing.pushLog.Push;
import com.nanjing.utils.CommonUtil;
import com.nanjing.utils.ExcelUtil;
import com.nanjing.utils.GeocodeUtil;
import com.nanjing.utils.ThreadPool;

/**
 * 
 * @ClassName:  UploadController   
 * @Description: 上传excel文件，根据地址字段在excel末尾添加经纬度字段，最后导出添加后的文件 
 * @author: Junnan
 * @date:   2019年8月10日 上午9:28:01
 */ 
@RestController 
@RequestMapping("/geocode")
public class UploadController extends Thread {

	private static Logger logger = (Logger) LoggerFactory.getLogger(UploadController.class);

	// excel文件名称
	private String fileName;

	// excel文件大小
	private long fileSize;

	// excel数据集合
	private static List<String[]> list = new ArrayList<String[]>();
	
	// excel记录数
	private static int listSize;

	// 正在执行的记录数
	private static int runningCount = 0;
	
	// 数据添加成功的记录数
	private static int successfulCount = 0;
	
	// 数据添加失败的记录数
	private static int unsuccessfulCount = 0;
	
	// 遍历api索引值
	private static int index = 0;
	
	// 倒计时器
	private static CountDownLatch cdl = null;

	//excel中需要转成经纬度的地址
	private static String fieldname;
	
	@Autowired 
	private Excel excel;

	
	@Autowired 
	private ThreadCount threadCount;
	
	/**
	 * 
	 * @Title: importFile   
	 * @Description: 上传excel文件，根据地址字段在excel末尾添加经纬度字段，最后导出添加后的文件 
	 * @param:  request
	 * @param:  response
	 * @return: String      
	 */
	@SuppressWarnings("all")
	@RequestMapping("/importFile")
	public String importFile(HttpServletRequest request, HttpServletResponse response) {
		// _程序开始时间
		long startTime = System.currentTimeMillis();
		JSONObject jsonObj = new JSONObject();
		runningCount = 0;
		successfulCount = 0;
		unsuccessfulCount = 0;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Iterator iter = multipartRequest.getFileNames();
			MultipartFile item = multipartRequest.getFile(iter.next().toString());
			if (!item.isEmpty()) {
				// excel解析
				String parseTime = parseExcel(item);
				if (parseTime == null) {
					return null;
				}
				// excel插入经纬度
				String insertTime = insertData();
				// excel导出
				exportExcel(startTime, parseTime, insertTime);
			}
			jsonObj.put("msg", "ok");
			jsonObj.put("code", "0");
		} else {
			jsonObj.put("msg", "error"); 
			jsonObj.put("code", "1");
		}
	     return jsonObj.toString();
	}
	
	/**
	 * 
	 * @Title: parseExcel   
	 * @Description: 解析excel，获取文件数据
	 * @param:  item
	 * @return: String  返回解析时间    
	 * @throws
	 */
	@SuppressWarnings("all")
	private String parseExcel(MultipartFile item) {
		fileName = item.getOriginalFilename();
		fileSize = item.getSize();
		fieldname = excel.getFieldname();
		InputStream is = null;
		// excel解析开始时间
		long parse_startTime = System.currentTimeMillis();
		try {
			is = item.getInputStream();
			//将excel流解析成List
			Push.pushLog("正在解析上传文件。。");
			list = ExcelUtil.readerExcel(is, excel.getSheet(), excel.getColumns());
			if (list != null) {
				listSize = list.size() - 1;
			}else {
				Push.pushLog("excel解析失败!检查excel列数是否配置正确!检查sheet是否配置正确！");
				return null ;
			}
			cdl = new CountDownLatch(listSize);
			Push.pushLog("@"+(listSize));
		} catch (Exception e) {
			logger.error("excel解析",e.getStackTrace());
			Push.pushLog("excel解析失败");
			e.printStackTrace();
		}
		// excel解析结束时间
		long parse_endTime = System.currentTimeMillis();
		logger.info("解析Excel完成！耗时" + CommonUtil.timeFormat(parse_endTime - parse_startTime));
		return CommonUtil.timeFormat(parse_endTime - parse_startTime);
	}

	/**
	 * 
	 * @Title: insertData   
	 * @Description: 给excel插入经纬度信息
	 * @return: String 插入时间    
	 * @throws
	 */
	private String insertData() {
		// excel添加经纬度开始时间
		long insert_startTime = System.currentTimeMillis();
		ThreadPool.execute(threadCount.getCount());
		try {
			cdl.await();
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// excel添加经纬度结束时间
		long insert_endTime = System.currentTimeMillis();
		logger.info("添加经纬度完成！耗时" + CommonUtil.timeFormat(insert_endTime - insert_startTime));
		return CommonUtil.timeFormat(insert_endTime - insert_startTime);
	}
	
	/**
	 * 
	 * @Title: exportExcel   
	 * @Description: 导出excel并打开文件导出文件夹
	 * @param:  startTime 项目开始时间
	 * @param:  parseTime 项目解析时间
	 * @param:  insertTime 项目插入时间
	 * @return: void      
	 * @throws
	 */
	private void exportExcel(long startTime, String parseTime,String insertTime) {
		// 将List写入excel，并导出excel
		String exportExcelPath = ExcelUtil.createExcelFile(list, fileName, excel.getExportPath());
		//  程序结束时间
		long endTime = System.currentTimeMillis();
		String sumTime = CommonUtil.timeFormat(endTime - startTime);
		logger.info(fileName + "为" + CommonUtil.byteFormat(fileSize) + "，共" + listSize + "条记录");
		logger.info("共耗时" + sumTime + "解析文件耗时" + parseTime + "，添加经纬度耗时" + insertTime);
		Push.pushLog("文件导出成功！导出路径："+exportExcelPath + "，共" + listSize + "条记录。成功添加"+ (successfulCount-threadCount.getCount()+1) +"条，无效数据"+unsuccessfulCount+"条。共耗时" + sumTime + "解析文件耗时" + parseTime + "，添加经纬度耗时" + insertTime);
		// 导出成功，打开导出文件的文件夹
		try {
			Runtime.getRuntime().exec(new String[] {"cmd","/c","start"," ",CommonUtil.getFilePath(exportExcelPath)});
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("########################################################################################################");
        System.out.println("##############                                 SUCCESS                                     #############");
        System.out.println("########################################################################################################");

	}
	
	@Override
	public void run() {
		insertGeocode();
	}
	
	/**
	 *  
	 * @Title: insertGeocode   
	 * @Description: 为excel插入经纬度
	 * @return: void      
	 */
	private void insertGeocode() {
	    int addressIndex = -1;
		String address = "";
		for (index = 0; index < listSize + 1; index++) {
			System.out.println(index);
			runningCount = index;
			String[] record = list.get((int) index);
			if (index == 0) {
				for (int j = 0; j < record.length; j++) {
					if (fieldname.equals(record[j])) {
						addressIndex = j;
						record[record.length - 2] = "经度";
						record[record.length - 1] = "纬度";
						break;
					}
				}
				if (addressIndex == -1) {
					logger.error("没有找到需要解析的字段名或excel表头不存在该字段!");
					Push.pushLog("没有找到需要解析的列名或excel表头不存在该列!");
					break;
				}
			} else {
				address = record[addressIndex];
				logger.info( UploadController.currentThread().getName()+"正在处理第" + runningCount +"条数据,共" + listSize + "条");
				Push.pushLog("正在处理第" + runningCount +"条数据,共" + listSize + "条");
				String[] geocode = GeocodeUtil.getGeocode(address);
				if (geocode != null) {
					if("Exception".equals(geocode[0])) {
						Push.pushLog(geocode[1]);
						break;
					}
					record[record.length - 2] = geocode[0];
					record[record.length - 1] = geocode[1];
					++successfulCount;
				}else {
					++unsuccessfulCount;
				}
				cdl.countDown();
			}
		}
	}
}
