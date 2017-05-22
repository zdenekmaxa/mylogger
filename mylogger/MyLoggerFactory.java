package mylogger;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

/**
 * @author Zdenek Maxa
 * MyLoggerFactory as used by Logger - necessary when subclassing the
 * Apache log4j Logger class.
 */
public class MyLoggerFactory implements LoggerFactory
{

    /**
     * The constructor should be public as it will be called by
     * configurators in different packages.
     */
    public MyLoggerFactory() { }
    
    
    public Logger makeNewLoggerInstance(String name) 
    {
        return new MyLogger(name);
        
    } // makeNewLoggerInstance() --------------------------------------------

} // class MyLoggerFactory ==================================================
