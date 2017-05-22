package mylogger;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;



/**
 * 
 * Window with text pane which acts as a logger appender.
 * Singleton class.
 * @author Zdenek Maxa
 *
 */
public class LoggerWindow extends JFrame
{
    private static MyLogger logger = MyLogger.getLogger(LoggerWindow.class);
    
    // singleton class
    private static LoggerWindow instance = null;

    JTextPane logTextPane = null;
    
    
    private LoggerWindow()
    {
        super();
        
        String level = logger.getCurrentLevel().toString();
        this.setTitle("Logging window (current level: " + level + ")");
        
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        logTextPane = new JTextPane();
        logTextPane.setEnabled(true);
        logTextPane.setEditable(false);
        logTextPane.setBorder(BorderFactory.createLineBorder(Color.black));
        logTextPane.setRequestFocusEnabled(false);
                        
        scrollPane.getViewport().add(logTextPane);
        logPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        JButton clearButton = new JButton("Clear");
        clearButton.setActionCommand("clear");
        clearButton.addActionListener(new ButtonListener());
        bottomPanel.add(clearButton);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        
        // connect Logger appender and logTextPane
        logger.addJTextPaneAppender(logTextPane);
        
        logger.info("Logging into GUI window should now be activated.");
        
        this.getContentPane().add(logPanel, BorderLayout.CENTER);  
        
        this.addWindowListener(new GUIWindowAdapter());
        
        this.setVisible(true);
        this.pack();
        this.setSize(400, 500);
        
    } // LoggerWindow() -----------------------------------------------------
    
    
    
    public void clearLoggingTextPane()
    {
        this.logTextPane.setText("");
        
    } // clearLoggingTextPane() ---------------------------------------------
    
    
    
    public static LoggerWindow getInstance()
    {
        if(instance == null)
        {
            instance = new LoggerWindow();
        }
        
        return instance;
        
    } // getInstance() ------------------------------------------------------
    
    
    
    public void showLoggingWindow()
    {
        instance.setVisible(true);
      
    } // showLoggingWindow() ------------------------------------------------
    
    
    
    public void closeWindow()
    {
        instance.dispose();
        instance = null;
        logger.debug("Logging window closed.");

    } // closeWindow() ------------------------------------------------------
    
    
} // class LoggerWindow =====================================================



/**
 * Easier than WindowListener - doesn't have to implement a number of
 * methods out of which majority would remain empty.
 */
class GUIWindowAdapter extends WindowAdapter
{
    public void windowClosing(WindowEvent we)
    {
        LoggerWindow.getInstance().closeWindow();
        
    } // windowClosing() ----------------------------------------------------
    
} // GUIWindowAdapter =======================================================



class ButtonListener implements ActionListener
{
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();
        
        if("clear".equals(command))
        {
            LoggerWindow.getInstance().clearLoggingTextPane();
        }
        
    } // actionPerformed() --------------------------------------------------
    
} // class ButtonListener ===================================================