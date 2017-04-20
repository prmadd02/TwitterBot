package TableUpdate;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;

import Utility.WritingToFile;
import Utility.MySQLConnection;

public class CrimeData extends TimerTask {
	
		
			//how to update databases
			File updateCSV = new File("update.csv");
			Connection con = null;
			Statement stmt = null;
									
			public void run()
			{
				System.out.println("LouCrimeUpdate Start");
				try
				{
					//Louisville Data Portal Location
					//URL changes with each update
					URL myURL = new URL("https://data.louisvilleky.gov/node/9061/download");
					
					//Copy file from Louisville Data Portal
					FileUtils.copyURLToFile(myURL, updateCSV);
					
					//Put file into Temp CrimeDatabase
					MySQLConnection.CSV2MySQL("update.csv", "CRIMEDATATEMP", true);
			
					con = MySQLConnection.getConnection();
			
					stmt = con.createStatement();
					
					//Merge Temp database with Live Database
					stmt.executeUpdate("INSERT INTO CRIMEDATA" +
								   " SELECT * FROM CRIMEDATATEMP A" +
							       " WHERE NOT EXISTS (SELECT 1 FROM CRIMEDATA X" +
							       " WHERE A.INCIDENT_NUMBER = X.INCIDENT_NUMBER AND" +
							   	   " A.UOR_DESC = X.UOR_DESC AND X.ID = A.ID);");
					
					//Log the update
					WritingToFile.CSVFile("InfoLog.csv", "Crime Database Updated", "", "", "", "");
					
					//Print to Console
					System.out.println("Crime Data Update Done");
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