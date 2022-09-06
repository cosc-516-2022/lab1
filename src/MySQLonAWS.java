import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;


/**
 * Performs SQL DDL and SELECT queries on a MySQL database hosted on AWS RDS.
 */
public class MySQLonAWS 
{
	/**
	 * Connection to database
	 */
	private Connection con;
	
	/**
	 * TODO: Fill in AWS connection information.	 
	 */
	private String url = "jdbc:mysql://mysql516.cleccolswdw9.us-east-1.rds.amazonaws.com/lab1";
	private String uid = "admin";
	private String pw = "test516#";

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
		MySQLonAWS q = new MySQLonAWS();
		q.connect();	
		q.drop();
		q.create();
		q.insert();	
		q.delete();		
		System.out.println(MySQLonAWS.resultSetToString(q.query1(), 1000));
		System.out.println(MySQLonAWS.resultSetToString(q.query2(), 1000));
		System.out.println(MySQLonAWS.resultSetToString(q.query3(), 1000));
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
		// TODO: For connect to work you must configure your AWS connection info in the private instance variables at the top of the file. 
		// If connection fails, make sure to modify your VPC rules to along inbound traffic to the database from your IP.
		System.out.println("Connecting to database.");
		// Note: Must assign connection to instance variable as well as returning it back to the caller
		con = DriverManager.getConnection(url, uid, pw);
		return con;		                       
	}
	

	/**
	 * Makes a connection using SSL. Returns connection to caller.
	 * 
	 * @return
	 * 		connection
	 * @throws SQLException
	 * 		if an error occurs
	 */
	public Connection connectSSL() throws SQLException
	{
		String urlSSL = url+"ssl";
				
		System.out.println("Connecting to database.");
		// Note: Must assign connection to instance variable as well as returning it back to the caller
		con = DriverManager.getConnection(urlSSL, uid, pw);
		return con;		                       
	}


	/**
	 * Closes connection to database.
	 */
	public void close()
	{
		System.out.println("Closing database connection.");
		try
		{
			if (con != null)
	            con.close();
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	/**
	 * Drops the tables from the database.  If a table does not exist, error is ignored. Drop stockprice first.
	 */
	public void drop()
	{
		System.out.println("Dropping table stockprice.");
		try
		{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS stockprice");			
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		System.out.println("Dropping table company.");
		try
		{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS company");			
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}		
	}
	
	/**
	 * Creates the table in the database.  
	 * Table name: company
	 * Fields:
	 *  - id - integer, must be primary key
	 *  - name - variable character field up to size 50
	 *  - ticker - character field always of size 10
	 *  - annualRevenue - must hold up to 999,999,999,999.99 exactly
	 *  - numEmployees - integer	 
	 * 
	 * Table name: stockprice
	 * Fields:
	 *  - companyId - integer
	 *  - priceDate - date of stock price
	 *  - openPrice - opening price must hold up to 99999999.99
	 *  - highPrice - high price must hold up to 99999999.99
	 *  - lowPrice - low price must hold up to 99999999.99
	 *  - closePrice - closing price must hold up to 99999999.99
	 *  - volume - number of shares traded, integer
	 *  - primary key must be companyId and priceDate
	 *  - add an appropriate foreign key
	 */
	public void create() throws SQLException
	{
		System.out.println("Creating table company.");
		Statement stmt = con.createStatement();
		stmt.executeUpdate("CREATE TABLE company ( "
							+" id integer primary key,"
							+" name varchar(50),"
							+" ticker char(10),"
							+" annualRevenue decimal(14,2),"
							+" numEmployees integer)");

							
		System.out.println("Creating table stockprice.");		
		stmt.executeUpdate("CREATE TABLE stockprice ( "
							+" companyId integer,"
							+" priceDate date,"
							+" openPrice decimal(10,2),"
							+" highPrice decimal(10,2),"
							+" lowPrice decimal(10,2),"	
							+" closePrice decimal(10,2),"	
							+" volume integer,"
							+" primary key (companyId, priceDate)," 
							+" foreign key (companyId) references company(id))");							
	}
	
	/**
	 * Inserts the test records in the database.  Must used a PreparedStatement.  
	 * 
	 * Data for company table: 
	 * 1, 'Apple', 'AAPL', 387540000000.00 , 154000
	 * 2, 'GameStop', 'GME', 611000000.00, 12000
	 * 3, 'Handy Repair', null, 2000000, 50
	 * 4, 'Microsoft', 'MSFT', '198270000000.00' , 221000
	 * 5, 'StartUp', null, 50000, 3
	 * 	 
	 * Data for stockprice table:
		1, '2022-08-15', 171.52, 173.39, 171.35, 173.19, 54091700
		1, '2022-08-16', 172.78, 173.71, 171.66, 173.03, 56377100
		1, '2022-08-17', 172.77, 176.15, 172.57, 174.55, 79542000
		1, '2022-08-18', 173.75, 174.90, 173.12, 174.15, 62290100
		1, '2022-08-19', 173.03, 173.74, 171.31, 171.52, 70211500
		1, '2022-08-22', 169.69, 169.86, 167.14, 167.57, 69026800
		1, '2022-08-23', 167.08, 168.71, 166.65, 167.23, 54147100
		1, '2022-08-24', 167.32, 168.11, 166.25, 167.53, 53841500
		1, '2022-08-25', 168.78, 170.14, 168.35, 170.03, 51218200
		1, '2022-08-26', 170.57, 171.05, 163.56, 163.62, 78823500
		1, '2022-08-29', 161.15, 162.90, 159.82, 161.38, 73314000
		1, '2022-08-30', 162.13, 162.56, 157.72, 158.91, 77906200
		2, '2022-08-15', 39.75,	40.39, 38.81, 39.68, 5243100
		2, '2022-08-16', 39.17,	45.53, 38.60, 42.19, 23602800
		2, '2022-08-17', 42.18,	44.36, 40.41, 40.52, 9766400
		2, '2022-08-18', 39.27,	40.07, 37.34, 37.93, 8145400
		2, '2022-08-19', 35.18,	37.19, 34.67, 36.49, 9525600
		2, '2022-08-22', 34.31,	36.20, 34.20, 34.50, 5798600
		2, '2022-08-23', 34.70,	34.99, 33.45, 33.53, 4836300
		2, '2022-08-24', 34.00,	34.94, 32.44, 32.50, 5620300
		2, '2022-08-25', 32.84,	32.89, 31.50, 31.96, 4726300
		2, '2022-08-26', 31.50,	32.38, 30.63, 30.94, 4289500
		2, '2022-08-29', 30.48,	32.75, 30.38, 31.55, 4292700
		2, '2022-08-30', 31.62,	31.87, 29.42, 29.84, 5060200
		4, '2022-08-15', 291.00, 294.18, 290.11, 293.47, 18085700
		4, '2022-08-16', 291.99, 294.04, 290.42, 292.71, 18102900
		4, '2022-08-17', 289.74, 293.35, 289.47, 291.32, 18253400
		4, '2022-08-18', 290.19, 291.91, 289.08, 290.17, 17186200
		4, '2022-08-19', 288.90, 289.25, 285.56, 286.15, 20557200
		4, '2022-08-22', 282.08, 282.46, 277.22, 277.75, 25061100
		4, '2022-08-23', 276.44, 278.86, 275.40, 276.44, 17527400
		4, '2022-08-24', 275.41, 277.23, 275.11, 275.79, 18137000
		4, '2022-08-25', 277.33, 279.02, 274.52, 278.85, 16583400
		4, '2022-08-26', 279.08, 280.34, 267.98, 268.09, 27532500
		4, '2022-08-29', 265.85, 267.40, 263.85, 265.23, 20338500
		4, '2022-08-30', 266.67, 267.05, 260.66, 262.97, 22767100
	 */
	public void insert() throws SQLException
	{
		System.out.println("Inserting records.");
		
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO company (id, name, ticker, annualRevenue, numEmployees) VALUES (?, ?, ?, ?, ?)");
		
		Integer []id = new Integer[]{1, 2, 3, 4, 5};				
		String[] name = new String[]{"Apple", "GameStop", "Handy Repair", "Microsoft", "StartUp"};
		String[] ticker = new String[]{"AAPL", "GME", null, "MSFT", null};
		BigDecimal[] revenue = new BigDecimal[]{new BigDecimal("387540000000.00"), new BigDecimal("611000000.00"), new BigDecimal("2000000"), new BigDecimal("198270000000.00"), new BigDecimal("50000")};
		Integer []emp = new Integer[]{154000, 12000, 50, 221000, 3};	

		for (int i=0; i < id.length; i++)
		{
			pstmt.setInt(1, id[i]);
			pstmt.setString(2,  name[i]);
			pstmt.setString(3,  ticker[i]);
			pstmt.setBigDecimal(4, revenue[i]);
			pstmt.setInt(5, emp[i]);			
			pstmt.executeUpdate();
		}

		String [] prices = new String[]{
			"1, '2022-08-15', 171.52, 173.39, 171.35, 173.19, 54091700",
			"1, '2022-08-16', 172.78, 173.71, 171.66, 173.03, 56377100",
			"1, '2022-08-17', 172.77, 176.15, 172.57, 174.55, 79542000",
			"1, '2022-08-18', 173.75, 174.90, 173.12, 174.15, 62290100",
			"1, '2022-08-19', 173.03, 173.74, 171.31, 171.52, 70211500",
			"1, '2022-08-22', 169.69, 169.86, 167.14, 167.57, 69026800",
			"1, '2022-08-23', 167.08, 168.71, 166.65, 167.23, 54147100",
			"1, '2022-08-24', 167.32, 168.11, 166.25, 167.53, 53841500",
			"1, '2022-08-25', 168.78, 170.14, 168.35, 170.03, 51218200",
			"1, '2022-08-26', 170.57, 171.05, 163.56, 163.62, 78823500",
			"1, '2022-08-29', 161.15, 162.90, 159.82, 161.38, 73314000",
			"1, '2022-08-30', 162.13, 162.56, 157.72, 158.91, 77906200",
			"2, '2022-08-15', 39.75,	40.39, 38.81, 39.68, 5243100",
			"2, '2022-08-16', 39.17,	45.53, 38.60, 42.19, 23602800",
			"2, '2022-08-17', 42.18,	44.36, 40.41, 40.52, 9766400",
			"2, '2022-08-18', 39.27,	40.07, 37.34, 37.93, 8145400",
			"2, '2022-08-19', 35.18,	37.19, 34.67, 36.49, 9525600",
			"2, '2022-08-22', 34.31,	36.20, 34.20, 34.50, 5798600",
			"2, '2022-08-23', 34.70,	34.99, 33.45, 33.53, 4836300",
			"2, '2022-08-24', 34.00,	34.94, 32.44, 32.50, 5620300",
			"2, '2022-08-25', 32.84,	32.89, 31.50, 31.96, 4726300",
			"2, '2022-08-26', 31.50,	32.38, 30.63, 30.94, 4289500",
			"2, '2022-08-29', 30.48,	32.75, 30.38, 31.55, 4292700",
			"2, '2022-08-30', 31.62,	31.87, 29.42, 29.84, 5060200",
			"4, '2022-08-15', 291.00, 294.18, 290.11, 293.47, 18085700",
			"4, '2022-08-16', 291.99, 294.04, 290.42, 292.71, 18102900",
			"4, '2022-08-17', 289.74, 293.35, 289.47, 291.32, 18253400",
			"4, '2022-08-18', 290.19, 291.91, 289.08, 290.17, 17186200",
			"4, '2022-08-19', 288.90, 289.25, 285.56, 286.15, 20557200",
			"4, '2022-08-22', 282.08, 282.46, 277.22, 277.75, 25061100",
			"4, '2022-08-23', 276.44, 278.86, 275.40, 276.44, 17527400",
			"4, '2022-08-24', 275.41, 277.23, 275.11, 275.79, 18137000",
			"4, '2022-08-25', 277.33, 279.02, 274.52, 278.85, 16583400",
			"4, '2022-08-26', 279.08, 280.34, 267.98, 268.09, 27532500",
			"4, '2022-08-29', 265.85, 267.40, 263.85, 265.23, 20338500",
			"4, '2022-08-30', 266.67, 267.05, 260.66, 262.97, 22767100",
		};

		pstmt = con.prepareStatement("INSERT INTO stockprice (companyId, priceDate, openPrice, highPrice, lowPrice, closePrice, volume) VALUES (?, ?, ?, ?, ?, ?,?)");
		
		// This demonstrates how to parse a string into components then insert
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		for (int i=0; i < prices.length; i++)
		{
			List<String> fields = Arrays.asList(prices[i].split(","));

			pstmt.setInt(1, Integer.parseInt(fields.get(0)));
			try {
				String dtfld = fields.get(1).trim();
				pstmt.setDate(2,  new java.sql.Date(df.parse(dtfld.substring(1,dtfld.length()-1)).getTime()));
			} catch (ParseException e) {	
				pstmt.setNull(2, java.sql.Types.DATE);
				e.printStackTrace();
			}						
			pstmt.setBigDecimal(3, new BigDecimal(fields.get(2).trim()));
			pstmt.setBigDecimal(4, new BigDecimal(fields.get(3).trim()));
			pstmt.setBigDecimal(5, new BigDecimal(fields.get(4).trim()));
			pstmt.setBigDecimal(6, new BigDecimal(fields.get(5).trim()));
			pstmt.setInt(7,  Integer.parseInt(fields.get(6).trim()));			
			pstmt.executeUpdate();
		}
	}
	
	/**
	 * Delete all stock price records where the date is before 2022-08-20 or the company is GameStop.
	 * 
	 * @return
	 * 		number of rows deleted
	 * @throws SQLException
	 * 		if an error occurs
	 */
	public int delete() throws SQLException
	{
		System.out.println("Deleting a record.");
		Statement stmt = con.createStatement();
		return stmt.executeUpdate("DELETE FROM stockprice WHERE priceDate < '2022-08-20' or companyId = (SELECT id FROM company WHERE name = 'GameStop')");			
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
		Statement stmt = con.createStatement();
		return stmt.executeQuery("SELECT name, salary FROM person ORDER BY salary DESC");			
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
		Statement stmt = con.createStatement();
		return stmt.executeQuery("SELECT substr(name, locate(' ',name)+1) as lastname, salary FROM person WHERE salary > (SELECT AVG(salary) FROM person)");			
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
		Statement stmt = con.createStatement();
		return stmt.executeQuery("SELECT * FROM person P1, person P2 where hour(timediff(P1.last_update, P2.last_update)) < 1 and P1.id < P2.id");	
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
