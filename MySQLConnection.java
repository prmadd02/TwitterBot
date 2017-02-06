import java.io.File;
import java.net.URL;
import java.sql.*;

import org.apache.commons.io.FileUtils;

public class MySQLConnection {
	
	private static Connection getCon()
	{
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
			CSVLoader loader = new CSVLoader(getCon());
			
			loader.loadCSV(csvFile, tableName, truncate);
		}
		catch (Exception ex)
		{
			WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
		}
	}
	
	public static void UpdateCrimeData (String tableName, URL URL)
	{
		File updateCSV = new File("update.csv");
		try
		{
			FileUtils.copyURLToFile(URL, updateCSV);
			
			CSV2MySQL("update.csv", tableName + "TEMP", true);
			
			Connection con = getCon();
			
			Statement stmt = con.createStatement();

			stmt.executeUpdate("INSERT INTO " + tableName +
							   " SELECT * FROM " + tableName + "TEMP A" +
							   " WHERE NOT EXISTS (SELECT 1 FROM " + tableName + " X" +
							   " WHERE A.INCIDENT_NUMBER = X.INCIDENT_NUMBER AND" +
							   " A.UOR_DESC = X.UOR_DESC AND X.ID = A.ID);");
		}
		catch (Exception ex)
		{
			WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
		}
	}
}
