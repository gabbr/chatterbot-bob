/**
 * Extension of basic AST type to include line number info 
 */
package it.unibz.lib.bob.check;

import antlr.CommonAST;
import antlr.Token;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @version $Id$
 */
public class MyAST extends CommonAST implements Serializable
{

  /**
   * <p>
   * 
   * </p>
   */
  private Token token;

  @Override
  public void initialize(Token tok)
  {
    token = tok;
    super.initialize(tok);
  }

  @Override
  public int getLine()
  {
    if (token != null)
    {
      return token.getLine();
    }
    else
    {
      return -1;
    }
  }

  @Override
  public String getText()
  {
    if (token != null)
    {
      return token.getText();
    }
    else
    {
      return null;
    }
  }

  @Override
  public int getColumn()
  {
    if (token != null)
    {
      return token.getColumn();
    }
    else
    {
      return -1;
    }
  }
}
