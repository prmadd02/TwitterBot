import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class MyBot{

    //if something goes wrong, we might see a TwitterException
    public static void main(String... args) throws TwitterException
    {
    	try
    	{
    		//Finding twitter id so that query isn't returning the same results
    		long lastID = 0;
    		long firstID = 0;
    		
    		//access the twitter API using your twitter4j.properties file
    		Twitter twitter = TwitterFactory.getSingleton();
		
		//Create a WeatherData object which will access the OpenWeatherMap API
		WeatherData louWeather = new WeatherData();
		
		//Create a Calendar object for keeping time
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("mm");
		int min = 0;
    		    		 		
    		//keep tweeting forever or till exception happens
    		while (true)
    		{
			//Retrieve number of minutes past the hour
    			min = Integer.parseInt(sdf.format(cal.getTime()));
			//Tweet a weather update every 30 minutes
			if(min % 30 == 0){
				SendTweet.Tweet(louWeather.getWeather());
			}
    			//creating a new search
        		Query query = new Query("Kentucky #UKvsUF -RT");
        		
        		setupSearchQuery(query, firstID, lastID);
    			
        		//Get the results from the search
    			QueryResult result;
    			
    			List<Status> tweets;
    			
    			ArrayList<Long> IDs = new ArrayList<Long>();
    			    			    			
    			do
    			{	
    				//Query Twitter
    				result = twitter.search(query);
    				
    				tweets = result.getTweets();
    				
    				//For each twitter status that matches our query we log it in the log file
    				for (Status status : tweets)
    				{
    					WritingToFile.CSVFile("InfoLog.csv", status.getText(), status.getUser().getScreenName(), Long.toString(status.getId() + 'L'));
    					    					    					
    					IDs.add(status.getId() + 'L');
    				}
    				
    				query = result.nextQuery();
    				checkRateLimit(result);
    			}
    			while (result == null || result.hasNext()); //we do this till the query is empty
    		
    			//URL myURL = new URL("https://data.louisvilleky.gov/sites/default/files/Crime_Data_2017_1.csv");
    			//MySQLConnection.UpdateCrimeData("CrimeData", myURL);
    			
    			//if no new tweets are found, no need to collect new IDs
    			if (IDs.size() > 0)
    			{
    				Collections.sort(IDs);
    			
    				lastID = IDs.get(0) + 'L';
    				firstID = IDs.get((IDs.size() - 1)) + 'L';
    			}
    			
    			System.out.println(Long.toString(firstID));
    			System.out.println(Long.toString(lastID)); 
    			System.out.println(Long.toString(lastID-1));// Help with Debug
    			System.out.println("Done"); //Help with Debug
    			
    			//Trying to avoid rate timeout
    			Thread.sleep(1000 * 60);
    		}
    	}
    	
        catch (TwitterException tex)
        {
        	WritingToFile.LogError(tex.getExceptionCode(), tex.getErrorMessage());
        	WritingToFile.LogError(tex.getMessage(), WritingToFile.exceptionStacktraceToString(tex));
        	WritingToFile.LogError(tex.getLocalizedMessage(),"TESTING");
        	
        }
    	
    	catch (Exception ex)
    	{
    		WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
    	}
    	
   }
    
   private static void checkRateLimit(QueryResult result)
   {
	   if (result.getRateLimitStatus().getRemaining() <= 0)
	   {
		   try
		   {
			  SendTweet.Tweet("I'm Sleeping!!");
	          Thread.sleep(result.getRateLimitStatus().getSecondsUntilReset() * 1000);
	          SendTweet.Tweet("I'm Awake!!");
		   } 
		   catch (Exception e) 
		   {
	            e.printStackTrace();
	            throw new RuntimeException(e);
	       }
	   }
   }
   
   private static void setupSearchQuery(Query query, long firstID, long lastID)
   {
	   	//Only looking for recent tweets not popular ones
		query.setResultType(Query.RECENT);
		
		//One hundred tweets per page (Doubt its necessary but nevertheless)
		query.setCount(10);
		
		//Making sure we don't get the same tweets over and over again.
		if (firstID != 0)
			query.setSinceId(firstID + 'L');
				
   }
   
   
}
