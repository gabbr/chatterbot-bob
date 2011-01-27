package test;

import it.unibz.lib.bob.bbcheck.BBCheck;
import it.unibz.lib.bob.bbcheck.BBCheckImpl;
import java.io.File;
import java.net.URL;

public class BBCheckTest
{
  public static void main(String[] args)
  {
    BBCheck bbCheck = new BBCheckImpl();

    String path = "/home/markus/KonRat/chatterbot-bob-read-only/"
            + "bob_knowledge_repository";
    
    String language = "DE";

    File topicTreeFile = new File(path + "/topictree.xml");
    File macrosENFile = new File(path + "/bob_macros_EN.txt");
    File macrosDEFile = new File(path + "/bob_macros_DE.txt");
    File macrosITFile = new File(path + "/bob_macros_IT.txt");
    File textCorpusENFile = new File(path + "/UKWAC-1.txt_sm");
    File textCorpusDEFile = new File(path + "/DEWAC-1.txt_sm");
    File textCorpusITFile = new File(path + "/ITWAC-1.txt_sm");
    File testQuestionsFile = new File(path + "/TestQuestions_DE.xls");

    URL topicTreeFileURL = null;
    URL macrosENFileURL = null;
    URL macrosDEFileURL = null;
    URL macrosITFileURL = null;
    URL textCorpusENFileURL = null;
    URL textCorpusDEFileURL = null;
    URL textCorpusITFileURL = null;
    URL testQuestionsFileURL = null;

    try
    {
      topicTreeFileURL = topicTreeFile.toURI().toURL();
      macrosENFileURL = macrosENFile.toURI().toURL();
      macrosDEFileURL = macrosDEFile.toURI().toURL();
      macrosITFileURL = macrosITFile.toURI().toURL();
      //textCorpusENFileURL = textCorpusENFile.toURI().toURL();
      //textCorpusDEFileURL = textCorpusDEFile.toURI().toURL();
      //textCorpusITFileURL = textCorpusITFile.toURI().toURL();
      testQuestionsFileURL = testQuestionsFile.toURI().toURL();
    }
    catch (Exception e)
    {
      System.err.println("Error: " + e.getMessage());
    }

    Boolean trainingMode = Boolean.TRUE;
    String outfilePath = "/home/markus";

    String result = bbCheck.performBBCheck(language, topicTreeFileURL,
            macrosENFileURL, macrosDEFileURL, macrosITFileURL,
            textCorpusENFileURL, textCorpusDEFileURL,
            textCorpusITFileURL, testQuestionsFileURL,
            trainingMode, outfilePath);
    
    System.out.println(result);

  }
}
