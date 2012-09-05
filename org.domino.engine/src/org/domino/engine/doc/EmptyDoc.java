/**
 * 
 */
package org.domino.engine.doc;

import java.util.Vector;

import org.domino.engine.foundation.ExtendDocument;


import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * @author iceeer
 *
 */
public class EmptyDoc extends ExtendDocument{

	/**
	 * 默认构造函数
	 */
	public EmptyDoc(Document doc) {
		super(doc);
	}
	
}
