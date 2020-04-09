package com.nanjing.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName:  Excel   
 * @Description:application.properties文件内excel实体
 * @author: Junnan
 * @date:   2019年8月30日 下午2:28:54
 */
@Component
@ConfigurationProperties(prefix="excel")
public class Excel {
	
	private int columns;
	
	private String fieldname;
	
	private String sheet;
	
	private String exportPath;

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getSheet() {
		return sheet;
	}

	public void setSheet(String sheet) {
		this.sheet = sheet;
	}

	public String getExportPath() {
		return exportPath;
	}

	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}
	
}
