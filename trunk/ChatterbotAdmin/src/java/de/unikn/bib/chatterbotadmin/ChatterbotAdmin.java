package de.unikn.bib.chatterbotadmin;

import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 *
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public interface ChatterbotAdmin
{
  public void uploadTopicTreeFile(UploadedFile topicTreeFile);

  public void uploadMacrosFile(UploadedFile macrosFile);

  public void uploadMacrosENFile(UploadedFile macrosENFile);

  public void uploadMacrosDEFile(UploadedFile macrosDEFile);

  public void uploadMacrosITFile(UploadedFile macrosITFile);

  public void uploadTextCorpusFile(UploadedFile textCorpusFile);

  public void uploadTextCorpusENFile(UploadedFile textCorpusENFile);

  public void uploadTextCorpusDEFile(UploadedFile textCorpusDEFile);

  public void uploadTextCorpusITFile(UploadedFile textCorpusITFile);

  public void uploadRngFile(UploadedFile rngFile);

  public void uploadTestQuestionsFile(UploadedFile testQuestionsFile);

  public String performBCheck(String language);

  public String performTTCheck();



}
