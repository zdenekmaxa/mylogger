## MyLogger - Java Logger wrapper based on Apache log4j.

GUI extension for logging in the TextArea GUI component. 

Main class MyLogger.

Reading command line options to configure severity, log file (main
MyLogger initialization):

```
String[] vals = l.getOptionValues('d');
// vals[0] - severity level
// vals[1] - destination to write logs to
try
{
    MyLogger.initialize(vals); // need to check vals
}
```

In every other class:

```
import mylogger.MyLogger;
private static MyLogger logger = MyLogger.getLogger(CheckListGUI.class);
logger.info("Creating an instance of CheckList ...");
```

Debug logging output window:

```
logger.openDebuggingWindow();
```

For detailed usage see applications `Atlantis`, `CheckList`, `RunCom`.

