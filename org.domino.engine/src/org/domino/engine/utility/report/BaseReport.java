/**
 * 
 */
package org.domino.engine.utility.report;

import java.util.Map;
import lotus.domino.Database;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * @author Administrator
 * 
 */
public class BaseReport {

	/**
	 * 
	 */
	private Database dbData = null;
	
	/**
	 * 
	 */
	private Map mapParam = null;
	
	/**
	 * 
	 */
	public BaseReport() {
	}

	/**
	 * 获得统计结果
	 * 
	 * @return
	 */
	public String getResult(Database dbData, Map mapParam) {
		String strResult = "";
		
		this.setDbData(dbData);
		this.setMapParam(mapParam);
		
		try {
			String strSearchString = "";//搜索字符串
			
			if (dbData.isFTIndexed()) {//判断是否启用索引
				dbData.updateFTIndex(false);//更新索引
			} else {
				dbData.createFTIndex(0, true);//创建索引
			}
			DocumentCollection dc = dbData.FTSearch(strSearchString);//搜索
			
			
		} catch (NotesException e) {
			e.printStackTrace();
		}
		
		return strResult;
	}

	/**
	 * @param dbData the dbData to set
	 */
	public void setDbData(Database dbData) {
		this.dbData = dbData;
	}

	/**
	 * @return the dbData
	 */
	public Database getDbData() {
		return dbData;
	}

	/**
	 * @param mapParam the mapParam to set
	 */
	public void setMapParam(Map mapParam) {
		this.mapParam = mapParam;
	}

	/**
	 * @return the mapParam
	 */
	public Map getMapParam() {
		return mapParam;
	}

}
