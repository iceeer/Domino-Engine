package org.domino.engine.utility.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

public class FileHelperTest extends TestCase{

	 // 源文件夹   
    static String url1 = "C:\\tmp\\20110531\\1\\";  
    // 目标文件夹   
    static String url2 = "C:\\tmp\\20110530\\2\\";  
	
	@Test
	public void testCheckFolder() {
		String strFolder = "C:\\tmp\\20110531\\1\\";
		
		Assert.assertTrue(FileHelper.checkFolder(strFolder, true));
	}
	
	@Test
	public void testCheckFolder2() {
		String strFolder = "C:\\tmp\\20110530\\2\\";
		
		Assert.assertTrue(FileHelper.checkFolder(strFolder, true));
		
		File f = new File(strFolder);
		Assert.assertTrue(f.exists());
	}
	
	@Test
	public void testCopyFile() {
		
	}
	
	@Test
	public void testCopyDirectiory() throws IOException {
		// 创建目标文件夹   
        (new File(url2)).mkdirs();  
        // 获取源文件夹当前下的文件或目录   
        File[] file = (new File(url1)).listFiles();  
        for (int i = 0; i < file.length; i++) {  
            if (file[i].isFile()) {  
                // 复制文件   
            	FileHelper.copyFile(file[i],new File(url2+file[i].getName()));  
            }  
            if (file[i].isDirectory()) {  
                // 复制目录   
                String sourceDir=url1+File.separator+file[i].getName();  
                String targetDir=url2+File.separator+file[i].getName();  
                FileHelper.copyDirectiory(sourceDir, targetDir);  
            }  
        }  
	}
	
	@Test
	public void testGetFiles() {
		File[] files = FileHelper.getFiles("C:\\", "*");
//		for (int i = 0; i < files.length; i++) {
//			System.out.println(files[i]);
//		}
		Assert.assertTrue(files.length > 0);
	}

}
