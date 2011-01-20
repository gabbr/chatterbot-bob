package it.unibz.lib.bob.qcheck;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;

import it.unibz.lib.bob.check.BobHelper;
import it.unibz.lib.bob.check.MyAST;
import it.unibz.lib.bob.check.Bob_Lexer;
import it.unibz.lib.bob.check.Bob_Parser;
import it.unibz.lib.bob.check.Bob_TreeParser;
import it.unibz.lib.bob.check.BobHelper.Pair;
import it.unibz.lib.bob.check.DialogueManager;

import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 *
 * @version $Id$
 */
public class QCheckImpl implements QCheck
{
  private static BobHelper sh = null;

  @Override
  public String performQCheck(String macroFile, String regularExpression,
          String userQuestion, String format)
  {
    String testResults = new String();

    if (regularExpression.length() > 0)
    {
      sh = new BobHelper(macroFile, "unknown_GUI_language");

      // just print warnings/errors to console for now:
      sh.checkAllRegexesInMacroMap();

      Pair[] longestMacros = sh.get3OverallLongestMacros();

      DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      Date date = new Date();
      String loadTime = dateFormat.format(date);

      testResults = testResults + "Macro file loaded at "
              + loadTime
              + ": "
              // + macroFileChooser.getSelectedFile().getCanonicalPath()
              + macroFile + "\nIts 3 longest macros:\n"
              + longestMacros[0].getMacro() + " -> "
              + longestMacros[0].getLength() + "\n"
              + longestMacros[1].getMacro() + " -> "
              + longestMacros[1].getLength() + "\n"
              + longestMacros[2].getMacro() + " -> "
              + longestMacros[2].getLength() + "\n";

      String regexResolved = sh.replaceMacros(regularExpression, "");
      testResults = testResults + regexResolved + "\n";

      if (format.equals("macro"))
      {
        // macro format
        regexResolved = '"' + regexResolved + '"';
      }
      else
      {
        // topic tree format
        regexResolved = regexResolved.replaceAll("_", "\\\\b");
      }

      regexResolved = regexResolved.replaceAll("&backslash;", "\\\\");
      regexResolved = regexResolved.replaceAll("&dollar;", "\\$");
      regexResolved = regexResolved.replaceAll("&amp;", "&");
      regexResolved = regexResolved.replaceAll("&lt;", "<");
      regexResolved = regexResolved.replaceAll("&gt;", ">");

      String stringToMatch = userQuestion;

      // remove accents, as in new versions of BoB

      stringToMatch = DialogueManager.removeAccents(stringToMatch);

      // show replaced text in UI
      testResults = testResults + stringToMatch + "\n";

      Reader reader;

      Bob_Lexer lexer;
      Bob_Parser parser;
      Bob_TreeParser treeparser;

      try
      {
        reader = new StringReader(regexResolved);
        lexer = new Bob_Lexer(reader);
        parser = new Bob_Parser(lexer);
        parser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");
        treeparser = new Bob_TreeParser();
        treeparser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");

        parser.bExpression();
        MyAST tree = (MyAST) parser.getAST();

        /*
        if (astFrame != null)
        {
          astFrame.removeAll();
          astFrame.setVisible(false);
        }

        astFrame = new ASTFrame("Regular Expression", tree);
        astFrame.setVisible(true);

        */

        if (treeparser.bExpression(tree, stringToMatch) == true)
        {
          testResults = testResults + "Test succeeded, user question is ok.\n";
        }
        else
        {
          testResults = testResults + "Test failed, user question is not ok.\n";
        }

      }
      catch (RecognitionException e)
      {
        testResults = testResults + "Error in boolean part of regular "
                + "expression (i.e., outside of the double quotes): "
                + e.toString() + "\n";
      }
      catch (TokenStreamException e)
      {
        testResults = testResults + "Error in boolean part of regular "
                + "expression (i.e., outside of the double quotes): "
                + e.toString() + "\n";
      }
      catch (ANTLRException e)
      {
        testResults = testResults + "Error in boolean part of regular "
                + "expression (i.e., outside of the double quotes): "
                + e.toString() + "\n";
      }
      catch (Exception e)
      {
        if (e.getMessage().endsWith("Expression is too large."))
        {
          testResults = testResults + "Error in Perl regex part of regular "
                  + "expression (i.e., inside the double quotes): Pattern is "
                  + "too complex!\n"
                  + "\nThese are the 3 longest macros used in this pattern "
                  + "(to be removed by hand, or shortened considerably):\n"
                  + longestMacros[0].getMacro()
                  + " -> "
                  + longestMacros[0].getLength()
                  + "\n"
                  + longestMacros[1].getMacro()
                  + " -> "
                  + longestMacros[1].getLength()
                  + "\n"
                  + longestMacros[2].getMacro()
                  + " -> "
                  + longestMacros[2].getLength() + "\n";
        }
        else
        {
          testResults = testResults + "Other error in Perl regex part of "
                  + "regular expression (i.e., inside the double quotes):\n"
                  + e.toString();
        }
      }
    }

    return testResults;
  }
}
