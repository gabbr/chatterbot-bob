// $ANTLR : "macroParser.g" -> "macroParser_Parser.java"$
package it.unibz.lib.bob.macroparser;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

import org.apache.log4j.Logger;

/**
 *
 * @version $Id$
 */
public class macroParser_Parser extends antlr.LLkParser implements macroParser_LexerTokenTypes
{
  protected macroParser_Parser(TokenBuffer tokenBuf, int k)
  {
    super(tokenBuf, k);
    tokenNames = _tokenNames;
    buildTokenTypeASTClassMap();
    astFactory = new ASTFactory(getTokenTypeToASTClassMap());
  }

  public macroParser_Parser(TokenBuffer tokenBuf)
  {
    this(tokenBuf, 1);
  }

  protected macroParser_Parser(TokenStream lexer, int k)
  {
    super(lexer, k);
    tokenNames = _tokenNames;
    buildTokenTypeASTClassMap();
    astFactory = new ASTFactory(getTokenTypeToASTClassMap());
  }

  public macroParser_Parser(TokenStream lexer)
  {
    this(lexer, 1);
  }

  public macroParser_Parser(ParserSharedInputState state)
  {
    super(state, 1);
    tokenNames = _tokenNames;
    buildTokenTypeASTClassMap();
    astFactory = new ASTFactory(getTokenTypeToASTClassMap());
  }

  public final void string() throws RecognitionException, TokenStreamException
  {

    returnAST = null;
    ASTPair currentAST = new ASTPair();
    AST string_AST = null;
    AST c_AST = null;
    AST b_AST = null;

    try
    {      // for error handling
      token();
      c_AST = (AST) returnAST;
      astFactory.addASTChild(currentAST, returnAST);
      {
        _loop22:
        do
        {
          if ((LA(1) == NON_MACRO || LA(1) == MACRO))
          {
            token();
            b_AST = (AST) returnAST;
            string_AST = (AST) currentAST.root;
            string_AST = (AST) astFactory.make((new ASTArray(3)).add(astFactory.create(STRING_, "string")).add(c_AST).add(b_AST));
            currentAST.root = string_AST;
            currentAST.child = string_AST != null && string_AST.getFirstChild() != null
                    ? string_AST.getFirstChild() : string_AST;
            currentAST.advanceChildToEnd();
          }
          else
          {
            break _loop22;
          }

        }
        while (true);
      }
      string_AST = (AST) currentAST.root;
    }
    catch (RecognitionException ex)
    {
      reportError(ex);
      recover(ex, _tokenSet_0);
    }
    returnAST = string_AST;
  }

  public final void token() throws RecognitionException, TokenStreamException
  {

    returnAST = null;
    ASTPair currentAST = new ASTPair();
    AST token_AST = null;
    Token a = null;
    AST a_AST = null;
    Token b = null;
    AST b_AST = null;

    try
    {      // for error handling
      {
        switch (LA(1))
        {
          case MACRO:
          {
            a = LT(1);
            a_AST = astFactory.create(a);
            astFactory.addASTChild(currentAST, a_AST);
            match(MACRO);
            token_AST = (AST) currentAST.root;
            token_AST = (AST) astFactory.make((new ASTArray(2)).add(astFactory.create(MACRO, "macro")).add(a_AST));
            currentAST.root = token_AST;
            currentAST.child = token_AST != null && token_AST.getFirstChild() != null
                    ? token_AST.getFirstChild() : token_AST;
            currentAST.advanceChildToEnd();
            break;
          }
          case NON_MACRO:
          {
            b = LT(1);
            b_AST = astFactory.create(b);
            astFactory.addASTChild(currentAST, b_AST);
            match(NON_MACRO);
            token_AST = (AST) currentAST.root;
            token_AST = (AST) astFactory.make((new ASTArray(2)).add(astFactory.create(NON_MACRO, "non_macro")).add(b_AST));
            currentAST.root = token_AST;
            currentAST.child = token_AST != null && token_AST.getFirstChild() != null
                    ? token_AST.getFirstChild() : token_AST;
            currentAST.advanceChildToEnd();
            break;
          }
          default:
          {
            throw new NoViableAltException(LT(1), getFilename());
          }
        }
      }
      token_AST = (AST) currentAST.root;
    }
    catch (RecognitionException ex)
    {
      reportError(ex);
      recover(ex, _tokenSet_1);
    }
    returnAST = token_AST;
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

  protected void buildTokenTypeASTClassMap()
  {
    tokenTypeToASTClassMap = null;
  }

  ;

  private static final long[] mk_tokenSet_0()
  {
    long[] data =
    {
      2L, 0L
    };
    return data;
  }
  public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

  private static final long[] mk_tokenSet_1()
  {
    long[] data =
    {
      50L, 0L
    };
    return data;
  }
  public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

}
