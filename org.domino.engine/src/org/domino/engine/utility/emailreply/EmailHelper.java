package org.domino.engine.utility.emailreply;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.ecs.xhtml.td;
import org.apache.ecs.xhtml.tr;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.EmbeddedObject;
import lotus.domino.Item;
import lotus.domino.Name;
import lotus.domino.RichTextItem;
import lotus.domino.Session;
import lotus.domino.View;

public class EmailHelper {

	public static final String VIEW_NAME_OF_All_HISTORY_BY_REF_DOC_ID = "vwAllFlowHistoryByRefDocumentID";

	public static final String VIEW_NAME_OF_ENABLE_EMAIL_CONFIGURATION = "vwEnableMailConfiguration";

	public static final String DIRECTORY_NAME_FOR_TMP_DOWNLOAD = "emailtemp";

	public static final String ERROR_CHANGE_NAME = "[" + new Date()
			+ "][错误][处理人已变更]";



	public static boolean attachFileToRichTextItem(lotus.domino.Document doc,
			String strFieldName, String strFilePath, String strFileName) {
		try {
			Item item = doc.getFirstItem(strFieldName);
			RichTextItem body = null;
			if (item == null) {
				body = doc.createRichTextItem(strFieldName);
			} else if (item.getType() == Item.RICHTEXT) {
				body = (RichTextItem) item;
			} else {
				item.remove();
				body = doc.createRichTextItem(strFieldName);
			}
			body.embedObject(EmbeddedObject.EMBED_ATTACHMENT, "", strFilePath,
					strFileName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/*
	 * 
	 */
	public static tr buildOneLineDataTr(String strData) {
		tr oTr = new tr();
		td oTd = new td();
		oTd.addElement(strData);
		oTr.addElement(oTd);
		return oTr;
	}

}
