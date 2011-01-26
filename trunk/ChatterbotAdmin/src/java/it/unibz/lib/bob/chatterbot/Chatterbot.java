package it.unibz.lib.bob.chatterbot;

import java.net.URL;

/**
 *
 * @version $Id$
 */
public interface Chatterbot
{
  public void updateChatterbotSettings(String language, URL topicTreeFileURL,
          URL macrosFileURL, URL textCorpusFileURL);

  public String getChatterbotAnswer(String question, String language);
}
