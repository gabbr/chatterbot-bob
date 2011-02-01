package it.unibz.lib.bob.chatterbot;

import it.unibz.lib.bob.check.DialogueManager;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * <p>
 * This class implements Chatterbot interface and is used to update chat
 * settings and to test chatterbot application.
 * </p>
 * 
 * @author manuel.kirschner@gmail.com
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public class ChatterbotImpl implements Chatterbot
{
  /**
   * <p>
   * </p>
   */
  private DialogueManager dialogueManager;

  /**
   * <p>
   * </p>
   *
   * @param lang
   * @return
   */
  public boolean understandsLanguage(String lang)
  {
    return dialogueManager.getTT().understandsLanguage(lang);
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param lang
   * @return true if this TopicTree was initialized with the corresponding
   * text corpus (for machine learning) files for the language lang
   */
  public boolean machineLearningEnabledLanguage(String lang)
  {
    if (lang.toUpperCase().equals("EN"))
    {
      return dialogueManager.getTT().getQAMB().getTfIdfEN().numDocuments() > 0;
    }
    if (lang.toUpperCase().equals("DE"))
    {
      return dialogueManager.getTT().getQAMB().getTfIdfDE().numDocuments() > 0;
    }
    else
    {
      return dialogueManager.getTT().getQAMB().getTfIdfIT().numDocuments() > 0;
    }
  }
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
   * This constructor sets DialogManager object to null when called.
   * </p>
   */
  public ChatterbotImpl()
  {
    dialogueManager = null;
  }

  @Override
  public void updateChatterbotSettings(URL topicTreeFileURL, URL macrosENFileURL,
          URL macrosDEFileURL, URL macrosITFileURL, URL textCorpusENFileURL,
          URL textCorpusDEFileURL, URL textCorpusITFileURL)
  {
    // create a new DialogueManager object if an additional
    // language has been activated in the form
    if (dialogueManager == null
            || macrosENFileURL != null && !dialogueManager.getTT().understandsLanguage("EN")
            || macrosDEFileURL != null && !dialogueManager.getTT().understandsLanguage("DE")
            || macrosITFileURL != null && !dialogueManager.getTT().understandsLanguage("IT")
            || textCorpusENFileURL != null && !dialogueManager.getTT().machineLearningEnabledLanguage("EN")
            || textCorpusDEFileURL != null && !dialogueManager.getTT().machineLearningEnabledLanguage("DE")
            || textCorpusITFileURL != null && !dialogueManager.getTT().machineLearningEnabledLanguage("IT"))
    {

      // check if DialogManager object already exists
      if (dialogueManager == null)
      {
        // DialogManager object does not exist
        log.debug("dialogueManager was null. creating a new one.");

        // create new DialogManager object
        dialogueManager = new DialogueManager(topicTreeFileURL, macrosENFileURL,
                textCorpusENFileURL, macrosDEFileURL, textCorpusDEFileURL,
                macrosITFileURL, textCorpusITFileURL);
      }
      else
      {
        // DialogManager object already exists

        // update existing DialogueManager object
        dialogueManager.reloadTT(topicTreeFileURL, macrosENFileURL,
                textCorpusENFileURL, macrosDEFileURL, textCorpusDEFileURL,
                macrosITFileURL, textCorpusITFileURL);

        // existing DialogueManager object has been updated
        log.debug("Existing DialogueManager object has been updated.");
      }

      // dialogue manager has been initialized
      log.info("New dialogue manager initialized with languages ["
              + ((macrosENFileURL != null) ? "EN " : "")
              + ((macrosDEFileURL != null) ? "DE " : "")
              + ((macrosITFileURL != null) ? "IT " : ""
              + "] and MachineLearning for ["
              + ((textCorpusENFileURL != null) ? "EN " : "")
              + ((textCorpusDEFileURL != null) ? "DE " : "")
              + ((textCorpusITFileURL != null) ? "IT " : ""))
              + "]");
    }

  }

  @Override
  public String getChatterbotAnswer(String question, String language)
  {
    // initialize chatterbot's new answer
    // as an empty String object
    String answer = new String();

    try
    {
      // get answer from chatterbot
      answer = dialogueManager.getNextResponse(question, language);

      // answer has been received
      log.debug("Bob's answer has been received: " + answer);

      // return answer
      return answer;
    }
    catch (Exception e)
    {
      // no answer has been received
      log.error("No answer has been received: " + e.getMessage());

      // report failure
      return null;
    }
  }
}
