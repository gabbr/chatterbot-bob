package it.unibz.lib.bob.check;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.apache.oro.text.MalformedCachePatternException;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import it.unibz.lib.bob.macroparser.macroParser_Lexer;
import it.unibz.lib.bob.macroparser.macroParser_LexerTokenTypes;
import it.unibz.lib.bob.macroparser.macroParser_Parser;

/**
 *
 * @author kirschner
 * @version $Id: BobHelper.java 313 2010-12-22 10:41:47Z markus.grandpre $
 */
public class BobHelper implements macroParser_LexerTokenTypes {
	public static void main(String args[]) {

		BobHelper helper = null;
		try {
			helper = new BobHelper(
					new URL(
							"file:///home/kirschner/workspace/libexperts/bob_macros_DE.txt"),
					"DE");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		
		String test_regex = "((\"(.*)\")&&!(\"#ST_AUSLEIHEN#|#ST_BESTELLEN#|mir eine geschichte|#DEUTSCHE_STAEDTE#|#SCHIMPFWORTE#|#ST_SIGNATUR#|#ST_ABHOLEN#|#ST_AUSGEBEN#|#V_ZURUECKGEBEN#|�ber #DICH#|#ST_KATALOG#|#ST_CAMPUSKATALOG#|ausserhalb|#ST_ABHOLEN#|#ST_FACHBIBLIOTHEK#|#ST_VIFAS#|ge�ffnet|#ST_STUDENT#|#ST_STABI#|entleihbar|#ST_BILDERBUCH#|kind(er)?|#DU# #BIST#|#ST_FERNLEIHE#|#V_SCHICKEN#|#LIEFERN#|#ST_PASSWORT#|#AKTUELL#|#ST_BUCH_ALT#|#ST_BUECHERTURM#|#ST_NACHSCHLAGEWERK#|#ST_AUSWEIS#|#ST_GEBAEUDE#|#ST_OEFFNUNGSZEIT#|_webis_|#EXIT#|(aus|ab)(schalte[nr]|blenden)|#V_FINDEN# .*#NICHT#|#NICHT# .*(#V_FINDEN#|da|vorhanden)|(worum|um was) .*geht|#ST_FREISCHALTEN#|#ST_ZEITSCHRIFTENTITEL#|#ST_KONTAKT#|#ST_NUMMER#|#ST_EMEDIEN_ZUGANG#|#ST_BEREITSTELLUNG#|#ST_MATERIALART#|#ST_WIWI#|#ST_INFORMATIK#\"))";

		// System.out.println(sh.replaceMacros(test_regex));
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("/home/kirschner/out.tmp"),
					"ISO-8859-1"));
			out.write("print if "
					+ helper.replaceMacros(test_regex, "").replaceAll("\"", "/"));
			out.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

	public static String simplifyAlphabet(String regex) {

		return regex.toLowerCase(Locale.GERMAN);
	}

	HashMap<String, String> macroMap;
	HashMap<String, String> macroMapSubsetUsedByCurrentPattern = new HashMap<String, String>();

	@SuppressWarnings("unused")
	private BobHelper() {
		
	}

	/**
	 * 
	 * @param shortcutFile
	 * @param lang
	 *            (just for output purposes)
	 */
	public BobHelper(URL shortcutFile, String lang) {
		macroMap = getMacroMapFromURL(shortcutFile.toExternalForm());
		expandMacroMap(lang);
	}

	/**
	 * 
	 * @param shortcutFile
	 * @param lang
	 *            (just for output purposes)
	 */
	public BobHelper(String shortcutFile, String lang) {
		macroMap = getMacroMapFromFile(shortcutFile);
		expandMacroMap(lang);
	}

	/**
	 * Expands the macroMap by iteratively applying the expansions on the map's
	 * values. Also, $ signs are escaped with a \
	 * 
	 * NEW: Ignore macro FUNCTION syntax: - in the rules:
	 * #WIE_FINDE_ICH(#ST_REZEPT#)# - in cm.pattern: $1$
	 * 
	 * @param lang
	 *            (just for output purposes)
	 * @return true if some value in the array was changed.
	 */
	private boolean expandMacroMap(String lang) {
		Pattern MACRO = Pattern.compile("#([^#]+)#");
		Matcher matcher;
		boolean somethingChanged;
		int temp = 0;
		do {
			somethingChanged = false;
			temp++;
			for (Map.Entry<String, String> entry : macroMap.entrySet()) {

				String key = entry.getKey();
				// System.err.println("initial expansion of macro " + key);
				String value = entry.getValue();
				// Ignore macro FUNCTION syntax:
				value = value.replaceAll("\\$1\\$", "foobar");
				matcher = MACRO.matcher(value);
				if (matcher.find()) {
					String replacementValue = replaceMacrosMasked(value, lang);
					macroMap.put(key, replacementValue);
					somethingChanged = true;
				}
			}
			// System.err.println("expandMacroMap(): expanded one level of
			// Macros.");
		} while (somethingChanged);
		System.err.println("Number of macro expansion iterations needed: "
				+ temp);

		return somethingChanged;
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
	 * @param urlString
	 *            cm.pattern
	 * @return
	 */
	HashMap<String, String> getMacroMapFromURL(String urlString) {
		Authenticator.setDefault(new MyAuthenticator());
		URL abkuerzungen = null;
		try {
			abkuerzungen = new URL(urlString);
			// "file:///home/kirschner/workspace/BoB_STELLA/topictree.small.xml"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InputStream inputStream = null;
		try {
			inputStream = abkuerzungen.openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(
				inputStream));

		String line = null;
		StringBuffer buffer = new StringBuffer();
		try {
			inputStream = abkuerzungen.openStream();
			while ((line = in.readLine()) != null) {
				buffer.append(line + "\n");
			}// end while
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String filecontent = buffer.toString();

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

	HashMap<String, String> getMacroMapFromFile(String fileName) {
		String filecontent = null;
		try {
			FileInputStream fis = new FileInputStream(fileName);
			FileChannel fc = fis.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc
					.size());
			// Charset charset = Charset.forName ("UTF-8");
			Charset charset = Charset.forName("ISO-8859-1");
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer charBuffer = decoder.decode(bb);
			charBuffer.flip();
			filecontent = new String(charBuffer.array());
			fc.close();
			fc = null;
		} catch (IOException e) {
			System.err.println("Konnte Abk�rzungsdatei " + fileName
					+ " nicht lesen!");
			System.exit(1);
		}
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

	

	public void printMacroMap(String filename) {
		try {
			OutputStream fout = new FileOutputStream(filename);
			OutputStream bout = new BufferedOutputStream(fout);
			OutputStreamWriter out = new OutputStreamWriter(bout, "ISO-8859-1");

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

	}

	public class Pair {
		private Integer length;
		private String macro;

		public Pair(Integer a1, String a2) {
			length = a1;
			macro = a2;
		}

		public Integer getLength() {
			return length;
		}

		public String getMacro() {
			return macro;
		}

		public void setLength(Integer arg) {
			length = arg;
		}

		public void setMacro(String arg) {
			macro = arg;
		}
	}

	public Pair[] get3OverallLongestMacros() {
		Pair[] result = new Pair[3];
		result[0] = new Pair(0, "");
		result[1] = new Pair(0, "");
		result[2] = new Pair(0, "");

		for (Map.Entry<String, String> entry : macroMap.entrySet()) {
			String macro = entry.getKey();
			int length = entry.getValue().toString().length();
			if (length > result[2].getLength()) {
				int i = 2;
				while (i > 0 && length > result[i - 1].getLength()) {
					i--;
				}
				for (int j = 1; j >= i; j--) {
					result[j + 1].setLength(result[j].getLength());
					result[j + 1].setMacro(result[j].getMacro());
				}
				result[i].setLength(length);
				result[i].setMacro(macro);
			}
		}
		return result;
	}

	public Pair[] get3CurrentPatternLongestMacros() {
		Pair[] result = new Pair[3];
		result[0] = new Pair(0, "");
		result[1] = new Pair(0, "");
		result[2] = new Pair(0, "");

		for (Map.Entry<String, String> entry : macroMapSubsetUsedByCurrentPattern
				.entrySet()) {
			String macro = entry.getKey();
			int length = entry.getValue().toString().length();
			if (length > result[2].getLength()) {
				int i = 2;
				while (i > 0 && length > result[i - 1].getLength()) {
					i--;
				}
				for (int j = 1; j >= i; j--) {
					result[j + 1].setLength(result[j].getLength());
					result[j + 1].setMacro(result[j].getMacro());
				}
				result[i].setLength(length);
				result[i].setMacro(macro);
			}
		}
		return result;
	}

	public boolean checkAllRegexesInMacroMap() {
		boolean result = true;
		for (Map.Entry<String, String> entry : macroMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			value = value.replaceAll("&backslash;", "\\\\");
			value = value.replaceAll("&dollar;", "\\$");

			try {
				if (!hasBalancedParentheses(value)) {
					throw new Exception(
							"Unmatched parentheses in abbreviation " + key);
				}

			} catch (MalformedCachePatternException me) {
				System.err
						.println("Caught other (non-round-parentheses) regex error for abbreviation "
								+ key);
				// + "\n\t" + value);
				result = false;
			} catch (Exception e) {
				System.err.println(e.toString());
				result = false;
			}

			Reader reader;
			Bob_Lexer lexer;
			Bob_Parser parser;
			Bob_TreeParser treeparser;
			try {
				reader = new StringReader('"' + value + '"');
				lexer = new Bob_Lexer(reader);
				parser = new Bob_Parser(lexer);
				parser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");
				treeparser = new Bob_TreeParser();
				treeparser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");
				// note that we do not use bexpression (since the abkdatei
				// doesn't contain extended regex)
				parser.quotedRe();

			} catch (RecognitionException e) {
				System.err.println("In abbreviation definition " + key
						+ ": TreeParser threw RecognitionException. "
						+ e.toString());
				result = false;
			} catch (TokenStreamException e) {
				System.err.println("In abbreviation definition " + key
						+ ": TreeParser threw TokenStreamException. "
						+ e.toString());
				result = false;
			} catch (Exception e) {
				System.err.println("In abbreviation definition " + key
						+ ": TreeParser threw Exception. " + e.toString());
				result = false;
			}

		}
		return result;
}

	/**
	 * Call private method, and add '_', '\' and '$' afterwards, where it had
	 * been removed before
	 * 
	 * @param s
	 * @param lang
	 *            (used for output only)
	 * @return
	 */
	public String replaceMacros(String s, String lang) {
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
	 * at some higher level.-
	 * 
	 * Does not convert _ and &dollar; to their PERL equivalents. Use the public
	 * replaceMacros() if you need this. * NEW: Ignore macro FUNCTION syntax: -
	 * in the rules: #WIE_FINDE_ICH(#ST_REZEPT#)# - in cm.pattern: $1$
	 * 
	 * NEW: populates the macroMapSubsetUsedByCurrentPattern map with patterns
	 * and definitions used in current operation of replaceMacrosMasked()
	 * 
	 * @param s
	 * @param lang
	 *            (used only for output)
	 * @return
	 */
	private String replaceMacrosMasked(String s, String lang) {
		macroMapSubsetUsedByCurrentPattern.clear();
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
						macroMapSubsetUsedByCurrentPattern.put(key.substring(1,
								key.length() - 1), macroMap.get(key.substring(
								1, key.length() - 1)));
						result += macroMap.get(key.substring(1,
								key.length() - 1));
					} else {
						result += ("----MacroNotFound-"
								+ key.substring(1, key.length() - 1) + "----");
						// deactivated WARNINGS for now, until all errors are
						// removed
						System.out
								.println("WARNING "
										+ lang
										+ "::"
										+ key
										+ " . This macro is undefined for the specified language.");
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
		 * System.err.println("ERROR: Unknown macro error on line:\n" + s + "\n" +
		 * e.getMessage()); Runtime.getRuntime().exit(1); }
		 * 
		 * matcher.appendTail(result); String res_str; res_str =
		 * result.toString(); return res_str;
		 */
	}

	public static boolean hasBalancedParentheses(String str) {
		java.text.StringCharacterIterator charIt = new java.text.StringCharacterIterator(
				str);
		int nestingLevel = 0;

		for (char ch = charIt.first(); ch != java.text.StringCharacterIterator.DONE; ch = charIt
				.next()) {
			if (ch == '(')
				nestingLevel++;
			if (ch == ')')
				nestingLevel--;
		}
		if (nestingLevel == 0)
			return true;
		return false;
	}

}