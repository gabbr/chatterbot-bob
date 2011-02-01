package it.unibz.lib.bob.check;

/**
 * <p>
 * Representation of the features we use to rerank first questions.
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @version $Id$
 */
public class QAFeatures
{
  /**
   * <p>
   * 
   * </p>
   */
  boolean regexmatch;

  /**
   * <p>
   * 
   * </p>
   */
  double regexmatchLen;

  /**
   * <p>
   * 
   * </p>
   */
  double regexmatchLetterLen;

  /**
   * <p>
   * 
   * </p>
   */
  double regexmatchPipeLen;

  /**
   * <p>
   * 
   * </p>
   */
  double regexmatchLetterOverPipe;

  /**
   * <p>
   * 
   * </p>
   */
  double tfIdfSimilarity;

  /**
   * <p>
   * 
   * </p>
   */
  double treeSearchRank;

  /**
   * <p>
   * 
   * </p>
   */
  double nAND;

  /**
   * <p>
   * 
   * </p>
   */
  double nOR;

  /**
   * <p>
   * 
   * </p>
   */
  double nANDoverOR;

  /**
   * <p>
   * 
   * </p>
   */
  int topicID;

  /**
   * <p>
   * 
   * </p>
   */
  double rankOverTopicID;

  /**
   * <p>
   * 
   * </p>
   */
  double treeSearchProminence;

  /**
   * <p>
   * 
   * </p>
   */
  double heuristicScore = 0;
  

  /**
   * <p>
   * The header line for the CSV file, containing one line for each feature
   * </p>
   */
  public static final String csvHeader = "\"regexmatch\","
    + "\"regexmatchLen\"," + "\"regexmatchLetterLen\","
    + "\"regexmatchPipeLen\"," + "\"regexmatchLetterOverPipe\","
    + "\"tfIdfSimilarity\"," + "\"treeSearchRank\","
    + "\"nAND\"," + "\"nOR\"," + "\"nANDoverOR\"," + "\"topicID\","
    + "\"rankOverTopicID\"," + "\"treeSearchProminence\","
    + "\"heuristicAnswerScore\"," + "\"answerCorrectness\"";

  /**
   * <p>
   * 
   * </p>
   * 
   * @param regexmatch
   * @param regexmatchLen
   * @param regexmatchLetterLen
   * @param regexmatchPipeLen
   * @param regexmatchLetterOverPipe
   * @param tfIdfSimilarity
   * @param treeSearchRank
   * @param nAND
   * @param nOR
   * @param nANDoverOR
   * @param topicID
   * @param rankOverTopicID
   * @param treeSearchProminence 
   */
  public QAFeatures(boolean regexmatch, double regexmatchLen,
    double regexmatchLetterLen, double regexmatchPipeLen,
    double regexmatchLetterOverPipe, double tfIdfSimilarity,
    double treeSearchRank, double nAND, double nOR, double nANDoverOR,
    int topicID, double rankOverTopicID, double treeSearchProminence)
  {

    this.regexmatch = regexmatch;
    this.regexmatchLen = regexmatchLen;
    this.regexmatchLetterLen = regexmatchLetterLen;
    this.regexmatchPipeLen = regexmatchPipeLen;
    this.regexmatchLetterOverPipe = regexmatchLetterOverPipe;
    this.tfIdfSimilarity = tfIdfSimilarity;
    this.treeSearchRank = treeSearchRank;
    this.nAND = nAND;
    this.nOR = nOR;
    this.nANDoverOR = nANDoverOR;
    this.topicID = topicID;
    this.rankOverTopicID = rankOverTopicID;
    this.treeSearchProminence = treeSearchProminence;

    heuristicScore = 0;

  }
  

  /**
   * <p>
   * Calculate the value that our heuristic assigns to these features
   * </p>
   * 
   * @return
   */
  private void _calculateValue()
  {
    // TODO
    double val = 0;
    
    // surface
    if (regexmatch)
    {
      val += 2.484;
    }

    val += 1.23 * regexmatchLen;

    heuristicScore = val;
  }

  /**
   * <p>
   * 
   * </p>
   * 
   * @param answerIsCorrect
   * @return one line with the feature values in CSV syntax
   */
  public String getDataCsvString(boolean answerIsCorrect)
  {
    _calculateValue(); // not used for training

    String result = (regexmatch ? "\"1\"," : "\"0\",");
    result += "\"" + String.valueOf(regexmatchLen) + "\",";
    result += "\"" + String.valueOf(regexmatchLetterLen) + "\",";
    result += "\"" + String.valueOf(regexmatchPipeLen) + "\",";
    result += "\"" + String.valueOf(regexmatchLetterOverPipe) + "\",";
    result += "\"" + String.valueOf(tfIdfSimilarity) + "\",";
    result += "\"" + String.valueOf(treeSearchRank) + "\",";

    result += "\"" + String.valueOf(nAND) + "\",";
    result += "\"" + String.valueOf(nOR) + "\",";
    result += "\"" + String.valueOf(nANDoverOR) + "\",";
    result += "\"" + String.valueOf(topicID) + "\",";
    result += "\"" + String.valueOf(rankOverTopicID) + "\",";
    result += "\"" + String.valueOf(treeSearchProminence) + "\",";

    // not used for training
    result += "\"" + String.valueOf(heuristicScore) + "\",";
    // the value we will try to predict
    result += (answerIsCorrect ? "\"1\"" : "\"0\"");
    result += "\n";
    return result;
  }

  /**
   * <p>
   * 
   * </p>
   * 
   * @return the already calculated value of the heuristic score
   */
  public double getValue()
  {
    return heuristicScore;
  }
}
