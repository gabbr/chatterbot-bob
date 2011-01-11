package de.unikn.bib.chatterbotadmin;

import it.unibz.lib.bob.bcheck.BCheck;
import it.unibz.lib.bob.bcheck.BCheckImpl;
import it.unibz.lib.bob.chatterbot.Chatterbot;
import it.unibz.lib.bob.chatterbot.ChatterbotImpl;
import it.unibz.lib.bob.macroparser.Macroparser;
import it.unibz.lib.bob.macroparser.MacroparserImpl;
import it.unibz.lib.bob.qcheck.QCheck;
import it.unibz.lib.bob.qcheck.QCheckImpl;
import it.unibz.lib.bob.ttcheck.TTCheck;
import it.unibz.lib.bob.ttcheck.TTCheckImpl;

// logging context
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

// myfaces context
import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 *
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public class ChatterbotAdminImpl implements ChatterbotAdmin
{
  private BCheck bCheck;

  private QCheck qCheck;

  private TTCheck ttCheck;

  private Macroparser macroparser;

  private Chatterbot chatterbot;

  private String topicTreeFilename;

  private String macrosFilename;

  private String macrosENFilename;

  private String macrosDEFilename;

  private String macrosITFilename;

  private String textCorpusFilename;

  private String textCorpusENFilename;

  private String textCorpusDEFilename;

  private String textCorpusITFilename;

  private String rngFilename;

  private String testQuestionsFilename;

  private final String FILE_PATH = "/home/markus/test/";

  /**
   * <p>
   * Logging of this class uses four different log levels:
   * </p>
   * <ul>
   * <li><b>DEBUG</b> to reproduce complete program flow</li>
   * <li><b>INFO</b> to reproduce system activities</li>
   * <li><b>WARN</b> to reproduce system warnings</li>
   * <li><b>ERROR</b> to reproduce system failures</li>
   * <li><b>FATAL</b> to reproduce fatal system failures</li>
   * </ul>
   * <p>
   * The corresponding <tt>log4j.properties</tt> file is located in the
   * <tt>WEB-INF/classes</tt> directory of this web application.
   * </p>
   */
  private Logger log = Logger.getLogger(ChatterbotAdmin.class);

  public ChatterbotAdminImpl()
  {
    bCheck = new BCheckImpl();
    qCheck = new QCheckImpl();
    ttCheck = new TTCheckImpl();
    macroparser = new MacroparserImpl();
    chatterbot = new ChatterbotImpl();
  }

  private void uploadFile(UploadedFile uploadedfile, String filename)
  {

    try
    {
      InputStream streamIn = uploadedfile.getInputStream();
      long size = uploadedfile.getSize();
      byte[] buffer = new byte[(int) size];
      streamIn.read(buffer, 0, (int) size);

      log.debug("Filename: " + filename);

      File fileOut = new File(filename);
      FileOutputStream streamOut = new FileOutputStream(fileOut);

      streamOut.write(buffer);

      streamIn.close();
      streamOut.close();
    }
    catch (FileNotFoundException e)
    {
      log.error("Error: " + e.getMessage());
    }
    catch (IOException e)
    {
      log.error("Error: " + e.getMessage());
    }
  }

  @Override
  public void uploadTopicTreeFile(UploadedFile topicTreeFile)
  {
    topicTreeFilename = FILE_PATH + topicTreeFile.getName();
    uploadFile(topicTreeFile, topicTreeFilename);
  }

  @Override
  public void uploadMacrosFile(UploadedFile macrosFile)
  {
    macrosFilename = FILE_PATH + macrosFile.getName();
    uploadFile(macrosFile, macrosFilename);
  }

  @Override
  public void uploadMacrosENFile(UploadedFile macrosENFile)
  {
    macrosENFilename = FILE_PATH + macrosENFile.getName();
    uploadFile(macrosENFile, macrosENFilename);
  }

  @Override
  public void uploadMacrosDEFile(UploadedFile macrosDEFile)
  {
    macrosDEFilename = FILE_PATH + macrosDEFile.getName();
    uploadFile(macrosDEFile, macrosDEFilename);
  }

  @Override
  public void uploadMacrosITFile(UploadedFile macrosITFile)
  {
    macrosITFilename = FILE_PATH + macrosITFile.getName();
    uploadFile(macrosITFile, macrosITFilename);
  }

  @Override
  public void uploadTextCorpusFile(UploadedFile textCorpusFile)
  {
    textCorpusFilename = FILE_PATH + textCorpusFile.getName();
    uploadFile(textCorpusFile, textCorpusFilename);
  }

  @Override
  public void uploadTextCorpusENFile(UploadedFile textCorpusENFile)
  {
    textCorpusENFilename = FILE_PATH + textCorpusENFile.getName();
    uploadFile(textCorpusENFile, macrosFilename);
  }

  @Override
  public void uploadTextCorpusDEFile(UploadedFile textCorpusDEFile)
  {
    textCorpusDEFilename = FILE_PATH + textCorpusDEFile.getName();
    uploadFile(textCorpusDEFile, textCorpusDEFilename);
  }

  @Override
  public void uploadTextCorpusITFile(UploadedFile textCorpusITFile)
  {
    textCorpusITFilename = FILE_PATH + textCorpusITFile.getName();
    uploadFile(textCorpusITFile, textCorpusITFilename);
  }

  @Override
  public void uploadRngFile(UploadedFile rngFile)
  {
    rngFilename = FILE_PATH + rngFile.getName();
    uploadFile(rngFile, rngFilename);
  }

  @Override
  public void uploadTestQuestionsFile(UploadedFile testQuestionsFile)
  {
    testQuestionsFilename = FILE_PATH + testQuestionsFile.getName();
    uploadFile(testQuestionsFile, testQuestionsFilename);
  }

  @Override
  public String performTTCheck()
  {
    return ttCheck.performTTCheck(topicTreeFilename, rngFilename, 
            macrosDEFilename, macrosENFilename, macrosITFilename);
  }
}
