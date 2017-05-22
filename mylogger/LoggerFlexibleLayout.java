package mylogger;

import java.util.Date;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;



public class LoggerFlexibleLayout extends Layout
{
    private static final String LINE_SEP = System.getProperty("line.separator");

    
    public String format(LoggingEvent event)
    {
        StringBuffer sb = new StringBuffer();
        
        String level = event.getLevel().toString();

        Date dateTime = new Date(event.getTimeStamp());        
        sb.append(String.format("%1$tH:%1$tM:%1$tS,%1$tL", dateTime));
        
        sb.append(" ");
        
        sb.append(level);
        
        sb.append(" ");
                        
        sb.append(event.getMessage());
        
        sb.append(LINE_SEP);
        
        if(event.getThrowableInformation() != null)
        {
            String[] s = event.getThrowableStrRep();
            for(int i = 0; i < s.length; i++)
            {
                sb.append(s[i]).append(LINE_SEP);
            }
        }
        
        return sb.toString();
        
    } // format() -----------------------------------------------------------
    
    
    
    // auto-generated method stubs (from Layout)
    public void activateOptions() {}
    
    
    public boolean ignoresThrowable()
    {       
        return false;
    }


} // class LoggerFlexibleLayout =============================================