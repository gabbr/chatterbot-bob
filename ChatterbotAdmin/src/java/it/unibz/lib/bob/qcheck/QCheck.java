package it.unibz.lib.bob.qcheck;

/**
 *
 * @version $Id$
 */
public interface QCheck
{
  public String performQCheck(String macroFile, String regularExpression,
          String userQuestion, String format);
}
