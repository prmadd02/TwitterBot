package Utility;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;

import org.apache.commons.lang3.RandomStringUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;



public class MyPDFReport{
	
		
        public static String CrimeDataZip(String userName, String Status) throws Exception
        {	
        	        	
        	// Establishing Connection through MySQLConnection class
            Connection PDFConnection = MySQLConnection.getConnection();
        
        	/* Statement object for sending queries to DB */
            Statement stmt = PDFConnection.createStatement();
                                    
            /* Define the SQL query */
            ResultSet query_set = stmt.executeQuery("SELECT DATE_OCCURED, CRIME_TYPE, BLOCK_ADDRESS, ZIP_CODE FROM crimedata WHERE ZIP_CODE = '" + Status + "' ORDER BY DATE_OCCURED DESC");
            
            //If nothing is found close database connections and exit method
            if (!query_set.next())
            {
            	query_set.close();
                stmt.close(); 
                PDFConnection.close();
                String noData = "No Data";
                System.out.println(noData);
                return noData;
            }
            	
                                
            /* Initialize PDF documents - logical objects */
            String fileName = RandomStringUtils.randomAlphanumeric(5) + ".pdf";
            Document my_pdf_report = new Document(PageSize.LETTER.rotate());
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream(fileName));
            my_pdf_report.open();    
        
            //We have fifteen columns in our table
            PdfPTable crime_data_table = new PdfPTable(4);
            //Create other table objects here.
            
            Font headerFont = FontFactory.getFont("Times-Roman", 12, Font.BOLD);
            Font font = FontFactory.getFont("Times-Roman", 10, Font.NORMAL);
            
            //Create a cell object
            PdfPCell table_cell = null;
            
            //Creating headers for the PDF file
            createZipHeaders(table_cell, crime_data_table, headerFont);
            
            
            while (query_set.next()) {
            	//Crimedata table to PDF (replicate for other tables)
                        //String INCIDENT_NUMBER = query_set.getString("INCIDENT_NUMBER");
                        //table_cell=new PdfPCell(new Phrase(INCIDENT_NUMBER));
                        //crime_data_table.addCell(table_cell);
                        //String DATE_REPORTED=query_set.getString("DATE_REPORTED");
                        //table_cell=new PdfPCell(new Phrase(DATE_REPORTED));
                        //crime_data_table.addCell(table_cell);
                        String DATE_OCCURED=query_set.getString("DATE_OCCURED");
                        table_cell=new PdfPCell(new Phrase(DATE_OCCURED, font));
                        crime_data_table.addCell(table_cell);
                        //String UOR_DESC=query_set.getString("UOR_DESC");
                        //table_cell=new PdfPCell(new Phrase(UOR_DESC));
                        //crime_data_table.addCell(table_cell);
                        String CRIME_TYPE = query_set.getString("CRIME_TYPE");
                        table_cell=new PdfPCell(new Phrase(CRIME_TYPE, font));
                        crime_data_table.addCell(table_cell);
                        //String NIBRS_CODE=query_set.getString("NIBRS_CODE");
                        //table_cell=new PdfPCell(new Phrase(NIBRS_CODE));
                        //crime_data_table.addCell(table_cell);
                        //String UCR_HIERARCHY=query_set.getString("UCR_HIERARCHY");
                        //table_cell=new PdfPCell(new Phrase(UCR_HIERARCHY));
                        //crime_data_table.addCell(table_cell);
                        //String ATT_COMP=query_set.getString("ATT_COMP");
                        //table_cell=new PdfPCell(new Phrase(ATT_COMP));
                        //crime_data_table.addCell(table_cell);
                        //String LMPD_DIVISION = query_set.getString("LMPD_DIVISION");
                        //table_cell=new PdfPCell(new Phrase(LMPD_DIVISION));
                        //crime_data_table.addCell(table_cell);
                        //String LMPD_BEAT=query_set.getString("LMPD_BEAT");
                        //table_cell=new PdfPCell(new Phrase(LMPD_BEAT));
                        //crime_data_table.addCell(table_cell);
                        //String PREMISE_TYPE=query_set.getString("PREMISE_TYPE");
                        //table_cell=new PdfPCell(new Phrase(PREMISE_TYPE));
                        //crime_data_table.addCell(table_cell);
                        String BLOCK_ADDRESS=query_set.getString("BLOCK_ADDRESS");
                        table_cell=new PdfPCell(new Phrase(BLOCK_ADDRESS, font));
                        crime_data_table.addCell(table_cell);
                        //String CITY = query_set.getString("CITY");
                        //table_cell=new PdfPCell(new Phrase(CITY));
                        //crime_data_table.addCell(table_cell);
                        String ZIP_CODE=query_set.getString("ZIP_CODE");
                        table_cell=new PdfPCell(new Phrase(ZIP_CODE, font));
                        crime_data_table.addCell(table_cell);
                        //String ID=query_set.getString("ID");
                        //table_cell=new PdfPCell(new Phrase(ID));
                        //crime_data_table.addCell(table_cell);
                        }
        
        
            /* Attach report table to PDF */
            my_pdf_report.add(crime_data_table);                       
            my_pdf_report.close();
        
            /* Close all DB related objects */
            query_set.close();
            stmt.close(); 
            PDFConnection.close();
            System.out.println("PDF Created");
            
            //Returning fileName
            System.out.println(fileName);
            return fileName;
        
        }
        
        private static void createZipHeaders(PdfPCell table_cell, PdfPTable crime_data_table, Font headerFont)
        {
        	//Setting Headers for Table
            table_cell=new PdfPCell(new Phrase("Date Occured", headerFont));
            crime_data_table.addCell(table_cell);
            
            table_cell=new PdfPCell(new Phrase("Crime Type", headerFont));
            crime_data_table.addCell(table_cell);
            
            table_cell=new PdfPCell(new Phrase("Block Address", headerFont));
            crime_data_table.addCell(table_cell);
            
            table_cell=new PdfPCell(new Phrase("Zip Code", headerFont));
            crime_data_table.addCell(table_cell);
        }
        		
}