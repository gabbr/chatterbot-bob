package it.unibz.lib.bob.chatterbot;

import it.unibz.lib.bob.check.DialogueManager;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 *
 * @version $Id$
 */
public class ChatterbotImpl implements Chatterbot
{
  private DialogueManager dialogueManager;

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

  /**
   * <p>
   * </p>
   */
  public ChatterbotImpl()
  {
    dialogueManager = null;
  }

  @Override
  public void updateChatterbotSettings(URL topicTreeFileURL,
          URL macrosENFileURL, URL textCorpusENFileURL, URL macrosDEFileURL,
          URL textCorpusDEFileURL, URL macrosITFileURL, URL textCorpusITFileURL)
  {
    if (dialogueManager == null)
    {
      dialogueManager = new DialogueManager(topicTreeFileURL, macrosENFileURL,
              textCorpusENFileURL, macrosDEFileURL, textCorpusDEFileURL,
              macrosITFileURL, textCorpusITFileURL);

      log.debug("New dialogue manager initialized.");
    }
  }

  @Override
  public String getChatterbotAnswer(String question, String language)
  {
    String answer = new String();

    try
    {
      answer = dialogueManager.getNextResponse(question, language);
      log.debug("Bob's answer has been received: " + answer);
    }
    catch (Exception e)
    {
      log.error("Failed to generate answer: " + e.getMessage());
      return null;
    }

    return answer;
  }
}
