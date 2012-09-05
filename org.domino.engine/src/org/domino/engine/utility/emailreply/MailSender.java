package org.domino.engine.utility.emailreply;


import org.apache.ecs.xhtml.fieldset;
import org.apache.ecs.xhtml.legend;
import org.domino.engine.Helper;
import org.domino.engine.utility.emailreply.configuration.EmailConfiguration;
import org.domino.engine.utility.emailreply.configuration.MailFormConfiguration;

import lotus.domino.Document;

/**
 * 
 */

/**
 * @author iceeer
 * 
 */
public class MailSender{

	/**
	 * 是否开启调试，显示调试信息
	 */
	private boolean isDebug = false;


	/**
	 * 默认构造函数
	 */
	public MailSender() {
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	public boolean doit(Document doc, Document docRef) {
		return true;
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * 
	 */
	public String getDocContentHTML(Document docEmailCfg, Document docRef) {
		String strHTML = "";
		try {
			EmailConfiguration oEmailConfiguration;
			if(docEmailCfg == null){
				oEmailConfiguration = new EmailConfiguration();
			}else{
				oEmailConfiguration = new EmailConfiguration(docEmailCfg);
			}
			MailFormConfiguration oMailFormConfiguration = oEmailConfiguration.getMailFormConfiguration(docRef);
			
			fieldset oFieldSet = new fieldset();
			legend oLegend = new legend();
			oLegend.addAttribute("align", "center");

			oLegend.addElement(oMailFormConfiguration.getFormTitle(docRef));
			oFieldSet.addElement(oLegend);

			oFieldSet.addElement(oMailFormConfiguration.getFormTableHTML(docRef));
			
			strHTML = oFieldSet.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.isDebug()) {
			Helper.logMessage(strHTML, docRef);
			System.out.println(strHTML);
		}
		return strHTML;
	}

	/**
	 * @param isDebug
	 *            the isDebug to set
	 */
	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	/**
	 * @return the isDebug
	 */
	public boolean isDebug() {
		return isDebug;
	}

}
