package it.unibz.lib.bob.check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * Parses a user question that had been identified to contain an OPAC query, and returns the contained query terms
 *
 * @author manuelkirschner
 * @version $Id$
 *
 */
public class OpacQueryQuestionParser
{
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
  private static Logger log = Logger.getLogger(OpacQueryQuestionParser.class);

  /*
   * Keyword questions
   */
  static class KeywordQuestion
  {
    private static final String about_EN = "(?:of|on|by|about)";

    private static final String question_EN = ".*\\b" + about_EN
            + "\\b ?([ \\w]*).*";

    // private static String keywordQuestion_EN = ".*(.*).*";
    static Pattern question_EN_pattern = Pattern.compile(question_EN,
            Pattern.CASE_INSENSITIVE | Pattern.CANON_EQ
            | Pattern.UNICODE_CASE);

    private static final String about_DE = "(?:[üu]ber|von)";

    private static final String question_DE = ".*\\b" + about_DE
            + "\\b ?([ \\w]*).*";

    static Pattern question_DE_pattern = Pattern.compile(question_DE,
            Pattern.CASE_INSENSITIVE | Pattern.CANON_EQ
            | Pattern.UNICODE_CASE);

    private static final String about_IT = "(?:su|di|da)";

    private static final String question_IT = ".*\\b" + about_IT
            + "\\b ?([ \\w]*).*";

    static Pattern question_IT_pattern = Pattern.compile("(?i)"
            + question_IT, Pattern.CASE_INSENSITIVE | Pattern.CANON_EQ
            | Pattern.UNICODE_CASE);

    /**
     *
     * @param str
     * @param language
     * @return the extracted query terms
     * @throws Exception
     */
    public static String getQueryTerms(String str, String language)
    {
      Matcher matcher = null;
      
      if (language.equals("EN"))
      {
        matcher = KeywordQuestion.question_EN_pattern.matcher(str);
      }
      else if (language.equals("DE"))
      {
        matcher = KeywordQuestion.question_DE_pattern.matcher(str);
      }
      else if (language.equals("IT"))
      {
        matcher = KeywordQuestion.question_IT_pattern.matcher(str);
      }
      else
      {
        log.error("Illegal language!");
      }

      boolean matchFound = matcher.find();

      if (matchFound)
      {
        return matcher.group(1);
      }
      else
      {
        return "";
      }
    }
  }
}
