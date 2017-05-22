package mylogger;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;


/**
 * Appender class for logging into graphical window (JTextPane) via log4j.
 */
public class JTextPaneLoggerAppender extends AppenderSkeleton
{
    private JTextPane textPane = null;
    private Layout layout = null;
    
    private Style styleNormal = null;
    private Style styleBold = null;
    private Style styleRed = null; 
    private Style styleBlue = null;    
    
    
    
    public JTextPaneLoggerAppender(JTextPane newTextPane)
    {
        super();
        
        textPane = newTextPane;
        
        StyledDocument doc = textPane.getStyledDocument();
        
        styleNormal = doc.addStyle("regular", null);
        StyleConstants.setForeground(styleNormal, Color.BLACK);
         
        styleBlue = doc.addStyle("blue", null);
        StyleConstants.setForeground(styleBlue, Color.BLUE);
         
        styleBold = doc.addStyle("bold", styleNormal);
        StyleConstants.setBold(styleBold, true);
        styleRed = doc.addStyle("red", styleBold);
        StyleConstants.setForeground(styleRed, Color.RED);
        
        layout = new LoggerFlexibleLayout();
         
    } // JTextPaneLoggerAppender() ------------------------------------------
    
    

    protected void append(LoggingEvent event)
    {
        String toLog = layout.format(event);
        
        if(textPane != null)
        {
            if(event.getLevel().equals(Level.ERROR))
            {
                logError(toLog);
            }
            else if(event.getLevel().equals(Level.FATAL))
            {
                logFatal(toLog);
            }
            else if(event.getLevel().equals(Level.WARN))
            {
                logWarning(toLog);
            }
            else if(event.getLevel().equals(Level.TRACE))
            {
                logTrace(toLog);
            }
            else if(event.getLevel().equals(Level.DEBUG))
            {
                log(toLog, styleNormal);
            }
            else if(event.getLevel().equals(Level.INFO))
            {
                log(toLog, styleNormal);
            }
        }
        
    } // append() -----------------------------------------------------------
    
    
    
    private void logTrace(String s)
    {
        log(s, styleNormal);
        
    } // logTrace() ---------------------------------------------------------
    
    

    private void logWarning(String s)
    {
        log(s, styleBlue);
        
    } // logWarning() -------------------------------------------------------
    
    

    private void logFatal(String s)
    {
        logError(s);
        
    } // logFatal() ---------------------------------------------------------
    
    
    
    private void logError(String s)
    {
        log(s, styleRed);
        
    } // logError() ---------------------------------------------------------
    
    
    
    private void log(String s, Style style)
    {
        if(s == null)
        {
            return;
        }
        
        if(textPane == null)
        {
            return;
        }
        
        StyledDocument doc = textPane.getStyledDocument();
        try 
        {
            doc.insertString(doc.getLength(), s, style);
        } 
        catch(BadLocationException e)
        {
            // this exception should never occur
            e.printStackTrace();
        }
        
    } // log() --------------------------------------------------------------

    
    
    // auto-generated method
    public void close() {}
    

    // auto-generated method
    public boolean requiresLayout()
    {
        return false;
    }


} // class JTextPaneLoggerAppender ==========================================
