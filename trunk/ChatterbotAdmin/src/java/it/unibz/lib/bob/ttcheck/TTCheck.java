package it.unibz.lib.bob.ttcheck;

/**
 *
 * @version $Id$
 */
public interface TTCheck
{
  // test
  public String performTTCheck(String ttFile, String rngFile, String macrosDEFile,
          String macrosENFile, String macrosITFile);
}
