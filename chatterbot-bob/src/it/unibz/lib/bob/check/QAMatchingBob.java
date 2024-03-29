package it.unibz.lib.bob.check;

import it.unibz.inf.alice.MyStemmedTokenizerFactory;

import java.net.URL;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Vector;


import com.aliasi.spell.TfIdfDistance;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

import com.mallardsoft.tuple.Quadruple;
import com.mallardsoft.tuple.Tuple;


/**
 * Train and test the answer reranker for first questions.
 * 
 * @author manuelkirschner
 * 
 */
public class QAMatchingBob {

	private TokenizerFactory tokenizerFactory = MyStemmedTokenizerFactory.FACTORY;
	private TfIdfDistance tfIdfEN = new TfIdfDistance(tokenizerFactory);
	private TfIdfDistance tfIdfDE = new TfIdfDistance(tokenizerFactory);
	private TfIdfDistance tfIdfIT = new TfIdfDistance(tokenizerFactory);

	public TfIdfDistance getTfIdfEN() {
		return tfIdfEN;
	}

	public TfIdfDistance getTfIdfDE() {
		return tfIdfDE;
	}

	public TfIdfDistance getTfIdfIT() {
		return tfIdfIT;
	}

	// private String lang;
	// private StellaHelper sh = null;

	/*
	 * public void QAMatchingBob(String lang, File ae, File ad, File ai) { try {
	 * sh = new StellaHelper(ae.toURL(), ad.toURL(), ai.toURL()); } catch
	 * (MalformedURLException e) {
	 * 
	 * e.printStackTrace(); } this.lang = lang; }
	 */

	public QAMatchingBob(URL ae, URL ad, URL ai, URL idftrainingdataEN,
			URL idftrainingdataDE, URL idftrainingdataIT) {
		// this should not do anything, since a static StellaHelper object
		// already exists...
		ChatterbotHelper.makeInstance(ae, ad, ai);
		trainIDF_EN(idftrainingdataEN);
		trainIDF_DE(idftrainingdataDE);
		trainIDF_IT(idftrainingdataIT);
	}

	/**
	 * Print IDF statistics for EN
	 */
	public void printIDFStatsEN() {
		System.err.printf("\n  %18s  %8s  %8s\n", "Term", "Doc Freq", "IDF");
		for (String term : tfIdfEN.termSet())
			System.err.printf("  %18s  %8d  %8.2f\n", term, tfIdfEN
					.docFrequency(term), tfIdfEN.idf(term));
	}

	/**
	 * Train EN IDF scores
	 * 
	 * @param s
	 */
	private void trainIDFsingleDocEN(String s) {
		tfIdfEN.trainIdf(s);
	}

	/**
	 * Train DE IDF scores
	 * 
	 * @param s
	 */
	private void trainIDFsingleDocDE(String s) {
		tfIdfDE.trainIdf(s);
	}

	/**
	 * Train IT IDF scores
	 * 
	 * @param s
	 */
	private void trainIDFsingleDocIT(String s) {
		tfIdfIT.trainIdf(s);
	}

	/**
	 * Trains EN IDF scores from doc
	 * 
	 * @param idftrainingdata
	 */
	void trainIDF_EN(URL idftrainingdata) {
		if (idftrainingdata != null) {
			System.err
					.println("*** Reading in English IDF training data from URL: "
							+ idftrainingdata.toString());

			ReadIn readin = new ReadIn("ISO-8859-1", idftrainingdata);
			int lcount = 0;

			for (String line; (line = readin.readLine()) != null;) {
				trainIDFsingleDocEN(line);
				lcount++;
				if (lcount % 10000 == 0) {
					System.err.print(".");
				}
				if (lcount == 100000)
					break;
			}
		}
	}

	/**
	 * Trains DE IDF scores from doc
	 * 
	 * @param idftrainingdata
	 */
	void trainIDF_DE(URL idftrainingdata) {
		if (idftrainingdata != null) {
			System.err
					.println("*** Reading in German IDF training data from URL: "
							+ idftrainingdata.toString());

			ReadIn readin = new ReadIn("ISO-8859-1", idftrainingdata);
			int lcount = 0;

			for (String line; (line = readin.readLine()) != null;) {
				trainIDFsingleDocDE(line);
				lcount++;
				if (lcount % 10000 == 0) {
					System.err.print(".");
				}
				if (lcount == 100000)
					break;
			}
		}
	}

	/**
	 * Trains IT IDF scores from doc
	 * 
	 * @param idftrainingdata
	 */
	void trainIDF_IT(URL idftrainingdata) {
		if (idftrainingdata != null) {
			System.err
					.println("*** Reading in Italian IDF training data from URL: "
							+ idftrainingdata.toString());

			ReadIn readin = new ReadIn("ISO-8859-1", idftrainingdata);
			int lcount = 0;

			for (String line; (line = readin.readLine()) != null;) {
				trainIDFsingleDocIT(line);
				lcount++;
				if (lcount % 10000 == 0) {
					System.err.print(".");
				}
				if (lcount == 100000)
					break;
			}
		}
	}

	@SuppressWarnings("unused")
	private void trainIDFfromVector(Vector<String> v) {
		for (String s : v) {
			tfIdfEN.trainIdf(s);
		}
		// System.out.printf("\n  %18s  %8s  %8s\n", "Term", "Doc Freq", "IDF");
		// for (String term : tfIdf.termSet())
		// System.out.printf("  %18s  %8d  %8.2f\n", term, tfIdf
		// .docFrequency(term), tfIdf.idf(term));
	}

	/**
	 * Generates the CSV lines with all feature values, needed for R as training
	 * data.
	 * 
	 * @param question
	 * @param correctID
	 * @param vresp
	 *            a vector with the ranked hypotheses (i.e., 4-tuples with
	 *            MatchedTopicID,
	 *            TopicIDAfterLinks,AnswerStringAfterLinks,RegexPattern
	 * 
	 * @param lang
	 * @return
	 */
	public StringBuffer getTrainingData(String question, String correctID,
			Vector<Quadruple<String, String, String, String>> vresp, String lang) {
		StringBuffer result = new StringBuffer();
		result.append("% Q: " + question + "\n");

		// skip the last match (i.e., the
		// "no pattern matched" topic)
		for (int k = 0; k < vresp.size() - 1; k++) {
			result.append("% A-ID: " + Tuple.get1(vresp.get(k)) + "\n");
			result.append("% A: " + Tuple.get3(vresp.get(k)) + "\n");

			String regex = Tuple.get4(vresp.get(k));
			// calculate feature values for this <Q, A> pair, add them to the
			// CSV
			boolean regexmatch = true;
			double regexmatchLen = regex.length();
			double regexmatchLetterLen = countLetters(regex, lang);
			double regexmatchPipeLen = countPipes(regex, lang);
			double regexmatchLetterOverPipe = regexmatchLetterLen
					/ (regexmatchPipeLen + 1);
			double tfIdfSimilarity = -1.0;
			if (lang.toUpperCase().equals("EN")) {
				tfIdfSimilarity = tfIdfEN.proximity(question, Tuple.get3(vresp
						.get(k)));
			} else if (lang.toUpperCase().equals("DE")) {
				tfIdfSimilarity = tfIdfDE.proximity(question, Tuple.get3(vresp
						.get(k)));
			} else if (lang.toUpperCase().equals("IT")) {
				tfIdfSimilarity = tfIdfIT.proximity(question, Tuple.get3(vresp
						.get(k)));
			}
			double treeSearchRank = k;
			// new features
			double nAND = countBooleanAND(regex);
			double nOR = countBooleanOR(regex);
			double nANDoverOR = nAND / (nOR + 1);
			int topicID = getMainTopicID(Tuple.get1(vresp.get(k)));
			double treeSearchProminence = getTreeSearchProminence(vresp.size(),
					k + 1);
			double rankOverTopicID = treeSearchRank / (topicID + 1);

			QAFeatures features = new QAFeatures(regexmatch, regexmatchLen,
					regexmatchLetterLen, regexmatchPipeLen,
					regexmatchLetterOverPipe, tfIdfSimilarity, treeSearchRank,
					nAND, nOR, nANDoverOR, topicID, rankOverTopicID,
					treeSearchProminence);

			boolean answerIsCorrect = correctID
					.equals(Tuple.get1(vresp.get(k)))
					|| correctID.equals(Tuple.get2(vresp.get(k)));

			result.append(features.getDataCsvString(answerIsCorrect));

		}
		return result;
	}

	/**
	 * Calculate total score for an answer candidate, represented by feature
	 * values
	 * 
	 * 
	 * @param lang
	 *            (EN, DE)
	 * @param regexmatchLen
	 * @param tfIdfSimilarity
	 * @param treeSearchRank
	 * @param nANDoverOR
	 * @param treeSearchProminence
	 * @param nOR
	 * @param topicID
	 * @param rankOverTopicID
	 * @return
	 */
	public static double calculateAnswerScore(String lang,
			double regexmatchLen, double tfIdfSimilarity,
			double treeSearchRank, double nANDoverOR,
			double treeSearchProminence, double nOR, double topicID,
			double rankOverTopicID, double nAND) {
		double score = 0;

		if (lang.toUpperCase().equals("EN")) {

			/**
			 * // old values 90.7% score += -0.0008953 * regexmatchLen; score +=
			 * 4.8400637 * tfIdfSimilarity; score += -0.5448473 *
			 * treeSearchRank; score += -0.0811879 * nANDoverOR; score +=
			 * -1.0888564 * treeSearchProminence;
			 **/
			
			/**
			// values trained on 15.05.09
			score += -0.001665 * regexmatchLen;
			score += 6.610790 * tfIdfSimilarity;
			score += -0.496984 * treeSearchRank;
			score += 0.031429 * nOR;
			**/
			
			// values with interaction (topicID) trained on 15.05.09
			
			score +=  -0.002179 * regexmatchLen;
			score += 6.454 * tfIdfSimilarity;
			score += -0.2313 * treeSearchRank;
			score += 0.02476 * nOR;
			score += -0.0239 * topicID;
			score += 0.00006478 * regexmatchLen * topicID;
			score += -0.03222 * topicID * treeSearchRank;
			

		} else if (lang.toUpperCase().equals("DE")) {
			/**
			// german parameters learned 9.2.09
			score += -0.002234 * regexmatchLen;
			score += 5.365298 * tfIdfSimilarity;
			score += 0.523105 * treeSearchRank;
			score += 0.045654 * nAND;
			score += 0.067315 * nOR;
			score += -0.105799 * topicID;
			score += -10.421505 * rankOverTopicID;
			score += 1.341573 * treeSearchProminence;
			**/
			
			/**
			// german parameters learned 15.05.09
			score += -0.001897 * regexmatchLen;
			score += 0.105587 * nOR;
			score +=  7.661679 * tfIdfSimilarity;
			score += -1.882006 * treeSearchRank;
			score +=  0.246798 * nANDoverOR;
			score += -0.081393 * topicID;
			**/
			/**
			// german parameters with interaction learned 15.05.09
			score += -0.0028564 * regexmatchLen;
			score +=  0.0857512 * nOR;
			score +=  7.7976499 * tfIdfSimilarity;
			score += -2.5083120  * treeSearchRank;
			score +=  0.0440263 * nANDoverOR;
			score += -0.2406180 * topicID;
			
			score +=  0.0001262 * regexmatchLen * topicID;
			score += 0.0646147 * topicID * treeSearchRank;
			score += 0.0215565 * topicID * nANDoverOR;
			**/
			// german parameter learned 16.06.09, after reordering TT topics by Ulli
			score += -0.001773 * regexmatchLen;
			score += 0.102951 * nOR;
			score +=  7.534425  * tfIdfSimilarity;
			score += -0.507660   * treeSearchRank;
			score +=    0.229997 * nANDoverOR;
			score += -0.158896* topicID;
					
		} else if (lang.toUpperCase().equals("IT")) {
			// hand-set values 23.04.09

			score += 1 * tfIdfSimilarity;
			score += -10 * treeSearchRank;

		}

		return score;
	}

	/**
	 * ... dropping the last answer candidate (i.e., the "no pattern matched"
	 * one).
	 * 
	 * @param question
	 * @param vresp
	 * @param lang
	 *            (EN, DE)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vector<Quadruple<String, String, String, String>> rerankAnswers(
			String question,
			Vector<Quadruple<String, String, String, String>> vresp, String lang) {

		SortedSet<com.mallardsoft.mytuple.Pair<Double, Quadruple<String, String, String, String>>> rankedAnswerCandidates = new SortedMultiSet();

		// skip the last match (i.e., the
		// "no pattern matched" topic)
		for (int k = 0; k < vresp.size() - 1; k++) {
			String regex = Tuple.get4(vresp.get(k));
			double regexmatchLen = regex.length();

			System.err
					.println("///// " + k + " regexmatchLen " + regexmatchLen);
			double tfIdfSimilarity = -1.0;
			if (lang.toUpperCase().equals("EN")) {
				System.err
						.println("@@@ Number of DOCS used in TFIDF.proximity: "
								+ tfIdfEN.numDocuments());
				tfIdfSimilarity = tfIdfEN.proximity(question, Tuple.get3(vresp
						.get(k)));
			} else if (lang.toUpperCase().equals("DE")) {
				System.err
						.println("@@@ Number of DOCS used in TFIDF.proximity: "
								+ tfIdfDE.numDocuments());
				tfIdfSimilarity = tfIdfDE.proximity(question, Tuple.get3(vresp
						.get(k)));
			} else if (lang.toUpperCase().equals("IT")) {
				System.err
						.println("@@@ Number of DOCS used in TFIDF.proximity: "
								+ tfIdfIT.numDocuments());
				tfIdfSimilarity = tfIdfIT.proximity(question, Tuple.get3(vresp
						.get(k)));
			}

			System.err.println("///// " + k + " tfIdfSimilarity between:\n\t"
					+ question + " && " + Tuple.get3(vresp.get(k)));
			System.err.println("///// " + k + " tfIdfSimilarity "
					+ tfIdfSimilarity);
			double treeSearchRank = k;
			System.err.println("///// " + k + " treeSearchRank "
					+ treeSearchRank);

			double nAND = countBooleanAND(regex);
			System.err.println("///// " + k + " nAND " + nAND);
			double nOR = countBooleanOR(regex);
			System.err.println("///// " + k + " nOR " + nOR);
			double nANDoverOR = nAND / (nOR + 1);
			System.err.println("///// " + k + " nANDoverOR " + nANDoverOR);
			int topicID = getMainTopicID(Tuple.get1(vresp.get(k)));
			System.err.println("///// " + k + " topicID " + topicID);
			double rankOverTopicID = treeSearchRank / (topicID + 1);
			System.err.println("///// " + k + " rankOverTopicID "
					+ rankOverTopicID);
			double treeSearchProminence = getTreeSearchProminence(vresp.size(),
					k + 1);
			System.err.println("///// " + k + " treeSearchProminence "
					+ treeSearchProminence);

			double totalscore = calculateAnswerScore(lang, regexmatchLen,
					tfIdfSimilarity, treeSearchRank, nANDoverOR,
					treeSearchProminence, nOR, topicID, rankOverTopicID, nAND);
			System.err.println("///// " + k + " totalscore " + totalscore);

			com.mallardsoft.mytuple.Pair<Double, Quadruple<String, String, String, String>> p = new com.mallardsoft.mytuple.Pair<Double, Quadruple<String, String, String, String>>(
					totalscore, vresp.get(k));
			rankedAnswerCandidates.add(p);
		}

		Vector<Quadruple<String, String, String, String>> rerankedAnswers = new Vector<Quadruple<String, String, String, String>>();

		Iterator<com.mallardsoft.mytuple.Pair<Double, Quadruple<String, String, String, String>>> it = rankedAnswerCandidates
				.iterator();
		System.err.println("@@@@@");
		while (it.hasNext()) {

			com.mallardsoft.mytuple.Pair<Double, Quadruple<String, String, String, String>> pair = it
					.next();

			Quadruple<String, String, String, String> quad = com.mallardsoft.mytuple.Tuple
					.get2(pair);
			System.err.print("reranked score: "
					+ com.mallardsoft.mytuple.Pair.get1(pair) + "\t"
					+ Tuple.get1(quad) + "\n");
			rerankedAnswers.add(quad);
		}
		return rerankedAnswers;
	}

	/**
	 * 
	 * 
	 * @param question
	 * @param correctID
	 * @param vresp
	 * @param lang
	 *            (EN, DE, IT)
	 * @return
	 */
	public boolean afterRerankingFirstCorrect(String question,
			String correctID,
			Vector<Quadruple<String, String, String, String>> vresp, String lang) {

		Vector<Quadruple<String, String, String, String>> reranked = rerankAnswers(
				question, vresp, lang);

		Quadruple<String, String, String, String> highestScoringAnsw = reranked
				.get(reranked.size() - 1);

		boolean answerIsCorrect = correctID.equals(Tuple
				.get1(highestScoringAnsw))
				|| correctID.equals(Tuple.get2(highestScoringAnsw));

		if (answerIsCorrect) {
			System.err
					.println("afterRerankingFirstCorrect returned good answer on top for Q:"
							+ question + "  --- correctID:" + correctID);
		} else {
			System.err
					.println("afterRerankingFirstCorrect returned BAD answer on top for Q:"
							+ question + "  --- correctID:" + correctID);
		}

		return answerIsCorrect;
	}

	/**
	 * Count the number of letters in a String
	 */
	private int countLetters(String str, String lang) {
		if (str == null)
			return 0;

		String expanded = ChatterbotHelper.replaceMacros(str, lang);
		int counter = 0;
		for (int i = 0; i < expanded.length(); i++) {
			if (Character.isLetter(expanded.charAt(i)))
				counter++;
		}
		return counter;
	}

	/**
	 * Count the number of && in a String
	 */
	private int countBooleanAND(String str) {
		if (str == null)
			return 0;
		int i = 0;
		String subString = "&&";
		for (int index = 0; (index = str.indexOf(subString, index)) != -1; i++) {
			index += subString.length();
		}
		return i;
	}

	/**
	 * Extracts the main topic ID + 1; 0 for errors
	 */
	private int getMainTopicID(String str) {
		if (str == null)
			return 0;
		int i = 0;
		if (str.startsWith("bob.")) {
			try {
				i = Integer.parseInt(str.substring(7, 9)) + 1;
			} catch (NumberFormatException e) {
				;
			}
		}
		return i;
	}

	/**
	 * Tree Search prominence. Rank 1 is always 1, last rank is always 0.
	 * 
	 * @param elements
	 * @param rank
	 *            starts with 1!
	 * @return
	 */
	private double getTreeSearchProminence(double elements, double rank) {
		double incr = 1.0 / (elements - 1.0);
		return (elements - rank) * incr;
	}

	/**
	 * Count the number of || in a String
	 */
	private int countBooleanOR(String str) {
		if (str == null)
			return 0;
		int i = 0;
		String subString = "||";
		for (int index = 0; (index = str.indexOf(subString, index)) != -1; i++) {
			index += subString.length();
		}
		return i;
	}

	/**
	 * Count the number of pipe symbols in a String
	 */
	private int countPipes(String str, String lang) {
		if (str == null)
			return 0;
		int counter = 0;
		String expanded = ChatterbotHelper.replaceMacros(str, lang);
		for (int i = 0; i < expanded.length(); i++) {
			if (expanded.charAt(i) == '|')
				counter++;
		}
		return counter;
	}

	public static void main(String[] args) {

		TokenizerFactory tokenizerFactory = MyStemmedTokenizerFactory.FACTORY;

		TfIdfDistance tfIdf = new TfIdfDistance(tokenizerFactory);
		tfIdf.trainIdf("hello");
		tfIdf.trainIdf("a device");
		tfIdf.trainIdf("lots Of Hope");
		tfIdf.trainIdf("An animal.");
		tfIdf.trainIdf("A man .");
		tfIdf.trainIdf("A CD-ROM.");

		tfIdf.trainIdf("where is The hat?");
		tfIdf.trainIdf("where do I get that?");

		for (String term : tfIdf.termSet()) {
			System.out.println(term);
		}

		System.err.println("@@ tfidf distance = "
				+ tfIdf.distance("eqewq  hope hat",
						"hat  ediewqewwqenburg dewwww "));
		String str2 = "\"PC-related\"";
		// String str2 =
		// "[04]There are <option>10 sections</option> on the CD-ROM: Stories will capture students' interest in plants."
		// ;
		char[] cArray = str2.toCharArray();

		Tokenizer tok = tokenizerFactory.tokenizer(cArray, 0, str2.length());

		Iterator<String> it = tok.iterator();

		while (it.hasNext()) {
			System.out.println(it.next());
		}

	}
}
