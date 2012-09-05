package org.domino.engine.utility.emailreply.demo;
import org.apache.ecs.xhtml.fieldset;
import org.apache.ecs.xhtml.legend;
import org.apache.ecs.xhtml.table;
import org.apache.ecs.xhtml.tbody;
import org.domino.engine.Engine;
import org.domino.engine.utility.DominoObjectHelper;
import org.domino.engine.utility.emailreply.DocumentInfo;
import org.domino.engine.utility.emailreply.EmailHelper;

import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * 
 */

/**
 * @author iceeer
 *
 */
public class TestDocumentInfo extends DocumentInfo {

	/**
	 * @param session
	 */
	public TestDocumentInfo() {
	}
	
	/**
	 * 
	 * 修改记录：
	 */
	public void buildSimpleFieldSet(Document doc,fieldset oFieldSet) throws NotesException{
		String strTotal = doc.getItemValueString("XML2itemCOUNT");
		int iTotal = strTotal.equals("")?0:Integer.parseInt(strTotal);

		legend oLegend = new legend();
		oLegend.addAttribute("align", "center");

		table oTable = new table();
		oTable.addAttribute("align", "left");


		tbody oTBody = new tbody();
		oTable.addElement(oTBody);

		oLegend.addElement("邮件审批测试审批单");
		oFieldSet.addElement(oLegend);

		//oTBody.addElement(Helper.buildOneLineDataTr("起草人：" + doc.getItemValueString("fldOriginAuthor")));
		oTBody.addElement(EmailHelper.buildOneLineDataTr("起草人：" 
				+ DominoObjectHelper.getCNFromField(Engine.getSession(), doc, "fldOriginAuthor")));
		oTBody.addElement(EmailHelper.buildOneLineDataTr("主题：" 
				+ doc.getItemValueString("fldSubject")));
		oTBody.addElement(EmailHelper.buildOneLineDataTr("起草日期：" 
				+ doc.getCreated().getDateOnly()));
		oFieldSet.addElement(oTable);
	}

}
