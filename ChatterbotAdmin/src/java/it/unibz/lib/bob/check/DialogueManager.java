package it.unibz.lib.bob.check;

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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mallardsoft.tuple.Quadruple;

import org.apache.log4j.Logger;

/**
 *
 * @version $Id$
 */
public class DialogueManager
{
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
   * Indicates if we\'re in a SubDialogue; this is set only by getNextNode()
   */
  private boolean sdMode = false;

  /**
   * Count cont. errors to handle them at some threshold
   */
  int continuousNoPatternFoundErrors = 0;

  /**
   * remember the user query which did not match any question pattern, as it
   * might be an OPAC query...
   */
  String possibleOpacQuery = null;

  /**
   * The Java Session ID that this DM is connected to; needed for logging
   */
  private String sessionID = "foobarSessionID";

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
  private Logger log = Logger.getLogger(DialogueManager.class);

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
          String idfEN, String idfDE, String idfIT)
  {
    // clumsy way of converting from file path to URL string
    try
    {
      urlTopicTreeStr = new File(tts).toURL().toString();
      urlAbbrevFileENStr = new File(ae).toURL().toString();
      urlAbbrevFileDEStr = new File(ad).toURL().toString();
      urlAbbrevFileITStr = new File(ai).toURL().toString();
      urlIDFtrainingDataENStr = idfEN;
      urlIDFtrainingDataDEStr = idfDE;
      urlIDFtrainingDataITStr = idfIT;
    }
    catch (MalformedURLException e)
    {
      log.error("Could not convert File to URL string: " + e.getMessage());
    }

    URL urlIDFtrainingDataEN = null;

    if (urlIDFtrainingDataENStr != null)
    {
      try
      {
        urlIDFtrainingDataEN = new URL(urlIDFtrainingDataENStr);
      }
      catch (MalformedURLException e)
      {
        log.error(e.toString() + ": " + e.getMessage());
      }
    }

    URL urlIDFtrainingDataDE = null;

    if (urlIDFtrainingDataDEStr != null)
    {
      try
      {
        urlIDFtrainingDataDE = new URL(urlIDFtrainingDataDEStr);
      }
      catch (MalformedURLException e)
      {
        log.error(e.toString() + ": " + e.getMessage());
      }
    }

    URL urlIDFtrainingDataIT = null;
    if (urlIDFtrainingDataITStr != null)
    {
      try
      {
        urlIDFtrainingDataIT = new URL(urlIDFtrainingDataITStr);
      }
      catch (MalformedURLException e)
      {
        log.error(e.toString() + ": " + e.getMessage());
      }
    }

    try
    {
      tt = TopicTree.getInstance(
              new URL(urlTopicTreeStr),
              new URL(urlAbbrevFileENStr),
              new URL(urlAbbrevFileDEStr),
              new URL(urlAbbrevFileITStr),
              urlIDFtrainingDataEN,
              urlIDFtrainingDataDE, urlIDFtrainingDataIT);
    }
    catch (MalformedURLException e)
    {
      log.error(e.toString() + ": " + e.getMessage());
    }

    C = tt.getRootNode();
  }

  /**
   * WebApp BoB constructor: topicTree and abbrev files have to be specified
   * via WEB-INF/web.xml; logging is enabled for debugging output
   */
  public DialogueManager()
  {
    Authenticator.setDefault(new MyAuthenticator());
    URL urlIDFtrainingDataEN = null;

    if (urlIDFtrainingDataENStr != null)
    {
      try
      {
        urlIDFtrainingDataEN = new URL(urlIDFtrainingDataENStr);
      }
      catch (MalformedURLException e)
      {
        log.error(e.toString() + ": " + e.getMessage());
      }
    }

    URL urlIDFtrainingDataDE = null;

    if (urlIDFtrainingDataDEStr != null)
    {
      try
      {
        urlIDFtrainingDataDE = new URL(urlIDFtrainingDataDEStr);
      }
      catch (MalformedURLException e)
      {
        log.error(e.toString() + ": " + e.getMessage());
      }
    }

    URL urlIDFtrainingDataIT = null;

    if (urlIDFtrainingDataITStr != null)
    {
      try
      {
        urlIDFtrainingDataIT = new URL(urlIDFtrainingDataITStr);
      }
      catch (MalformedURLException e)
      {
        log.error(e.toString() + ": " + e.getMessage());
      }
    }

    try
    {
      tt = TopicTree.getInstance(
              new URL(urlTopicTreeStr),
              new URL(urlAbbrevFileENStr),
              new URL(urlAbbrevFileDEStr),
              new URL(urlAbbrevFileITStr),
              urlIDFtrainingDataEN,
              urlIDFtrainingDataDE, urlIDFtrainingDataIT);
    }
    catch (MalformedURLException e)
    {
      log.error(e.toString() + ": " + e.getMessage());
    }

    C = tt.getRootNode();

    log.debug(sessionID + ", new DialogueManager created.");
  }

  public void setSessionID(String id)
  {
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
  public String getNextResponse(String userUtt, String lang)
  {
    if (!(lang.toUpperCase().equals("EN") || lang.toUpperCase().equals("DE")
            || lang.toUpperCase().equals("IT")))
    {
      log.error("ERROR in getNextResponse(): illegal language: "
              + lang + "!");
    }

    String response = null;
    C = getNextNode(removeAccents(userUtt), lang);

    // if topic ID is special OPAC query case, before getting answer from
    // topicTree, extracted keywords from the user question to be inserted
    // into the topicTree\'s response
    if (tt.getNodeName(C, sessionID).equals("bob.90_OPAC_MESSAGES.11_KEYWORD_SEARCH"))
    {
      log.debug("OPAC question!");
      possibleOpacQuery = OpacQueryQuestionParser.KeywordQuestion.getQueryTerms(userUtt, lang);
    }

    // get answer from TopicTree
    response = tt.getNodeAnswer(C, lang, sessionID, true);

    if (C == null)
    {
      C = tt.getRootNode();
      sdMode = false;
      tt.reset();
    }

    response = response.replaceAll("__OPAC_QUERY__", possibleOpacQuery != null
            ? possibleOpacQuery : "");

    // the OPAC query string
    // $1 is language selector
    // $2 is \'+\'-separated list of query terms

    String opacLanguageCode = "1"; // german

    if (lang.equals("EN"))
    {
      opacLanguageCode = "2";
    }

    if (lang.equals("IT"))
    {
      opacLanguageCode = "3";
    }

    String opacLinkUrl = "http://pro.unibz.it/opacuni/index.asp?SEARCH=TRUE&"
            + "bSWIN=TRUE&lang=__LANGCODE__&WITHTHESIS=false&"
            + "STICHWORT=__QUERY__";

    opacLinkUrl = opacLinkUrl.replaceFirst("__LANGCODE__", opacLanguageCode);

    String possibleOpacQuery_escaped = EscapeChars.forHrefAmpersand(
            EscapeChars.forURL(possibleOpacQuery != null ? possibleOpacQuery
            : ""));

    opacLinkUrl = opacLinkUrl.replaceFirst("__QUERY__", possibleOpacQuery_escaped);
    response = response.replaceAll("__OPAC_LINK_URL__", opacLinkUrl);

    return response;
  }

  /**
   * Remove umlauts and accents (preserving upper/lower case, just in case)
   *
   * @param s
   * @return
   */
  public static String removeAccents(String s)
  {
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
          String userUtt, String lang)
  {
    // setLanguage(lang);
    Vector<Quadruple<String, String, String, String>> responses = new Vector<Quadruple<String, String, String, String>>();
    C = tt.getRootNode();
    Queue<Node> matchingNodes = getNextNodes_allPossibleNormalPatterns(removeAccents(userUtt), lang);

    if (matchingNodes == null)
    {
      log.error("ERROR in getAllPossibleNormalResponses(): "
              + "getNextNodes_allPossibleNormalPatterns() returned null!");
    }

    for (Node n : matchingNodes)
    {
      // no SD mode setting allowed
      Node next = followLink(n, lang, false);

      String answer = "";

      if (tt.hasNodeSysResponse(n, lang, sessionID))
      {
        answer = tt.getNodeAnswer(n, lang, sessionID, true);
      }
      else
      {
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
   * @return a NodeList with all active \'normal\' or \'local\' topic nodes
   */
  NodeList getAllNormallyReachableNodes()
  {
    String xpath_expression = "//topic[@isSubDialogue=\'false\' and "
            + "@active=\'true\' and @isSystemInitiative=\'false\' "
            + "and not(starts-with(@name , \'bob.99_ERROR_MESSAGES\') "
            + "or starts-with(@name , \'PHRASES\')) ]";
    XPath xpath = XPathFactory.newInstance().newXPath();
    NodeList nodelist = null;

    try
    {
      nodelist = (NodeList) xpath.evaluate(xpath_expression, getTT().getRootNode(), XPathConstants.NODESET);
    }
    catch (XPathExpressionException e)
    {
      log.error(e.toString() + ": " + e.getMessage());
    }

    if (nodelist.item(0) == null)
    {
      log.error("ERROR in getAllNormallyReachableNodes(): Could not find any topic!");
    }

    return nodelist;
  }

  /**
   *
   * @param lang
   *
   * @return a topicID-sorted map &lt;answer, topicID&gt; of all the answers
   *         that can be reached from the topictree\'s "normal" topic nodes
   */
  public SortedMap<String, String> getAllAnswersFromNormallyReachableNodes(
          String lang)
  {
    NodeList normaltopics = getAllNormallyReachableNodes();

    Vector<String> topicAnswers = null;

    TreeMap<String, String> tMap = new TreeMap<String, String>();

    for (int i = 0; i < normaltopics.getLength(); i++)
    {
      // if (normaltopics.item(i).getNodeName().equals("topic")) {
      // log.debug(dm.getAnswerFromTopicID(normaltopics.item(i)
      // .getNodeValue(), "EN", false));
      Node n = normaltopics.item(i);

      String mySessionID = "somemySessionID";

      if (n != null)
      {

        // log.error("getAnswerFromTopicID() - original ID: " +
        // id
        // +
        // ": "
        // + tt.getNodeAnswer(node.item(0)));

        if (!getTT().hasNodeSysResponse(n, lang, mySessionID)
                && getTT().hasNodeLink(n, mySessionID))
        {
          // no SD mode setting allowed ???
          Node next = followLink(n, lang, false);
          // log.error("getAnswerFromTopicID() - linked ID:   "
          // + tt.getNodeName(next));

          topicAnswers = getTT().getNodeAllAnswers(next, lang,
                  mySessionID, false);

          for (String a : topicAnswers)
          {
            if (getTT().getNodeName(normaltopics.item(i), "foo") != null
                    && !"".equals(a))
            {

              tMap.put(a, getTT().getNodeName(
                      normaltopics.item(i), "foo"));
            }
          }
        }
      }
      topicAnswers = getTT().getNodeAllAnswers(n, lang, mySessionID, false);

      for (String a : topicAnswers)
      {

        if (getTT().getNodeName(normaltopics.item(i), "foo") != null
                && !"".equals(a))
        {
          tMap.put(a, getTT().getNodeName(normaltopics.item(i), "foo"));
        }
      }
    }

    // sort map by value (topic ID)
    SortedMap<String, String> sortedtMap = new TreeMap<String, String>(
            new MapValueSort.ValueComparer(tMap));
    sortedtMap.putAll(tMap);

    return sortedtMap;
  }

  public TopicTree getTT()
  {
    return tt;
  }

  /**
   *
   * @param id1
   * @param id2
   * @return true if id1 and id2 are alternative names for the same topic
   *         (considering topic\'s @name attribute and all its name-trace
   *         elements) AND IF id1 and id2 are not names of existing DISTINCT
   *         topics
   */
  public boolean topicIDsAreEquivalent(String id1, String id2)
  {
    Node n1 = tt.getNodeFromTopicID(id1, "fooSessionID");
    Node n2 = tt.getNodeFromTopicID(id2, "fooSessionID");

    if (n1 == null || n2 == null)
    {
      return false;
    }

    return n1.equals(n2);
  }

  /**
   *
   * @param topicID
   * @return Set of all IDs of topicID\'s sister name-trace elements, including
   *         itself
   */
  public Set<String> getAllEquivalentTopicIDs(String topicID)
  {
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
          boolean answerIncludesPhrasesURL)
  {
    Node n = tt.getNodeFromTopicID(id, sessionID);

    if (n != null)
    {
      if (!tt.hasNodeSysResponse(n, lang, sessionID)
              && tt.hasNodeLink(n, sessionID))
      {
        // no SD mode setting allowed ???
        Node next = followLink(n, lang, false);
        String nextAnswer = tt.getNodeAnswer(next, lang, sessionID,
                answerIncludesPhrasesURL);

        if (nextAnswer == null)
        {
          log.error(sessionID + ", linked-to answer. Link: "
                  + id + " ->"
                  + tt.getNodeName(next, sessionID)
                  + " is null!");
        }

        return nextAnswer;
      }

      if (!tt.hasNodeSysResponse(n, lang, sessionID))
      {
        log.error(sessionID + ". answer at " + id + " is null!");
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
  private Node getNextNode(String userUtt, String lang)
  {

    Node match = null;
    // Special case, after having entered the PHRASES branch of the
    // topic tree. After being directed to a topic in PHRASES, need to
    // go back to the main root of the topic tree.

    if (tt.getNodeName(C, sessionID).startsWith("PHRASES."))
    {
      C = tt.getRootNode();

      log.debug(sessionID.substring(0, 8) + ", after retrieving topic from "
              + "PHRASES.*, resetting dialogue state.");
      sdMode = false;
    }

    if (tt.getNodeName(C, sessionID).startsWith("bob.99_ERROR_MESSAGES."))
    {
      C = tt.getRootNode();

      log.debug(sessionID.substring(0, 8) + ", after retrieving ERROR message "
              + "topic, resetting dialogue state.");

      sdMode = false;
    }

    if (tt.getNodeName(C, sessionID).startsWith("bob.15_HELLOGOODBYE.SALUTATION."))
    {
      C = tt.getRootNode();

      log.debug(sessionID.substring(0, 8) + ", after retrieving topic with "
              + "initial greeting, resetting dialogue state.");

      sdMode = false;
    }

    if (sdMode == true)
    {

      // move to the subdialogue rule (= the analyzer) that was triggered
      // by previous user input
      match = tt.getNodeLinkedNode(C, sessionID);

      /*
       * // Special case: after returning an error message, BoB has
       * followed // a link to the root node (bob.bob), and entered
       * subdialogue // mode. Here, we have to override SubDialogue mode,
       * because the // next search should not be within subdialogues. if
       * (tt.getNodeName(match, sessionID).equals("bob.bob")) { sdMode =
       * false; C = match;  { log.debug(
       * sessionID + " DEBUG: [Verlasse (ERROR-) SubDialog]"); } }
       */
    }

    if (sdMode == true)
    {
      // / DISABLE THIS FOR A TEST: to solve Ulli\'s ISSN issue
      // might have to follow non-SD links from here, too
      // /match = followLink(match);
      Node newnode = null;

      if ((newnode = tt.searchSD(match, userUtt, lang, sessionID)) != null)
      {
        continuousNoPatternFoundErrors = 0;

        log.debug(sessionID.substring(0, 8) + ", following matching "
                + "sub-dialogue Topic: " + tt.getNodeName(newnode, sessionID));

        // SD mode setting allowed
        return followLink(newnode, lang, true);
      }
      else
      {
        sdMode = false;

        log.debug(sessionID.substring(0, 8) + ", leaving sub-dialogue at "
                + "topic " + tt.getNodeName(match, sessionID));
      }
    }

    if (sdMode == false)
    {
      if ((match = tt.searchLocalSiblings(C, userUtt, lang, sessionID)) != null)
      {
        continuousNoPatternFoundErrors = 0;

        log.debug(sessionID.substring(0, 8) + ", following matching topic "
                + "(possibly among \'isLocal=\"true\"\' topics): "
                + tt.getNodeName(match, sessionID));

        // SD mode setting allowed
        return followLink(match, lang, true);
      }
    }

    Vector<Quadruple<String, String, String, String>> originalResponses = getAllPossibleNormalResponses(userUtt, lang);

    // should never happen: not even the no pattern
    // matched pattern was found
    if (originalResponses.size() == 0)
    {
      log.debug(sessionID.substring(0, 8) + ", no pattern matched! "
              + "(Not even an error pattern, omg.) ");

      return null;
    }

    // only "error" pattern matched; keep count of this event
    if (originalResponses.size() == 1)
    {
      continuousNoPatternFoundErrors++;
      possibleOpacQuery = userUtt;

      // handle continuous error messages over some threshold: go to
      // special rule
      if (continuousNoPatternFoundErrors > 1)
      {
        possibleOpacQuery = null;
        Node node = tt.getNodeFromTopicID(
                "bob.99_ERROR_MESSAGES.00_NO_PATTERN_FOUND.80_CONTINUED_ERROR",
                sessionID);

        log.debug(sessionID.substring(0, 8) + ", giving proactive help after "
                + continuousNoPatternFoundErrors
                + "continuous error messages. Going to: "
                + tt.getNodeName(node, sessionID));

        // SD mode setting allowed
        return followLink(node, lang, true);
      }

      Node node = tt.getNodeFromTopicID(Quadruple.get1(originalResponses.get(0)), sessionID);

      log.debug(sessionID.substring(0, 8) + ", following unambiguously matched "
              + "\\'normal\\' (error) Topic: "
              + tt.getNodeName(node, sessionID));

      // SD mode setting allowed
      return followLink(node, lang, true);
    }

    // unique answer
    if (originalResponses.size() == 2)
    {
      continuousNoPatternFoundErrors = 0;

      Node node = tt.getNodeFromTopicID(
              Quadruple.get1(originalResponses.get(0)), sessionID);

      log.debug(sessionID.substring(0, 8) + ", following unambiguously "
              + "matched \\'normal\\' Topic: "
              + tt.getNodeName(node, sessionID));

      // SD mode setting allowed
      return followLink(node, lang, true);
    }

    // Answer ambiguity: Enter the answer reranker for the right language
    continuousNoPatternFoundErrors = 0;
    possibleOpacQuery = null;
    Vector<Quadruple<String, String, String, String>> reranked = null;

    if (lang.toUpperCase().equals("EN"))
    {
      reranked = tt.getQAMB().rerankAnswers(userUtt, originalResponses, "EN");
    }
    else if (lang.toUpperCase().equals("DE"))
    {
      reranked = tt.getQAMB().rerankAnswers(userUtt, originalResponses, "DE");
    }
    else if (lang.toUpperCase().equals("IT"))
    {
      reranked = tt.getQAMB().rerankAnswers(userUtt, originalResponses, "IT");
    }

    Node topNode = tt.getNodeFromTopicID(Quadruple.get1(
            reranked.get(reranked.size() - 1)), sessionID);


    log.debug(sessionID.substring(0, 8) + ", answer reranker reranked "
            + reranked.size()
            + " \'normal\' topics, shown below after \'#\' signs "
            + "(picked topic: " + tt.getNodeName(topNode, sessionID) + " )");

    for (int x = reranked.size() - 1; x >= 0; x--)
    {
      log.debug(sessionID.substring(0, 8) + ", # "
              + tt.getNodeName(tt.getNodeFromTopicID(
              Quadruple.get1(reranked.get(x)), sessionID), sessionID));
    }

    // SD mode setting allowed
    return followLink(topNode, lang, true);
  }

  /*
   * TODO: this is only used for getting all "normal" nodes (NO: SD, local)
   */
  private Queue<Node> getNextNodes_allPossibleNormalPatterns(String userUtt,
          String lang)
  {
    return tt.searchRecursiveSiblings(C, userUtt, lang, sessionID);
  }

  /**
   * resets the dialogue history (C)
   */
  public void resetDialogue()
  {
    tt.reset();
    continuousNoPatternFoundErrors = 0;
    C = tt.getRootNode();

    log.debug(sessionID.substring(0, 8) + ", reset; topictree not reloaded!");

    sdMode = false;

    log.debug(sessionID.substring(0, 8) + " : *****************************");
  }

  /**
   * reloads TT and abbrev files
   */
  public void reloadTT()
  {
    Authenticator.setDefault(new MyAuthenticator());

    TopicTree.reloadInstance();
    log.debug(sessionID.substring(0, 8) + ", topic tree and macro files "
            + "reloaded and DialogueManager reset.");

    tt.reset();
    continuousNoPatternFoundErrors = 0;
    C = tt.getRootNode();
    sdMode = false;

    log.debug(sessionID.substring(0, 8) + " : *****************************");
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
  public Node followLink(Node node, String lang, boolean setSDmode)
  {
    if (tt.hasNodeLink(node, sessionID))
    {
      Node linkednode = tt.getNodeLinkedNode(node, sessionID);

      // link to PHRASES. Do not set SD mode
      if (tt.getNodeName(linkednode, sessionID).startsWith("PHRASES."))
      {
        if (tt.hasNodeSysResponse(node, lang, sessionID))
        {
          log.debug(sessionID.substring(0, 8) + ", answer followed by #"
                  + "PHRASES");
          return node;
        }
        else
        {
          node = tt.getNodeLinkedNode(node, sessionID);

          log.debug(sessionID.substring(0, 8) + ", following a link to topic "
                  + tt.getNodeName(node, sessionID));

          return followLink(node, lang, setSDmode);
        }
      }
      // link to ERROR. Don\'t set SD mode
      else if (tt.getNodeName(node, sessionID).startsWith(
              "bob.99_ERROR_MESSAGES."))
      {
        if (tt.hasNodeSysResponse(node, lang, sessionID))
        {
          log.debug(sessionID.substring(0, 8) + ", error message followed by "
                  + "link to root.");

          return node;
        }
        else
        {
          node = tt.getNodeLinkedNode(node, sessionID);

          log.debug(sessionID.substring(0, 8) + ", following a link to topic "
                  + tt.getNodeName(node, sessionID));

          return followLink(node, lang, setSDmode);
        }
      }
      // a normal SD condition -- do set SD mode
      else
      {
        if (tt.hasNodeSysResponse(node, lang, sessionID))
        {
          sdMode = true;

          log.debug(sessionID.substring(0, 8) + ", SubDialogue: "
                  + " TMP: ... because this node has a link and an answer. "
                  + tt.getNodeName(node,
                  sessionID));

          return node;
        }
        else
        {
          node = tt.getNodeLinkedNode(node, sessionID);

          log.debug(sessionID.substring(0, 8) + ", following a link to topic "
                  + tt.getNodeName(node, sessionID));

          return followLink(node, lang, setSDmode);
        }
      }
    }
    else
    {
      // no link found
      return node;
    }
  }

  /*
   * Setter methods for the InitServlet
   */
  public static void setUrlTopicTree(String initParameter)
  {
    urlTopicTreeStr = initParameter;
  }

  public static void setUrlIDFtrainingDataEN(String initParameter)
  {
    urlIDFtrainingDataENStr = initParameter;
  }

  public static void setUrlIDFtrainingDataDE(String initParameter)
  {
    urlIDFtrainingDataDEStr = initParameter;
  }

  public static void setUrlIDFtrainingDataIT(String initParameter)
  {
    urlIDFtrainingDataITStr = initParameter;
  }

  public static void setUrlAbbrevFileEN(String initParameter)
  {
    urlAbbrevFileENStr = initParameter;
  }

  public static void setUrlAbbrevFileDE(String initParameter)
  {
    urlAbbrevFileDEStr = initParameter;
  }

  public static void setUrlAbbrevFileIT(String initParameter)
  {
    urlAbbrevFileITStr = initParameter;
  }
}
