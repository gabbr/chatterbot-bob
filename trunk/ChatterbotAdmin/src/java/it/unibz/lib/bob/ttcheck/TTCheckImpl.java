package it.unibz.lib.bob.ttcheck;

import java.io.File;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidationSchemaFactory;

import org.apache.log4j.Logger;

/**
 * <p>
 * This class is used to to perform test of ttcheck application. When test 
 * has finished, test results are provided as a String object.
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @author markus.grandpre@uni-konstanz.de
 * @version $Id$
 */
public class TTCheckImpl implements TTCheck
{

  /**
   * <p>
   * 
   * </p>
   */
  private XMLValidationSchemaFactory schemaFactory;

  /**
   * <p>
   * 
   * </p>
   */
  private XMLValidationSchema schema;

  /**
   * <p>
   * 
   * </p>
   */
  private XMLInputFactory2 inputFactory;

  /**
   * <p>
   * 
   * </p>
   */
  private XMLStreamReader2 streamReader;

  /**
   * <p>
   * 
   * </p>
   */
  private File schemaFile;

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
  private Logger log = Logger.getLogger(TTCheckImpl.class);

  @Override
  public String performTTCheck(String topicTreeFile, String rngFile,
    String macrosENFile, String macrosDEFile, String macrosITFile)
  {
    String testResults = new String();
    XMLValidationSchemaFactory schemaFactory = XMLValidationSchemaFactory.
      newInstance(XMLValidationSchema.SCHEMA_ID_RELAXNG);
    schemaFile = new File(rngFile);
    XMLValidationSchema rng2 = null;
    
    try
    {
      rng2 = schemaFactory.createSchema(schemaFile);
    }
    catch (XMLStreamException xe)
    {
      testResults += "Could not load RNG file ('" + schemaFile + "'): \n\t" + xe.
        getMessage();

    }

    testResults = testResults + "Using RNG file for XML validation: "
      + rngFile + "\n";

    // document validation
    File inputFile = new File(topicTreeFile);

    try
    {
      inputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();
      streamReader = inputFactory.createXMLStreamReader(inputFile);

      try
      {
        streamReader.validateAgainst(rng2);
        while (streamReader.hasNext())
        {
          streamReader.next();
        }

        /*
         * Check all regex patterns in abbrev file and topic tree, using
         * abbreviations from the 3 abbrev files
         */
        testResults += RegexCheckHelper.doRegexChecks(inputFile, macrosDEFile,
          macrosENFile, macrosITFile);
      }
      catch (Exception vex)
      {
        testResults += "File '" + topicTreeFile
          + "' did NOT pass validation: "
          + vex.getMessage();
      }
    }
    catch (XMLStreamException xse)
    {
      testResults += "Could not read input file ('"
        + topicTreeFile + "'): " + xse.getMessage();
    }
    return testResults;
  }
}
