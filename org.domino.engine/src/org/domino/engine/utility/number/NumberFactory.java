/**
 * 
 */
package org.domino.engine.utility.number;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.foundation.DataFactory;
import org.domino.engine.utility.DominoObjectHelper;

import lotus.notes.DocumentCollection;

/**
 * @author iceeer
 * 
 */
public class NumberFactory {

	/**
	 * 
	 */
	private static final String DEFAULT_NUMBER_MANAGE_CONFIGURATION_VIEW_NAME_KEY = "number.configuration.view.name";

	/**
	 * 
	 */
	private static final String DEFAULT_NUMBER_MANAGE_ITEM_CONFIGURATION_VIEW_NAME_KEY = "number.item.configuration.view.name";

	private static Database dbNumberManage = null;

	private static Database dbNumberData = null;

	/**
	 * 
	 */
	Session session = null;

	/**
	 * 
	 */
	public NumberFactory(Session session) throws NotesException {
		this.session = session;

		this.dbNumberManage = Application.getSettingDB(session);
		this.dbNumberData = Application.getApplicationDB(session);
	}

	/**
	 * 获得编号
	 * 
	 * @param strNumberConfID
	 *            编号配置标示
	 * @param mapParameter
	 *            编号配置
	 * @param needCommit
	 *            是否获得编号后就更新编号库
	 * @return 编号
	 * @throws NotesException
	 */
	public String getNumber(String strNumberConfID, Map mapParameter,
			boolean needCommit) throws NotesException {
		String strNumber = "";// 编号

		try {
			Document docNumberConfiguration = getNumberConfigurationDocument(strNumberConfID);// 获得编号配置文档

			// 判断编号配置文档是否存在
			if (docNumberConfiguration != null) {
				Date dateNow = doDateParameter(mapParameter);// 处理日期参数

				// String strNumberConfigurationID = docNumberConfiguration
				// .getItemValueString("ConfigurationID");// 编号配置标识

				// strDataValue = docData.getItemValueString("DataValue");
				synchronized (com.sun.faces.application.ApplicationAssociate.class) {
					// 获得编号项配置文档集合
					ViewEntryCollection vec = getNumberItemConfigurationCollection(strNumberConfID);

					if (vec.getCount() > 0) {// 判断编号项配置文档集合是否为空
						Boolean blNeedIncrease = false;// 是否需要增量
						ViewEntry tmpentry;
						ViewEntry entry = vec.getFirstEntry();// 获得第一个视图项
						while (entry != null) {// 循环编号项配置文档
							Document docNumberItemConfiguration = entry
									.getDocument();// 编号项配置文档
							String strNumberItemConfigurationID = docNumberItemConfiguration
									.getItemValueString("ConfigurationID");// 编号项配置标识
							String strItemType = docNumberItemConfiguration
									.getItemValueString("ItemType");// 编号项配置类型
							String strItemFormatString = docNumberItemConfiguration
									.getItemValueString("ItemFormatString");// 编号项配置格式
							String strItemInitValue = docNumberItemConfiguration
									.getItemValueString("ItemInitValue");// 编号项初始值
							Integer intItemNo = docNumberItemConfiguration
									.getItemValueInteger("ItemNo");// 编号项配置排序号

							// 根据编号项类型配置获得编号项
							if (strItemType.equals("1")) {// 数字序列
								String strText = doNumberItemType1(
										strNumberConfID, blNeedIncrease,
										strNumberItemConfigurationID,
										strItemFormatString, strItemInitValue,
										intItemNo, needCommit);

								// 组合编号
								strNumber += strText;
							} else if (strItemType.equals("2")) {// 字母序列
								// TODO
							} else if (strItemType.equals("3")) {// 常量代码
								blNeedIncrease = doNumberItemType3(
										strNumberConfID,
										strNumberItemConfigurationID,
										strItemInitValue, intItemNo, needCommit);

								// 组合编号
								strNumber += strItemInitValue;
							} else if (strItemType.equals("4")) {// 日期
								// 根据配置的日期格式生成日期编号项
								SimpleDateFormat dateformat = new SimpleDateFormat(
										strItemFormatString);
								String strDateNumber = dateformat
										.format(dateNow);

								blNeedIncrease = doNumberItemType4(
										strNumberConfID,
										strNumberItemConfigurationID,
										intItemNo, strDateNumber, needCommit);

								// 组合编号
								strNumber += strDateNumber;
							}

							// 获得下一视图项
							tmpentry = vec.getNextEntry();
							entry.recycle();
							entry = tmpentry;
						}

					}
				}

				// 创建编号数据文档
				String strRelDocumentId = (String) (mapParameter.get("relid"));
				createNumberDoc(strRelDocumentId, strNumber);

				System.out.println("Get Number:" + strNumber);
			} else {
				throw new NumberException("编号配置文档[" + strNumberConfID + "]不存在于");
			}

		} catch (NumberException e) {
			System.out.println(e);
			strNumber = "0";
		}

		return strNumber;
	}

	public boolean commitNumber() {
		return true;
	}

	/**
	 * 
	 * @param strNumberConfigurationID
	 * @param strNumberItemConfigurationID
	 * @param intItemNo
	 * @param strDateNumber
	 * @return
	 * @throws NotesException
	 */
	private Boolean doNumberItemType4(String strNumberConfigurationID,
			String strNumberItemConfigurationID, Integer intItemNo,
			String strDateNumber, boolean needCommit) throws NotesException {
		Boolean blNeedIncrease;
		Document docNumberItem = getNumberItemDoc(strNumberConfigurationID,
				strNumberItemConfigurationID);
		if (docNumberItem == null) {// 没有编过号
			blNeedIncrease = false;

			if (needCommit) {
				// 保存编号项
				createNumberItemData(strNumberConfigurationID,
						strNumberItemConfigurationID, intItemNo, strDateNumber,
						strDateNumber);
			}
		} else {
			// 判断当前编号库中编号项与计算出的编号项是否相等（是否同一时间段）
			String strNumberItemData = docNumberItem
					.getItemValueString("NumberItemData");
			if (strNumberItemData.equals(strDateNumber)) {//
				blNeedIncrease = true;
			} else {// 不是同一时间段则修改编号库中编号项
				if (needCommit) {
					setNumberItemData(strDateNumber, strDateNumber,
							docNumberItem);
				}

				blNeedIncrease = false;
			}
		}
		return blNeedIncrease;
	}

	/**
	 * 
	 * @param strNumberConfigurationID
	 * @param strNumberItemConfigurationID
	 * @param strItemInitValue
	 * @param intItemNo
	 * @return
	 * @throws NotesException
	 */
	private Boolean doNumberItemType3(String strNumberConfigurationID,
			String strNumberItemConfigurationID, String strItemInitValue,
			Integer intItemNo, boolean needCommit) throws NotesException {
		Boolean blNeedIncrease;
		// 判断是否编过号
		Document docNumberItem = getNumberItemDoc(strNumberConfigurationID,
				strNumberItemConfigurationID, strItemInitValue);
		if (docNumberItem == null) {// 没有编过号
			blNeedIncrease = false;

			if (needCommit) {
				// 保存编号项
				createNumberItemData(strNumberConfigurationID,
						strNumberItemConfigurationID, intItemNo,
						strItemInitValue, strItemInitValue);
			}
		} else {
			blNeedIncrease = true;
		}
		return blNeedIncrease;
	}

	/**
	 * 
	 * @param strNumberConfigurationID
	 * @param blNeedIncrease
	 * @param strNumberItemConfigurationID
	 * @param strItemFormatString
	 * @param strItemInitValue
	 * @param intItemNo
	 * @return
	 * @throws NotesException
	 */
	private String doNumberItemType1(String strNumberConfigurationID,
			Boolean blNeedIncrease, String strNumberItemConfigurationID,
			String strItemFormatString, String strItemInitValue,
			Integer intItemNo, boolean needCommit) throws NotesException {
		// 获得初始数值设置
		Long lngValue = 0l;
		if (strItemInitValue.equals("")) {// 判断是否设置初始值
		} else {
			// 获得初始数值
			try {
				lngValue = Long.parseLong(strItemInitValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String strValue = lngValue.toString();
		String strText = strValue;

		// 判断是否编过号
		Document docNumberItem = getNumberItemDoc(strNumberConfigurationID,
				strNumberItemConfigurationID);
		if (docNumberItem == null) {// 没有编过号

			if (strItemFormatString.equals("")) {
			} else {
				// 根据配置的格式生成编号项
				strText = String.format(strItemFormatString, lngValue);
			}

			if (needCommit) {
				// 保存编号项
				createNumberItemData(strNumberConfigurationID,
						strNumberItemConfigurationID, intItemNo, strValue,
						strText);
			}
		} else {
			// System.out.println("blNeedIncrease:" + blNeedIncrease);
			if (blNeedIncrease == true) {// 加增量
				// 获得编号库中编号项值
				String strNumberItemData = docNumberItem
						.getItemValueString("NumberItemData");
				lngValue = Long.parseLong(strNumberItemData);
				lngValue = lngValue + 1;
				strValue = lngValue.toString();

			} else {

			}
			if (strItemFormatString.equals("")) {
			} else {
				// 根据配置的格式生成编号项
				strText = String.format(strItemFormatString, lngValue);
			}

			if (needCommit) {
				// 修改编号库中编号项
				setNumberItemData(strValue, strText, docNumberItem);
			}
		}
		return strText;
	}

	/**
	 * 
	 * @param strValue
	 * @param strText
	 * @param docNumberItem
	 * @throws NotesException
	 */
	private void setNumberItemData(String strValue, String strText,
			Document docNumberItem) throws NotesException {
		docNumberItem.replaceItemValue("NumberItemData", strValue);
		docNumberItem.replaceItemValue("NumberItemText", strText);
		docNumberItem.save();
	}

	/**
	 * 
	 * @param strNumberConfigurationID
	 * @param strNumberItemConfigurationID
	 * @return
	 * @throws NotesException
	 */
	private Document getNumberItemDoc(String strNumberConfigurationID,
			String strNumberItemConfigurationID) throws NotesException {
		Vector<String> key = new Vector<String>();
		key.add(strNumberConfigurationID);
		key.add(strNumberItemConfigurationID);
		Document docNumberItem = DominoObjectHelper.getDocumentByKey(
				dbNumberData, "vwAllNumberItemByRelConfigurationID", key);
		return docNumberItem;
	}

	/**
	 * 
	 * @param strNumberConfigurationID
	 * @param strNumberItemConfigurationID
	 * @param strItemInitValue
	 * @return
	 * @throws NotesException
	 */
	private Document getNumberItemDoc(String strNumberConfigurationID,
			String strNumberItemConfigurationID, String strItemInitValue)
			throws NotesException {
		Vector<String> key = new Vector<String>();
		key.add(strNumberConfigurationID);
		key.add(strNumberItemConfigurationID);
		key.add(strItemInitValue);
		Document docNumberItem = DominoObjectHelper.getDocumentByKey(
				dbNumberData, "vwAllNumberItemByRelConfigurationID", key);
		return docNumberItem;
	}

	/**
	 * 
	 * @param strNumberConfID
	 * @return
	 * @throws NotesException
	 */
	private ViewEntryCollection getNumberItemConfigurationCollection(
			String strNumberConfID) throws NotesException {
		ViewEntryCollection vec = DominoObjectHelper
				.getAllEntriesByKey(
						dbNumberManage,
						Engine.getProperty(DEFAULT_NUMBER_MANAGE_ITEM_CONFIGURATION_VIEW_NAME_KEY),
						strNumberConfID);
		return vec;
	}

	/**
	 * 
	 * @return
	 * @throws NotesException
	 */
	private boolean createNumberDoc(String strRelDocumentId, String strNumber) {
		Document docNumber;
		try {
			docNumber = dbNumberData.createDocument();
			docNumber.replaceItemValue("Form", "fmNumberData");
			String strNumberID = buildDocumentStaticID(docNumber);
			docNumber.replaceItemValue("DataID", strNumberID);
			// 保存编号
			docNumber.replaceItemValue("Number", strNumber);
			// 保存关联文档
			docNumber.replaceItemValue("relDocumentID", strRelDocumentId);
			docNumber.save();
			return true;
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param mapParameter
	 * @return
	 */
	private Date doDateParameter(Map mapParameter) {
		Date dateNow = new Date();

		// 判断日期是否传入
		String strDate = (String) mapParameter.get("date");
		if (Helper.ValidateNotEmpty(strDate)) {
			try {
				DateFormat dateFormat;
				dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS",
						Locale.ENGLISH);// 设定格式
				// dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",
				// Locale.ENGLISH);
				dateFormat.setLenient(false);
				dateNow = dateFormat.parse(strDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return dateNow;
	}

	/**
	 * 
	 * @param strNumberConfID
	 * @return
	 * @throws NotesException
	 */
	private Document getNumberConfigurationDocument(String strNumberConfID)
			throws NotesException {

		return DominoObjectHelper
				.getDocumentByKey(
						dbNumberManage,
						Engine.getProperty(DEFAULT_NUMBER_MANAGE_CONFIGURATION_VIEW_NAME_KEY),
						strNumberConfID);
	}

	/**
	 * 
	 * @param doc
	 * @return
	 * @throws NotesException
	 */
	public String buildDocumentStaticID(Document doc) throws NotesException {
		return "S" + doc.getUniversalID();
	}

	/**
	 * 
	 * @param strNumberConfigurationID
	 * @param strNumberItemConfigurationID
	 * @param intItemNo
	 * @param strItemData
	 * @param strItemText
	 * @return
	 * @throws NotesException
	 */
	private Document createNumberItemData(String strNumberConfigurationID,
			String strNumberItemConfigurationID, Integer intItemNo,
			String strItemData, String strItemText) throws NotesException {
		// 创建编号项数据文档
		Document docNumberItem = dbNumberData.createDocument();
		docNumberItem.replaceItemValue("Form", "fmNumberItemData");
		docNumberItem.replaceItemValue("DataID",
				buildDocumentStaticID(docNumberItem));

		// docNumberItem.replaceItemValue("relNumberID", strNumberID);
		docNumberItem.replaceItemValue("relNumberConfigurationID",
				strNumberConfigurationID);
		docNumberItem.replaceItemValue("relNumberItemConfigurationID",
				strNumberItemConfigurationID);
		docNumberItem.replaceItemValue("NumberItemDataNo", intItemNo);
		setNumberItemData(strItemData, strItemText, docNumberItem);

		return docNumberItem;
	}

}
