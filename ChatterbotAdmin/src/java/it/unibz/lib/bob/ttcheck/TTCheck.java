package it.unibz.lib.bob.ttcheck;

/**
 *
 * @version $Id: TTCheck.java 313 2010-12-22 10:41:47Z markus.grandpre $
 */
public interface TTCheck
{
  // test
  public String performTTCheck(String ttFile, String rngFile, String macrosDEFile,
          String macrosENFile, String macrosITFile);
}
