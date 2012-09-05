/**
 * 
 */
package org.domino.engine.doc;

import java.util.*;

import lotus.domino.Database;
import lotus.domino.Document;

import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.foundation.ExtendDocument;
import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * @author iceeer
 * 
 */
public class DatabaseCollectionSettingDoc extends ExtendDocument {
	Set s = new HashSet();
	/**
	 * 默认构造函数
	 * 
	 * @param session
	 * @param doc
	 */
	public DatabaseCollectionSettingDoc(Document doc) {
		super();
		this.setDoc(doc);
	}
	
	/**
	 * 
	 * @param session
	 * @param strDatabaseCollectionSettingID 默认构造函数
	 */
	public DatabaseCollectionSettingDoc(String strDatabaseCollectionSettingID) {
		//TODO 根据标识获得文档对象
		Document doc = null;
		try {
			doc = Application.getFullSettingDB().getView("vwDatabaseCollectionSetting").getDocumentByKey(strDatabaseCollectionSettingID,true);
		} catch (NotesException e) {
			e.printStackTrace();
		}
		this.setDoc(doc);
	}

	/**
	 * 
	 * @return
	 * @throws NotesException
	 */
	public String getServerName() {
		try {
			return this.getDoc().getItemValueString("ServerName");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws NotesException
	 */
	public List getIncludeDBPathPattern()  {
		List l = new ArrayList();
		try {
			l = this.getDoc().getItemValue("IncludeDBPathPattern");
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	/**
	 * 
	 * @return
	 * @throws NotesException
	 */
	public List getExcludeDBPathPattern()  {
		List l = new ArrayList();
		try {
			l = this.getDoc().getItemValue("ExcludeDBPathPattern");
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return l;
	}

	/**
	 * 
	 * @param strServerName服务器名
	 * @param strIncludeDBPathPattern 包含数据库路径
	 * @param strExcludeDBPathPattern 排除数据库路径
	 * @return
	 */
	public Vector getDBCollection(String strServerName, List strIncludeDBPathPattern, List strExcludeDBPathPattern){
		Vector v = new Vector();//数据库对象集合		
		NSFselect nsf = new NSFselect(Engine.getSessionAsSignerWithFullAccess());
		for(Object o:strIncludeDBPathPattern){
			s.addAll(nsf.selectAll(o.toString()));
		}
		for(Object o:strExcludeDBPathPattern){
			s.removeAll(nsf.selectAll(o.toString()));
		}
		v.addAll(s);
		return v;
	}
	
	/**
	 * 
	 * @return
	 */
	public Vector getDBCollection(){
		return getDBCollection(this.getServerName(), this.getIncludeDBPathPattern(), this.getExcludeDBPathPattern());
	}

	/* （非 Javadoc）
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		return result;
	}

	/* （非 Javadoc）
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DatabaseCollectionSettingDoc))
			return false;
		DatabaseCollectionSettingDoc other = (DatabaseCollectionSettingDoc) obj;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}
	
	
}
