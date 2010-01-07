package it.unibz.lib.bob.bcheck;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

class CsvToXls {
	private static final String FS = "¤";

	/**
	 * Generates temp.xls
	 * 
	 * @param fName
	 */
	@SuppressWarnings("deprecation")
	public static void convert(String fName) {
		System.err.println("Converting CSV->XLS: " + fName);
		ArrayList<ArrayList<String>> arList = null;
		ArrayList<String> al = null;

		String thisLine;
		ReadIn read = new ReadIn("UTF-8", fName);

		int i = 0;
		arList = new ArrayList<ArrayList<String>>();

		while ((thisLine = read.readLine()) != null) {
			al = new ArrayList<String>();
			String strar[] = thisLine.split(FS);
			for (int j = 0; j < strar.length; j++) {
				al.add(strar[j]);
			}
			arList.add(al);
			i++;
		}

		try {
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("new sheet");
			for (int k = 0; k < arList.size(); k++) {
				ArrayList<String> ardata = arList.get(k);

				HSSFRow row = sheet.createRow((short) 0 + k);
				for (int p = 0; p < ardata.size(); p++) {

					HSSFCell cell = row.createCell((short) p);
					cell.setCellValue(ardata.get(p).toString());
				}

			}
			FileOutputStream fileOut = new FileOutputStream("temp.xls");
			hwb.write(fileOut);
			fileOut.close();
			System.err
					.println("Temporary excel file temp.xls has been generated");
		} catch (Exception ex) {
		} // main method ends
	}
}

/**
 * Load an XLS sheet containing annotatable bob logs (output of
 * libexperts/BoB_Entwicklungsversionen/BoB_Logs/processLogfile.sh), and
 * color-code the annotatable cells to make hand-annotation easier.
 * 
 * @author manuelkirschner
 * 
 */
public class AddColorsToAnnotatableXLS {

	private String filename_in = null;

	public AddColorsToAnnotatableXLS(String filename_in) {
		this.filename_in = filename_in;
	}

	@SuppressWarnings( { "unchecked", "deprecation" })
	public HSSFWorkbook getColorCodedWorkbook() {
		InputStream myxls = null;
		HSSFWorkbook wb_in = null;

		System.err.println("Reading XLS file " + filename_in);
		try {
			myxls = new FileInputStream(filename_in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			wb_in = new HSSFWorkbook(myxls);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HSSFWorkbook wb_out = new HSSFWorkbook();
		HSSFSheet sheet_out = wb_out.createSheet("dialogues_to_annotate");

		sheet_out.createFreezePane(0, 1, 0, 1);

		// Blue (header)
		HSSFCellStyle styleBlue = wb_out.createCellStyle();
		styleBlue.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
		styleBlue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// Yellow
		HSSFCellStyle styleYellow = wb_out.createCellStyle();
		styleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		styleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// Orange
		HSSFCellStyle styleOrange = wb_out.createCellStyle();
		styleOrange.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
		styleOrange.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleOrange.setWrapText(true);

		// Green
		HSSFCellStyle styleGreen = wb_out.createCellStyle();
		styleGreen.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleGreen.setWrapText(true);

		// get first sheet
		HSSFSheet sheet_in = wb_in.getSheetAt(0);
		int in_rown = 0;
		String prevRowsStatus = "";
		for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>) sheet_in.rowIterator(); rit
				.hasNext(); in_rown++) {
			// in_rown++;
			HSSFRow row_in = rit.next();
			HSSFRow row_out = sheet_out.createRow((short) in_rown);
			int celln = 0;
			for (Iterator<HSSFCell> cit = (Iterator<HSSFCell>) row_in
					.cellIterator(); cit.hasNext(); celln++) {
				HSSFCell cell_in = cit.next();
				HSSFCell cell_out = row_out.createCell((short) celln);

				if (cell_in.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					cell_out.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell_out.setCellValue(new HSSFRichTextString(cell_in
							.getStringCellValue()));

				} else if (cell_in.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					cell_out.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell_out.setCellValue(cell_in.getNumericCellValue());

				} else {
					System.err.println("ERROR: unexpected cell type at line "
							+ in_rown);
				}
				// style header row
				if (in_rown == 0) {
					if (celln == 0 || celln == 1 || celln == 6 || celln == 7
							|| celln == 8 || celln == 9) {
						cell_out.setCellStyle(styleBlue);
					}
					if (celln == 2) {
						cell_out.setCellStyle(styleOrange);
					}
					if (celln == 3 || celln == 4 || celln == 5) {
						cell_out.setCellStyle(styleGreen);
					}
				}
			}

			// after reading a row of the body, style cells
			if (in_rown > 0 && celln >= 16) {

				// col "G": Status
				if (row_out.getCell(7).getStringCellValue().equals("User")) {
					// col "C": QUESTIONS
					row_out.getCell(2).setCellStyle(styleOrange);
					// col K: uttID
					// XLS converted manually in OpenOffice makes these fields
					// NUMERIC
					if (row_out.getCell(11).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						if (!(row_out.getCell(11).getNumericCellValue() == 1)) {
							// col E: QUESTIONS2
							row_out.getCell(4).setCellStyle(styleGreen);
							// col F: FUQ
							row_out.getCell(5).setCellStyle(styleGreen);
						}
					}
					// CsvToXls makes these fields STRING
					else if (row_out.getCell(11).getCellType() == HSSFCell.CELL_TYPE_STRING) {
						if (!(row_out.getCell(11).getStringCellValue()
								.equals("1"))) {
							// col E: QUESTIONS2
							row_out.getCell(4).setCellStyle(styleGreen);
							// col F: FUQ
							row_out.getCell(5).setCellStyle(styleGreen);
						}
					}
				}
				// col P: QtechnicallyContinuesSD
				// XLS converted manually in OpenOffice makes these fields
				// NUMERIC
				if (row_out.getCell(16).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					if (row_out.getCell(16).getNumericCellValue() == 1) {
						row_out.getCell(2).setCellStyle(styleYellow);
					}
				}
				// CsvToXls makes these fields STRING
				else if (row_out.getCell(16).getCellType() == HSSFCell.CELL_TYPE_STRING) {
					if (row_out.getCell(16).getStringCellValue().equals("1")) {
						row_out.getCell(2).setCellStyle(styleYellow);
					}
				}
				if (in_rown > 2) {
					if (prevRowsStatus.equals("User")) {
						// Col D: ANSWERS
						row_out.getCell(3).setCellStyle(styleGreen);
					}
					prevRowsStatus = row_out.getCell(7).getStringCellValue();
				}
			}
		}
		return wb_out;
	}

	/**
	 * writes color-coded XLS to output file
	 * 
	 * @param args
	 *            : input output
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err
					.println("Usage: AddColorsToAnnotatableXLS <input.xls> <output.xls>");
			System.exit(1);
		}
		AddColorsToAnnotatableXLS a = null;
		if (args[0].endsWith(".csv")) {
			CsvToXls.convert(args[0]);
			a = new AddColorsToAnnotatableXLS("temp.xls");
		} else {
			a = new AddColorsToAnnotatableXLS(args[0]);
		}

		FileOutputStream fileOut = null;
		HSSFWorkbook wb = a.getColorCodedWorkbook();

		try {
			fileOut = new FileOutputStream(args[1]);
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
