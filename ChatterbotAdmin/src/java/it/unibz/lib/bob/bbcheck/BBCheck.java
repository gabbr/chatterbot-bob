package it.unibz.lib.bob.bbcheck;

import java.net.URL;

/**
 *
 * @version $Id$
 */
public interface BBCheck
{
  public String performBBCheck(String language, URL topicTreeFileURL,
          URL macrosENFileURL, URL macrosDEFileURL, URL macrosITFileURL,
          URL textCorpusENFileURL, URL textCorpusDEFileURL,
          URL textCorpusITFileURL, URL testQuestionsFileURL,
          Boolean trainingMode, String outfilePath);

  public String getBBCheckTestReportFile();
}
