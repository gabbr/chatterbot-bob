package it.unibz.lib.bob.bcheck;

/**
 *
 * @version $Id$
 */
public interface BCheck
{
  public String performBCheck(String testQuestionsFilename,
          String topicTreeFilename,
          String macrosENFilename, String macrosDEFilename,
          String macrosITFilename,
          String textCorpusENFilename, String textCorpusDEFilename,
          String textCorpusITFilename,
          String language, Boolean trainingMode);
}
