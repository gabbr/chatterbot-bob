package it.unibz.lib.bob.bbcheck;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * <p>
 *
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @version $Id$
 */
public class ErrorReportSpreadSheet
{
  /**
   * <p>
   *
   * </p>
   */
  private String lang = null;

  /**
   * <p>
   *
   * </p>
   */
  private HSSFWorkbook wb = new HSSFWorkbook();

  /**
   * <p>
   *
   * </p>
   */
  private FileOutputStream fileOut = null;

  /**
   * <p>
   *
   * </p>
   */
  private Vector<String> statusCategory = new Vector<String>();

  /**
   * <p>
   *
   * </p>
   */
  private Vector<String> testQuestion = new Vector<String>();

  /**
   * <p>
   *
   * </p>
   */
  private Vector<String> correctID = new Vector<String>();

  /**
   * <p>
   *
   * </p>
   */
  private Vector<String> regexPattern = new Vector<String>();

  /**
   * <p>
   *
   * </p>
   */
  private Vector<Vector<String>> wronglyMatchedIDs = new Vector<Vector<String>>();

  /**
   * <p>
   * This String object contains the filename of test report file of bbcheck
   * test.
   * </p>
   */
  private String filename = null;

  /**
   * <p>
   *
   * </p>
   */
  private boolean ml;

  /**
   * <p>
   * Logging of this class uses four different log levels:
   * </p>
   * <ul>
   * <li><b>DEBUG</b> to reproduce complete program flow</li>
   * <li><b>INFO</b> to reproduce system activities</li>
   * <li><b>WARN</b> to reproduce system warnings</li>
   * <li><b>ERROR</b> to reproduce system failures</li>
   * <li><b>FATAL</b> to reproduce fatal system failures</li>
   * </ul>
   * <p>
   * The corresponding <tt>log4j.properties</tt> file is located in the
   * <tt>WEB-INF/classes</tt> directory of this web application.
   * </p>
   */
  private Logger log = Logger.getLogger(ErrorReportSpreadSheet.class);

  /**
   * <p>
   *
   * </p>
   *
   * @param lang
   * @param ml
   */
  public ErrorReportSpreadSheet(String lang, boolean ml)
  {
    this.lang = lang;
    this.ml = ml;
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param statusCategory
   * @param testQuestion
   * @param correctID
   * @param regexPattern
   * @param wronglyMatchedIDs
   */
  public void addRow(String statusCategory, String testQuestion,
          String correctID, String regexPattern,
          Vector<String> wronglyMatchedIDs)
  {
    this.statusCategory.add(statusCategory);
    this.testQuestion.add(testQuestion);
    this.correctID.add(correctID);
    this.regexPattern.add(regexPattern);
    this.wronglyMatchedIDs.add(wronglyMatchedIDs);
  }

  /**
   * <p>
   *
   * </p>
   *
   * @return
   */
  public static String now()
  {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd-HHmmss");
    return sdf.format(cal.getTime());
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param path
   */
  public void writeSheet(String path)
  {

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
    for (int i = 0; i < statusCategory.size(); i++)
    {
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

      // this extracts just the 2-digit main topic numbers
      try
      {
        seenMainTopics.add(correctID.get(i).substring(4, 6));
      }
      catch (Exception e)
      {
        // not implemented yet
      }

      cell2.setCellValue(new HSSFRichTextString(statusCategory.get(i)));
      // color style the status cell
      if (statusCategory.get(i).startsWith("A "))
      {
        cell2.setCellStyle(styleOrange);
      }
      else if (statusCategory.get(i).startsWith("B "))
      {
        cell2.setCellStyle(styleGreen);
      }
      else if (statusCategory.get(i).startsWith("C "))
      {
        cell2.setCellStyle(styleOrange);
      }
      else if (statusCategory.get(i).startsWith("D "))
      {
        cell2.setCellStyle(styleGreen);
      }
      // skip E cases (machine learning stuff)
      else if (statusCategory.get(i).startsWith("F "))
      {
        cell2.setCellStyle(styleOrange);
      }
      cell3.setCellValue(new HSSFRichTextString(regexPattern.get(i)));

      if (wronglyMatchedIDs.get(i).size() > 0)
      {

        cell4.setCellValue(new HSSFRichTextString(wronglyMatchedIDs.get(i).get(0)));

      }
      if (wronglyMatchedIDs.get(i).size() > 1)
      {
        cell5.setCellValue(new HSSFRichTextString(wronglyMatchedIDs.get(i).get(1)));

      }
      if (wronglyMatchedIDs.get(i).size() > 2)
      {
        cell6.setCellValue(new HSSFRichTextString(wronglyMatchedIDs.get(i).get(2)));
      }
      if (wronglyMatchedIDs.get(i).size() > 3)
      {
        cell7.setCellValue(new HSSFRichTextString(wronglyMatchedIDs.get(i).get(3)));
      }
      if (wronglyMatchedIDs.get(i).size() > 4)
      {
        cell8.setCellValue(new HSSFRichTextString(wronglyMatchedIDs.get(i).get(4)));
      }
      if (wronglyMatchedIDs.get(i).size() > 5)
      {
        cell9.setCellValue(new HSSFRichTextString(wronglyMatchedIDs.get(i).get(5)));
      }
      if (wronglyMatchedIDs.get(i).size() > 6)
      {
        cell10.setCellValue(new HSSFRichTextString(wronglyMatchedIDs.get(i).get(6)));
      }
      if (wronglyMatchedIDs.get(i).size() > 7)
      {
        cell11.setCellValue(new HSSFRichTextString(wronglyMatchedIDs.get(i).get(7)));
      }

    }

    filename = "checkbob_" + lang + "_" + now() + "__";

    for (String id : seenMainTopics)
    {
      filename += id + "-";
    }

    filename = filename.substring(0, filename.length() - 1);

    if (ml)
    {
      filename += "__ML";
    }

    filename += ".xls";

    try
    {
      fileOut = new FileOutputStream(path + "/" + filename);
      wb.write(fileOut);
      fileOut.close();
    }
    catch (IOException e)
    {
      log.error("Error: " + e.getMessage());
    }
  }

  /**
   * <p>
   * This method return the filename of test report file of bbcheck test after
   * test has been finished.
   * </p>
   *
   * @return filename of test report file
   */
  public String getFilename()
  {
    return filename;
  }
}
