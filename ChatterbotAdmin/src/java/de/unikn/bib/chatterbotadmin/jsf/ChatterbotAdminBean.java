package de.unikn.bib.chatterbotadmin.jsf;

import de.unikn.bib.chatterbotadmin.ChatterbotAdmin;
import de.unikn.bib.chatterbotadmin.ChatterbotAdminImpl;

// Java SE context
import java.io.Serializable;

// logging context
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
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
  private String text;

  private String bcheckLanguage;

  private String bcheckLearningInput;

  private Boolean bcheckLearningSelected;

  private String bResults;

  private String qFormat;

  private String ttResults;

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
  private Boolean bcheckSelected;

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
    String path = scontext.getRealPath("/shared-files");

    log.debug("Shared files path is: " + path);

    chatterbotAdmin = new ChatterbotAdminImpl(path);
  }

  public String load()
  {
    log.debug("load");

    return SUCCESS;
  }

  public String uploadTopicTreeFile()
  {
    log.debug("test");
    chatterbotAdmin.uploadTopicTreeFile(topicTreeFile);
    log.debug("Upload of file " + topicTreeFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadMacrosFile()
  {
    chatterbotAdmin.uploadMacrosFile(macrosFile);
    log.debug("Upload of file " + macrosFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadMacrosENFile()
  {
    chatterbotAdmin.uploadMacrosENFile(macrosENFile);
    log.debug("Upload of file " + macrosENFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadMacrosDEFile()
  {
    chatterbotAdmin.uploadMacrosDEFile(macrosDEFile);
    log.debug("Upload of file " + macrosDEFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadMacrosITFile()
  {
    chatterbotAdmin.uploadMacrosITFile(macrosITFile);
    log.debug("Upload of file " + macrosITFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadTextCorpusFile()
  {
    chatterbotAdmin.uploadTextCorpusFile(textCorpusFile);
    log.debug("Upload of file " + textCorpusFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadTextCorpusENFile()
  {
    chatterbotAdmin.uploadTextCorpusENFile(textCorpusENFile);
    log.debug("Upload of file " + textCorpusENFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadTextCorpusDEFile()
  {
    chatterbotAdmin.uploadTextCorpusDEFile(textCorpusDEFile);
    log.debug("Upload of file " + textCorpusDEFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadTextCorpusITFile()
  {
    chatterbotAdmin.uploadTextCorpusITFile(textCorpusITFile);
    log.debug("Upload of file " + textCorpusITFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadRngFile()
  {
    chatterbotAdmin.uploadRngFile(rngFile);
    log.debug("Upload of file " + rngFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String uploadTestQuestionsFile()
  {
    chatterbotAdmin.uploadTestQuestionsFile(testQuestionsFile);
    log.debug("Upload of file " + testQuestionsFile.getName() + " has succeeded.");

    return SUCCESS;
  }

  public String selectBcheckLearning()
  {
    log.debug("Machine learning mode: " + bcheckLearningInput);

    if (bcheckLearningInput != null)
    {
      if (bcheckLearningInput.equals("yes"))
      {
        setBcheckLearningSelected(Boolean.TRUE);

        log.debug("Machine learning mode has been enabled: "
                + bcheckLearningInput);
      }
      else
      {
        setBcheckLearningSelected(Boolean.FALSE);

        log.debug("Machine learning mode has been disabled: "
                + bcheckLearningInput);
      }
    }
    else
    {
      bcheckLearningInput = "no";
      setBcheckLearningSelected(Boolean.FALSE);

      log.debug("Machine learning mode has been disabled: "
              + bcheckLearningInput);
    }

    return SUCCESS;
  }

  public String performBCheck()
  {
    log.debug("Perform bcheck.");

    bResults = new String();

    bResults = chatterbotAdmin.performBCheck(bcheckLanguage);

    if (bResults.isEmpty() || bResults == null)
    {
      log.warn("No test results for bcheck received.");

      return FAILED;
    }

    log.debug("Test results for bcheck received.");

    return SUCCESS;
  }

  public String performTTCheck()
  {
    log.debug("Perform ttcheck.");
    
    ttResults = new String();

    ttResults = chatterbotAdmin.performTTCheck();

    if (ttResults.isEmpty() || ttResults == null)
    {
      log.warn("No test results for ttcheck received.");

      return FAILED;
    }

    log.debug("Test results for ttcheck received.");

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
    setBcheckSelected(Boolean.TRUE);
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
    setBcheckSelected(Boolean.FALSE);
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
    setBcheckSelected(Boolean.FALSE);
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
    setBcheckSelected(Boolean.FALSE);
    setQcheckSelected(Boolean.FALSE);
    setTtcheckSelected(Boolean.FALSE);
    setChatterbotSelected(Boolean.TRUE);

    // chatterbot application has been selected
    log.info("Chatterbot application has been selected.");
  }

  /**
   * @return the text
   */
  public String getText()
  {
    return text;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text)
  {
    this.text = text;
  }

  /**
   * @return the bcheckLanguage
   */
  public String getBcheckLanguage()
  {
    return bcheckLanguage;
  }

  /**
   * @param bcheckLanguage the bcheckLanguage to set
   */
  public void setBcheckLanguage(String bcheckLanguage)
  {
    this.bcheckLanguage = bcheckLanguage;
  }

  /**
   * @return the bcheckLearningInput
   */
  public String getBcheckLearningInput()
  {
    return bcheckLearningInput;
  }

  /**
   * @param bcheckLearningInput the bcheckLearningInput to set
   */
  public void setBcheckLearningInput(String bcheckLearningInput)
  {
    this.bcheckLearningInput = bcheckLearningInput;
  }

  /**
   * @return the bcheckLearningSelected
   */
  public Boolean getBcheckLearningSelected()
  {
    return bcheckLearningSelected;
  }

  /**
   * @param bcheckLearningSelected the bcheckLearningSelected to set
   */
  public void setBcheckLearningSelected(Boolean bcheckLearningSelected)
  {
    this.bcheckLearningSelected = bcheckLearningSelected;
  }

  /**
   * @return the bResults
   */
  public String getbResults()
  {
    return bResults;
  }

  /**
   * @param bResults the bResults to set
   */
  public void setbResults(String bResults)
  {
    this.bResults = bResults;
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
   * @return the bcheckSelected
   */
  public Boolean getBcheckSelected()
  {
    return bcheckSelected;
  }

  /**
   * @param bcheckSelected the bcheckSelected to set
   */
  public void setBcheckSelected(Boolean bcheckSelected)
  {
    this.bcheckSelected = bcheckSelected;
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
}
