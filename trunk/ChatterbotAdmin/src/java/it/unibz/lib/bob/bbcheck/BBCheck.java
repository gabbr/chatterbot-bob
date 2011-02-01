package it.unibz.lib.bob.bbcheck;

import java.net.URL;

/**
 * <p>
 * This interface is used to update test settings and to perform test of
 * bbcheck application. When test has finished, test  results are provided
 * as String object and as a test report file in XLS format.
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public interface BBCheck
{
  /**
   * <p>
   * This method is used to update all test settings that are required to
   * perform test of bbcheck application. The bbcheck depends on currently
   * set language setting, therefore a macro files that corresponds to the
   * selected language may not be null. If a text corpus file is set, bbcheck
   * test will perform in machine learning mode. Outfile path is requiresd to
   * store test report file.
   * </p>
   *
   * @param language language setting to perform test
   * @param topicTreeFileURL URL of topic tree file
   * @param macrosENFileURL URL of English macro file
   * @param macrosDEFileURL URL of German macro file
   * @param macrosITFileURL URL of Italian macro file
   * @param textCorpusENFileURL URL of English text corpus file
   * @param textCorpusDEFileURL URL of German text corpus file
   * @param textCorpusITFileURL URL of Italian text corpus file
   * @param testQuestionsFileURL URL of test questions file
   * @param outfilePath path to store test report file on disk
   */
  public void updateBBCheckSettings(String language, URL topicTreeFileURL,
          URL macrosENFileURL, URL macrosDEFileURL, URL macrosITFileURL,
          URL textCorpusENFileURL, URL textCorpusDEFileURL,
          URL textCorpusITFileURL, URL testQuestionsFileURL,
          String outfilePath);

  /**
   * <p>
   * This methods starts bbcheck test after test settings have been updated.
   * When test has finished test results are returned as String object and
   * a test report file is provided to dwonload in XLS format.
   * </p>
   *
   * @return test results of bbcheck test
   */
  public String performBBCheck();

  /**
   * <p>
   * This method is used to return the filename of test report file after
   * bbcheck test has finished.
   * </p>
   *
   * @return filename of test report file
   */
  public String getBBCheckTestReportFile();
}
