package it.unibz.lib.bob.bbcheck;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class mergeXLS {

	private String[] inputSheets = null;
	private String outputSheet = null;
	private FileOutputStream fileOut = null;

	@SuppressWarnings( { "unchecked", "deprecation" })
	public HSSFWorkbook getMergedWB() {

		InputStream myxls = null;
		HSSFWorkbook wb_in = null;

		HSSFWorkbook wb_out = new HSSFWorkbook();
		HSSFSheet sheet_out = wb_out.createSheet("merged_testQuestions");

		// create title row
		HSSFRow row = sheet_out.createRow((short) 0);
		HSSFCell cell0 = row.createCell((short) 0);
		HSSFCell cell1 = row.createCell((short) 1);
		cell0.setCellValue(new HSSFRichTextString("Test Question"));
		cell1.setCellValue(new HSSFRichTextString("Correct ID"));

		int out_rown = 0;

		for (String file : inputSheets) {
			System.out.println("Reading XLS file " + file);
			try {
				myxls = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				wb_in = new HSSFWorkbook(myxls);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HSSFSheet sheet_in = wb_in.getSheetAt(0);
			int in_rown = 0;
			for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>) sheet_in
					.rowIterator(); rit.hasNext(); in_rown++) {
				// skip title row
				if (in_rown == 0) {
					rit.next();
					continue;
				}
				out_rown++;
				HSSFRow row_in = rit.next();
				HSSFRow row_out = sheet_out.createRow((short) out_rown);
				int celln = 0;
				for (Iterator<HSSFCell> cit = (Iterator<HSSFCell>) row_in
						.cellIterator(); cit.hasNext(); celln++) {
					HSSFCell cell_in = cit.next();
					HSSFCell cell_out = row_out.createCell((short) celln);
					cell_out = row_out.getCell(celln);
					cell_out.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell_out.setCellValue(new HSSFRichTextString(cell_in
							.getStringCellValue()));

				}
			}
		}
		return wb_out;
	}

	public mergeXLS(String[] args) {
		if (args.length < 3) {
			System.err.println("Not enough arguments.");
			System.err.println("Usage: mergeXLS.java targetFile sourceFiles");
			System.exit(1);
		}
		outputSheet = args[0];
		inputSheets = new String[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			inputSheets[i - 1] = args[i];
		}
	}

	public static void main(String[] args) {

		// override args
		System.err.println("Overriding CMD args...");

		args = new String[] {
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/merged_"
						+ ErrorReportSpreadSheet.now() + ".xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_00.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_01.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_02.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_03.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_04.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_05.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_06.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_07.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_08.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_09.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_10.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_11.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_12.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_13.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_15.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_17.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_18.xls",
				"/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_19.xls",
		// "/Users/manuelkirschner/svn_base/libexperts/BoB_Entwicklungsversionen/Tests_BoB/TestQuestions_UK_EN_20.xls"
		};

		if (args.length < 3) {
			System.err
					.println("Usage: mergeXLS <mergedFile.xls> <part1.xls> <part2.xls> ...");
			System.exit(1);
		}

		mergeXLS m = new mergeXLS(args);

		String filename = args[0];

		FileOutputStream fileOut = null;

		HSSFWorkbook wb = m.getMergedWB();

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
