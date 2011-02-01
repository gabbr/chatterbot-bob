package it.unibz.lib.bob.chatterbot;

import java.net.URL;

/**
 * <p>
 * This interface is used to update chat settings and to test chatterbot 
 * application.
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public interface Chatterbot
{
  /**
   * <p>
   * This method is used to update all chat settings that are required to
   * test of chatterbot application. The chat depends on currently set language
   * setting, therefore macro and text corpus files that correspond to the
   * selected language may not be null. 
   * </p>
   *
   * @param topicTreeFileURL URL of topic tree file
   * @param macrosENFileURL URL of English macro file
   * @param macrosDEFileURL URL of German macro file
   * @param macrosITFileURL URL of Italian macro file
   * @param textCorpusENFileURL URL of English text corpus file
   * @param textCorpusDEFileURL URL of German text corpus file
   * @param textCorpusITFileURL URL of Italian text corpus file
   */
  public void updateChatterbotSettings(URL topicTreeFileURL, URL macrosENFileURL,
          URL macrosDEFileURL, URL macrosITFileURL, URL textCorpusENFileURL,
          URL textCorpusDEFileURL, URL textCorpusITFileURL);

  /**
   * <p>
   * This method is used to generate an appropriate answer from chatterbot
   * after user question and language has been set.
   * </p>
   *
   * @param question user question
   * @param language language user question is posed
   * @return appropriate answer from chatterbot
   */
  public String getChatterbotAnswer(String question, String language);
}
