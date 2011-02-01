package test;

import it.unibz.lib.bob.ttcheck.TTCheck;
import it.unibz.lib.bob.ttcheck.TTCheckImpl;

/**
 *
 * @author markus
 */
public class TTCheckTest
{

  public static void main(String[] args)
  {
    TTCheck tt = new TTCheckImpl();
    System.out.println(
      tt.performTTCheck(
      "/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/Application_Data/topictree.xml",
      "/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/Application_Data/library_domain.rng",
      "/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/Application_Data/bob_macros_DE.txt",
      "/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/Application_Data/bob_macros_EN.txt",
      "/Users/manuelkirschner/svn_base/libexperts/BoB_FUB/Application_Data/bob_macros_IT.txt"));
  }

}
