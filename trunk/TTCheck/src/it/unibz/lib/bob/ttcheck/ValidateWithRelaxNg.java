package it.unibz.lib.bob.ttcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidationSchemaFactory;

public class ValidateWithRelaxNg {
	public static void main(String[] args) {

		String rng = null;
		String xml = null;
		String abk_DE = null;
		String abk_EN = null;
		String abk_IT = null;

		if (args.length == 0) {
			rng = "library_domain.rng";
			xml = "topictree.xml";
			abk_DE = "bob_macros_DE.txt";
			abk_EN = "bob_macros_EN.txt";
			abk_IT = "bob_macros_IT.txt";
		} else if (args.length == 5) {
			rng = args[0];
			xml = args[1];
			abk_DE = args[2];
			abk_EN = args[3];
			abk_IT = args[4];
		} else {
			System.err
					.println("Usage: java ValidateWithRelaxNg rng-file topic-tree-file macroFile-DE macroFile-EN macroFile-IT");
			System.exit(1);
		}

		/**
		 * Check XML validity against RNG schema
		 */
		XMLValidationSchemaFactory schemaFactory = XMLValidationSchemaFactory
				.newInstance(XMLValidationSchema.SCHEMA_ID_RELAXNG);
		File schemaFile = new File(rng);
		XMLValidationSchema rng2 = null;
		try {
			rng2 = schemaFactory.createSchema(schemaFile);
		} catch (XMLStreamException xe) {
			System.err.println("Could not load RNG file ('" + schemaFile
					+ "'): \n\t" + xe.getMessage());
			System.exit(1);
		}
		System.err.println("Using RNG file for XML validation: " + rng);
		// document validation
		File inputFile = new File(xml);
		try {
			XMLInputFactory2 inputFactory = (XMLInputFactory2) XMLInputFactory
					.newInstance();
			XMLStreamReader2 streamReader = inputFactory
					.createXMLStreamReader(inputFile);
			System.err.println("Validating topictree xml file: " + xml);

			try {
				streamReader.validateAgainst(rng2);
				while (streamReader.hasNext()) {
					streamReader.next();
				}

				/**
				 * Check all regex patterns in abbrev file and topic tree, using
				 * abbreviations from the 3 abbrev files
				 */
				RegexCheckHelper.doRegexChecks(inputFile, abk_DE, abk_EN, abk_IT);

			} catch (Exception vex) {
				System.err.println("File '" + inputFile
						+ "' did NOT pass validation: "
						+ vex.getMessage());
				ReadReturnKeyFromConsole();
				System.exit(1);
			}
		} catch (XMLStreamException xse) {
			System.err.println("Could not read input file ('"
					+ inputFile + "'): " + xse.getMessage());
			ReadReturnKeyFromConsole();
			System.exit(1);
		}

		// Only makes sense if an uncought exception is thrown after an error
		// has been detected, so that this only shows when no errors exist...
		// System.out.println("Document '" + inputFile
		// + "' succesfully validated.");

		ReadReturnKeyFromConsole();
	}

	public static void ReadReturnKeyFromConsole() {
		try {
			BufferedReader inStream = new BufferedReader(new InputStreamReader(
					System.in));
			System.err.print("Please press <return> to continue");
			inStream.readLine();
		} catch (IOException e) {
			System.err.println("IOException: " + e);
			return;
		}
	}

}
