/**
 * 
 */
package org.domino.engine.foundation;


import java.util.Iterator;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * 文档扩展类
 * 
 * @author iceeer
 * 
 */
public class ExtendDocument {

	/**
	 * 主文档
	 */
	private Document doc = null;

	/**
	 * 默认构造函数
	 */
	public ExtendDocument() {
	}
	
	/**
	 * 默认构造函数
	 */
	public ExtendDocument(Document doc) {
		this.setDoc(doc);
	}

	/**
	 * 设置文档对象
	 * 
	 * @param doc
	 */
	public void setDoc(Document doc) {
		this.doc = doc;
	}

	/**
	 * 获得文档对象
	 * 
	 * @return 文档对象
	 */
	public Document getDoc() {
		return doc;
	}
	
	/**
	 * 返回文档UNID标识
	 * @return UNID标识
	 * @throws NotesException
	 */
	public String getUniversalID() throws NotesException{
		return doc.getUniversalID();
	}
	
	/**
	 * 
	 * @return
	 */
	public ExtendItem[] getAllExtendItem(){
		try {
			Vector v = doc.getItems();
			ExtendItem[] items = new ExtendItem[v.size()];
			for(int i=0;i<v.size();i++){
				Item item = (Item)v.get(i);
				items[i] = new ExtendItem(item);
			}
			
			//TOOD attachment
			return items;
		} catch (NotesException e) {
			e.printStackTrace();
			return new ExtendItem[0];
		}
		
	}
	
	
	
	

}
