/**
 * 
 */
package org.domino.engine.utility.report;

import java.lang.reflect.Constructor;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletResponse;


import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.View;

import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.report.*;

/**
 * @author Administrator
 * 
 */
public class ReportFactory {

	/**
	 * 
	 */
	FacesContext facesContext;

	/**
	 * 
	 */
	ExternalContext exCon;

	/**
	 * 
	 */
	Map mapRequestParameter = null;

	/**
	 * 
	 */
	Map mapRequest = null;

	/**
	 * 
	 */
	public ReportFactory() {
		facesContext = FacesContext.getCurrentInstance();

		// The external context gives access to the servlet environment
		exCon = facesContext.getExternalContext();

		this.mapRequestParameter = exCon.getRequestParameterMap();
		this.mapRequest = exCon.getRequestMap();
	}

	/**
	 * 获得统计结果
	 * 
	 * @return
	 */
	public String getResult() {
		String strResult = "";

		// System.out.println(mapRequestParameter);
		// System.out.println(mapRequest);
		try {
			if (mapRequest != null) {
				// get the stat parm
				String strType = getRequestString("statType");

				if (Helper.ValidateNotEmpty(strType)) {
					Document docStatSetting = getStatTypeDoc(strType);
					String strClassName = docStatSetting
							.getItemValueString("StatClassName");
					String strServerName = docStatSetting
							.getItemValueString("StatServerName");
					String strDBFilePath = docStatSetting
							.getItemValueString("StatDatabaseFilePath");

					if (!(Helper.ValidateNotEmpty(strServerName))) {
						strServerName = Engine.getSession().getServerName();
					}

					Database dbData = Engine.getSession().getDatabase(
							strServerName, strDBFilePath);

					BaseReport report = null;
					if (Helper.ValidateNotEmpty(strClassName)) {
						// 根据统计类名生成统计对象
						Class tools = Class.forName(strClassName);
						Constructor conTools = tools
								.getConstructor(new Class[] { });
						report = (BaseReport) conTools
								.newInstance(new Object[] { });

						// JKReport report = new JKReport(this.getSession());
					} else {
						report = new BaseReport();
					}
					
					strResult = report.getResult(dbData, mapRequest);
					//System.out.println(strResult);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strResult;
	}

	/**
	 * 获得requestScope参数字符窜
	 * 
	 * @param strParameterName
	 * @return
	 */
	public String getRequestString(String strParameterName) {
		String strValue = "";
		strValue = (mapRequest.get(strParameterName) != null) ? ((String) mapRequest
				.get(strParameterName))
				: "";
		return strValue;
	}

	/**
	 * 获得统计配置文档
	 * 
	 * @param strTypeID
	 * @return
	 */
	public Document getStatTypeDoc(String strTypeID) {
		Document doc = null;

		try {
			Database db = Application.getSettingDB();
			View vw = db.getView("vwAllStatTypeByID");

			doc = vw.getDocumentByKey(strTypeID);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;

	}

}
