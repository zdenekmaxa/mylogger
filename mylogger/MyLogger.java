package mylogger;

import java.io.IOException;
import java.io.File;
import java.util.Properties;

import javax.swing.JTextPane;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;



/**
 * The class MyLogger is a common wrapper for logging facility, uses
 * apache-logging library log4j (more complex and powerful than Java
 * native java.util.logging (e.g. flushing is automated, allows customised
 * message logs layout, different levels for different sources, etc).
 * Because it is subclassing from Logger, it needs LoggerFactory mechanism
 * to be in place. Reference to it is given via Properties, if more
 * complex properties are to be defined, it will be put in a file (such
 * properties file must be accessed generically so that it is load in
 * correctly no matter whether application is run locally (and have
 * distribution directories available) or from a .jar file
 * (webstart execution).
 * @author Zdenek Maxa
 */
public final class MyLogger extends Logger
{		
	// the class is a singleton, reference to itself
	private static Logger root = null;
	
	// it's enough to instantiate a factory once and for all
	private static MyLoggerFactory factory = new MyLoggerFactory();
	
	// logging layouts definition (format characters explained at PatternLayout class)
	private static final String CONSOLE_LOGGING_LAYOUT =
		"%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p  %m%n";
	// %c - category (must be specified at .getLogger(Class.class))
	private static final String FILE_LOGGING_LAYOUT =
		"%-5p %d{yyyy-MM-dd HH:mm:ss,SSS}  %m%n  (%c thread:%t)%n";
	// contains some format characters which are slow - *only* for debugging
	private static final String FILE_LOGGING_LAYOUT_FULL =
		"%-5p %d{yyyy-MM-dd HH:mm:ss,SSS}  %m%n  (%c.%M() [line %L], thread:%t)%n";
	
	
	
	// logging levels as defined in org.apache.log4j.Level
	// this variable is used via a getter method to print out command
	// line options help
	private static String stringLevels = 
		Level.DEBUG.toString()  + ", " + // int value: 10000
		Level.INFO.toString()   + ", " + // int value: 20000
		Level.WARN.toString()   + ", " + // int value: 30000
		Level.ERROR.toString() + ", " +  // int value: 30000
		Level.FATAL.toString();          // int value: 50000

	// default logging level (if it wasn't specified or was incorrect)
	private static final Level DEFAULT_LEVEL = Level.DEBUG;

	
	
	/**
	 * The constructor must be protected so that it is visible
	 * from ALoggerFactory. Should not be instantiated directly, but via
	 * .getInstance() 
	 * @param name
	 */
	protected MyLogger(String name)
	{
		super(name);
		
	} // MyLogger() ---------------------------------------------------------

	
	
	/**
	 * initialize(String[] options)
	 * Initializes the logger
	 * options[0] - severity level as read from the command line option
	 * options[1] - destination to write logs to, as read from the command line
	 */	
	public static void initialize(String[] options) throws Exception
	{
		String level = null;
		String destination = null;
		
		// check the input first, may be null or incomplete
		if(options == null)
		{
			// no options were provided, set default
			level = DEFAULT_LEVEL.toString();
		}
		else if(options.length == 1)
		{
			level = options[0];
		}
		else if(options.length == 2)
		{
			level = options[0];
			destination = options[1];
		}

		// set logging level, if level contains nonsense, set default level
		Level currentLevel = Level.toLevel(level, DEFAULT_LEVEL);
		

		// set properties - MyLogggerFactory
		// log4j.loggerFactory=<factory class name>
		// this may also be done via properties file (if necessary for
		// more complex logging configuration without necessity to
		// recompile). properties file contains pairs key=value
		// caution: must be accessed generically (so that it also
		// works when application is packed in a .jar file)
		// without setting LoggerFactory - ClassCastException when getting
		// Logger via .getLogger("name");
		Properties up = new Properties();
		up.setProperty("log4j.loggerFactory", "mylogger.MyLoggerFactory");
		PropertyConfigurator.configure(up);
		
		// if instantiated this way
		// logger = new MyLogger();
		// -> NullPointerException when adding appender
		
		// all loggers created afterwards will have properties of this root logger
		root = Logger.getRootLogger();
		
		root.setLevel(currentLevel);

		
		// logging will always be done to console (System.out - stdout)

		Layout consoleLayout = new PatternLayout(CONSOLE_LOGGING_LAYOUT);
		ConsoleAppender ca = new ConsoleAppender(consoleLayout,
				ConsoleAppender.SYSTEM_OUT);
		ca.setImmediateFlush(true);
		root.addAppender(ca);

		root.warn("Logging to console (System.out) initialised " +
				  "(current level: " + root.getLevel().toString() + ").");
	
		// if a file was specified as logging destination, then logs
		// will be duplicated to that file as well
		if(destination != null)
		{
			// check if the file already exists
			File f = new File(destination);
			boolean fileExists = false;
			if(f.exists())
			{
				fileExists = true;
			}
			try
			{
				// some file was specified on the command line
				Layout fileLayout = new PatternLayout(FILE_LOGGING_LAYOUT);
				// if the file exists, it will be appended to
				FileAppender fa = new FileAppender(fileLayout, destination);
				fa.setImmediateFlush(true);
				root.addAppender(fa);
				if(fileExists)
				{
					String msg = "==================================  " +
					             "Log file " + destination + " exists, " +
					             "opening it for appending.";
					root.warn(msg);
				}
				root.warn("Logging to " + destination + " initialised.");
			}
			catch(IOException ex)
			{
				throw new Exception("Can't create or write into: " + destination);
			}
		}
				
	} // initialize() -------------------------------------------------------

	
	
	/**
	 * Method which is called from outside as logger.openDebuggingWindow()
	 * The only interaction with the outside code.
	 */
	public void openDebuggingWindow()
	{
	    root.debug("Openning special logging window ...");
	    LoggerWindow.getInstance().showLoggingWindow();
	    
	} // openDebuggingWindow() ----------------------------------------------

	
	
	/**
	 * This method could also be called from outside, if logging is to be
	 * done into already created JTextPane component within some
	 * existing GUI.
	 * @param textPane
	 */
	public void addJTextPaneAppender(JTextPane textPane)
	{
        JTextPaneLoggerAppender textPaneAppender = null;
        textPaneAppender = new JTextPaneLoggerAppender(textPane);        
        root.addAppender(textPaneAppender);
	    
	} // addJTextPaneAppender() ---------------------------------------------
	
	

	public static MyLogger getLogger(String name) 
	{
		if(root != null)
		{	
			return (MyLogger) Logger.getLogger(name, factory);
		}
		else
		{
			// logger hasn't been initialised yet, initialise with
			// default values (i.e. logging only to console with INFO level)			
			String[] o = new String[] { DEFAULT_LEVEL.toString() };
			try
			{
				initialize(o);
			}
			catch(Exception ex)
			{
				// this exception should never occur
				ex.printStackTrace();
			}
			return (MyLogger) Logger.getLogger(name, factory);
		}
		
	} // getLogger() --------------------------------------------------------

	

	public static MyLogger getLogger(Class clazz)
	{
		return MyLogger.getLogger(clazz.getName());
		
	} // getLogger() --------------------------------------------------------

	
	
	public void forcedLog(String fqcn, Object msg, Throwable t)
	{
		super.forcedLog(fqcn, Level.ALL, msg, t);
		
	} // forcedLog() --------------------------------------------------------

	
	
	public static String getStringLevels()
	{
		return stringLevels;
		
	} // getStringLevels() --------------------------------------------------
	
	
	
	/**
	 * Accessing Logger - Category geLevel() on logger returned by getLogger()
	 * returns null unless the level was implicitly assigned. This method
	 * provides means of accessing root logger current level which all
	 * instantiated loggers inherit. (see API docs on Category.getLevel())
	 * @return
	 */
	public Level getCurrentLevel()
	{
	    return root.getLevel();
	    
	} // getCurrentLevel() --------------------------------------------------
	
} // class MyLogger =========================================================
