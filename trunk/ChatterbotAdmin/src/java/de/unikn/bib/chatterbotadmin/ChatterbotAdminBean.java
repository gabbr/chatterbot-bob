package de.unikn.bib.chatterbotadmin;

import it.unibz.lib.bob.bbCheck.bbCheck;
import it.unibz.lib.bob.bbCheck.bbCheckImpl;
import it.unibz.lib.bob.chatterbot.Chatterbot;
import it.unibz.lib.bob.chatterbot.ChatterbotImpl;
import it.unibz.lib.bob.macroparser.Macroparser;
import it.unibz.lib.bob.macroparser.MacroparserImpl;
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
  private bbCheck bbCheck;

  private String bbCheckLanguage;

  private String bbCheckLearningInput;

  private Boolean bbCheckLearningSelected;

  private String bbCheckResults;

  private String bbCheckReportFileURL;

  private String bbCheckReportFilename;

  private String bbCheckReportFileContentType;

  private QCheck qCheck;

  private String qCheckFormat;

  private String qCheckRegularExpression;

  private String qCheckUserQuestion;

  private String qCheckResults;

  private TTCheck ttCheck;

  private String ttCheckResults;

  private Chatterbot chatterbot;

  private String chatterbotLanguage;

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

  private String rngFileName;

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
   * This Boolean object indicates if bcheck application is selected.
   * </p>
   */
  private Boolean bbCheckSelected;

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
    bbCheck = new bbCheckImpl();
    qCheck = new QCheckImpl();
    ttCheck = new TTCheckImpl();
    macroparser = new MacroparserImpl();
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

  public void uploadTopicTreeFile(UploadedFile topicTreeFile)
  {
    topicTreeFilename = sharedFilesPath + "/" + topicTreeFile.getName();
    uploadFile(topicTreeFile, topicTreeFilename);
  }

  public void uploadMacrosFile(UploadedFile macrosFile)
  {
    macrosFilename = sharedFilesPath + "/" + macrosFile.getName();
    uploadFile(macrosFile, macrosFilename);
  }

  public void uploadMacrosENFile(UploadedFile macrosENFile)
  {
    macrosENFilename = sharedFilesPath + "/" + macrosENFile.getName();
    uploadFile(macrosENFile, macrosENFilename);
  }

  public void uploadMacrosDEFile(UploadedFile macrosDEFile)
  {
    macrosDEFilename = sharedFilesPath + "/" + macrosDEFile.getName();
    uploadFile(macrosDEFile, macrosDEFilename);
  }

  public void uploadMacrosITFile(UploadedFile macrosITFile)
  {
    macrosITFilename = sharedFilesPath + "/" + macrosITFile.getName();
    uploadFile(macrosITFile, macrosITFilename);
  }

  public void uploadTextCorpusFile(UploadedFile textCorpusFile)
  {
    textCorpusFilename = sharedFilesPath + "/" + textCorpusFile.getName();
    uploadFile(textCorpusFile, textCorpusFilename);
  }

  public void uploadTextCorpusENFile(UploadedFile textCorpusENFile)
  {
    textCorpusENFilename = sharedFilesPath + "/" + textCorpusENFile.getName();
    uploadFile(textCorpusENFile, textCorpusENFilename);
  }

  public void uploadTextCorpusDEFile(UploadedFile textCorpusDEFile)
  {
    textCorpusDEFilename = sharedFilesPath + "/" + textCorpusDEFile.getName();
    uploadFile(textCorpusDEFile, textCorpusDEFilename);
  }

  public void uploadTextCorpusITFile(UploadedFile textCorpusITFile)
  {
    textCorpusITFilename = sharedFilesPath + "/" + textCorpusITFile.getName();
    uploadFile(textCorpusITFile, textCorpusITFilename);
  }

  public void uploadRngFile(UploadedFile rngFile)
  {
    rngFilename = sharedFilesPath + "/" + rngFile.getName();
    uploadFile(rngFile, rngFilename);
  }

  public void uploadTestQuestionsFile(UploadedFile testQuestionsFile)
  {
    testQuestionsFilename = sharedFilesPath + "/" + testQuestionsFile.getName();
    uploadFile(testQuestionsFile, testQuestionsFilename);
  }

  public String performbbCheck(String language)
  {
    return bbCheck.performbbCheck(testQuestionsFilename, topicTreeFilename,
            macrosENFilename, macrosDEFilename, macrosITFilename,
            textCorpusENFilename, textCorpusDEFilename, textCorpusITFilename,
            language, Boolean.FALSE, sharedFilesPath);
  }

  public String getbbCheckTestReportFile()
  {
    return bbCheck.getbbCheckTestReportFile();
  }

  public String performQCheck(String regularExpression, String userQuestion,
          String format)
  {
    return qCheck.performQCheck(macrosFilename, regularExpression, userQuestion,
            format);
  }

  public String performTTCheck()
  {
    return ttCheck.performTTCheck(topicTreeFilename, rngFilename,
            macrosDEFilename, macrosENFilename, macrosITFilename);
  }

  public String chat(String question, String language)
  {
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
    log.debug("Machine learning mode: " + getbbCheckLearningInput());

    if (getbbCheckLearningInput() != null)
    {
      if (getbbCheckLearningInput().equals("yes"))
      {
        setbbCheckLearningSelected(Boolean.TRUE);

        log.debug("Machine learning mode has been enabled: "
                + getbbCheckLearningInput());
      }
      else
      {
        setbbCheckLearningSelected(Boolean.FALSE);

        log.debug("Machine learning mode has been disabled: "
                + getbbCheckLearningInput());
      }
    }
    else
    {
      setbbCheckLearningInput("no");
      setbbCheckLearningSelected(Boolean.FALSE);

      log.debug("Machine learning mode has been disabled: "
              + getbbCheckLearningInput());
    }

    return SUCCESS;
  }

  public String performbbCheck()
  {
    log.debug("Perform bcheck.");

    setBbResults(new String());

    setBbResults(chatterbotAdmin.performbbCheck(getbbCheckLanguage()));

    if (getBbResults().isEmpty() || getBbResults() == null)
    {
      log.warn("No test results for bcheck received.");

      return FAILED;
    }

    log.debug("Test results for bcheck received.");

    return SUCCESS;
  }

  public String downloadbbCheckReport()
  {

    try
    {
      // set content type
      bbReportFileContentType = "application/octet-stream";

      // content type set
      log.debug("Content type set: " + bbReportFileContentType);

      // read filename of test report file from manager object
      bbReportFilename = chatterbotAdmin.getbbCheckTestReportFile();

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
      HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

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

    chatterbotAnswer = chatterbot.chat(chatterbotQuestion, chatterbotLanguage,
            topicTreeFilename, macrosENFilename, macrosDEFilename,
            macrosITFilename, textCorpusENFilename, textCorpusDEFilename,
            textCorpusITFilename);

    if (chatterbotAnswer.isEmpty() || chatterbotAnswer == null)
    {
      log.warn("No answer received.");

      setChatterbotAnswer("Error");

      return FAILED;
    }

    setChatterbotAnswer(answer);

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
    setbbCheckSelected(Boolean.TRUE);
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
    setbbCheckSelected(Boolean.FALSE);
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
    setbbCheckSelected(Boolean.FALSE);
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
    setbbCheckSelected(Boolean.FALSE);
    setQcheckSelected(Boolean.FALSE);
    setTtcheckSelected(Boolean.FALSE);
    setChatterbotSelected(Boolean.TRUE);

    // chatterbot application has been selected
    log.info("Chatterbot application has been selected.");
  }

  
}
