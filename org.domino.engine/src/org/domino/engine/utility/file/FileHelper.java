/**
 * 
 */
package org.domino.engine.utility.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lotus.domino.Document;
import lotus.domino.ViewEntryCollection;

import org.domino.engine.Helper;

/**
 * @author iceeer
 * 
 */
public class FileHelper {
	/**
	 * 检查文件夹是否存在，不存在则根据参数创建
	 * 
	 * @param strFolder
	 * @param blCreate
	 * @return
	 */
	public static boolean checkFolder(String strFolder, boolean blCreate) {
		try {
			File f = new File(strFolder);
			if (!f.exists()) {
				if (blCreate) {
					f.mkdirs();
					System.out.println("Directory Created:" + strFolder);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param strFilePath
	 * @return
	 */
	public static boolean checkFile(String strFilePath) {
		try {
			File f = new File(strFilePath);
			if (f.exists()) {
				return true;
			} else {
				Helper.logError("File Is Not Exist:" + strFilePath);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
	}

	// 复制文件
	public static boolean copyFile(File sourceFile, File targetFile) {
		try {
			// 新建文件输入流并对它进行缓冲
			FileInputStream input = new FileInputStream(sourceFile);
			BufferedInputStream inBuff = new BufferedInputStream(input);

			// 新建文件输出流并对它进行缓冲
			FileOutputStream output = new FileOutputStream(targetFile);
			BufferedOutputStream outBuff = new BufferedOutputStream(output);

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();

			// 关闭流
			inBuff.close();
			outBuff.close();
			output.close();
			input.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 复制文件夹
	public static boolean copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}

		return true;
	}


	/**
	 * 创建查询指定目录下文件的方法
	 * 
	 * @param allList
	 *            指定目录
	 * @param endName
	 *            指定以“”结尾的文件
	 * @return 得到的文件数目
	 */
	public int findTxtFileCount(File allList, String endName) {
		//
		int textCount = 0;
		// 创建fileArray名字的数组
		File[] fileArray = allList.listFiles();
		// 如果传进来一个以文件作为对象的allList 返回0
		if (null == fileArray) {
			return 0;
		}
		// 偏历目录下的文件
		for (int i = 0; i < fileArray.length; i++) {
			// 如果是个目录
			if (fileArray[i].isDirectory()) {
				// System.out.println("目录: "+fileArray[i].getAbsolutePath());
				// 递归调用
				textCount += findTxtFileCount(fileArray[i].getAbsoluteFile(),
						endName);
				// 如果是文件
			} else if (fileArray[i].isFile()) {
				// 如果是以“”结尾的文件
				if (fileArray[i].getName().endsWith(endName)) {
					// 展示文件
					System.out.println("exe文件: "
							+ fileArray[i].getAbsolutePath());
					// 所有以“”结尾的文件相加
					textCount++;
				}
			}
		}
		return textCount;

	}

	/**
	 * 在本文件夹下查找
	 * 
	 * @param s
	 *            String 文件名
	 * @return File[] 找到的文件
	 */
	public static File[] getFiles(String s) {
		return getFiles("./", s);
	}

	/**
	 * 获取文件 可以根据正则表达式查找
	 * 
	 * @param dir
	 *            String 文件夹名称
	 * @param s
	 *            String 查找文件名，可带*.?进行模糊查询
	 * @return File[] 找到的文件
	 */
	public static File[] getFiles(String dir, String s) {
		// 开始的文件夹
		File file = new File(dir);

		s = s.replace('.', '#');
		s = s.replaceAll("#", "\\\\.");
		s = s.replace('*', '#');
		s = s.replaceAll("#", ".*");
		s = s.replace('?', '#');
		s = s.replaceAll("#", ".?");
		s = "^" + s + "$";

		System.out.println(s);
		Pattern p = Pattern.compile(s);
		ArrayList list = filePattern(file, p);

		File[] rtn = new File[list.size()];
		list.toArray(rtn);
		return rtn;
	}

	/**
	 * @param file
	 *            File 起始文件夹
	 * @param p
	 *            Pattern 匹配类型
	 * @return ArrayList 其文件夹下的文件夹
	 */

	private static ArrayList filePattern(File file, Pattern p) {
		if (file == null) {
			return null;
		} else if (file.isFile()) {
			Matcher fMatcher = p.matcher(file.getName());
			if (fMatcher.matches()) {
				ArrayList list = new ArrayList();
				list.add(file);
				return list;
			}
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				ArrayList list = new ArrayList();
				for (int i = 0; i < files.length; i++) {
					ArrayList rlist = filePattern(files[i], p);
					if (rlist != null) {
						list.addAll(rlist);
					}
				}
				return list;
			}
		}
		return null;
	}
	
	

	/**
	 * 获得文件修改时间
	 * 
	 * @param file
	 * @return
	 */
	public static Date getModifyDate(File file) {
		return new Date(file.lastModified());
	}

}
