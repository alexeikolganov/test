package abego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.Calendar;

/**
 * Date Picker
 * @author akolganov
 *
 */
public class CalendarW extends JPanel
{
	private static final long serialVersionUID 	= 2793154058073858045L;
	
	private static String DEFAULT_DATE_FORMAT 	= "dd/MM/yyyy";
    private static final int DIALOG_WIDTH  		= 240;
    private static final int DIALOG_HEIGHT 		= 200;
    private static final int FIELD_HEIGHT  		= 32;
    private static final int BORDER_WIDTH  		= 2;
    private static final int BUTTON_X_Y    		= FIELD_HEIGHT - BORDER_WIDTH * 2;

    private SimpleDateFormat dateFormat;
    private DatePanel datePanel = null;
    private JDialog dateDialog  = null;
	
	private JTextField dateField;
	private JButton btn;
	
	/**
	 * Constructor
	 * 
	 * textfield
	 * button
	 * wrapped by a panel
	 * 
	 * date picker appears on click
	 */
	public CalendarW( )
	{
		setSize( DIALOG_WIDTH, FIELD_HEIGHT );
		setPreferredSize( new Dimension( DIALOG_WIDTH, FIELD_HEIGHT ) );
		setBorder( new LineBorder( Color.gray, BORDER_WIDTH ) );
		setLayout(null);
		
		dateField = new JTextField( );
		dateField.setBounds( BORDER_WIDTH, BORDER_WIDTH, DIALOG_WIDTH - FIELD_HEIGHT, BUTTON_X_Y );
		dateField.setBorder( null );
		add( dateField );
		
		btn = new JButton( "..." );
		btn.setBounds( DIALOG_WIDTH - BUTTON_X_Y - BORDER_WIDTH, BORDER_WIDTH, BUTTON_X_Y, BUTTON_X_Y );
		btn.setBorder( null );
		btn.setBackground( Color.gray );
		add(btn);
				
		setCursor( new Cursor( Cursor.HAND_CURSOR ) );
        addListeners( );
        
	}
	/**
	 * Add listener to invoke a date picker on mouse click
	 * in text field and button
	 */
	private void addListeners() 
	{
        btn.addMouseListener( new MouseAdapter() 
        {
            public void mouseClicked( MouseEvent paramMouseEvent ) 
            {
                if( datePanel == null ) 
                {
                    datePanel = new DatePanel();
                }
                Point point = getLocationOnScreen();
                point.y = point.y + FIELD_HEIGHT - BORDER_WIDTH;
                showDateDialog( datePanel, point );
            }
        });
        dateField.addMouseListener( new MouseAdapter() 
        {
            public void mouseClicked( MouseEvent paramMouseEvent ) 
            {
                if( datePanel == null ) 
                {
                    datePanel = new DatePanel();
                }
                Point point = getLocationOnScreen();
                point.y = point.y + FIELD_HEIGHT - BORDER_WIDTH;
                showDateDialog( datePanel, point );
            }
        });
    }

	/**
	 * Create Date Picker dialog
	 * which is closed when picker loses focus
	 * @param dateChooser - date picker panel
	 * @param position - cursor position
	 */
    private void showDateDialog( DatePanel dateChooser, Point position ) 
    {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor( CalendarW.this );
        if( dateDialog == null || dateDialog.getOwner() != owner ) 
        {
            dateDialog = createDateDialog( owner, dateChooser );
        }
        dateDialog.setModal( false );
        dateDialog.setLocation( getAppropriateLocation( owner, position ) );
        dateDialog.setVisible( true );
        dateDialog.addWindowFocusListener( new WindowFocusListener() 
        {
            public void windowGainedFocus (WindowEvent e ) 
            {
            
            }

            public void windowLostFocus( WindowEvent e ) 
            {
            	dateDialog.setVisible( false );
            }
        });
    }

    /**
     * Create Date Picker dialog
     * @param owner - parent frame
     * @param contentPanel - Date Picker panel to be injected into dialog
     * @return Date Picker dialog
     */
    private JDialog createDateDialog( Frame owner, JPanel contentPanel ) 
    {
    	JDialog dialog = new JDialog( owner, "Date Selected", true );
        dialog.setUndecorated (true );
        dialog.getContentPane( ).add( contentPanel, BorderLayout.CENTER );
        dialog.pack();
        dialog.setSize( DIALOG_WIDTH, DIALOG_HEIGHT );
        return dialog;
    }

    /**
     * Calculate Date Picker position
     * to be displayed right under calendar field / button
     * @param owner
     * @param position
     * @return
     */
    private Point getAppropriateLocation( Frame owner, Point position ) 
    {
        Point result = new Point( position );
        Point p 	 = owner.getLocation();
        int offsetX  = ( position.x + DIALOG_WIDTH ) - ( p.x + owner.getWidth( ) );
        int offsetY  = ( position.y + DIALOG_HEIGHT ) - ( p.y + owner.getHeight( ) );

        if( offsetX > 0 ) 
        {
            result.x -= offsetX;
        }

        if( offsetY > 0 ) 
        {
            result.y -= offsetY;
        }
        return result;
    }

    /** 
     * Get format
     * @return
     */
    private SimpleDateFormat getDefaultDateFormat() 
    {
        if( dateFormat == null ) 
        {
            dateFormat = new SimpleDateFormat( DEFAULT_DATE_FORMAT );
        }
        return dateFormat;
    }

    /**
     * Set text
     */
    public void setText( Date date ) 
    {
        setDate( date );
    }

    /**
     * Set date
     */
    public void setDate( Date date ) 
    {
    	dateField.setText( getDefaultDateFormat( ).format( date ) );
    }

    /**
     * Get date
     */
    public Date getDate() 
    {
        try 
        {
            return getDefaultDateFormat( ).parse( dateField.getText( ) );
        } 
        catch( ParseException ex ) 
        {
            return new Date();
        }
    }

    /**
     * Date widget panel
     * @author akolganov
     *
     */
    private class DatePanel extends JPanel implements ChangeListener 
    {

		private static final long serialVersionUID = 1909445351334061891L;
		int startYear = 1980;
        int lastYear = 2050;

        Color backGroundColor  	= new Color( 125, 125, 125 );
        Color palletTableColor 	= new Color( 125, 125, 125 );
        Color todayBackColor 	= Color.orange;
        Color weekFontColor 	= Color.white;
        Color dateFontColor 	= Color.white;
        Color weekendFontColor 	= Color.red;

        Color controlLineColor 	= new Color( 125, 125, 125 );
        Color controlTextColor 	= Color.white;

        JSpinner yearSpin;
        JSpinner monthSpin;
        JButton[][] daysButton = new JButton[6][7];

        DatePanel() 
        {
            setLayout( new BorderLayout() );
            setBorder( new LineBorder( backGroundColor, 2) );
            setBackground( backGroundColor );
            setFont( new Font( "Segoe UI", Font.PLAIN, 16 ) );

            JPanel topYearAndMonth = createYearAndMonthPanel();
            add(topYearAndMonth, BorderLayout.NORTH);
            JPanel centerWeekAndDay = createWeekAndDayPanel();
            add(centerWeekAndDay, BorderLayout.CENTER);

            reflushWeekAndDay();
        }

        private JPanel createYearAndMonthPanel() 
        {
            Calendar cal 	 = getCalendar();
            int currentYear  = cal.get( Calendar.YEAR );
            int currentMonth = cal.get( Calendar.MONTH ) + 1;

            JPanel panel = new JPanel();
            panel.setLayout( new FlowLayout() );
            panel.setBackground( controlLineColor );

            yearSpin = new JSpinner( new SpinnerNumberModel( currentYear, startYear, lastYear, 1 ) );
            yearSpin.setFont( new Font( "Segoe UI", Font.PLAIN, 16 ) );
            yearSpin.setPreferredSize( new Dimension(56, 24) );
            yearSpin.setName("Year");
            yearSpin.setBorder( null );
            yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
            yearSpin.addChangeListener(this);
            panel.add(yearSpin);

            JLabel yearLabel = new JLabel("Year");
            yearLabel.setForeground(controlTextColor);
            yearLabel.setFont( new Font( "Segoe UI", Font.PLAIN, 16 ) );
            panel.add(yearLabel);

            monthSpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1, 12, 1));
            monthSpin.setFont( new Font( "Segoe UI", Font.PLAIN, 16 ) );
            monthSpin.setPreferredSize(new Dimension(35, 24));
            monthSpin.setName("Month");
            monthSpin.setBorder( null );
            monthSpin.addChangeListener(this);
            panel.add(monthSpin);

            JLabel monthLabel = new JLabel("Month");
            monthLabel.setForeground(controlTextColor);
            monthLabel.setFont( new Font( "Segoe UI", Font.PLAIN, 16 ) );
            panel.add(monthLabel);

            return panel;
        }

        private JPanel createWeekAndDayPanel() 
        {
            String colname[] = { "S", "M", "T", "W", "T", "F", "S" };
            JPanel panel = new JPanel();
            panel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panel.setLayout(new GridLayout(7, 7));
            panel.setBackground(backGroundColor);

            for (int i = 0; i < 7; i++) {
                JLabel cell = new JLabel(colname[i]);
                cell.setHorizontalAlignment(JLabel.RIGHT);
                cell.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                if( i == 0 || i == 6 ) 
                {
                    cell.setForeground( weekendFontColor );
                } 
                else 
                {
                    cell.setForeground( weekFontColor );
                }
                panel.add(cell);
            }

            int actionCommandId = 0;
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 7; j++) {
                    JButton numBtn = new JButton();
                    numBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    numBtn.setBorder(null);
                    numBtn.setHorizontalAlignment(SwingConstants.RIGHT);
                    numBtn.setActionCommand(String
                            .valueOf(actionCommandId));
                    numBtn.setBackground(palletTableColor);
                    numBtn.setForeground(dateFontColor);
                    numBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            JButton source = (JButton) event.getSource();
                            if (source.getText().length() == 0) {
                                return;
                            }
                            dayColorUpdate(true);
                            source.setForeground(todayBackColor);
                            int newDay = Integer.parseInt(source.getText());
                            Calendar cal = getCalendar();
                            cal.set(Calendar.DAY_OF_MONTH, newDay);
                            setDate(cal.getTime());

                            dateDialog.setVisible(false);
                        }
                    });

                    if (j == 0 || j == 6)
                        numBtn.setForeground(weekendFontColor);
                    else
                        numBtn.setForeground(dateFontColor);
                    daysButton[i][j] = numBtn;
                    panel.add(numBtn);
                    actionCommandId++;
                }

            return panel;
        }

        private Calendar getCalendar() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getDate());
            return calendar;
        }

        private int getSelectedYear() {
            return ((Integer) yearSpin.getValue()).intValue();
        }

        private int getSelectedMonth() {
            return ((Integer) monthSpin.getValue()).intValue();
        }

        private void dayColorUpdate(boolean isOldDay) {
            Calendar cal = getCalendar();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int actionCommandId = day - 2 + cal.get(Calendar.DAY_OF_WEEK);
            int i = actionCommandId / 7;
            int j = actionCommandId % 7;
            if (isOldDay) {
                daysButton[i][j].setForeground(dateFontColor);
            } else {
                daysButton[i][j].setForeground(todayBackColor);
            }
        }

        private void reflushWeekAndDay() {
            Calendar cal = getCalendar();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int maxDayNo = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int dayNo = 2 - cal.get(Calendar.DAY_OF_WEEK);
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    String s = "";
                    if (dayNo >= 1 && dayNo <= maxDayNo) {
                        s = String.valueOf(dayNo);
                    }
                    daysButton[i][j].setText(s);
                    dayNo++;
                }
            }
            dayColorUpdate(false);
        }

        public void stateChanged(ChangeEvent e) {
            dayColorUpdate(true);

            JSpinner source = (JSpinner) e.getSource();
            Calendar cal = getCalendar();
            if (source.getName().equals("Year")) {
                cal.set(Calendar.YEAR, getSelectedYear());
            } else {
                cal.set(Calendar.MONTH, getSelectedMonth() - 1);
            }
            setDate(cal.getTime());
            reflushWeekAndDay();
        }
    }
}
