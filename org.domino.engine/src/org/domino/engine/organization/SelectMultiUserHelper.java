/**
 * 
 */
package org.domino.engine.organization;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.domino.engine.Application;
import org.domino.engine.Engine;


import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;

/**
 * @author Administrator
 * 
 */
public class SelectMultiUserHelper {

	/**
	 * 
	 */
	public SelectMultiUserHelper() {
	}

	public String selectUserFromView(String strSelectedValue,
			String[] arrSelectedIDs) throws NotesException {

		java.util.LinkedHashMap map = new java.util.LinkedHashMap();
		
		map = convertStringToMap(map, strSelectedValue);

		for (int i = 0; i < arrSelectedIDs.length; i++) {
			String strKey = "";
			String strValue = "";

			String docId = arrSelectedIDs[i];
			Database database = Application.getApplicationDB();
			Document doc = database.getDocumentByID(docId);

			// deal with the selected document
			String strForm = doc.getItemValueString("Form");
			if (strForm.equals("PersonUnit")) {// 判断是否是人员所属机构文档
				String strPersonID = doc.getItemValueString("PersonID");

				// 获得人员文档
				View vw = database.getView(Engine.getProperty("organization.personunit.by.id.view.name"));
				Document docPerson = vw.getDocumentByKey(strPersonID, true);
				addPersonToMap(map, strPersonID, docPerson);
			} else if (strForm.equals("Unit")) {// 判断是否是机构文档
				String strUnitID = doc.getItemValueString("UnitID");
				Unit unit = new Unit(strUnitID);
				List listUser = unit.getAllUnitUser();
				for (int j = 0; j < listUser.size(); j++) {
					Person person = (Person)listUser.get(j);
					Document docPerson = person.getPersonDoc();
					addPersonToMap(map, person.getPersonID(), docPerson);
				}
				
			}
		}

		return convertMapToString(map);
	}

	/**
	 * @param map
	 * @param strPersonID
	 * @param docPerson
	 * @throws NotesException
	 */
	private void addPersonToMap(java.util.LinkedHashMap map,
			String strPersonID, Document docPerson) throws NotesException {
		String strKey;
		String strValue;
		if (docPerson != null) {// 判断人员文档是否存在

			String strPersonName = docPerson
					.getItemValueString("PersonName");
			if ((strPersonName != null) && (!"".equals(strPersonName))) {// 判断用户名是否设置
				strKey = strPersonID;
				strValue = strPersonName;
			} else {
				Name _nn = Engine.getSession().createName(strPersonID);
				strKey = strPersonID;
				strValue = _nn.getCommon();
			}
		} else {
			Name _nn = Engine.getSession().createName(strPersonID);
			strKey = strPersonID;
			strValue = _nn.getCommon();
		}
		map.put(strKey, strValue);
	}

	/**
	 * 
	 * @param map
	 * @param strValues
	 * @return
	 */
	private java.util.LinkedHashMap convertStringToMap(java.util.LinkedHashMap map, String strValues) {
		if (map == null) {
			map = new java.util.LinkedHashMap();
		}
		String[] arrValues = strValues.split("\\,");

		for (int i = 0; i < arrValues.length; i++) {
			String strValue = arrValues[i];
			String[] arrValue = strValue.split("\\|");
			if (arrValue.length > 1) {
				String strKey = arrValue[1];
				String strText = arrValue[0];
				
				if (!strKey.equals("")) {
					if (!map.containsKey(strKey)) {
						map.put(strKey, strText);
					}
				}
			}
		}

		return map;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	private String convertMapToString(java.util.LinkedHashMap map) {
		String strValues = "";

		for (Iterator i = map.keySet().iterator(); i.hasNext();) {
			String strKey = (String) i.next();
			String strText = (String) map.get(strKey);

			if (strValues.equals("")) {
				strValues = strText + "|" + strKey;
			} else {
				strValues += "," + strText + "|" + strKey;
			}
		}

		return strValues;
	}

}
