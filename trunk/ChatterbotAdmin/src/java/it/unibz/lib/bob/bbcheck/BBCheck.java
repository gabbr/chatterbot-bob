package it.unibz.lib.bob.bbcheck;

/**
 *
 * @version $Id$
 */
public interface BBCheck
{
  public String performBBCheck(String testQuestionsFilename,
          String topicTreeFilename,
          String macrosENFilename, String macrosDEFilename,
          String macrosITFilename,
          String textCorpusENFilename, String textCorpusDEFilename,
          String textCorpusITFilename,
          String language, Boolean trainingMode);
}
