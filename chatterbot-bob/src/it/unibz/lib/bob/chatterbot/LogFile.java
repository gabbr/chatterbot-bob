package it.unibz.lib.bob.chatterbot;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile {

	static final String LOG_DATE_FORMAT = "yyyy-MM-dd_HHmmss";
	// logfile_path is set by InitServlet, to
	// context.getAttribute("javax.servlet.context.tempdir"), which is probably:
	// /usr/share/tomcat5.5/work/Catalina/localhost/bob
	private static String logfile_path;
	private static String fileName;

	// XML dateTime compatible
	// static final String LOG_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	private Date timestamp;

	// machine-friendly string
	String getLogTimestamp() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(LOG_DATE_FORMAT);
		return dateFormat.format(timestamp);
	}

	// the dialogue to write to disk
	public synchronized void saveDialogue(String content) {
		timestamp = new Date();
		fileName = new String(logfile_path + "/IQA_" + getLogTimestamp()
				+ ".xml");

		try {
			OutputStream fout = new FileOutputStream(fileName);
			OutputStream bout = new BufferedOutputStream(fout);
			OutputStreamWriter out = new OutputStreamWriter(bout, "UTF-8");

			out.write(content);

			out.flush(); // Don't forget to flush!
			out.close();
		} catch (UnsupportedEncodingException e) {
			System.out
					.println("This VM does not support the UTF-8 character set.");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public static void setFilePath(String path) {
		logfile_path = path;
	}

	public String getFileName() {
		return fileName;
	}

}
