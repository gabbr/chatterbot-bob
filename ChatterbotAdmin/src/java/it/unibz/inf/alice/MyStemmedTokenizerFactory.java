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
 * <p>
 * A <code>MyStemmedTokenizerFactory</code> creates tokenizers for
 * subsequences of character arrays.
 * </p>
 * <p> 
 * The tokenization is based on the following pipe, using existing LingPipe
 * code:
 * </p>
 * <ul>
 *  <li> IndoEuropeanTokenizerFactory</li>
 *  <li> LowerCaseFilterTokenizer</li>
 *  <li> EnglishStopListFilterTokenizer</li>
 *  <li> PorterStemmerFilterTokenizer</li>
 * </ul>
 * 
 * @author manuel.kirschner@gmail.com
 * @version $Id$
 */
public class MyStemmedTokenizerFactory implements Compilable, TokenizerFactory,
        Serializable
{
  /**
   * <p>
   * An instance of a tokenizer factory.
   * </p>
   */
  public static final TokenizerFactory FACTORY = new MyStemmedTokenizerFactory();

  @Override
  public Tokenizer tokenizer(char[] ch, int start, int length)
  {
    Tokenizer tokenizer = IndoEuropeanTokenizerFactory.FACTORY.tokenizer(
            ch, start, length);
    tokenizer = new LowerCaseFilterTokenizer(tokenizer);
    tokenizer = new EnglishStopListFilterTokenizer(tokenizer);
    tokenizer = new PorterStemmerFilterTokenizer(tokenizer);
    return tokenizer;
  }

  @Override
  public void compileTo(ObjectOutput objOut) throws IOException
  {
    objOut.writeObject(new Externalizer());
  }

  /**
   * <p>
   * 
   * </p>
   */
  private static class Externalizer extends AbstractExternalizable
  {
    /**
     * <p>
     * 
     * </p>
     */
    static final long serialVersionUID = 3826670589236636230L;

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
