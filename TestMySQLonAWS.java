import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

public class TestMySQLonAWS 
{
	/**
	 * Class being tested
	 */
	private static MySQLonAWS q;
	
	/**
	 * Connection to the database
	 */
	private static Connection con;
	
	/**
	 * Database name used	 
	 */
	private static String databaseName = "lab1";

	/**
	 * Requests a connection to the database.
	 * 
	 * @throws Exception
	 * 		if an error occurs
	 */
	@BeforeAll
	public static void init() throws Exception 
	{		
		q = new MySQLonAWS();
		con = q.connect();					
	}
	
	/**
	 * Closes connection.
	 * 
	 * @throws Exception
	 * 		if an error occurs
	 */
	@AfterAll
	public static void end() throws Exception 
	{
		q.close();
        
	}
	
	/**
     * Tests SSL connection
     */
    @Test
    public void testConnectSSL() 
    {   
		try
    	{ 		
    		Connection c = q.connectSSL();
			if (c == null)
				fail("FAIL: No connection");
			Statement stmt = c.createStatement();
			
	    	ResultSet rst = stmt.executeQuery("SELECT * FROM performance_schema.session_status WHERE VARIABLE_NAME IN ('Ssl_version','Ssl_cipher')");

			String answer = "Total columns: 2"
							+"\nVARIABLE_NAME, VARIABLE_VALUE"
							+"\nSsl_cipher, TLS_AES_256_GCM_SHA384"
							+"\nSsl_version, TLSv1.3"
							+"\nTotal results: 2";
			String queryResult = MySQLonAWS.resultSetToString(rst, 100);
    		System.out.println(queryResult);
    		assertEquals(answer, queryResult);   
			c.close();
		}
		catch (SQLException e)
		{
			System.out.println(e);
			fail("SSL connection failed");
		}    	
    }

	/**
     * Tests drop command.
     */
    @Test
    public void testDrop() 
    {    		
    	q.drop();
    	
    	// See if table exists
    	try
    	{
			if (con == null)
				fail("FAIL: No connection");			
	    	Statement stmt = con.createStatement();
			stmt.execute("USE "+databaseName);
	    	stmt.executeQuery("SELECT * FROM company");
	    	fail("Table company exists and should be dropped!");
			stmt.executeQuery("SELECT * FROM stockprice");
	    	fail("Table stockprice exists and should be dropped!");
    	}
    	catch (SQLException e)
    	{
    		System.out.println(e);
    	}
    }
    
    /**
     * Tests create.
     */
    @Test
    public void testCreate() throws SQLException
    {   
    	q.drop();
    	q.create();
    	
    	// See if table exists
    	try
    	{
			if (con == null)
				fail("FAIL: No connection");			
	    	Statement stmt = con.createStatement();
			stmt.execute("USE "+databaseName);
	    	ResultSet rst = stmt.executeQuery("SELECT * FROM company");	    	
	    	
	    	// Verify its metadata
	    	ResultSetMetaData rsmd = rst.getMetaData();
	    	String st = MySQLonAWS.resultSetMetaDataToString(rsmd);
	    	System.out.println(st);	    			
	    	assertEquals("id (id, 4-INT, 10, 10, 0), name (name, 12-VARCHAR, 50, 50, 0), ticker (ticker, 1-CHAR, 10, 10, 0), annualRevenue (annualRevenue, 3-DECIMAL, 14, 14, 2), numEmployees (numEmployees, 4-INT, 10, 10, 0)", st);	 
						
			rst = stmt.executeQuery("SELECT * FROM stockprice");	    		    	
	    	// Verify its metadata
	    	rsmd = rst.getMetaData();
	    	st = MySQLonAWS.resultSetMetaDataToString(rsmd);
	    	System.out.println(st);	    			
	    	assertEquals("companyId (companyId, 4-INT, 10, 10, 0), priceDate (priceDate, 91-DATE, 10, 10, 0), openPrice (openPrice, 3-DECIMAL, 10, 10, 2), highPrice (highPrice, 3-DECIMAL, 10, 10, 2), lowPrice (lowPrice, 3-DECIMAL, 10, 10, 2), closePrice (closePrice, 3-DECIMAL, 10, 10, 2), volume (volume, 4-INT, 10, 10, 0)", st);
    	}
    	catch (SQLException e)
    	{
    		System.out.println(e);
    		fail("Table person does not exist!");
    	}
    }
    
    /**
     * Tests insert.
     */
    @Test
    public void testInsert() throws SQLException
    {    
		if (con == null)
			fail("FAIL: No connection");			

    	q.drop();
    	q.create();
    	q.insert();
    	
    	// Verify data was inserted properly
    	String answer = "Total columns: 5"		
						+"\nid, name, ticker, annualRevenue, numEmployees"
						+"\n1, Apple, AAPL, 387540000000.00, 154000"
						+"\n2, GameStop, GME, 611000000.00, 12000"
						+"\n3, Handy Repair, null, 2000000.00, 50"
						+"\n4, Microsoft, MSFT, 198270000000.00, 221000"
						+"\n5, StartUp, null, 50000.00, 3"
						+"\nTotal results: 5";
				
    	runSQLQuery("SELECT * FROM company", answer);    	
		  
		answer = "Total columns: 7"
				+"\ncompanyId, priceDate, openPrice, highPrice, lowPrice, closePrice, volume"
				+"\n1, 2022-08-15, 171.52, 173.39, 171.35, 173.19, 54091700"
				+"\n1, 2022-08-16, 172.78, 173.71, 171.66, 173.03, 56377100"
				+"\n1, 2022-08-17, 172.77, 176.15, 172.57, 174.55, 79542000"
				+"\n1, 2022-08-18, 173.75, 174.90, 173.12, 174.15, 62290100"
				+"\n1, 2022-08-19, 173.03, 173.74, 171.31, 171.52, 70211500"
				+"\n1, 2022-08-22, 169.69, 169.86, 167.14, 167.57, 69026800"
				+"\n1, 2022-08-23, 167.08, 168.71, 166.65, 167.23, 54147100"
				+"\n1, 2022-08-24, 167.32, 168.11, 166.25, 167.53, 53841500"
				+"\n1, 2022-08-25, 168.78, 170.14, 168.35, 170.03, 51218200"
				+"\n1, 2022-08-26, 170.57, 171.05, 163.56, 163.62, 78823500"
				+"\n1, 2022-08-29, 161.15, 162.90, 159.82, 161.38, 73314000"
				+"\n1, 2022-08-30, 162.13, 162.56, 157.72, 158.91, 77906200"
				+"\n2, 2022-08-15, 39.75, 40.39, 38.81, 39.68, 5243100"
				+"\n2, 2022-08-16, 39.17, 45.53, 38.60, 42.19, 23602800"
				+"\n2, 2022-08-17, 42.18, 44.36, 40.41, 40.52, 9766400"
				+"\n2, 2022-08-18, 39.27, 40.07, 37.34, 37.93, 8145400"
				+"\n2, 2022-08-19, 35.18, 37.19, 34.67, 36.49, 9525600"
				+"\n2, 2022-08-22, 34.31, 36.20, 34.20, 34.50, 5798600"
				+"\n2, 2022-08-23, 34.70, 34.99, 33.45, 33.53, 4836300"
				+"\n2, 2022-08-24, 34.00, 34.94, 32.44, 32.50, 5620300"
				+"\n2, 2022-08-25, 32.84, 32.89, 31.50, 31.96, 4726300"
				+"\n2, 2022-08-26, 31.50, 32.38, 30.63, 30.94, 4289500"
				+"\n2, 2022-08-29, 30.48, 32.75, 30.38, 31.55, 4292700"
				+"\n2, 2022-08-30, 31.62, 31.87, 29.42, 29.84, 5060200"
				+"\n4, 2022-08-15, 291.00, 294.18, 290.11, 293.47, 18085700"
				+"\n4, 2022-08-16, 291.99, 294.04, 290.42, 292.71, 18102900"
				+"\n4, 2022-08-17, 289.74, 293.35, 289.47, 291.32, 18253400"
				+"\n4, 2022-08-18, 290.19, 291.91, 289.08, 290.17, 17186200"
				+"\n4, 2022-08-19, 288.90, 289.25, 285.56, 286.15, 20557200"
				+"\n4, 2022-08-22, 282.08, 282.46, 277.22, 277.75, 25061100"
				+"\n4, 2022-08-23, 276.44, 278.86, 275.40, 276.44, 17527400"
				+"\n4, 2022-08-24, 275.41, 277.23, 275.11, 275.79, 18137000"
				+"\n4, 2022-08-25, 277.33, 279.02, 274.52, 278.85, 16583400"
				+"\n4, 2022-08-26, 279.08, 280.34, 267.98, 268.09, 27532500"
				+"\n4, 2022-08-29, 265.85, 267.40, 263.85, 265.23, 20338500"
				+"\n4, 2022-08-30, 266.67, 267.05, 260.66, 262.97, 22767100"
				+"\nTotal results: 36";

		runSQLQuery("SELECT * FROM stockprice", answer);  
    }
    
    /**
     * Tests delete.  
     */
    @Test
    public void testDelete() throws SQLException
    {    
		if (con == null)
			fail("FAIL: No connection");			
    	q.drop();
    	q.create();
    	q.insert();
    	q.delete();
    	
    	// Verify data was deleted properly
    	String answer = "Total columns: 7"		
						+"\ncompanyId, priceDate, openPrice, highPrice, lowPrice, closePrice, volume"
						+"\n1, 2022-08-22, 169.69, 169.86, 167.14, 167.57, 69026800"
						+"\n1, 2022-08-23, 167.08, 168.71, 166.65, 167.23, 54147100"
						+"\n1, 2022-08-24, 167.32, 168.11, 166.25, 167.53, 53841500"
						+"\n1, 2022-08-25, 168.78, 170.14, 168.35, 170.03, 51218200"
						+"\n1, 2022-08-26, 170.57, 171.05, 163.56, 163.62, 78823500"
						+"\n1, 2022-08-29, 161.15, 162.90, 159.82, 161.38, 73314000"
						+"\n1, 2022-08-30, 162.13, 162.56, 157.72, 158.91, 77906200"
						+"\n4, 2022-08-22, 282.08, 282.46, 277.22, 277.75, 25061100"
						+"\n4, 2022-08-23, 276.44, 278.86, 275.40, 276.44, 17527400"
						+"\n4, 2022-08-24, 275.41, 277.23, 275.11, 275.79, 18137000"
						+"\n4, 2022-08-25, 277.33, 279.02, 274.52, 278.85, 16583400"
						+"\n4, 2022-08-26, 279.08, 280.34, 267.98, 268.09, 27532500"
						+"\n4, 2022-08-29, 265.85, 267.40, 263.85, 265.23, 20338500"
						+"\n4, 2022-08-30, 266.67, 267.05, 260.66, 262.97, 22767100"
						+"\nTotal results: 14";
				
    	runSQLQuery("SELECT * FROM stockprice", answer);    	
    }
    
    /**
     * Tests first query.
     */
    @Test
    public void testQuery1() throws SQLException
    {    
    	q.drop();
    	q.create();
    	q.insert();    	
    	
    	ResultSet rst = q.query1();
    	
    	// Verify result
		String answer = "Total columns: 3"
						+"\nname, annualRevenue, numEmployees"
						+"\nApple, 387540000000.00, 154000"
						+"\nGameStop, 611000000.00, 12000"
						+"\nMicrosoft, 198270000000.00, 221000"
						+"\nStartUp, 50000.00, 3"
						+"\nTotal results: 4";
    	String queryResult = MySQLonAWS.resultSetToString(rst, 100);
    	System.out.println(queryResult);
    	assertEquals(answer, queryResult);    	    
    }
    
    /**
     * Tests second query.
     */
    @Test
    public void testQuery2() throws SQLException
    {    
    	q.drop();
    	q.create();
    	q.insert();    	
    	
    	ResultSet rst = q.query2();
    	
    	// Verify result
    	String answer = "Total columns: 6"		
						+"\nname, ticker, rangeLow, rangeHigh, avgClose, avgVolume"
						+"\nApple, AAPL, 163.56, 171.05, 167.196000, 61411420.0000"
						+"\nMicrosoft, MSFT, 267.98, 282.46, 275.384000, 20968280.0000"
						+"\nGameStop, GME, 30.63, 36.20, 32.686000, 5054200.0000"
						+"\nTotal results: 3";						
	
    	String queryResult = MySQLonAWS.resultSetToString(rst, 100);
    	System.out.println(queryResult);
    	assertEquals(answer, queryResult);   
    }
    
    /**
     * Tests third query.
     */
    @Test
    public void testQuery3() throws SQLException
    {    
    	q.drop();
    	q.create();
    	q.insert();    	
    	
    	ResultSet rst = q.query3();
    	
    	// Verify result
    	String answer = "Total columns: 3"
						+"\nname, ticker, closePrice"
						+"\nApple, AAPL, 158.91"
						+"\nHandy Repair, null, null"
						+"\nStartUp, null, null"
						+"\nTotal results: 3";
				
    	String queryResult = MySQLonAWS.resultSetToString(rst, 100);
    	System.out.println(queryResult);
    	assertEquals(answer, queryResult);   
    }    
    
    /**
     * Runs an SQL query and compares answer to expected answer.  
     * 
     * @param sql
     * 		SQL query
     * @param answer
     * 		expected answer          
     */
    public static void runSQLQuery(String sql, String answer)
    {    	 
         try
         {
        	Statement stmt = con.createStatement();
 	    	ResultSet rst = stmt.executeQuery(sql);	    	
 	    	
 	    	String st = MySQLonAWS.resultSetToString(rst, 1000);
 	    	System.out.println(st);	    			
 	    		
 	    	assertEquals(answer, st);	           	             
            
 	    	stmt.close();
         }            
         catch (SQLException e)
         {	
        	 System.out.println(e);
        	 fail("Incorrect exception: "+e);
         }              
    }
}
