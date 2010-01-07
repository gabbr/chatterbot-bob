package it.unibz.lib.bob.check;


import org.apache.log4j.Level;

/**
 * * My own {@link org.apache.log4j.Level} for logging. * * @author Jaikiran Pai
 * *
 * */
public class CustomLog extends Level {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * * Value of my trace level. This value is lesser than *
	 * {@link org.apache.log4j.Priority#DEBUG_INT} * and higher than
	 * {@link org.apache.log4j.Level#TRACE_INT}
	 * */
	public static final int MY_TRACE_INT = DEBUG_INT;

	/**
	 * * {@link Level} representing my log level
	 * */
	public static final Level MY_TRACE = new CustomLog(MY_TRACE_INT,
			"MY_TRACE", 7);

	/**
	 * * Constructor * * @param arg0 * @param arg1 * @param arg2
	 * */
	protected CustomLog(int arg0, String arg1, int arg2) {
		super(arg0, arg1, arg2);

	}

	/**
	 * * Checks whether sArg is "MY_TRACE" level. If yes then returns *
	 * {@link MyTraceLevel#MY_TRACE}, else calls *
	 * {@link MyTraceLevel#toLevel(String, Level)} passing it *
	 * {@link Level#DEBUG} as the defaultLevel. * * @see
	 * Level#toLevel(java.lang.String) * @see Level#toLevel(java.lang.String,
	 * org.apache.log4j.Level) *
	 * */
	public static Level toLevel(String sArg) {
		if (sArg != null && sArg.toUpperCase().equals("MY_TRACE")) {
			return MY_TRACE;
		}
		return (Level) toLevel(sArg, Level.DEBUG);
	}

	/**
	 * * Checks whether val is {@link MyTraceLevel#MY_TRACE_INT}. * If yes then
	 * returns {@link MyTraceLevel#MY_TRACE}, else calls *
	 * {@link MyTraceLevel#toLevel(int, Level)} passing it {@link Level#DEBUG} *
	 * as the defaultLevel * * @see Level#toLevel(int) * @see Level#toLevel(int,
	 * org.apache.log4j.Level) *
	 * */
	public static Level toLevel(int val) {
		if (val == MY_TRACE_INT) {
			return MY_TRACE;
		}
		return (Level) toLevel(val, Level.DEBUG);
	}

	/**
	 * * Checks whether val is {@link MyTraceLevel#MY_TRACE_INT}. * If yes then
	 * returns {@link MyTraceLevel#MY_TRACE}, * else calls
	 * {@link Level#toLevel(int, org.apache.log4j.Level)} * * @see
	 * Level#toLevel(int, org.apache.log4j.Level)
	 * */
	public static Level toLevel(int val, Level defaultLevel) {
		if (val == MY_TRACE_INT) {
			return MY_TRACE;
		}
		return Level.toLevel(val, defaultLevel);
	}

	/**
	 * * Checks whether sArg is "MY_TRACE" level. * If yes then returns
	 * {@link MyTraceLevel#MY_TRACE}, else calls *
	 * {@link Level#toLevel(java.lang.String, org.apache.log4j.Level)} * * @see
	 * Level#toLevel(java.lang.String, org.apache.log4j.Level)
	 * */
	public static Level toLevel(String sArg, Level defaultLevel) {
		if (sArg != null && sArg.toUpperCase().equals("MY_TRACE")) {
			return MY_TRACE;
		}
		return Level.toLevel(sArg, defaultLevel);
	}
}
