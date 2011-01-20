// $ANTLR : "Bob.g" -> "Bob_TreeParser.java"$
package it.unibz.lib.bob.check;

import org.apache.oro.text.perl.Perl5Util;

import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;

import org.apache.log4j.Logger;

/**
 *
 * @version $Id$
 */
public class Bob_TreeParser extends antlr.TreeParser implements Bob_LexerTokenTypes
{
  public Bob_TreeParser()
  {
    tokenNames = _tokenNames;
  }

  public final boolean bExpression(AST _t,
          String query) throws RecognitionException, ANTLRException
  {
    boolean result;

    it.unibz.lib.bob.check.MyAST bExpression_AST_in = (_t == ASTNULL) ? null : (it.unibz.lib.bob.check.MyAST) _t;
    it.unibz.lib.bob.check.MyAST s = null;

    result = false;
    boolean a = false;
    boolean b = false;
    Perl5Util regex = new Perl5Util();


    try
    {      // for error handling
      if (_t == null)
      {
        _t = ASTNULL;
      }
      switch (_t.getType())
      {
        case OR:
        {
          AST __t34 = _t;
          it.unibz.lib.bob.check.MyAST tmp1_AST_in = (it.unibz.lib.bob.check.MyAST) _t;
          match(_t, OR);
          _t = _t.getFirstChild();
          a = bExpression(_t, query);
          _t = _retTree;
          b = bExpression(_t, query);
          _t = _retTree;
          _t = __t34;
          _t = _t.getNextSibling();
          result = a || b;
          break;
        }
        case AND:
        {
          AST __t35 = _t;
          it.unibz.lib.bob.check.MyAST tmp2_AST_in = (it.unibz.lib.bob.check.MyAST) _t;
          match(_t, AND);
          _t = _t.getFirstChild();
          a = bExpression(_t, query);
          _t = _retTree;
          b = bExpression(_t, query);
          _t = _retTree;
          _t = __t35;
          _t = _t.getNextSibling();
          result = a && b;
          break;
        }
        case NOT:
        {
          AST __t36 = _t;
          it.unibz.lib.bob.check.MyAST tmp3_AST_in = (it.unibz.lib.bob.check.MyAST) _t;
          match(_t, NOT);
          _t = _t.getFirstChild();
          a = bExpression(_t, query);
          _t = _retTree;
          _t = __t36;
          _t = _t.getNextSibling();
          result = !a;
          break;
        }
        case STRING_LITERAL:
        {
          s = (it.unibz.lib.bob.check.MyAST) _t;
          match(_t, STRING_LITERAL);
          _t = _t.getNextSibling();
          //try {
          result = regex.match(s + "i", query);
          //}
          //catch (MalformedCachePatternException me){
          //    System.err.println("Caught regex error"); //on line " + s.getLine());
          //    throw new ANTLRException();

          //}
          //catch (Exception me){
          //    System.err.println("Caught strange error"); //on line " + s.getLine());
          //    throw new ANTLRException();
          //}



          break;
        }
        default:
        {
          throw new NoViableAltException(_t);
        }
      }
    }
    catch (ANTLRException ex)
    {

      //reportError("Oops: " + ex.toString());

      //needed so that giedre's code can detect the error
      throw ex;

    }
    _retTree = _t;
    return result;
  }
  public static final String[] _tokenNames =
  {
    "<0>",
    "EOF",
    "<2>",
    "NULL_TREE_LOOKAHEAD",
    "OR",
    "AND",
    "NOT",
    "LEFT_PAREN",
    "RIGHT_PAREN",
    "BLANK",
    "STRING_LITERAL",
    "ESCAPE",
    "CONC_",
    "EMPTYSTRING_"
  };

}
