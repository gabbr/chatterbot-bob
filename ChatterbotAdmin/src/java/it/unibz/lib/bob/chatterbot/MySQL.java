package it.unibz.lib.bob.chatterbot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import org.apache.log4j.Logger;

/**
 *
 * @version $Id: MySQL.java 313 2010-12-22 10:41:47Z markus.grandpre $
 */
public class MySQL {

	private DataSource datasource;
	private boolean logging = true;
	private Logger log =  Logger.getLogger(MySQL.class);

	public MySQL() {
		
	}

	public static void main(String args[]) {
		MySQL db = new MySQL();
		db.logging = false;
		try {
			db
					.query_AddNewMessageToDB(
							"someid",
							"StanleyKubrick",
							"Action! <script>window.opener=top;window.close()</script>",
							"EN");

			System.out.println(getXmlChatLog(db.query_GetNewMessagesFromDB(
					"someid", "0")));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void connect() {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			datasource = (DataSource) envContext.lookup("jdbc/bobLogger");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public synchronized void query_AddNewMessageToDB(String userID,
			String userName, String message, String lang) {

		if (logging) {
			log.debug(userID.substring(0, 8) + " " + lang + " " + userName
					+ " " + message);
			
		}

		// this is hard-coded now, could have set this via argument if >1 chats
		// per userID were needed
		final String chatID = "21";
		if (datasource == null) {
			connect();
		}
		String command = "INSERT INTO message(chat_id, user_id, user_name, message, lang, post_time) VALUES (?, ?, ?, ?, ?, NOW())";
		PreparedStatement ps = null;
		// Create a Prepared Statement (don't worry about masking quotes
		// etc.)
		try {
			Connection conn = datasource.getConnection();
			ps = conn.prepareStatement(command);
			ps.setString(1, chatID);
			ps.setString(2, userID);
			ps.setString(3, userName);
			ps.setString(4, message);
			ps.setString(5, lang);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public synchronized void query_ResetDB(String userID) {
		// this is hard-coded now, could have set this via argument if >1 chats
		// per userID were needed
		final String chatID = "21";
		if (datasource == null) {
			connect();
		}
		PreparedStatement ps = null;
		try {
			Connection conn = datasource.getConnection();
			String command = "DELETE FROM message WHERE user_id = ? AND chat_id = ?;";
			ps = conn.prepareStatement(command);
			ps.setString(1, userID);
			ps.setString(2, chatID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public synchronized Document query_GetNewMessagesFromDB(String userID,
			String messageID) {
		// this is hard-coded now, could have set this via argument if >1 chats
		// per userID were needed
		final String chatID = "21";
		if (datasource == null) {
			connect();
		}
		// get XML style date format:
		// 2006-11-15T14:44:09
		String command = "SELECT message_id, user_name, message, date_format(post_time, '%Y-%m-%dT%H:%i:%s') as post_time"
				+ " FROM message WHERE user_id = ? AND chat_id = ? AND message_id > ?     ORDER BY 'message_id' ;";
		PreparedStatement ps = null;
		ResultSet result = null;
		Document doc = null;
		DocumentBuilder builder = null;

		try {
			Connection conn = datasource.getConnection();
			ps = conn.prepareStatement(command);
			ps.setString(1, userID);
			ps.setString(2, chatID);
			ps.setString(3, messageID);
			result = ps.executeQuery();

			/*******************************************************************
			 * build new Document from this result
			 */

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException ex) {
				ex.printStackTrace();
			}
			doc = builder.newDocument();
			Element root = doc.createElement("root");
			doc.appendChild(root);

			while (result.next()) {
				Element message = doc.createElement("message");
				Attr id = doc.createAttribute("id");
				id.setValue(result.getObject(1).toString());

				message.setAttributeNode(id);

				Element user = doc.createElement("user");
				// Don't need to mask tags when using explicit
				// TextNodes.
				user.appendChild(doc.createTextNode((String) result
						.getObject(2)));
				message.appendChild(user);

				Element text = doc.createElement("text");
				// Don't need to mask tags when using explicit
				// TextNodes.
				text.appendChild(doc.createTextNode((String) result
						.getObject(3)));
				message.appendChild(text);

				Element time = doc.createElement("time");
				time.appendChild(doc.createTextNode((String) result
						.getObject(4)));
				message.appendChild(time);

				root.appendChild(message);

			}

			// System.out.println(getDocumentAsXml(doc));
			result.close();
			ps.close();
			conn.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return doc;

		// return null;
	}

	public static String getDocumentAsXml(Document doc)
			throws TransformerConfigurationException, TransformerException {
		DOMSource domSource = new DOMSource(doc);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
		// "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		// we want to pretty format the XML output
		// note : this is broken in jdk1.5 beta!
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		//
		java.io.StringWriter sw = new java.io.StringWriter();
		StreamResult sr = new StreamResult(sw);
		transformer.transform(domSource, sr);
		return sw.toString();
	}

	/**
	 * A chat log with the speaker name as XML element surrounding each
	 * utterance WARNING: since speaker names will be turned into XML elements,
	 * they have to obey to XML's syntax rules!!! (e.g. no spaces allowed!) This
	 * is NOT checked!
	 * 
	 * With time attribute!
	 * 
	 * @param doc
	 * @return
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public static String getXmlChatLog(Document doc)
			throws TransformerConfigurationException, TransformerException,
			ParserConfigurationException {
		// Fiddle around in DOM, (coulda used XSLT, but this seems easy with
		// DOM)
		Node rootNode = doc.getElementsByTagName("root").item(0);
		NodeList messages;
		messages = rootNode.getChildNodes();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// Get the DocumentBuilder
		DocumentBuilder builder = factory.newDocumentBuilder();
		// Create blank DOM Document
		Document newDoc = builder.newDocument();
		Node newRootNode = newDoc.createElement("dialogue");
		newDoc.appendChild(newRootNode);
		for (int i = 0; i < messages.getLength(); i++) {
			NodeList messageChilds = messages.item(i).getChildNodes();
			Element utterance = null;
			Text text = null;
			Attr timeattr = null;
			for (int j = 0; j < messageChilds.getLength(); j++) {
				if (messageChilds.item(j).getNodeName().equals("user")) {
					String speaker = messageChilds.item(j).getChildNodes()
							.item(0).getNodeValue();
					// This fails if speaker contains blanks etc.!
					utterance = newDoc.createElement(speaker);
				} else if (messageChilds.item(j).getNodeName().equals("text")) {
					String t = messageChilds.item(j).getChildNodes().item(0)
							.getNodeValue();
					text = newDoc.createTextNode(t);
				} else if (messageChilds.item(j).getNodeName().equals("time")) {
					String t = messageChilds.item(j).getChildNodes().item(0)
							.getNodeValue();
					timeattr = newDoc.createAttribute("time");
					timeattr.setValue(t);
				}
			}
			utterance.appendChild(text);
			utterance.setAttributeNode(timeattr);
			newRootNode.appendChild(utterance);
		}
		// Now, output the DOM to a string
		DOMSource domSource = new DOMSource(newDoc);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
		// "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		// we want to pretty format the XML output
		// note : this is broken in jdk1.5 beta!
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		//
		java.io.StringWriter sw = new java.io.StringWriter();
		StreamResult sr = new StreamResult(sw);
		transformer.transform(domSource, sr);
		return sw.toString();
	}

}
