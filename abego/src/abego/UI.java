package abego;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

public class UI 
{
	private static JFrame frame;
	private final static int WIDTH  = 1400;
	private final static int HEIGHT = 800;
	public final static int BLOCK_WIDTH = 160;
	public final static int BLOCK_HEIGHT = 80;
	public final static int MARGIN = 20;
	public final static int BORDER_WIDTH = 2;
	
	public final static int X_START = WIDTH / 2;
	public final static int Y_START = BLOCK_HEIGHT * 3 + MARGIN * 2;//HEIGHT / 2;
	
	private static int N0 = -1;

	private static JPanel mainPanel;
	
	private static int rightMostX = 0;
	
	private static final double COMPRESSION_RATIO = 0.6;
	
	public static void addComponentsToPane2( Container pane ) throws IOException 
	{

		mainPanel = new JPanel();
		mainPanel.setPreferredSize( new Dimension( WIDTH, HEIGHT ) );
        mainPanel.setLayout( null );

        JScrollPane scrollFrame = new JScrollPane(mainPanel);
        scrollFrame.setPreferredSize(new Dimension( WIDTH, HEIGHT ));
        pane.add( scrollFrame );
        
        int selectedContact = 1;
        
        // 1. Contact
        Contact contact = Relation.getContact( selectedContact );
        contact.setCoordinates( getNextX( 0 ), Y_START );
        drawContact( contact, mainPanel );
        
        drawParent( contact, 0, "M" );
        drawParent( contact, 0, "F" );
        
        // 2. Spouse(s)
        List<Contact> spouses = Relation.getSpouses(selectedContact); 
        if( spouses != null )
        {
     	    for( int i=0;i<spouses.size();i++ )
     	    {     	    	
	     	    int x = getNextX( 0 );
	     	    rightMostX = ( x > rightMostX ) ? x : rightMostX;
     	    	spouses.get(i).setCoordinates( x, Y_START );
	 	        drawContact( spouses.get(i), mainPanel );
	 	        
	 	        linkContacts( ( i==0 ) ? contact : spouses.get(i-1), spouses.get(i), mainPanel );
	 	        
	 	        drawParent( spouses.get(i), 0, "M" );
	 	        drawParent( spouses.get(i), 0, "F" );
	 	        
	 	       drawCommonChildren( ( i==0 ) ? contact : spouses.get(i-1), spouses.get(i), mainPanel );
     	    } 
        }
        int x = rightMostX + BLOCK_WIDTH + MARGIN;
        rightMostX = x;
        Contact spouse = new Contact( -1, "Add Spouse" );
        spouse.setCoordinates( x, Y_START );
	    drawContact( spouse, mainPanel );
	    linkContacts( ( spouses == null ) ? contact : spouses.get(spouses.size()-1), spouse, mainPanel );
        
        //System.out.println( rightMostX + BLOCK_WIDTH + MARGIN );
        mainPanel.setPreferredSize( new Dimension( rightMostX + BLOCK_WIDTH, HEIGHT ) );
	}
	
	private static int getNextX( int level )
	{
		return (int) ((( BLOCK_WIDTH + MARGIN ) * 2 + ( BLOCK_WIDTH + MARGIN ) * 6 * ++N0) * COMPRESSION_RATIO);
		
	}
	
	private static void drawParent( Contact contact, int level, String parentGender )
	{
		if( level >= -1 )
		{
			Contact parent;
			double x = 0;
			if( parentGender.equals( "M" ) )
			{
				parent = Relation.getFather( contact.id );
				if( parent == null )
				{
					parent = new Contact( -1, "Add Father" );
				}
				x = contact.x - ( BLOCK_WIDTH + MARGIN ) * COMPRESSION_RATIO;
				rightMostX = ( x > rightMostX ) ? (int)x : rightMostX;
			}
			else
			{
				parent = Relation.getMother( contact.id );
				if( parent == null )
				{
					parent = new Contact( -1, "Add Mother" );
				}
				x = contact.x + ( BLOCK_WIDTH + MARGIN ) * COMPRESSION_RATIO;	
				rightMostX = ( x > rightMostX ) ? (int)x : rightMostX;
			}
			if( parent != null )
			{
				parent.setCoordinates( x, contact.y - BLOCK_HEIGHT - MARGIN );
				//System.out.println( parent.x + ", " + parent.y );
			    drawContact( parent, mainPanel );
			    linkContacts( contact, parent, mainPanel );
			    
			    if( parent.id != -1 )
			    {
			    	drawParent( parent, level - 1, parentGender );
			    }
			}
		}
	}
		
	private static void drawContact( Contact contact, JPanel panel )
	{
		JLabel lbl = new JLabel( contact.name );
		lbl.setOpaque( true );
		lbl.setBorder( new LineBorder( Color.red) );
		lbl.setBackground( (contact.id!=-1) ? new Color( 125, 150, 175 ) : Color.white );
		lbl.setBounds( (int)contact.x, (int)contact.y, BLOCK_WIDTH, BLOCK_HEIGHT );
				
		panel.add( lbl );
	}
	
	private static void linkContacts( Contact c1, Contact c2, JPanel panel )
	{
		if( c1.x > c2.x ) // c1 is to the right of c2
		{
			if( c1.y > c2.y ) // c1 is below c2
			{
				//System.out.println("c2 bottom-left-c1 top");
				drawLine( c2.lineX, c2.bottomLineY1, c2.lineX, c2.bottomLineY2, mainPanel );
				drawLine( c2.lineX, c2.bottomLineY2, c1.lineX, c1.topLineY1, mainPanel );				
				drawLine( c1.lineX, c1.topLineY1, c1.lineX, c1.topLineY2, mainPanel );
			}
			else if( c1.y == c2.y ) // c1 and c2 are on the same level
			{
				//System.out.println("left-right");
				drawLine( c2.rightLineX1, c2.lineY, c1.leftLineX2, c1.lineY, mainPanel );
			}
			else // c1 is above c2
			{
				//System.out.println("bottom-left");
			}		
		}
		else if( c1.x < c2.x )
		{
			if( c1.y > c2.y ) // c1 is below c2
			{
				//System.out.println("c1 top-right-c2 bottom");
				drawLine( c2.lineX, c2.bottomLineY1, c2.lineX, c2.bottomLineY2, mainPanel );
				drawLine( c1.lineX, c2.bottomLineY2, c2.lineX, c1.topLineY1, mainPanel );
				drawLine( c1.lineX, c1.topLineY1, c1.lineX, c1.topLineY2, mainPanel );
			}
			else if( c1.y == c2.y ) // c1 and c2 are on the same level
			{
				//System.out.println("c1 right-c2 left");
				drawLine( c1.rightLineX1, c1.lineY, c2.leftLineX2, c2.lineY, mainPanel );
			}
			else // c1 is above c2
			{
				//System.out.println("bottom-left");
			}	
		}
	}
	
	private static void drawLine( double x1, double y1, double x2, double y2, JPanel panel )
	{
		JLabel lbl = new JLabel( );
		lbl.setBackground( Color.red );
		lbl.setOpaque( true );
		lbl.setBounds( (int)x1, (int)y1, (int) (x2-x1+BORDER_WIDTH), (int) (y2-y1+BORDER_WIDTH) );
		//System.out.println( lbl.getBounds());
		panel.add( lbl );
	}

	private static void drawCommonChildren( Contact contact, Contact spouse, JPanel panel )
	{
		List<Contact> children = Relation.getCommonChildren( contact.id, spouse.id );
		Contact addChild = new Contact( -1, "Add Child" );
		children.add( addChild );
		
		int maxPerRow 	= 3;
		int rows 		= children.size() / maxPerRow; // children rows may have max 6 contacts
		int remainingContacts = children.size() % maxPerRow; // remaining contacts
		double middleX = contact.x + BLOCK_WIDTH + MARGIN + ( spouse.leftLineX1 - contact.rightLineX2 ) / 2; // middle point between spouses
				
		double start =  middleX - ( BLOCK_WIDTH + MARGIN ) * ( maxPerRow ) / 2 + MARGIN / 2;
		int y = Y_START + BLOCK_HEIGHT * 2 + MARGIN * 2;
		
		for( int i=0;i<rows * maxPerRow;i++ )
		{	
			children.get(i).setCoordinates( start, y );
		    drawContact( children.get(i), panel );   
		    
		    drawLine( middleX, contact.lineY, middleX, y - MARGIN / 2, panel );
		    drawLine( children.get(i).lineX, children.get(i).topLineY1, children.get(i).lineX, children.get(i).topLineY2, panel );
		    if( middleX > children.get(i).lineX )
		    {
		    	drawLine( children.get(i).lineX, children.get(i).topLineY1, middleX, children.get(i).topLineY1, panel );  
		    }
		    else
		    {
		    	drawLine( middleX, children.get(i).topLineY1, children.get(i).lineX, children.get(i).topLineY1, panel );
		    }
		    
		    start += MARGIN + BLOCK_WIDTH;
			if( i != 0 && i % maxPerRow == maxPerRow-1 )
			{			
				y += BLOCK_HEIGHT + MARGIN;
				start = middleX - ( BLOCK_WIDTH + MARGIN ) * ( maxPerRow ) / 2 + MARGIN / 2;
			}
		    
		}
		if( remainingContacts > 0 )
		{	
			start = middleX - ( BLOCK_WIDTH + MARGIN ) * ( remainingContacts ) / 2 + MARGIN / 2;
			
			for( int i=rows*maxPerRow;i<children.size();i++ )
			{
				children.get(i).setCoordinates( start, y );
			    drawContact( children.get(i), panel );  
			    
				drawLine( middleX, contact.lineY, middleX, y - MARGIN / 2, panel );
			    drawLine( children.get(i).lineX, children.get(i).topLineY1, children.get(i).lineX, children.get(i).topLineY2, panel );
			    if( middleX > children.get(i).lineX )
			    {
			    	drawLine( children.get(i).lineX, children.get(i).topLineY1, middleX, children.get(i).topLineY1, panel );  
			    }
			    else
			    {
			    	drawLine( middleX, children.get(i).topLineY1, children.get(i).lineX, children.get(i).topLineY1, panel );
			    }
				 
			   // drawLine( children.get(i), panel, "top" );
			    
			    start += MARGIN + BLOCK_WIDTH;
			}
		}
		//panel.addLine( contact.rightLineX2, contact.lineY, contact.rightLineX2, y - MARGIN / 2 );
		//panel.addLine( children.get(rows*maxPerRow).lineX, children.get(rows*maxPerRow).topLineY1, children.get(children.size()-1).lineX, children.get(children.size()-1).topLineY1 );
				
	}

	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     * @throws IOException 
     */
    private static void createAndShowGUI() throws IOException {
        
        //Create and set up the window.
        frame = new JFrame("Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation( dim.width/2-frame.getSize().width/2-WIDTH/2, dim.height/2-frame.getSize().height/2-HEIGHT/2 );

        addComponentsToPane2( frame.getContentPane() );
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
					createAndShowGUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}
