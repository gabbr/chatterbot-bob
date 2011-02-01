package it.unibz.lib.bob.bbcheck;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.log4j.Logger;

/**
 * <p>
 *
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @version $Id$
 */
public class TestQuestionSpreadSheet
{
  /**
   * <p>
   *
   * </p>
   */
  InputStream myxls;

  /**
   * <p>
   *
   * </p>
   */
  HSSFWorkbook wb;

  /**
   * <p>
   *
   * </p>
   */
  List<String> questions = new ArrayList<String>();

  /**
   * <p>
   *
   * </p>
   */
  List<String> ids = new ArrayList<String>();

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
  private Logger log = Logger.getLogger(TestQuestionSpreadSheet.class);

  /**
   * <p>
   *
   * </p>
   *
   * @param file
   */
  public TestQuestionSpreadSheet(File file)
  {
    try
    {
      myxls = new FileInputStream(file);
    }
    catch (FileNotFoundException e)
    {
      log.error("Error: " + e.getMessage());
    }
    try
    {
      wb = new HSSFWorkbook(myxls);
    }
    catch (IOException e)
    {
      log.error("Error: " + e.getMessage());
    }

  }

  /**
   * <p>
   *
   * </p>
   *
   */
  public void loadSheet()
  {
    HSSFSheet sheet = wb.getSheetAt(0);

    int rown = 1;
    int goodrows = 0;
    
    log.debug("Loading Test Questions and correct Answer IDs:");

    for (Iterator<HSSFRow> rit = sheet.rowIterator(); rit.hasNext(); rown++)
    {
      if (rown == 1)
      {
        // skip title row
        rit.next(); 
        continue;
      }

      HSSFRow row = rit.next();
      HSSFCell qCell = row.getCell(0);
      HSSFCell idCell = row.getCell(1);

      if (qCell != null && idCell != null
              && qCell.getCellType() == HSSFCell.CELL_TYPE_STRING
              && idCell.getCellType() == HSSFCell.CELL_TYPE_STRING
              && qCell.getRichStringCellValue().length() > 0
              && idCell.getRichStringCellValue().length() > 0)
      {
        goodrows++;
        questions.add(qCell.getRichStringCellValue().getString());
        ids.add(idCell.getRichStringCellValue().getString());

        if (goodrows > 0 && goodrows <= 3)
        {
          log.debug(" " + rown + ": '"
                  + questions.get(goodrows - 1) + "' - "
                  + ids.get(goodrows - 1));
        }
      }
    }

    // don't count title row
    log.debug("Read " + goodrows + " entries.");
  }
}
