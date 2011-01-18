package it.unibz.lib.bob.chatterbot;

/**
 *
 * @version $Id$
 */
public interface Chatterbot
{
  public String chat(String question, String language, String topicTreeFilename,
          String macrosENFilename, String macrosDEFilename,
          String macrosITFilename, String textCorpusENFilename,
          String textCorpusDEFilename, String textCorpusITFilename);
}
