/**
 * 
 */
package org.domino.engine.utility.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import org.domino.engine.utility.file.FileHelper;
import org.domino.engine.utility.sign.SignHelper;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * TODO leave domino database
 * @author iceeer
 *
 */
public class PDFHelper {
	public static Font fontChinese = FontFactory.getFont("STSong-Light",
			"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

	public static Font fontChineseBold = FontFactory.getFont("STSong-Light",
			"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED,21,Font.BOLD);
	
	
	public static Font fontChineseEmbedded(){
		registerFontDirectory();

		Font fontChinese = FontFactory.getFont("黑体",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		return fontChinese;
	}
	
	public static Font fontChineseBoldEmbedded(){
		
		
		Font fontChinese = FontFactory.getFont("黑体",BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 21, Font.BOLD);
		return fontChinese;
	}
	
	public static void registerFontDirectory(){
		FontFactory.registerDirectory("C:\\WINDOWS\\Fonts");
//		System.out.println(FontFactory.getRegisteredFonts().size());
//		for(String fontname: FontFactory.getRegisteredFonts())
//		{
//			//System.out.println(fontname + "\n");
//		}
	}
	
	/**
	 * 
	 * @param strCommonName
	 * @return
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public Element buildSignImageElement(String strCommonName, int align) {
		try {
			String strImageFilePath = SignHelper
					.getSignImagePath(strCommonName);
			if (FileHelper.checkFile(strImageFilePath)) {
				Image image;

				image = Image.getInstance(strImageFilePath);

				image.setAlignment(align);
				return image;
			} else {
				Paragraph p = new Paragraph(strCommonName, PDFHelper
						.fontChineseEmbedded());
				p.setAlignment(align);
				return p;
			}
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Paragraph p = new Paragraph("");
		p.setAlignment(align);
		return p;
	}

}
