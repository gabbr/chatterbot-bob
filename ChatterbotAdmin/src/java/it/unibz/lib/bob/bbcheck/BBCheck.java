package it.unibz.lib.bob.bbcheck;

import java.net.URL;

/**
 *
 * @version $Id$
 */
public interface BBCheck
{
  public void updateBBCheckSettings(String language, URL topicTreeFileURL,
          URL macrosENFileURL, URL macrosDEFileURL, URL macrosITFileURL,
          URL textCorpusENFileURL, URL textCorpusDEFileURL,
          URL textCorpusITFileURL, URL testQuestionsFileURL,
          String outfilePath);

  public String performBBCheck();

  public String getBBCheckTestReportFile();
}
