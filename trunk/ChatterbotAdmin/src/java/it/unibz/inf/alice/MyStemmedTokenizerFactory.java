package it.unibz.inf.alice;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import com.aliasi.tokenizer.EnglishStopListFilterTokenizer;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseFilterTokenizer;
import com.aliasi.tokenizer.PorterStemmerFilterTokenizer;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Compilable;

/**
 * A <code>MyStemmedTokenizerFactory</code> creates tokenizers for
 * subsequences of character arrays.
 * 
 * The tokenization is based on the following pipe, using existing LingPipe
 * code:
 * <ul>
 * <li> IndoEuropeanTokenizerFactory
 * <li> LowerCaseFilterTokenizer
 * <li> EnglishStopListFilterTokenizer
 * <li> PorterStemmerFilterTokenizer
 * </ul>
 * 
 * @author Manuel Kirschner
 * @version $Id$
 */
public class MyStemmedTokenizerFactory implements Compilable, TokenizerFactory,
        Serializable
{

  private static final long serialVersionUID = -6920267163317533680L;

  /**
   * An instance of a tokenizer factory.
   */
  public static final TokenizerFactory FACTORY = new MyStemmedTokenizerFactory();

  /**
   * Returns a tokenizer for the specified subsequence of characters.
   *
   * @param ch
   *            Characters to tokenize.
   * @param start
   *            Index of first character to tokenize.
   * @param length
   *            Number of characters to tokenize.
   */
  public Tokenizer tokenizer(char[] ch, int start, int length)
  {

    Tokenizer tokenizer = IndoEuropeanTokenizerFactory.FACTORY.tokenizer(
            ch, start, length);
    tokenizer = new LowerCaseFilterTokenizer(tokenizer);
    tokenizer = new EnglishStopListFilterTokenizer(tokenizer);
    tokenizer = new PorterStemmerFilterTokenizer(tokenizer);
    return tokenizer;
  }

  /**
   * Compiles this tokenizer factory to the specified object output. The
   * tokenizer factory read back in is reference identical to the static
   * constant {@link #FACTORY}.
   *
   * @param objOut
   *            Object output to which this tokenizer factory is compiled.
   * @throws IOException
   *             If there is an I/O error during the write.
   */
  public void compileTo(ObjectOutput objOut) throws IOException
  {
    objOut.writeObject(new Externalizer());
  }

  private static class Externalizer extends AbstractExternalizable
  {
    static final long serialVersionUID = 3826670589236636230L;

    public Externalizer()
    {
      /* do nothing */
    }

    @Override
    public void writeExternal(ObjectOutput objOut)
    {
      /* do nothing */
    }

    @Override
    public Object read(ObjectInput objIn)
    {
      return FACTORY;
    }
  }
}
