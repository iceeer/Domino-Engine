/**
 * 
 */
package org.domino.engine.utility.convert;

import java.io.IOException;
import java.util.Map;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocListener;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Image;
import com.itextpdf.text.html.simpleparser.ChainedProperties;
import com.itextpdf.text.html.simpleparser.ImageProvider;
import com.itextpdf.text.pdf.BaseFont;

/**
 * @author iceeer
 * 
 */
public class ConvertHelper {
	/**
	 * Inner class implementing the ImageProvider class. This is needed if you
	 * want to resolve the paths to images.
	 */
	public static class FullURLImageFactory implements ImageProvider {
		public Image getImage(String src, Map<String, String> h,
				ChainedProperties cprops, DocListener doc) {
			try {
				return Image.getInstance(src);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	/**
	 * Inner class implementing the ImageProvider class. This is needed if you
	 * want to resolve the paths to images.
	 */
	public static class MyImageFactory implements ImageProvider {
		public Image getImage(String src, Map<String, String> h,
				ChainedProperties cprops, DocListener doc) {
			try {
				return Image.getInstance(String.format("resources/posters/%s",
						src.substring(src.lastIndexOf("/") + 1)));
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * Inner class implementing the FontProvider class. This is needed if you
	 * want to select the correct fonts.
	 */
	public static class MyFontFactory implements FontProvider {
		public Font getFont(String fontname, String encoding, boolean embedded,
				float size, int style, BaseColor color) {
			// return new Font(FontFamily.TIMES_ROMAN, size, style, color);
			/**
			 * 新建一个字体,iText的方法 STSong-Light 是字体，在iTextAsian.jar 中以property为后缀
			 * UniGB-UCS2-H 是编码，在iTextAsian.jar 中以cmap为后缀 H 代表文字版式是 横版， 相应的 V
			 * 代表竖版
			 */
			Font fontChinese = FontFactory.getFont("STSong-Light",
					"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			return fontChinese;
		}

		public boolean isRegistered(String fontname) {
			return false;
		}
	}
}
