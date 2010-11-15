package it;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class MyContext implements ServletContextListener {

	private ServletContext context = null;

	public MyContext() {
	}

	public void contextDestroyed(ServletContextEvent event) {

		// back up old file
		File f = new File(context.getAttribute("javax.servlet.context.tempdir")
				+ "/internal.log");

		f.renameTo(new File(context.getRealPath("/")
				+ "/WEB-INF/internal.log__contextDestroyedBackup__" + now()
				+ ".log"));

		this.context = null;
	}

	// This method is invoked when the Web Application
	// is ready to service requests
	public void contextInitialized(ServletContextEvent event) {
		this.context = event.getServletContext();

		System.setProperty("log4jFile",
				context.getAttribute("javax.servlet.context.tempdir")
						+ "/internal.log");
	}

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd-HHmmss");
		return sdf.format(cal.getTime());
	}
	
	
	
}
