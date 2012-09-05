/**
 * 
 */
package org.domino.engine.utility.convert;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import org.domino.engine.Helper;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author iceeer
 * 
 */
public class HTMLToPDF {

	private String html = "";

	private String out_filename = "temp.pdf";

	private String subject = "";

	private String author = "";

	private String creator = "";

	private String title = "";

	private StyleSheet style = null;

	/**
	 * 
	 */
	public HTMLToPDF() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @return
	 */
	public boolean convert() {
		try {

			com.itextpdf.text.Document document = new com.itextpdf.text.Document(
					PageSize.A4);
			PdfWriter pdfWriter = PdfWriter.getInstance(document,
					new FileOutputStream(this.getOutFileName()));
			document.open();
			document.addAuthor(getAuthor());
			document.addCreator(getCreator());
			document.addSubject(getSubject());
			document.addCreationDate();
			document.addTitle(getTitle());

			HTMLWorker htmlWorker = new HTMLWorker(document);
			// htmlWorker.parse(new StringReader(getHTMLString()));

			HashMap<String, Object> providers = new HashMap<String, Object>();
			providers.put(HTMLWorker.FONT_PROVIDER,
					new ConvertHelper.MyFontFactory());
			providers.put(HTMLWorker.IMG_PROVIDER,
					new ConvertHelper.FullURLImageFactory());

			List<Element> objects = HTMLWorker.parseToList(new StringReader(
					getHTMLString()), this.getStyle(), providers);
			for (Element element : objects) {
				if (element.getClass().getName().endsWith("PdfPTable")) {
					PdfPTable oPdfPTable = (PdfPTable) element;
					Helper.logMessage(oPdfPTable.toString());
				}
				Helper.logMessage(element.getClass().toString());
				document.add(element);
			}

			// document.newPage();//换页

			document.close();

			return true;
		} catch (com.itextpdf.text.DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedPdfException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param html
	 *            the html to set
	 */
	public void setHTMLString(String html) {
		this.html = html;
	}

	/**
	 * @return the html
	 */
	public String getHTMLString() {
		return html;
	}

	/**
	 * @param out_filename
	 *            the out_filename to set
	 */
	public void setOutFileName(String out_filename) {
		this.out_filename = out_filename;
	}

	/**
	 * @return the out_filename
	 */
	public String getOutFileName() {
		return out_filename;
	}

	/**
	 * @param author
	 *            要设置的 author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param subject
	 *            要设置的 subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param title
	 *            要设置的 title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param creator
	 *            要设置的 creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param style
	 *            要设置的 style
	 */
	public void setStyle(StyleSheet style) {
		this.style = style;
	}

	/**
	 * @return style
	 */
	public StyleSheet getStyle() {
		return style;
	}

}
