package test;

import it.unibz.lib.bob.chatterbot.Chatterbot;
import it.unibz.lib.bob.chatterbot.ChatterbotImpl;
import java.io.File;
import java.net.URL;

public class ChatterbotTest
{
  public static void main(String[] args)
  {
    Chatterbot chatterbot = new ChatterbotImpl();

    String question = null;

    String answer = null;

    String path = "/home/markus/KonRat/chatterbot-bob-read-only/"
            + "bob_knowledge_repository";

    File topicTreeFile = new File(path + "/topictree.xml");
    File macrosENFile = new File(path + "/bob_macros_EN.txt");
    File macrosDEFile = new File(path + "/bob_macros_DE.txt");
    File macrosITFile = new File(path + "/bob_macros_IT.txt");
    File textCorpusENFile = new File(path + "/UKWAC-1.txt_sm");
    File textCorpusDEFile = new File(path + "/DEWAC-1.txt_sm");
    File textCorpusITFile = new File(path + "/ITWAC-1.txt_sm");



    String[] languages =
    {
      "EN", "DE", "IT", "DE", "DE", "IT", "EN"
    };

    for (String language : languages)
    {

      URL topicTreeFileURL = null;
      URL macrosENFileURL = null;
      URL macrosDEFileURL = null;
      URL macrosITFileURL = null;
      URL textCorpusENFileURL = null;
      URL textCorpusDEFileURL = null;
      URL textCorpusITFileURL = null;

      try
      {
        topicTreeFileURL = topicTreeFile.toURI().toURL();
        
        if (language.equals("DE"))
        {
          macrosDEFileURL = macrosDEFile.toURI().toURL();
          textCorpusDEFileURL = textCorpusDEFile.toURI().toURL();

          question = "Wie geht es Dir?";
        }
        
        if (language.equals("EN"))
        {
          macrosENFileURL = macrosENFile.toURI().toURL(); 
          textCorpusENFileURL = textCorpusENFile.toURI().toURL();

          question = "How are you doing?";
        }
        
        if (language.equals("IT"))
        {
          macrosITFileURL = macrosITFile.toURI().toURL();
          textCorpusITFileURL = textCorpusITFile.toURI().toURL();

          question = "Come stai?";
        }
      }
      catch (Exception e)
      {
        System.err.println("Error: " + e.getMessage());
      }
      
      System.out.println("topicTreeFileURL: " + topicTreeFileURL);
      System.out.println("macrosENFileURL: " + macrosENFileURL);
      System.out.println("macrosDEFileURL: " + macrosDEFileURL);
      System.out.println("macrosITFileURL: " + macrosITFileURL);
      System.out.println("textCorpusENFileURL: " + textCorpusENFileURL);
      System.out.println("textCorpusDEFileURL: " + textCorpusDEFileURL);
      System.out.println("textCorpusITFileURL: " + textCorpusITFileURL);

      chatterbot.updateChatterbotSettings(topicTreeFileURL, macrosENFileURL,
              textCorpusENFileURL, macrosDEFileURL, textCorpusDEFileURL,
              macrosITFileURL, textCorpusITFileURL);

      answer = chatterbot.getChatterbotAnswer(question, language);

      System.out.println(language + "\n\tQ: " + question + "\n\tA: " + answer);
    }
  }
}
