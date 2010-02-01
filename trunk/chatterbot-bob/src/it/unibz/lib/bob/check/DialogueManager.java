package it.unibz.lib.bob.check;


import it.CustomLog;

import java.io.File;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mallardsoft.tuple.Quadruple;

public class DialogueManager {

	// switch for log4j logging (used for debug output window)
	private boolean logging;

	/**
	 * URL of the topic tree file
	 */
	// private static String urlTopicTree =
	// "https://babbage.inf.unibz.it/krdb/libexperts/BoB_Entwicklungsversionen/ulrike/topictree.xml"
	// ;
	private static String urlTopicTreeStr = "empty";

	/**
	 * URL of the EN abbreviation file
	 */
	private static String urlAbbrevFileENStr = "empty";

	/**
	 * URL of the DE abbreviation file
	 */
	private static String urlAbbrevFileDEStr = "empty";

	/**
	 * URL of the IT abbreviation file
	 */
	private static String urlAbbrevFileITStr = "empty";

	/**
	 * URL of EN IDF training data
	 */
	private static String urlIDFtrainingDataENStr = "empty";

	/**
	 * URL of DE IDF training data
	 */
	private static String urlIDFtrainingDataDEStr = "empty";

	/**
	 * URL of IT IDF training data
	 */
	private static String urlIDFtrainingDataITStr = "empty";

	/**
	 * TopicTree provides most of the tree searching logic
	 */
	private TopicTree tt = null;

	/**
	 * The current topic
	 */
	private Node C = null;

	/**
	 * Indicates if we're in a SubDialogue; this is set only by getNextNode()
	 */
	private boolean sdMode = false;

	/**
	 * Count cont. errors to handle them at some threshold
	 */
	int continuousNoPatternFoundErrors = 0;

	/**
	 * The Java Session ID that this DM is connected to; needed for logging
	 */
	private String sessionID = "foobarSessionID";

	/**
	 * Non-Webapp Constructor: disables logging, takes all needed files directly
	 * as parameters
	 * 
	 * @param tts
	 *            Path to TopicTree
	 * @param ae
	 *            Path to EN abbrev file
	 * @param ad
	 *            Path to DE abbrev file
	 * @param ai
	 *            Path to IT abbrev file
	 * @param idfEN
	 *            String of URL to EN IDF training data
	 * @param idfDE
	 *            String of URL to DE IDF training data
	 * @param idfIT
	 *            String of URL to IT IDF training data
	 */
	public DialogueManager(String tts, String ae, String ad, String ai,
			String idfEN, String idfDE, String idfIT) {
		logging = false;
		// clumsy way of converting from file path to URL string
		try {
			urlTopicTreeStr = new File(tts).toURL().toString();
			urlAbbrevFileENStr = new File(ae).toURL().toString();
			urlAbbrevFileDEStr = new File(ad).toURL().toString();
			urlAbbrevFileITStr = new File(ai).toURL().toString();
			urlIDFtrainingDataENStr = idfEN;
			urlIDFtrainingDataDEStr = idfDE;
			urlIDFtrainingDataITStr = idfIT;

		} catch (MalformedURLException e1) {
			System.err.println("Could not convert File to URL string.");
			e1.printStackTrace();
		}
		URL urlIDFtrainingDataEN = null;
		if (urlIDFtrainingDataENStr != null) {
			try {
				urlIDFtrainingDataEN = new URL(urlIDFtrainingDataENStr);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		URL urlIDFtrainingDataDE = null;
		if (urlIDFtrainingDataDEStr != null) {
			try {
				urlIDFtrainingDataDE = new URL(urlIDFtrainingDataDEStr);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		URL urlIDFtrainingDataIT = null;
		if (urlIDFtrainingDataITStr != null) {
			try {
				urlIDFtrainingDataIT = new URL(urlIDFtrainingDataITStr);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		try {
			tt = TopicTree.getInstance(new URL(urlTopicTreeStr), new URL(
					urlAbbrevFileENStr), new URL(urlAbbrevFileDEStr), new URL(
					urlAbbrevFileITStr), urlIDFtrainingDataEN,
					urlIDFtrainingDataDE, urlIDFtrainingDataIT);
			// tt = new TopicTree(new URL(urlTopicTree), new
			// URL(urlAbbrevFileEN), new URL(urlAbbrevFileDE), new
			// URL(urlAbbrevFileIT));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		C = tt.getRootNode();
	}

	/**
	 * WebApp BoB constructor: topicTree and abbrev files have to be specified
	 * via WEB-INF/web.xml; logging is enabled for debugging output
	 */
	public DialogueManager() {
		logging = true;
		Authenticator.setDefault(new MyAuthenticator());
		URL urlIDFtrainingDataEN = null;
		if (urlIDFtrainingDataENStr != null) {
			try {
				urlIDFtrainingDataEN = new URL(urlIDFtrainingDataENStr);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		URL urlIDFtrainingDataDE = null;
		if (urlIDFtrainingDataDEStr != null) {
			try {
				urlIDFtrainingDataDE = new URL(urlIDFtrainingDataDEStr);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		URL urlIDFtrainingDataIT = null;
		if (urlIDFtrainingDataITStr != null) {
			try {
				urlIDFtrainingDataIT = new URL(urlIDFtrainingDataITStr);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		try {
			// tt = new TopicTree(new URL(urlTopicTree), new
			// URL(urlAbbrevFileEN),
			// new URL(urlAbbrevFileDE), new URL(urlAbbrevFileIT));
			tt = TopicTree.getInstance(new URL(urlTopicTreeStr), new URL(
					urlAbbrevFileENStr), new URL(urlAbbrevFileDEStr), new URL(
					urlAbbrevFileITStr), urlIDFtrainingDataEN,
					urlIDFtrainingDataDE, urlIDFtrainingDataIT);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		C = tt.getRootNode();
		System.out.println(sessionID
				+ "[DialogueManager] New DialogueManager() created.");
	}

	/**
	 * Test application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DialogueManager dm = new DialogueManager(
				"foobar",
				"foobar",
				"foobar",
				"foobar",
				"file:///foobar",
				"file:///foobar",
				"file:///foobar");
		dm.setSessionID("someIDsetInMain");

		// System.out.println(dm.getNextResponse("hello BoB", "EN"));
		// System.out.println(dm.getAllPossibleNormalResponses("hello BoB",
		// "EN"));

		// System.out.println(dm.getAnswerFromTopicID("someTopicID", "EN",
		// true));

		System.out.println(dm.topicIDsAreEquivalent("12", "12"));
		System.out.println(dm.topicIDsAreEquivalent("bob.bob",
				"bob.bob"));

		System.out.println(dm.topicIDsAreEquivalent(
				"bob.04_RECHERCHE.01_WIE_FINDE_ICH.46_Tests", "foobar"));
		System.out.println(dm.topicIDsAreEquivalent(
				"bob.04_RECHERCHE.01_WIE_FINDE_ICH.46_Tests",
				"bob.04_RECHERCHE.01_WIE_FINDE_ICH.46_Tests"));

		System.out.println(dm.topicIDsAreEquivalent("foobar", "foobar"));
		System.out.println(dm.topicIDsAreEquivalent(
				"bob.04_RECHERCHE.01_WIE_FINDE_ICH.46_Tests",
				"bob.04_RECHERCHE.01_WIE_FINDE_ICH"));

		/*
		 * try { BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
		 * new FileOutputStream("dialogueManager_output.utf8.txt"), "UTF8"));
		 * out.write(dm.getNextResponse("aha")); out.close(); } catch
		 * (UnsupportedEncodingException ue) { System.err.println("Not supported
		 * : "); } catch (IOException e) { System.err.println(e.getMessage()); }
		 */
	}

	public void setSessionID(String id) {
		sessionID = id;
	}

	/**
	 * Entry point from getChat.jsp. Gets the next system response from BoB.
	 * Sets the current focus node attribute (C), and the sdMode attribute.
	 * Removes accents from user question.
	 * 
	 * @param userUtt
	 * @return a String with the system response (or with an error message)
	 */
	public String getNextResponse(String userUtt, String lang) {
		if (!(lang.toUpperCase().equals("EN")
				|| lang.toUpperCase().equals("DE") || lang.toUpperCase()
				.equals("IT"))) {
			System.err.println("ERROR in getNextResponse(): illegal language: "
					+ lang + "!");
		}
		// setLanguage(lang);
		String response = null;
		C = getNextNode(removeAccents(userUtt), lang);
		response = tt.getNodeAnswer(C, lang, sessionID, true);
		if (C == null) {
			C = tt.getRootNode();
			sdMode = false;
			tt.reset();
		}
		return response;
	}

	/**
	 * Remove umlauts and accents (preserving upper/lower case, just in case)
	 * 
	 * @param s
	 * @return
	 */
	public static String removeAccents(String s) {
		// simplify umlauts/special chars
		s = s.replaceAll("ä", "ae");
		s = s.replaceAll("ö", "oe");
		s = s.replaceAll("ü", "ue");
		s = s.replaceAll("ß", "ss");
		s = s.replaceAll("æ", "ae");

		s = s.replaceAll("Ä", "AE");
		s = s.replaceAll("Ö", "OE");
		s = s.replaceAll("Ü", "UE");
		s = s.replaceAll("Æ", "AE");

		// remove accents
		s = s.replaceAll("[àáâãå]", "a");
		s = s.replaceAll("[èéêë]", "e");
		s = s.replaceAll("[ìíîï]", "i");
		s = s.replaceAll("[òóôõø]", "o");
		s = s.replaceAll("[ùúû]", "u");
		s = s.replaceAll("ç", "c");
		s = s.replaceAll("ñ", "n");

		s = s.replaceAll("[ÀÁÂÃÅ]", "A");
		s = s.replaceAll("[ÈÉÊË]", "E");
		s = s.replaceAll("[ÌÍÎÏ]", "I");
		s = s.replaceAll("[ÒÓÔÕØ]", "O");
		s = s.replaceAll("[ÙÚÛ]", "U");
		s = s.replaceAll("Ç", "C");
		s = s.replaceAll("Ñ", "N");

		return s;
	}
	/**
	 * For a give user question and language, returns a Vector of 4-tuples:
	 * MatchedTopicID, TopicIDAfterLinks, AnswerString at MatchedTopicID (or at
	 * TopicIDAfterLinks), RegexPattern at MatchedTopicID Removes accents from
	 * user question.
	 * 
	 * @param userUtt
	 * @param lang
	 * @return MatchedTopicID, TopicIDAfterLinks, AnswerStringAfterLinks,
	 *         RegexPattern
	 */
	public Vector<Quadruple<String, String, String, String>> getAllPossibleNormalResponses(
			String userUtt, String lang) {
		// setLanguage(lang);
		Vector<Quadruple<String, String, String, String>> responses = new Vector<Quadruple<String, String, String, String>>();
		C = tt.getRootNode();
		Queue<Node> matchingNodes = getNextNodes_allPossibleNormalPatterns(
				removeAccents(userUtt), lang);

		if (matchingNodes == null) {
			System.err
					.println("ERROR in getAllPossibleNormalResponses(): getNextNodes_allPossibleNormalPatterns() returned null!");
		}

		for (Node n : matchingNodes) {
			// no SD mode setting allowed
			Node next = followLink(n, lang, false);

			String answer = "";
			if (tt.hasNodeSysResponse(n, lang, sessionID)) {
				answer = tt.getNodeAnswer(n, lang, sessionID, true);
			} else {
				answer = tt.getNodeAnswer(next, lang, sessionID, true);
			}

			Quadruple<String, String, String, String> tr = new Quadruple<String, String, String, String>(
					tt.getNodeName(n, sessionID), tt.getNodeName(next,
							sessionID), answer, tt.getNodeRegexPattern(n,
							sessionID));

			responses.add(tr);
		}

		// WARNING: we must not set C to the node of the answer here (i.e.,
		// ignore any links), because this is the context set only for
		// follow-up Qs, whereas we want to test the same FIRST question
		// again!
		// C = getNextNode(userUtt);

		// instead, advance C to the following node in the tree

		return responses;
	}

	/**
	 * 
	 * @return a NodeList with all active 'normal' or 'local' topic nodes
	 */
	NodeList getAllNormallyReachableNodes() {
		String xpath_expression = "//topic[@isSubDialogue='false' and @active='true' and @isSystemInitiative='false' "
				+ " and not(starts-with(@name , 'bob.99_ERROR_MESSAGES') or starts-with(@name , 'PHRASES')) ]";
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodelist = null;
		try {
			nodelist = (NodeList) xpath.evaluate(xpath_expression, getTT()
					.getRootNode(), XPathConstants.NODESET);
		} catch (XPathExpressionException xe) {
			System.err.println(xe.toString());
		}
		if (nodelist.item(0) == null) {
			System.err
					.println("ERROR in getAllNormallyReachableNodes(): Could not find any topic!");
		}
		return nodelist;
	}

	/**
	 * 
	 * @param lang
	 * 
	 * @return a topicID-sorted map &lt;answer, topicID&gt; of all the answers
	 *         that can be reached from the topictree's "normal" topic nodes
	 */
	public SortedMap<String, String> getAllAnswersFromNormallyReachableNodes(
			String lang) {
		NodeList normaltopics = getAllNormallyReachableNodes();

		Vector<String> topicAnswers = null;

		TreeMap<String, String> tMap = new TreeMap<String, String>();

		for (int i = 0; i < normaltopics.getLength(); i++) {
			// if (normaltopics.item(i).getNodeName().equals("topic")) {
			// System.out.println(dm.getAnswerFromTopicID(normaltopics.item(i)
			// .getNodeValue(), "EN", false));

			Node n = normaltopics.item(i);

			String sessionID = "someSessionID";
			if (n != null) {

				// System.err.println("getAnswerFromTopicID() - original ID: " +
				// id
				// +
				// ": "
				// + tt.getNodeAnswer(node.item(0)));

				if (!getTT().hasNodeSysResponse(n, lang, sessionID)
						&& getTT().hasNodeLink(n, sessionID)) {
					// no SD mode setting allowed ???
					Node next = followLink(n, lang, false);
					// System.err.println("getAnswerFromTopicID() - linked ID:   "
					// + tt.getNodeName(next));

					topicAnswers = getTT().getNodeAllAnswers(next, lang,
							sessionID, false);

					for (String a : topicAnswers) {
						if (getTT().getNodeName(normaltopics.item(i), "foo") != null
								&& !"".equals(a)) {

							tMap.put(a, getTT().getNodeName(
									normaltopics.item(i), "foo"));
						}
					}
				}
			}
			topicAnswers = getTT().getNodeAllAnswers(n, lang, sessionID, false);

			for (String a : topicAnswers) {

				if (getTT().getNodeName(normaltopics.item(i), "foo") != null
						&& !"".equals(a)) {

					tMap.put(a, getTT()
							.getNodeName(normaltopics.item(i), "foo"));
				}
			}
		}

		// sort map by value (topic ID)
		SortedMap<String, String> sortedtMap = new TreeMap<String, String>(
				new MapValueSort.ValueComparer(tMap));
		sortedtMap.putAll(tMap);
		return sortedtMap;
	}

	public TopicTree getTT() {
		return tt;
	}

	/**
	 * 
	 * @param id1
	 * @param id2
	 * @return true if id1 and id2 are alternative names for the same topic
	 *         (considering topic's @name attribute and all its name-trace
	 *         elements) AND IF id1 and id2 are not names of existing DISTINCT
	 *         topics
	 */
	public boolean topicIDsAreEquivalent(String id1, String id2) {
		Node n1 = tt.getNodeFromTopicID(id1, "fooSessionID");
		Node n2 = tt.getNodeFromTopicID(id2, "fooSessionID");
		if (n1 == null || n2 == null) {
			return false;
		}
		return n1.equals(n2);
	}

	/**
	 * 
	 * @param topicID
	 * @return Set of all IDs of topicID's sister name-trace elements, including
	 *         itself
	 */
	public Set<String> getAllEquivalentTopicIDs(String topicID) {
		return tt.getAllEquivalentTopicIDs(topicID, sessionID);
	}

	/**
	 * 
	 * @param id
	 *            topic ID
	 * @param lang
	 *            EN DE IT
	 * @param answerIncludesPhrasesURL
	 *            should URL from linked-to PHRASES topic be appended to this
	 *            answer?
	 * @return
	 */
	public String getAnswerFromTopicID(String id, String lang,
			boolean answerIncludesPhrasesURL) {
		Node n = tt.getNodeFromTopicID(id, sessionID);

		if (n != null) {

			// System.err.println("getAnswerFromTopicID() - original ID: " + id
			// +
			// ": "
			// + tt.getNodeAnswer(node.item(0)));

			if (!tt.hasNodeSysResponse(n, lang, sessionID)
					&& tt.hasNodeLink(n, sessionID)) {
				// no SD mode setting allowed ???
				Node next = followLink(n, lang, false);
				// System.err
				// .println(sessionID
				// +
				// "[DialogueManager] INFO in getAnswerFromTopicID(): Returning linked-to answer. Link: "
				// + id + " ->" + tt.getNodeName(next, sessionID));
				String nextAnswer = tt.getNodeAnswer(next, lang, sessionID,
						answerIncludesPhrasesURL);
				if (nextAnswer == null) {
					System.err
							.println(sessionID
									+ "[DialogueManager] ERROR in getAnswerFromTopicID(): Linked-to answer. Link: "
									+ id + " ->"
									+ tt.getNodeName(next, sessionID)
									+ " is null!");
				}
				return nextAnswer;
			}
			if (!tt.hasNodeSysResponse(n, lang, sessionID)) {
				System.err
						.println(sessionID
								+ "[DialogueManager] ERROR in getAnswerFromTopicID(): Answer at "
								+ id + " is null!");
			}
		}
		return tt.getNodeAnswer(n, lang, sessionID, answerIncludesPhrasesURL);
	}

	/**
	 * The focus tree search algorithm. May also set sdMode attribute!
	 * 
	 * @param userUtt
	 * @return the node containing the next system response for the current
	 *         query, or null if no node was found
	 */
	private Node getNextNode(String userUtt, String lang) {
		Logger log = null;
		if (logging) {
			log = Logger.getLogger(DialogueManager.class);
		}
		Node match = null;
		// Special case, after having entered the PHRASES branch of the
		// topic tree. After being directed to a topic in PHRASES, need to
		// go back to the main root of the topic tree.
		if (tt.getNodeName(C, sessionID).startsWith("PHRASES.")) {
			C = tt.getRootNode();
			if (logging) {
				log
						.log(
								CustomLog.MY_TRACE,
								sessionID.substring(0, 8)
										+ " DEBUG: [After retrieving topic from PHRASES.*, resetting dialogue state.]");
			}
			sdMode = false;
		}

		if (tt.getNodeName(C, sessionID)
				.startsWith("bob.99_ERROR_MESSAGES.")) {
			C = tt.getRootNode();
			if (logging) {
				log
						.log(
								CustomLog.MY_TRACE,
								sessionID.substring(0, 8)
										+ " DEBUG: [After retrieving ERROR message topic, resetting dialogue state.]");
			}
			sdMode = false;
		}

		if (tt.getNodeName(C, sessionID).startsWith(
				"bob.15_HELLOGOODBYE.SALUTATION.")) {
			C = tt.getRootNode();
			if (logging) {
				log
						.log(
								CustomLog.MY_TRACE,
								sessionID.substring(0, 8)
										+ " DEBUG: [After retrieving topic with initial greeting, resetting dialogue state.]");
			}
			sdMode = false;
		}

		if (sdMode == true) {

			// move to the subdialogue rule (= the analyzer) that was triggered
			// by previous user input
			match = tt.getNodeLinkedNode(C, sessionID);

			/*
			 * // Special case: after returning an error message, BoB has
			 * followed // a link to the root node (bob.bob), and entered
			 * subdialogue // mode. Here, we have to override SubDialogue mode,
			 * because the // next search should not be within subdialogues. if
			 * (tt.getNodeName(match, sessionID).equals("bob.bob")) {
			 * sdMode = false; C = match; if (logging) {
			 * log.log(CustomLog.MY_TRACE, sessionID +
			 * " DEBUG: [Verlasse (ERROR-) SubDialog]"); } }
			 */
		}

		if (sdMode == true) {
			// / DISABLE THIS FOR A TEST: to solve Ulli's ISSN issue
			// might have to follow non-SD links from here, too
			// /match = followLink(match);
			Node newnode = null;
			if ((newnode = tt.searchSD(match, userUtt, lang, sessionID)) != null) {
				continuousNoPatternFoundErrors = 0;
				if (logging) {
					log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
							+ " DEBUG: Following matching sub-dialogue Topic: "
							+ tt.getNodeName(newnode, sessionID));
				}
				// SD mode setting allowed
				return followLink(newnode, lang, true);
			} else {
				sdMode = false;
				if (logging) {
					log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
							+ " DEBUG: Leaving sub-dialogue at topic "
							+ tt.getNodeName(match, sessionID));
				}
			}
		}
		if (sdMode == false) {
			if ((match = tt.searchLocalSiblings(C, userUtt, lang, sessionID)) != null) {
				continuousNoPatternFoundErrors = 0;
				if (logging) {
					log
							.log(
									CustomLog.MY_TRACE,
									sessionID.substring(0, 8)
											+ " DEBUG: Following matching topic (possibly among 'isLocal=\"true\"' topics): "
											+ tt.getNodeName(match, sessionID));
				}
				// SD mode setting allowed
				return followLink(match, lang, true);
			}
		}

		Vector<Quadruple<String, String, String, String>> originalResponses = getAllPossibleNormalResponses(
				userUtt, lang);

		// should never happen: not even the no pattern matched pattern was
		// found
		if (originalResponses.size() == 0) {
			if (logging) {
				log
						.log(
								CustomLog.MY_TRACE,
								sessionID.substring(0, 8)
										+ " DEBUG: No pattern matched! (Not even an error pattern, omg.) ");
			}
			return null;
		}

		// only "error" pattern matched; keep count of this event
		if (originalResponses.size() == 1) {
			continuousNoPatternFoundErrors++;

			// handle continuous error messages over some threshold: go to
			// special rule
			if (continuousNoPatternFoundErrors > 1) {
				Node node = tt
						.getNodeFromTopicID(
								"bob.99_ERROR_MESSAGES.00_NO_PATTERN_FOUND.80_CONTINUED_ERROR",
								sessionID);
				if (logging) {
					log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
							+ " DEBUG: Giving proactive help after "
							+ continuousNoPatternFoundErrors
							+ " continuous error messages. Going to: "
							+ tt.getNodeName(node, sessionID));
				}
				// SD mode setting allowed
				return followLink(node, lang, true);
			}

			Node node = tt.getNodeFromTopicID(Quadruple.get1(originalResponses
					.get(0)), sessionID);
			if (logging) {
				log
						.log(
								CustomLog.MY_TRACE,
								sessionID.substring(0, 8)
										+ " DEBUG: Following unambiguously matched 'normal' (error) Topic: "
										+ tt.getNodeName(node, sessionID));
			}
			// SD mode setting allowed
			return followLink(node, lang, true);
		}

		// unique answer
		if (originalResponses.size() == 2) {
			continuousNoPatternFoundErrors = 0;
			Node node = tt.getNodeFromTopicID(Quadruple.get1(originalResponses
					.get(0)), sessionID);
			if (logging) {
				log
						.log(
								CustomLog.MY_TRACE,
								sessionID.substring(0, 8)
										+ " DEBUG: Following unambiguously matched 'normal' Topic: "
										+ tt.getNodeName(node, sessionID));
			}
			// SD mode setting allowed
			return followLink(node, lang, true);
		}

		// Answer ambiguity: Enter the answer reranker for the right language
		continuousNoPatternFoundErrors = 0;
		Vector<Quadruple<String, String, String, String>> reranked = null;
		if (lang.toUpperCase().equals("EN")) {
			reranked = tt.getQAMB().rerankAnswers(userUtt, originalResponses,
					"EN");
		} else if (lang.toUpperCase().equals("DE")) {
			reranked = tt.getQAMB().rerankAnswers(userUtt, originalResponses,
					"DE");
		} else if (lang.toUpperCase().equals("IT")) {
			reranked = tt.getQAMB().rerankAnswers(userUtt, originalResponses,
					"IT");
		}

		Node topNode = tt.getNodeFromTopicID(Quadruple.get1(reranked
				.get(reranked.size() - 1)), sessionID);
		if (logging) {
			log
					.log(
							CustomLog.MY_TRACE,
							sessionID.substring(0, 8)
									+ " DEBUG: Answer reranker reranked "
									+ reranked.size()
									+ " 'normal' topics, shown below after '#' signs (picked topic: "
									+ tt.getNodeName(topNode, sessionID) + " )");

			for (int x = reranked.size() - 1; x >= 0; x--) {
				log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
						+ " DEBUG: #"
						+ tt.getNodeName(tt.getNodeFromTopicID(Quadruple
								.get1(reranked.get(x)), sessionID), sessionID));
			}
		}
		// SD mode setting allowed
		return followLink(topNode, lang, true);

		/*
		 * Queue<Node> matches = null; if ((matches =
		 * tt.searchRecursiveSiblings(C, userUtt, lang, sessionID)) != null) {
		 * if (logging) { log.log(CustomLog.MY_TRACE, sessionID +
		 * " DEBUG: Folge passendem 'normalen' Topic: " +
		 * tt.getNodeName(matches.element(), sessionID)); } // get first element
		 * from queue return followLink(matches.element(), lang); } Node parent
		 * = C; Node oldLevel = null;
		 * 
		 * // check if root node has been reached, then break while ((parent =
		 * tt.getNodeParent(parent, sessionID)) != oldLevel) { oldLevel =
		 * parent; if ((matches = tt.searchRecursiveSiblings(parent, userUtt,
		 * lang, sessionID)) != null) { if (logging) {
		 * log.log(CustomLog.MY_TRACE, sessionID +
		 * " DEBUG: Folge passendem 'normalem' Topic: " +
		 * tt.getNodeName(matches.element(), sessionID)); } // get first element
		 * from queue return followLink(matches.element(), lang); } return
		 * null;}
		 */

	}

	/*
	 * TODO: this is only used for getting all "normal" nodes (NO: SD, local)
	 */
	private Queue<Node> getNextNodes_allPossibleNormalPatterns(String userUtt,
			String lang) {
		return tt.searchRecursiveSiblings(C, userUtt, lang, sessionID);
	}

	/**
	 * resets the dialogue history (C)
	 */
	public void resetDialogue() {
		Logger log = null;
		if (logging) {
			log = Logger.getLogger(DialogueManager.class);
		}
		tt.reset();
		continuousNoPatternFoundErrors = 0;
		C = tt.getRootNode();
		System.out
				.println(sessionID.substring(0, 8)
						+ "[DialogueManager] DialogueManager reset; topictree not reloaded!");
		sdMode = false;
		if (logging) {
			log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
					+ " DEBUG: *******************************");
		}
	}

	/**
	 * reloads TT and abbrev files
	 */
	public void reloadTT() {
		Logger log = null;
		if (logging) {
			log = Logger.getLogger(DialogueManager.class);
		}

		Authenticator.setDefault(new MyAuthenticator());

		TopicTree.reloadInstance();
		System.out
				.println(sessionID.substring(0, 8)
						+ "[DialogueManager] Topic tree and macro files reloaded and DialogueManager reset.");

		tt.reset();
		continuousNoPatternFoundErrors = 0;
		C = tt.getRootNode();
		sdMode = false;
		
		
		if (logging) {
			log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
					+ " DEBUG: *******************************");
		}
	}

	/**
	 * Follow any remaining links from node, setting the sdMode class attribute
	 * if indicated by setSDmode
	 * 
	 * @param node
	 * @param lang
	 * @param setSDmode
	 *            true if method is allowed to change the class attribute
	 *            sdMode; also toggles outputting of debug messages
	 * @return the new Node to which node links (recursively)
	 */
	public Node followLink(Node node, String lang, boolean setSDmode) {

		boolean loghere = setSDmode && logging;

		Logger log = null;
		if (loghere) {
			log = Logger.getLogger(DialogueManager.class);
		}
		if (tt.hasNodeLink(node, sessionID)) {
			Node linkednode = tt.getNodeLinkedNode(node, sessionID);

			// link to PHRASES. Don't set SD mode
			if (tt.getNodeName(linkednode, sessionID).startsWith("PHRASES.")) {

				if (tt.hasNodeSysResponse(node, lang, sessionID)) {

					if (logging) {
						log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
								+ " DEBUG: [Answer followed by PHRASES]");

					}
					return node;
				} else {
					node = tt.getNodeLinkedNode(node, sessionID);
					if (logging) {
						log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
								+ " DEBUG: Following a link to topic "
								+ tt.getNodeName(node, sessionID));
					}
					return followLink(node, lang, setSDmode);
				}
			}

			// link to ERROR. Don't set SD mode
			else if (tt.getNodeName(node, sessionID).startsWith(
					"bob.99_ERROR_MESSAGES.")) {

				if (tt.hasNodeSysResponse(node, lang, sessionID)) {

					if (loghere) {
						log
								.log(
										CustomLog.MY_TRACE,
										sessionID.substring(0, 8)
												+ " DEBUG: [ERROR message followed by link to root.]");

					}
					return node;
				} else {
					node = tt.getNodeLinkedNode(node, sessionID);
					if (loghere) {
						log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
								+ " DEBUG: Following a link to topic "
								+ tt.getNodeName(node, sessionID));
					}
					return followLink(node, lang, setSDmode);
				}
			}

			// a normal SD condition -- do set SD mode
			else {
				if (tt.hasNodeSysResponse(node, lang, sessionID)) {
					sdMode = true;
					if (loghere) {
						log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
								+ " DEBUG: [SubDialogue]");
						log
								.log(
										CustomLog.MY_TRACE,
										sessionID.substring(0, 8)
												+ " TMP: ... because this node has a link and an answer. "
												+ tt.getNodeName(node,
														sessionID));
					}
					return node;
				} else {
					node = tt.getNodeLinkedNode(node, sessionID);
					if (loghere) {
						log.log(CustomLog.MY_TRACE, sessionID.substring(0, 8)
								+ " DEBUG: Following a link to topic "
								+ tt.getNodeName(node, sessionID));
					}
					return followLink(node, lang, setSDmode);
				}
			}
		} else {
			// no link found
			return node;
		}
	}

	/*
	 * Setter methods for the InitServlet
	 */

	public static void setUrlTopicTree(String initParameter) {
		urlTopicTreeStr = initParameter;
	}

	public static void setUrlIDFtrainingDataEN(String initParameter) {
		urlIDFtrainingDataENStr = initParameter;
	}

	public static void setUrlIDFtrainingDataDE(String initParameter) {
		urlIDFtrainingDataDEStr = initParameter;
	}

	public static void setUrlIDFtrainingDataIT(String initParameter) {
		urlIDFtrainingDataITStr = initParameter;
	}

	public static void setUrlAbbrevFileEN(String initParameter) {
		urlAbbrevFileENStr = initParameter;
	}

	public static void setUrlAbbrevFileDE(String initParameter) {
		urlAbbrevFileDEStr = initParameter;
	}

	public static void setUrlAbbrevFileIT(String initParameter) {
		urlAbbrevFileITStr = initParameter;
	}

	// public void setLanguage(String lang) {
	// tt.setLanguage(lang);
	// }
}
