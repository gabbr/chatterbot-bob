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

  public boolean understandsLanguage(String lang)
  {
    return dialogueManager.getTT().understandsLanguage(lang);
  }

  /**
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
      return dialogueManager.getTT().getQAMB().getTfIdfIT().numDocuments() > 0; //italian
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

      if (dialogueManager == null)
      {
        log.info("dialogueManager was null. creating a new one.");
        dialogueManager = new DialogueManager(topicTreeFileURL, macrosENFileURL,
                textCorpusENFileURL, macrosDEFileURL, textCorpusDEFileURL,
                macrosITFileURL, textCorpusITFileURL);
      }
      else
      {
        log.info("dialogueManager was not null. reloading topictree.");

        dialogueManager.reloadTT(topicTreeFileURL, macrosENFileURL,
                textCorpusENFileURL, macrosDEFileURL, textCorpusDEFileURL,
                macrosITFileURL, textCorpusITFileURL);
      }

      log.info("New dialogue manager initialized with languages ["
              + ((macrosENFileURL != null) ? "EN " : "")
              + ((macrosDEFileURL != null) ? "DE " : "")
              + ((macrosITFileURL != null) ? "IT " : "" + "] and MachineLearning for ["
              + ((textCorpusENFileURL != null) ? "EN " : "")
              + ((textCorpusDEFileURL != null) ? "DE " : "")
              + ((textCorpusITFileURL != null) ? "IT " : "")) + "]");
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


  public static void main (String[] args) {
      URL urlTt = null;
      URL urlMacrosEN = null;
      URL urlMacrosDE = null;
      URL urlMacrosIT = null;
      URL urlCorpusEN = null;
      URL urlCorpusDE = null;
      URL urlCorpusIT = null;

      try{
        urlTt = new URL("file:/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/Application_Data/topictree.xml");
        urlMacrosEN = new URL("file:/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/Application_Data/bob_macros_EN.txt");
        urlMacrosDE = new URL("file:/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/Application_Data/bob_macros_DE.txt");
        urlMacrosIT = new URL("file:/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/Application_Data/bob_macros_IT.txt");
        urlCorpusEN = new URL("file:/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/TrainingData_AnswerReranker/ukwac/UKWAC-1.txt_sm");
        urlCorpusDE = new URL("file:/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/TrainingData_AnswerReranker/dewac/DEWAC-1.txt_sm");
        urlCorpusIT = new URL("file:/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/TrainingData_AnswerReranker/itwac/ITWAC-1.txt_sm");

      } catch (Exception e) {
          e.printStackTrace();
      };

      Chatterbot bot = new ChatterbotImpl();
      bot.updateChatterbotSettings(urlTt, urlMacrosEN, urlMacrosDE, urlMacrosIT, urlCorpusEN, urlCorpusDE, urlCorpusIT);
      
      System.out.println(bot.getChatterbotAnswer("ich suche ein buch", "DE"));
  }
}
