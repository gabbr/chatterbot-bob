package it.unibz.lib.bob.check;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import it.unibz.lib.bob.macroparser.macroParser_Lexer;
import it.unibz.lib.bob.macroparser.macroParser_LexerTokenTypes;
import it.unibz.lib.bob.macroparser.macroParser_Parser;

import org.apache.log4j.Logger;

/**
 * ChatterbotHelper is another modified version of BobHelper class,
 * used only in BoB web application. Differs form BobHelper
 * by the constructor parameters and the usage of static methods.
 *  
 * It provides some helper methods to work with the 
 * Bob-imported topic tree and the abbreviations file.
 * 
 * @author kirschner
 * @version $Id$
 * 
 */
public class ChatterbotHelper implements macroParser_LexerTokenTypes
{
  private static URL urlAbbreviationsFileEN;

  private static URL urlAbbreviationsFileDE;

  private static URL urlAbbreviationsFileIT;

  private static HashMap<String, String> macroMapEN = null;

  private static HashMap<String, String> macroMapDE = null;

  private static HashMap<String, String> macroMapIT = null;

  // implementing the Singleton pattern
  private static ChatterbotHelper instance = null;

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
  private static Logger log = Logger.getLogger(Bob_Parser.class);

  public static String simplifyAlphabet(String regex)
  {
    return regex.toLowerCase(Locale.GERMAN);
  }

  public static synchronized ChatterbotHelper makeInstance(URL en, URL de, URL it)
  {
    urlAbbreviationsFileEN = en;
    urlAbbreviationsFileDE = de;
    urlAbbreviationsFileIT = it;

    if (instance == null)
    {
      instance = new ChatterbotHelper(en, de, it);
      log.debug("ChatterbotHelper: creating new BoBHelper()");
    }
    else
    {
      log.debug("ChatterbotHelper: getting old instance...");
    }

    return instance;
  }


  /**
   * update filenames, so that reloadInstance() can be called next
   * @param urlAbbreviationsFileEN
   * @param urlAbbreviationsFileDE
   * @param urlAbbreviationsFileIT
   */
  public static void updateFiles(URL en, URL de, URL it) {
      urlAbbreviationsFileEN = en;
      urlAbbreviationsFileDE = de;
      urlAbbreviationsFileIT = it;
  }

  /**
   * reload abbrev files
   */
  public static void reloadInstance()
  {
    log.debug("Called ChatterbotHelper::reloadInstance().");

    instance = null;
    instance = new ChatterbotHelper(urlAbbreviationsFileEN,
            urlAbbreviationsFileDE, urlAbbreviationsFileIT);
  }

  private ChatterbotHelper()
  {
  }

  /**
   *
   * Any of the arguments could be == null, disabling that language
   *
   * @param urlAbbreviationsFileEN
   * @param urlAbbreviationsFileDE
   * @param urlAbbreviationsFileIT
   */
  private ChatterbotHelper(URL urlAbbreviationsFileEN,
          URL urlAbbreviationsFileDE, URL urlAbbreviationsFileIT)
  {
    // macroMapEN = getMacroMap(urlAbbreviationsFileEN.toExternalForm());
    if (urlAbbreviationsFileEN != null)
        macroMapEN = getMacroMap(urlAbbreviationsFileEN);
    if (urlAbbreviationsFileDE != null)
        macroMapDE = getMacroMap(urlAbbreviationsFileDE);
    if (urlAbbreviationsFileIT != null)
        macroMapIT = getMacroMap(urlAbbreviationsFileIT);
    expandMacroMaps();
  }

  /**
   * Expands the 3 macroMaps by iteratively applying the expansions on the
   * map's values.
   */
  private void expandMacroMaps()
  {
    Pattern macro = Pattern.compile("#([^#]+)#");
    Matcher matcher;
    boolean somethingChanged;
    int temp;

    /*
     * EN
     */
    if (macroMapEN != null) {
        temp = 0;
        do
        {
          somethingChanged = false;
          temp++;
          for (Map.Entry<String, String> entry : macroMapEN.entrySet())
          {
            String key = entry.getKey();
            String value = entry.getValue();

            // Ignore macro FUNCTION syntax:
            value = value.replaceAll("\\$1\\$", "foobar");
            matcher = macro.matcher(value);

            if (matcher.find())
            {
              String replacementValue = replaceMacrosMasked(value, "EN");
              macroMapEN.put(key, replacementValue);
              somethingChanged = true;
            }
          }
        }
        while (somethingChanged);

        log.debug("Number of macro expansion iterations needed for EN: " + temp);
    }

    /*
     * DE
     */
    if (macroMapDE != null) {
        temp = 0;
        do
        {
          somethingChanged = false;
          temp++;

          for (Map.Entry<String, String> entry : macroMapDE.entrySet())
          {
            String key = entry.getKey();
            String value = entry.getValue();

            // Ignore macro FUNCTION syntax:
            value = value.replaceAll("\\$1\\$", "foobar");
            matcher = macro.matcher(value);

            if (matcher.find())
            {
              String replacementValue = replaceMacrosMasked(value, "DE");
              macroMapDE.put(key, replacementValue);
              somethingChanged = true;
            }
          }
        }
        while (somethingChanged);

    log.debug("Number of macro expansion iterations needed for DE: " + temp);
    }
    /*
     * IT
     */
    if (macroMapIT != null) {
        temp = 0;

        do
        {
          somethingChanged = false;
          temp++;

          for (Map.Entry<String, String> entry : macroMapIT.entrySet())
          {
            String key = entry.getKey();
            String value = entry.getValue();

            // Ignore macro FUNCTION syntax:
            value = value.replaceAll("\\$1\\$", "foobar");
            matcher = macro.matcher(value);

            if (matcher.find())
            {
              String replacementValue = replaceMacrosMasked(value, "IT");
              macroMapIT.put(key, replacementValue);
              somethingChanged = true;
            }
          }
        }

        while (somethingChanged);

        log.debug("Number of macro expansion iterations needed for IT: " + temp);
    }
  }

  /**
   * Extract all Shortcuts and RegEx patterns from the Shortcut definition
   * file MUST NOT have blank lines!
   *
   * Change when reading the file: 1. $ -> &dollar;
   *
   * @param url
   * @return
   */
  private static HashMap<String, String> getMacroMap(URL url)
  {
    Authenticator.setDefault(new MyAuthenticator());
    ReadIn in = new ReadIn("ISO-8859-1", url);
    String filecontent = in.readAll();
    HashMap<String, String> stringMap = new HashMap<String, String>();

    if (filecontent != null)
    {
      // extract all Shortcuts and RegEx patterns from the Shortcut
      // definition file
      String patternRegex = "^([^=]*)(?: )=(?: )(.*)$";
      Pattern pattern = Pattern.compile(patternRegex, Pattern.MULTILINE);
      Matcher matcher = pattern.matcher(filecontent);

      if (matcher != null)
      {
        int n_macros = 0;
        while (matcher.find())
        {
          n_macros++;
          String value = matcher.group(2).replaceAll("\\$", "&dollar;");
          value = value.replaceAll("\\\\", "&backslash;");
          stringMap.put(matcher.group(1), value);
        }

        log.info("Number of macro definitions loaded: " + n_macros);
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
  public boolean printMacroMap(String filename, String lang)
  {
    try
    {
      OutputStream fout = new FileOutputStream(filename);
      OutputStream bout = new BufferedOutputStream(fout);
      OutputStreamWriter out = new OutputStreamWriter(bout, "ISO-8859-1");

      Map<String, String> macroMap = null;

      if (lang.equals("EN"))
      {
        macroMap = macroMapEN;
      }

      if (lang.equals("DE"))
      {
        macroMap = macroMapDE;
      }

      if (lang.equals("IT"))
      {
        macroMap = macroMapIT;
      }

      if (macroMap == null)
      {
        return false;
      }

      for (Map.Entry<String, String> entry : macroMap.entrySet())
      {
        String key = entry.getKey();
        String value = entry.getValue();

        out.write(key + " = " + value + "\n");
      }

      // Don't forget to flush!
      out.flush(); 
      out.close();
    }
    catch (UnsupportedEncodingException e)
    {
      log.error("This VM does not support the UTF-8 character set.");
    }
    catch (IOException e)
    {
      log.error(e.getMessage());
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
  public static String replaceMacros(String s, String lang)
  {
    String result = s;
    log.debug("+++ " + result);

    result = replaceMacrosMasked(s, lang);
    log.debug("*** " + result);

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
  private static String replaceMacrosMasked(String s, String lang)
  {
    Map<String, String> macroMap = null;
    lang = lang.toUpperCase();

    if (lang.equals("EN"))
    {
      macroMap = macroMapEN;
    }
    else if (lang.equals("DE"))
    {
      macroMap = macroMapDE;
    }
    else if (lang.equals("IT"))
    {
      macroMap = macroMapIT;
    }
    else
    {
      log.debug("Error in replaceMacrosMasked(): lang = " + lang);
    }

    if (macroMap == null)
    {
      log.fatal("Illegal macro map -- language " + lang + " was not initalized!");
      throw new IllegalArgumentException("Language " + lang + " was not initialized!");
    }

    Reader reader;
    macroParser_Lexer lexer;
    String result = "";
    reader = new StringReader(s);
    lexer = new macroParser_Lexer(reader);
    macroParser_Parser parser = new macroParser_Parser(lexer);

    try
    {
      parser.string();
      AST ast = parser.getAST();
      AST firstchild = ast;

      // The parser happens to not create a root node of type STRING_ if
      // the tree contains only 1 token; in this case, we don't have to
      // move down first.
      if (ast.getType() == STRING_)
      {
        firstchild = ast.getFirstChild();
      }

      do
      {
        if (firstchild.getType() == MACRO)
        {
          String key = firstchild.getFirstChild().toString();

          if (macroMap.get(key.substring(1, key.length() - 1)) != null)
          {
            result += macroMap.get(key.substring(1,
                    key.length() - 1));
          }
          else
          {
            result += ("----MacroNotFound-" + key.substring(1, key.length() - 1) + "----");
            log.warn("MacroNotFound: " + key);
          }
        }
        else if (firstchild.getType() == NON_MACRO)
        {
          result += firstchild.getFirstChild().toString();
        }
      }
      while ((firstchild = firstchild.getNextSibling()) != null);

    }
    catch (RecognitionException e)
    {
      log.error(e.toString() + ": " + e.getMessage());
    }
    catch (TokenStreamException e)
    {
      log.error(e.toString() + ": " + e.getMessage());
    }

    return result;
  }
}
