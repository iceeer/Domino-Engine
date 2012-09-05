package org.domino.engine.doc;
import java.io.*;
import java.util.*;

import lotus.domino.*;


public class NSFselect {
	Session session = null;

	public NSFselect() {
		super();
	}

	public NSFselect(Session session) {
		super();
		this.session = session;
	}

	public List selectAll(String path){
		List l = new ArrayList();
		String msg = null;
		int count = 0;
		try{
			String serverName = session.getServerName();			
			if (!"".equals(path)){
				if (path.contains("*")){
					//通配符支持			
					DbDirectory dbdir = session.getDbDirectory(serverName);
					Database db = dbdir.getFirstDatabase(DbDirectory.DATABASE);
					while(db!=null){
						if (db.getFilePath().contains(path.substring(0, path.indexOf("*")))){
							l.add(db.getFilePath());
							count++;
						}
						db = dbdir.getNextDatabase();
					}
				}else if (path.contains(".nsf")||path.contains(".ntf")){
					Database db = session.getDatabase(serverName,path);
					if(db.isOpen()){
						l.add(db.getFilePath());
						count++;
					}
				}else{

				}			
			}
		}catch(NotesException e){
			System.out.print("到出错数-----------"+count);
			e.printStackTrace();
		}
		System.out.print("已加入数-----------"+count);
		return l;
	}

	public List remove(List li,List lr){
		li.removeAll(lr);
		return li;
	}

	/**
	 * 复制文件,无乱码
	 * @param srcPath 原文件路径
	 * @param destPath 目的文件路径
	 */
	public static void copyForFile(String srcPath,String destPath){
		BufferedInputStream br = null;
		BufferedOutputStream bw = null;
		try {
			br =new BufferedInputStream(new FileInputStream(srcPath));
			bw = new BufferedOutputStream(new FileOutputStream(destPath));
			byte[] b = new byte[1024];
			int size = -1;
			//循环读
			while ((size = br.read(b)) != -1) {
				bw.write(b, 0, size);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(br != null){
					br.close();
				}
				if(bw != null){
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteFile(File file){ 
		if(file.exists()){ 
			if(file.isFile()){ 
				file.delete(); 
			}else if(file.isDirectory()){ 
				File files[] = file.listFiles(); 
				for(int i=0;i<files.length;i++){ 
					this.deleteFile(files[i]); 
				} 
			} 
			file.delete(); 
		}else{ 
			System.out.println("所删除的文件不存在！"+'\n'); 
		} 
	} 

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}

