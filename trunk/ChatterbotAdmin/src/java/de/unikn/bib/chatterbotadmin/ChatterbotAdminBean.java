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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 * <p>
 *  This class represents a Java Server Faces Bean class that implements the
 *  application logic of ChatterbotAdmin application by controlling in- and
 *  outputs on view. It also aggregates the tests applications (bbCheck, qCheck
 *  and ttCheck) and a chat application (chatterbot) for testing purpose.
 * </p>
 * <p>
 *  All attributes and their default values that are managed by the UI of
 *  this application are also listed in faces-config.xml file.
 * </p>
 *
 * @author manuel.kirschner@gmail.com
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
   * This String object contains the currently set language to perform test
   * of bbCheck application.
   * </p>
   */
  private String bbCheckLanguage;

  /**
   * <p>
   * This Boolean object indicates if English language is currently set to
   * perform test of bbCheck application.
   * </p>
   */
  private Boolean bbCheckLanguageENSelected;

  /**
   * <p>
   * This Boolean object indicates if German language is currently set to
   * perform test of bbCheck application.
   * </p>
   */
  private Boolean bbCheckLanguageDESelected;

  /**
   * <p>
   * This Boolean object indicates if Italian language is currently set to
   * perform test of bbCheck application.
   * </p>
   */
  private Boolean bbCheckLanguageITSelected;

  /**
   * <p>
   * This String object is used to enable/disable selection of machine
   * learning mode to perform test of bbCheck application.
   * </p>
   */
  private String bbCheckLearningInput;

  /**
   * <p>
   * This Boolean object indicates if machine learning mode has been selected
   * to perform test of bbCheck application.
   * </p>
   */
  private Boolean bbCheckLearningSelected;

  /**
   * <p>
   * This String object contains the test results after test of bbCheck
   * application has finished.
   * </p>
   */
  private String bbCheckResults;

  /**
   * <p>
   * This Boolean object indicated if a test report of bbCheck application
   * is available for download.
   * </p>
   */
  private Boolean bbCheckReportIsAvailable;

  /**
   * <p>
   * This String object contains the URL ("file://") of the test report file
   * of bbCheck application. Path of this file is usually determined by value
   * of sharedFilePath attribute.
   * </p>
   */
  private String bbCheckReportFileURL;

  /**
   * <p>
   * This String object contains the filename of the test report file
   * of bbCheck application.
   * </p>
   */
  private String bbCheckReportFilename;

  /**
   * <p>
   * This String object contains the content type of the test report file
   * of bbCheck application. It is usually set to "application/octet-stream".
   * </p>
   */
  private String bbCheckReportFileContentType;

  /**
   * <p>
   * This String object is used to select which format is about to be used
   * to perform test of qCheck application.
   * </p>
   */
  private String qCheckFormat;

  /**
   * <p>
   * This String object contains input of a regular expression to perform
   * test of qCheck application.
   * </p>
   */
  private String qCheckRegularExpression;

  /**
   * <p>
   * This String object contains input of a user question  to perform
   * test of qCheck application.
   * </p>
   */
  private String qCheckUserQuestion;

  /**
   * <p>
   * This String object contains the test results after test of qCheck
   * application has finished.
   * </p>
   */
  private String qCheckResults;

  /**
   * <p>
   * This String object contains the test results after test of ttCheck
   * application has finished.
   * </p>
   */
  private String ttCheckResults;

  /**
   * <p>
   * This String object is used to select which language is about
   * to be used to perform test of chatterbot application.
   * </p>
   */
  private String chatterbotLanguage;

  /**
   * <p>
   * This Boolean object indicates if English language is currently set to
   *  perform test of chatterbot application.
   * </p>
   */
  private Boolean chatterbotLanguageENSelected;

  /**
   * <p>
   * This Boolean object indicates if German language is currently set to
   * perform test of chatterbot application.
   * </p>
   */
  private Boolean chatterbotLanguageDESelected;

  /**
   * <p>
   * This Boolean object indicates if Italian language is currently set to
   * perform test of chatterbot application.
   * </p>
   */
  private Boolean chatterbotLanguageITSelected;

  /**
   * <p>
   * This String object contains a user's question for to
   * perform test of chatterbot application.
   * </p>
   */
  private String chatterbotQuestion;

  /**
   * <p>
   * This String object contains Bob's answer to as test result when
   * test of chatterbot application has finished.
   * </p>
   */
  private String chatterbotAnswer;

  /**
   * <p>
   * This String object contains all user questions and their corresponding
   * answers from Bob depending on currently set language.
   * </p>
   */
  private String chatterbotChatText;

  /**
   * <p>
   * This String object contains all user questions and their corresponding
   * answers from Bob when English language has been selected.
   * </p>
   */
  private String chatterbotChatTextEN;

  /**
   * <p>
   * This String object contains all user questions and their corresponding
   * answers from Bob when German language has been selected.
   * </p>
   */
  private String chatterbotChatTextDE;

  /**
   * <p>
   * This String object contains all user questions and their corresponding
   * answers from Bob when Italian language has been selected.
   * </p>
   */
  private String chatterbotChatTextIT;

  /**
   * <p>
   * This Object represents the reference to the content of topic tree file
   * after upload of this file has succeeded.
   * </p>
   */
  private UploadedFile topicTreeFile;

  /**
   * <p>
   * This String object contains the filename of topic tree file after upload
   * of this file has succeeded.
   * </p>
   */
  private String topicTreeFilename;

  /**
   * <p>
   * This String object contains the file URL of topic tree file after upload
   * of this file has succeeded.
   * </p>
   */
  private URL topicTreeFileURL;

  /**
   * <p>
   * This Boolean object indicates if topic tree file has already been uploaded.
   * </p>
   */
  private Boolean topicTreeFileIsUploaded;

  /**
   * <p>
   * This Object represents the reference to the content of a macros file
   * after upload of this file has succeeded.
   * </p>
   */
  private UploadedFile macrosFile;

  /**
   * <p>
   * This String object contains the filename of a macros file after upload
   * of this file has succeeded.
   * </p>
   */
  private String macrosFilename;

  /**
   * <p>
   * This String object contains the file URL of a macros file after upload
   * of this file has succeeded.
   * </p>
   */
  private URL macrosFileURL;

  /**
   * <p>
   * This Boolean object indicates if a macros file has already been uploaded.
   * </p>
   */
  private Boolean macrosFileIsUploaded;

  /**
   * <p>
   * This Object represents the reference to the content of English macros file
   * after upload of this file has succeeded.
   * </p>
   */
  private UploadedFile macrosENFile;

  /**
   * <p>
   * This String object contains the filename of English macros file after
   * upload of this file has succeeded.
   * </p>
   */
  private String macrosENFilename;

  /**
   * <p>
   * This String object contains the file url of English macros file after
   * upload of this file has succeeded.
   * </p>
   */
  private URL macrosENFileURL;

  /**
   * <p>
   * This Boolean object indicates if English macros file has already been
   * uploaded.
   * </p>
   */
  private Boolean macrosENFileIsUploaded;

  /**
   * <p>
   * This Object represents the reference to the content of German macros file
   * after upload of this file has succeeded.
   * </p>
   */
  private UploadedFile macrosDEFile;

  /**
   * <p>
   * This String object contains the filename of German macros file after
   * upload of this file has succeeded.
   * </p>
   */
  private String macrosDEFilename;

  /**
   * <p>
   * This String object contains the file URL of German macros file after
   * upload of this file has succeeded.
   * </p>
   */
  private URL macrosDEFileURL;

  /**
   * <p>
   * This Boolean object indicates if German macros file has already been
   * uploaded.
   * </p>
   */
  private Boolean macrosDEFileIsUploaded;

  /**
   * <p>
   * This Object represents the reference to the content of Italian macros file
   * after upload of this file has succeeded.
   * </p>
   */
  private UploadedFile macrosITFile;

  /**
   * <p>
   * This String object contains the filename of Italian macros file after
   * upload of this file has succeeded.
   * </p>
   */
  private String macrosITFilename;

  /**
   * <p>
   * This String object contains the file URL of Italian macros file after
   * upload of this file has succeeded.
   * </p>
   */
  private URL macrosITFileURL;

  /**
   * <p>
   * This Boolean object indicates if Italian macros file has already been
   * uploaded.
   * </p>
   */
  private Boolean macrosITFileIsUploaded;

  /**
   * <p>
   * This Object represents the reference to the content of a text corpus file
   * after upload of this file has succeeded.
   * </p>
   */
  private UploadedFile textCorpusFile;

  /**
   * <p>
   * This String object contains the filename of a text corpus file after
   * upload of this file has succeeded.
   * </p>
   */
  private String textCorpusFilename;

  /**
   * <p>
   * This String object contains the file url of a text corpus file after
   * upload of this file has succeeded.
   * </p>
   */
  private URL textCorpusFileURL;

  /**
   * <p>
   * This Boolean object indicates if a text corpus file has already been
   * uploaded.
   * </p>
   */
  private Boolean textCorpusFileIsUploaded;

  /**
   * <p>
   * This Object represents the reference to the content of English text corpus
   * file after upload of this file has succeeded.
   * </p>
   */
  private UploadedFile textCorpusENFile;

  /**
   * <p>
   * This String object contains the filename of English text corpus file after
   * upload of this file has succeeded.
   * </p>
   */
  private String textCorpusENFilename;

  /**
   * <p>
   * This String object contains the file URL of English text corpus file after
   * upload of this file has succeeded.
   * </p>
   */
  private URL textCorpusENFileURL;

  /**
   * <p>
   * This Boolean object indicates if English text corpus file has already been
   * uploaded.
   * </p>
   */
  private Boolean textCorpusENFileIsUploaded;

  /**
   * <p>
   * This Object represents the reference to the content of German text corpus
   * file after upload of this file has succeeded.
   * </p>
   */
  private UploadedFile textCorpusDEFile;

  /**
   * <p>
   * This String object contains the filename of German text corpus file after
   * upload of this file has succeeded.
   * </p>
   */
  private String textCorpusDEFilename;

  /**
   * <p>
   * This String object contains the file url of German text corpus file after
   * upload of this file has succeeded.
   * </p>
   */
  private URL textCorpusDEFileURL;

  /**
   * <p>
   * This Boolean object indicates if German text corpus file has already been
   * uploaded.
   * </p>
   */
  private Boolean textCorpusDEFileIsUploaded;

  /**
   * <p>
   * This Object represents the reference to the content of Italian text corpus
   * file after upload of this file has succeeded.
   * </p>
   */
  private UploadedFile textCorpusITFile;

  /**
   * <p>
   * This String object contains the filename of Italian text corpus file after
   * upload of this file has succeeded.
   * </p>
   */
  private String textCorpusITFilename;

  /**
   * <p>
   * This String object contains the file URL of Italian text corpus file after
   * upload of this file has succeeded.
   * </p>
   */
  private URL textCorpusITFileURL;

  /**
   * <p>
   * This Boolean object indicates if Italian text corpus file has already been
   * uploaded.
   * </p>
   */
  private Boolean textCorpusITFileIsUploaded;

  /**
   * <p>
   * This Object represents the reference to the content of rng file after
   * upload of this file has succeeded.
   * </p>
   */
  private UploadedFile rngFile;

  /**
   * <p>
   * This String object contains the filename of a rng file after upload of
   * this file has succeeded.
   * </p>
   */
  private String rngFilename;

  /**
   * <p>
   * This String object contains the file URL of a rng file after upload of
   * this file has succeeded.
   * </p>
   */
  private URL rngFileURL;

  /**
   * <p>
   * This Boolean object indicates if a rng file has already been uploaded.
   * </p>
   */
  private Boolean rngFileIsUploaded;

  /**
   * <p>
   * This Object represents the reference to the content of English test
   * questions file after upload of this file has succeeded.
   * </p>
   */
  private UploadedFile testQuestionsFile;

  /**
   * <p>
   * This String object contains the filename of a test questions file after
   * upload of this file has succeeded.
   * </p>
   */
  private String testQuestionsFilename;

  /**
   * <p>
   * This String object contains the file URL of a test questions file after
   * upload of this file has succeeded.
   * </p>
   */
  private URL testQuestionsFileURL;

  /**
   * <p>
   * This Boolean object indicates if a test questions file has already been
   * uploaded.
   * </p>
   */
  private Boolean testQuestionsFileIsUploaded;

  /**
   * <p>
   * This String object contains
   * </p>
   */
  private String sharedFilesPath;

  /**
   * <p>
   * This String value contains the currently chosen language to display
   * messages from message bundle file.
   * </p>
   */
  private String locale;

  /**
   * <p>
   * This Boolean object indicates if bbcheck application has been selected.
   * </p>
   */
  private Boolean bbCheckSelected;

  /**
   * <p>
   * This Boolean object indicates if qcheck application has been selected.
   * </p>
   */
  private Boolean qCheckSelected;

  /**
   * <p>
   * This Boolean object indicates if ttcheck application has been selected.
   * </p>
   */
  private Boolean ttCheckSelected;

  /**
   * <p>
   * This Boolean object indicates if chatterbot application has been selected.
   * </p>
   */
  private Boolean chatterbotSelected;

  /**
   * <p>
   * This String represents the directory name where all files are stored that
   * are required by this application.
   * </p>
   */
  private final String SHARED_FILES_DIR_NAME = "/shared-files";

  /**
   * <p>
   * This String constant contains the value that is used to check whether
   * English language has been selected to perform any test of this application.
   * This value must equal the item value that is used for appropriate
   * <tt>selectItem</tt> tag in <tt>bbcheck.jspx</tt>:
   * </p>
   * <xmp>
   *  <t:selectOneRadio
   *   ...
   *   value="#{chatterbotAdminBean.bbCheckLanguage}">
   *   <f:selectItem
   *    ...
   *    itemValue="EN" />
   *  </t:selectOneRadio>
   * </xmp>
   * <p>
   *  and <tt>chatterbot.jspx</tt>:
   * </p>
   * <xmp>
   *  <t:selectOneRadio
   *   ...
   *   value="#{chatterbotAdminBean.chatterbotLanguage}">
   *   ...
   *   <f:selectItem
   *    ...
   *    itemValue="EN" />
   *    ...
   *  </t:selectOneRadio>
   * </xmp>
   */
  private final String EN = "EN";

  /**
   * <p>
   * This String constant contains the value that is used to check whether
   * German language has been selected to perform any test of this application.
   * This value must equal the item value that is used for appropriate
   * <tt>selectItem</tt> tag in <tt>bbcheck.jspx</tt>:
   * </p>
   * <xmp>
   *  <t:selectOneRadio
   *   ...
   *   value="#{chatterbotAdminBean.bbCheckLanguage}">
   *   <f:selectItem
   *    ...
   *    itemValue="DE" />
   *  </t:selectOneRadio>
   * </xmp>
   * <p>
   *  and <tt>chatterbot.jspx</tt>:
   * </p>
   * <xmp>
   *  <t:selectOneRadio
   *   ...
   *   value="#{chatterbotAdminBean.chatterbotLanguage}">
   *   ...
   *   <f:selectItem
   *    ...
   *    itemValue="DE" />
   *    ...
   *  </t:selectOneRadio>
   * </xmp>
   */
  private final String DE = "DE";

  /**
   * <p>
   * This String constant contains the value that is used to check whether
   * Italian language has been selected to perform any test of this application.
   ** This value must equal the item value that is used for appropriate
   * <tt>selectItem</tt> tag in <tt>bbcheck.jspx</tt>:
   * </p>
   * <xmp>
   *  <t:selectOneRadio
   *   ...
   *   value="#{chatterbotAdminBean.bbCheckLanguage}">
   *   <f:selectItem
   *    ...
   *    itemValue="IT" />
   *    ...
   *  </t:selectOneRadio>
   * </xmp>
   * <p>
   *  and <tt>chatterbot.jspx</tt>:
   * </p>
   * <xmp>
   *  <t:selectOneRadio
   *   ...
   *   value="#{chatterbotAdminBean.chatterbotLanguage}">
   *   <f:selectItem
   *    ...
   *    itemValue="IT" />
   *    ...
   *  </t:selectOneRadio>
   * </xmp>
   */
  private final String IT = "IT";

  /**
   * <p>
   * This String constant contains the value that is used to enable machine
   * learning mode for bbCheck application. This value must equal the item
   * value that is used for appropriate <tt>selectItem</tt> tag in
   * <tt>bbcheck.jspx</tt>:
   * </p>
   * <xmp>
   *  <t:selectOneRadio
   *   ...
   *   value="#{chatterbotAdminBean.bbCheckLearningInput}">
   *   ...
   *   <f:selectItem
   *    ...
   *    itemValue="yes" />
   *    ...
   *  </t:selectOneRadio>
   * </xmp>
   */
  private final String BBCHECK_ENABLE_MACHINE_LEARNING_MODE = "yes";

  /**
   * <p>
   * This String constant contains the value that is used to enable machine
   * learning mode for bbCheck application. This value must equal the item
   * value that is used for appropriate <tt>selectItem</tt> tag in
   * <tt>bbcheck.jspx</tt>:
   * </p>
   * <xmp>
   *  <t:selectOneRadio
   *   ...
   *   value="#{chatterbotAdminBean.bbCheckLearningInput}">
   *   ...
   *   <f:selectItem
   *    ...
   *    itemValue="no" />
   *    ...
   *  </t:selectOneRadio>
   * </xmp>
   */
  private final String BBCHECK_DISABLE_MACHINE_LEARNING_MODE = "no";

  /**
   * <p>
   * This String constant contains the value that is used to enable the topic
   * tree file format for qCheck application. This value must equal the item
   * value that is used for appropriate <tt>selectItem</tt> tag in
   * <tt>qcheck.jspx</tt>:
   * </p>
   * <xmp>
   *  <t:selectOneRadio
   *   ...
   *   value="#{chatterbotAdminBean.bbCheckLearningInput}">
   *   ...
   *   <f:selectItem
   *    ...
   *    itemValue="tt" />
   *    ...
   *  </t:selectOneRadio>
   * </xmp>
   * </p>
   */
  private final String QCHECK_TT_FILE_FORMAT = "tt";

  /**
   * <p>
   * This String constant the value that is used to enable the macro file
   * format for qCheck application. This value must equal the item value
   * that is used for appropriate <tt>selectItem</tt> tag in
   * <tt>qcheck.jspx</tt>:
   * </p>
   * <xmp>
   *  <t:selectOneRadio
   *   ...
   *   value="#{chatterbotAdminBean.bbCheckLearningInput}">
   *   ...
   *   <f:selectItem
   *    ...
   *    itemValue="macro" />
   *    ...
   *  </t:selectOneRadio>
   * </xmp>
   * </p>
   */
  private final String QCHECK_MACRO_FILE_FORMAT = "macro";

  /**
   * <p>
   * This String constant is used to separate single question/answer of chat
   * text output String in chatterbot application.
   * </p>
   */
  private final String SEPARATOR = "\n-------------------------\n";

  /**
   * <p>
   * This String constant represents a return value for bean methods that are
   * triggered by ui elements from view and whose operation has succeeded. This
   * return values is used for corresponding JSF navigation rule that is defined
   * in the web application's <tt>faces-config.xml</tt> configuration file.
   * </p>
   */
  private final String SUCCESS = "success";

  /**
   * <p>
   * This String constant represents a return value for bean methods that are
   * triggered by ui elements from view and whose operation has failed. This
   * return values is used for corresponding JSF navigation rule that is defined
   * in the web application's <tt>faces-config.xml</tt> configuration file.
   * </p>
   */
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
   *  This constructor is called by JSF Frame work and initializes ui elements
   *  of view by reading default values from <tt>faces-config.xml</tt>.
   *  Moreover this constructor is used to include all test applications of and
   *  to determine the location on harddisk where all files of this application
   *  are supposed to be stored.
   * </p>
   */
  public ChatterbotAdminBean()
  {
    // bean has been called from JSF framework
    log.debug("ChatterbotAdmin application has been started.");

    // initialize references to test applications
    bbCheck = new BBCheckImpl();
    qCheck = new QCheckImpl();
    ttCheck = new TTCheckImpl();
    chatterbot = new ChatterbotImpl();

    // initialize chat text objects
    chatterbotChatText = new String();
    chatterbotChatTextEN = new String();
    chatterbotChatTextDE = new String();
    chatterbotChatTextIT = new String();

    // test applications have been initialized
    log.debug("Test applications have been initialized.");

    // get faces context in order to retrieve
    // root path of this web application
    FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();
    ServletContext sc = (ServletContext) ec.getContext();

    // if root path is retrieved set path to specified
    // directory where all uploaded files and all files
    // that are suposed to be downloaded reside
    sharedFilesPath = sc.getRealPath(SHARED_FILES_DIR_NAME);

    // shared files path has been initialized
    log.debug("Shared files path has been initialized: " + sharedFilesPath);

    // application has been initialized
    log.debug("ChatterbotAdmin application has been initialized.");
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
      log.debug("File " + uploadedFile.getName() + " has been uploaded: "
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
   *  This method is used to provide content of a file for download.
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
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }

    // start upload
    uploadFile(topicTreeFile, topicTreeFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new topic tree file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewTopicTreeFile()
  {
    // enable new upload
    topicTreeFileIsUploaded = Boolean.FALSE;

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of any macro file.
   * </p>
   *
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }

    // start upload
    uploadFile(macrosFile, macrosFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new macros file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewMacrosFile()
  {
    // enable new upload
    macrosFileIsUploaded = Boolean.FALSE;

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of English macro file.
   * </p>
   *
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }

    // start upload
    uploadFile(macrosENFile, macrosENFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new English macros file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewMacrosENFile()
  {
    // enable new upload
    macrosENFileIsUploaded = Boolean.FALSE;

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of German macro file.
   * </p>
   *
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }

    // start upload
    uploadFile(macrosDEFile, macrosDEFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new German macros file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewMacrosDEFile()
  {
    // enable new upload
    macrosDEFileIsUploaded = Boolean.FALSE;

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of Italian macro file.
   * </p>
   *
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }

    // start upload
    uploadFile(macrosITFile, macrosITFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new Italian macros file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewMacrosITFile()
  {
    // enable new upload
    macrosITFileIsUploaded = Boolean.FALSE;

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of any text corpus file.
   * </p>
   *
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }

    // start upload
    uploadFile(textCorpusFile, textCorpusFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new text corpus file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewTextCorpusFile()
  {
    // enable new upload
    textCorpusFileIsUploaded = Boolean.FALSE;

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of English text corpus file.
   * </p>
   *
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }

    // start upload
    uploadFile(textCorpusENFile, textCorpusENFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new English text corpus file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewTextCorpusENFile()
  {
    // enable new upload
    textCorpusENFileIsUploaded = Boolean.FALSE;

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of German text corpus file.
   * </p>
   *
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }

    // start upload
    uploadFile(textCorpusDEFile, textCorpusDEFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new German text corpus file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewTextCorpusDEFile()
  {
    // enable new upload
    textCorpusDEFileIsUploaded = Boolean.FALSE;

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of Italian text corpus file.
   * </p>
   *
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }

    // start upload
    uploadFile(textCorpusITFile, textCorpusITFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new Italian text corpus file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewTextCorpusITFile()
  {
    // enable new upload
    textCorpusITFileIsUploaded = Boolean.FALSE;

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of rng file.
   * </p>
   *
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }

    // start upload
    uploadFile(rngFile, rngFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new rng file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewRngFile()
  {
    // enable new upload
    rngFileIsUploaded = Boolean.FALSE;

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to set appropriate file location and to trigger
   * file upload of test questions file.
   * </p>
   *
   * @return result string for JSF navigation rules
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

      // report exception to view
      return FAILED;
    }


    // start upload
    uploadFile(testQuestionsFile, testQuestionsFilename);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to enable the upload of a new test questions file.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String uploadNewTestQuestionsFile()
  {
    // enable new upload
    testQuestionsFileIsUploaded = Boolean.FALSE;

    // report success to view
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
    // check if language setting exits
    if (bbCheckLanguage == null || bbCheckLanguage.isEmpty())
    {
      // language setting does not exit
      log.error("Language setting does not exit for bbcheck.");

      // report failure to view
      return FAILED;
    }

    // check which language has been selected
    if (bbCheckLanguage.equals(EN))
    {
      // English language has been selected

      // enable upload of files that are related
      // to English language by setting appropriate
      // Boolean values for view
      bbCheckLanguageENSelected = Boolean.TRUE;
      bbCheckLanguageDESelected = Boolean.FALSE;
      bbCheckLanguageITSelected = Boolean.FALSE;
    }
    else if (bbCheckLanguage.equals(DE))
    {
      // German language has been selected

      // enable upload of files that are related
      // to German language by setting appropriate
      // Boolean values for view
      bbCheckLanguageENSelected = Boolean.FALSE;
      bbCheckLanguageDESelected = Boolean.TRUE;
      bbCheckLanguageITSelected = Boolean.FALSE;
    }
    else if (bbCheckLanguage.equals(IT))
    {
      // Italian language has been selected

      // enable upload of files that are related
      // to Italian language by setting appropriate
      // Boolean values for view
      bbCheckLanguageENSelected = Boolean.FALSE;
      bbCheckLanguageDESelected = Boolean.FALSE;
      bbCheckLanguageITSelected = Boolean.TRUE;
    }
    else
    {
      // no language has been selected
      log.error("No language has been selected");

      // disable test report for download
      bbCheckReportIsAvailable = Boolean.FALSE;

      // test report is not available for download
      log.debug("Test report is not available for download.");

      // report failure to view
      return FAILED;
    }

    // language for bbcheck has been selected
    log.debug("Language for bbcheck has been selected: " + bbCheckLanguage);

    // report success to vuiew
    return SUCCESS;
  }

  /**
   * <p>
   *  This method is used to enable/disable machine learning mode of test of
   *  bbCheck application. Therefore content of variable
   *  <tt>bbCheckLearningInput</tt> is evaluated:
   * </p>
   * <ul>
   *  <li><tt>yes</tt>: machine learning mode has been enabled</li>
   *  <li><tt>no</tt>: machine learning mode has been disabled</li>
   * </ul>
   *
   * @return result string for JSF navigation rules
   */
  public String selectBBCheckLearningMode()
  {
    // check if input string to enable/disable 
    // machine learning mode exists
    if (bbCheckLearningInput == null || bbCheckLearningInput.isEmpty())
    {
      // input string does not exist
      log.error("Input string to enable/disable machine learning mode"
              + " does not exist");

      // disable test report for download
      bbCheckReportIsAvailable = Boolean.FALSE;

      // test report is not available for download
      log.debug("Test report is not available for download.");

      // report failure to ui
      return FAILED;
    }

    // check input string to enable/disable machine learning mode
    if (bbCheckLearningInput.equals(BBCHECK_ENABLE_MACHINE_LEARNING_MODE))
    {
      // machine leaning mode is about to be enabled
      bbCheckLearningSelected = Boolean.TRUE;

      // machine learning mode has been enabled
      log.debug("Machine learning mode has been enabled: "
              + bbCheckLearningInput);
    }
    else if (bbCheckLearningInput.equals(BBCHECK_DISABLE_MACHINE_LEARNING_MODE))
    {
      // machine leaning is about to be disabled
      bbCheckLearningSelected = Boolean.FALSE;

      // machine learning mode has been disabled
      log.debug("Machine learning mode has been disabled: "
              + bbCheckLearningInput);
    }
    else
    {
      // failed to enable/disable machine leaning mode
      log.error("Failed to enable/disable machine leaning mode");

      // disable test report for download
      bbCheckReportIsAvailable = Boolean.FALSE;

      // test report is not available for download
      log.debug("Test report is not available for download.");

      // report failure to view
      return FAILED;
    }

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to trigger test of bbcheck application. Therefore
   * </p>
   * <ul>
   *  <li>language must have been set</li>
   *  <li>topic tree file must have been uploaded</li>
   *  <li>macros file must have been uploaded</li>
   *  <li>
   *   text corpus file must have been uploaded
   *   when machine learning mode is selected
   *  </li>
   *  <li>test questions file must have been uploaded</li>
   *  <li>shared files path must have been set</li>
   * </ul>
   * <p>
   * A test result is received after test of bbCheck application has finished.
   * </p>
   * 
   * @return result string for JSF navigation rules
   */
  public String performBBCheck()
  {
    // check if language has been set
    if (bbCheckLanguage == null || bbCheckLanguage.isEmpty())
    {
      // language has not been set
      log.error("Language has not been set for bbCheck.");

      // disable test report for download
      bbCheckReportIsAvailable = Boolean.FALSE;

      // test report is not available for download
      log.debug("Test report is not available for download.");

      // report failure to view
      return FAILED;
    }

    // check if file URL of topic tree file exists
    if (topicTreeFileURL == null)
    {
      // topic tree file does not exist
      log.error("Topic tree file does not exist for bbCheck.");

      // reset file settings for topic tree file
      topicTreeFile = null;
      topicTreeFileIsUploaded = Boolean.FALSE;
      topicTreeFilename = "";

      // settings for topic tree file have been reset
      log.debug("Settings for topic tree file have been reset.");

      // disable test report for download
      bbCheckReportIsAvailable = Boolean.FALSE;

      // test report is not available for download
      log.debug("Test report is not available for download.");

      // report failure to view
      return FAILED;
    }

    // check if file URL of test questions file exists
    if (testQuestionsFileURL == null)
    {
      // test questions file does not exist
      log.error("Test questions file does not exist for bbCheck.");

      // reset file settings for test questions file
      testQuestionsFile = null;
      testQuestionsFileIsUploaded = Boolean.FALSE;
      testQuestionsFilename = "";

      // settings for test questions file have been reset
      log.debug("Settings for test questions file have been reset.");

      // disable test report for download
      bbCheckReportIsAvailable = Boolean.FALSE;

      // test report is not available for download
      log.debug("Test report is not available for download.");

      // report failure to view
      return FAILED;
    }

    // check which language has been selected
    if (bbCheckLanguage.equals(EN))
    {
      // English language has been selected

      // check if file URL of macros file exists
      if (macrosENFileURL == null)
      {
        // macros file does not exist
        log.error("Macros file does not exist for bbCheck.");

        // reset file settings for macros file
        macrosENFile = null;
        macrosENFileIsUploaded = Boolean.FALSE;
        macrosENFilename = "";

        // settings for macros file have been reset
        log.debug("Settings for macros file (EN) have been reset.");

        // disable test report for download
        bbCheckReportIsAvailable = Boolean.FALSE;

        // test report is not available for download
        log.debug("Test report is not available for download.");

        // report failure to view
        return FAILED;
      }

      // check if machine learning mode has been selected
      if (bbCheckLearningSelected)
      {
        // machine learning mode has been selected

        // check if file URL of text corpus file exists
        if (textCorpusENFileURL == null)
        {
          // text corpus file does not exist
          log.error("Macros file does not exist for bbCheck.");

          // reset file settings for text corpus file
          textCorpusENFile = null;
          textCorpusENFileIsUploaded = Boolean.FALSE;
          textCorpusENFilename = "";

          // settings for text corpus file have been reset
          log.debug("Settings for text corpus file (EN) have been reset.");

          // disable test report for download
          bbCheckReportIsAvailable = Boolean.FALSE;

          // test report is not available for download
          log.debug("Test report is not available for download.");

          // report failure to view
          return FAILED;
        }

        // update settings to perform test of
        // bbcheck application for English language
        bbCheck.updateBBCheckSettings(
                bbCheckLanguage,
                topicTreeFileURL,
                macrosENFileURL, null, null,
                textCorpusENFileURL, null, null,
                testQuestionsFileURL,
                sharedFilesPath);
      }
      else
      {
        // machine learning mode has not been selected

        // update settings to perform test of
        // bbcheck application for English language
        bbCheck.updateBBCheckSettings(
                bbCheckLanguage,
                topicTreeFileURL,
                macrosENFileURL, null, null,
                null, null, null,
                testQuestionsFileURL,
                sharedFilesPath);
      }
    }
    else if (bbCheckLanguage.equals(DE))
    {
      // German language has been selected

      // check if file URL of macros file exists
      if (macrosDEFileURL == null)
      {
        // macros file does not exist
        log.error("Macros file does not exist for bbCheck.");

        // reset file settings for macros file
        macrosDEFile = null;
        macrosDEFileIsUploaded = Boolean.FALSE;
        macrosDEFilename = "";

        // settings for macros file have been reset
        log.debug("Settings for macros file (DE) have been reset.");

        // disable test report for download
        bbCheckReportIsAvailable = Boolean.FALSE;

        // test report is not available for download
        log.debug("Test report is not available for download.");

        // report failure to view
        return FAILED;
      }

      // check if machine learning mode is selected
      if (bbCheckLearningSelected)
      {
        // machine learning mode has been selected

        // check if file URL of text corpus file exists
        if (textCorpusDEFileURL == null)
        {
          // text corpus file does not exist
          log.error("Macros file does not exist for bbCheck.");

          // reset file settings for text corpus file
          textCorpusDEFile = null;
          textCorpusDEFileIsUploaded = Boolean.FALSE;
          textCorpusDEFilename = "";

          // settings for text corpus file have been reset
          log.debug("Settings for text corpus file (DE) have been reset.");

          // disable test report for download
          bbCheckReportIsAvailable = Boolean.FALSE;

          // test report is not available for download
          log.debug("Test report is not available for download.");

          // report failure to view
          return FAILED;
        }

        // update settings to perform test of
        // bbcheck application for German language
        bbCheck.updateBBCheckSettings(
                bbCheckLanguage,
                topicTreeFileURL,
                null, macrosDEFileURL, null,
                null, textCorpusDEFileURL, null,
                testQuestionsFileURL,
                sharedFilesPath);
      }
      else
      {
        // machine learning mode has not been selected

        // update settings to perform test of
        // bbcheck application for German language
        bbCheck.updateBBCheckSettings(
                bbCheckLanguage,
                topicTreeFileURL,
                null, macrosDEFileURL, null,
                null, null, null,
                testQuestionsFileURL,
                sharedFilesPath);
      }
    }
    else if (bbCheckLanguage.equals(IT))
    {
      // Italian language has been selected

      // check if file URL of macros file exists
      if (macrosITFileURL == null)
      {
        // macros file does not exist
        log.error("Macros file does not exist for bbCheck.");

        // reset file settings for macros file
        macrosITFile = null;
        macrosITFileIsUploaded = Boolean.FALSE;
        macrosITFilename = "";

        // settings for macros file have been reset
        log.debug("Settings for macros file (IT) have been reset.");

        // disable test report for download
        bbCheckReportIsAvailable = Boolean.FALSE;

        // test report is not available for download
        log.debug("Test report is not available for download.");

        // report failure to view
        return FAILED;
      }

      // check if machine learning mode is selected
      if (bbCheckLearningSelected)
      {
        // machine learning mode has been selected

        // check if file URL of text corpus file exists
        if (textCorpusITFileURL == null)
        {
          // text corpus file does not exist
          log.error("Macros file does not exist for bbCheck.");

          // reset file settings for text corpus file
          textCorpusITFile = null;
          textCorpusITFileIsUploaded = Boolean.FALSE;
          textCorpusITFilename = "";

          // settings for text corpus file have been reset
          log.debug("Settings for text corpus file (IT) have been reset.");

          // disable test report for download
          bbCheckReportIsAvailable = Boolean.FALSE;

          // test report is not available for download
          log.debug("Test report is not available for download.");

          // report failure to view
          return FAILED;
        }

        // update settings to perform test of
        // bbcheck application for German language
        bbCheck.updateBBCheckSettings(
                bbCheckLanguage,
                topicTreeFileURL,
                null, null, macrosITFileURL,
                null, null, textCorpusITFileURL,
                testQuestionsFileURL,
                sharedFilesPath);
      }
      else
      {
        // machine learning mode has not been selected

        // update settings to perform test of
        // bbcheck application for German language
        bbCheck.updateBBCheckSettings(
                bbCheckLanguage,
                topicTreeFileURL,
                null, null, macrosITFileURL,
                null, null, null,
                testQuestionsFileURL,
                sharedFilesPath);
      }
    }
    else
    {
      // no language has been selected
      log.error("No language has been selected.");

      // disable test report for download
      bbCheckReportIsAvailable = Boolean.FALSE;

      // test report is not available for download
      log.debug("Test report is not available for download.");

      // report filure to view
      return FAILED;
    }

    // settings of bbcheck test has been updated
    log.debug("Settings of bbcheck test has been updated for language: "
            + bbCheckLanguage);

    // perform bbcheck test and receive test results
    bbCheckResults = bbCheck.performBBCheck();

    // check if test results have been received
    if (bbCheckResults.isEmpty() || bbCheckResults == null)
    {
      // no test results for bbcheck test have been received
      log.warn("No test results for bbcheck test have been received.");

      // disable test report for download
      bbCheckReportIsAvailable = Boolean.FALSE;

      // test report is not available for download
      log.debug("Test report is not available for download.");

      // report failure to view
      return FAILED;
    }

    // test results for bbcheck have been received
    log.debug("Test results for bbcheck have been received: " + bbCheckResults);

    // enable test report for download
    bbCheckReportIsAvailable = Boolean.TRUE;

    // test report is now available for download
    log.debug("Test report is now available for download.");

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * </p>
   * @return result string for JSF navigation rules
   */
  public String downloadBBCheckReport()
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

    try
    {
      // get JSF context is used to access HttpServletResponse
      // objects from JSF context.
      FacesContext fc = FacesContext.getCurrentInstance();
      ExternalContext ec = fc.getExternalContext();

      // get HttpServletResponse object
      HttpServletResponse response = (HttpServletResponse) ec.getResponse();

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
      fc.responseComplete();

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

  /**
   * <p>
   * This method is used if
   * </p>
   * @return result string for JSF navigation rules
   */
  public String selectQCheckFormat()
  {
    // check which format has been selected
    if (qCheckFormat.equals(QCHECK_TT_FILE_FORMAT))
    {
      // topic tree file format has been selected
      log.debug("Topic tree file format has been selected for qcheck: "
              + qCheckFormat);
    }
    else if (qCheckFormat.equals(QCHECK_MACRO_FILE_FORMAT))
    {
      // macros file format has been selected
      log.debug("Macro file format has been selected for qcheck: "
              + qCheckFormat);
    }
    else
    {
      // no fromat has been selected
      log.error("No format has been selected for qcheck.");

      // report failure to view
      return FAILED;
    }

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to trigger test of qcheck application. Therefore
   * </p>
   * <ul>
   *  <li>macros file must have been uploaded</li>
   *  <li>regular expression must have been entered</li>
   *  <li>user question must have been entered</li>
   *  <li>format must have been set</li>
   * </ul>
   * <p>
   * A test result is received after test of qCheck application has finished.
   * </p>
   * 
   * @return result string for JSF navigation rules
   */
  public String performQCheck()
  {
    // check if macros file exists
    if (macrosFileURL == null)
    {
      // macros file does not exist
      log.error("Macros file does not exist");

      // reset file settings for topic tree file
      macrosFile = null;
      macrosFileIsUploaded = Boolean.FALSE;
      macrosFilename = "";

      // settings for macros file have been reset
      log.debug("Settings for topic tree file have been reset.");

      // report filure to view
      return FAILED;
    }

    // check if regular expression exists
    if (qCheckRegularExpression == null || qCheckRegularExpression.isEmpty())
    {
      // regular expression does not exist
      log.error("QCheck regular expression does not exist.");

      // report filure to view
      return FAILED;
    }

    // check if user question exists
    if (qCheckUserQuestion == null || qCheckUserQuestion.isEmpty())
    {
      // user question does not exist
      log.error("QCheck user question does not exist.");

      // report filure to view
      return FAILED;
    }

    // check if format exists
    if (qCheckFormat == null || qCheckFormat.isEmpty())
    {
      // format does not exist
      log.error("QCheck format does not exist.");

      // report filure to view
      return FAILED;
    }

    // perform qcheck test and receive test results
    qCheckResults = qCheck.performQCheck(macrosFilename, qCheckRegularExpression,
            qCheckUserQuestion, qCheckFormat);

    // check if test results have been received
    if (qCheckResults.isEmpty() || qCheckResults == null)
    {
      // no test results for qCheck have been received
      log.warn("No test results for qCheck have been received.");

      // report failure to view
      return FAILED;
    }

    // test results for qCheck have been received
    log.debug("Test results for qCheck have been received: "
            + qCheckResults);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to trigger test of ttcheck application. Therefore
   * </p>
   * <ul>
   *  <li>topic tree file must have been uploaded</li>
   *  <li>rng file must have been uploaded</li>
   *  <li>macros files EN, DE and IT must have been uploaded</li>
   * </ul>
   * <p>
   * A test result is received after test of qCheck application has finished.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String performTTCheck()
  {
    // check if filename of topic tree file exists
    if (topicTreeFileURL == null)
    {
      // topic tree file does not exist
      log.error("Topic tree file does not exist for qCheck.");

      // reset file settings for topic tree file
      topicTreeFile = null;
      topicTreeFileIsUploaded = Boolean.FALSE;
      topicTreeFileURL = null;

      // settings for topic tree file have been reset
      log.debug("Settings for topic tree file have been reset.");

      // report failure to view
      return FAILED;
    }

    // check if filename of rng file exists
    if (rngFileURL == null)
    {
      // rng file does not exist
      log.error("Topic tree file does not exist for qCheck.");

      // reset file settings for rng file
      rngFile = null;
      rngFileIsUploaded = Boolean.FALSE;
      rngFileURL = null;

      // settings for rng file have been reset
      log.debug("Settings for rng file have been reset.");

      // report failure to view
      return FAILED;
    }

    // check if filename of macros file exists
    if (macrosENFilename == null)
    {
      // macros filename does not exist
      log.error("Macros filename does not exist for ttCheck.");

      // reset file settings for macros file
      macrosENFile = null;
      macrosENFileIsUploaded = Boolean.FALSE;
      macrosENFileURL = null;

      // settings for macros file have been reset
      log.debug("Settings for macros file (EN) have been reset.");

      // report failure to view
      return FAILED;
    }

    // check if filename of macros file exists
    if (macrosDEFilename == null)
    {
      // macros filename does not exist
      log.error("Macros filename does not exist for ttCheck.");

      // reset file settings for macros file
      macrosDEFile = null;
      macrosDEFileIsUploaded = Boolean.FALSE;
      macrosDEFileURL = null;

      // settings for macros file have been reset
      log.debug("Settings for macros file (DE) have been reset.");

      // report failure to view
      return FAILED;
    }

    // check if filename of macros file exists
    if (macrosITFilename == null)
    {
      // macros filename does not exist
      log.error("Macros filename does not exist for ttCheck.");

      // reset file settings for macros file
      macrosITFile = null;
      macrosITFileIsUploaded = Boolean.FALSE;
      macrosITFileURL = null;

      // settings for macros file have been reset
      log.debug("Settings for macros file (IT) have been reset.");

      // report failure to view
      return FAILED;
    }

    // perform ttcheck test and receive test results
    ttCheckResults = ttCheck.performTTCheck(topicTreeFilename, rngFilename,
            macrosENFilename, macrosDEFilename, macrosITFilename);

    // check if test results have been received
    if (ttCheckResults.isEmpty() || ttCheckResults == null)
    {
      // no test results have been received
      log.warn("No test results for ttCheck received.");

      // report failure to view
      return FAILED;
    }

    // test results have been received
    log.debug("Test results for ttCheck have been received: "
            + ttCheckResults);

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to select language for chatterbot application. 
   * Upload of language related files and display of chat text depends on
   * current language selection.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String selectChatterbotLanguage()
  {
    // check if language setting exits
    if (chatterbotLanguage == null || chatterbotLanguage.isEmpty())
    {
      // language setting does not exit
      log.error("Language setting does not exit for chatterbot.");

      // report failure to view
      return FAILED;
    }

    // check which language has been selected
    if (chatterbotLanguage.equals(EN))
    {
      // language is set to English (default)

      // enable upload of files that are related
      // to English language by setting appropriate
      // Boolean values for view
      chatterbotLanguageENSelected = Boolean.TRUE;
      chatterbotLanguageDESelected = Boolean.FALSE;
      chatterbotLanguageITSelected = Boolean.FALSE;

      // update chat text
      chatterbotChatText = chatterbotChatTextEN;
    }
    else if (chatterbotLanguage.equals(DE))
    {
      // German language has been selected

      // enable upload of files that are related
      // to German language by setting appropriate
      // Boolean values for view
      chatterbotLanguageENSelected = Boolean.FALSE;
      chatterbotLanguageDESelected = Boolean.TRUE;
      chatterbotLanguageITSelected = Boolean.FALSE;

      // update chat text
      chatterbotChatText = chatterbotChatTextDE;
    }
    else if (chatterbotLanguage.equals(IT))
    {
      // language is set to Italian

      // enable upload of files that are related
      // to Italian language by setting appropriate
      // Boolean values for view
      chatterbotLanguageENSelected = Boolean.FALSE;
      chatterbotLanguageDESelected = Boolean.FALSE;
      chatterbotLanguageITSelected = Boolean.TRUE;

      // update chat text
      chatterbotChatText = chatterbotChatTextIT;
    }
    else
    {
      // no language has been selected
      log.error("No language has been selected");

      // report failure to view
      return FAILED;
    }

    // language for chatterbot has been selected
    log.debug("Language for chatterbot has been selected: "
            + chatterbotLanguage);

    // report success to vuiew
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to trigger test of chatterbot application by passing
   * a userquery to chatterbot application and receiving an answer as test
   * result.
   * </p>
   * <ul>
   *  <li>language must have been set</li>
   *  <li>topic tree file must have been uploaded</li>
   *  <li>macros file must have been uploaded</li>
   *  <li>text corpus file must have been uploaded</li>
   *  <li>test questions file must have been uploaded</li>
   * </ul>
   *
   * @return result string for JSF navigation rules
   */
  public String chat()
  {
    // check if language setting exits
    if (chatterbotLanguage == null || chatterbotLanguage.isEmpty())
    {
      // language setting does not exit
      log.error("Language setting does not exit for chatterbot.");

      // report failure to view
      return FAILED;
    }

    // check if user question exits
    if (chatterbotQuestion == null || chatterbotQuestion.isEmpty())
    {
      // user question does not exit
      log.error("User question does not exit for chatterbot.");

      // report failure to view
      return FAILED;
    }

    // check which language has been selected
    if (chatterbotLanguage.equals(EN))
    {
      // English language has been selected

      // update settings to perform test of
      // chatterbot application for English language
      chatterbot.updateChatterbotSettings(
              topicTreeFileURL,
              macrosENFileURL, null, null,
              textCorpusENFileURL, null, null);
    }
    else if (chatterbotLanguage.equals(DE))
    {
      // German language has been selected

      // update settings to perform test of
      // chatterbot application for German language
      chatterbot.updateChatterbotSettings(
              topicTreeFileURL,
              null, macrosDEFileURL, null,
              null, textCorpusDEFileURL, null);
    }
    else if (chatterbotLanguage.equals(IT))
    {
      // Italian language has been selected

      // update settings to perform test of
      // chatterbot application for Italian language
      chatterbot.updateChatterbotSettings(
              topicTreeFileURL,
              null, null, macrosITFileURL,
              null, null, textCorpusITFileURL);
    }
    else
    {
      // no language has been selected
      log.error("No language has been selected");

      // report failure to view
      return FAILED;
    }

    // chatterbot settings have been updated
    log.debug("Chatterbot settings have been updated for language: "
            + chatterbotLanguage);

    // receive chatterbot answer by sending
    // user question and set language
    chatterbotAnswer = chatterbot.getChatterbotAnswer(chatterbotQuestion,
            chatterbotLanguage);

    // check if answer has been received
    if (chatterbotAnswer == null || chatterbotAnswer.isEmpty())
    {
      // no answer has been received
      log.error("No answer has been received from Bob.");

      // report failure to view
      return FAILED;
    }

    // answer has been received
    log.debug("Bob's answer has been received: " + chatterbotAnswer);

    // check if English language has been selected
    if (chatterbotLanguage.equals(EN))
    {
      // English language has been selected

      // check if chat has just started
      if (!chatterbotChatTextEN.isEmpty())
      {
        // chat has not just started

        // prepend separator to English chat text
        chatterbotChatTextEN = SEPARATOR + chatterbotChatTextEN;
      }

      // compose English chat text
      chatterbotChatTextEN = "\nBob: " + chatterbotAnswer + chatterbotChatTextEN;
      chatterbotChatTextEN = "Du: " + chatterbotQuestion + chatterbotChatTextEN;

      // update chat text that is displayed in view
      // with English chat text
      chatterbotChatText = chatterbotChatTextEN;
    }

    // check if German language has been selected
    if (chatterbotLanguage.equals(DE))
    {
      // German language has been selected

      // check if chat has just started
      if (!chatterbotChatTextDE.isEmpty())
      {
        // chat has not just started

        // prepend separator to German chat text
        chatterbotChatTextDE = SEPARATOR + chatterbotChatTextDE;
      }

      // compose German chat text
      chatterbotChatTextDE = "\nBob: " + chatterbotAnswer + chatterbotChatTextDE;
      chatterbotChatTextDE = "Du: " + chatterbotQuestion + chatterbotChatTextDE;

      // update chat text that is displayed in view
      // with German chat text
      chatterbotChatText = chatterbotChatTextDE;
    }

    // check if Italian language has been selected
    if (chatterbotLanguage.equals(IT))
    {
      // Italian language has been selected

      // check if chat has just started
      if (!chatterbotChatTextIT.isEmpty())
      {
        // chat has not just started

        // prepend separator to Italian chat text
        chatterbotChatTextIT = SEPARATOR + chatterbotChatTextIT;
      }

      // compose Italian chat text
      chatterbotChatTextIT = "\nBob: " + chatterbotAnswer + chatterbotChatTextIT;
      chatterbotChatTextIT = "Du: " + chatterbotQuestion + chatterbotChatTextIT;

      // update chat text that is displayed in view
      // with German chat text
      chatterbotChatText = chatterbotChatTextIT;
    }

    // chat text has been updated
    log.debug("Chat text (" + chatterbotLanguage + ") has been updated: "
            + chatterbotChatText);

    // reset user question
    chatterbotQuestion = "";

    // user question has been reset
    log.debug("User question has been reset.");

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to select bbcheck application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String selectBBCheck()
  {
    // enable bbcheck application, disable other applications
    bbCheckSelected = Boolean.TRUE;
    qCheckSelected = Boolean.FALSE;
    ttCheckSelected = Boolean.FALSE;
    chatterbotSelected = Boolean.FALSE;

    // bbcheck application has been selected
    log.info("BBCheck application has been selected.");

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to select qcheck application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String selectQCheck()
  {
    // enable qcheck application, disable other applications
    bbCheckSelected = Boolean.FALSE;
    qCheckSelected = Boolean.TRUE;
    ttCheckSelected = Boolean.FALSE;
    chatterbotSelected = Boolean.FALSE;

    // qcheck application has been selected
    log.info("QCheck application has been selected.");

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to select ttcheck application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String selectTTCheck()
  {
    // enable qcheck application, disable other applications
    bbCheckSelected = Boolean.FALSE;
    qCheckSelected = Boolean.FALSE;
    ttCheckSelected = Boolean.TRUE;
    chatterbotSelected = Boolean.FALSE;

    // ttcheck application has been selected
    log.info("TTCheck application has been selected.");

    // report success to view
    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to select chatterbot application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   *
   * @return result string for JSF navigation rules
   */
  public String selectChatterbot()
  {
    // enable chatterbot application, disable other applications
    bbCheckSelected = Boolean.FALSE;
    qCheckSelected = Boolean.FALSE;
    ttCheckSelected = Boolean.FALSE;
    chatterbotSelected = Boolean.TRUE;

    // chatterbot application has been selected
    log.info("Chatterbot application has been selected.");

    // report success to view
    return SUCCESS;
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
   * @return the bbCheckReportIsAvailable
   */
  public Boolean getBbCheckReportIsAvailable()
  {
    return bbCheckReportIsAvailable;
  }

  /**
   * @param bbCheckReportIsAvailable the bbCheckReportIsAvailable to set
   */
  public void setBbCheckReportIsAvailable(Boolean bbCheckReportIsAvailable)
  {
    this.bbCheckReportIsAvailable = bbCheckReportIsAvailable;
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
   * @return the chatterbotChatText
   */
  public String getChatterbotChatText()
  {
    return chatterbotChatText;
  }

  /**
   * @param chatterbotChatText the chatterbotChatText to set
   */
  public void setChatterbotChatText(String chatterbotChatText)
  {
    this.chatterbotChatText = chatterbotChatText;
  }

  /**
   * @return the chatterbotChatTextEN
   */
  public String getChatterbotChatTextEN()
  {
    return chatterbotChatTextEN;
  }

  /**
   * @param chatterbotChatTextEN the chatterbotChatTextEN to set
   */
  public void setChatterbotChatTextEN(String chatterbotChatTextEN)
  {
    this.chatterbotChatTextEN = chatterbotChatTextEN;
  }

  /**
   * @return the chatterbotChatTextDE
   */
  public String getChatterbotChatTextDE()
  {
    return chatterbotChatTextDE;
  }

  /**
   * @param chatterbotChatTextDE the chatterbotChatTextDE to set
   */
  public void setChatterbotChatTextDE(String chatterbotChatTextDE)
  {
    this.chatterbotChatTextDE = chatterbotChatTextDE;
  }

  /**
   * @return the chatterbotChatTextIT
   */
  public String getChatterbotChatTextIT()
  {
    return chatterbotChatTextIT;
  }

  /**
   * @param chatterbotChatTextIT the chatterbotChatTextIT to set
   */
  public void setChatterbotChatTextIT(String chatterbotChatTextIT)
  {
    this.chatterbotChatTextIT = chatterbotChatTextIT;
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
   * @return the topicTreeFileURL
   */
  public URL getTopicTreeFileURL()
  {
    return topicTreeFileURL;
  }

  /**
   * @param topicTreeFileURL the topicTreeFileURL to set
   */
  public void setTopicTreeFileURL(URL topicTreeFileURL)
  {
    this.topicTreeFileURL = topicTreeFileURL;
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
   * @return the macrosFileURL
   */
  public URL getMacrosFileURL()
  {
    return macrosFileURL;
  }

  /**
   * @param macrosFileURL the macrosFileURL to set
   */
  public void setMacrosFileURL(URL macrosFileURL)
  {
    this.macrosFileURL = macrosFileURL;
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
   * @return the macrosENFileURL
   */
  public URL getMacrosENFileURL()
  {
    return macrosENFileURL;
  }

  /**
   * @param macrosENFileURL the macrosENFileURL to set
   */
  public void setMacrosENFileURL(URL macrosENFileURL)
  {
    this.macrosENFileURL = macrosENFileURL;
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
   * @return the macrosDEFileURL
   */
  public URL getMacrosDEFileURL()
  {
    return macrosDEFileURL;
  }

  /**
   * @param macrosDEFileURL the macrosDEFileURL to set
   */
  public void setMacrosDEFileURL(URL macrosDEFileURL)
  {
    this.macrosDEFileURL = macrosDEFileURL;
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
   * @return the macrosITFileURL
   */
  public URL getMacrosITFileURL()
  {
    return macrosITFileURL;
  }

  /**
   * @param macrosITFileURL the macrosITFileURL to set
   */
  public void setMacrosITFileURL(URL macrosITFileURL)
  {
    this.macrosITFileURL = macrosITFileURL;
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
   * @return the textCorpusFileURL
   */
  public URL getTextCorpusFileURL()
  {
    return textCorpusFileURL;
  }

  /**
   * @param textCorpusFileURL the textCorpusFileURL to set
   */
  public void setTextCorpusFileURL(URL textCorpusFileURL)
  {
    this.textCorpusFileURL = textCorpusFileURL;
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
   * @return the textCorpusENFileURL
   */
  public URL getTextCorpusENFileURL()
  {
    return textCorpusENFileURL;
  }

  /**
   * @param textCorpusENFileURL the textCorpusENFileURL to set
   */
  public void setTextCorpusENFileURL(URL textCorpusENFileURL)
  {
    this.textCorpusENFileURL = textCorpusENFileURL;
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
   * @return the textCorpusDEFileURL
   */
  public URL getTextCorpusDEFileURL()
  {
    return textCorpusDEFileURL;
  }

  /**
   * @param textCorpusDEFileURL the textCorpusDEFileURL to set
   */
  public void setTextCorpusDEFileURL(URL textCorpusDEFileURL)
  {
    this.textCorpusDEFileURL = textCorpusDEFileURL;
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
   * @return the textCorpusITFileURL
   */
  public URL getTextCorpusITFileURL()
  {
    return textCorpusITFileURL;
  }

  /**
   * @param textCorpusITFileURL the textCorpusITFileURL to set
   */
  public void setTextCorpusITFileURL(URL textCorpusITFileURL)
  {
    this.textCorpusITFileURL = textCorpusITFileURL;
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
   * @return the rngFileURL
   */
  public URL getRngFileURL()
  {
    return rngFileURL;
  }

  /**
   * @param rngFileURL the rngFileURL to set
   */
  public void setRngFileURL(URL rngFileURL)
  {
    this.rngFileURL = rngFileURL;
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
   * @return the testQuestionsFileURL
   */
  public URL getTestQuestionsFileURL()
  {
    return testQuestionsFileURL;
  }

  /**
   * @param testQuestionsFileURL the testQuestionsFileURL to set
   */
  public void setTestQuestionsFileURL(URL testQuestionsFileURL)
  {
    this.testQuestionsFileURL = testQuestionsFileURL;
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
}
