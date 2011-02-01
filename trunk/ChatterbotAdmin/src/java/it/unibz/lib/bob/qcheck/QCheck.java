package it.unibz.lib.bob.qcheck;

/**
 * <p>
 * This interface is used to to perform test of qcheck application. When test 
 * has finished, test results are provided as a String object.
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public interface QCheck
{
  /**
   * <p>
   * This methods starts qcheck test. When test has finished collected test 
   * results are returned as String object.
   * </p>
   * 
   * @param macroFile file location of a macro file
   * @param regularExpression regular expression to test 
   * @param userQuestion user question to test
   * @param format format type 
   * @return test results of qcheck test
   */
  public String performQCheck(String macroFile, String regularExpression,
          String userQuestion, String format);
}