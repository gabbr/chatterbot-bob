package it.unibz.lib.bob.bcheck;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ErrorReportSpreadSheet {

	private String lang = null;
	private HSSFWorkbook wb = new HSSFWorkbook();
	private FileOutputStream fileOut = null;

	private Vector<String> statusCategory = new Vector<String>();
	private Vector<String> testQuestion = new Vector<String>();
	private Vector<String> correctID = new Vector<String>();
	private Vector<String> regexPattern = new Vector<String>();
	private Vector<Vector<String>> wronglyMatchedIDs = new Vector<Vector<String>>();

	private String filename = null;
	private boolean ml;

	public String getFilename() {
		return filename;
	}

	@SuppressWarnings("unused")
	private ErrorReportSpreadSheet() {
		// nop
	}

	/**
	 * 
	 * @param lang
	 *            language
	 * @param ml
	 *            add the ML suffix to the filename?
	 */
	public ErrorReportSpreadSheet(String lang, boolean ml) {
		this.lang = lang;
		this.ml = ml;
	}

	public void addRow(String statusCategory, String testQuestion,
			String correctID, String regexPattern,
			Vector<String> wronglyMatchedIDs) {
		this.statusCategory.add(statusCategory);
		this.testQuestion.add(testQuestion);
		this.correctID.add(correctID);
		this.regexPattern.add(regexPattern);
		this.wronglyMatchedIDs.add(wronglyMatchedIDs);
	}

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd-HHmmss");
		return sdf.format(cal.getTime());
	}

	public void writeSheet() {

		// Header row
		HSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(HSSFColor.CORAL.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// Orange
		HSSFCellStyle styleOrange = wb.createCellStyle();
		styleOrange.setFillForegroundColor(HSSFColor.ORANGE.index);
		styleOrange.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// Green
		HSSFCellStyle styleGreen = wb.createCellStyle();
		styleGreen.setFillForegroundColor(HSSFColor.GREEN.index);
		styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFSheet sheet = wb.createSheet("checkbob");
		HSSFRow row = sheet.createRow((short) 0);

		HSSFCell cell0 = row.createCell((short) 0);
		HSSFCell cell1 = row.createCell((short) 1);
		HSSFCell cell2 = row.createCell((short) 2);
		HSSFCell cell3 = row.createCell((short) 3);
		HSSFCell cell4 = row.createCell((short) 4);
		HSSFCell cell5 = row.createCell((short) 5);
		HSSFCell cell6 = row.createCell((short) 6);
		HSSFCell cell7 = row.createCell((short) 7);
		HSSFCell cell8 = row.createCell((short) 8);
		HSSFCell cell9 = row.createCell((short) 9);
		HSSFCell cell10 = row.createCell((short) 10);
		HSSFCell cell11 = row.createCell((short) 11);

		cell0.setCellValue(new HSSFRichTextString("Test Question"));
		cell1.setCellValue(new HSSFRichTextString("Correct ID"));
		cell2.setCellValue(new HSSFRichTextString("Status"));
		cell3.setCellValue(new HSSFRichTextString("Matched Question Pattern"));
		cell4.setCellValue(new HSSFRichTextString("Wrongly Matched ID 1"));
		cell5.setCellValue(new HSSFRichTextString("Wrongly Matched ID 2"));
		cell6.setCellValue(new HSSFRichTextString("Wrongly Matched ID 3"));
		cell7.setCellValue(new HSSFRichTextString("Wrongly Matched ID 4"));
		cell8.setCellValue(new HSSFRichTextString("Wrongly Matched ID 5"));
		cell9.setCellValue(new HSSFRichTextString("Wrongly Matched ID 6"));
		cell10.setCellValue(new HSSFRichTextString("Wrongly Matched ID 7"));
		cell11.setCellValue(new HSSFRichTextString("Wrongly Matched ID 8"));

		cell0.setCellStyle(style);
		cell1.setCellStyle(style);
		cell2.setCellStyle(style);
		cell3.setCellStyle(style);
		cell4.setCellStyle(style);
		cell5.setCellStyle(style);
		cell6.setCellStyle(style);
		cell7.setCellStyle(style);
		cell8.setCellStyle(style);
		cell9.setCellStyle(style);
		cell10.setCellStyle(style);
		cell11.setCellStyle(style);

		sheet.autoSizeColumn((short) 0); // adjust width of the first column
		sheet.autoSizeColumn((short) 1); // adjust width of the second column
		sheet.autoSizeColumn((short) 2); // adjust width of the second column
		sheet.autoSizeColumn((short) 3); // adjust width of the second column
		sheet.autoSizeColumn((short) 4); // adjust width of the second column
		sheet.autoSizeColumn((short) 5); // adjust width of the second column
		sheet.autoSizeColumn((short) 6); // adjust width of the second column
		sheet.autoSizeColumn((short) 7); // adjust width of the second column
		sheet.autoSizeColumn((short) 8); // adjust width of the second column
		sheet.autoSizeColumn((short) 9); // adjust width of the second column
		sheet.autoSizeColumn((short) 10); // adjust width of the second column
		sheet.autoSizeColumn((short) 11); // adjust width of the second column

		sheet.createFreezePane(0, 1, 0, 1);

		SortedSet<String> seenMainTopics = new TreeSet<String>();

		// spreadsheet body
		for (int i = 0; i < statusCategory.size(); i++) {
			row = sheet.createRow((short) i + 1);
			cell0 = row.createCell((short) 0);
			cell1 = row.createCell((short) 1);
			cell2 = row.createCell((short) 2);
			cell3 = row.createCell((short) 3);
			cell4 = row.createCell((short) 4);
			cell5 = row.createCell((short) 5);
			cell6 = row.createCell((short) 6);
			cell7 = row.createCell((short) 7);
			cell8 = row.createCell((short) 8);
			cell9 = row.createCell((short) 9);
			cell10 = row.createCell((short) 10);
			cell11 = row.createCell((short) 11);

			cell0.setCellValue(new HSSFRichTextString(testQuestion.get(i)));
			cell1.setCellValue(new HSSFRichTextString(correctID.get(i)));
			try {
				seenMainTopics.add(correctID.get(i).substring(7, 9));
			} catch (Exception e) {
				;
			}

			cell2.setCellValue(new HSSFRichTextString(statusCategory.get(i)));
			// color style the status cell
			if (statusCategory.get(i).startsWith("A ")) {
				cell2.setCellStyle(styleOrange);
			} else if (statusCategory.get(i).startsWith("B ")) {
				cell2.setCellStyle(styleGreen);
			} else if (statusCategory.get(i).startsWith("C ")) {
				cell2.setCellStyle(styleOrange);
			} else if (statusCategory.get(i).startsWith("D ")) {
				cell2.setCellStyle(styleGreen);
			}
			// skip E cases (machine learning stuff)
			else if (statusCategory.get(i).startsWith("F ")) {
				cell2.setCellStyle(styleOrange);
			}
			cell3.setCellValue(new HSSFRichTextString(regexPattern.get(i)));

			if (wronglyMatchedIDs.get(i).size() > 0) {

				cell4.setCellValue(new HSSFRichTextString(wronglyMatchedIDs
						.get(i).get(0)));

			}
			if (wronglyMatchedIDs.get(i).size() > 1) {
				cell5.setCellValue(new HSSFRichTextString(wronglyMatchedIDs
						.get(i).get(1)));

			}
			if (wronglyMatchedIDs.get(i).size() > 2)
				cell6.setCellValue(new HSSFRichTextString(wronglyMatchedIDs
						.get(i).get(2)));
			if (wronglyMatchedIDs.get(i).size() > 3)
				cell7.setCellValue(new HSSFRichTextString(wronglyMatchedIDs
						.get(i).get(3)));
			if (wronglyMatchedIDs.get(i).size() > 4)
				cell8.setCellValue(new HSSFRichTextString(wronglyMatchedIDs
						.get(i).get(4)));
			if (wronglyMatchedIDs.get(i).size() > 5)
				cell9.setCellValue(new HSSFRichTextString(wronglyMatchedIDs
						.get(i).get(5)));
			if (wronglyMatchedIDs.get(i).size() > 6)
				cell10.setCellValue(new HSSFRichTextString(wronglyMatchedIDs
						.get(i).get(6)));
			if (wronglyMatchedIDs.get(i).size() > 7)
				cell11.setCellValue(new HSSFRichTextString(wronglyMatchedIDs
						.get(i).get(7)));

		}
		filename = "checkbob_" + lang + "_" + now() + "__";
		for (String id : seenMainTopics) {
			filename += id + "-";
		}
		filename = filename.substring(0, filename.length() - 1);

		if (ml) {
			filename += "__ML";
		}

		filename += ".xls";
		try {
			fileOut = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}