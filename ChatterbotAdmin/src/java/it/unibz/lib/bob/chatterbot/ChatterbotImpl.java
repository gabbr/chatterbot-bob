package it.unibz.lib.bob.chatterbot;

import it.unibz.lib.bob.check.DialogueManager;
import java.io.File;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

/**
 *
 * @version $Id$
 */
public class ChatterbotImpl implements Chatterbot
{
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
  private Logger log = Logger.getLogger(ChatterbotImpl.class);

  @Override
  public String chat(String question, String language, String topicTreeFilename,
          String macrosENFilename, String macrosDEFilename,
          String macrosITFilename, String textCorpusENFilename,
          String textCorpusDEFilename, String textCorpusITFilename)
  {

    log.debug("Language set to: " + language);

    String answer = new String();



       String macrosENURLName = null;

    if (macrosENFilename != null)
    {
      try
      {
        macrosENURLName = new File(macrosENFilename).toURL().toString();
      }
      catch (MalformedURLException e)
      {
        log.error("Error: " + e.getMessage());
      }
    }
     String macrosDEURLName = null;

    if (macrosDEFilename != null)
    {
      try
      {
        macrosDEURLName = new File(macrosDEFilename).toURL().toString();
      }
      catch (MalformedURLException e)
      {
        log.error("Error: " + e.getMessage());
      }
    }

      String macrosITURLName = null;

    if (macrosITFilename != null)
    {
      try
      {
        macrosITURLName = new File(macrosITFilename).toURL().toString();
      }
      catch (MalformedURLException e)
      {
        log.error("Error: " + e.getMessage());
      }
    }







    String idfUrlEN = null;

    if (textCorpusENFilename != null)
    {
      try
      {
        idfUrlEN = new File(textCorpusENFilename).toURL().toString();
      }
      catch (MalformedURLException e)
      {
        log.error("Error: " + e.getMessage());
      }
    }

    String idfUrlDE = null;

    if (textCorpusDEFilename != null)
    {
      try
      {
        idfUrlDE = new File(textCorpusDEFilename).toURL().toString();
      }
      catch (MalformedURLException e)
      {
        log.error("Error: " + e.getMessage());
      }
    }

    String idfUrlIT = null;

    if (textCorpusITFilename != null)
    {
      try
      {
        idfUrlIT = new File(textCorpusITFilename).toURL().toString();
      }
      catch (MalformedURLException e)
      {
        log.error("Error: " + e.getMessage());
      }
    }

    DialogueManager dm = null;

    try
    {
      dm = new DialogueManager(topicTreeFilename, macrosENURLName,
              macrosDEURLName, macrosITURLName, idfUrlEN, idfUrlDE,
              idfUrlIT);

      answer = dm.getNextResponse(question, language);
    }
    catch (Exception e)
    {
      log.error("Failed to generate answer."
              + e.getMessage());

      return null;
    }

    return answer;
  }
}
