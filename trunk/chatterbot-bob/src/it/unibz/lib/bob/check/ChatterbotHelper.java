package it.unibz.lib.bob.check;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;


import it.unibz.lib.macroparser.macroParser_Lexer;
import it.unibz.lib.macroparser.macroParser_LexerTokenTypes;
import it.unibz.lib.macroparser.macroParser_Parser;

/**
 * ChatterbotHelper is another modified version of BobHelper class,
 * used only in BoB web application. Differs form BobHelper
 * by the constructor parameters and the usage of static methods.
 *  
 * It provides some helper methods to work with the 
 * Bob-imported topic tree and the abbreviations file.
 * 
 * @author kirschner
 * 
 */
public class ChatterbotHelper implements macroParser_LexerTokenTypes {
	public static void main(String args[]) {

		try {
			ChatterbotHelper
					.makeInstance(
							new URL(
									"file:///home/kirschner/workspace/libexperts/bob_macros_EN.txt"),
							new URL(
									"file:///home/kirschner/workspace/libexperts/bob_macros_DE.txt"),
							new URL(
									"file:///home/kirschner/workspace/libexperts/bob_macros_IT.txt"));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		
		String test_regex = "((\"(.*)\")&&!(\"#ST_AUSLEIHEN#|#ST_BESTELLEN#|mir eine geschichte|#DEUTSCHE_STAEDTE#|#SCHIMPFWORTE#|#ST_SIGNATUR#|#ST_ABHOLEN#|#ST_AUSGEBEN#|#V_ZURUECKGEBEN#|über #DICH#|#ST_KATALOG#|#ST_CAMPUSKATALOG#|ausserhalb|#ST_ABHOLEN#|#ST_FACHBIBLIOTHEK#|#ST_VIFAS#|geöffnet|#ST_STUDENT#|#ST_STABI#|entleihbar|#ST_BILDERBUCH#|kind(er)?|#DU# #BIST#|#ST_FERNLEIHE#|#V_SCHICKEN#|#LIEFERN#|#ST_PASSWORT#|#AKTUELL#|#ST_BUCH_ALT#|#ST_BUECHERTURM#|#ST_NACHSCHLAGEWERK#|#ST_AUSWEIS#|#ST_GEBAEUDE#|#ST_OEFFNUNGSZEIT#|_webis_|#EXIT#|(aus|ab)(schalte[nr]|blenden)|#V_FINDEN# .*#NICHT#|#NICHT# .*(#V_FINDEN#|da|vorhanden)|(worum|um was) .*geht|#ST_FREISCHALTEN#|#ST_ZEITSCHRIFTENTITEL#|#ST_KONTAKT#|#ST_NUMMER#|#ST_EMEDIEN_ZUGANG#|#ST_BEREITSTELLUNG#|#ST_MATERIALART#|#ST_WIWI#|#ST_INFORMATIK#\"))";

		// System.out.println(sh.replaceMacros(test_regex));
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("/home/kirschner/out.tmp"),
					"ISO-8859-1"));
			out.write("print if "
					+ ChatterbotHelper.replaceMacros(test_regex, "DE").replaceAll(
							"\"", "/"));
			out.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

	public static String simplifyAlphabet(String regex) {

		return regex.toLowerCase(Locale.GERMAN);
	}

	private static URL urlAbbreviationsFileEN;
	private static URL urlAbbreviationsFileDE;
	private static URL urlAbbreviationsFileIT;

	private static HashMap<String, String> macroMapEN;
	private static HashMap<String, String> macroMapDE;
	private static HashMap<String, String> macroMapIT;

	// implementing the Singleton pattern
	private static ChatterbotHelper instance = null;

	public static synchronized ChatterbotHelper makeInstance(URL en, URL de, URL it) {

		urlAbbreviationsFileEN = en;
		urlAbbreviationsFileDE = de;
		urlAbbreviationsFileIT = it;
		if (instance == null) {
			instance = new ChatterbotHelper(en, de, it);
			System.err.println("ChatterbotHelper: creating new BoBHelper()");
		} else {
			System.err.println("ChatterbotHelper: getting old instance...");
		}
		return instance;
	}

	/**
	 * reload abbrev files
	 */
	public static void reloadInstance() {
		System.err.println("Called ChatterbotHelper::reloadInstance().");
		instance = null;
		instance = new ChatterbotHelper(urlAbbreviationsFileEN,
				urlAbbreviationsFileDE, urlAbbreviationsFileIT);
	}

	private ChatterbotHelper() {
	}

	/**
	 * 
	 * @param urlAbbreviationsFileEN
	 * @param urlAbbreviationsFileDE
	 * @param urlAbbreviationsFileIT
	 */
	private ChatterbotHelper(URL urlAbbreviationsFileEN,
			URL urlAbbreviationsFileDE, URL urlAbbreviationsFileIT) {
		// macroMapEN = getMacroMap(urlAbbreviationsFileEN.toExternalForm());
		macroMapEN = getMacroMap(urlAbbreviationsFileEN);
		macroMapDE = getMacroMap(urlAbbreviationsFileDE);
		macroMapIT = getMacroMap(urlAbbreviationsFileIT);
		expandMacroMaps();
	}

	/**
	 * Expands the 3 macroMaps by iteratively applying the expansions on the
	 * map's values.
	 */
	private void expandMacroMaps() {
		Pattern MACRO = Pattern.compile("#([^#]+)#");
		Matcher matcher;
		boolean somethingChanged;
		int temp;

		/*
		 * EN
		 */
		temp = 0;
		do {
			somethingChanged = false;
			temp++;
			for (Map.Entry<String, String> entry : macroMapEN.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				// Ignore macro FUNCTION syntax:
				value = value.replaceAll("\\$1\\$", "foobar");
				matcher = MACRO.matcher(value);
				if (matcher.find()) {
					String replacementValue = replaceMacrosMasked(value, "EN");
					macroMapEN.put(key, replacementValue);
					somethingChanged = true;
				}
			}
		} while (somethingChanged);
		System.err
				.println("Number of macro expansion iterations needed for EN: "
						+ temp);

		/*
		 * DE
		 */
		temp = 0;
		do {
			somethingChanged = false;
			temp++;
			for (Map.Entry<String, String> entry : macroMapDE.entrySet()) {
				String key = entry.getKey();

				String value = entry.getValue();
				// Ignore macro FUNCTION syntax:
				value = value.replaceAll("\\$1\\$", "foobar");
				matcher = MACRO.matcher(value);
				if (matcher.find()) {
					String replacementValue = replaceMacrosMasked(value, "DE");
					macroMapDE.put(key, replacementValue);
					somethingChanged = true;
				}
			}
		} while (somethingChanged);
		System.err
				.println("Number of macro expansion iterations needed for DE: "
						+ temp);

		/*
		 * IT
		 */
		temp = 0;
		do {
			somethingChanged = false;
			temp++;
			for (Map.Entry<String, String> entry : macroMapIT.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				// Ignore macro FUNCTION syntax:
				value = value.replaceAll("\\$1\\$", "foobar");
				matcher = MACRO.matcher(value);
				if (matcher.find()) {
					String replacementValue = replaceMacrosMasked(value, "IT");
					macroMapIT.put(key, replacementValue);
					somethingChanged = true;
				}
			}
		} while (somethingChanged);
		System.err
				.println("Number of macro expansion iterations needed for IT: "
						+ temp);
	}

	/*
	 * public static String _replaceShortcuts(String regex, Map<String, String>
	 * ruleRegexMap) {
	 * 
	 * String result = regex; for (int i = 0; i < regex.length(); i++) { if
	 * (regex.charAt(i) == '#') { String variable = ""; i++; do { variable +=
	 * regex.charAt(i); i++; } while (regex.charAt(i) != '#'); String
	 * replacement = ruleRegexMap.get(variable); if (replacement == null) {
	 * System.err.println("No variable definition entry for " + variable); }
	 * else { result.replaceAll('#' + variable + '#', replacement); } } }
	 * System.out.println(result); return result; }
	 */

	/**
	 * Extract all Shortcuts and RegEx patterns from the Shortcut definition
	 * file MUST NOT have blank lines!
	 * 
	 * Change when reading the file: 1. $ -> &dollar;
	 * 
	 * @param url
	 * @return
	 */
	private static HashMap<String, String> getMacroMap(URL url) {
		Authenticator.setDefault(new MyAuthenticator());
		/**
		 * deprecated conversion String -> URL
		 */
		/**
		 * URL abkuerzungen = null; try { abkuerzungen = new URL(fileName); //
		 * "file:///home/kirschner/workspace/BoB_STELLA/topictree.small.xml"));
		 * } catch (MalformedURLException e) { e.printStackTrace(); }
		 **/

		ReadIn in = new ReadIn("ISO-8859-1", url);
		String filecontent = in.readAll();

		HashMap<String, String> stringMap = new HashMap<String, String>();
		if (filecontent != null) {
			// extract all Shortcuts and RegEx patterns from the Shortcut
			// definition file
			String patternRegex = "^([^=]*)(?: )=(?: )(.*)$";
			Pattern pattern = Pattern.compile(patternRegex, Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(filecontent);
			if (matcher != null) {
				int n_macros = 0;
				while (matcher.find()) {
					n_macros++;
					String value = matcher.group(2).replaceAll("\\$",
							"&dollar;");
					value = value.replaceAll("\\\\", "&backslash;");
					// System.err.println("adding to MACRO map: " +
					// matcher.group(1) + " -> " + value);
					stringMap.put(matcher.group(1), value);
					// System.err.println("Adding Macro definition for: " +
					// matcher.group(1));
				}
				System.err.println("Number of macro definitions loaded: "
						+ n_macros);
			}
		}

		return stringMap;
	}

	

	/**
	 * 
	 * @param filename
	 *            of the output text file
	 * @param lang
	 *            of the macro file to be printed (one of "EN", "DE", "IT")
	 * 
	 * @return false if an illegal lang was chosen
	 */
	public boolean printMacroMap(String filename, String lang) {
		try {
			OutputStream fout = new FileOutputStream(filename);
			OutputStream bout = new BufferedOutputStream(fout);
			OutputStreamWriter out = new OutputStreamWriter(bout, "ISO-8859-1");

			Map<String, String> macroMap = null;
			if (lang.equals("EN")) {
				macroMap = macroMapEN;
			}
			if (lang.equals("DE")) {
				macroMap = macroMapDE;
			}
			if (lang.equals("IT")) {
				macroMap = macroMapIT;
			}
			if (macroMap == null)
				return false;

			for (Map.Entry<String, String> entry : macroMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				out.write(key + " = " + value + "\n");
			}
			out.flush(); // Don't forget to flush!
			out.close();
		} catch (UnsupportedEncodingException e) {
			System.out
					.println("This VM does not support the UTF-8 character set.");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return true;
	}

	/**
	 * Call private method, and add '_', '\' and '$' afterwards, where it had
	 * been removed before
	 * 
	 * @param s
	 * @param lang
	 * @return String
	 */
	public static String replaceMacros(String s, String lang) {
		String result = s;
		// System.out.println("+++ " + result);
		result = replaceMacrosMasked(s, lang);
		// System.out.println("*** " + result);
		result = result.replaceAll("_", "\\\\b");
		result = result.replaceAll("&backslash;", "\\\\");
		result = result.replaceAll("&dollar;", "\\$");

		result = result.replaceAll("&amp;", "&");
		result = result.replaceAll("&lt;", "<");
		result = result.replaceAll("&gt;", ">");
		return result;
	}

	/**
	 * Does only one replacement step for all macros in the string. I.e., if the
	 * Map contains expansions with embedded macros, this has to be dealt with
	 * at some higher level.
	 * 
	 * Does not convert _ and &dollar; to their PERL equivalents. Use the public
	 * replaceMacros() if you need this.
	 * 
	 * @param s
	 * @param lang
	 * @return String
	 */
	private static String replaceMacrosMasked(String s, String lang) {
		Map<String, String> macroMap = null;
		lang = lang.toUpperCase();
		if (lang.equals("EN")) {
			macroMap = macroMapEN;
		} else if (lang.equals("DE")) {
			macroMap = macroMapDE;
		} else if (lang.equals("IT")) {
			macroMap = macroMapIT;
		} else {
			System.err
					.println("Error in replaceMacrosMasked(): lang = " + lang);
		}

		if (macroMap == null) {
			System.err.println("AAAARGH!!!!");
		}

		Reader reader;
		macroParser_Lexer lexer;
		String result = "";
		reader = new StringReader(s);
		lexer = new macroParser_Lexer(reader);
		macroParser_Parser parser = new macroParser_Parser(lexer);
		
		try {
			parser.string();
			AST ast = parser.getAST();
			AST firstchild = ast;
			// The parser happens to not create a root node of type STRING_ if
			// the tree contains only 1 token; in this case, we don't have to
			// move
			// down first.
			if (ast.getType() == STRING_) {
				firstchild = ast.getFirstChild();
			}
			do {
				if (firstchild.getType() == MACRO) {
					// if (macroMap.get(firstchild.getFirstChild().toString())
					// != null) {
					String key = firstchild.getFirstChild().toString();

					if (macroMap.get(key.substring(1, key.length() - 1)) != null) {
						result += macroMap.get(key.substring(1,
								key.length() - 1));
					} else {
						result += ("----MacroNotFound-"
								+ key.substring(1, key.length() - 1) + "----");
						// System.out.println("WARNING: MacroNotFound: " + key);
					}
				} else if (firstchild.getType() == NON_MACRO) {
					result += firstchild.getFirstChild().toString();
				}
			} while ((firstchild = firstchild.getNextSibling()) != null);
		} catch (RecognitionException e) {
			System.err.println(e.toString());
		} catch (TokenStreamException e) {
			System.err.println(e.toString());
		}

		return result;
		/*
		 * Pattern MACRO = Pattern.compile("#([^#]+)#"); StringBuffer result =
		 * new StringBuffer(); Matcher matcher = MACRO.matcher(s); try { while
		 * (matcher.find()) { if (macroMap.get(matcher.group(1)) != null) {
		 * matcher.appendReplacement(result, macroMap.get(matcher .group(1))); }
		 * else { // System.err .println("ERROR: Reference to a macro name //
		 * that is undefined in the macro definition file"); // + s + "\n" +
		 * result.toString()); matcher.appendReplacement(result,
		 * "MacroNotFound"); } } } catch (Exception e) {
		 * System.err.println("ERROR: Unknown macro error on line:\n" + s + "\n"
		 * + e.getMessage()); Runtime.getRuntime().exit(1); }
		 * 
		 * matcher.appendTail(result); String res_str; res_str =
		 * result.toString(); return res_str;
		 */
	}

}
