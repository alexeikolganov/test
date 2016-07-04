package abego;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteJDBC
{
	static List<Contact> contacts = new ArrayList<Contact>();	
  
	public static List<Contact> getContacts( String query )
	{
		List<Contact> contacts = new ArrayList<Contact>();
		Connection c   = null;
	    Statement stmt = null;
	    ResultSet rs   = null;
	    try 
	    {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:test.db");
	    	c.setAutoCommit(false);

	    	stmt = c.createStatement();
	    	rs = stmt.executeQuery( query );
	    	while ( rs.next() ) 
			{
				int contactId = rs.getInt("contact_id");	
				String contactName = rs.getString("contact_name");
				contacts.add( new Contact( contactId, contactName ) );
			}
	    	rs.close();
	    	stmt.close();
	    	c.close();
	    } 
	    catch ( Exception e ) 
	    {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    	System.exit(0);
	    }
	    return contacts;
	}
	
	
/*	public static List<Contact> getData( String query )
  {
	  
	  Connection c = null;
	    Statement stmt = null;
	    try 
	    {
	    	
	    	
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:test.db");
	    	c.setAutoCommit(false);

	    	stmt = c.createStatement();
	    	ResultSet rs = stmt.executeQuery( query );
	    	
	    	System.out.println("0sh: " + rs.getString("c1_name"));
	    	Contact contact = new Contact( rs.getString("c1_name"), 450, 300, 0, 0, 0, false );
	    	contacts.add( contact );
	    	int x = 0, y = 0;
	    	int noSiblings = 0, noParents = 0, noChildren = 0;
	    	while ( rs.next() ) {
		         String  c1_name = rs.getString("c1_name");
		         String  c2_name = rs.getString("c2_name");
		         int lookup_level  = rs.getInt("lookup_level");
	        
		         if( Contact.getContact( contacts, c2_name ) == null )
		         {
			         Contact linkedContact = Contact.getContact( contacts, c1_name );
			         switch( lookup_level )
			         {
			         	case 0:		         		
			         		x = linkedContact.x - UI.MARGIN - UI.BLOCK_WIDTH/2 + ( linkedContact.noParents*( UI.BLOCK_WIDTH + UI.MARGIN )  ); // prev contact - padding - half block width
			         		y = linkedContact.y - UI.MARGIN - UI.BLOCK_HEIGHT;  // prev contact - padding - block height
			         		noSiblings = 0;
			         		noParents = 0;
			         		noChildren = 1;
			         		linkedContact.noParents++;    			         		
			         		break;
			         	case 1:
			         		x = linkedContact.x + UI.MARGIN + UI.BLOCK_WIDTH; // prev contact + padding
			         		y = linkedContact.y;  // prev contact
			         		linkedContact.noSiblings++; 
			         		noSiblings = 1;
			         		noParents = 0;
			         		noChildren = 0;
			         		break;
			         	case 2:
			         		x = linkedContact.x - UI.BLOCK_WIDTH/2 + ( linkedContact.noChildren*( UI.BLOCK_WIDTH + UI.MARGIN )  ); // prev contact - padding - half block width
			         		y = linkedContact.y + UI.MARGIN + UI.BLOCK_HEIGHT;  // prev contact - padding - block height
			         		linkedContact.noChildren++;
			         		noSiblings = 0;
			         		noParents = 1;
			         		noChildren = 0;
			         		break;
			         }
			         contact = new Contact( rs.getString("c2_name"), x, y, noParents, noChildren, noSiblings, false );
				     contacts.add( contact );
			         System.out.println( "c2_name = " + c2_name + " (" + x + "," + y +"), level = " + lookup_level );
		         }
		         
	    	}
	    	rs.close();
	    	stmt.close();
	    	c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	   return contacts;
  }
 */ 
}