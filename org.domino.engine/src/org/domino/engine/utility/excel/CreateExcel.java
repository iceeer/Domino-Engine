/**
 * 
 */
package org.domino.engine.utility.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import java.io.FileOutputStream;

/**
 * @author iceeer
 * 
 */
public class CreateExcel {
	/** Excel 文件要存放的位置，假定在D盘JTest目录下 */
	public static String outputFile = "D:/gongye.xls";

	public static void main(String argv[]) {
		try {
			// 创建新的Excel 工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 在Excel工作簿中建一工作表，其名为缺省值
			// 如要新建一名为"效益指标"的工作表，其语句为：
			// HSSFSheet sheet = workbook.createSheet("效益指标");
			HSSFSheet sheet = workbook.createSheet();
			
			// 在索引0的位置创建行（最顶端的行）
			HSSFRow row = sheet.createRow((short) 0);
			// 在索引0的位置创建单元格（左上端）
			HSSFCell cell = row.createCell((short) 0);
			// 定义单元格为字符串类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			
			// 创建字体，设置其为红色、粗体：
			HSSFFont font = workbook.createFont();
			font.setColor(HSSFFont.COLOR_RED);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 创建格式
			HSSFCellStyle cellStyle= workbook.createCellStyle();
			cellStyle.setFont(font);
			// 应用格式 
			cell.setCellStyle(cellStyle);
			
			// 在单元格中输入一些内容
			cell.setCellValue("增加值");
			
			// 新建一输出文件流
			FileOutputStream fOut = new FileOutputStream(outputFile);
			// 把相应的Excel 工作簿存盘
			workbook.write(fOut);
			fOut.flush();
			// 操作结束，关闭文件
			fOut.close();
			System.out.println("文件生成...");

		} catch (Exception e) {
			System.out.println("已运行 xlCreate() : " + e);
		}
	}
}
