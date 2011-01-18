package de.unikn.bib.chatterbotadmin.jsf;

import de.unikn.bib.chatterbotadmin.ChatterbotAdmin;
import de.unikn.bib.chatterbotadmin.ChatterbotAdminImpl;

// Java SE context
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

// logging context
import java.net.URL;
import java.net.URLConnection;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

// myfaces context
import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 *
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public class ChatterbotAdminBean implements Serializable
{
  private String bbcheckLanguage;

  private String bbcheckLearningInput;

  private Boolean bbcheckLearningSelected;

  private String bbResults;

  private String bbReportFileURL;

  private String bbReportFilename;

  private String bbReportFileContentType;

  private String qFormat;

  private String qcheckRegularExpression;

  private String qcheckUserQuestion;

  private String qResults;

  private String ttResults;

  private String chatterbotLanguage;

  private String chatterbotAnswer;

  private String chatterbotQuestion;

  private UploadedFile topicTreeFile;

  private UploadedFile macrosFile;

  private UploadedFile macrosENFile;

  private UploadedFile macrosDEFile;

  private UploadedFile macrosITFile;

  private UploadedFile textCorpusFile;

  private UploadedFile textCorpusENFile;

  private UploadedFile textCorpusDEFile;

  private UploadedFile textCorpusITFile;

  private UploadedFile rngFile;

  private UploadedFile testQuestionsFile;

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
   * This Boolean object indicates if bcheck application is selected.
   * </p>
   */
  private Boolean bbcheckSelected;

  /**
   * <p>
   * This Boolean object indicates if qcheck application is selected.
   * </p>
   */
  private Boolean qcheckSelected;

  /**
   * <p>
   * This Boolean object indicates if ttcheck application is selected.
   * </p>
   */
  private Boolean ttcheckSelected;

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
  private final String SUCCESS = "success";

  /**
   * <p>
   * This String object contains a return value any use case operation that
   * indicates if corresponding operation has succeeded. This return values is
   * used for JSF navigation rule that is defined in the web application's
   * <tt>faces-config.xml</tt> configuration file.
   * </p>
   */
  private final String FAILED = "failed";

  private ChatterbotAdmin chatterbotAdmin;

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
    FacesContext fcontext = FacesContext.getCurrentInstance();
    ServletContext scontext = (ServletContext) fcontext.getExternalContext().getContext();
    sharedFilesPath = scontext.getRealPath("/shared-files");

    log.debug("Shared files path is: " + sharedFilesPath);

    chatterbotAdmin = new ChatterbotAdminImpl(sharedFilesPath);
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

  public String load()
  {
    log.debug("load");

    return SUCCESS;
  }

  public String uploadTopicTreeFile()
  {
    log.debug("test");
    chatterbotAdmin.uploadTopicTreeFile(getTopicTreeFile());
    log.debug("Upload of file " + getTopicTreeFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadMacrosFile()
  {
    chatterbotAdmin.uploadMacrosFile(getMacrosFile());
    log.debug("Upload of file " + getMacrosFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadMacrosENFile()
  {
    chatterbotAdmin.uploadMacrosENFile(getMacrosENFile());
    log.debug("Upload of file " + getMacrosENFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadMacrosDEFile()
  {
    chatterbotAdmin.uploadMacrosDEFile(getMacrosDEFile());
    log.debug("Upload of file " + getMacrosDEFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadMacrosITFile()
  {
    chatterbotAdmin.uploadMacrosITFile(getMacrosITFile());
    log.debug("Upload of file " + getMacrosITFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadTextCorpusFile()
  {
    chatterbotAdmin.uploadTextCorpusFile(getTextCorpusFile());
    log.debug("Upload of file " + getTextCorpusFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadTextCorpusENFile()
  {
    chatterbotAdmin.uploadTextCorpusENFile(getTextCorpusENFile());
    log.debug("Upload of file " + getTextCorpusENFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadTextCorpusDEFile()
  {
    chatterbotAdmin.uploadTextCorpusDEFile(getTextCorpusDEFile());
    log.debug("Upload of file " + getTextCorpusDEFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadTextCorpusITFile()
  {
    chatterbotAdmin.uploadTextCorpusITFile(getTextCorpusITFile());
    log.debug("Upload of file " + getTextCorpusITFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadRngFile()
  {
    chatterbotAdmin.uploadRngFile(getRngFile());
    log.debug("Upload of file " + getRngFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadTestQuestionsFile()
  {
    chatterbotAdmin.uploadTestQuestionsFile(getTestQuestionsFile());
    log.debug("Upload of file " + getTestQuestionsFile().getName() + " has succeeded.");

    return SUCCESS;
  }

  public String selectBcheckLearning()
  {
    log.debug("Machine learning mode: " + getBbcheckLearningInput());

    if (getBbcheckLearningInput() != null)
    {
      if (getBbcheckLearningInput().equals("yes"))
      {
        setBbcheckLearningSelected(Boolean.TRUE);

        log.debug("Machine learning mode has been enabled: "
                + getBbcheckLearningInput());
      }
      else
      {
        setBbcheckLearningSelected(Boolean.FALSE);

        log.debug("Machine learning mode has been disabled: "
                + getBbcheckLearningInput());
      }
    }
    else
    {
      setBbcheckLearningInput("no");
      setBbcheckLearningSelected(Boolean.FALSE);

      log.debug("Machine learning mode has been disabled: "
              + getBbcheckLearningInput());
    }

    return SUCCESS;
  }

  public String performBBCheck()
  {
    log.debug("Perform bcheck.");

    setBbResults(new String());

    setBbResults(chatterbotAdmin.performBBCheck(getBbcheckLanguage()));

    if (getBbResults().isEmpty() || getBbResults() == null)
    {
      log.warn("No test results for bcheck received.");

      return FAILED;
    }

    log.debug("Test results for bcheck received.");

    return SUCCESS;
  }

  public String downloadBBCheckReport()
  {

    try
    {
      // set content type
      bbReportFileContentType = "application/octet-stream";

      // content type set
      log.debug("Content type set: " + bbReportFileContentType);

      // read filename of test report file from manager object
      bbReportFilename = chatterbotAdmin.getBBCheckTestReportFile();

      // filename of test report file read from manager object
      log.debug("Filename of test report file read from manager "
              + "object: " + bbReportFilename);

      // create URL
      bbReportFileURL = "file://" + sharedFilesPath + "/" + bbReportFilename;

      // url of test report file created
      log.debug("URL of test report file created: " + bbReportFileURL);

      // get JSF context is used to access HttpServlerResponse
      // objects from Java EE context.
      FacesContext facesContext = FacesContext.getCurrentInstance();

      // get HttpServletResponse object to add file name to the header of HTTP
      // response.
      HttpServletResponse response = (HttpServletResponse)
              facesContext.getExternalContext().getResponse();

      // set content type and add filename of vpn profile
      // to header of HTTP response
      response.setContentType(bbReportFileContentType);
      response.setHeader("Content-Disposition", "attachment;filename=\""
              + bbReportFilename + "\"");

      // get ServletOutputStream object to flush file content for download
      ServletOutputStream outputStream = response.getOutputStream();

      // flush content of certification chain by retrieving file
      // specified URL from
      downloadStream(bbReportFileURL, outputStream);
      outputStream.flush();

      // complete response
      facesContext.responseComplete();

      // certificate chain has been downloaded
      log.info("Certificate chain has been downloaded.");

      // report success for JSF navigation rules
      return SUCCESS;
    }
    catch (IOException e)
    {
      // download of VPN profile has failed
      log.error("Download of VPN profile has failed: " + e.getMessage());

      // report exception for JSF navigation rules
      return FAILED;
    }
  }

  public String performQCheck()
  {
    log.debug("Perform qcheck.");

    setqResults(new String());

    setqResults(chatterbotAdmin.performQCheck(qcheckRegularExpression,
            qcheckUserQuestion, qFormat));

    if (getqResults().isEmpty() || getqResults() == null)
    {
      log.warn("No test results for qcheck received.");

      return FAILED;
    }

    log.debug("Test results for qcheck received.");

    return SUCCESS;
  }

  public String performTTCheck()
  {
    log.debug("Perform ttcheck.");

    setTtResults(new String());

    setTtResults(chatterbotAdmin.performTTCheck());

    if (getTtResults().isEmpty() || getTtResults() == null)
    {
      log.warn("No test results for ttcheck received.");

      return FAILED;
    }

    log.debug("Test results for ttcheck received.");

    return SUCCESS;
  }

  public String chat()
  {
    log.debug("Perform chatterbot.");

    setChatterbotAnswer(new String());

    setChatterbotAnswer(chatterbotAdmin.chat(getChatterbotQuestion(), getChatterbotLanguage()));

    if (getChatterbotAnswer().isEmpty() || getChatterbotAnswer() == null)
    {
      log.warn("No answer received.");

      setChatterbotAnswer("Error");

      return FAILED;
    }

    log.debug("Bob's answer has been received.");

    return SUCCESS;
  }

  /**
   * <p>
   * This method is used to select bcheck application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>chatterbotadmin.jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   */
  public void selectBCheck()
  {
    // enable bcheck application, disable other applications
    setBbcheckSelected(Boolean.TRUE);
    setQcheckSelected(Boolean.FALSE);
    setTtcheckSelected(Boolean.FALSE);
    setChatterbotSelected(Boolean.FALSE);

    // bcheck application has been selected
    log.info("BCheck application has been selected.");
  }

  /**
   * <p>
   * This method is used to select qcheck application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>chatterbotadmin.jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   */
  public void selectQCheck()
  {
    // enable qcheck application, disable other applications
    setBbcheckSelected(Boolean.FALSE);
    setQcheckSelected(Boolean.TRUE);
    setTtcheckSelected(Boolean.FALSE);
    setChatterbotSelected(Boolean.FALSE);

    // qcheck application has been selected
    log.info("QCheck application has been selected.");
  }

  /**
   * <p>
   * This method is used to select ttcheck application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>chatterbotadmin.jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   */
  public void selectTTCheck()
  {
    // enable qcheck application, disable other applications
    setBbcheckSelected(Boolean.FALSE);
    setQcheckSelected(Boolean.FALSE);
    setTtcheckSelected(Boolean.TRUE);
    setChatterbotSelected(Boolean.FALSE);

    // ttcheck application has been selected
    log.info("TTCheck application has been selected.");
  }

  /**
   * <p>
   * This method is used to select chatterbot application by setting the
   * corresponding Boolean value that determines which Java Server Page
   * has to become included in <tt>chatterbotadmin.jsp<tt>. This method
   * is invoked from <tt>menu.jsp</tt>.
   * </p>
   */
  public void selectChatterbot()
  {
    // enable chatterbot application, disable other applications
    setBbcheckSelected(Boolean.FALSE);
    setQcheckSelected(Boolean.FALSE);
    setTtcheckSelected(Boolean.FALSE);
    setChatterbotSelected(Boolean.TRUE);

    // chatterbot application has been selected
    log.info("Chatterbot application has been selected.");
  }

  /**
   * @return the bbcheckLanguage
   */
  public String getBbcheckLanguage()
  {
    return bbcheckLanguage;
  }

  /**
   * @param bbcheckLanguage the bbcheckLanguage to set
   */
  public void setBbcheckLanguage(String bbcheckLanguage)
  {
    this.bbcheckLanguage = bbcheckLanguage;
  }

  /**
   * @return the bbcheckLearningInput
   */
  public String getBbcheckLearningInput()
  {
    return bbcheckLearningInput;
  }

  /**
   * @param bbcheckLearningInput the bbcheckLearningInput to set
   */
  public void setBbcheckLearningInput(String bbcheckLearningInput)
  {
    this.bbcheckLearningInput = bbcheckLearningInput;
  }

  /**
   * @return the bbcheckLearningSelected
   */
  public Boolean getBbcheckLearningSelected()
  {
    return bbcheckLearningSelected;
  }

  /**
   * @param bbcheckLearningSelected the bbcheckLearningSelected to set
   */
  public void setBbcheckLearningSelected(Boolean bbcheckLearningSelected)
  {
    this.bbcheckLearningSelected = bbcheckLearningSelected;
  }

  /**
   * @return the bbResults
   */
  public String getBbResults()
  {
    return bbResults;
  }

  /**
   * @param bbResults the bbResults to set
   */
  public void setBbResults(String bbResults)
  {
    this.bbResults = bbResults;
  }

  /**
   * @return the qFormat
   */
  public String getqFormat()
  {
    return qFormat;
  }

  /**
   * @param qFormat the qFormat to set
   */
  public void setqFormat(String qFormat)
  {
    this.qFormat = qFormat;
  }

  public String getQcheckRegularExpression()
  {
    return qcheckRegularExpression;
  }

  public void setQcheckRegularExpression(String qcheckRegularExpression)
  {
    this.qcheckRegularExpression = qcheckRegularExpression;
  }

  public String getQcheckUserQuestion()
  {
    return qcheckUserQuestion;
  }

  public void setQcheckUserQuestion(String qcheckUserQuestion)
  {
    this.qcheckUserQuestion = qcheckUserQuestion;
  }

  

  /**
   * @return the ttResults
   */
  public String getTtResults()
  {
    return ttResults;
  }

  /**
   * @param ttResults the ttResults to set
   */
  public void setTtResults(String ttResults)
  {
    this.ttResults = ttResults;
  }

  public String getChatterbotLanguage()
  {
    return chatterbotLanguage;
  }

  public void setChatterbotLanguage(String chatterbotLanguage)
  {
    this.chatterbotLanguage = chatterbotLanguage;
  }

  public String getChatterbotAnswer()
  {
    return chatterbotAnswer;
  }

  public void setChatterbotAnswer(String chatterbotAnswer)
  {
    this.chatterbotAnswer = chatterbotAnswer;
  }

  public String getChatterbotQuestion()
  {
    return chatterbotQuestion;
  }

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
   * @return the bbcheckSelected
   */
  public Boolean getBbcheckSelected()
  {
    return bbcheckSelected;
  }

  /**
   * @param bbcheckSelected the bbcheckSelected to set
   */
  public void setBbcheckSelected(Boolean bbcheckSelected)
  {
    this.bbcheckSelected = bbcheckSelected;
  }

  /**
   * @return the qcheckSelected
   */
  public Boolean getQcheckSelected()
  {
    return qcheckSelected;
  }

  /**
   * @param qcheckSelected the qcheckSelected to set
   */
  public void setQcheckSelected(Boolean qcheckSelected)
  {
    this.qcheckSelected = qcheckSelected;
  }

  /**
   * @return the ttcheckSelected
   */
  public Boolean getTtcheckSelected()
  {
    return ttcheckSelected;
  }

  /**
   * @param ttcheckSelected the ttcheckSelected to set
   */
  public void setTtcheckSelected(Boolean ttcheckSelected)
  {
    this.ttcheckSelected = ttcheckSelected;
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
   * @return the qResults
   */
  public String getqResults()
  {
    return qResults;
  }

  /**
   * @param qResults the qResults to set
   */
  public void setqResults(String qResults)
  {
    this.qResults = qResults;
  }
}
