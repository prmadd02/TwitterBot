package Hashtags;
import Utility.WritingToFile;
import Utility.MyPDFReport;
import Utility.DropBox;
import Utility.MySQLConnection;
import Utility.SendTweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class LouCrimeZip extends TimerTask {
	
	int select = -1;
	int previous = -1;
			
	public void run()
	{
		//access the twitter API using your twitter4j.properties file
		Twitter twitter = TwitterFactory.getSingleton();
		
		//Finding twitter id so that query isn't returning the same results
		long lastID = 0L;
		long firstID = 0L;
		
		try
		{
		
		//creating a new search
		Query query = new Query("#LouCrimeZip @LouMetroBot -RT");
	
		setupSearchQuery(query, firstID, lastID);
	
		//Get the results from the search
		QueryResult result;
	
		//Collecting tweets to put into arraylist
		List<Status> tweets;
	
		//Collecting IDs to find lowest and highest status IDs
		ArrayList<Long> IDs = new ArrayList<Long>();
	    			    			
		do
		{	
			//Query Twitter
			result = twitter.search(query);
		
			tweets = result.getTweets();
		
			//For each twitter status that matches our query we log it in the log file
			for (Status status : tweets)
			{
				//Write to csv file
				WritingToFile.CSVFile("InfoLog.csv", status.getText(), status.getUser().getScreenName(), Long.toString(status.getId() + 'L'), tweetCleanup(status.getText()), "RECEIVED");
			
				//Creating PDF Report
				String pdf = MyPDFReport.CrimeDataZip(status.getUser().getScreenName(), tweetCleanup(status.getText()));
			
				//Uploading to Dropbox and collecting link 
				String link = DropBox.getLink(pdf);
			
				if (link == "No Data")
				{
					// Random string generator to avoid duplicate API status errors					
					String [] arr = {"Sorry, your search didn't return any results.", 
									 "Sorry, we couldn't find that zip code in the Open Data Portal.",
									 "Sorry, our bot couldn't find what you were looking for.", 
									 "@" + status.getUser().getScreenName() + " please press Alt+F4 to continue.",
									 "Sorry, @" + status.getUser().getScreenName() + " we couldn't find the information you were looking for.",
									 "The data that was requested from @" + status.getUser().getScreenName() + " is unavailable at this time."};
					
					Random random = new Random();
					
					//keep selecting an array as long as select and previous match up
					do {
						
						select = random.nextInt(arr.length);
						System.out.println("Select = " + select);
						System.out.println("Previous = " + previous);
					}
					while (select == previous);
					
					if (select == 3 || select == 4 || select == 5)
					{
						SendTweet.Tweet(arr[select]);
					}
					else
					{
						SendTweet.Tweet(status.getUser().getScreenName(), arr[select]);
					}
										
					previous = select;
				}
				else
				{
					//Replying to the tweet
					SendTweet.PDFTweet(status.getUser().getScreenName(), tweetCleanup(status.getText()), link, status.getId() + 'L');
				}
			 
				//Add to ID arraylist
				IDs.add(status.getId() + 'L');
			}
		
			//Checking to see if there are more pages
			query = result.nextQuery();
		
			//Checking rate limit and will go to sleep if rate limit is reached
			checkRateLimit(result);
		}
		while (result == null || result.hasNext()); //we do this till the query is empty

	
		//if no new tweets are found, no need to collect new IDs
		if (IDs.size() > 0)
		{
			//Sorts from smallest to biggest
			Collections.sort(IDs);
	
			lastID = IDs.get(0) + 'L';
			firstID = IDs.get((IDs.size() - 1)) + 'L';
		
			MySQLConnection.StoreStatusIDs("Zip", firstID, lastID);
		}
	
		System.out.println(Long.toString(firstID));// Help with Debug
		System.out.println(Long.toString(lastID)); // Help with Debug
		System.out.println(Long.toString(lastID-1));// Help with Debug
		System.out.println("LouCrimeZip Done"); //Help with Debug
		
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
   //If we go over our rate limit
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
	
	//Ten tweets per page (Doubt its necessary but nevertheless)
	query.setCount(10);
	
	firstID = MySQLConnection.CollectFirstID("Zip");
			
	//Making sure we don't get the same tweets over and over again.
	if (firstID != 0L)
		query.setSinceId(firstID + 'L');
					
}

private static String tweetCleanup (String status)
{
   //Remove Hashtags from Tweets
   status = status.replaceAll("#[^\\s]*", "");
   
   //Remove URLs in Tweet
   status = status.replaceAll("http[^\\s]*", "");
   
   //Remove @UserNames from Tweet Text
   status = status.replaceAll("@[^\\s]*", "");
   
   //Remove commas and spaces that could interfere with the search
   status = status.replace(",", "");
   status = status.replace("\n", "").replace("\r", "");
   status = status.replaceAll("\\s+", "");
   
   return status;
}

}