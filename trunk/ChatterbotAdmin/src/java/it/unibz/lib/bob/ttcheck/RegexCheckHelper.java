package it.unibz.lib.bob.ttcheck;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator; //import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;

import it.unibz.lib.bob.check.Bob_Lexer;
import it.unibz.lib.bob.check.Bob_Parser;
import it.unibz.lib.bob.check.Bob_TreeParser;

import it.unibz.lib.bob.check.MyAST;
import it.unibz.lib.bob.check.BobHelper;
import it.unibz.lib.bob.check.BobHelper.Pair;

import org.apache.log4j.Logger;

/**
 * <p>
 * 
 * </p>
 * 
 * @author giedre
 * @author manuel.kirschner@gmail.com
 * @version $Id$
 */
public class RegexCheckHelper extends DefaultHandler
{

  /**
   * <p>
   * 
   * </p>
   */
  private static BobHelper helper_DE = null;

  /**
   * <p>
   * 
   * </p>
   */
  private static BobHelper helper_EN = null;

  /**
   * <p>
   * 
   * </p>
   */
  private static BobHelper helper_IT = null;

  /**
   * <p>
   * 
   * </p>
   */
  private static java.util.List<String> languagesToCheck = new java.util.LinkedList<String>();

  /**
   * <p>
   * 
   * </p>
   */
  private static String testResults = new String();

  /**
   * <p>
   * 
   * </p>
   */
  static
  {
    languagesToCheck.add("DE");
    languagesToCheck.add("EN");
    languagesToCheck.add("IT");
  }

  /**
   * <p>
   * 
   * </p>
   */
  private java.util.Stack<String> path = new java.util.Stack<String>();

  /**
   * <p>
   * 
   * </p>
   */
  private java.util.HashMap<String, String> values = new java.util.HashMap<String, String>();

  /**
   * <p>
   * 
   * </p>
   */
  private Locator locator;

  /**
   * <p>
   * 
   * </p>
   */
  private int errNum;

  /**
   * <p>
   * 
   * </p>
   */
  private int lineNum = 0;

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
  private Logger log = Logger.getLogger(RegexCheckHelper.class);

  /**
   * <p>
   * Does the checking of all regular expressions in the XML focus tree
   * </p>
   *
   * @param fileFocusTree
   * @param abbrevFile_DE
   * @param abbrevFile_EN
   * @param abbrevFile_IT
   * @throws Exception
   */
  public static String doRegexChecks(File fileFocusTree, String abbrevFile_DE,
    String abbrevFile_EN, String abbrevFile_IT) throws Exception
  {
    /*
     * Check abbrev files alone
     */
    testResults = testResults + "\n\nChecking macro file "
      + abbrevFile_DE + " ...";

    // make an URL
    helper_DE = new BobHelper(abbrevFile_DE, "abbrevFile_DE");
    if (helper_DE.checkAllRegexesInMacroMap() == true)
    {
      Pair[] longestMacros = helper_DE.get3OverallLongestMacros();
      testResults = testResults + "The 3 longest macros:\n"
        + longestMacros[0].getMacro() + " -> "
        + longestMacros[0].getLength() + "\n"
        + longestMacros[1].getMacro() + " -> "
        + longestMacros[1].getLength() + "\n"
        + longestMacros[2].getMacro() + " -> "
        + longestMacros[2].getLength() + "\n";

      testResults = testResults + "+++ OK: German macro file is correct.\n";

    }
    else
    {
      testResults = testResults + "+++ ERROR: German macro file contains "
        + "errors.\n";

    }

    testResults = testResults + "\n\nChecking macro file "
      + abbrevFile_EN + " ...";

    // make an URL
    helper_EN = new BobHelper(abbrevFile_EN, "abbrevFile_EN");
    if (helper_EN.checkAllRegexesInMacroMap() == true)
    {
      Pair[] longestMacros = helper_EN.get3OverallLongestMacros();
      testResults = testResults + "The 3 longest macros:\n"
        + longestMacros[0].getMacro() + " -> "
        + longestMacros[0].getLength() + "\n"
        + longestMacros[1].getMacro() + " -> "
        + longestMacros[1].getLength() + "\n"
        + longestMacros[2].getMacro() + " -> "
        + longestMacros[2].getLength() + "\n";

      testResults = testResults + "+++ OK: English macro file is correct.\n";

    }
    else
    {
      testResults = testResults + "+++ ERROR: English macro file contains "
        + "errors.\n";
    }

    testResults = testResults + "\n\nChecking macro file "
      + abbrevFile_IT + " ...";


    // make an URL
    helper_IT = new BobHelper(abbrevFile_IT, "abbrevFile_IT");
    if (helper_IT.checkAllRegexesInMacroMap() == true)
    {
      Pair[] longestMacros = helper_IT.get3OverallLongestMacros();
      testResults = testResults + "The 3 longest macros:\n"
        + longestMacros[0].getMacro() + " -> "
        + longestMacros[0].getLength() + "\n"
        + longestMacros[1].getMacro() + " -> "
        + longestMacros[1].getLength() + "\n"
        + longestMacros[2].getMacro() + " -> "
        + longestMacros[2].getLength() + "\n";

      testResults = testResults + "+++ OK: Italian macro file is correct.\n";

    }
    else
    {
      testResults = testResults + "+++ ERROR: Italian macro file contains "
        + "errors.\n";
    }

    /*
     * Now check the topic tree
     */
    testResults = testResults + "\n\nChecking all question patterns in the Topic "
      + "Tree ...\n";


    DefaultHandler handler = new RegexCheckHelper();
    // ContentHandler handler = new Regex();

    SAXParserFactory factory = SAXParserFactory.newInstance();
    // try {
    // out = new OutputStreamWriter(System.out, "UTF8");

    // parse the input
    SAXParser saxParser = factory.newSAXParser();
    saxParser.parse(fileFocusTree, handler);

    // } catch (ParserConfigurationException e) {
    // System.err.println(e.toString());

    // }
    // System.exit(1);
    testResults = testResults + "\n\nFinished checking all question patterns in the topic tree.\n";
    return testResults;

  }

  @Override
  public void startElement(String uri, String localName, String qName,
    // Attributes attributes) throws SAXException {
    Attributes attributes)
  {
    path.push(qName);
    values.put(qName, "");
  }

  @Override
  public void setDocumentLocator(Locator locator)
  {
    this.locator = locator;
  }

  /**
   * <p>
   * 
   * </p>
   * 
   * @return 
   */
  private int getLineNumber()
  {
    int line = locator.getLineNumber();
    return line;
  }

  @Override
  public void endElement(String uri, String localName, String qName)
    throws org.xml.sax.SAXException
  {
    path.pop();
    if (qName.equals("regex"))
    {
      for (String lang : languagesToCheck)
      {
        String regexElem = values.get(lang);
        if (regexElem != "" && regexElem != null)
        {

          // String buf = regexElem.trim();

          String expandedRegex = null;
          if (lang.equals("DE"))
          {
            expandedRegex = helper_DE.replaceMacros(regexElem, "DE");
          }
          else if (lang.equals("EN"))
          {
            expandedRegex = helper_EN.replaceMacros(regexElem, "EN");
          }
          else if (lang.equals("IT"))
          {
            expandedRegex = helper_IT.replaceMacros(regexElem, "IT");
          }

          Reader reader;
          // any string to match, we don't care for result
          String stringToMatch = "#POSITIVE##SATZENDE#";

          // String result = null;

          Bob_Lexer lexer;
          Bob_Parser parser;
          Bob_TreeParser treeparser;
          MyAST tree = null;
          try
          {

            reader = new StringReader(expandedRegex);
            lexer = new Bob_Lexer(reader);
            parser = new Bob_Parser(lexer);
            parser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");
            treeparser = new Bob_TreeParser();
            treeparser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");

            parser.bExpression();
            tree = (MyAST) parser.getAST();
            // result = "(" + treeparser.bExpression(tree,
            // stringToMatch) + ")";
            treeparser.bExpression(tree, stringToMatch);
            // System.out.println(result);

            // output AST
						/*
             * try { BufferedWriter out = new BufferedWriter( new
             * OutputStreamWriter(new FileOutputStream( "ast.txt"),
             * "UTF8")); out.write(tree.toStringTree());
             * out.close(); } catch (UnsupportedEncodingException
             * ue) { System.err.println("Not supported : "); } catch
             * (IOException e) { System.err.println(e.getMessage());
             * }
             */
            /*
             * ASTFrame frame = new ASTFrame("The tree", tree);
             * frame.setVisible(true);
             */
            // end output AST
          }
          catch (RecognitionException e)
          {
            testResults = testResults + "+++ Error in a regular expression "
              + "pattern (OUTSIDE of quotation marks (\"\")):\n"
              + e.toString();

            testResults = testResults + "(around line number " + (errNum)
              + " in the Topic Tree xml file)";

            // throw new SAXException("Regex-Fehler");
            // System.exit(1);
          }
          catch (TokenStreamException e)
          {
            testResults = testResults + "+++ Error in a regular expression "
              + "pattern (INSIDE of quotation marks (\"\")): Invalid "
              + "character.\n" + e.toString();

            testResults = testResults + "(around line number " + (errNum)
              + " in the Topic Tree xml file)";


          }
          catch (ANTLRException e)
          {
            testResults = testResults + "+++ Error in the regular expression "
              + "pattern. " + e.toString();

            testResults = testResults + "(around line number " + (errNum)
              + " in the Topic Tree xml file)";


          }
          catch (Exception e)
          {

            if (e.getMessage().endsWith("Expression is too large."))
            {
              testResults = testResults + "+++ Error in a regular expression "
                + "pattern (INSIDE of quotation marks (\"\")): Pattern "
                + "is too large/complex!";
            }
            else if (e.getMessage().endsWith(
              "Unmatched parentheses."))
            {
              testResults = testResults + "+++ Error in a regular expression "
                + "pattern (INSIDE of quotation marks (\"\")): "
                + "Parentheses don't match:\n" + e.toString();

            }
            else
            {
              testResults = testResults + "+++ Unknown error in the regular "
                + "expression pattern (INSIDE of quotation marks (\"\")):\n"
                + e.toString();

            }
            testResults = testResults + "(around line number " + (errNum)
              + " in the Topic Tree xml file)";



          }
        }
      }
    }
  }

  @Override
  public void characters(char[] ch, int start, int length)
  {
    String currentTag = path.peek();

    // look only for //regex/languages/XX, where XX is in languagesToCheck
    if (languagesToCheck.contains(currentTag) && path.search("regex") == 3)
    {
      String text = new String(ch, start, length);
      String currentValue = values.get(currentTag);
      currentValue = currentValue + text;
      values.put(currentTag, currentValue);

      int tmpNum = getLineNumber();
      if (lineNum < tmpNum)
      {
        lineNum = tmpNum;
        errNum = getLineNumber() - 1;
      }
    }
  }

}
