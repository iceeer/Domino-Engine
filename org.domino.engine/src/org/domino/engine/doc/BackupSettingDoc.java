/**
 * 
 */
package org.domino.engine.doc;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Document;

import org.domino.engine.Engine;
import org.domino.engine.foundation.ExtendDocument;
import lotus.domino.NotesException;
import lotus.domino.Session;
import org.domino.engine.utility.compress.CompressFile;

/**
 * @author iceeer
 *
 */
public class BackupSettingDoc extends ExtendDocument{

	/**
	 * 默认构造函数
	 */
	public BackupSettingDoc(Document doc) {
		super();
		this.setDoc(doc);
	}

	/**
	 * 获得数据库集合标识
	 * @return 数据库集合标识字符串
	 */
	public String getBackupDBCollectionSettingID(){
		String strBackupDBCollectionSettingID = "";
		try {
			strBackupDBCollectionSettingID = this.getDoc().getItemValueString("BackupDBCollectionSettingID");
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return strBackupDBCollectionSettingID;
	}

	/**
	 * 获得归档路径
	 * @return 归档路径字符串
	 */
	public String getArchiveFilePath(){
		String strArchiveFilePath = "";
		try {
			strArchiveFilePath = this.getDoc().getItemValueString("ArchiveFilePath");
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return strArchiveFilePath;
	}

	/**
	 * SettingName
	 * @return strSettingName
	 */
	public String SettingName(){
		String strSettingName = "";
		try {
			strSettingName = this.getDoc().getItemValueString("SettingName");
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return strSettingName;
	}

	/**
	 * ClearFileAfterZip
	 * @return isClearFileAfterZip
	 */
	public boolean ClearFileAfterZip(){
		boolean isClearFileAfterZip = false;
		try {
			if("yes".equals(this.getDoc().getItemValueString("ClearFileAfterZip")))
				isClearFileAfterZip = true;			;
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return isClearFileAfterZip;
	}

	/**
	 * NeedZip
	 * @return isNeedZip
	 */
	public boolean NeedZip(){
		boolean isNeedZip = false;
		try {
			if("yes".equals(this.getDoc().getItemValueString("NeedZip")))
				isNeedZip = true;		
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return isNeedZip;
	}

	/**
	 * 获得数据库对象集合
	 * @return 数据库对象集合
	 */
	public Vector getDBCollection(){
		DatabaseCollectionSettingDoc oDatabaseCollectionSettingDoc = new DatabaseCollectionSettingDoc(getBackupDBCollectionSettingID());

		return oDatabaseCollectionSettingDoc.getDBCollection();
	}

	/**
	 * 运行备份
	 * @return 运行结果字符串
	 */
	public String runBackup(){
		String strResult ="TODO"; 
		String path = "";
		NSFselect nsf = new NSFselect();
		try {
			strResult = Engine.getSessionAsSignerWithFullAccess().getEnvironmentString("Directory",true);				
		} catch (NotesException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		path = getArchiveFilePath()+"\\"+SettingName();
		File f = new File(path);

		if(!f.exists())
			f.mkdirs();

		for(Object o:getDBCollection()){
			String s = o.toString();
			NSFselect.copyForFile(strResult+"\\"+s, path+"\\"+s.substring(s.lastIndexOf("\\")));

		}
		
		try{
			if(NeedZip()){
				CompressFile bean = CompressFile.getInstance(); 
				bean.zip(f, path+".zip");
				if(ClearFileAfterZip())
					nsf.deleteFile(f);
			}
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}

		return strResult;
	}
}
