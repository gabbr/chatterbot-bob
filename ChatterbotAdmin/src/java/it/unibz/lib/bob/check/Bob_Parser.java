// $ANTLR : "Bob.g" -> "Bob_Parser.java"$
package it.unibz.lib.bob.check;

import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.MalformedCachePatternException;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

/**
 *
 * @version $Id: Bob_Parser.java 313 2010-12-22 10:41:47Z markus.grandpre $
 */
public class Bob_Parser extends antlr.LLkParser implements Bob_LexerTokenTypes
{
  protected Bob_Parser(TokenBuffer tokenBuf, int k)
  {
    super(tokenBuf, k);
    tokenNames = _tokenNames;
    buildTokenTypeASTClassMap();
    astFactory = new ASTFactory(getTokenTypeToASTClassMap());
  }

  public Bob_Parser(TokenBuffer tokenBuf)
  {
    this(tokenBuf, 1);
  }

  protected Bob_Parser(TokenStream lexer, int k)
  {
    super(lexer, k);
    tokenNames = _tokenNames;
    buildTokenTypeASTClassMap();
    astFactory = new ASTFactory(getTokenTypeToASTClassMap());
  }

  public Bob_Parser(TokenStream lexer)
  {
    this(lexer, 1);
  }

  public Bob_Parser(ParserSharedInputState state)
  {
    super(state, 1);
    tokenNames = _tokenNames;
    buildTokenTypeASTClassMap();
    astFactory = new ASTFactory(getTokenTypeToASTClassMap());
  }

  public final void bExpression() throws RecognitionException, TokenStreamException
  {

    returnAST = null;
    ASTPair currentAST = new ASTPair();
    it.unibz.lib.bob.check.MyAST bExpression_AST = null;

    bExpression2();
    astFactory.addASTChild(currentAST, returnAST);
    match(Token.EOF_TYPE);
    bExpression_AST = (it.unibz.lib.bob.check.MyAST) currentAST.root;
    returnAST = bExpression_AST;
  }

  public final void bExpression2() throws RecognitionException, TokenStreamException
  {

    returnAST = null;
    ASTPair currentAST = new ASTPair();
    it.unibz.lib.bob.check.MyAST bExpression2_AST = null;

    bTerm();
    astFactory.addASTChild(currentAST, returnAST);
    {
      _loop18:
      do
      {
        if ((LA(1) == OR))
        {
          it.unibz.lib.bob.check.MyAST tmp5_AST = null;
          tmp5_AST = (it.unibz.lib.bob.check.MyAST) astFactory.create(LT(1));
          astFactory.makeASTRoot(currentAST, tmp5_AST);
          match(OR);
          bTerm();
          astFactory.addASTChild(currentAST, returnAST);
        }
        else
        {
          break _loop18;
        }

      }
      while (true);
    }
    bExpression2_AST = (it.unibz.lib.bob.check.MyAST) currentAST.root;
    returnAST = bExpression2_AST;
  }

  public final void bTerm() throws RecognitionException, TokenStreamException
  {

    returnAST = null;
    ASTPair currentAST = new ASTPair();
    it.unibz.lib.bob.check.MyAST bTerm_AST = null;

    notFactor();
    astFactory.addASTChild(currentAST, returnAST);
    {
      _loop21:
      do
      {
        if ((LA(1) == AND))
        {
          it.unibz.lib.bob.check.MyAST tmp6_AST = null;
          tmp6_AST = (it.unibz.lib.bob.check.MyAST) astFactory.create(LT(1));
          astFactory.makeASTRoot(currentAST, tmp6_AST);
          match(AND);
          notFactor();
          astFactory.addASTChild(currentAST, returnAST);
        }
        else
        {
          break _loop21;
        }

      }
      while (true);
    }
    bTerm_AST = (it.unibz.lib.bob.check.MyAST) currentAST.root;
    returnAST = bTerm_AST;
  }

  public final void notFactor() throws RecognitionException, TokenStreamException
  {

    returnAST = null;
    ASTPair currentAST = new ASTPair();
    it.unibz.lib.bob.check.MyAST notFactor_AST = null;
    {
      _loop24:
      do
      {
        if ((LA(1) == BLANK))
        {
          match(BLANK);
        }
        else
        {
          break _loop24;
        }

      }
      while (true);
    }
    {
      switch (LA(1))
      {
        case NOT:
        {
          it.unibz.lib.bob.check.MyAST tmp8_AST = null;
          tmp8_AST = (it.unibz.lib.bob.check.MyAST) astFactory.create(LT(1));
          astFactory.makeASTRoot(currentAST, tmp8_AST);
          match(NOT);
          {
            _loop27:
            do
            {
              if ((LA(1) == BLANK))
              {
                match(BLANK);
              }
              else
              {
                break _loop27;
              }

            }
            while (true);
          }
          break;
        }
        case LEFT_PAREN:
        case STRING_LITERAL:
        {
          break;
        }
        default:
        {
          throw new NoViableAltException(LT(1), getFilename());
        }
      }
    }
    bFactor();
    astFactory.addASTChild(currentAST, returnAST);
    {
      _loop29:
      do
      {
        if ((LA(1) == BLANK))
        {
          match(BLANK);
        }
        else
        {
          break _loop29;
        }

      }
      while (true);
    }
    notFactor_AST = (it.unibz.lib.bob.check.MyAST) currentAST.root;
    returnAST = notFactor_AST;
  }

  public final void bFactor() throws RecognitionException, TokenStreamException
  {

    returnAST = null;
    ASTPair currentAST = new ASTPair();
    it.unibz.lib.bob.check.MyAST bFactor_AST = null;

    switch (LA(1))
    {
      case LEFT_PAREN:
      {
        parExpression();
        astFactory.addASTChild(currentAST, returnAST);
        bFactor_AST = (it.unibz.lib.bob.check.MyAST) currentAST.root;
        break;
      }
      case STRING_LITERAL:
      {
        quotedRe();
        astFactory.addASTChild(currentAST, returnAST);
        bFactor_AST = (it.unibz.lib.bob.check.MyAST) currentAST.root;
        break;
      }
      default:
      {
        throw new NoViableAltException(LT(1), getFilename());
      }
    }
    returnAST = bFactor_AST;
  }

  public final void parExpression() throws RecognitionException, TokenStreamException
  {

    returnAST = null;
    ASTPair currentAST = new ASTPair();
    it.unibz.lib.bob.check.MyAST parExpression_AST = null;

    match(LEFT_PAREN);
    bExpression2();
    astFactory.addASTChild(currentAST, returnAST);
    match(RIGHT_PAREN);
    parExpression_AST = (it.unibz.lib.bob.check.MyAST) currentAST.root;
    returnAST = parExpression_AST;
  }

  public final void quotedRe() throws RecognitionException, TokenStreamException
  {

    returnAST = null;
    ASTPair currentAST = new ASTPair();
    it.unibz.lib.bob.check.MyAST quotedRe_AST = null;
    Token x = null;
    it.unibz.lib.bob.check.MyAST x_AST = null;

    x = LT(1);
    x_AST = (it.unibz.lib.bob.check.MyAST) astFactory.create(x);
    astFactory.addASTChild(currentAST, x_AST);
    match(STRING_LITERAL);
    quotedRe_AST = (it.unibz.lib.bob.check.MyAST) currentAST.root;
    returnAST = quotedRe_AST;
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

  protected void buildTokenTypeASTClassMap()
  {
    tokenTypeToASTClassMap = null;
  }

  ;
}
