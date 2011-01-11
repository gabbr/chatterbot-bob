// $ANTLR : "macroParser.g" -> "macroParser_TreeParser.java"$
package it.unibz.lib.bob.macroparser;

import antlr.TreeParser;
import antlr.Token;
import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.collections.impl.BitSet;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

/**
 *
 * @version $Id: macroParser_TreeParser.java 313 2010-12-22 10:41:47Z markus.grandpre $
 */
public class macroParser_TreeParser extends antlr.TreeParser implements macroParser_LexerTokenTypes
{
  public macroParser_TreeParser()
  {
    tokenNames = _tokenNames;
  }

  public final String string(AST _t) throws RecognitionException
  {
    String result;

    AST string_AST_in = (_t == ASTNULL) ? null : (AST) _t;
    AST ast = null;

    result = "";


    try
    {      // for error handling
      ast = (AST) _t;
      match(_t, STRING_);
      _t = _t.getNextSibling();

      AST firstchild = ast.getFirstChild();
      do
      {
        if (firstchild.getType() == MACRO)
        {
          result += "MACRO";
        }
        else if (firstchild.getType() == NON_MACRO)
        {
          result += firstchild.getFirstChild().toString();
        }
      }
      while ((firstchild = firstchild.getNextSibling()) != null);

    }
    catch (RecognitionException ex)
    {
      reportError(ex);
      if (_t != null)
      {
        _t = _t.getNextSibling();
      }
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
    "NON_MACRO",
    "MACRO",
    "STRING_"
  };

}
