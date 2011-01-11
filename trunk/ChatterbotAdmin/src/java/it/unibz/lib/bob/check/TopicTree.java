package it.unibz.lib.bob.check;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 *
 * @version $Id: TopicTree.java 313 2010-12-22 10:41:47Z markus.grandpre $
 */
public class TopicTree {

	// implementing the Singleton pattern
	private static TopicTree instance = null;

	public static synchronized TopicTree getInstance(URL urlTopicTreeFile,
			URL urlAbbreviationsFileEN, URL urlAbbreviationsFileDE,
			URL urlAbbreviationsFileIT, URL urlIDFtrainingDataEN,
			URL urlIDFtrainingDataDE, URL urlIDFtrainingDataIT) {
		if (instance == null) {
			instance = new TopicTree(urlTopicTreeFile, urlAbbreviationsFileEN,
					urlAbbreviationsFileDE, urlAbbreviationsFileIT,
					urlIDFtrainingDataEN, urlIDFtrainingDataDE, urlIDFtrainingDataIT);
		}
		return instance;
	}

	/**
	 * Reload TT and abbrev file
	 */
	public static synchronized void reloadInstance() {
		instance.initDOM();

		ChatterbotHelper.reloadInstance();

		instance.qamb = null;
		instance.qamb = new QAMatchingBob(urlAbbreviationsFileEN,
				urlAbbreviationsFileDE, urlAbbreviationsFileIT,
				idftrainingdataEN, idftrainingdataDE, idftrainingdataIT);

		System.err
				.println("*** reloadInstance(): reloaded TT and abbrev files ");

	}

	private TopicTree() {
	}

	/**
	 * Constructor that specifies TopicTree (the XML file) and the 3
	 * abbreviations files
	 * 
	 * @param urlTopicTreeFile
	 * @param urlAbbreviationsFileEN
	 * @param urlAbbreviationsFileDE
	 * @param urlAbbreviationsFileIT
	 * 
	 *            language selection from the topicTree, either "DE" or "EN" or
	 *            "IT"
	 */
	private TopicTree(URL urlTopicTreeFile, URL urlAbbreviationsFileEN,
			URL urlAbbreviationsFileDE, URL urlAbbreviationsFileIT,
			URL idftrainingdataEN, URL idftrainingdataDE, URL idftrainingdataIT) {
		this.urlTopicTreeFile = urlTopicTreeFile;
		TopicTree.urlAbbreviationsFileEN = urlAbbreviationsFileEN;
		TopicTree.urlAbbreviationsFileDE = urlAbbreviationsFileDE;
		TopicTree.urlAbbreviationsFileIT = urlAbbreviationsFileIT;
		TopicTree.idftrainingdataEN = idftrainingdataEN;
		TopicTree.idftrainingdataDE = idftrainingdataDE;
		TopicTree.idftrainingdataIT = idftrainingdataIT;
		initDOM();
		ChatterbotHelper.makeInstance(urlAbbreviationsFileEN,
				urlAbbreviationsFileDE, urlAbbreviationsFileIT);

		qamb = new QAMatchingBob(urlAbbreviationsFileEN,
				urlAbbreviationsFileDE, urlAbbreviationsFileIT,
				idftrainingdataEN, idftrainingdataDE, idftrainingdataIT);

		// setLanguage("EN");
		fillEquivalentTopicIDs("someSessionID");
	}

	private Document document = null;

	private Map<String, Set<String>> equivalentTopicIDs = new HashMap<String, Set<String>>();

	private synchronized Document getDocument() {
		return document;
	}

	// private String language = null;

	private URL urlTopicTreeFile = null;

	private static URL urlAbbreviationsFileEN = null;
	private static URL urlAbbreviationsFileDE = null;
	private static URL urlAbbreviationsFileIT = null;
	private static URL idftrainingdataEN = null;
	private static URL idftrainingdataDE = null;
	private static URL idftrainingdataIT = null;

	private QAMatchingBob qamb;

	public QAMatchingBob getQAMB() {
		return qamb;
	}

	/**
	 * Change the language of Q patterns and answers
	 * 
	 * @param lang
	 */
	/**
	 * public boolean setLanguage(String lang) { if (lang.equals("EN") ||
	 * lang.equals("DE") || lang.equals("IT")) { language = lang; return true; }
	 * return false; }
	 */

	/**
	 * Builds the DOM tree from url, stores it in document
	 */
	private synchronized void initDOM() {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(false);
			factory.setIgnoringComments(true);
			DocumentBuilder builder = factory.newDocumentBuilder();

			/**
			 * Anonymous inner class adapter to provide an ErrorHandler
			 */
			builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
				// treat validation errors as fatal
				public void error(SAXParseException e) throws SAXParseException {
					throw e;
				}

				// ignore fatal errors (an exception is guaranteed)
				public void fatalError(SAXParseException exception)
						throws SAXException {
					throw exception;
				}

				// dump warnings too
				public void warning(SAXParseException err) {
					// throws SAXParseException {
					System.err
							.println("*** org.xml.sax.ErrorHandler() *** WARNING"
									+ ", line "
									+ err.getLineNumber()
									+ ", uri " + err.getSystemId());
					System.err.println("*** org.xml.sax.ErrorHandler() ***   "
							+ err.getMessage());
				}
			});
			document = builder.parse(urlTopicTreeFile.toString());
		} catch (SAXException sxe) {
			// Error generated during parsing
			Exception x = sxe;
			if (sxe.getException() != null)
				x = sxe.getException();
			x.printStackTrace();
		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			pce.printStackTrace();
		} catch (IOException ioe) {
			// I/O error
			ioe.printStackTrace();
		}

		this.document = document;
		// getCurrentTopic();
		System.err.println("Begin replacePhraseTopicsInLinksWithURLs() ...");
		replacePhraseTopicsInLinksWithURLs();
		System.err.println("Finished replacePhraseTopicsInLinksWithURLs() .");
	}

	/**
	 * @return the root topic node
	 */
	public synchronized Node getRootNode() {
		NodeList offspring;
		Node rootNode = getDocument().getElementsByTagName("library_domain")
				.item(0);
		offspring = rootNode.getChildNodes();
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("topic")) {
				return offspring.item(i);
			}
		}
		return null;
	}

	// used in replacePhraseTopicsInLinksWithURLsRecursive
	private Pattern myPattern = Pattern
			.compile("([^<>]*)<linkToPhrases>([^|]*)\\|([^<>]*)</linkToPhrases>([^<>]*)");

	/**
	 * Traverse the DOM, remove all empty text nodes. Then, find all
	 * <linkToPhrases> elements in the answer, replace the PHRASES element
	 * inside it with the corresponding URL gotten from the TT
	 */
	public synchronized void replacePhraseTopicsInLinksWithURLs() {
		removeEmptyTextNodes(document.getDocumentElement());
		NodeList offspring = getRootNode().getChildNodes();
		for (int i = 0; i < offspring.getLength(); i++) {
			// replace linkToPhrases
			if (offspring.item(i).getNodeName().equals("topic")) {
				replacePhraseTopicsInLinksWithURLsRecursive(offspring.item(i));
			}
		}
	}

	/**
	 * remove empty text nodes (ie nothing else than spaces and carriage return)
	 * that are in the DOM :-(
	 * 
	 * @param node
	 */
	private synchronized void removeEmptyTextNodes(Node node) {
		Node child = node.getFirstChild();
		while (child != null) {
			// save the sibling of the node that will
			// perhaps be removed and set to null
			Node c = child.getNextSibling();
			if (child.getNodeType() == Node.TEXT_NODE) {
				if (child.getNodeValue().trim().length() == 0
						|| child.getNodeValue().equals("\n")
						|| child.getNodeValue().equals("\r")
						|| child.getNodeValue().equals("\r\n")) {
					node.removeChild(child);
				}
			}
			child = c;
		}
		// process children
		child = node.getFirstChild();
		while (child != null) {
			removeEmptyTextNodes(child);
			child = child.getNextSibling();
		}
	}

	/**
	 * Recursive method, does the replacement in the local answer, recurses down
	 * in the tree. Note: Does NOT honor "name-trace" elements when looking for
	 * the targeted topic nodes! I.e., any moved topics will generate an error.
	 * 
	 * @param topicnode
	 */
	private synchronized void replacePhraseTopicsInLinksWithURLsRecursive(
			Node topicnode) {
		NodeList answers = topicnode.getChildNodes();
		for (int j = 0; j < answers.getLength(); j++) {
			if (answers.item(j).getNodeName().equals("answer")) {
				NodeList languages = answers.item(j).getChildNodes();
				for (int k = 0; k < languages.getLength(); k++) {
					if (languages.item(k).getNodeName().equals("languages")) {
						NodeList en_de_it = languages.item(k).getChildNodes();
						for (int l = 0; l < en_de_it.getLength(); l++) {
							NodeList answer = en_de_it.item(l).getChildNodes();
							for (int m = 0; m < answer.getLength(); m++) {
								String str = answer.item(m).getNodeValue();
								if (str != null
										&& str.contains("linkToPhrases")) {
									// System.out.println(str);

									// will be re-escaped implicitly
									str = str.replaceAll("&lt;", "<");
									str = str.replaceAll("&gt;", ">");

									// mask any other pointy brackets for now
									str = str.replaceAll("<option>",
											"__OPTION__");
									str = str.replaceAll("</option>",
											"__/OPTION__");

									Matcher myMatcher = myPattern.matcher(str);
									String newstr = "";

									while (myMatcher.find()) {
										if (myMatcher.groupCount() != 4) {
											System.err.println("Matched "
													+ myMatcher.groupCount()
													+ "  groups!");
										} else {
											newstr += myMatcher.group(1);
											newstr += "<linkToPhrases>";

											String xpath_expression = "//topic[@name='"
													+ myMatcher.group(2)
													+ "']"
													+ "/answer/languages/"
													+ en_de_it.item(l)
															.getNodeName()
													+ "/text()" + "[1]";

											XPath xpath = XPathFactory
													.newInstance().newXPath();
											NodeList linkedNode = null;
											try {
												linkedNode = (NodeList) xpath
														.evaluate(
																xpath_expression,
																getRootNode(),
																XPathConstants.NODESET);
											} catch (XPathExpressionException xe) {
												System.err.println("*** "
														+ xe.toString());
											}
											if (linkedNode.item(0) == null) {
												System.err
														.println("*** ERROR: Could not find PHRASES element using its ID!!! \n"
																+ "Current Link ID: "
																+ myMatcher
																		.group(2)
																+ " ; Offending language: "
																+ en_de_it
																		.item(l)
																		.getNodeName());
												newstr += "ERROR-IN-PHRASES";
											}

											else {
												newstr += linkedNode.item(0)
														.getNodeValue();
											}
											newstr += "|" + myMatcher.group(3);
											newstr += "</linkToPhrases>";
											newstr += myMatcher.group(4);
										}
									}

									// System.out.println(str);
									// System.out.println(newstr);

									newstr = newstr.replaceAll("__OPTION__",
											"<option>");
									newstr = newstr.replaceAll("__/OPTION__",
											"</option>");

									Text text = getDocument().createTextNode(
											newstr);

									en_de_it.item(l).replaceChild(text,
											answer.item(m));
								}
							}
						}
					}
				}
			} else if (answers.item(j).getNodeName().equals("topic")) {
				replacePhraseTopicsInLinksWithURLsRecursive(answers.item(j));
			}
		}
	}

	/**
	 * 
	 * @param node
	 * @param language
	 *            EN DE IT
	 * @param sessionID
	 * @param appendPhrase
	 *            add to the answer the URL from PHRASES that node links to?
	 * @return (randomly one of the) answer(s) of this node, or an error message
	 *         if the node has no answer. If the Node (which we know must
	 *         contain an answer per se) also has a link into a 'PHRASES' topic,
	 *         the answer (= a URL) from there is retrieved and appended to the
	 *         first answer if needed
	 */
	public synchronized String getNodeAnswer(Node node, String language,
			String sessionID, boolean appendPhrase) {
		if (node == null) {
			System.err.println(sessionID
					+ " getNodeAnswer(): called with null startnode!");
			return new String(
					"[ERROR: no pattern found (not even a generic one for the error message).]");
		}
		NodeList offspring;
		Vector<String> responses = new Vector<String>();
		Random generator = new Random();
		offspring = node.getChildNodes();
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("answer")) {
				// ignore empty element
				if (offspring.item(i).getFirstChild() != null) {
					// get the answer string for the appropriate language
					NodeList languages = offspring.item(i).getFirstChild()
							.getChildNodes();
					for (int j = 0; j < languages.getLength(); j++) {
						if (languages.item(j).getNodeName().equals(language)) {
							String answer = "";
							if (languages.item(j).getFirstChild() == null) {
								answer = "[No answer was provided for this topic]";
							} else {
								answer = languages.item(j).getFirstChild()
										.getNodeValue();
							}

							// add something to the answer, if there is a
							// LINK
							// to the 'PHRASES' part of the topic tree from
							// the
							// current node.
							if (hasNodeLink(node, sessionID)) {
								Node linkedNode = getNodeLinkedNode(node,
										sessionID);
								String linkedNodeName = getNodeName(linkedNode,
										sessionID);
								if (linkedNodeName.startsWith("PHRASES.")) {
									// answer += " <linkToPhrases>"
									// + getNodeAnswer(linkedNode,
									// language, sessionID)
									// + "|"
									// + getNodeAnswer(linkedNode,
									// language, sessionID)
									// + "</linkToPhrases>";
									answer += getNodeAnswer(linkedNode,
											language, sessionID, false);
								}
							}
							responses.add(answer);
						}
					}
				}
			}
		}
		if (responses.size() == 0) {
			System.err.println(sessionID + " No answer node found at node "
					+ getNodeName(node, sessionID)
					+ ". Returning empty string.");
			return "";
		}
		return responses.elementAt(generator.nextInt(responses.size()));
	}

	/**
	 * 
	 * @param node
	 * @param language
	 *            EN DE IT
	 * @param sessionID
	 * @param appendPhrase
	 *            add to the answer the URL from PHRASES that node links to?
	 * @return ALL answer(s) of this node, or an error message if the node has
	 *         no answer. If the Node (which we know must contain an answer per
	 *         se) also has a link into a 'PHRASES' topic, the answer (= a URL)
	 *         from there is retrieved and appended to the first answer if
	 *         needed
	 */
	public synchronized Vector<String> getNodeAllAnswers(Node node,
			String language, String sessionID, boolean appendPhrase) {
		if (node == null) {
			System.err.println(sessionID
					+ " getNodeAllAnswers(): called with null startnode!");
			Vector<String> result = new Vector<String>();
			result
					.add("[ERROR: no pattern found (not even a generic one for the error message).]");
			return result;
		}
		NodeList offspring;
		Vector<String> responses = new Vector<String>();

		offspring = node.getChildNodes();
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("answer")) {
				// ignore empty element
				if (offspring.item(i).getFirstChild() != null) {
					// get the answer string for the appropriate language
					NodeList languages = offspring.item(i).getFirstChild()
							.getChildNodes();
					for (int j = 0; j < languages.getLength(); j++) {
						if (languages.item(j).getNodeName().equals(language)) {
							String answer = "";
							if (languages.item(j).getFirstChild() == null) {
								answer = "[No answer was provided for this topic]";
							} else {
								answer = languages.item(j).getFirstChild()
										.getNodeValue();
							}

							// add something to the answer, if there is a
							// LINK
							// to the 'PHRASES' part of the topic tree from
							// the
							// current node.
							if (hasNodeLink(node, sessionID)) {
								Node linkedNode = getNodeLinkedNode(node,
										sessionID);
								String linkedNodeName = getNodeName(linkedNode,
										sessionID);
								if (linkedNodeName.startsWith("PHRASES.")) {
									// answer += " <linkToPhrases>"
									// + getNodeAnswer(linkedNode,
									// language, sessionID)
									// + "|"
									// + getNodeAnswer(linkedNode,
									// language, sessionID)
									// + "</linkToPhrases>";
									answer += getNodeAnswer(linkedNode,
											language, sessionID, false);
								}
							}
							responses.add(answer);
						}
					}
				}
			}
		}
		if (responses.size() == 0) {
			System.err.println(sessionID + " No answer node found at node "
					+ getNodeName(node, sessionID)
					+ ". Returning empty string.");
			Vector<String> result = new Vector<String>();
			result.add("");
			return result;
		}
		return responses;
	}

	/**
	 * @param node
	 * @return true if node has a link attribute
	 */
	public synchronized boolean hasNodeLink(Node node, String sessionID) {
		if (node == null) {
			System.err.println(sessionID
					+ " hasNodeLink(): called with null startnode!");
			return false;
		}
		NodeList offspring;
		offspring = node.getChildNodes();
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("link")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get (randomly one of) the linked nodes
	 * 
	 * @param node
	 * @return (randomly one of) the linked node(s)
	 */
	public synchronized Node getNodeLinkedNode(Node node, String sessionID) {
		if (node == null) {
			System.err.println(sessionID
					+ " getNodeLinkedNode(): called with null startnode!");
			return null;
		}
		NodeList offspring;
		Vector<String> links = new Vector<String>();
		Random generator = new Random();
		offspring = node.getChildNodes();
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("link")) {
				// ignore empty element
				if (offspring.item(i).getFirstChild() != null) {
					links.add(offspring.item(i).getFirstChild().getNodeValue());
				}
			}
		}
		if (links.size() == 0) {
			return null;
		}
		String randomLinkName = links
				.elementAt(generator.nextInt(links.size()));

		Node result = getNodeFromTopicID(randomLinkName, sessionID);

		return result;
	}

	/**
	 * 
	 * @param node
	 * @return true iff node has a NON-NULL Answer/SysResponse
	 */
	public synchronized boolean hasNodeSysResponse(Node node, String language,
			String sessionID) {
		if (node == null) {
			System.err.println(sessionID
					+ " hasNodeSysResponse(): called with null startnode!");
			return false;
		}
		NodeList offspring;
		offspring = node.getChildNodes();
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("answer")) {
				if (offspring.item(i).getFirstChild() == null) {
					return false;
				}
				NodeList child_offspring;
				child_offspring = offspring.item(i).getChildNodes();
				for (int j = 0; j < child_offspring.getLength(); j++) {
					if (child_offspring.item(j).getNodeName().equals(
							"languages")) {
						if (child_offspring.item(j).getFirstChild() == null) {
							return false;
						}
						NodeList child_offspring2;
						child_offspring2 = child_offspring.item(j)
								.getChildNodes();
						for (int k = 0; k < child_offspring2.getLength(); k++) {
							if (child_offspring2.item(k).getNodeName().equals(
									language)) {
								if (child_offspring2.item(k).getFirstChild() == null) {
									return false;
								} else {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Search C for closest matching SD node. Only traverse EMPTY patterns
	 * downwards.
	 * 
	 * @param startNode
	 *            starting node for search
	 * @param query
	 * @return closest matching SD node, or null
	 */
	public synchronized Node searchSD(Node startNode, String query,
			String language, String sessionID) {
		if (startNode == null) {
			System.err.println(sessionID
					+ " searchSD(): called with null startnode!");
			return null;
		}
		//
		// startNode = getNodeParent(startNode);

		// 1. the current node matches the user query
		if (matchesNode(query, startNode, false, true, true, false, language,
				sessionID)) {
			if (hasNodeSysResponse(startNode, language, sessionID)
					|| hasNodeLink(startNode, sessionID)) {
				return startNode;
			}
		}

		NodeList offspring;
		offspring = startNode.getChildNodes();
		Queue<Node> q = new LinkedList<Node>();

		// 2. one of the subtopics of the current node matches the user query

		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("topic")) {
				// SD rules WITHOUT following empty regex

				if (matchesNode(query, offspring.item(i), false, true, true,
						false, language, sessionID)) {
					if (hasNodeSysResponse(offspring.item(i), language,
							sessionID)
							|| hasNodeLink(offspring.item(i), sessionID)) {
						return offspring.item(i);
					}
				}
			}
		}

		// After not finding any match in siblings, follow any SD rules WITH
		// empty regex

		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("topic")) {
				if (matchesNode(query, offspring.item(i), true, true, true,
						false, language, sessionID)) {
					q.addAll(getNodeMatchingChildren(query, offspring.item(i),
							true, true, true, false, language, sessionID));
				}
			}
		}
		if (q.size() < 1) {
			System.err
					.println(sessionID
							+ " WARNING: searchSD found no matching SubDialogue nodes!");
			return null;
		}
		if (q.size() > 1) {
			System.err
					.println(sessionID
							+ " WARNING: searchSD found >1 matching SubDialogue nodes!");
		}
		return q.element();

	}

	/**
	 * Search C and C's siblings for 1. first matching normal node right here,
	 * 2. "closest (in the tree)" matching normal node after traversing empty
	 * patterns.
	 * 
	 * @param startNode
	 *            starting node for search
	 * @param query
	 * @return Queue with ordered matching nodes, or null
	 */
	public synchronized Queue<Node> searchRecursiveSiblings(Node startNode,
			String query, String language, String sessionID) {
		if (startNode == null) {
			System.err
					.println(sessionID
							+ " searchRecursiveSiblings(): called with null startnode!");
			return null;
		}
		startNode = getNodeParent(startNode, sessionID);

		Queue<Node> q = new LinkedList<Node>();
		NodeList offspring;
		offspring = startNode.getChildNodes();

		// First, check for matching patterns in the current location
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("topic")) {
				// normal rules WITHOUT following empty regex
				if (matchesNode(query, offspring.item(i), false, false, false,
						true, language, sessionID)) {
					System.err.println(sessionID
							+ " found a matching topic in current location: "
							+ getNodeName(offspring.item(i), sessionID));
					q.addAll(getNodeMatchingChildren(query, offspring.item(i),
							false, false, false, true, language, sessionID));
				}
			}
		}
		// After not finding any match in siblings, follow any normal rules WITH
		// empty regex
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("topic")) {
				if (matchesNode(query, offspring.item(i), true, false, false,
						true, language, sessionID)) {
					q.addAll(getNodeMatchingChildren(query, offspring.item(i),
							true, false, false, true, language, sessionID));
				}
			}
		}
		if (q.size() < 1) {
			System.err
					.println(sessionID
							+ " WARNING: searchRecursiveSiblings found no matching normal nodes!");
			return null;
		}
		/**
		 * if (q.size() > 1) {
		 * System.err.println("WARNING: searchRecursiveSiblings found " +
		 * q.size() + " matching normal nodes!"); }
		 **/
		return q;
	}

	/**
	 * Search C and C's siblings for closest matching local node. Do not
	 * traverse empty regex nodes.
	 * 
	 * @param startNode
	 * @param query
	 * @return closest matching local node, or null
	 */
	public synchronized Node searchLocalSiblings(Node startNode, String query,
			String language, String sessionID) {
		if (startNode == null) {
			System.err.println(sessionID
					+ " searchLocalSiblings(): called with null startnode!");
			return null;
		}
		startNode = getNodeParent(startNode, sessionID);

		NodeList offspring;
		offspring = startNode.getChildNodes();
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("topic")) {
				// local rules WITHOUT following empty regex
				if (matchesNode(query, offspring.item(i), false, true, false,
						false, language, sessionID)) {
					return offspring.item(i);
				}
			}
		}
		return null;
	}

	/**
	 * Checks whether a query matches a node. "System Initiative" nodes are
	 * always rejected (since they don't need to be MATCHed in the first place).
	 * Only 'active' nodes can ever match.
	 * 
	 * @param query
	 * @param node
	 * @param matchEmptyRegex
	 *            should an empty regex element lead to a match?
	 * @param matchLocalRules
	 *            should rules declared as "local" be tried?
	 * @param matchSubDialogueRules
	 *            should rules declared as "SubDialogue" be tried?
	 * @param matchNormalRules
	 *            should normal rules be tried?
	 * @return true if node is active, and the node's regex pattern (in the
	 *         language set with the language class attribute) matches with
	 *         query, or if is empty and empty patterns are allowed via the
	 *         matchEmptyRegex flag.
	 */
	private synchronized boolean matchesNode(String query, Node node,
			boolean matchEmptyRegex, boolean matchLocalRules,
			boolean matchSubDialogueRules, boolean matchNormalRules,
			String language, String sessionID) {
		if (node == null) {
			System.err.println(sessionID
					+ " matchesNode(): called with null startnode!");
			return false;
		}
		// System.out.println("matchesNode() Q=" + query + " Node="
		// + getNodeName(node) + " matchEmptyRegex=" + matchEmptyRegex
		// + " matchLocal=" + matchLocalRules + " matchSD="
		// + matchSubDialogueRules + " matchNormal=" + matchNormalRules);
		NamedNodeMap atts = node.getAttributes();
		if (atts.getNamedItem("active").getNodeValue().equals("false")) {
			return false;
		}
		if (atts.getNamedItem("isSubDialogue").getNodeValue().equals("true")
				&& !matchSubDialogueRules) {
			return false;
		}
		if (atts.getNamedItem("isLocal").getNodeValue().equals("true")
				&& !matchLocalRules) {
			return false;
		}
		if (atts.getNamedItem("isSystemInitiative").getNodeValue().equals(
				"true")) {
			return false;
		}
		if (atts.getNamedItem("isSubDialogue").getNodeValue().equals("true")
				&& atts.getNamedItem("isLocal").getNodeValue().equals("true")
				&& atts.getNamedItem("isSystemInitiative").getNodeValue()
						.equals("true") && !matchNormalRules) {
			return false;
		}
		NodeList offspring;
		offspring = node.getChildNodes();
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("regex")) {
				if (offspring.item(i).getFirstChild() == null) {
					// whole regex element is empty (no nested languages).
					return matchEmptyRegex;
				} else {
					// get the regex string for the appropriate language
					NodeList languages = offspring.item(i).getFirstChild()
							.getChildNodes();
					for (int j = 0; j < languages.getLength(); j++) {
						if (languages.item(j).getNodeName().equals(language)) {
							if (languages.item(j).getFirstChild() == null) {
								return matchEmptyRegex;
							}

							String regex = languages.item(j).getFirstChild()
									.getNodeValue();
							if (matchExtendedRegex(regex, query, language,
									sessionID)) {
								return true;
							} else
								return false;
						}
					}

				}
			}
		}
		// node has no children at all (no regex element etc.)!
		return false;
	}

	/**
	 * Retrieves a queue of matching nodes for a query and a node NEW: now also
	 * follows links in the absence of sysresponses
	 * 
	 * @param query
	 * @param node
	 * @param followEmptyRegex
	 *            should empty regex be traversed for finding matching nodes?
	 * @param matchLocalRules
	 *            should local nodes be included in results?
	 * @param matchSubDialogueRules
	 *            should SubDialogue nodes be included in results?
	 * @param matchNormalRules
	 *            should normal nodes be included in results?
	 * @return a queue of nodes that match query under the given constraints (or
	 *         an empty queue)
	 */
	private synchronized Queue<Node> getNodeMatchingChildren(String query,
			Node node, boolean followEmptyRegex, boolean matchLocalRules,
			boolean matchSubDialogueRules, boolean matchNormalRules,
			String language, String sessionID) {
		if (node == null) {
			System.err
					.println(sessionID
							+ " getNodeMatchingChildren(): called with null startnode!");
			return null;
		}
		Queue<Node> q = new LinkedList<Node>();
		NodeList offspring;
		offspring = node.getChildNodes();
		for (int i = 0; i < offspring.getLength(); i++) {
			if (offspring.item(i).getNodeName().equals("topic")) {
				if (matchesNode(query, offspring.item(i), followEmptyRegex,
						matchLocalRules, matchSubDialogueRules,
						matchNormalRules, language, sessionID)) {
					if (hasNodeEmptyRegex(offspring.item(i), sessionID)) {
						if (followEmptyRegex) {
							// empty regex: recursively add matching
							// children
							// System.err
							// .println(
							// "Following empty regex recursively, at: "
							// + getNodeName(offspring.item(i),
							// sessionID));
							q.addAll(getNodeMatchingChildren(query, offspring
									.item(i), followEmptyRegex,
									matchLocalRules, matchSubDialogueRules,
									matchNormalRules, language, sessionID));
						}
					} else if (hasNodeSysResponse(offspring.item(i), language,
							sessionID)) {
						q.offer(offspring.item(i));
					} else if (hasNodeLink(offspring.item(i), sessionID)) {
						q.offer(offspring.item(i));
						// q.offer(getNodeLinkedNode(offspring.item(i)));
					} else {
						// just a "filtering" pattern (no empty regex, NO
						// sysResponse): have to try to go down the tree
						q.addAll(getNodeMatchingChildren(query, offspring
								.item(i), followEmptyRegex, matchLocalRules,
								matchSubDialogueRules, matchNormalRules,
								language, sessionID));
					}
				}
			}
		}
		return q;
	}

	/**
	 * Evaluates a string against an "extended regex" pattern
	 * 
	 * @param regex
	 *            the "extended regex" pattern (= Perl regex + boolean
	 *            connectives)
	 * @param query
	 * @return true if query and regex match
	 */
	public boolean matchExtendedRegex(String regex, String query,
			String language, String sessionID) {

		regex = ChatterbotHelper.replaceMacros(regex, language);

		Reader reader = new StringReader(regex);
		Bob_Lexer lexer = new Bob_Lexer(reader);
		Bob_Parser parser = new Bob_Parser(lexer);
		parser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");
		Bob_TreeParser treeparser = new Bob_TreeParser();
		treeparser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");
		boolean result = false;
		try {
			parser.bExpression();
			it.unibz.lib.bob.check.MyAST tree = (it.unibz.lib.bob.check.MyAST) parser.getAST();
			// System.err.println("Tree = " + tree.toStringTree());
			// ASTFrame frame = new ASTFrame("The tree", tree);
			// frame.setVisible(true);
			try {
				result = treeparser.bExpression(tree, query);
			} catch (ANTLRException ae) {
				System.err.println(sessionID
						+ " ERROR with regular expression.\n" + regex.trim()
						+ "\nQuery: " + query);
			}
		} catch (RecognitionException e) {
			System.err.println(sessionID + e.toString());
		} catch (TokenStreamException e) {
			System.err.println(sessionID + e.toString());
		} catch (Exception e) {
			System.err.println(sessionID
					+ " Other exception regarding the Regex Pattern -- "
					+ e.toString());
		}
		return result;
	}

	public synchronized String getNodeRegexPattern(Node node, String sessionID) {
		if (node == null) {
			System.err.println(sessionID
					+ " ERROR: getNodeRegexPattern() called for null node.");
			return new String("");
		}

		NodeList offspring;
		offspring = node.getChildNodes();
		for (int j = 0; j < offspring.getLength(); j++) {

			if (offspring.item(j).getNodeName().equals("regex")) {
				if (offspring.item(j).getFirstChild() != null) {
					return offspring.item(j).getFirstChild().getTextContent();
				}
			}
		}
		return "ERROR finding (non-empty) regex";
	}

	/**
	 * 
	 * @param node
	 * @return the node name, or an empty string for illegal node
	 */
	public synchronized String getNodeName(Node node, String sessionID) {
		if (node == null) {
			System.err.println(sessionID
					+ " ERROR: getNodeName() called for null node.");
			return new String("");
		}
		String result = null;
		NamedNodeMap atts = node.getAttributes();
		result = atts.getNamedItem("name").getNodeValue();
		return result;
	}

	/**
	 * Retrieve the DOM node of the "topic" element with the specified (via
	 * <b>id</b>) "name" attribute. If no such element exists, search for a
	 * "topic" element that has a "name-trace" element indicating that at an
	 * earlier point it had the topicID <id>id</id>.
	 * 
	 * @param id
	 * @param sessionID
	 * @return
	 */
	public synchronized Node getNodeFromTopicID(String id, String sessionID) {
		String xpath_expression = "//topic[@name='" + id + "'][1]";
		String xpath_expression2 = "//topic[name-trace/text()='" + id + "'][1]";
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList node = null;
		try {
			node = (NodeList) xpath.evaluate(xpath_expression, getRootNode(),
					XPathConstants.NODESET);
		} catch (XPathExpressionException xe) {
			System.err.println(sessionID
					+ " ERROR (Exception) in getNodeFromTopicID() with id="
					+ id + ". " + xe.toString());
		}

		if (node == null || node.item(0) == null) {

			try {
				node = (NodeList) xpath.evaluate(xpath_expression2,
						getRootNode(), XPathConstants.NODESET);
			} catch (XPathExpressionException xe) {
				System.err.println(sessionID
						+ " ERROR (Exception) in getNodeFromTopicID() with id="
						+ id + ". " + xe.toString());
			}

			if (node == null || node.item(0) == null) {
				System.err.println(sessionID
						+ " ERROR in getNodeFromTopicID() with id=" + id
						+ ". Returning null.");
				return null;
			}
		}
		return node.item(0);
	}

	/**
	 * 
	 * @param topicID
	 * @param sessionID
	 * @return Set of all IDs of topicID's sister name-trace elements, including
	 *         itself
	 */
	public synchronized Set<String> getAllEquivalentTopicIDs(String topicID,
			String sessionID) {

		if (!equivalentTopicIDs.containsKey(topicID)) {
			System.err.println(sessionID
					+ " ERROR in getAllEquivalentTopicIDs(): topicID "
					+ topicID + " was not found!");
			Set<String> result = new HashSet<String>();
			return result;
		}

		return equivalentTopicIDs.get(topicID);
	}

	/**
	 * Fills equivalentTopicIDs with all topicIDs, mapping to any equivalent ID
	 * (including itself). Does all the XPATH querying once, so that later
	 * getAllEquivalentTopicIDs is fast
	 * 
	 * @param sessionID
	 */
	public void fillEquivalentTopicIDs(String sessionID) {
		System.out.println(sessionID
				+ " INFO: loading equivalent topic info...");
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodes = null;
		String xpath_expression = "//topic";
		try {
			nodes = (NodeList) xpath.evaluate(xpath_expression, getRootNode(),
					XPathConstants.NODESET);
		} catch (XPathExpressionException xe) {
			System.err.println(sessionID
					+ " ERROR (Exception) in fillEquivalentTopicIDs()."
					+ xe.toString());
		}
		String xpath_expression2 = "name-trace/text()";
		for (int j = 0; j < nodes.getLength(); j++) {
			NodeList nodes2 = null;
			try {
				nodes2 = (NodeList) xpath.evaluate(xpath_expression2, nodes
						.item(j), XPathConstants.NODESET);
				// System.out.println("++" + context.getNodeValue());
			} catch (XPathExpressionException xe) {
				System.err.println(xe.toString() + " . Expression: "
						+ xpath_expression);
			}
			if (nodes2 == null) {
				System.err
						.println(sessionID
								+ " ERROR in fillEquivalentTopicIDs(): Could not find specified node.");
			}
			for (int k = 0; k < nodes2.getLength(); k++) {
				String id = nodes2.item(k).getNodeValue();
				equivalentTopicIDs.put(id, new HashSet<String>());
				for (int l = 0; l < nodes2.getLength(); l++) {
					String id2 = nodes2.item(l).getNodeValue();
					equivalentTopicIDs.get(id).add(id2);
				}
			}
		}
		System.out.println(sessionID + " INFO: " + equivalentTopicIDs.size()
				+ " topicIDs loaded into equivalence map.");
	}

	/**
	 * 
	 * @param startNode
	 * @return startNode's parent, or startNode itself in the case of the root
	 *         node
	 */
	public synchronized Node getNodeParent(Node startNode, String sessionID) {
		if (startNode == null) {
			System.err.println(sessionID
					+ " ERROR: getNodeParent() called for null node.");
			return null;
		}
		if (startNode.getParentNode().getNodeName().equals("library_domain")) {
			// already were at the root topic node. Don't go up further.
		} else {
			startNode = startNode.getParentNode();
		}
		return startNode;
	}

	/**
	 * 
	 * @param startNode
	 * @return startNode's next sibling (follower), or null
	 */
	public synchronized Node getNodeNextSibling(Node startNode, String sessionID) {
		if (startNode == null) {
			System.err.println(sessionID
					+ " ERROR: getNodeNextSibiling() called for null node.");
			return null;
		}
		return startNode.getNextSibling();
	}

	/**
	 * 
	 * @param node
	 * @return true if node has an empty regex pattern
	 */
	private synchronized boolean hasNodeEmptyRegex(Node node, String sessionID) {
		if (node == null) {
			System.err.println(sessionID
					+ " ERROR: hasNodeEmptyRegex() called for null node.");
			return false;
		}
		NodeList offspring;
		offspring = node.getChildNodes();
		for (int j = 0; j < offspring.getLength(); j++) {

			if (offspring.item(j).getNodeName().equals("regex")) {
				if (offspring.item(j).getFirstChild() == null) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * not doing anything now. (could reload abbrev files, or tt)
	 */
	public void reset() {	
		
	}

	public static void main(String[] args) {
		TopicTree tt = null;
		Authenticator.setDefault(new MyAuthenticator());
		try {
			// don't provide URLs for IDF training data here...
			tt = new TopicTree(
					new URL(
							"https://foobar"),
					new URL(
					"https://foobar"),
					new URL(
					"https://foobar"),
					new URL(
					"https://foobar"),
					null, null, null);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// System.out.println(tt.matchExtendedRegex("\"#ST_HOCHSCHULE#\"",
		// "universität", "DE", "someSessionID"));

		// tt.replacePhraseTopicsInLinksWithURLs();

		String xpath_expression = "//topic[@name='bob.03_SERVICE.08_OEFFNUNGSZEIT.07_Oeffungszeit_Sonderfall_Analyzer__0']"
				+ "/answer/languages/EN/text()" + "[1]";

		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList linkedNode = null;
		try {
			linkedNode = (NodeList) xpath.evaluate(xpath_expression, tt
					.getRootNode(), XPathConstants.NODESET);
		} catch (XPathExpressionException xe) {
			System.err.println("*** " + xe.toString());
		}

		System.out.println(linkedNode.item(0).getNodeValue());

		System.out
				.println(tt
						.getAllEquivalentTopicIDs(
								"bob.02_ORGANISATION.04_FACHBIBLIOTHEKEN.04_Sprachlehrforschung",
								"whateverSessID"));

	}
}
