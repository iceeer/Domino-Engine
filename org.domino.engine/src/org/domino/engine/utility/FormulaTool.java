/**
 * 
 */
package org.domino.engine.utility;

import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.NotesException;

/**
 * @author iceeer
 * 
 */
public class FormulaTool {

	/**
	 * 
	 * @return
	 */
	public static String getFormulaValue(Session session,
			String formula, Document doc) {
		String strValue = "";
		try {
			if (formula.equals("")) {

			} else {
				Vector v;
				if (doc != null) {
					v = session.evaluate(formula, doc);
				} else {
					v = session.evaluate(formula);
				}
				if (v.size() > 0) {
					strValue = v.firstElement().toString();
				}
			}
		} catch (NotesException e) {
			System.out.println("formula:" + formula);
			if (doc != null) {
				try {
					System.out.println("doc parent db path:"
							+ doc.getParentDatabase().getFilePath());
					System.out.println("doc unid:" + doc.getUniversalID());
				} catch (NotesException e1) {
				}
			}
			e.printStackTrace();
		}

		return strValue;
	}

}
