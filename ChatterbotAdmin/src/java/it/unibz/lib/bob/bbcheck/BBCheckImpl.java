package it.unibz.lib.bob.bbcheck;

import java.io.File;
import java.util.Vector;

import com.mallardsoft.tuple.Quadruple;
import com.mallardsoft.tuple.Tuple;

import it.unibz.lib.bob.check.QAMatchingBob;
import it.unibz.lib.bob.check.TopicTree;
import it.unibz.lib.bob.check.DialogueManager;
import it.unibz.lib.bob.check.QAFeatures;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * <p>
 * This class implements BBCheck interface and is used to update test settings
 * and to perform test of bbcheck application. When test has finished, test
 * results are provided as String object and as a test report file in XLS
 * format.
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public class BBCheckImpl implements BBCheck
{
  /**
   * <p>
   * This String object is used to set language setting for bbcheck test.
   * </p>
   */
  private String language;

  /**
   * <p>
   * This URL object is used to set topic tree file for bbcheck test.
   * </p>
   */
  private URL topicTreeFileURL;

  /**
   * <p>
   * This URL object is used to set English macro file for bbcheck test.
   * </p>
   */
  private URL macrosENFileURL;

  /**
   * <p>
   * This URL object is used to set German macro file for bbcheck test.
   * </p>
   */
  private URL macrosDEFileURL;

  /**
   * <p>
   * This URL object is used to set Italian macro file for bbcheck test.
   * </p>
   */
  private URL macrosITFileURL;

  /**
   * <p>
   * This URL object is used to set English text corpus file for bbcheck test.
   * </p>
   */
  private URL textCorpusENFileURL;

  /**
   * <p>
   * This URL object is used to set German text corpus file for bbcheck test.
   * </p>
   */
  private URL textCorpusDEFileURL;

  /**
   * <p>
   * This URL object is used to set Italian text corpus file for bbcheck test.
   * </p>
   */
  private URL textCorpusITFileURL;

  /**
   * <p>
   * This URL object is used to set test questions file for bbcheck test.
   * </p>
   */
  private URL testQuestionsFileURL;

  /**
   * <p>
   * This String object is used to store test report file on disk.
   * </p>
   */
  private String outfilePath;

  /**
   * <p>
   *
   * </p>
   */
  private Boolean trainingMode;

  /**
   * <p>
   *
   * </p>
   */
  private TestQuestionSpreadSheet infile;

  /**
   * <p>
   *
   * </p>
   */
  private ErrorReportSpreadSheet outfile;

  /**
   * <p>
   *
   * </p>
   */
  private DialogueManager dialogueManager;

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
  private Logger log = Logger.getLogger(BBCheckImpl.class);

  /**
   * <p>
   * This constructor initializes some test setting with null and disables
   * training mode of bbcheck test.
   * </p>
   */
  public BBCheckImpl()
  {
    language = null;
    outfilePath = null;

    // disable training mode permanently
    trainingMode = Boolean.FALSE;
    dialogueManager = null;
  }

  @Override
  public void updateBBCheckSettings(String language, URL topicTreeFileURL,
          URL macrosENFileURL, URL macrosDEFileURL, URL macrosITFileURL,
          URL textCorpusENFileURL, URL textCorpusDEFileURL,
          URL textCorpusITFileURL, URL testQuestionsFileURL,
          String outfilePath)
  {
    this.language = language;
    this.trainingMode = Boolean.FALSE;
    this.outfilePath = outfilePath;

    this.topicTreeFileURL = topicTreeFileURL;
    this.macrosENFileURL = macrosENFileURL;
    this.macrosDEFileURL = macrosDEFileURL;
    this.macrosITFileURL = macrosITFileURL;
    this.textCorpusENFileURL = macrosITFileURL;
    this.textCorpusDEFileURL = textCorpusDEFileURL;
    this.textCorpusITFileURL = textCorpusITFileURL;
    this.testQuestionsFileURL = testQuestionsFileURL;

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

      // check if DialogManager object already exists
      if (dialogueManager == null)
      {
        // DialogManager object does not exist
        log.debug("dialogueManager was null. creating a new one.");

        // create new DialogManager object
        dialogueManager = new DialogueManager(topicTreeFileURL, macrosENFileURL,
                textCorpusENFileURL, macrosDEFileURL, textCorpusDEFileURL,
                macrosITFileURL, textCorpusITFileURL);
      }
      else
      {
        // DialogManager object already exists

        // update existing DialogueManager object
        dialogueManager.reloadTT(topicTreeFileURL, macrosENFileURL,
                textCorpusENFileURL, macrosDEFileURL, textCorpusDEFileURL,
                macrosITFileURL, textCorpusITFileURL);

        // existing DialogueManager object has been updated
        log.debug("Existing DialogueManager object has been updated.");
      }
      
      // dialogue manager has been initialized
      log.info("New dialogue manager initialized with languages ["
              + ((macrosENFileURL != null) ? "EN " : "")
              + ((macrosDEFileURL != null) ? "DE " : "")
              + ((macrosITFileURL != null) ? "IT " : ""
              + "] and MachineLearning for ["
              + ((textCorpusENFileURL != null) ? "EN " : "")
              + ((textCorpusDEFileURL != null) ? "DE " : "")
              + ((textCorpusITFileURL != null) ? "IT " : ""))
              + "]");
    }
  }

  @Override
  public String performBBCheck()
  {
    // initialize String object to collect test results
    String testResults = new String();

    infile = new TestQuestionSpreadSheet(new File(testQuestionsFileURL.getFile()));
    infile.loadSheet();

    outfile = new ErrorReportSpreadSheet(language,
            !trainingMode && (macrosENFileURL != null || macrosDEFileURL != null
            || macrosITFileURL != null));

    ResultFileWriter trainingCsv_out = null;

    if (trainingMode)
    {
      String rFileURL = "trainingdata.csv";
      trainingCsv_out = new ResultFileWriter(rFileURL);
      trainingCsv_out.println(QAFeatures.csvHeader);
    }

    int countCorrectMultisol_luckyOrder = 0;
    int countCorrectOnesol = 0;

    int countWrongOnesol = 0;
    int countWrongNoSol = 0;
    int countWrongMultisol_allBad = 0;
    int countWrongMultisol_unluckyOrder = 0;

    int reranked_DtoE = 0;
    int reranked_EtoD = 0;


    for (int i = 0; i < infile.questions.size(); i++)
    {
      Vector<Quadruple<String, String, String, String>> vresp =
              dialogueManager.getAllPossibleNormalResponses(infile.questions.get(i),
              language.toUpperCase());

      // do the eval

      // should always have matched "no answer found" pattern
      if (vresp.isEmpty())
      {
        testResults = testResults + "\n*****ERROR: TopicTree contains no Question "
                + "Pattern for 'No answer found'!\n";
      }

      // no Match
      if (vresp.size() == 1)
      {
        outfile.addRow("A - no match", infile.questions.get(i),
                infile.ids.get(i), "", new Vector<String>());
        countWrongNoSol++;
      }

      // Unique solution
      if (vresp.size() == 2)
      {
        Quadruple<String, String, String, String> bestresp = vresp.get(0);
        // topicID match

        if (Tuple.get3(bestresp).equals(""))
        {
          testResults = testResults + "WARNING: empty answer found!\n";
        }

        if (dialogueManager.topicIDsAreEquivalent(Tuple.get1(bestresp),
                infile.ids.get(i))
                || dialogueManager.topicIDsAreEquivalent(Tuple.get2(bestresp),
                infile.ids.get(i)))
        {
          outfile.addRow(
                  "B - correct match, single solution",
                  infile.questions.get(i), infile.ids.get(i),
                  Tuple.get4(bestresp), new Vector<String>());
          countCorrectOnesol++;
        }
        else
        {
          Vector<String> v = new Vector<String>();
          v.add(Tuple.get1(bestresp));

          outfile.addRow("C - wrong match, single solution",
                  infile.questions.get(i), infile.ids.get(i),
                  Tuple.get4(bestresp), v);

          countWrongOnesol++;
        }
      }

      // Multi solutions; these cases can be used in
      // compiling the R training data csv
      if (vresp.size() > 2)
      {

        // use reranker?
        Vector<Quadruple<String, String, String, String>> reranked = null;

        if ((textCorpusENFileURL != null || textCorpusDEFileURL != null
                || textCorpusITFileURL != null) || trainingMode)
        {
          reranked = new Vector<Quadruple<String, String, String, String>>();
        }

        // enter BoB MarLey, the answer reranker
        if (!trainingMode)
        {
          /**
          testResults = testResults + "###\n";

          for (int m = 0; m < vresp.size(); m++)
          {
          testResults = testResults + "original " + m + " "
          + Tuple.get1(vresp.get(m)) + "\n";
          }
           **/
          if (textCorpusENFileURL != null || textCorpusDEFileURL != null
                  || textCorpusITFileURL != null)
          {
            reranked = dialogueManager.getTT().getQAMB().rerankAnswers(
                    infile.questions.get(i), vresp, language);
          }

        }

        Quadruple<String, String, String, String> firstMatch = null;

        if (trainingMode || (textCorpusENFileURL == null
                && textCorpusDEFileURL == null && textCorpusITFileURL == null))
        {
          firstMatch = vresp.get(0);
        }
        else if (textCorpusENFileURL != null || textCorpusDEFileURL != null
                || textCorpusITFileURL != null)
        {
          // reranker reverses order of answers :-(
          firstMatch = reranked.get(reranked.size() - 1);
        }

        if (Tuple.get3(firstMatch).equals(""))
        {
          testResults = testResults + "WARNING: empty answer found!\n";
        }

        if (dialogueManager.topicIDsAreEquivalent(Tuple.get1(firstMatch),
                infile.ids.get(i))
                || dialogueManager.topicIDsAreEquivalent(Tuple.get2(firstMatch),
                infile.ids.get(i)))
        {

          Vector<String> wronglyMatchedIDs = new Vector<String>();
          // we skip the last match (i.e., the
          // "no pattern matched" topic)
          if ((textCorpusENFileURL == null && textCorpusDEFileURL == null
                  && textCorpusITFileURL != null) || trainingMode)
          {
            for (int k = 0; k < vresp.size() - 1; k++)
            {

              if (!dialogueManager.topicIDsAreEquivalent(Tuple.get1(vresp.get(k)),
                      infile.ids.get(i))
                      && !dialogueManager.topicIDsAreEquivalent(Tuple.get1(vresp.get(k)),
                      infile.ids.get(i)))
              {
                wronglyMatchedIDs.add(Tuple.get1(vresp.get(k)));
              }
              else
              {
                wronglyMatchedIDs.add("[Correct ID ranks here]");
              }
            }
          }
          else if (textCorpusENFileURL != null || textCorpusDEFileURL != null
                  || textCorpusITFileURL != null)
          {
            // reranker reverses order of answers :-(
            // "no pattern matched" topic already dropped by
            // reranker
            for (int k = reranked.size() - 1; k >= 0; k--)
            {

              if (!dialogueManager.topicIDsAreEquivalent(Tuple.get1(reranked.get(k)), infile.ids.get(i))
                      && !dialogueManager.topicIDsAreEquivalent(Tuple.get1(reranked.get(k)),
                      infile.ids.get(i)))
              {
                wronglyMatchedIDs.add(Tuple.get1(reranked.get(k)));
              }
              else
              {
                wronglyMatchedIDs.add("[Correct ID ranks here]");
              }
            }
          }
          outfile.addRow("D - correct match, many solutions",
                  infile.questions.get(i), infile.ids.get(i),
                  Tuple.get4(firstMatch), wronglyMatchedIDs);
          countCorrectMultisol_luckyOrder++;

          if (trainingMode)
          {
            // generate training data from this case

            TopicTree totr = dialogueManager.getTT();
            QAMatchingBob quan = totr.getQAMB();

            if (totr == null)
            {
              testResults = testResults + "totr == null\n";
            }
            if (quan == null)
            {
              testResults = testResults + "quan == null\n";
            }

            trainingCsv_out.append(quan.getTrainingData(
                    infile.questions.get(i), infile.ids.get(i), vresp, language));
            if (textCorpusENFileURL != null || textCorpusDEFileURL != null)
            {
              if (!dialogueManager.getTT().getQAMB().afterRerankingFirstCorrect(
                      infile.questions.get(i),
                      infile.ids.get(i), vresp,
                      language))
              {
                reranked_DtoE++;
              }
            }
          }
        }
        else
        { // first match is wrong. Now,
          // check if correct
          // one is among other solutions
          int rankOfCorrectAnswer = -1;
          Vector<String> wrongIDs = new Vector<String>();

          if ((textCorpusENFileURL == null && textCorpusDEFileURL == null && textCorpusITFileURL == null)
                  || trainingMode)
          {
            for (int k = 0; k < vresp.size() - 1; k++)
            {

              if (Tuple.get3(vresp.get(k)).equals(""))
              {
                testResults = testResults + "WARNING: empty answer found!\n";
              }

              if (dialogueManager.topicIDsAreEquivalent(Tuple.get1(vresp.get(k)), infile.ids.get(i))
                      || dialogueManager.topicIDsAreEquivalent(Tuple.get2(vresp.get(k)),
                      (infile.ids.get(i))))
              {
                rankOfCorrectAnswer = k;
                wrongIDs.add("[Correct ID ranks here]");
              }
              else
              {
                // add wrong answer ID to vector
                wrongIDs.add(Tuple.get1(vresp.get(k)));
              }
            }
          }
          else if (textCorpusENFileURL != null || textCorpusDEFileURL != null
                  || textCorpusITFileURL != null)
          { // reranker
            // mode
            // "no pattern matched" topic already dropped by
            // reranker
            for (int k = reranked.size() - 1; k >= 0; k--)
            {
              if (Tuple.get3(reranked.get(k)).equals(""))
              {
                testResults = testResults + "WARNING: empty answer found!\n";
              }

              if (dialogueManager.topicIDsAreEquivalent(Tuple.get1(reranked.get(k)), (infile.ids.get(i)))
                      || dialogueManager.topicIDsAreEquivalent(Tuple.get2(reranked.get(k)),
                      (infile.ids.get(i))))
              {
                rankOfCorrectAnswer = k;
                wrongIDs.add("[Correct ID ranks here]");
              }
              else
              {

                // add wrong answer ID to vector
                wrongIDs.add(Tuple.get1(reranked.get(k)));
              }
            }

          }
          if (rankOfCorrectAnswer >= 0)
          {
            outfile.addRow(
                    "E - wrong match, many solutions, including correct one",
                    infile.questions.get(i),
                    infile.ids.get(i), Tuple.get4(firstMatch),
                    wrongIDs);
            countWrongMultisol_unluckyOrder++;

            if (trainingMode)
            {
              if (textCorpusENFileURL != null || textCorpusDEFileURL != null
                      || textCorpusITFileURL != null)
              {
                if (dialogueManager.getTT().getQAMB().afterRerankingFirstCorrect(
                        infile.questions.get(i),
                        infile.ids.get(i),
                        vresp, language))
                {
                  reranked_EtoD++;
                }
              }
              // generate training data from this case
              trainingCsv_out.append(dialogueManager.getTT().getQAMB().getTrainingData(
                      infile.questions.get(i),
                      infile.ids.get(i), vresp,
                      language));
            }
          }
          else
          {
            outfile.addRow(
                    "F - wrong match, many solutions, all wrong",
                    infile.questions.get(i),
                    infile.ids.get(i), Tuple.get4(firstMatch),
                    wrongIDs);
            countWrongMultisol_allBad++;

            if (trainingMode)
            {

              // generate training data from this case
              trainingCsv_out.append(dialogueManager.getTT().getQAMB().getTrainingData(
                      infile.questions.get(i),
                      infile.ids.get(i), vresp,
                      language));
            }
          }
        }
      }
    }

    if (!trainingMode && (textCorpusENFileURL != null
            || textCorpusDEFileURL != null || textCorpusITFileURL != null))
    {
      testResults = testResults + "Answer Reranker enabled.\n";
    }
    else
    {
      testResults = testResults + "Answer Reranker disabled.\n";
    }

    testResults = testResults + "BoB's answer correctness: "
            + (100 * (.0 + countCorrectOnesol + countCorrectMultisol_luckyOrder) / (.0
            + countWrongNoSol
            + countCorrectOnesol
            + countWrongOnesol
            + countCorrectMultisol_luckyOrder
            + countWrongMultisol_unluckyOrder + countWrongMultisol_allBad))
            + "%\n";

    testResults = testResults + "********************\n";
    testResults = testResults + "A - !!! No match: " + countWrongNoSol + "\n";
    testResults = testResults + "B - Correct match, single solution: "
            + countCorrectOnesol + "\n";
    testResults = testResults + "C - !!! Wrong match, single solution: "
            + countWrongOnesol + "\n";
    testResults = testResults + "D - Correct match, many solutions: "
            + countCorrectMultisol_luckyOrder + "\n";
    testResults = testResults + "E - wrong match, many solutions, including correct one: "
            + countWrongMultisol_unluckyOrder + "\n";
    testResults = testResults + "F - !!! wrong match, many solutions, all wrong: "
            + countWrongMultisol_allBad + "\n";

    if (trainingMode && (textCorpusENFileURL != null
            || textCorpusDEFileURL != null || textCorpusITFileURL != null))
    {
      testResults = testResults + "****** How the answer reranker WOULD have done here: \n";
      testResults = testResults + "D->E - !!! after machine learning reranker: "
              + reranked_DtoE + "\n";
      testResults = testResults + "E->D - after machine learning reranker: "
              + reranked_EtoD + "\n";
      testResults = testResults + "********************\n";
    }

    // generate output files
    outfile.writeSheet(outfilePath);
    testResults = testResults + "Report .xls generated: " + outfile.getFilename();

    if (trainingMode)
    {
      trainingCsv_out.close();
    }

    return testResults;
  }

  @Override
  public String getBBCheckTestReportFile()
  {
    return outfile.getFilename();
  }
}
