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
import java.net.URL;
import java.net.URLConnection;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 *
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public class ChatterbotAdminBean implements Serializable
{
  private BBCheck bbCheck;

  private QCheck qCheck;

  private TTCheck ttCheck;

  private Chatterbot chatterbot;

  private String bbCheckLanguage;

  private Boolean bbCheckLanguageENSelected;

  private Boolean bbCheckLanguageDESelected;

  private Boolean bbCheckLanguageITSelected;

  private String bbCheckLearningInput;

  private Boolean bbCheckLearningSelected;

  private String bbCheckResults;

  private String bbCheckReportFileURL;

  private String bbCheckReportFilename;

  private String bbCheckReportFileContentType;

  private String qCheckFormat;

  private String qCheckRegularExpression;

  private String qCheckUserQuestion;

  private String qCheckResults;

  private String ttCheckResults;

  private String chatterbotLanguage;

  private Boolean chatterbotLanguageENSelected;

  private Boolean chatterbotLanguageDESelected;

  private Boolean chatterbotLanguageITSelected;

  private String chatterbotAnswer;

  private String chatterbotQuestion;

  private UploadedFile topicTreeFile;

  private String topicTreeFilename;

  private UploadedFile macrosFile;

  private String macrosFilename;

  private UploadedFile macrosENFile;

  private String macrosENFilename;

  private UploadedFile macrosDEFile;

  private String macrosDEFilename;

  private UploadedFile macrosITFile;

  private String macrosITFilename;

  private UploadedFile textCorpusFile;

  private String textCorpusFilename;

  private UploadedFile textCorpusENFile;

  private String textCorpusENFilename;

  private UploadedFile textCorpusDEFile;

  private String textCorpusDEFilename;

  private UploadedFile textCorpusITFile;

  private String textCorpusITFilename;

  private UploadedFile rngFile;

  private String rngFilename;

  private UploadedFile testQuestionsFile;

  private String testQuestionsFilename;

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
   * This String object contains a return value any use case operation that
   * indicates if corresponding operation has succeeded. This return values is
   * used for JSF navigation rule that is defined in the web application's
   * <tt>faces-config.xml</tt> configuration file.
   * </p>
   */
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

  public ChatterbotAdminBean()
  {
    bbCheck = new BBCheckImpl();
    qCheck = new QCheckImpl();
    ttCheck = new TTCheckImpl();
    chatterbot = new ChatterbotImpl();

    FacesContext fcontext = FacesContext.getCurrentInstance();
    ServletContext scontext = (ServletContext) fcontext.getExternalContext().getContext();
    sharedFilesPath = scontext.getRealPath("/shared-files");

    log.debug("Shared files path is: " + sharedFilesPath);
  }

  private void uploadFile(UploadedFile uploadedFile, String file)
  {
    log.debug("Read file: " + uploadedFile.getName() + "(" + file + ")");

    try
    {
      InputStream streamIn = uploadedFile.getInputStream();
      long size = uploadedFile.getSize();
      byte[] buffer = new byte[(int) size];
      streamIn.read(buffer, 0, (int) size);

      File fileOut = new File(file);
      FileOutputStream streamOut = new FileOutputStream(fileOut);

      streamOut.write(buffer);

      streamIn.close();
      streamOut.close();

      log.debug("File has been uploaded: " + uploadedFile.getName());
    }
    catch (FileNotFoundException e)
    {
      log.error("Failed to upload file " + uploadedFile.getName()
              + ": " + e.getMessage());
    }
    catch (IOException e)
    {
      log.error("Failed to upload file " + uploadedFile.getName()
              + ": " + e.getMessage());
    }
  }

  private void downloadStream(String urlstr, ServletOutputStream outstr)
          throws IOException
  {
    try
    {
      BufferedInputStream bis = null;
      BufferedOutputStream bos = null;

      try
      {
        URL url = new URL(urlstr);
        URLConnection urlc = url.openConnection();
        int length = urlc.getContentLength();
        InputStream in = urlc.getInputStream();
        bis = new BufferedInputStream(in);
        bos = new BufferedOutputStream(outstr);
        byte[] buff = new byte[length];
        int bytesRead;

        while (-1 != (bytesRead = bis.read(buff, 0, buff.length)))
        {
          bos.write(buff, 0, bytesRead);
        }
      }
      catch (IOException e)
      {
        log.error("Failed to set download stream: " + e.getMessage());
        throw e;
      }
      finally
      {
        if (bis != null)
        {
          bis.close();
        }
        if (bos != null)
        {
          bos.close();
        }
        if (outstr != null)
        {
          outstr.flush();
          outstr.close();
        }
      }
    }
    catch (Exception e)
    {
      log.error("Failed to get download stream: " + e.getMessage());
    }
  }

  public void uploadTopicTreeFile()
  {
    topicTreeFilename = sharedFilesPath + "/" + topicTreeFile.getName();
    uploadFile(topicTreeFile, topicTreeFilename);
  }

  public void uploadMacrosFile()
  {
    macrosFilename = sharedFilesPath + "/" + macrosFile.getName();
    uploadFile(macrosFile, macrosFilename);
  }

  public void uploadMacrosENFile()
  {
    macrosENFilename = sharedFilesPath + "/" + macrosENFile.getName();
    uploadFile(macrosENFile, macrosENFilename);
  }

  public void uploadMacrosDEFile()
  {
    macrosDEFilename = sharedFilesPath + "/" + macrosDEFile.getName();
    uploadFile(macrosDEFile, macrosDEFilename);
  }

  public void uploadMacrosITFile()
  {
    macrosITFilename = sharedFilesPath + "/" + macrosITFile.getName();
    uploadFile(macrosITFile, macrosITFilename);
  }

  public void uploadTextCorpusFile()
  {
    textCorpusFilename = sharedFilesPath + "/" + textCorpusFile.getName();
    uploadFile(textCorpusFile, textCorpusFilename);
  }

  public void uploadTextCorpusENFile()
  {
    textCorpusENFilename = sharedFilesPath + "/" + textCorpusENFile.getName();
    uploadFile(textCorpusENFile, textCorpusENFilename);
  }

  public void uploadTextCorpusDEFile()
  {
    textCorpusDEFilename = sharedFilesPath + "/" + textCorpusDEFile.getName();
    uploadFile(textCorpusDEFile, textCorpusDEFilename);
  }

  public void uploadTextCorpusITFile()
  {
    textCorpusITFilename = sharedFilesPath + "/" + textCorpusITFile.getName();
    uploadFile(textCorpusITFile, textCorpusITFilename);
  }

  public void uploadRngFile()
  {
    rngFilename = sharedFilesPath + "/" + rngFile.getName();
    uploadFile(rngFile, rngFilename);
  }

  public void uploadTestQuestionsFile()
  {
    testQuestionsFilename = sharedFilesPath + "/" + testQuestionsFile.getName();
    uploadFile(testQuestionsFile, testQuestionsFilename);
  }

  public String selectBBCheckLanguage()
  {
    log.debug("BBCheck language set to: " + bbCheckLanguage);

    if (bbCheckLanguage.equals("DE"))
    {
      bbCheckLanguageENSelected = Boolean.FALSE;
      bbCheckLanguageDESelected = Boolean.TRUE;
      bbCheckLanguageITSelected = Boolean.FALSE;
    }
    else if (bbCheckLanguage.equals("IT"))
    {
      bbCheckLanguageENSelected = Boolean.FALSE;
      bbCheckLanguageDESelected = Boolean.FALSE;
      bbCheckLanguageITSelected = Boolean.TRUE;
    }
    else
    {
      bbCheckLanguageENSelected = Boolean.TRUE;
      bbCheckLanguageDESelected = Boolean.FALSE;
      bbCheckLanguageITSelected = Boolean.FALSE;
    }

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
      bbCheckReportFilename = ""; //bbCheckReportFile.getName();

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

  public String selectChatterbotLanguage()
  {
    log.debug("Chatterbot language set to: " + chatterbotLanguage);

    if (chatterbotLanguage.equals("DE"))
    {
      chatterbotLanguageENSelected = Boolean.FALSE;
      chatterbotLanguageDESelected = Boolean.TRUE;
      chatterbotLanguageITSelected = Boolean.FALSE;
    }
    else if (chatterbotLanguage.equals("IT"))
    {
      chatterbotLanguageENSelected = Boolean.FALSE;
      chatterbotLanguageDESelected = Boolean.FALSE;
      chatterbotLanguageITSelected = Boolean.TRUE;
    }
    else
    {
      chatterbotLanguageENSelected = Boolean.TRUE;
      chatterbotLanguageDESelected = Boolean.FALSE;
      chatterbotLanguageITSelected = Boolean.FALSE;
    }

    return SUCCESS;
  }

  public String chat()
  {
    log.debug("Perform chatterbot.");

    chatterbotAnswer = chatterbot.chat(chatterbotQuestion, chatterbotLanguage,
            topicTreeFilename, macrosENFilename, macrosDEFilename,
            macrosITFilename, textCorpusENFilename, textCorpusDEFilename,
            textCorpusITFilename);

    if (chatterbotAnswer.isEmpty() || chatterbotAnswer == null)
    {
      log.warn("No answer received.");

      chatterbotAnswer = "Error";

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
  public void setBbCheckReportFileContentType(String bbCheckReportFileContentType)
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
  public void setChatterbotLanguageENSelected(Boolean chatterbotLanguageENSelected)
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
  public void setChatterbotLanguageDESelected(Boolean chatterbotLanguageDESelected)
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
  public void setChatterbotLanguageITSelected(Boolean chatterbotLanguageITSelected)
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
