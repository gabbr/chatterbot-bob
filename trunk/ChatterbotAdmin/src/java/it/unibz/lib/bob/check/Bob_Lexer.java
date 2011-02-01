// $ANTLR : "Bob.g" -> "Bob_Lexer.java"$
package it.unibz.lib.bob.check;

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.TokenStream;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;

/**
 * <p>
 *
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @version $Id$
 */
public class Bob_Lexer extends antlr.CharScanner implements Bob_LexerTokenTypes, TokenStream
{
  /**
   * <p>
   *
   * </p>
   *
   * @param in
   */
  public Bob_Lexer(InputStream in)
  {
    this(new ByteBuffer(in));
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param in
   */
  public Bob_Lexer(Reader in)
  {
    this(new CharBuffer(in));
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param ib
   */
  public Bob_Lexer(InputBuffer ib)
  {
    this(new LexerSharedInputState(ib));
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param state
   */
  public Bob_Lexer(LexerSharedInputState state)
  {
    super(state);
    caseSensitiveLiterals = true;
    setCaseSensitive(true);
    literals = new Hashtable();
  }

  @Override
  public Token nextToken() throws TokenStreamException
  {
    Token theRetToken = null;
    tryAgain:
    for (;;)
    {
      Token _token = null;
      int _ttype = Token.INVALID_TYPE;
      resetText();
      try
      {   // for char stream error handling
        try
        {   // for lexical error handling
          switch (LA(1))
          {
            case '|':
            {
              mOR(true);
              theRetToken = _returnToken;
              break;
            }
            case '&':
            {
              mAND(true);
              theRetToken = _returnToken;
              break;
            }
            case '!':
            {
              mNOT(true);
              theRetToken = _returnToken;
              break;
            }
            case '(':
            {
              mLEFT_PAREN(true);
              theRetToken = _returnToken;
              break;
            }
            case ')':
            {
              mRIGHT_PAREN(true);
              theRetToken = _returnToken;
              break;
            }
            case '\t':
            case '\n':
            case '\r':
            case ' ':
            {
              mBLANK(true);
              theRetToken = _returnToken;
              break;
            }
            case '"':
            {
              mSTRING_LITERAL(true);
              theRetToken = _returnToken;
              break;
            }
            default:
            {
              if (LA(1) == EOF_CHAR)
              {
                uponEOF();
                _returnToken = makeToken(Token.EOF_TYPE);
              }
              else
              {
                throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
              }
            }
          }
          if (_returnToken == null)
          {
            continue tryAgain; // found SKIP token
          }
          _ttype = _returnToken.getType();
          _ttype = testLiteralsTable(_ttype);
          _returnToken.setType(_ttype);
          return _returnToken;
        }
        catch (RecognitionException e)
        {
          throw new TokenStreamRecognitionException(e);
        }
      }
      catch (CharStreamException cse)
      {
        if (cse instanceof CharStreamIOException)
        {
          throw new TokenStreamIOException(((CharStreamIOException) cse).io);
        }
        else
        {
          throw new TokenStreamException(cse.getMessage());
        }
      }
    }
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param _createToken
   * @throws RecognitionException
   * @throws CharStreamException
   * @throws TokenStreamException
   */
  public final void mOR(boolean _createToken)
          throws RecognitionException, CharStreamException, TokenStreamException
  {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = OR;
    int _saveIndex;

    match("||");
    if (_createToken && _token == null && _ttype != Token.SKIP)
    {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param _createToken
   * @throws RecognitionException
   * @throws CharStreamException
   * @throws TokenStreamException
   */
  public final void mAND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException
  {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = AND;
    int _saveIndex;

    match("&&");
    if (_createToken && _token == null && _ttype != Token.SKIP)
    {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param _createToken
   * @throws RecognitionException
   * @throws CharStreamException
   * @throws TokenStreamException
   */
  public final void mNOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException
  {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = NOT;
    int _saveIndex;

    match("!");
    if (_createToken && _token == null && _ttype != Token.SKIP)
    {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param _createToken
   * @throws RecognitionException
   * @throws CharStreamException
   * @throws TokenStreamException
   */
  public final void mLEFT_PAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException
  {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = LEFT_PAREN;
    int _saveIndex;

    match('(');
    if (_createToken && _token == null && _ttype != Token.SKIP)
    {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param _createToken
   * @throws RecognitionException
   * @throws CharStreamException
   * @throws TokenStreamException
   */
  public final void mRIGHT_PAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException
  {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = RIGHT_PAREN;
    int _saveIndex;

    match(')');
    if (_createToken && _token == null && _ttype != Token.SKIP)
    {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param _createToken
   * @throws RecognitionException
   * @throws CharStreamException
   * @throws TokenStreamException
   */
  public final void mBLANK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException
  {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = BLANK;
    int _saveIndex;
    {
      switch (LA(1))
      {
        case '\r':
        {
          match('\r');
          match('\n');
          newline();
          break;
        }
        case '\n':
        {
          match('\n');
          newline();
          break;
        }
        case ' ':
        {
          match(' ');
          break;
        }
        case '\t':
        {
          match('\t');
          break;
        }
        default:
        {
          throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
        }
      }
    }
    if (_createToken && _token == null && _ttype != Token.SKIP)
    {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param _createToken
   * @throws RecognitionException
   * @throws CharStreamException
   * @throws TokenStreamException
   */
  public final void mSTRING_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException
  {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = STRING_LITERAL;
    int _saveIndex;
    {
      match('"');
      {
        _loop12:
        do
        {
          if ((LA(1) == '\\'))
          {
            mESCAPE(false);
          }
          else if ((_tokenSet_0.member(LA(1))))
          {
            {
              match(_tokenSet_0);
            }
          }
          else
          {
            break _loop12;
          }

        }
        while (true);
      }
      match('"');
    }
    if (_createToken && _token == null && _ttype != Token.SKIP)
    {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  /**
   * <p>
   *
   * </p>
   *
   * @param _createToken
   * @throws RecognitionException
   * @throws CharStreamException
   * @throws TokenStreamException
   */
  protected final void mESCAPE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException
  {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = ESCAPE;
    int _saveIndex;

    match('\\');
    {
      switch (LA(1))
      {
        case 'n':
        {
          match('n');
          text.setLength(_begin);
          text.append("\\n");
          break;
        }
        case 'r':
        {
          match('r');
          text.setLength(_begin);
          text.append("\\r");
          break;
        }
        case 't':
        {
          match('t');
          text.setLength(_begin);
          text.append("\\t");
          break;
        }
        case 'b':
        {
          match('b');
          text.setLength(_begin);
          text.append("\\b");
          break;
        }
        case '"':
        {
          match('"');
          text.setLength(_begin);
          text.append("\"");
          break;
        }
        case '\\':
        {
          match('\\');
          break;
        }
        case '?':
        {
          match('?');
          break;
        }
        case '!':
        {
          match('!');
          break;
        }
        case '.':
        {
          match('.');
          break;
        }
        case '+':
        {
          match('+');
          break;
        }
        case '*':
        {
          match('*');
          break;
        }
        case '[':
        {
          match('[');
          break;
        }
        case ']':
        {
          match(']');
          break;
        }
        case '(':
        {
          match('(');
          break;
        }
        case ')':
        {
          match(')');
          break;
        }
        case '^':
        {
          match('^');
          break;
        }
        case '$':
        {
          match('$');
          break;
        }
        case '/':
        {
          match('/');
          break;
        }
        case '\'':
        {
          match("'");
          break;
        }
        default:
        {
          throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
        }
      }
    }
    if (_createToken && _token == null && _ttype != Token.SKIP)
    {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  /**
   * <p>
   *
   * </p>
   *
   * @return
   */
  private static long[] mk_tokenSet_0()
  {
    long[] data = new long[2048];
    data[0] = -17179869185L;
    data[1] = -268435457L;
    for (int i = 2; i <= 1022; i++)
    {
      data[i] = -1L;
    }
    data[1023] = 9223372036854775807L;
    return data;
  }
  /**
   *
   */
  public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
}
