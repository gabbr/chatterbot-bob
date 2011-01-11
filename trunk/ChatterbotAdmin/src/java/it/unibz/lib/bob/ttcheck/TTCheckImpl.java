package it.unibz.lib.bob.ttcheck;

import java.io.File;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidationSchemaFactory;

// logging context
import org.apache.log4j.Logger;

/**
 *
 * @version $Id$
 */
public class TTCheckImpl implements TTCheck
{
  private XMLValidationSchemaFactory schemaFactory;

  private XMLValidationSchema schema;

  private XMLInputFactory2 inputFactory;

  private XMLStreamReader2 streamReader;

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
  public String performTTCheck(String ttFile, String rngFile,
          String macrosDEFile,
          String macrosENFile, String macrosITFile)
  {

    String testResults = new String();

    try
    {

      schemaFactory = XMLValidationSchemaFactory.newInstance(XMLValidationSchema.SCHEMA_ID_RELAXNG);
      schemaFile = new File(rngFile);
      XMLValidationSchema rng2 = null;
      rng2 = schemaFactory.createSchema(schemaFile);

      testResults = testResults + "Using RNG file for XML validation: "
              + rngFile + "\n";

      // document validation
      File fileFocusTree = new File(ttFile);
      inputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();
      streamReader = inputFactory.createXMLStreamReader(fileFocusTree);

      testResults = testResults + "Validating topictree xml file: "
              + ttFile + "\n";

      streamReader.validateAgainst(rng2);

      while (streamReader.hasNext())
      {
        streamReader.next();
      }

      // Check all regex patterns in abbrev file and topic tree, using
      // abbreviations from the 3 abbrev files
      testResults = testResults + RegexCheckHelper.doRegexChecks(fileFocusTree,
              macrosDEFile, macrosENFile, macrosITFile);
    }
    catch (XMLStreamException e)
    {
      log.error("Could not load file (" + schemaFile.getName() + "): "
              + e.getMessage());
    }
    catch (Exception e)
    {
    }

    return testResults;
  }
}
