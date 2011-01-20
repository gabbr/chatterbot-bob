package it.unibz.lib.bob.bbcheck;

/**
 * Write results to ARFF or CSV
 * At some point should provide a nice API for writing correct ARFF files
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 *
 * @author
 * @version $Id$
 */
public class ResultFileWriter
{
  private PrintWriter out;

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
  private Logger log = Logger.getLogger(BBCheckImpl.class);

  public ResultFileWriter(String filename)
  {
    try
    {
      out = new PrintWriter(new FileWriter(filename));
    }
    catch (IOException e)
    {
      log.error("Error: " + e.getMessage());
    }
  }

  public void println(String s)
  {
    out.println(s);
  }

  public void append(StringBuffer sb)
  {
    out.append(sb);
  }

  public void close()
  {
    out.close();
  }
}
