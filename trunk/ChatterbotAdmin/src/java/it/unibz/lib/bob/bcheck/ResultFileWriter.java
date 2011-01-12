package it.unibz.lib.bob.bcheck;

/**
 * Write results to ARFF or CSV
 * At some point should provide a nice API for writing correct ARFF files
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ResultFileWriter {

	private PrintWriter out;

	public ResultFileWriter(String filename) {
		try {
			out = new PrintWriter(new FileWriter(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

	public void println(String s) {
		out.println(s);
	}

	public void append(StringBuffer sb) {
		out.append(sb);
	}

	public void close() {
		out.close();
	}
}