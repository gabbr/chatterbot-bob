package it.unibz.lib.bob.chatterbot;

/**
 * At some point should provide a nice API for writing correct ARFF files
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ARFFWriter {

	private PrintWriter out;

	public ARFFWriter(String filename) {
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