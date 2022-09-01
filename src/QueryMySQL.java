import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Performs CREATE, INSERT, DELETE, and SELECT on a MySQL database.
 */
public class QueryMySQL 
{
	/**
	 * Connection to database
	 */
	private Connection con;
	
	
	/**
	 * Main method is only used for convenience.  Use JUnit test file to verify your answer.
	 * 
	 * @param args
	 * 		none expected
	 * @throws SQLException
	 * 		if a database error occurs
	 */
	public static void main(String[] args) throws SQLException
	{
		QueryMySQL q = new QueryMySQL();
		q.connect();	
		q.drop();
		q.create();
		q.insert();	
		q.delete();		
		System.out.println(QueryMySQL.resultSetToString(q.query1(), 1000));
		System.out.println(QueryMySQL.resultSetToString(q.query2(), 1000));
		System.out.println(QueryMySQL.resultSetToString(q.query3(), 1000));
		q.close();
	}

	/**
	 * Makes a connection to the database and returns connection to caller.
	 * 
	 * @return
	 * 		connection
	 * @throws SQLException
	 * 		if an error occurs
	 */
	public Connection connect() throws SQLException
	{
		String url = "jdbc:mysql://localhost/lab1";
		String uid = "testuser";
		String pw = "404testpw";
		
		System.out.println("Connecting to database.");		
		// Note: Must assign connection to instance variable as well as returning it back to the caller
		// TODO: Make a connection to the database and store connection in con variable before returning it.
		return null;	                       
	}
	
	/**
	 * Closes connection to database.
	 */
	public void close()
	{
		System.out.println("Closing database connection.");

		// TODO: Close the database connection.  Catch any exception and print out if it occurs.				
	}
	
	/**
	 * Drops the table from the database.  If table does not exist, error is ignored.
	 */
	public void drop()
	{
		System.out.println("Dropping table person.");

		// TODO: Drop the table person.  Catch any exception and print out if it occurs.				
	}
	
	/**
	 * Creates the table in the database.  Table name: person
	 * Fields:
	 *  - id - integer, must auto-increment
	 *  - name - variable character field up to size 40
	 *  - salary - must hold up to 99,999,999.99 exactly
	 *  - birthdate - date
	 *  - last_update - datetime
	 */
	public void create() throws SQLException
	{
		System.out.println("Creating table person.");
		// TODO: Create the table person.			
	}
	
	/**
	 * Inserts the test records in the database.  Must used a PreparedStatement.  
	 * 
	 * Data:
	 * Names = "Ann Alden", "Bob Baron", "Chloe Cat", "Don Denton", "Eddy Edwards"
	 * Salaries = "123000", "225423", "99999999.99", "91234.24", "55125125.25"
	 * Birthdates = "1986-03-04", "1993-12-02", "1999-01-15", "2004-08-03", "2003-05-17"
	 * Last_updates = "2022-01-04 11:30:30", "2022-01-04 12:30:25", "2022-01-04 12:25:45", "2022-01-04 12:45:00", "2022-01-05 23:00:00"
	 */
	public void insert() throws SQLException
	{
		System.out.println("Inserting records.");
		// TODO: Insert records using a PreparedStatement.		
	}
	
	/**
	 * Delete the row where person name is 'Bob Baron'.
	 * 
	 * @return
	 * 		number of rows deleted
	 * @throws SQLException
	 * 		if an error occurs
	 */
	public int delete() throws SQLException
	{
		System.out.println("Deleting a record.");
		// TODO: Delete record where name is 'Bob Baron'
		return 0;	
	}
	
	/**
	 * Query returns the person name and salary where rows are sorted by salary descending.
	 * 
	 * @return
	 * 		ResultSet
	 * @throws SQLException
	 * 		if an error occurs
	 */
	public ResultSet query1() throws SQLException
	{
		System.out.println("Executing query #1.");
		// TODO: Write SQL query
		return null;		
	}
	
	/**
	 * Query returns the person last name and salary if the person's salary is greater than the average salary of all people.
	 * 
	 * @return
	 * 		ResultSet
	 * @throws SQLException
	 * 		if an error occurs
	 */
	public ResultSet query2() throws SQLException
	{
		System.out.println("Executing query #2.");
		// TODO: Write SQL query
		return null;
	}
	
	/**
	 * Query returns all fields of a pair of people where a pair of people is returned if the last_update field of their records have been updated less than an hour apart.
	 * Do not duplicate pairs.  Example: Only show (Ann, Bob) and not also (Bob, Ann).
	 * 
	 * @return
	 * 		ResultSet
	 * @throws SQLException
	 * 		if an error occurs
	 */
	public ResultSet query3() throws SQLException
	{
		System.out.println("Executing query #3.");
		// TODO: Write SQL query
		return null;
	}
	
	/*
	 * Do not change anything below here.
	 */
	/**
     * Converts a ResultSet to a string with a given number of rows displayed.
     * Total rows are determined but only the first few are put into a string.
     * 
     * @param rst
     * 		ResultSet
     * @param maxrows
     * 		maximum number of rows to display
     * @return
     * 		String form of results
     * @throws SQLException
     * 		if a database error occurs
     */    
    public static String resultSetToString(ResultSet rst, int maxrows) throws SQLException
    {                       
        StringBuffer buf = new StringBuffer(5000);
        int rowCount = 0;
        ResultSetMetaData meta = rst.getMetaData();
        buf.append("Total columns: " + meta.getColumnCount());
        buf.append('\n');
        if (meta.getColumnCount() > 0)
            buf.append(meta.getColumnName(1));
        for (int j = 2; j <= meta.getColumnCount(); j++)
            buf.append(", " + meta.getColumnName(j));
        buf.append('\n');
                
        while (rst.next()) 
        {
            if (rowCount < maxrows)
            {
                for (int j = 0; j < meta.getColumnCount(); j++) 
                { 
                	Object obj = rst.getObject(j + 1);                	 	                       	                                	
                	buf.append(obj);                    
                    if (j != meta.getColumnCount() - 1)
                        buf.append(", ");                    
                }
                buf.append('\n');
            }
            rowCount++;
        }            
        buf.append("Total results: " + rowCount);
        return buf.toString();
    }
    
    /**
     * Converts ResultSetMetaData into a string.
     * 
     * @param meta
     * 		 ResultSetMetaData
     * @return
     * 		string form of metadata
     * @throws SQLException
     * 		if a database error occurs
     */
    public static String resultSetMetaDataToString(ResultSetMetaData meta) throws SQLException
    {
	    StringBuffer buf = new StringBuffer(5000);                                   
	    buf.append(meta.getColumnName(1)+" ("+meta.getColumnLabel(1)+", "+meta.getColumnType(1)+"-"+meta.getColumnTypeName(1)+", "+meta.getColumnDisplaySize(1)+", "+meta.getPrecision(1)+", "+meta.getScale(1)+")");
	    for (int j = 2; j <= meta.getColumnCount(); j++)
	        buf.append(", "+meta.getColumnName(j)+" ("+meta.getColumnLabel(j)+", "+meta.getColumnType(j)+"-"+meta.getColumnTypeName(j)+", "+meta.getColumnDisplaySize(j)+", "+meta.getPrecision(j)+", "+meta.getScale(j)+")");
	    return buf.toString();
    }
}
