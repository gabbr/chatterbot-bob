package it.unibz.lib.bob.check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a user question that had been identified to contain an OPAC query, and returns the contained query terms
 *
 * @author manuelkirschner
 * @version $Id$
 *
 */
public class OpacQueryQuestionParser
{

  /*
   * Keyword questions
   */
  static class KeywordQuestion
  {
    private static String about_EN = "(?:of|on|by|about)";

    private static String question_EN = ".*\\b" + about_EN
            + "\\b ?([ \\w]*).*";

    // private static String keywordQuestion_EN = ".*(.*).*";
    static Pattern question_EN_pattern = Pattern.compile(question_EN,
            Pattern.CASE_INSENSITIVE | Pattern.CANON_EQ
            | Pattern.UNICODE_CASE);

    private static String about_DE = "(?:[üu]ber|von)";

    private static String question_DE = ".*\\b" + about_DE
            + "\\b ?([ \\w]*).*";

    static Pattern question_DE_pattern = Pattern.compile(question_DE,
            Pattern.CASE_INSENSITIVE | Pattern.CANON_EQ
            | Pattern.UNICODE_CASE);

    private static String about_IT = "(?:su|di|da)";

    private static String question_IT = ".*\\b" + about_IT
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
        System.err.println("Illegal language!");
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

  public static void main(String[] args)
  {
    String input = "do you have books about Noam chomsky. thanks!";
    System.out.println("EN "
            + OpacQueryQuestionParser.KeywordQuestion.getQueryTerms(input,
            "EN"));
    input = "buecher über Noam chomsky.";
    System.out.println("DE "
            + OpacQueryQuestionParser.KeywordQuestion.getQueryTerms(input,
            "DE"));
    input = "libri da Noam chomsky";
    System.out.println("IT "
            + OpacQueryQuestionParser.KeywordQuestion.getQueryTerms(input,
            "IT"));

  }
}
