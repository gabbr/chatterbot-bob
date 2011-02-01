package it.unibz.lib.bob.ttcheck;

/**
 * <p>
 * This interface is used to to perform test of ttcheck application. When test 
 * has finished, test results are provided as a String object.
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public interface TTCheck
{
  /**
   * <p>
   * This methods starts ttcheck test. When test has finished collected test 
   * results are returned as String object.
   * </p>
   * 
   * @param ttFile file location of topic tree file
   * @param rngFile file location of rng file
   * @param macrosDEFile file location of German macros file
   * @param macrosENFile file location of English macros file
   * @param macrosITFile file location of Italian macros file
   */
  public String performTTCheck(String topicTreeFile, String rngFile, 
    String macrosENFile, String macrosDEFile, String macrosITFile);
}
