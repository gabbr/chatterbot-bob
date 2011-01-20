package it.unibz.lib.bob.check;

/*************************************************************************
 *  Compilation:  javac In.java
 *  Execution:    java In
 *
 *  Reads in data of various types from: stdin, file, URL.
 *
 *  % java In
 *
 *  Remarks
 *  -------
 *    - isEmpty() returns true if there is no more input or
 *      it is all whitespace. This might lead to surprising behavior
 *      when used with readChar()
 *
 *************************************************************************/
import java.net.URLConnection;
import java.net.URL;
import java.net.Socket;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 *
 * @version $Id$
 */
public final class ReadIn
{
  private Scanner scanner;

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
  private Logger log = Logger.getLogger(ReadIn.class);

  // assume Unicode UTF-8 encoding
  //private String charsetName = "UTF-8";
  //private String charsetName = "ISO-8859-1";
  // for stdin

  public ReadIn(String charsetName)
  {
    scanner = new Scanner(System.in, charsetName);
  }

  // for socket
  public ReadIn(String charsetName, Socket socket)
  {
    try
    {
      InputStream is = socket.getInputStream();
      scanner = new Scanner(is, charsetName);
    }
    catch (IOException ioe)
    {
      log.error("Could not open " + socket.toString());
    }
  }

  // for URLs
  public ReadIn(String charsetName, URL url)
  {
    try
    {
      URLConnection site = url.openConnection();
      InputStream is = site.getInputStream();
      scanner = new Scanner(is, charsetName);
    }
    catch (IOException ioe)
    {
      log.error("Could not open " + url);
    }
  }

  // for files and web pages
  public ReadIn(String charsetName, String s)
  {
    try
    {
      // first try to read file from local file system
      File file = new File(s);
      if (file.exists())
      {
        scanner = new Scanner(file, charsetName);
        return;
      }

      // next try for files included in jar
      URL url = getClass().getResource(s);

      // or URL from web
      if (url == null)
      {
        url = new URL(s);
      }

      URLConnection site = url.openConnection();
      InputStream is = site.getInputStream();
      scanner = new Scanner(is, charsetName);
    }
    catch (IOException e)
    {
      log.error("Could not open " + s + ": " + e.getMessage());
    }
  }

  // does input stream exists
  public boolean exists()
  {
    return scanner != null;
  }

  // return true if only whitespace left
  public boolean isEmpty()
  {
    return !scanner.hasNext();
  }

  public boolean hasNextLine()
  {
    boolean value = true;
    try
    {
      value = !scanner.hasNextLine();
    }
    catch (Exception e)
    {
      log.error(e.toString() + ": " + e.getMessage());
    }

    return value;
  }

  // return next line, or null if no such line
  public String readLine()
  {
    String line = null;
    try
    {
      line = scanner.nextLine();
    }
    catch (Exception e)
    {
      log.error(e.toString() + ": " + e.getMessage());
    }
    return line;
  }

  // next character
  public char readChar()
  {
    // (?s) for DOTALL mode so . matches any character, including a line termination character
    // 1 says look only one character ahead
    // consider precompiling the pattern
    String s = scanner.findWithinHorizon("(?s).", 1);

    return s.charAt(0);
  }

  // return rest of input as string
  public String readAll()
  {
    if (!scanner.hasNextLine())
    {
      return null;
    }

    // reference: http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
    return scanner.useDelimiter("\\A").next();
  }

  // next string, int, double, long, byte, boolean
  public String readString()
  {
    return scanner.next();
  }

  public int readInt()
  {
    return scanner.nextInt();
  }

  public double readDouble()
  {
    return scanner.nextDouble();
  }

  public double readFloat()
  {
    return scanner.nextFloat();
  }

  public long readLong()
  {
    return scanner.nextLong();
  }

  public byte readByte()
  {
    return scanner.nextByte();
  }

  // read in a boolean, allowing "true" or "1" for true and "false" or "0" for false
  public boolean readBoolean()
  {
    String s = readString();
    if (s.equalsIgnoreCase("true"))
    {
      return true;
    }
    if (s.equalsIgnoreCase("false"))
    {
      return false;
    }
    if (s.equals("1"))
    {
      return true;
    }
    if (s.equals("0"))
    {
      return false;
    }

    throw new java.util.InputMismatchException();
  }

  // close the scanner
  public void close()
  {
    scanner.close();
  }
}
