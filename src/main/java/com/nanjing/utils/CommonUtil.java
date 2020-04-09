package com.nanjing.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: CommonUtil
 * @Description:公共工具
 * @author: Junnan
 * @date: 2019年8月8日 下午4:44:51
 */
public class CommonUtil {

	/**
	 * 
	 * @Title: getDate
	 * @Description: 获取当前系统时间
	 * @return: String
	 */
	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyyMMddkkmmss");
		return dateFormat.format(date);
	}

	/**
	 * 
	 * @Title: cleanBlank
	 * @Description: 清除字符串中的空格
	 * @param: str
	 * @return: String
	 */
	public static String cleanBlank(String str) {
		if (str != null) {
			str = str.replaceAll("\\s*", "");
			return str;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Title: convertionString 
	 * @Description: 将编码格式iso-8859-1转成utf-8 
	 * @param: str 
	 * @return: String 
	 */
	public static String encoded(String str) {
		if (str != null && !"".equals(str)) {
			byte[] b;
			String s = "";
			try {
				b = str.getBytes("iso-8859-1");
				s = new String(b, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return s;
		}else{
			return "";
		}
	}
	
	/**
	 * 
	 * @Title: getFilePath   
	 * @Description: 根据文件绝对路径获取文件夹路径（不包含文件名）   
	 * @param:  @param filePath
	 * @param:  @return
	 * @return: String      
	 */
	public static String getFilePath(String filePath) {
		if ((filePath != null) && (filePath.length() > 0)) {
			int dot = filePath.lastIndexOf('\\');
			if ((dot > -1) && (dot < (filePath.length() - 1))) {
				return filePath.substring(0, dot+1);
			}
		}
		return filePath;
	}
	
	/**
	 * 
	 * @Title: getFileName   
	 * @Description: 根据文件绝对路径获取文件名（包含文件扩展名）     
	 * @param:  @param fileName
	 * @param:  @return
	 * @return: String      
	 */
	public static String getFileName(String fileName) {
		if ((fileName != null) && (fileName.length() > 0)) {
			int dot = fileName.lastIndexOf('\\');
			if ((dot > -1) && (dot < (fileName.length() - 1))) {
				return fileName.substring(dot + 1);
			}
		}
		return fileName;
	}

	/**
	 * 
	 * @Title: getExtensionName
	 * @Description: 获取文件扩展名
	 * @param: fileName 文件名
	 * @return: String
	 */
	public static String getExtensionName(String fileName) {
		if ((fileName != null) && (fileName.length() > 0)) {
			int dot = fileName.lastIndexOf('.');
			if ((dot > -1) && (dot < (fileName.length() - 1))) {
				return fileName.substring(dot + 1);
			}
		}
		return fileName;
	}

	/**
	 * 
	 * @Title: getFileNameNoEx
	 * @Description: 获取不带扩展名的文件名
	 * @param: fileName 文件名
	 * @return: String
	 */
	public static String getFileNameNoEx(String fileName) {
		if ((fileName != null) && (fileName.length() > 0)) {
			int dot = fileName.lastIndexOf('.');
			if ((dot > -1) && (dot < (fileName.length()))) {
				return fileName.substring(0, dot);
			}
		}
		return fileName;
	}


	/**
	 * 
	 * @Title: format   
	 * @Description: 将毫秒换算成时分秒
	 * @param:  t 毫秒
	 * @return: String      
	 */
	public static String timeFormat(long t) {
		if (t < 60000) {
			return (t % 60000) / 1000 + "秒";
		} else if ((t >= 60000) && (t < 3600000)) {
			return (t % 3600000) / 60000 + "分钟" + (t % 60000) / 1000 + "秒";
		} else {
			return t / 3600000 + "小时" + (t % 3600000) / 60000 + "分钟"+ (t % 60000) / 1000 + "秒";
		}
	}

	/**
	 * 
	 * @Title: byteFormat   
	 * @Description: 将字节秒换算成KB、MB、GB 
	 * @param:  size byte长度
	 * @return: String      
	 */
	public static String byteFormat(long size){
		long rest = 0;
		if(size < 1024){
			return String.valueOf(size) + "B";
		}else{
			size /= 1024;
		}

		if(size < 1024){
			return String.valueOf(size) + "KB";
		}else{
			rest = size % 1024;
			size /= 1024;
		}

		if(size < 1024){
			size = size * 100;
			return String.valueOf((size / 100)) + "." + String.valueOf((rest * 100 / 1024 % 100)) + "MB";
		}else{
			size = size * 100 / 1024;
			return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
		}
	}

}
