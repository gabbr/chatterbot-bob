package it.unibz.lib.bob.chatterbot;

import it.CustomLog;
import it.unibz.lib.bob.check.DialogueManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class InitServlet extends HttpServlet {

	public void init(ServletConfig config) throws ServletException {
		System.out.println("Begin Init OK");

		try {
			super.init(config);
			// Some parameters are context init parameter, so to output them
			// from welcome.jsp
			ServletContext context = this.getServletContext();

			// Init parameters for DialogueManager
			DialogueManager.setUrlTopicTree(context
					.getInitParameter("urlTopicTree"));
			DialogueManager.setUrlAbbrevFileEN(context
					.getInitParameter("urlAbbrevFileEN"));
			DialogueManager.setUrlAbbrevFileDE(context
					.getInitParameter("urlAbbrevFileDE"));
			DialogueManager.setUrlAbbrevFileIT(context
					.getInitParameter("urlAbbrevFileIT"));
			DialogueManager.setUrlIDFtrainingDataEN(context
					.getInitParameter("urlIDFtrainingDataEN"));
			DialogueManager.setUrlIDFtrainingDataDE(context
					.getInitParameter("urlIDFtrainingDataDE"));
			DialogueManager.setUrlIDFtrainingDataIT(context
					.getInitParameter("urlIDFtrainingDataIT"));

			String logHome = ""
					+ context.getAttribute("javax.servlet.context.tempdir");
			LogFile.setFilePath(logHome);
			LogReader.setFilePath(logHome);

			Logger log = Logger.getLogger(InitServlet.class);

			log.log(CustomLog.MY_TRACE, "Init OK");

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
