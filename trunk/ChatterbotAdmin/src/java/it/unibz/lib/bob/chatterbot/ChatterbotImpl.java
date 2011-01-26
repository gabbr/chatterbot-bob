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

  private DialogueManager dialogueManagerEN;

  private DialogueManager dialogueManagerDE;

  private DialogueManager dialogueManagerIT;

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
    dialogueManagerEN = null;
    dialogueManagerDE = null;
    dialogueManagerIT = null;
  }

  @Override
  public void updateChatterbotSettings(String language, URL topicTreeFileURL,
          URL macrosFileURL, URL textCorpusFileURL)
  {
    log.debug("Update chatterbot settings: " + language);

    if (language.equals("DE"))
    {
      if (dialogueManagerDE == null)
      {
        dialogueManagerDE = new DialogueManager(language, topicTreeFileURL,
                macrosFileURL, textCorpusFileURL);

        log.debug("dialogueManagerDE: " + dialogueManagerDE);
      }

      dialogueManager = dialogueManagerDE;
    }
    else if (language.equals("IT"))
    {
      if (dialogueManagerIT == null)
      {
        dialogueManagerIT = new DialogueManager(language, topicTreeFileURL,
                macrosFileURL, textCorpusFileURL);

        log.debug("dialogueManagerIT: " + dialogueManagerIT);
      }

      dialogueManager = dialogueManagerIT;
    }
    else
    {
      if (dialogueManagerEN == null)
      {
        dialogueManagerEN = new DialogueManager(language, topicTreeFileURL,
                macrosFileURL, textCorpusFileURL);

        log.debug("dialogueManagerEN: " + dialogueManagerEN);
      }

      dialogueManager = dialogueManagerEN;
    }

    log.debug("Current dialogue manager: " + dialogueManager);
  }

  @Override
  public String getChatterbotAnswer(String question, String language)
  {
    String answer = new String();

    try
    {
      answer = dialogueManager.getNextResponse(question, language);
    }
    catch (Exception e)
    {
      log.error("Failed to generate answer: " + e.getMessage());
      return null;
    }

    return answer;
  }
}
