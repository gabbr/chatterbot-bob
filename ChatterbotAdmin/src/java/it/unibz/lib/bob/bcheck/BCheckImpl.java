package it.unibz.lib.bob.bcheck;

import it.unibz.lib.bob.check.DialogueManager;
import it.unibz.lib.bob.check.QAFeatures;

import java.io.File;
import java.net.MalformedURLException;

import com.mallardsoft.tuple.Quadruple;
import com.mallardsoft.tuple.Tuple;

// logging context
import it.unibz.lib.bob.check.QAMatchingBob;
import it.unibz.lib.bob.check.TopicTree;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 * 
 * @version $Id$
 */
public class BCheckImpl implements BCheck
{
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
  private Logger log = Logger.getLogger(BCheckImpl.class);

  @Override
  public String performBCheck(String testQuestionsFilename,
          String topicTreeFilename,
          String macrosENFilename, String macrosDEFilename,
          String macrosITFilename,
          String textCorpusENFilename, String textCorpusDEFilename,
          String textCorpusITFilename,
          String language, Boolean trainingMode)
  {

    String testResults = new String();
    TestQuestionSpreadSheet infile = new TestQuestionSpreadSheet(
            new File(testQuestionsFilename));
    infile.loadSheet();

    ErrorReportSpreadSheet outfile = new ErrorReportSpreadSheet(language,
            !trainingMode && (macrosENFilename != null || macrosENFilename != null
            || macrosENFilename != null));

    ResultFileWriter trainingCsv_out = null;

    if (trainingMode)
    {
      String rfilename = "trainingdata.csv";
      trainingCsv_out = new ResultFileWriter(rfilename);
      trainingCsv_out.println(QAFeatures.csvHeader);
    }

    String idfUrlEN = null;

    if (textCorpusENFilename != null)
    {
      try
      {
        idfUrlEN = new File(textCorpusENFilename).toURL().toString();
      }
      catch (MalformedURLException e)
      {
        log.error("Error: " + e.getMessage());
      }
    }

    String idfUrlDE = null;

    if (textCorpusENFilename != null)
    {
      try
      {
        idfUrlDE = new File(textCorpusENFilename).toURL().toString();
      }
      catch (MalformedURLException e)
      {
        log.error("Error: " + e.getMessage());
      }
    }

    String idfUrlIT = null;

    if (textCorpusENFilename != null)
    {
      try
      {
        idfUrlIT = new File(textCorpusENFilename).toURL().toString();
      }
      catch (MalformedURLException e)
      {
        log.error("Error: " + e.getMessage());
      }
    }

    DialogueManager dm = null;

    try
    {
      dm = new DialogueManager(topicTreeFilename, macrosENFilename,
              macrosDEFilename, macrosITFilename, idfUrlEN, idfUrlDE,
              idfUrlIT);

      // dm.setLanguage(lang.toUpperCase());
    }
    catch (Exception e)
    {
      testResults = testResults  + "\n*****ERROR: Reading TopicTree, Abbreviations Files "
              + "or IDF files: " + e.getMessage() + "\n";

      return testResults;
    }

    int countCorrectMultisol_luckyOrder = 0;
    int countCorrectOnesol = 0;

    int countWrongOnesol = 0;
    int countWrongNoSol = 0;
    int countWrongMultisol_allBad = 0;
    int countWrongMultisol_unluckyOrder = 0;

    int reranked_DtoE = 0;
    int reranked_EtoD = 0;

    // TODO: should we also train on Q/A ?
    /**
     * // determine correct answers, and add them to the
     * ErrorReportSpreadSheet Vector<String> answers = new Vector<String>();
     * for (String i : infile.ids) {
     * answers.add(dm.getAnswerFromTopicID(i)); }
     *
     * if (infile.ids.size() != answers.size()) { System.err
     * .println("ERROR: answers vector length != IDs vector length!"); }
     */
    // QAMatchingBob qamb = null;
    // try {
    // qamb = new QAMatchingBob(new File(ae).toURL(),
    // new File(ad).toURL(), new File(ai).toURL());
    // } catch (MalformedURLException e1) {
    // e1.printStackTrace();
    // }
    // TODO: should we also train on Q/A ?
    // qamb.trainIDF(answers);
    // qamb.trainIDF(infile.questions);
		/*
     * if (!trainingMode && idf != null) { ReadIn readin = new
     * ReadIn("ISO-8859-1", idf); int lcount = 0;
     *
     * results = result +  + "training@@@");
     *
     * for (String line; (line = readin.readLine()) != null;) {
     * qamb.trainIDF(line); lcount++; if (lcount % 10000 == 0) {
     * System.err.print("+");
     *
     * monitor.setCurrent("Training IDF scores: " + lcount / 1000 + "k of "
     * + readIdfLines / 1000 + "k sentences", lcount / 10000); } // stop
     * after x lines of training data if (lcount == readIdfLines) break; }
     *
     * }
     */
    // qamb.printIDFStats();
    try
    {
      if (dm != null)
      {
        for (int i = 0; i < infile.questions.size(); i++)
        {
          testResults = testResults  + "Checking " + (i + 1) + " of "
                  + infile.questions.size() + " questions. \n";

          Timer timer = new Timer();
          Vector<Quadruple<String, String, String, String>> vresp =
                  dm.getAllPossibleNormalResponses(infile.questions.get(i),
                  language.toUpperCase());
          timer.print("Elapsed time for getting all responses from DM for "
                  + i
                  + "th test question ("
                  + infile.questions.get(i) + "): ");

          // do the eval

          // should always have matched "no answer found" pattern
          if (vresp.isEmpty())
          {
            testResults = testResults  + "\n*****ERROR: TopicTree contains no Question "
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
              testResults = testResults  + "WARNING: empty answer found!\n";
            }

            if (dm.topicIDsAreEquivalent(Tuple.get1(bestresp),
                    infile.ids.get(i))
                    || dm.topicIDsAreEquivalent(Tuple.get2(bestresp), infile.ids.get(i)))
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

            if ((textCorpusENFilename != null || textCorpusDEFilename != null
                    || textCorpusITFilename != null) || trainingMode)
            {
              reranked = new Vector<Quadruple<String, String, String, String>>();
            }

            // enter BoB MarLey, the answer reranker
            if (!trainingMode)
            {
              testResults = testResults  + "###\n";

              for (int m = 0; m < vresp.size(); m++)
              {
                testResults = testResults  + "original " + m + " "
                        + Tuple.get1(vresp.get(m)) + "\n";
              }

              if (textCorpusENFilename != null || textCorpusDEFilename != null
                      || textCorpusITFilename != null)
              {
                reranked = dm.getTT().getQAMB().rerankAnswers(
                        infile.questions.get(i), vresp, language);
              }

            }

            Quadruple<String, String, String, String> firstMatch = null;

            if (trainingMode || (textCorpusENFilename == null
                    && textCorpusDEFilename == null && textCorpusITFilename == null))
            {
              firstMatch = vresp.get(0);
            }
            else if (textCorpusENFilename != null || textCorpusDEFilename != null
                    || textCorpusITFilename != null)
            {
              // reranker reverses order of answers :-(
              firstMatch = reranked.get(reranked.size() - 1);
            }

            if (Tuple.get3(firstMatch).equals(""))
            {
              testResults = testResults  + "WARNING: empty answer found!\n";
            }

            if (dm.topicIDsAreEquivalent(Tuple.get1(firstMatch),
                    infile.ids.get(i))
                    || dm.topicIDsAreEquivalent(Tuple.get2(firstMatch), infile.ids.get(i)))
            {

              Vector<String> wronglyMatchedIDs = new Vector<String>();
              // we skip the last match (i.e., the
              // "no pattern matched" topic)
              if ((textCorpusENFilename == null && textCorpusDEFilename == null && textCorpusITFilename != null)
                      || trainingMode)
              {
                for (int k = 0; k < vresp.size() - 1; k++)
                {

                  if (!dm.topicIDsAreEquivalent(Tuple.get1(vresp.get(k)), infile.ids.get(i))
                          && !dm.topicIDsAreEquivalent(Tuple.get1(vresp.get(k)),
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
              else if (textCorpusENFilename != null || textCorpusDEFilename != null
                      || textCorpusITFilename != null)
              {
                // reranker reverses order of answers :-(
                // "no pattern matched" topic already dropped by
                // reranker
                for (int k = reranked.size() - 1; k >= 0; k--)
                {

                  if (!dm.topicIDsAreEquivalent(Tuple.get1(reranked.get(k)), infile.ids.get(i))
                          && !dm.topicIDsAreEquivalent(Tuple.get1(reranked.get(k)),
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

                TopicTree totr = dm.getTT();
                QAMatchingBob quan = totr.getQAMB();

                if (totr == null)
                {
                  testResults = testResults  + "totr == null\n";
                }
                if (quan == null)
                {
                  testResults = testResults  + "quan == null\n";
                }

                trainingCsv_out.append(quan.getTrainingData(
                        infile.questions.get(i), infile.ids.get(i), vresp, language));
                if (textCorpusENFilename != null || textCorpusDEFilename != null)
                {
                  if (!dm.getTT().getQAMB().afterRerankingFirstCorrect(
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

              if ((textCorpusENFilename == null && textCorpusDEFilename == null && textCorpusITFilename == null)
                      || trainingMode)
              {
                for (int k = 0; k < vresp.size() - 1; k++)
                {

                  if (Tuple.get3(vresp.get(k)).equals(""))
                  {
                    testResults = testResults  + "WARNING: empty answer found!\n";
                  }

                  if (dm.topicIDsAreEquivalent(Tuple.get1(vresp.get(k)), infile.ids.get(i))
                          || dm.topicIDsAreEquivalent(Tuple.get2(vresp.get(k)),
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
              else if (textCorpusENFilename != null || textCorpusDEFilename != null
                      || textCorpusITFilename != null)
              { // reranker
                // mode
                // "no pattern matched" topic already dropped by
                // reranker
                for (int k = reranked.size() - 1; k >= 0; k--)
                {
                  if (Tuple.get3(reranked.get(k)).equals(""))
                  {
                    testResults = testResults  + "WARNING: empty answer found!\n";
                  }

                  if (dm.topicIDsAreEquivalent(Tuple.get1(reranked.get(k)), (infile.ids.get(i)))
                          || dm.topicIDsAreEquivalent(Tuple.get2(reranked.get(k)),
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
                  if (textCorpusENFilename != null || textCorpusDEFilename != null
                          || textCorpusITFilename != null)
                  {
                    if (dm.getTT().getQAMB().afterRerankingFirstCorrect(
                            infile.questions.get(i),
                            infile.ids.get(i),
                            vresp, language))
                    {
                      reranked_EtoD++;
                    }
                  }
                  // generate training data from this case
                  trainingCsv_out.append(dm.getTT().getQAMB().getTrainingData(
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
                  trainingCsv_out.append(dm.getTT().getQAMB().getTrainingData(
                          infile.questions.get(i),
                          infile.ids.get(i), vresp,
                          language));
                }
              }
            }
          }
        }
        testResults = testResults  + "\n********************\n";

        if (!trainingMode && (textCorpusENFilename != null
                || textCorpusDEFilename != null || textCorpusITFilename != null))
        {
          testResults = testResults  + "Answer Reranker enabled.\n";
        }
        else
        {
          testResults = testResults  + "Answer Reranker disabled.\n";
        }

        testResults = testResults  + "BoB's answer correctness: "
                + (100 * (.0 + countCorrectOnesol + countCorrectMultisol_luckyOrder) / (.0
                + countWrongNoSol
                + countCorrectOnesol
                + countWrongOnesol
                + countCorrectMultisol_luckyOrder
                + countWrongMultisol_unluckyOrder + countWrongMultisol_allBad))
                + "%\n";

        testResults = testResults  + "********************\n";
        testResults = testResults  + "A - !!! No match: " + countWrongNoSol + "\n";
        testResults = testResults  + "B - Correct match, single solution: "
                + countCorrectOnesol + "\n";
        testResults = testResults  + "C - !!! Wrong match, single solution: "
                + countWrongOnesol + "\n";
        testResults = testResults  + "D - Correct match, many solutions: "
                + countCorrectMultisol_luckyOrder + "\n";
        testResults = testResults  + "E - wrong match, many solutions, including correct one: "
                + countWrongMultisol_unluckyOrder + "\n";
        testResults = testResults  + "F - !!! wrong match, many solutions, all wrong: "
                + countWrongMultisol_allBad + "\n";

        if (trainingMode && (textCorpusENFilename != null
                || textCorpusDEFilename != null || textCorpusITFilename != null))
        {
          testResults = testResults  + "****** How the answer reranker WOULD have done here: \n";
          testResults = testResults  + "D->E - !!! after machine learning reranker: "
                  + reranked_DtoE + "\n";
          testResults = testResults  + "E->D - after machine learning reranker: "
                  + reranked_EtoD + "\n";
          testResults = testResults  + "********************\n";
        }
      }
    }
    finally
    {
      // to ensure that progress dlg is closed in case of any exception
      /*
      if (monitor.getCurrent() != monitor.getTotal())
      {
        monitor.setCurrent(null, monitor.getTotal());
      }
      */

      // generate output files
      outfile.writeSheet();
      testResults = testResults  + "Report .xls generated: " + outfile.getFilename();
      testResults = testResults  + "\nPlease restart checkbob if you want to run a new test!";

      if (trainingMode)
      {
        trainingCsv_out.close();
      }
    }

    return testResults;
  }
}
