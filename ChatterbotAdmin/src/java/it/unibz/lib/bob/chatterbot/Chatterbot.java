package it.unibz.lib.bob.chatterbot;

import java.net.URL;

/**
 *
 * @version $Id$
 */
public interface Chatterbot
{
  public void updateChatterbotSettings(URL topicTreeFileURL,
          URL macrosENFileURL, URL textCorpusENFileURL, URL macrosDEFileURL,
          URL textCorpusDEFileURL, URL macrosITFileURL, URL textCorpusITFileURL);
 
  public String getChatterbotAnswer(String question, String language);
}
