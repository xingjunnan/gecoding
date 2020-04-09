package com.nanjing.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.nanjing.pushLog.Push;

/**
 * 
 * @ClassName:  ExcelUtil   
 * @Description: 解析读取、创建导出excel工具   
 * @author: Junnan
 * @date:   2019年8月30日 下午3:20:25
 */
public class ExcelUtil {

	private static Logger logger = (Logger) LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * 
	 * @Title: readerExcel   
	 * @Description:     解析读取Excel
	 * @param path       文件路径
	 * @param sheetName  sheet名称
	 * @param minColumns 列总数
	 * @param:  @throws IOException
	 * @param:  @throws OpenXML4JException
	 * @param:  @throws ParserConfigurationException
	 * @param:  @throws SAXException
	 * @return: List<String[]>      
	 * @throws
	 */
	public static List<String[]> readerExcel(InputStream is, String sheetName, int minColumns)
			throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
		OPCPackage p = OPCPackage.open(is);
		XLSXCovertCSVReader xlsx2csv = new XLSXCovertCSVReader(p, System.out, sheetName, minColumns);
		List<String[]> list = null;
		try {
			list = xlsx2csv.process();
		} catch (Exception e) {
			Push.pushLog("excel解析失败!检查excel列数是否配置正确!检查Sheet是否配置正确！");
		}finally {
			p.close();
		}
		return list;
	}


	/**
	 * 
	 * @Title: createExcelFile   
	 * @Description: 创建导出excel  
	 * @param:  @param list
	 * @param:  @param fileName
	 * @param:  @param exportPath
	 * @param:  @return
	 * @return: String      
	 * @throws
	 */
	@SuppressWarnings({ "all" })
	public static String createExcelFile(List<String[]> list, String fileName, String exportPath) {
		String exportExcelPath = getExportExcelPath(fileName, exportPath);
		SXSSFWorkbook workbook = null;
		try {
			workbook = new SXSSFWorkbook(500);
		} catch (Exception e) {
			System.out.println("It cause Error on CREATING excel workbook: ");
			e.printStackTrace();
		}
		if (workbook != null) {
			CellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			Sheet sheet = workbook.createSheet("Sheet1");
			for (int rowNum = 0; rowNum < list.size(); rowNum++) {
				Row row = sheet.createRow(rowNum);
				String[] record = (String[]) list.get(rowNum);
				for (int i = 0; i < record.length; i++) {
					sheet.setColumnWidth(i, 5000);
					Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
					cell.setCellValue(record[i]);
				}
			}
			logger.info("数据组装完毕！开始导出文件！");
			try {
				FileOutputStream outputStream = new FileOutputStream(exportExcelPath);
				workbook.write(outputStream);
				workbook.close();
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("文件导出成功！导出路径：" + exportExcelPath);
		return exportExcelPath;
	}

	/**
	 * 
	 * @Title: getExportExcelPath   
	 * @Description: 获取excel导出路径
	 * @param:  @param fileName
	 * @param:  @param exportPath
	 * @param:  @return
	 * @return: String      
	 * @throws
	 */
	private static String getExportExcelPath(String fileName, String exportPath) {
		if (fileName.contains(":\\")) {
			exportPath = CommonUtil.getFilePath(fileName);
			fileName = CommonUtil.getFileName(fileName);
		}
		String exportExcelPath = exportPath + CommonUtil.getFileNameNoEx(fileName) + CommonUtil.getDate() + "."
				+ CommonUtil.getExtensionName(fileName);
		return exportExcelPath;
	}
}
