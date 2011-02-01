package it.unibz.lib.bob.check;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * </p><p>
 * Convenience methods for escaping special characters related to HTML, XML,
 * and regular expressions.
 * </p>
 * </p><p>
 * To keep you safe by default, WEB4J goes to some effort to escape
 * characters in your data when appropriate, such that you <em>usually</em>
 * don't need to think too much about escaping special characters. Thus, you
 * shouldn't need to <em>directly</em> use the services of this class very often.
 * </p>
 * </p><p><span class='highlight'>
 * For Model Objects containing free form user input, it is highly recommended
 * that you use {@link SafeText}, not <tt>String</tt></span>. Free form user
 * input is open to malicious use, such as
 * <a href='http://www.owasp.org/index.php/Cross_Site_Scripting'>
 *  Cross Site Scripting
 * </a>
 * attacks. Using <tt>SafeText</tt> will protect you from such attacks, by
 * always escaping special characters automatically in its <tt>toString()</tt>
 * method.
 * </p>
 * </p><p>
 * The following WEB4J classes will automatically escape special characters
 * for you, when needed:
 * </p>
 * <ul>
 *  <li>
 *   the {@link SafeText} class, used as a building block class for your
 *   application's Model Objects, for modeling all free form user input
 *  </li>
 *  <li>
 *   the {@link Populate} tag used with forms
 *  </li>
 *  <li>
 *   the {@link Report} class used for creating quick reports
 *  </li>
 *  <li>
 *   the {@link Text}, {@link TextFlow}, and {@link Tooltips} custom tags used
 *   for translation
 *  </li>
 * </ul>
 *
 * @author manuel.kirschner@gmail.com
 * @version $Id$
 */
public final class EscapeChars
{
  /**
   * </p><p>
   *
   * </p>
   */
  private EscapeChars()
  {
    //empty - prevent construction
  }
  /**
   * </p><p>
   *
   * </p>
   */
  private static final Pattern SCRIPT = Pattern.compile(
          "<SCRIPT>", Pattern.CASE_INSENSITIVE);

  /**
   * </p><p>
   *
   * </p>
   */
  private static final Pattern SCRIPT_END = Pattern.compile("</SCRIPT>", Pattern.CASE_INSENSITIVE);

  /**
   * </p><p>
   * Escape characters for text appearing in HTML markup.
   * </p
   * </p><p>
   * This method exists as a defence against Cross Site Scripting (XSS) hacks.
   * The idea is to neutralize control characters commonly used by scripts, 
   * such that they will not be executed by the browser. This is done by
   * replacing the control characters with their escaped equivalents. See
   * {@link hirondelle.web4j.security.SafeText} as well.
   * </p>
   * </p><p>
   * The following characters are replaced with corresponding HTML character
   * entities:
   * </p>
   * <table border='1' cellpadding='3' cellspacing='0'>
   *  <tr><th> Character </th><th>Replacement</th></tr>
   *  <tr><td> < </td><td> &lt; </td></tr>
   *  <tr><td> > </td><td> &gt; </td></tr>
   *  <tr><td> & </td><td> &amp; </td></tr>
   *  <tr><td> " </td><td> &quot;</td></tr>
   *  <tr><td> \t </td><td> &#009;</td></tr>
   *  <tr><td> ! </td><td> &#033;</td></tr>
   *  <tr><td> # </td><td> &#035;</td></tr>
   *  <tr><td> $ </td><td> &#036;</td></tr>
   *  <tr><td> % </td><td> &#037;</td></tr>
   *  <tr><td> ' </td><td> &#039;</td></tr>
   *  <tr><td> ( </td><td> &#040;</td></tr>
   *  <tr><td> ) </td><td> &#041;</td></tr>
   *  <tr><td> * </td><td> &#042;</td></tr>
   *  <tr><td> + </td><td> &#043; </td></tr>
   *  <tr><td> , </td><td> &#044; </td></tr>
   *  <tr><td> - </td><td> &#045; </td></tr>
   *  <tr><td> . </td><td> &#046; </td></tr>
   *  <tr><td> / </td><td> &#047; </td></tr>
   *  <tr><td> : </td><td> &#058;</td></tr>
   *  <tr><td> ; </td><td> &#059;</td></tr>
   *  <tr><td> = </td><td> &#061;</td></tr>
   *  <tr><td> ? </td><td> &#063;</td></tr>
   *  <tr><td> @ </td><td> &#064;</td></tr>
   *  <tr><td> [ </td><td> &#091;</td></tr>
   *  <tr><td> \ </td><td> &#092;</td></tr>
   *  <tr><td> ] </td><td> &#093;</td></tr>
   *  <tr><td> * </td><td> &#094;</td></tr>
   *  <tr><td> _ </td><td> &#095;</td></tr>
   *  <tr><td> ` </td><td> &#096;</td></tr>
   *  <tr><td> { </td><td> &#123;</td></tr>
   *  <tr><td> | </td><td> &#124;</td></tr>
   *  <tr><td> } </td><td> &#125;</td></tr>
   *  <tr><td> ~ </td><td> &#126;</td></tr>
   * </table>
   * </p><p>
   * Note that JSTL's {@code <c:out>} escapes <em>only the first five</em>
   * of the above characters.
   * </p>
   *
   * @param aText
   * @return
   */
  public static String forHTML(String aText)
  {
    final StringBuilder result = new StringBuilder();
    final StringCharacterIterator iterator = new StringCharacterIterator(aText);
    char character = iterator.current();

    while (character != CharacterIterator.DONE)
    {
      if (character == '<')
      {
        result.append("&lt;");
      }
      else if (character == '>')
      {
        result.append("&gt;");
      }
      else if (character == '&')
      {
        result.append("&amp;");
      }
      else if (character == '\"')
      {
        result.append("&quot;");
      }
      else if (character == '\t')
      {
        addCharEntity(9, result);
      }
      else if (character == '!')
      {
        addCharEntity(33, result);
      }
      else if (character == '#')
      {
        addCharEntity(35, result);
      }
      else if (character == '$')
      {
        addCharEntity(36, result);
      }
      else if (character == '%')
      {
        addCharEntity(37, result);
      }
      else if (character == '\'')
      {
        addCharEntity(39, result);
      }
      else if (character == '(')
      {
        addCharEntity(40, result);
      }
      else if (character == ')')
      {
        addCharEntity(41, result);
      }
      else if (character == '*')
      {
        addCharEntity(42, result);
      }
      else if (character == '+')
      {
        addCharEntity(43, result);
      }
      else if (character == ',')
      {
        addCharEntity(44, result);
      }
      else if (character == '-')
      {
        addCharEntity(45, result);
      }
      else if (character == '.')
      {
        addCharEntity(46, result);
      }
      else if (character == '/')
      {
        addCharEntity(47, result);
      }
      else if (character == ':')
      {
        addCharEntity(58, result);
      }
      else if (character == ';')
      {
        addCharEntity(59, result);
      }
      else if (character == '=')
      {
        addCharEntity(61, result);
      }
      else if (character == '?')
      {
        addCharEntity(63, result);
      }
      else if (character == '@')
      {
        addCharEntity(64, result);
      }
      else if (character == '[')
      {
        addCharEntity(91, result);
      }
      else if (character == '\\')
      {
        addCharEntity(92, result);
      }
      else if (character == ']')
      {
        addCharEntity(93, result);
      }
      else if (character == '^')
      {
        addCharEntity(94, result);
      }
      else if (character == '_')
      {
        addCharEntity(95, result);
      }
      else if (character == '`')
      {
        addCharEntity(96, result);
      }
      else if (character == '{')
      {
        addCharEntity(123, result);
      }
      else if (character == '|')
      {
        addCharEntity(124, result);
      }
      else if (character == '}')
      {
        addCharEntity(125, result);
      }
      else if (character == '~')
      {
        addCharEntity(126, result);
      }
      else
      {
        //the char is not a special one
        //add it to the result as is
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }

  /**
   * <p>
   * Escape all ampersand characters in a URL.
   * </p>
   * <p>
   * Replaces all <tt>'&'</tt> characters with <tt>'&amp;'</tt>.
   * </p>
   * <p>
   * An ampersand character may appear in the query string of a URL. The
   * ampersand character is indeed valid in a URL. <em>However, URLs usually
   * appear as an <tt>HREF</tt> attribute, and such attributes have the
   * additional constraint that ampersands must be escaped.</em>
   * </p>
   * <p>
   * The JSTL<tt>c:url</tt> tag does indeed perform proper URL encoding of
   * query parameters. But it does not, in general, produce text which
   * is valid as an <tt>HREF</tt> attribute, simply because it does
   * not escape the ampersand character. This is a nuisance when
   * multiple query parameters appear in the URL, since it requires a little
   * extra work.
   * </p>
   *
   * @param aURL
   * @return
   */
  public static String forHrefAmpersand(String aURL)
  {
    return aURL.replace("&", "&amp;");
  }

  /**
   * <p>
   * Synonym for <tt>URLEncoder.encode(String, "UTF-8")</tt>.
   * </p>
   * <p>
   * Used to ensure that HTTP query strings are in proper form, by escaping
   * special characters such as spaces.
   * </p>
   * <p>
   * It is important to note that if a query string appears in an <tt>HREF</tt>
   * attribute, then there are two issues - ensuring the query string is valid HTTP
   * (it is URL-encoded), and ensuring it is valid HTML (ensuring the
   * ampersand is escaped).
   * </p>
   *
   * @param aURLFragment
   * @return
   */
  public static String forURL(String aURLFragment)
  {
    String result = null;
    try
    {
      result = URLEncoder.encode(aURLFragment, "UTF-8");
    }
    catch (UnsupportedEncodingException ex)
    {
      throw new RuntimeException("UTF-8 not supported", ex);
    }
    return result;
  }

  /**
   * <p>
   * Escape characters for text appearing as XML data, between tags.
   * </p>
   * <p>
   * The following characters are replaced with corresponding character
   * entities:
   * </p>
   * <table border='1' cellpadding='3' cellspacing='0'>
   *  <tr><th> Character </th><th> Encoding </th></tr>
   *  <tr><td> < </td><td> &lt; </td></tr>
   *  <tr><td> > </td><td> &gt; </td></tr>
   *  <tr><td> & </td><td> &amp; </td></tr>
   *  <tr><td> " </td><td> &quot;</td></tr>
   *  <tr><td> ' </td><td> &#039;</td></tr>
   * </table>
   * <p>
   * Note that JSTL's {@code <c:out>} escapes the exact same set of characters
   * as this method. <span class='highlight'>That is, {@code <c:out>}
   * is good for escaping to produce valid XML, but not for producing safe
   * HTML.</span>
   * </p>
   *
   * @param aText
   * @return
   */
  public static String forXML(String aText)
  {
    final StringBuilder result = new StringBuilder();
    final StringCharacterIterator iterator = new StringCharacterIterator(aText);
    char character = iterator.current();
    while (character != CharacterIterator.DONE)
    {
      if (character == '<')
      {
        result.append("&lt;");
      }
      else if (character == '>')
      {
        result.append("&gt;");
      }
      else if (character == '\"')
      {
        result.append("&quot;");
      }
      else if (character == '\'')
      {
        result.append("&#039;");
      }
      else if (character == '&')
      {
        result.append("&amp;");
      }
      else
      {
        //the char is not a special one
        //add it to the result as is
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }

  /**
   * <p>
   * Escapes characters for text appearing as data in the
   * <a href='http://www.json.org/'>Javascript Object Notation</a>
   * (JSON) data interchange format.
   * </p>
   * <p>
   * The following commonly used control characters are escaped :
   * </p>
   * <table border='1' cellpadding='3' cellspacing='0'>
   *  <tr><th> Character </th><th> Escaped As </th></tr>
   *  <tr><td> " </td><td> \" </td></tr>
   *  <tr><td> \ </td><td> \\ </td></tr>
   *  <tr><td> / </td><td> \/ </td></tr>
   *  <tr><td> back space </td><td> \b </td></tr>
   *  <tr><td> form feed </td><td> \f </td></tr>
   *  <tr><td> line feed </td><td> \n </td></tr>
   *  <tr><td> carriage return </td><td> \r </td></tr>
   *  <tr><td> tab </td><td> \t </td></tr>
   *  </table>
   *</p>
   * <p>
   * See <a href='http://www.ietf.org/rfc/rfc4627.txt'>RFC 4627</a>
   * for more information.
   * </p>
   *
   * @param aText
   * @return
   */
  public static String forJSON(String aText)
  {
    final StringBuilder result = new StringBuilder();
    StringCharacterIterator iterator = new StringCharacterIterator(aText);
    char character = iterator.current();

    while (character != StringCharacterIterator.DONE)
    {
      if (character == '\"')
      {
        result.append("\\\"");
      }
      else if (character == '\\')
      {
        result.append("\\\\");
      }
      else if (character == '/')
      {
        result.append("\\/");
      }
      else if (character == '\b')
      {
        result.append("\\b");
      }
      else if (character == '\f')
      {
        result.append("\\f");
      }
      else if (character == '\n')
      {
        result.append("\\n");
      }
      else if (character == '\r')
      {
        result.append("\\r");
      }
      else if (character == '\t')
      {
        result.append("\\t");
      }
      else
      {
        //the char is not a special one
        //add it to the result as is
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }

  /**
   * <p>
   * Return <tt>aText</tt> with all <tt>'<'</tt> and <tt>'>'</tt> characters
   * replaced by their escaped equivalents.
   * </p>
   *
   * @param aText
   * @return
   */
  public static String toDisableTags(String aText)
  {
    final StringBuilder result = new StringBuilder();
    final StringCharacterIterator iterator = new StringCharacterIterator(aText);
    char character = iterator.current();

    while (character != CharacterIterator.DONE)
    {
      if (character == '<')
      {
        result.append("&lt;");
      }
      else if (character == '>')
      {
        result.append("&gt;");
      }
      else
      {
        //the char is not a special one
        //add it to the result as is
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }

  /**
   * <p>
   * Replace characters having special meaning in regular expressions
   * with their escaped equivalents, preceded by a '\' character.
   * </p>
   * <p>
   * The escaped characters include:
   * </p>
   * <ul>
   *  <li>.
   *  <li>\
   *  <li>?, * , and +
   *  <li>&
   *  <li>:
   *  <li>{ and }
   *  <li>[ and ]
   *  <li>( and )
   *  <li>* and $
   * </ul>
   *
   * @param aRegexFragment
   * @return
   */
  public static String forRegex(String aRegexFragment)
  {
    final StringBuilder result = new StringBuilder();

    final StringCharacterIterator iterator =
            new StringCharacterIterator(aRegexFragment);
    char character = iterator.current();
    while (character != CharacterIterator.DONE)
    {
      /*
      All literals need to have backslashes doubled.
       */
      if (character == '.')
      {
        result.append("\\.");
      }
      else if (character == '\\')
      {
        result.append("\\\\");
      }
      else if (character == '?')
      {
        result.append("\\?");
      }
      else if (character == '*')
      {
        result.append("\\*");
      }
      else if (character == '+')
      {
        result.append("\\+");
      }
      else if (character == '&')
      {
        result.append("\\&");
      }
      else if (character == ':')
      {
        result.append("\\:");
      }
      else if (character == '{')
      {
        result.append("\\{");
      }
      else if (character == '}')
      {
        result.append("\\}");
      }
      else if (character == '[')
      {
        result.append("\\[");
      }
      else if (character == ']')
      {
        result.append("\\]");
      }
      else if (character == '(')
      {
        result.append("\\(");
      }
      else if (character == ')')
      {
        result.append("\\)");
      }
      else if (character == '^')
      {
        result.append("\\^");
      }
      else if (character == '$')
      {
        result.append("\\$");
      }
      else
      {
        //the char is not a special one
        //add it to the result as is
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }

  /**
   * <p>
   * Escape <tt>'$'</tt> and <tt>'\'</tt> characters in replacement strings.
   * </p>
   * <p>
   * Synonym for <tt>Matcher.quoteReplacement(String)</tt>.
   *</p>
   * <p>
   * The following methods use replacement strings which treat <tt>'$'</tt>
   * and <tt>'\'</tt> as special characters:
   * </p>
   * <ul>
   *  <li><tt>String.replaceAll(String, String)</tt>
   *  <li><tt>String.replaceFirst(String, String)</tt>
   *  <li><tt>Matcher.appendReplacement(StringBuffer, String)</tt>
   * </ul>
   * <p>
   * If replacement text can contain arbitrary characters, then you
   * will usually need to escape that text, to ensure special characters
   * are interpreted literally.
   * </p>
   */
  public static String forReplacementString(String aInput)
  {
    return Matcher.quoteReplacement(aInput);
  }

  /**
   * <p>
   * Disable all <tt><SCRIPT></tt> tags in <tt>aText</tt>.
   * </p>
   *
   * <p>
   * Insensitive to case.
   * </p>
   *
   * @param aText
   * @return
   */
  public static String forScriptTagsOnly(String aText)
  {
    String result = null;
    Matcher matcher = SCRIPT.matcher(aText);
    result = matcher.replaceAll("&lt;SCRIPT>");
    matcher = SCRIPT_END.matcher(result);
    result = matcher.replaceAll("&lt;/SCRIPT>");
    return result;
  }

  /**
   * <p>
   * 
   * </p>
   *
   * @param aIdx
   * @param aBuilder
   */
  private static void addCharEntity(Integer aIdx, StringBuilder aBuilder)
  {
    String padding = "";

    if (aIdx <= 9)
    {
      padding = "00";
    }
    else if (aIdx <= 99)
    {
      padding = "0";
    }
    else
    {
      //no prefix
    }

    String number = padding + aIdx.toString();
    aBuilder.append("&#" + number + ";");
  }
}
