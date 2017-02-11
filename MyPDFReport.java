import java.io.FileOutputStream;
import java.io.*;
import java.util.*;
import java.sql.*; 
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;



public class MyPDFReport {
	public static void main(String[] args) throws Exception{
        
        /* Create Connection objects */
        Class.forName ("oracle.jdbc.OracleDriver");
        
        /* DB Connection goes here */
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitterbot","root","root");

        /* Statement object for sending queries to DB */
        Statement stmt = conn.createStatement();
        
        /* Define the SQL query */
        ResultSet query_set = stmt.executeQuery("SELECT * FROM crimedata");
        
        /* Initialize PDF documents - logical objects */
        Document my_pdf_report = new Document();
        PdfWriter.getInstance(my_pdf_report, new FileOutputStream("TwitterBot Record.pdf"));
        my_pdf_report.open();    
        
        //We have fifteen columns in our table
        PdfPTable crime_data_table = new PdfPTable(15);
        //Create other table objects here.
        
        //Create a cell object
        PdfPCell table_cell;
       
        while (query_set.next()) {
        	//Crimedata table to PDF (replicate for other tables)
                        String INCIDENT_NUMBER = query_set.getString("INCIDENT_NUMBER");
                        table_cell=new PdfPCell(new Phrase(INCIDENT_NUMBER));
                        crime_data_table.addCell(table_cell);
                        String DATE_REPORTED=query_set.getString("DATE_REPORTED");
                        table_cell=new PdfPCell(new Phrase(DATE_REPORTED));
                        crime_data_table.addCell(table_cell);
                        String DATE_OCCURED=query_set.getString("DATE_OCCURED");
                        table_cell=new PdfPCell(new Phrase(DATE_OCCURED));
                        crime_data_table.addCell(table_cell);
                        String UOR_DESC=query_set.getString("UOR_DESC");
                        table_cell=new PdfPCell(new Phrase(UOR_DESC));
                        crime_data_table.addCell(table_cell);
                        String CRIME_TYPE = query_set.getString("CRIME_TYPE");
                        table_cell=new PdfPCell(new Phrase(CRIME_TYPE));
                        crime_data_table.addCell(table_cell);
                        String NIBRS_CODE=query_set.getString("NIBRS_CODE");
                        table_cell=new PdfPCell(new Phrase(NIBRS_CODE));
                        crime_data_table.addCell(table_cell);
                        String UCR_HIERARCHY=query_set.getString("UCR_HIERARCHY");
                        table_cell=new PdfPCell(new Phrase(UCR_HIERARCHY));
                        crime_data_table.addCell(table_cell);
                        String ATT_COMP=query_set.getString("ATT_COMP");
                        table_cell=new PdfPCell(new Phrase(ATT_COMP));
                        crime_data_table.addCell(table_cell);
                        String LMPD_DIVISION = query_set.getString("LMPD_DIVISION");
                        table_cell=new PdfPCell(new Phrase(LMPD_DIVISION));
                        crime_data_table.addCell(table_cell);
                        String LMPD_BEAT=query_set.getString("LMPD_BEAT");
                        table_cell=new PdfPCell(new Phrase(LMPD_BEAT));
                        crime_data_table.addCell(table_cell);
                        String PREMISE_TYPE=query_set.getString("PREMISE_TYPE");
                        table_cell=new PdfPCell(new Phrase(PREMISE_TYPE));
                        crime_data_table.addCell(table_cell);
                        String BLOCK_ADDRESS=query_set.getString("BLOCK_ADDRESS");
                        table_cell=new PdfPCell(new Phrase(BLOCK_ADDRESS));
                        crime_data_table.addCell(table_cell);
                        String CITY = query_set.getString("CITY");
                        table_cell=new PdfPCell(new Phrase(CITY));
                        crime_data_table.addCell(table_cell);
                        String ZIP_CODE=query_set.getString("ZIP_CODE");
                        table_cell=new PdfPCell(new Phrase(ZIP_CODE));
                        crime_data_table.addCell(table_cell);
                        String ID=query_set.getString("ID");
                        table_cell=new PdfPCell(new Phrase(ID));
                        crime_data_table.addCell(table_cell);
                        }
        
        
        /* Attach report table to PDF */
        my_pdf_report.add(crime_data_table);                       
        my_pdf_report.close();
        
        /* Close all DB related objects */
        query_set.close();
        stmt.close(); 
        conn.close();               
        System.out.println("PDF Created");
        
}
}
