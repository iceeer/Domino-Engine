/**
 * 
 */
package org.domino.engine.doc;

import java.util.Date;
import java.util.Vector;

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;

import com.ibm.xsp.model.domino.wrapped.DominoDocument;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * 固定域：
 * 静态标识	$ID 编辑文本域 "S" + @Text(@DocumentUniqueID)
 * 创建日期	$Created 编辑日期域 @Created
 * 更新日期 	$Updated 编辑日期多值域
 * 创建人	$CreatedBy 编辑姓名域 @UserName
 * 作者		$AuthorList 编辑作者域
 * 读者		$ReaderList 编辑读者域
 * 删除标记 	$Deleted 文本域，"1"为已删除
 * 文件 Files 富文本域
 * @author iceeer
 *
 */
public class DocHelper {
	
	/**
	 * 默认静态标识域名
	 */
	public static final String DEFAULT_ID_ITEM_NAME = "$ID";
	
	/**
	 * 默认创建日期域名
	 */
	public static final String DEFAULT_CREATED_DATE_ITEM_NAME = "$Created";
	
	/**
	 * 默认更新日期域名
	 */
	public static final String DEFAULT_UPDATED_DATE_ITEM_NAME = "$Updated";
	
	/**
	 * 默认作者域名
	 */
	public static final String DEFAULT_AUTHOR_ITEM_NAME = "$AuthorList";
	
	/**
	 * 默认读者域名
	 */
	public static final String DEFAULT_READER_ITEM_NAME = "$ReaderList";
	
	/**
	 * 默认文件存储域名
	 */
	public static final String DEFAULT_FILES_ITEM_NAME = "Files";
	
	/**
	 * 默认删除标记域名
	 */
	public static final String DEFAULT_DELETED_ITEM_NAME = "$Deleted";
	
	/**
	 * 默认所有文档按标识排序的视图名
	 */
	public static final String ALL_DOCUMENT_BY_ID_VIEW_NAME = "vwAllDocumentByID";
	
	/**
	 * 设置作者
	 * @param doc
	 * @param strAuthor
	 * @return
	 */
	public static boolean setAuthor(Document doc, String strAuthor){
		try {
			doc.removeItem(DEFAULT_AUTHOR_ITEM_NAME);
			return addAuthor(doc, strAuthor);
		} catch (NotesException e) {
			Helper.logError(e);
			return false;
		}
	}
	
	/**
	 * 设置读者
	 * @param doc
	 * @param strReader
	 * @return
	 */
	public static boolean setReader(Document doc, String strReader){
		try {
			doc.removeItem(DEFAULT_READER_ITEM_NAME);
			return addReader(doc, strReader);
		} catch (NotesException e) {
			Helper.logError(e);
			return false;
		}
	}
	
	/**
	 * 设置作者
	 * @param doc
	 * @param strAuthor
	 * @return
	 */
	public static boolean setAuthor(Document doc, Vector v){
		try {
			doc.removeItem(DEFAULT_AUTHOR_ITEM_NAME);
			return addAuthor(doc, v);
		} catch (NotesException e) {
			Helper.logError(e);
			return false;
		}
	}
	
	/**
	 * 设置读者
	 * @param doc
	 * @param strReader
	 * @return
	 */
	public static boolean setReader(Document doc, Vector v){
		try {
			doc.removeItem(DEFAULT_READER_ITEM_NAME);
			return addReader(doc, v);
		} catch (NotesException e) {
			Helper.logError(e);
			return false;
		}
	}
	
	/**
	 * 添加作者
	 * @param doc
	 * @param strAuthor
	 * @return
	 */
	public static boolean addAuthor(Document doc, String strAuthor){
		try {
			Item item = null;
			if(doc.hasItem(DEFAULT_AUTHOR_ITEM_NAME)){
				item = doc.getFirstItem(DEFAULT_AUTHOR_ITEM_NAME);
			}else{
				item = doc.replaceItemValue(DEFAULT_AUTHOR_ITEM_NAME, "");
			}
			item.appendToTextList(strAuthor);
			DominoObjectHelper.setItemAuthors(item);
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 添加读者
	 * @param doc
	 * @param strReader
	 * @return
	 */
	public static boolean addReader(Document doc, String strReader){
		try {
			Item item = null;
			if(doc.hasItem(DEFAULT_READER_ITEM_NAME)){
				item = doc.getFirstItem(DEFAULT_READER_ITEM_NAME);
			}else{
				item = doc.replaceItemValue(DEFAULT_READER_ITEM_NAME, "");
			}
			item.appendToTextList(strReader);
			DominoObjectHelper.setItemReaders(item);
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 添加作者
	 * 
	 * @param doc
	 * @param strAuthor
	 * @return
	 */
	public static boolean addAuthor(Document doc, Vector v) {
		try {
			Item item = null;
			if (doc.hasItem(DEFAULT_AUTHOR_ITEM_NAME)) {
				item = doc.getFirstItem(DEFAULT_AUTHOR_ITEM_NAME);
			} else {
				item = doc.replaceItemValue(DEFAULT_AUTHOR_ITEM_NAME, "");
			}
			item.appendToTextList(v);
			DominoObjectHelper.setItemAuthors(item);
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 添加读者
	 * 
	 * @param doc
	 * @param strReader
	 * @return
	 */
	public static boolean addReader(Document doc, Vector v) {
		try {
			Item item = null;
			if (doc.hasItem(DEFAULT_READER_ITEM_NAME)) {
				item = doc.getFirstItem(DEFAULT_READER_ITEM_NAME);
			} else {
				item = doc.replaceItemValue(DEFAULT_READER_ITEM_NAME, "");
			}
			item.appendToTextList(v);
			DominoObjectHelper.setItemReaders(item);
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 构建通用域
	 * @param doc
	 * @return
	 */
	public static boolean buildCommonField(Session session, DominoDocument doc){
		try {
			doc.replaceItemValue("$ID", DominoObjectHelper.buildStaticID(doc.getDocument()));
			doc.replaceItemValue("$CreatedBy", DominoObjectHelper.getCurrentUserName(session));
			doc.replaceItemValue("$Created", session.createDateTime(new Date()));
		} catch (Exception e) {
			Helper.logError(e);
		}
		return true;
	}
	
	/**
	 * 构建通用域
	 * @param doc
	 * @return
	 */
	public static boolean buildCommonField(Session session, Document doc){
		try {
			doc.replaceItemValue("$ID", DominoObjectHelper.buildStaticID(doc));
			doc.replaceItemValue("$CreatedBy", DominoObjectHelper.getCurrentUserName(session));
			doc.replaceItemValue("$Created", session.createDateTime(new Date()));
		} catch (Exception e) {
			Helper.logError(e);
		}
		return true;
	}
	
	/**
	 * 获得创建人
	 * 
	 * @return
	 */
	public static String getCreator(Session session, Document doc) {
		String strCreator = "";
		try {
			strCreator = doc.getItemValueString("$CreatedBy");
		} catch (Exception e) {
			Helper.logError(e);
		}
		return strCreator;
	}

	/**
	 * 获得创建人简称
	 * 
	 * @return
	 */
	public static String getCreatorCN(Session session, Document doc) {
		String strCreator = "";
		try {
			strCreator = DominoObjectHelper.getCommonNameString(session, getCreator(session, doc));
		} catch (Exception e) {
			Helper.logError(e);
		}
		return strCreator;
	}
	
	/**
	 * 获得创建日期（如2010年6月1日）
	 * @return
	 */
	public static String getCreateDateString(Document doc) {
		String strCreateDate = "    年  月  日";
		try {
			Date dt = DominoObjectHelper.getDateFromField(doc, "$Created");
			strCreateDate = Helper.sdfChineseDate.format(dt);
		} catch (Exception e) {
			Helper.logError(e);//TOOD Why has exception
		}
		return strCreateDate;
	}
	
	/**
	 * 获得创建日期对象
	 * @return
	 */
	public static Date getCreateDate(Document doc) {
		try {
			return DominoObjectHelper.getDateFromField(doc, "$Created");
		} catch (Exception e) {
			Helper.logError(e);//TOOD Why has exception
		}
		return null;
	}
	
	/**
	 * 获得文档标识
	 * 
	 * @return
	 */
	public static String getID(Document doc) {
		String strSubject = "";
		try {
			strSubject = doc.getItemValueString("$ID");
		} catch (Exception e) {
			Helper.logError(e);
		}
		return strSubject;
	}
	
	/**
	 * 更具标识获得文档
	 * @param db
	 * @param strID
	 * @return
	 */
	public static Document getDocumentByID(Database db,String strID){
		try {
			return DominoObjectHelper.getDocumentByKey(db, ALL_DOCUMENT_BY_ID_VIEW_NAME, strID);
		} catch (Exception e) {
			Helper.logError(e);
			return null;
		}
	}
	
	/**
	 * 根据UNID获得文档
	 * @param db
	 * @param strUNID
	 * @return
	 */
	public static Document getDocumentByUNID(Database db,String strUNID){
		try {
			if(Helper.ValidateNotNull(db)){
				return db.getDocumentByUNID(strUNID);
			}else{
				Helper.logMessage("db is null when getDocumentByUNID");
			}
		} catch (Exception e) {
			Helper.logError("document" + strUNID + " is not exist in db " + db);
		}
		return null;
	}
	
	/**
	 * 附加域值
	 * @param doc
	 * @param strFieldName
	 * @param strValue
	 * @return
	 */
	public static boolean addValue(Document doc, String strFieldName,String strValue){
		try {
			Item item = null;
			if(doc.hasItem(strFieldName)){
				item = doc.getFirstItem(strFieldName);
			}else{
				item = doc.replaceItemValue(strFieldName, "");
			}
			item.appendToTextList(strValue);
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
