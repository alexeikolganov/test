package abego;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CalendarUI implements FocusListener
{
	private static JFrame frame;
	private final static int WIDTH  = 1400;
	private final static int HEIGHT = 800;
	public final static int BLOCK_WIDTH = 100;
	public final static int BLOCK_HEIGHT = 40;
	public final static int MARGIN = 28;
	
	public final static int X_START = WIDTH / 2;
	public final static int Y_START = BLOCK_HEIGHT * 3 + MARGIN * 2;//HEIGHT / 2;
	
	JPanel layer;
	
	public void focusGained(FocusEvent e) {
        layer.setVisible(true);
        layer.getParent().revalidate();
        layer.getParent().repaint();
    }

    public void focusLost(FocusEvent e) {
    	System.out.println("Focus lost");
    	 layer.setVisible(false);
    	 layer.getParent().revalidate();
    	 layer.getParent().repaint();
    }
	public void addComponentsToPane( Container pane ) throws IOException 
    {
		JPanel mainPanel = new JPanel();
	    mainPanel.setPreferredSize( new Dimension( WIDTH, HEIGHT ) );
	    //mainPanel.setLayout( null );
	    pane.add( mainPanel);
		
		
		
		CalendarW cld = new CalendarW();
		cld.setLocation( MARGIN, MARGIN );
		
		CalendarW cld2 = new CalendarW();
		cld2.setLocation( MARGIN, MARGIN*3 );
		
		mainPanel.add( cld );
		mainPanel.add( cld2 );
		
		DateTextField dtf = new DateTextField();
		mainPanel.add( dtf );
    }
	
	
	
	
	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     * @throws IOException 
     */
    private void createAndShowGUI() throws IOException {
        
        //Create and set up the window.
        frame = new JFrame("Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation( dim.width/2-frame.getSize().width/2-WIDTH/2, dim.height/2-frame.getSize().height/2-HEIGHT/2 );

        addComponentsToPane( frame.getContentPane() );
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					CalendarUI clndr = new CalendarUI();
					clndr.createAndShowGUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}
