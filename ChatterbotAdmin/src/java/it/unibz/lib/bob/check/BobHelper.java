package it.unibz.lib.bob.check;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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

import org.apache.log4j.Logger;

/**
 *
 * @author kirschner
 * @version $Id$
 */
public class BobHelper implements macroParser_LexerTokenTypes
{
  HashMap<String, String> macroMap;

  HashMap<String, String> macroMapSubsetUsedByCurrentPattern = new HashMap<String, String>();

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
  private Logger log = Logger.getLogger(BobHelper.class);


  /**
   *
   * @param shortcutFile
   * @param lang (just for output purposes)
   */
  public BobHelper(URL shortcutFile, String lang)
  {
    macroMap = getMacroMapFromURL(shortcutFile.toExternalForm());
    expandMacroMap(lang);
  }

  /**
   *
   * @param shortcutFile
   * @param lang
   *            (just for output purposes)
   */
  public BobHelper(String shortcutFile, String lang)
  {
    macroMap = getMacroMapFromFile(shortcutFile);
    expandMacroMap(lang);
  }

  public static String simplifyAlphabet(String regex)
  {
    return regex.toLowerCase(Locale.GERMAN);
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
  private boolean expandMacroMap(String lang)
  {
    Pattern macro = Pattern.compile("#([^#]+)#");
    Matcher matcher;
    boolean somethingChanged;
    int temp = 0;

    do
    {
      somethingChanged = false;
      temp++;
      for (Map.Entry<String, String> entry : macroMap.entrySet())
      {

        String key = entry.getKey();
        String value = entry.getValue();

        // Ignore macro FUNCTION syntax:
        value = value.replaceAll("\\$1\\$", "foobar");
        matcher = macro.matcher(value);

        if (matcher.find())
        {
          String replacementValue = replaceMacrosMasked(value, lang);
          macroMap.put(key, replacementValue);
          somethingChanged = true;
        }
      }
    }
    while (somethingChanged);

    log.debug("Number of macro expansion iterations needed: " + temp);

    return somethingChanged;
  }

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
  HashMap<String, String> getMacroMapFromURL(String urlString)
  {
    Authenticator.setDefault(new MyAuthenticator());
    URL abkuerzungen = null;

    try
    {
      abkuerzungen = new URL(urlString);
    }
    catch (MalformedURLException e)
    {
      log.error("Error: " + e.getMessage());
    }

    InputStream inputStream = null;

    try
    {
      inputStream = abkuerzungen.openStream();
    }
    catch (IOException e)
    {
      log.error("Error: " + e.getMessage());
    }

    BufferedReader in =
            new BufferedReader(new InputStreamReader(inputStream));

    String line = null;

    StringBuffer buffer = new StringBuffer();

    try
    {
      inputStream = abkuerzungen.openStream();

      while ((line = in.readLine()) != null)
      {
        buffer.append(line + "\n");
      }

      in.close();
    }
    catch (IOException e)
    {
      log.error("Error: " + e.getMessage());
    }

    String filecontent = buffer.toString();

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
          String value = matcher.group(2).replaceAll("\\$","&dollar;");
          value = value.replaceAll("\\\\", "&backslash;");
          stringMap.put(matcher.group(1), value);
        }

        log.info("Number of macro definitions loaded: " + n_macros);
      }
    }

    return stringMap;
  }

  HashMap<String, String> getMacroMapFromFile(String fileName)
  {
    String filecontent = null;
    try
    {
      FileInputStream fis = new FileInputStream(fileName);
      FileChannel fc = fis.getChannel();
      MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
      // Charset charset = Charset.forName ("UTF-8");
      Charset charset = Charset.forName("ISO-8859-1");
      CharsetDecoder decoder = charset.newDecoder();
      CharBuffer charBuffer = decoder.decode(bb);
      charBuffer.flip();
      filecontent = new String(charBuffer.array());
      fc.close();
      fc = null;
    }
    catch (IOException e)
    {
      log.error("Failed to read file: " + fileName);
      System.exit(1);
    }

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

  public void printMacroMap(String filename)
  {
    try
    {
      OutputStream fout = new FileOutputStream(filename);
      OutputStream bout = new BufferedOutputStream(fout);
      OutputStreamWriter out = new OutputStreamWriter(bout, "ISO-8859-1");

      for (Map.Entry<String, String> entry : macroMap.entrySet())
      {
        String key = entry.getKey();
        String value = entry.getValue();

        out.write(key + " = " + value + "\n");
      }

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
  }

  public class Pair
  {
    private Integer length;

    private String macro;

    public Pair(Integer a1, String a2)
    {
      length = a1;
      macro = a2;
    }

    public Integer getLength()
    {
      return length;
    }

    public String getMacro()
    {
      return macro;
    }

    public void setLength(Integer arg)
    {
      length = arg;
    }

    public void setMacro(String arg)
    {
      macro = arg;
    }
  }

  public Pair[] get3OverallLongestMacros()
  {
    Pair[] result = new Pair[3];
    result[0] = new Pair(0, "");
    result[1] = new Pair(0, "");
    result[2] = new Pair(0, "");

    for (Map.Entry<String, String> entry : macroMap.entrySet())
    {
      String macro = entry.getKey();
      int length = entry.getValue().toString().length();
      if (length > result[2].getLength())
      {
        int i = 2;
        while (i > 0 && length > result[i - 1].getLength())
        {
          i--;
        }
        for (int j = 1; j >= i; j--)
        {
          result[j + 1].setLength(result[j].getLength());
          result[j + 1].setMacro(result[j].getMacro());
        }
        result[i].setLength(length);
        result[i].setMacro(macro);
      }
    }
    return result;
  }

  public Pair[] get3CurrentPatternLongestMacros()
  {
    Pair[] result = new Pair[3];
    result[0] = new Pair(0, "");
    result[1] = new Pair(0, "");
    result[2] = new Pair(0, "");

    for (Map.Entry<String, String> entry : macroMapSubsetUsedByCurrentPattern.entrySet())
    {
      String macro = entry.getKey();
      int length = entry.getValue().toString().length();
      if (length > result[2].getLength())
      {
        int i = 2;
        while (i > 0 && length > result[i - 1].getLength())
        {
          i--;
        }
        for (int j = 1; j >= i; j--)
        {
          result[j + 1].setLength(result[j].getLength());
          result[j + 1].setMacro(result[j].getMacro());
        }
        result[i].setLength(length);
        result[i].setMacro(macro);
      }
    }
    return result;
  }

  public boolean checkAllRegexesInMacroMap() 
  {
    boolean result = true;
    for (Map.Entry<String, String> entry : macroMap.entrySet())
    {
      String key = entry.getKey();
      String value = entry.getValue();
      value = value.replaceAll("&backslash;", "\\\\");
      value = value.replaceAll("&dollar;", "\\$");

      try
      {
        if (!hasBalancedParentheses(value))
        {
          log.error("Unmatched parentheses in abbreviation: " + key);
          throw new Exception();
        }
      }
      catch (MalformedCachePatternException e)
      {
        log.error("Caught other (non-round-parentheses) regex error "
                + "for abbreviation " + key + ": " + e.getMessage());

        result = false;
      }
      catch (Exception e)
      {
        log.error(e.toString() + ": " + e.getMessage());
        result = false;
      }

      Reader reader;
      Bob_Lexer lexer;
      Bob_Parser parser;
      Bob_TreeParser treeparser;

      try
      {
        reader = new StringReader('"' + value + '"');
        lexer = new Bob_Lexer(reader);
        parser = new Bob_Parser(lexer);
        parser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");
        treeparser = new Bob_TreeParser();
        treeparser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");

        // note that we do not use bexpression (since the abkdatei
        // doesn't contain extended regex)
        parser.quotedRe();
      }
      catch (RecognitionException e)
      {
        log.error("In abbreviation definition " + key
                + ": TreeParser threw RecognitionException. "
                + e.toString() + ": " + e.getMessage());
        result = false;
      }
      catch (TokenStreamException e)
      {
        log.error("In abbreviation definition " + key
                + ": TreeParser threw TokenStreamException. "
                + e.toString() + ": " + e.getMessage());
        result = false;
      }
      catch (Exception e)
      {
        log.error("In abbreviation definition " + key
                + ": TreeParser threw Exception. " + e.toString() + ": " + e.getMessage());
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
  public String replaceMacros(String s, String lang)
  {
    String result = s;
    // log.debug("+++ " + result);
    result = replaceMacrosMasked(s, lang);
    // log.debug("*** " + result);
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
  private String replaceMacrosMasked(String s, String lang)
  {
    macroMapSubsetUsedByCurrentPattern.clear();
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
      // move
      // down first.
      if (ast.getType() == STRING_)
      {
        firstchild = ast.getFirstChild();
      }
      do
      {
        if (firstchild.getType() == MACRO)
        {
          // if (macroMap.get(firstchild.getFirstChild().toString())
          // != null) {
          String key = firstchild.getFirstChild().toString();
          if (macroMap.get(key.substring(1, key.length() - 1)) != null)
          {
            macroMapSubsetUsedByCurrentPattern.put(key.substring(1,
                    key.length() - 1), macroMap.get(key.substring(
                    1, key.length() - 1)));
            result += macroMap.get(key.substring(1,
                    key.length() - 1));
          }
          else
          {
            result += ("----MacroNotFound-"
                    + key.substring(1, key.length() - 1) + "----");
            // deactivated WARNINGS for now, until all errors are removed
            log.warn("This macro is undefined for the specified "
                    + "language: " + lang + "::"+ key);
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

  public static boolean hasBalancedParentheses(String str)
  {
    java.text.StringCharacterIterator charIt = new java.text.StringCharacterIterator(
            str);
    int nestingLevel = 0;

    for (char ch = charIt.first(); ch != java.text.StringCharacterIterator.DONE; ch = charIt.next())
    {
      if (ch == '(')
      {
        nestingLevel++;
      }
      if (ch == ')')
      {
        nestingLevel--;
      }
    }
    if (nestingLevel == 0)
    {
      return true;
    }
    return false;
  }
}
