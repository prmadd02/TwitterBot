package Utility;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;

public class MySQLConnection {
	
	private static Connection getCon()
	{
		//Getting connection to MySQL Database
		Connection connection = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			connection = DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/twitterbot","root","root");
		}
		catch (Exception ex)
		{
			WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
		}
		
		return connection;
	}
	
	public static void CSV2MySQL (String csvFile, String tableName, boolean truncate)
	{
		try
		{
			//loading csv file to database
			CSVLoader loader = new CSVLoader(getCon());
			
			loader.loadCSV(csvFile, tableName, truncate);
			
			//Connections handled in the CSVLoader class
		}
		catch (Exception ex)
		{
			WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
		}
		
				
	}
	
	public static void StoreStatusIDs (String ZipOrWea,long FirstID, long LastID)
	{
		Connection con = null;
		Statement stmt = null;
		
		if (ZipOrWea == "Weather")
		{
			try
			{
				con = getCon();
		
				stmt = con.createStatement();

				stmt.executeUpdate("UPDATE STATUSID " +
							   	   "SET WeaFirstID = " + FirstID + ", " +
							       "WeaLastID = " + LastID + ";");
			} 
			catch (Exception ex)
			{
				WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
			}
		
			//Close all Connections
			finally
			{
				if (stmt != null) 
				{
					try 
					{
						stmt.close();
					} 
					catch (SQLException e) 
					{ /* ignored */}
				}
			
				if (con != null)
				{
					try 
					{
						con.close();
					}
					catch (SQLException e)
					{ /* ignored */}
				}
			}
		}
		if (ZipOrWea == "Zip")
		{
				try
				{
					con = getCon();
			
					stmt = con.createStatement();

					stmt.executeUpdate("UPDATE STATUSID " +
								   	   "SET ZipFirstID = " + FirstID + ", " +
								       "ZipLastID = " + LastID + ";");
				} 
				catch (Exception ex)
				{
					WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
				}
			
				//Close all Connections
				finally
				{
					if (stmt != null) 
					{
						try 
						{
							stmt.close();
						} 
						catch (SQLException e) 
						{ /* ignored */}
					}
				
					if (con != null)
					{
						try 
						{
							con.close();
						}
						catch (SQLException e)
						{ /* ignored */}
					}
				}
		}
		
	}
	
	public static long CollectFirstID(String ZipOrWea)
	{
		long id = 0L;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		if (ZipOrWea == "Weather")
		{
			try
			{
				con = getCon();
		
				stmt = con.createStatement();

				rs = stmt.executeQuery("SELECT WeaFirstID FROM STATUSID;");
			
				if(rs.next()) 
				{ 
					id = rs.getLong("WeaFirstID") + 'L';
				}
			
				return id;
			} 
		
			catch (Exception ex)
			{
				WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
			
				return id;
			}
		
			//Close all Connections
			finally
			{
				if (rs != null) 
				{
					try 
					{
						rs.close();
					} 
					catch (SQLException e) 
					{ /* ignored */}
				}
			
				if (stmt != null) 
				{
					try 
					{
						stmt.close();
					} 
					catch (SQLException e) 
					{ /* ignored */}
				}
			
				if (con != null)
				{
					try 
					{
						con.close();
					}
					catch (SQLException e)
					{ /* ignored */}
				}
			}
		}
		
		else
		{
			try
			{
				con = getCon();
		
				stmt = con.createStatement();

				rs = stmt.executeQuery("SELECT ZipFirstID FROM STATUSID;");
			
				if(rs.next()) 
				{ 
					id = rs.getLong("ZipFirstID") + 'L';
				}
			
				return id;
			} 
		
			catch (Exception ex)
			{
				WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
			
				return id;
			}
		
			//Close all Connections
			finally
			{
				if (rs != null) 
				{
					try 
					{
						rs.close();
					} 
					catch (SQLException e) 
					{ /* ignored */}
				}
			
				if (stmt != null) 
				{
					try 
					{
						stmt.close();
					} 
					catch (SQLException e) 
					{ /* ignored */}
				}
			
				if (con != null)
				{
					try 
					{
						con.close();
					}
					catch (SQLException e)
					{ /* ignored */}
				}
			}
		}
		
		
	}
	
	public static Connection getConnection()
	{
		return getCon();
	}
	
}
