package abego;

import java.util.List;

public class Relation 
{

	public static Contact getContact( int contactId )
	{
		String query = 	"SELECT  contact_id, contact_name " +
						"FROM contacts " + 
						"WHERE contact_id = " + contactId + ";";
		
		List<Contact> contacts = SQLiteJDBC.getContacts( query );
		
		return contacts.get( 0 );
	}
	
	public static List<Contact> getCommonChildren( int contactId, int spouseContactId )
	{
		String query = 	"SELECT contact_id, contact_name " +
						"FROM contacts " +
						"JOIN contact_reln " +
						"ON contacts.contact_id = contact_reln.object_contact_id " +
						"AND contact_reln.subject_contact_id = " + contactId + " " +
						"AND contact_reln.lookup_id IN ( 5, 6 ) " +
						"JOIN contact_reln spouse_reln " +
						"ON contacts.contact_id = spouse_reln.object_contact_id " +
						"AND spouse_reln.subject_contact_id = " + spouseContactId + " " +
						"AND spouse_reln.lookup_id IN ( 5, 6 )";
		
		List<Contact> contacts = SQLiteJDBC.getContacts( query );
		
		return contacts;
	}
	
	public static Contact getSpouse( int contactId )
	{
		return getRelatedContact( contactId, 3 );
	}
	
	public static List<Contact> getSpouses( int contactId )
	{
		return getRelatedContacts( contactId, 3 );
	}
	
	public static Contact getMother( int contactId )
	{		
		return getRelatedContact( contactId, 1 );
	}
	public static Contact getFather( int contactId )
	{		
		return getRelatedContact( contactId, 2 );
	}
	
	private static Contact getRelatedContact( int contactId, int lookupId )
	{
		String query = prepareQuery( contactId, lookupId );		
		List<Contact> contacts = SQLiteJDBC.getContacts( query );
		return ( contacts.size() > 0 ) ? contacts.get( 0 ) : null;
	}
	private static List<Contact> getRelatedContacts( int contactId, int lookupId )
	{
		String query = prepareQuery( contactId, lookupId );		
		List<Contact> contacts = SQLiteJDBC.getContacts( query );
		return contacts;
	}
	
	private static String prepareQuery( int contactId, int lookupId )
	{
		return "SELECT  contact_id, contact_name, lookup_value " +
				"FROM contacts " + 
				"JOIN contact_reln " + 
				"ON contacts.contact_id = contact_reln.object_contact_id " +
				"JOIN lookup " + 
				"ON contact_reln.lookup_id = lookup.lookup_id " + 
				"AND contact_reln.subject_contact_id = " + contactId + " " +
				"AND contact_reln.lookup_id = " + lookupId + ";";
	}

}
