package it.unibz.lib.bob.chatterbot;

import java.io.*;

public class LogReader {

	private long lastReadLineNumber = 0;

	private String logfile = "";
	private static String logfile_path = "";

	private StringBuffer tail = new StringBuffer();

	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}

	public static void main(String args[]) {
		LogReader l = new LogReader();
		// l.setLogfile("/usr/share/tomcat5.5/logs/bob/debug_out.txt");
		l.setLogfile(logfile_path + "/internal.log");
		System.out.println(l.getTail());
		System.out.println(l.getTail());
		System.out.println(l.getTail());
	}

	public String getTail() {

		// read the file, store the contents in the 'tail' variable
		// update the lastReadLineNumber, so that you need to read from that
		// line onwards next time.
		try {

			this.setLogfile(logfile_path + "/internal.log");
			FileReader fr = new FileReader(logfile);
			BufferedReader buff_read = new BufferedReader(fr);

			String line;

			int l = 0;
			while ((line = buff_read.readLine()) != null) {
				l++;
				if (l > lastReadLineNumber) {
					// if (line.startsWith("DEBUG: ")) {

					// get rid of date, time, etc information here, which is
					// still needed in the log file after all.
					tail.append(line.substring(33));
					// tail.append(System.getProperty("line.separator"));
					tail.append("<br/>");
					// }
				}

			}
			lastReadLineNumber = l;
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		int firstchar = tail.length() - 2000;
		if (firstchar < 0) {
			firstchar = 0;
		}

		return tail.substring(firstchar);
	}

	public static void setFilePath(String path) {
		logfile_path = path;
	}
}
