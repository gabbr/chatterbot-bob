package de.unikn.bib.chatterbotadmin;

import it.unibz.lib.bob.bbcheck.BBCheck;
import it.unibz.lib.bob.bbcheck.BBCheckImpl;
import it.unibz.lib.bob.chatterbot.Chatterbot;
import it.unibz.lib.bob.chatterbot.ChatterbotImpl;
import it.unibz.lib.bob.qcheck.QCheck;
import it.unibz.lib.bob.qcheck.QCheckImpl;
import it.unibz.lib.bob.ttcheck.TTCheck;
import it.unibz.lib.bob.ttcheck.TTCheckImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 * <p>
 *  This class represents a Java Server Faces Bean class and contains the
 *  application logic of ChatterbotAdmin web application. It aggregates
 *  several tests applications (bbCheck, qCheck and ttCheck) and a chat
 *  application (chatterbot) for testing purpose.
 * </p>
 * <p>
 *  All attributes and their default values that are managed by the UI of
 *  this application are also listed in faces-config.xml file.
 * </p>
 *
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public class ChatterbotAdminBean implements Serializable
{
  /**
   * <p>
   * This reference provides access to bbCheck application.
   * </p>
   */
  private BBCheck bbCheck;

  /**
   * <p>
   * This reference provides access to qCheck application.
   * </p>
   */
  private QCheck qCheck;

  /**
   * <p>
   * This reference provides access to ttCheck application.
   * </p>
   */
  private TTCheck ttCheck;

  /**
   * <p>
   * This reference provides access to chatterbot application.
   * </p>
   */
  private Chatterbot chatterbot;

  /**
   * <p>
   * This String object represents the currently set language ("DE", "EN" and
   * "IT") to perform bbCheck.
   * </p>
   */
  private String bbCheckLanguage;

  /**
   * <p>
   * This Boolean object indicates if English language is currently set to
   * perform bbCheck.
   * </p>
   */
  private Boolean bbCheckLanguageENSelected;

  /**
   * <p>
   * This Boolean object indicates if German language is currently set to
   * perform bbCheck.
   * </p>
   */
  private Boolean bbCheckLanguageDESelected;

  /**
   * <p>
   * This Boolean object indicates if Italian language is currently set to
   * perform bbCheck.
   * </p>
   */
  private Boolean bbCheckLanguageITSelected;

  /**
   * <p>
   * </p>
   */
  private String bbCheckLearningInput;

  /**
   * <p>
   * </p>
   */
  private Boolean bbCheckLearningSelected;

  /**
   * <p>
   * This String object represents the test results after bbCheck test has
   * finished.
   * </p>
   */
  private String bbCheckResults;

  /**
   * <p>
   * This String object represents the URL ("file://") of the test report file
   * of bbCheck application. Path of this file is usually determined by value
   * of sharedFilePath attribute.
   * </p>
   */
  private String bbCheckReportFileURL;

  /**
   * <p>
   * This String object represents the filename of the test report file
   * of bbCheck application.
   * </p>
   */
  private String bbCheckReportFilename;

  /**
   * <p>
   * This String object represents the content type of the test report file
   * of bbCheck application. It is usually set
   * </p>
   */
  private String bbCheckReportFileContentType;

  /**
   * <p>
   * </p>
   */
  private String qCheckFormat;

  /**
   * <p>
   * </p>
   */
  private String qCheckRegularExpression;

  /**
   * <p>
   * </p>
   */
  private String qCheckUserQuestion;

  /**
   * <p>
   * </p>
   */
  private String qCheckResults;

  /**
   * <p>
   * </p>
   */
  private String ttCheckResults;

  /**
   * <p>
   * </p>
   */
  private String chatterbotLanguage;

  /**
   * <p>
   * </p>
   */
  private Boolean chatterbotLanguageENSelected;

  /**
   * <p>
   * </p>
   */
  private Boolean chatterbotLanguageDESelected;

  /**
   * <p>
   * </p>
   */
  private Boolean chatterbotLanguageITSelected;

  /**
   * <p>
   * </p>
   */
  private String chatterbotAnswer;

  /**
   * <p>
   * </p>
   */
  private String chatterbotQuestion;

  /**
   * <p>
   * </p>
   */
  private UploadedFile topicTreeFile;

  /**
   * <p>
   * </p>
   */
  private String topicTreeFilename;

  /**
   * <p>
   * </p>
   */
  private URL topicTreeFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean topicTreeFileIsUploaded;

  /**
   * <p>
   * </p>
   */
  private UploadedFile macrosFile;

  /**
   * <p>
   * </p>
   */
  private String macrosFilename;

  /**
   * <p>
   * </p>
   */
  private URL macrosFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean macrosFileIsUploaded;

  /**
   * <p>
   * </p>
   */
  private UploadedFile macrosENFile;

  /**
   * <p>
   * </p>
   */
  private String macrosENFilename;

  /**
   * <p>
   * </p>
   */
  private URL macrosENFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean macrosENFileIsUploaded;

  /**
   * <p>
   * </p>
   */
  private UploadedFile macrosDEFile;

  /**
   * <p>
   * </p>
   */
  private String macrosDEFilename;

  /**
   * <p>
   * </p>
   */
  private URL macrosDEFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean macrosDEFileIsUploaded;

  /**
   * <p>
   * </p>
   */
  private UploadedFile macrosITFile;

  /**
   * <p>
   * </p>
   */
  private String macrosITFilename;

  /**
   * <p>
   * </p>
   */
  private URL macrosITFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean macrosITFileIsUploaded;

  /**
   * <p>
   * </p>
   */
  private UploadedFile textCorpusFile;

  /**
   * <p>
   * </p>
   */
  private String textCorpusFilename;

  /**
   * <p>
   * </p>
   */
  private URL textCorpusFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean textCorpusFileIsUploaded;

  /**
   * <p>
   * </p>
   */
  private UploadedFile textCorpusENFile;

  /**
   * <p>
   * </p>
   */
  private String textCorpusENFilename;

  /**
   * <p>
   * </p>
   */
  private URL textCorpusENFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean textCorpusENFileIsUploaded;

  /**
   * <p>
   * </p>
   */
  private UploadedFile textCorpusDEFile;

  /**
   * <p>
   * </p>
   */
  private String textCorpusDEFilename;

  /**
   * <p>
   * </p>
   */
  private URL textCorpusDEFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean textCorpusDEFileIsUploaded;

  /**
   * <p>
   * </p>
   */
  private UploadedFile textCorpusITFile;

  /**
   * <p>
   * </p>
   */
  private String textCorpusITFilename;

  /**
   * <p>
   * </p>
   */
  private URL textCorpusITFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean textCorpusITFileIsUploaded;

  /**
   * <p>
   * </p>
   */
  private UploadedFile rngFile;

  /**
   * <p>
   * </p>
   */
  private String rngFilename;

  /**
   * <p>
   * </p>
   */
  private URL rngFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean rngFileIsUploaded;

  /**
   * <p>
   * </p>
   */
  private UploadedFile testQuestionsFile;

  /**
   * <p>
   * </p>
   */
  private String testQuestionsFilename;

  /**
   * <p>
   * </p>
   */
  private URL testQuestionsFileURL;

  /**
   * <p>
   * </p>
   */
  private Boolean testQuestionsFileIsUploaded;

  /**
   * <p>
   * This String object represents
   * </p>
   */
  private String sharedFilesPath;

  /**
   * <p>
   * This String value represents the currently chosen language to display
   * messages from message bundle file.
   * </p>
   */
  private String locale;

  /**
   * <p>
   * This Boolean object indicates if bbcheck application is selected.
   * </p>
   */
  private Boolean bbCheckSelected;

  /**
   * <p>
   * This Boolean object indicates if qcheck application is selected.
   * </p>
   */
  private Boolean qCheckSelected;

  /**
   * <p>
   * This Boolean object indicates if ttcheck application is selected.
   * </p>
   */
  private Boolean ttCheckSelected;

  /**
   * <p>
   * This Boolean object indicates if chatterbot application is selected.
   * </p>
   */
  private Boolean chatterbotSelected;

  /**
   * <p>
   * This String object represents a constant return value any use case
   * operation that indicates if corresponding operation has failed.
   * This constant return values is used for JSF navigation rules that are
   * defined in the web application's <tt>faces-config.xml</tt> configuration
   * file.
   * </p>
   */
  private String SHARED_FILES_DIR_NAME = "/shared-files";

  private String SUCCESS = "success";

  private String FAILED = "failed";

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
  private Logger log = Logger.getLogger(ChatterbotAdminBean.class);

  /**
   * <p>
   *
   * </p>
   */
  public ChatterbotAdminBean()
  {
    // bean has been called from JSF framework
    log.debug("About to start ChatterbotAdmin application.");

    // initialize references to test applications
    bbCheck = new BBCheckImpl();
    qCheck = new QCheckImpl();
    ttCheck = new TTCheckImpl();
    chatterbot = new ChatterbotImpl();

    // get faces context in order to retrieve
    // root path of this web application
    FacesContext fcontext = FacesContext.getCurrentInstance();
    ServletContext scontext = (ServletContext) fcontext.getExternalContext().getContext();

    // if root path is retrieved set path to specified
    // directory where all uploaded files and all files
    // that are suposed to be downloaded reside
    sharedFilesPath = scontext.getRealPath(SHARED_FILES_DIR_NAME);
    log.debug("Shared files path is: " + sharedFilesPath);

    // application has been initialized
    log.debug("Application has been initialized.");
  }

  /**
   * <p>
   * This method is used to upload any file that becomes stored in
   * shared file path under its filename for further processing by
   * test applications.
   * </p>
   *
   * @param uploadedFile content of uploaded file
   * @param fileLocation path and filename of uploaded file
   */
  private void uploadFile(UploadedFile uploadedFile, String fileLocation)
  {
    // start upload
    log.debug("About to upload file: " + uploadedFile.getName());

    try
    {

      // get data of uploaded file
      InputStream streamIn = uploadedFile.getInputStream();
      long size = uploadedFile.getSize();
      byte[] buffer = new byte[(int) size];
      streamIn.read(buffer, 0, (int) size);

      // save data of uploaded file in new file
      File fileOut = new File(fileLocation);
      FileOutputStream streamOut = new FileOutputStream(fileOut);
      streamOut.write(buffer);

      // close stream and new file
      streamIn.close();
      streamOut.close();

      // upload has succeeded
      log.debug("File has been uploaded: " + uploadedFile.getName()
              + " to " + fileLocation);
    }
    catch (FileNotFoundException e)
    {
      // upload has failed
      log.error("Failed to upload file " + uploadedFile.getName()
              + ": " + e.getMessage());
    }
    catch (IOException e)
    {
      // upload has failed
      log.error("Failed to upload file " + uploadedFile.getName()
              + ": " + e.getMessage());
    }
  }

  /**
   * <p>
   *  This method is used to provide a file content for download.
   * </p>
   *
   * @param fileURL location of file in URL representation
   * @param outputStream to provide file with HTTP response
   * @throws IOException is thrown if downloaded file can not be created
   */
  private void downloadStream(String fileURL, ServletOutputStream outputStream)
          throws IOException
  {
    // initialize streams to read and write file content
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;

    // initialize URL string of file location to URL object
    URL url = new URL(fileURL);

    try
    {
      URLConnection urlc = url.openConnection();
      int length = urlc.getContentLength();
      InputStream in = urlc.getInputStream();

      // set streams to read and write file content
      bis = new BufferedInputStream(in);
      bos = new BufferedOutputStream(outputStream);

      // set buffer to store file content
      byte[] buff = new byte[length];
      int bytesRead;

      // read file and write its content
      // to download file
      while (-1 != (bytesRead = bis.read(buff, 0, buff.length)))
      {
        // write content
        bos.write(buff, 0, bytesRead);
      }

      // download file successfully created
      log.debug("Download file successfully created.");
    }
    catch (IOException e)
    {
      // failed to create file for download
      log.error("Failed to create file for download: " + e.getMessage());

      // report exception
      throw e;
    }
    finally
    {
      // check if input stream exists
      if (bis != null)
      {
        // input stream exists

        // close input stream
        bis.close();
      }

      // check if output stream exists
      if (bos != null)
      {
        // output stream exists

        // close ouput stream
        bos.close();
      }

      // check if servlet output stream exists
      if (outputStream != null)
      {
        // servlet output stream exists
        outputStream.flush();
        outputStream.close();
      }

      // download file successfully created
      log.debug("Streams closed.");
    }
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of topic tree file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadTopicTreeFile()
  {
    // disable further uploads
    topicTreeFileIsUploaded = Boolean.TRUE;

    // set file location of topic tree file
    topicTreeFilename = sharedFilesPath + "/" + topicTreeFile.getName();

    try
    {
      // set file url
      topicTreeFileURL = new File(topicTreeFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + topicTreeFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + topicTreeFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }

    // start upload
    uploadFile(topicTreeFile, topicTreeFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new topic tree file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewTopicTreeFile()
  {
    // enable new upload
    topicTreeFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of any macro file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadMacrosFile()
  {
    // disable further uploads
    macrosFileIsUploaded = Boolean.TRUE;

    // set file location of macro file
    macrosFilename = sharedFilesPath + "/" + macrosFile.getName();

    try
    {
      // set file url
      macrosFileURL = new File(macrosFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + macrosFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + macrosFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }

    // start upload
    uploadFile(macrosFile, macrosFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new macros file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewMacrosFile()
  {
    // enable new upload
    macrosFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of English macro file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadMacrosENFile()
  {
    // disable further uploads
    macrosENFileIsUploaded = Boolean.TRUE;

    // set file location of Englisch macro file
    macrosENFilename = sharedFilesPath + "/" + macrosENFile.getName();

    try
    {
      // set file url
      macrosENFileURL = new File(macrosENFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + macrosENFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + macrosENFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }

    // start upload
    uploadFile(macrosENFile, macrosENFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new English macros file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewMacrosENFile()
  {
    // enable new upload
    macrosENFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of German macro file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadMacrosDEFile()
  {
    // disable further uploads
    macrosDEFileIsUploaded = Boolean.TRUE;

    // set file location of German macro file
    macrosDEFilename = sharedFilesPath + "/" + macrosDEFile.getName();

    try
    {
      // set file url
      macrosDEFileURL = new File(macrosDEFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + macrosDEFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + macrosDEFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }

    // start upload
    uploadFile(macrosDEFile, macrosDEFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new German macros file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewMacrosDEFile()
  {
    // enable new upload
    macrosDEFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of Italian macro file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadMacrosITFile()
  {
    // disable further downloads
    macrosITFileIsUploaded = Boolean.TRUE;

    // set file location of Italian macro file
    macrosITFilename = sharedFilesPath + "/" + macrosITFile.getName();

    try
    {
      // set file url
      macrosITFileURL = new File(macrosITFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + macrosITFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + macrosITFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }

    // start upload
    uploadFile(macrosITFile, macrosITFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new Italian macros file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewMacrosITFile()
  {
    // enable new upload
    macrosITFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of any text corpus file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadTextCorpusFile()
  {
    // disable further uploads
    textCorpusFileIsUploaded = Boolean.TRUE;

    // set file location of text corpus file
    textCorpusFilename = sharedFilesPath + "/" + textCorpusFile.getName();

    try
    {
      // set file url
      textCorpusFileURL = new File(textCorpusFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + textCorpusFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + textCorpusFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }

    // start upload
    uploadFile(textCorpusFile, textCorpusFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new text corpus file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewTextCorpusFile()
  {
    // enable new upload
    textCorpusFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of English text corpus file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadTextCorpusENFile()
  {
    // disable further downloads
    textCorpusENFileIsUploaded = Boolean.TRUE;

    // set file location of English text corpus file
    textCorpusENFilename = sharedFilesPath + "/" + textCorpusENFile.getName();

    try
    {
      // set file url
      textCorpusENFileURL = new File(textCorpusENFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + textCorpusENFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + textCorpusENFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }

    // start upload
    uploadFile(textCorpusENFile, textCorpusENFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new English text corpus file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewTextCorpusENFile()
  {
    // enable new upload
    textCorpusENFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of German text corpus file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadTextCorpusDEFile()
  {
    // disable further downloads
    textCorpusDEFileIsUploaded = Boolean.TRUE;

    // set file location of German text corpus file
    textCorpusDEFilename = sharedFilesPath + "/" + textCorpusDEFile.getName();

    try
    {
      // set file url
      textCorpusDEFileURL = new File(textCorpusDEFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + textCorpusDEFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + textCorpusDEFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }

    // start upload
    uploadFile(textCorpusDEFile, textCorpusDEFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new German text corpus file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewTextCorpusDEFile()
  {
    // enable new upload
    textCorpusDEFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of Italian text corpus file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadTextCorpusITFile()
  {
    // disable further uploads
    textCorpusITFileIsUploaded = Boolean.TRUE;

    // set file location of Italian text corpus file
    textCorpusITFilename = sharedFilesPath + "/" + textCorpusITFile.getName();

    try
    {
      // set file url
      textCorpusITFileURL = new File(textCorpusITFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + textCorpusITFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + textCorpusITFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }

    // start upload
    uploadFile(textCorpusITFile, textCorpusITFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new Italian text corpus file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewTextCorpusITFile()
  {
    // enable new upload
    textCorpusITFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of rng file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadRngFile()
  {
    // disable further uploads
    rngFileIsUploaded = Boolean.TRUE;

    // set file location of rng file
    rngFilename = sharedFilesPath + "/" + rngFile.getName();

    try
    {
      // set file url
      rngFileURL = new File(rngFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + rngFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + rngFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }

    // start upload
    uploadFile(rngFile, rngFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new rng file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewRngFile()
  {
    // enable new upload
    rngFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of test questions file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadTestQuestionsFile()
  {
    // disable further uploads
    testQuestionsFileIsUploaded = Boolean.TRUE;

    // set file location of test questions file
    testQuestionsFilename = sharedFilesPath + "/" + testQuestionsFile.getName();

    try
    {
      // set file url
      testQuestionsFileURL = new File(testQuestionsFilename).toURI().toURL();

      // file url set
      log.debug("File url set to: " + testQuestionsFileURL);
    }
    catch (MalformedURLException e)
    {
      // failed to set url
      log.error("Failed to set url for file " + testQuestionsFile.getName()
              + ": " + e.getMessage());

      // report exception to ui
      return FAILED;
    }


    // start upload
    uploadFile(testQuestionsFile, testQuestionsFilename);

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new test questions file.
   * </p>
   *
   * @return report success or failure of this operation to ui
   */
  public String uploadNewTestQuestionsFile()
  {
    // enable new upload
    testQuestionsFileIsUploaded = Boolean.FALSE;

    // report success to ui
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to select language for bbCheck application and
   * to enable upload of language related files.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String selectBBCheckLanguage()
  {
    // langauge is already set
    log.debug("BBCheck language set to: " + bbCheckLanguage);

    // check if language is German
    if (bbCheckLanguage.equals("DE"))
    {
      // language is set to German

      // enable upload of files that are related
      // to German language by setting appropriate
      // Boolean values for view
      bbCheckLanguageENSelected = Boolean.FALSE;
      bbCheckLanguageDESelected = Boolean.TRUE;
      bbCheckLanguageITSelected = Boolean.FALSE;
    }
    else if (bbCheckLanguage.equals("IT"))
    {
      // langauge is set to Italian

      // enable upload of files that are related
      // to Italian language by setting appropriate
      // Boolean values for view
      bbCheckLanguageENSelected = Boolean.FALSE;
      bbCheckLanguageDESelected = Boolean.FALSE;
      bbCheckLanguageITSelected = Boolean.TRUE;
    }
    else
    {
      // langauge is set to English (default)

      // enable upload of files that are related
      // to English language by setting appropriate
      // Boolean values for view
      bbCheckLanguageENSelected = Boolean.TRUE;
      bbCheckLanguageDESelected = Boolean.FALSE;
      bbCheckLanguageITSelected = Boolean.FALSE;
    }

    // does not fail
    return SUCCESS;
  }

  public String selectBcheckLearning()
  {
    log.debug("Machine learning mode: " + bbCheckLearningInput);

    if (bbCheckLearningInput != null)
    {
      if (bbCheckLearningInput.equals("yes"))
      {
        bbCheckLearningSelected = Boolean.TRUE;

        log.debug("Machine learning mode has been enabled: "
                + bbCheckLearningInput);
      }
      else
      {
        bbCheckLearningSelected = Boolean.FALSE;

        log.debug("Machine learning mode has been disabled: "
                + bbCheckLearningInput);
      }
    }
    else
    {
      bbCheckLearningInput = "no";
      bbCheckLearningSelected = Boolean.FALSE;

      log.debug("Machine learning mode has been disabled: "
              + bbCheckLearningInput);
    }

    return SUCCESS;
  }

  public String performBBCheck()
  {
    log.debug("Perform bbCheck.");

    bbCheckResults = bbCheck.performBBCheck(testQuestionsFilename, topicTreeFilename,
            macrosENFilename, macrosDEFilename, macrosITFilename,
            textCorpusENFilename, textCorpusDEFilename, textCorpusITFilename,
            bbCheckLanguage, Boolean.FALSE, sharedFilesPath);

    if (bbCheckResults.isEmpty() || bbCheckResults == null)
    {
      log.warn("No test results for bbcheck received.");

      return FAILED;
    }

    log.debug("Test results for bbcheck received.");

    return SUCCESS;
  }

  public String downloadBBCheckReport()
  {

    try
    {
      // set content type
      bbCheckReportFileContentType = "application/octet-stream";

      // content type set
      log.debug("Content type set: " + bbCheckReportFileContentType);

      // read filename of test report file from manager object
      bbCheckReportFilename = bbCheck.getBBCheckTestReportFile();

      // filename of test report file read from manager object
      log.debug("Filename of test report file read from manager "
              + "object: " + bbCheckReportFilename);

      // create URL
      bbCheckReportFileURL = "file://" + sharedFilesPath + "/"
              + bbCheckReportFilename;

      // url of test report file created
      log.debug("URL of test report file created: " + bbCheckReportFileURL);

      // get JSF context is used to access HttpServlerResponse
      // objects from Java EE context.
      FacesContext facesContext = FacesContext.getCurrentInstance();

      // get HttpServletResponse object to add file name to the header of HTTP
      // response.
      HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

      // set content type and add filename of vpn profile
      // to header of HTTP response
      response.setContentType(bbCheckReportFileContentType);
      response.setHeader("Content-Disposition", "attachment;filename=\""
              + bbCheckReportFilename + "\"");

      // get ServletOutputStream object to flush file content for download
      ServletOutputStream outputStream = response.getOutputStream();

      // flush content of certification chain by retrieving file
      // specified URL from
      downloadStream(bbCheckReportFileURL, outputStream);
      outputStream.flush();

      // complete response
      facesContext.responseComplete();

      // bbCheck test report has been downloaded
      log.info("BBCheck test report has been downloaded.");

      // report success for JSF navigation rules
      return SUCCESS;
    }
    catch (IOException e)
    {
      // download bbCheck test report has failed
      log.error("Download bbCheck test report has failed: " + e.getMessage());

      // report exception for JSF navigation rules
      return FAILED;
    }
  }

  public String performQCheck()
  {
    log.debug("Perform qcheck.");

    qCheckResults = qCheck.performQCheck(macrosFilename, qCheckRegularExpression,
            qCheckUserQuestion, qCheckFormat);

    if (qCheckResults.isEmpty() || qCheckResults == null)
    {
      log.warn("No test results for qCheck received.");

      return FAILED;
    }

    log.debug("Test results for qCheck received.");

    return SUCCESS;
  }

  public String performTTCheck()
  {
    log.debug("Perform ttcheck.");

    ttCheckResults = ttCheck.performTTCheck(topicTreeFilename, rngFilename,
            macrosDEFilename, macrosENFilename, macrosITFilename);

    if (ttCheckResults.isEmpty() || ttCheckResults == null)
    {
      log.warn("No test results for ttCheck received.");

      return FAILED;
    }

    log.debug("Test results for ttCheck received.");

    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to select language for chatterbot application and
   * to enable upload of language related files.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String selectChatterbotLanguage()
  {
    // langauge is already set
    log.debug("chatterbot language set to: " + chatterbotLanguage);

    // check if language is German
    if (chatterbotLanguage.equals("DE"))
    {
      // language is set to German

      // enable upload of files that are related
      // to German language by setting appropriate
      // Boolean values for view
      chatterbotLanguageENSelected = Boolean.FALSE;
      chatterbotLanguageDESelected = Boolean.TRUE;
      chatterbotLanguageITSelected = Boolean.FALSE;
    }
    else if (chatterbotLanguage.equals("IT"))
    {
      // langauge is set to Italian

      // enable upload of files that are related
      // to Italian language by setting appropriate
      // Boolean values for view
      chatterbotLanguageENSelected = Boolean.FALSE;
      chatterbotLanguageDESelected = Boolean.FALSE;
      chatterbotLanguageITSelected = Boolean.TRUE;
    }
    else
    {
      // langauge is set to English (default)

      // enable upload of files that are related
      // to English language by setting appropriate
      // Boolean values for view
      chatterbotLanguageENSelected = Boolean.TRUE;
      chatterbotLanguageDESelected = Boolean.FALSE;
      chatterbotLanguageITSelected = Boolean.FALSE;
    }

    // does not fail
    return SUCCESS;
  }

  public String chat()
  {
    if (chatterbotLanguage.equals("DE"))
    {
      log.debug("Update chatterbot with language: " + chatterbotLanguage);
      log.debug("Update chatterbot with topic tree file: " + topicTreeFileURL);
      log.debug("Update chatterbot with macros file: " + macrosDEFileURL);
      log.debug("Update chatterbot with text corpus file: " + textCorpusDEFileURL);

      chatterbot.updateChatterbotSettings(chatterbotLanguage, topicTreeFileURL,
              macrosDEFileURL, textCorpusDEFileURL);
    }
    else if (chatterbotLanguage.equals("IT"))
    {
      log.debug("Update chatterbot with language: " + chatterbotLanguage);
      log.debug("Update chatterbot with topic tree file: " + topicTreeFileURL);
      log.debug("Update chatterbot with macros file: " + macrosITFileURL);
      log.debug("Update chatterbot with text corpus file: " + textCorpusITFileURL);

      chatterbot.updateChatterbotSettings(chatterbotLanguage, topicTreeFileURL,
              macrosITFileURL, textCorpusITFileURL);
    }
    else
    {
      log.debug("Update chatterbot with language: " + chatterbotLanguage);
      log.debug("Update chatterbot with topic tree file: " + topicTreeFileURL);
      log.debug("Update chatterbot with macros file: " + macrosENFileURL);
      log.debug("Update chatterbot with text corpus file: " + textCorpusENFileURL);

      chatterbot.updateChatterbotSettings(chatterbotLanguage, topicTreeFileURL,
              macrosENFileURL, textCorpusENFileURL);
    }

    log.debug("Chatterbot settings updated.");

    chatterbotAnswer 
            = chatterbot.getChatterbotAnswer(chatterbotQuestion, chatterbotLanguage);

    if (chatterbotAnswer == null || chatterbotAnswer.isEmpty())
    {
      log.warn("No answer received from Bob.");

      chatterbotAnswer = "Error: selected language was not initialized!";

      return FAILED;
    }

    log.debug("Bob's answer has been received.");

    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to select bbcheck application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   */
  public void selectBBCheck()
  {
    // enable bbcheck application, disable other applications
    bbCheckSelected = Boolean.TRUE;
    qCheckSelected = Boolean.FALSE;
    ttCheckSelected = Boolean.FALSE;
    chatterbotSelected = Boolean.FALSE;

    // bbcheck application has been selected
    log.info("BBCheck application has been selected.");
  }

  /**
   * <p>
   * This method is used to select qcheck application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   */
  public void selectQCheck()
  {
    // enable qcheck application, disable other applications
    bbCheckSelected = Boolean.FALSE;
    qCheckSelected = Boolean.TRUE;
    ttCheckSelected = Boolean.FALSE;
    chatterbotSelected = Boolean.FALSE;

    // qcheck application has been selected
    log.info("QCheck application has been selected.");
  }

  /**
   * <p>
   * This method is used to select ttcheck application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   */
  public void selectTTCheck()
  {
    // enable qcheck application, disable other applications
    bbCheckSelected = Boolean.FALSE;
    qCheckSelected = Boolean.FALSE;
    ttCheckSelected = Boolean.TRUE;
    chatterbotSelected = Boolean.FALSE;

    // ttcheck application has been selected
    log.info("TTCheck application has been selected.");
  }

  /**
   * <p>
   * This method is used to select chatterbot application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   */
  public void selectChatterbot()
  {
    // enable chatterbot application, disable other applications
    bbCheckSelected = Boolean.FALSE;
    qCheckSelected = Boolean.FALSE;
    ttCheckSelected = Boolean.FALSE;
    chatterbotSelected = Boolean.TRUE;

    // chatterbot application has been selected
    log.info("Chatterbot application has been selected.");
  }

  /**
   * @return the bbCheck
   */
  public BBCheck getBbCheck()
  {
    return bbCheck;
  }

  /**
   * @param bbCheck the bbCheck to set
   */
  public void setBbCheck(BBCheck bbCheck)
  {
    this.bbCheck = bbCheck;
  }

  /**
   * @return the qCheck
   */
  public QCheck getqCheck()
  {
    return qCheck;
  }

  /**
   * @param qCheck the qCheck to set
   */
  public void setqCheck(QCheck qCheck)
  {
    this.qCheck = qCheck;
  }

  /**
   * @return the ttCheck
   */
  public TTCheck getTtCheck()
  {
    return ttCheck;
  }

  /**
   * @param ttCheck the ttCheck to set
   */
  public void setTtCheck(TTCheck ttCheck)
  {
    this.ttCheck = ttCheck;
  }

  /**
   * @return the chatterbot
   */
  public Chatterbot getChatterbot()
  {
    return chatterbot;
  }

  /**
   * @param chatterbot the chatterbot to set
   */
  public void setChatterbot(Chatterbot chatterbot)
  {
    this.chatterbot = chatterbot;
  }

  /**
   * @return the bbCheckLanguage
   */
  public String getBbCheckLanguage()
  {
    return bbCheckLanguage;
  }

  /**
   * @param bbCheckLanguage the bbCheckLanguage to set
   */
  public void setBbCheckLanguage(String bbCheckLanguage)
  {
    this.bbCheckLanguage = bbCheckLanguage;
  }

  /**
   * @return the bbCheckLanguageENSelected
   */
  public Boolean getBbCheckLanguageENSelected()
  {
    return bbCheckLanguageENSelected;
  }

  /**
   * @param bbCheckLanguageENSelected the bbCheckLanguageENSelected to set
   */
  public void setBbCheckLanguageENSelected(Boolean bbCheckLanguageENSelected)
  {
    this.bbCheckLanguageENSelected = bbCheckLanguageENSelected;
  }

  /**
   * @return the bbCheckLanguageDESelected
   */
  public Boolean getBbCheckLanguageDESelected()
  {
    return bbCheckLanguageDESelected;
  }

  /**
   * @param bbCheckLanguageDESelected the bbCheckLanguageDESelected to set
   */
  public void setBbCheckLanguageDESelected(Boolean bbCheckLanguageDESelected)
  {
    this.bbCheckLanguageDESelected = bbCheckLanguageDESelected;
  }

  /**
   * @return the bbCheckLanguageITSelected
   */
  public Boolean getBbCheckLanguageITSelected()
  {
    return bbCheckLanguageITSelected;
  }

  /**
   * @param bbCheckLanguageITSelected the bbCheckLanguageITSelected to set
   */
  public void setBbCheckLanguageITSelected(Boolean bbCheckLanguageITSelected)
  {
    this.bbCheckLanguageITSelected = bbCheckLanguageITSelected;
  }

  /**
   * @return the bbCheckLearningInput
   */
  public String getBbCheckLearningInput()
  {
    return bbCheckLearningInput;
  }

  /**
   * @param bbCheckLearningInput the bbCheckLearningInput to set
   */
  public void setBbCheckLearningInput(String bbCheckLearningInput)
  {
    this.bbCheckLearningInput = bbCheckLearningInput;
  }

  /**
   * @return the bbCheckLearningSelected
   */
  public Boolean getBbCheckLearningSelected()
  {
    return bbCheckLearningSelected;
  }

  /**
   * @param bbCheckLearningSelected the bbCheckLearningSelected to set
   */
  public void setBbCheckLearningSelected(Boolean bbCheckLearningSelected)
  {
    this.bbCheckLearningSelected = bbCheckLearningSelected;
  }

  /**
   * @return the bbCheckResults
   */
  public String getBbCheckResults()
  {
    return bbCheckResults;
  }

  /**
   * @param bbCheckResults the bbCheckResults to set
   */
  public void setBbCheckResults(String bbCheckResults)
  {
    this.bbCheckResults = bbCheckResults;
  }

  /**
   * @return the bbCheckReportFileURL
   */
  public String getBbCheckReportFileURL()
  {
    return bbCheckReportFileURL;
  }

  /**
   * @param bbCheckReportFileURL the bbCheckReportFileURL to set
   */
  public void setBbCheckReportFileURL(String bbCheckReportFileURL)
  {
    this.bbCheckReportFileURL = bbCheckReportFileURL;
  }

  /**
   * @return the bbCheckReportFilename
   */
  public String getBbCheckReportFilename()
  {
    return bbCheckReportFilename;
  }

  /**
   * @param bbCheckReportFilename the bbCheckReportFilename to set
   */
  public void setBbCheckReportFilename(String bbCheckReportFilename)
  {
    this.bbCheckReportFilename = bbCheckReportFilename;
  }

  /**
   * @return the bbCheckReportFileContentType
   */
  public String getBbCheckReportFileContentType()
  {
    return bbCheckReportFileContentType;
  }

  /**
   * @param bbCheckReportFileContentType the bbCheckReportFileContentType to set
   */
  public void setBbCheckReportFileContentType(
          String bbCheckReportFileContentType)
  {
    this.bbCheckReportFileContentType = bbCheckReportFileContentType;
  }

  /**
   * @return the qCheckFormat
   */
  public String getqCheckFormat()
  {
    return qCheckFormat;
  }

  /**
   * @param qCheckFormat the qCheckFormat to set
   */
  public void setqCheckFormat(String qCheckFormat)
  {
    this.qCheckFormat = qCheckFormat;
  }

  /**
   * @return the qCheckRegularExpression
   */
  public String getqCheckRegularExpression()
  {
    return qCheckRegularExpression;
  }

  /**
   * @param qCheckRegularExpression the qCheckRegularExpression to set
   */
  public void setqCheckRegularExpression(String qCheckRegularExpression)
  {
    this.qCheckRegularExpression = qCheckRegularExpression;
  }

  /**
   * @return the qCheckUserQuestion
   */
  public String getqCheckUserQuestion()
  {
    return qCheckUserQuestion;
  }

  /**
   * @param qCheckUserQuestion the qCheckUserQuestion to set
   */
  public void setqCheckUserQuestion(String qCheckUserQuestion)
  {
    this.qCheckUserQuestion = qCheckUserQuestion;
  }

  /**
   * @return the qCheckResults
   */
  public String getqCheckResults()
  {
    return qCheckResults;
  }

  /**
   * @param qCheckResults the qCheckResults to set
   */
  public void setqCheckResults(String qCheckResults)
  {
    this.qCheckResults = qCheckResults;
  }

  /**
   * @return the ttCheckResults
   */
  public String getTtCheckResults()
  {
    return ttCheckResults;
  }

  /**
   * @param ttCheckResults the ttCheckResults to set
   */
  public void setTtCheckResults(String ttCheckResults)
  {
    this.ttCheckResults = ttCheckResults;
  }

  /**
   * @return the chatterbotLanguage
   */
  public String getChatterbotLanguage()
  {
    return chatterbotLanguage;
  }

  /**
   * @param chatterbotLanguage the chatterbotLanguage to set
   */
  public void setChatterbotLanguage(String chatterbotLanguage)
  {
    this.chatterbotLanguage = chatterbotLanguage;
  }

  /**
   * @return the chatterbotLanguageENSelected
   */
  public Boolean getChatterbotLanguageENSelected()
  {
    return chatterbotLanguageENSelected;
  }

  /**
   * @param chatterbotLanguageENSelected the chatterbotLanguageENSelected to set
   */
  public void setChatterbotLanguageENSelected(
          Boolean chatterbotLanguageENSelected)
  {
    this.chatterbotLanguageENSelected = chatterbotLanguageENSelected;
  }

  /**
   * @return the chatterbotLanguageDESelected
   */
  public Boolean getChatterbotLanguageDESelected()
  {
    return chatterbotLanguageDESelected;
  }

  /**
   * @param chatterbotLanguageDESelected the chatterbotLanguageDESelected to set
   */
  public void setChatterbotLanguageDESelected(
          Boolean chatterbotLanguageDESelected)
  {
    this.chatterbotLanguageDESelected = chatterbotLanguageDESelected;
  }

  /**
   * @return the chatterbotLanguageITSelected
   */
  public Boolean getChatterbotLanguageITSelected()
  {
    return chatterbotLanguageITSelected;
  }

  /**
   * @param chatterbotLanguageITSelected the chatterbotLanguageITSelected to set
   */
  public void setChatterbotLanguageITSelected(
          Boolean chatterbotLanguageITSelected)
  {
    this.chatterbotLanguageITSelected = chatterbotLanguageITSelected;
  }

  /**
   * @return the chatterbotAnswer
   */
  public String getChatterbotAnswer()
  {
    return chatterbotAnswer;
  }

  /**
   * @param chatterbotAnswer the chatterbotAnswer to set
   */
  public void setChatterbotAnswer(String chatterbotAnswer)
  {
    this.chatterbotAnswer = chatterbotAnswer;
  }

  /**
   * @return the chatterbotQuestion
   */
  public String getChatterbotQuestion()
  {
    return chatterbotQuestion;
  }

  /**
   * @param chatterbotQuestion the chatterbotQuestion to set
   */
  public void setChatterbotQuestion(String chatterbotQuestion)
  {
    this.chatterbotQuestion = chatterbotQuestion;
  }

  /**
   * @return the topicTreeFile
   */
  public UploadedFile getTopicTreeFile()
  {
    return topicTreeFile;
  }

  /**
   * @param topicTreeFile the topicTreeFile to set
   */
  public void setTopicTreeFile(UploadedFile topicTreeFile)
  {
    this.topicTreeFile = topicTreeFile;
  }

  /**
   * @return the topicTreeFilename
   */
  public String getTopicTreeFilename()
  {
    return topicTreeFilename;
  }

  /**
   * @param topicTreeFilename the topicTreeFilename to set
   */
  public void setTopicTreeFilename(String topicTreeFilename)
  {
    this.topicTreeFilename = topicTreeFilename;
  }

  /**
   * @return the topicTreeFileIsUploaded
   */
  public Boolean getTopicTreeFileIsUploaded()
  {
    return topicTreeFileIsUploaded;
  }

  /**
   * @param topicTreeFileIsUploaded the topicTreeFileIsUploaded to set
   */
  public void setTopicTreeFileIsUploaded(Boolean topicTreeFileIsUploaded)
  {
    this.topicTreeFileIsUploaded = topicTreeFileIsUploaded;
  }

  /**
   * @return the macrosFile
   */
  public UploadedFile getMacrosFile()
  {
    return macrosFile;
  }

  /**
   * @param macrosFile the macrosFile to set
   */
  public void setMacrosFile(UploadedFile macrosFile)
  {
    this.macrosFile = macrosFile;
  }

  /**
   * @return the macrosFilename
   */
  public String getMacrosFilename()
  {
    return macrosFilename;
  }

  /**
   * @param macrosFilename the macrosFilename to set
   */
  public void setMacrosFilename(String macrosFilename)
  {
    this.macrosFilename = macrosFilename;
  }

  /**
   * @return the macrosFileIsUploaded
   */
  public Boolean getMacrosFileIsUploaded()
  {
    return macrosFileIsUploaded;
  }

  /**
   * @param macrosFileIsUploaded the macrosFileIsUploaded to set
   */
  public void setMacrosFileIsUploaded(Boolean macrosFileIsUploaded)
  {
    this.macrosFileIsUploaded = macrosFileIsUploaded;
  }

  /**
   * @return the macrosENFile
   */
  public UploadedFile getMacrosENFile()
  {
    return macrosENFile;
  }

  /**
   * @param macrosENFile the macrosENFile to set
   */
  public void setMacrosENFile(UploadedFile macrosENFile)
  {
    this.macrosENFile = macrosENFile;
  }

  /**
   * @return the macrosENFilename
   */
  public String getMacrosENFilename()
  {
    return macrosENFilename;
  }

  /**
   * @param macrosENFilename the macrosENFilename to set
   */
  public void setMacrosENFilename(String macrosENFilename)
  {
    this.macrosENFilename = macrosENFilename;
  }

  /**
   * @return the macrosENFileIsUploaded
   */
  public Boolean getMacrosENFileIsUploaded()
  {
    return macrosENFileIsUploaded;
  }

  /**
   * @param macrosENFileIsUploaded the macrosENFileIsUploaded to set
   */
  public void setMacrosENFileIsUploaded(Boolean macrosENFileIsUploaded)
  {
    this.macrosENFileIsUploaded = macrosENFileIsUploaded;
  }

  /**
   * @return the macrosDEFile
   */
  public UploadedFile getMacrosDEFile()
  {
    return macrosDEFile;
  }

  /**
   * @param macrosDEFile the macrosDEFile to set
   */
  public void setMacrosDEFile(UploadedFile macrosDEFile)
  {
    this.macrosDEFile = macrosDEFile;
  }

  /**
   * @return the macrosDEFilename
   */
  public String getMacrosDEFilename()
  {
    return macrosDEFilename;
  }

  /**
   * @param macrosDEFilename the macrosDEFilename to set
   */
  public void setMacrosDEFilename(String macrosDEFilename)
  {
    this.macrosDEFilename = macrosDEFilename;
  }

  /**
   * @return the macrosDEFileIsUploaded
   */
  public Boolean getMacrosDEFileIsUploaded()
  {
    return macrosDEFileIsUploaded;
  }

  /**
   * @param macrosDEFileIsUploaded the macrosDEFileIsUploaded to set
   */
  public void setMacrosDEFileIsUploaded(Boolean macrosDEFileIsUploaded)
  {
    this.macrosDEFileIsUploaded = macrosDEFileIsUploaded;
  }

  /**
   * @return the macrosITFile
   */
  public UploadedFile getMacrosITFile()
  {
    return macrosITFile;
  }

  /**
   * @param macrosITFile the macrosITFile to set
   */
  public void setMacrosITFile(UploadedFile macrosITFile)
  {
    this.macrosITFile = macrosITFile;
  }

  /**
   * @return the macrosITFilename
   */
  public String getMacrosITFilename()
  {
    return macrosITFilename;
  }

  /**
   * @param macrosITFilename the macrosITFilename to set
   */
  public void setMacrosITFilename(String macrosITFilename)
  {
    this.macrosITFilename = macrosITFilename;
  }

  /**
   * @return the macrosITFileIsUploaded
   */
  public Boolean getMacrosITFileIsUploaded()
  {
    return macrosITFileIsUploaded;
  }

  /**
   * @param macrosITFileIsUploaded the macrosITFileIsUploaded to set
   */
  public void setMacrosITFileIsUploaded(Boolean macrosITFileIsUploaded)
  {
    this.macrosITFileIsUploaded = macrosITFileIsUploaded;
  }

  /**
   * @return the textCorpusFile
   */
  public UploadedFile getTextCorpusFile()
  {
    return textCorpusFile;
  }

  /**
   * @param textCorpusFile the textCorpusFile to set
   */
  public void setTextCorpusFile(UploadedFile textCorpusFile)
  {
    this.textCorpusFile = textCorpusFile;
  }

  /**
   * @return the textCorpusFilename
   */
  public String getTextCorpusFilename()
  {
    return textCorpusFilename;
  }

  /**
   * @param textCorpusFilename the textCorpusFilename to set
   */
  public void setTextCorpusFilename(String textCorpusFilename)
  {
    this.textCorpusFilename = textCorpusFilename;
  }

  /**
   * @return the textCorpusFileIsUploaded
   */
  public Boolean getTextCorpusFileIsUploaded()
  {
    return textCorpusFileIsUploaded;
  }

  /**
   * @param textCorpusFileIsUploaded the textCorpusFileIsUploaded to set
   */
  public void setTextCorpusFileIsUploaded(Boolean textCorpusFileIsUploaded)
  {
    this.textCorpusFileIsUploaded = textCorpusFileIsUploaded;
  }

  /**
   * @return the textCorpusENFile
   */
  public UploadedFile getTextCorpusENFile()
  {
    return textCorpusENFile;
  }

  /**
   * @param textCorpusENFile the textCorpusENFile to set
   */
  public void setTextCorpusENFile(UploadedFile textCorpusENFile)
  {
    this.textCorpusENFile = textCorpusENFile;
  }

  /**
   * @return the textCorpusENFilename
   */
  public String getTextCorpusENFilename()
  {
    return textCorpusENFilename;
  }

  /**
   * @param textCorpusENFilename the textCorpusENFilename to set
   */
  public void setTextCorpusENFilename(String textCorpusENFilename)
  {
    this.textCorpusENFilename = textCorpusENFilename;
  }

  /**
   * @return the textCorpusENFileIsUploaded
   */
  public Boolean getTextCorpusENFileIsUploaded()
  {
    return textCorpusENFileIsUploaded;
  }

  /**
   * @param textCorpusENFileIsUploaded the textCorpusENFileIsUploaded to set
   */
  public void setTextCorpusENFileIsUploaded(Boolean textCorpusENFileIsUploaded)
  {
    this.textCorpusENFileIsUploaded = textCorpusENFileIsUploaded;
  }

  /**
   * @return the textCorpusDEFile
   */
  public UploadedFile getTextCorpusDEFile()
  {
    return textCorpusDEFile;
  }

  /**
   * @param textCorpusDEFile the textCorpusDEFile to set
   */
  public void setTextCorpusDEFile(UploadedFile textCorpusDEFile)
  {
    this.textCorpusDEFile = textCorpusDEFile;
  }

  /**
   * @return the textCorpusDEFilename
   */
  public String getTextCorpusDEFilename()
  {
    return textCorpusDEFilename;
  }

  /**
   * @param textCorpusDEFilename the textCorpusDEFilename to set
   */
  public void setTextCorpusDEFilename(String textCorpusDEFilename)
  {
    this.textCorpusDEFilename = textCorpusDEFilename;
  }

  /**
   * @return the textCorpusDEFileIsUploaded
   */
  public Boolean getTextCorpusDEFileIsUploaded()
  {
    return textCorpusDEFileIsUploaded;
  }

  /**
   * @param textCorpusDEFileIsUploaded the textCorpusDEFileIsUploaded to set
   */
  public void setTextCorpusDEFileIsUploaded(Boolean textCorpusDEFileIsUploaded)
  {
    this.textCorpusDEFileIsUploaded = textCorpusDEFileIsUploaded;
  }

  /**
   * @return the textCorpusITFile
   */
  public UploadedFile getTextCorpusITFile()
  {
    return textCorpusITFile;
  }

  /**
   * @param textCorpusITFile the textCorpusITFile to set
   */
  public void setTextCorpusITFile(UploadedFile textCorpusITFile)
  {
    this.textCorpusITFile = textCorpusITFile;
  }

  /**
   * @return the textCorpusITFilename
   */
  public String getTextCorpusITFilename()
  {
    return textCorpusITFilename;
  }

  /**
   * @param textCorpusITFilename the textCorpusITFilename to set
   */
  public void setTextCorpusITFilename(String textCorpusITFilename)
  {
    this.textCorpusITFilename = textCorpusITFilename;
  }

  /**
   * @return the textCorpusITFileIsUploaded
   */
  public Boolean getTextCorpusITFileIsUploaded()
  {
    return textCorpusITFileIsUploaded;
  }

  /**
   * @param textCorpusITFileIsUploaded the textCorpusITFileIsUploaded to set
   */
  public void setTextCorpusITFileIsUploaded(Boolean textCorpusITFileIsUploaded)
  {
    this.textCorpusITFileIsUploaded = textCorpusITFileIsUploaded;
  }

  /**
   * @return the rngFile
   */
  public UploadedFile getRngFile()
  {
    return rngFile;
  }

  /**
   * @param rngFile the rngFile to set
   */
  public void setRngFile(UploadedFile rngFile)
  {
    this.rngFile = rngFile;
  }

  /**
   * @return the rngFilename
   */
  public String getRngFilename()
  {
    return rngFilename;
  }

  /**
   * @param rngFilename the rngFilename to set
   */
  public void setRngFilename(String rngFilename)
  {
    this.rngFilename = rngFilename;
  }

  /**
   * @return the rngFileIsUploaded
   */
  public Boolean getRngFileIsUploaded()
  {
    return rngFileIsUploaded;
  }

  /**
   * @param rngFileIsUploaded the rngFileIsUploaded to set
   */
  public void setRngFileIsUploaded(Boolean rngFileIsUploaded)
  {
    this.rngFileIsUploaded = rngFileIsUploaded;
  }

  /**
   * @return the testQuestionsFile
   */
  public UploadedFile getTestQuestionsFile()
  {
    return testQuestionsFile;
  }

  /**
   * @param testQuestionsFile the testQuestionsFile to set
   */
  public void setTestQuestionsFile(UploadedFile testQuestionsFile)
  {
    this.testQuestionsFile = testQuestionsFile;
  }

  /**
   * @return the testQuestionsFilename
   */
  public String getTestQuestionsFilename()
  {
    return testQuestionsFilename;
  }

  /**
   * @param testQuestionsFilename the testQuestionsFilename to set
   */
  public void setTestQuestionsFilename(String testQuestionsFilename)
  {
    this.testQuestionsFilename = testQuestionsFilename;
  }

  /**
   * @return the testQuestionsFileIsUploaded
   */
  public Boolean getTestQuestionsFileIsUploaded()
  {
    return testQuestionsFileIsUploaded;
  }

  /**
   * @param testQuestionsFileIsUploaded the testQuestionsFileIsUploaded to set
   */
  public void setTestQuestionsFileIsUploaded(Boolean testQuestionsFileIsUploaded)
  {
    this.testQuestionsFileIsUploaded = testQuestionsFileIsUploaded;
  }

  /**
   * @return the sharedFilesPath
   */
  public String getSharedFilesPath()
  {
    return sharedFilesPath;
  }

  /**
   * @param sharedFilesPath the sharedFilesPath to set
   */
  public void setSharedFilesPath(String sharedFilesPath)
  {
    this.sharedFilesPath = sharedFilesPath;
  }

  /**
   * @return the locale
   */
  public String getLocale()
  {
    return locale;
  }

  /**
   * @param locale the locale to set
   */
  public void setLocale(String locale)
  {
    this.locale = locale;
  }

  /**
   * @return the bbCheckSelected
   */
  public Boolean getBbCheckSelected()
  {
    return bbCheckSelected;
  }

  /**
   * @param bbCheckSelected the bbCheckSelected to set
   */
  public void setBbCheckSelected(Boolean bbCheckSelected)
  {
    this.bbCheckSelected = bbCheckSelected;
  }

  /**
   * @return the qCheckSelected
   */
  public Boolean getqCheckSelected()
  {
    return qCheckSelected;
  }

  /**
   * @param qCheckSelected the qCheckSelected to set
   */
  public void setqCheckSelected(Boolean qCheckSelected)
  {
    this.qCheckSelected = qCheckSelected;
  }

  /**
   * @return the ttCheckSelected
   */
  public Boolean getTtCheckSelected()
  {
    return ttCheckSelected;
  }

  /**
   * @param ttCheckSelected the ttCheckSelected to set
   */
  public void setTtCheckSelected(Boolean ttCheckSelected)
  {
    this.ttCheckSelected = ttCheckSelected;
  }

  /**
   * @return the chatterbotSelected
   */
  public Boolean getChatterbotSelected()
  {
    return chatterbotSelected;
  }

  /**
   * @param chatterbotSelected the chatterbotSelected to set
   */
  public void setChatterbotSelected(Boolean chatterbotSelected)
  {
    this.chatterbotSelected = chatterbotSelected;
  }

  /**
   * @return the SHARED_FILES_DIR_NAME
   */
  public String getSHARED_FILES_DIR_NAME()
  {
    return SHARED_FILES_DIR_NAME;
  }

  /**
   * @param SHARED_FILES_DIR_NAME the SHARED_FILES_DIR_NAME to set
   */
  public void setSHARED_FILES_DIR_NAME(String SHARED_FILES_DIR_NAME)
  {
    this.SHARED_FILES_DIR_NAME = SHARED_FILES_DIR_NAME;
  }

  /**
   * @return the SUCCESS
   */
  public String getSUCCESS()
  {
    return SUCCESS;
  }

  /**
   * @param SUCCESS the SUCCESS to set
   */
  public void setSUCCESS(String SUCCESS)
  {
    this.SUCCESS = SUCCESS;
  }

  /**
   * @return the FAILED
   */
  public String getFAILED()
  {
    return FAILED;
  }

  /**
   * @param FAILED the FAILED to set
   */
  public void setFAILED(String FAILED)
  {
    this.FAILED = FAILED;
  }

  /**
   * @return the log
   */
  public Logger getLog()
  {
    return log;
  }

  /**
   * @param log the log to set
   */
  public void setLog(Logger log)
  {
    this.log = log;
  }
}
