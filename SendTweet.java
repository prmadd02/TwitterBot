import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class SendTweet {
	
	//access the twitter API using your twitter4j.properties file
	static Twitter twitter = TwitterFactory.getSingleton();
	
	public static void Tweet (String msg) throws TwitterException, InterruptedException
	{
		try
		{
			if (msg.length() <= 140)
			{
				//Send the Tweet
				Status status = twitter.updateStatus(msg);
        
				//write to log file of said Tweet
				WritingToFile.LogInfo("SENT", msg, "LouMetroBot");
    
				//print a message so we know when it finishes (Debug)
				System.out.println("Done.");
			}
			else
			{	
				//Parse string based on spaces and put the words into an array.
				String delims = "[ ]+";
				String[] tokens = msg.split(delims);
				//Setup new strings
				String newTweet = "";
				String newTweet2 = "..."; //Letting user know that this is from a previous tweet
				//If the new string in less then 100 characters, then add token and space
				//This way words stay intact during tweets
				for (String token : tokens)
				{
					if (newTweet.length() < 100)
						newTweet += token + " ";
					//Once over 100 characters, start new Tweet string
					else
						newTweet2 += token + " ";
				}
				
				//adding continuation at the end of tweet to let user know more tweets are coming.
				newTweet += "...";
				//Try sending new tweets
				Tweet(newTweet);
				Tweet(newTweet2);
				
			}
		}
		
		catch (TwitterException tex)
        {
        	WritingToFile.LogError(tex.getExceptionCode(), tex.getErrorMessage());
        }
    	
    	catch (Exception ex)
    	{
    		WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
    		WritingToFile.LogInfo("NOT SENT", msg, "LouMetroBot");
    	}
	}
	
	public static void Tweet (String userName, String msg) throws TwitterException, InterruptedException 
	{
		String tweet = "@" + userName + " " + msg;
		
		try
		{
			if (tweet.length() <= 140)
			{		
				//Send the Tweet
				Status status = twitter.updateStatus(tweet);
		        
				//write to log file of said Tweet
				WritingToFile.LogInfo("SENT", msg, userName);
		    
				//print a message so we know when it finishes (Debug)
				System.out.println("Done.");
			}
			else
			{
				//Parse string based on spaces and put the words into an array.
				String delims = "[ ]+";
				String[] tokens = msg.split(delims);
				//Setup new strings
				String newTweet = "";
				String newTweet2 = "..."; //Letting user know that this is from a previous tweet
				//If the new string in less then 100 characters, then add token and space
				//This way words stay intact during tweets
				for (String token : tokens)
				{
					if (newTweet.length() < 100)
						newTweet += token + " ";
					//Once over 100 characters, start new Tweet string
					else
						newTweet2 += token + " ";
				}
				
				//adding continuation at the end of tweet to let user know more tweets are coming.
				newTweet += "...";
				//Try sending new tweets
				Tweet(userName, newTweet);
				Tweet(userName, newTweet2);
			}
		}
		 catch (TwitterException tex)
        {
        	WritingToFile.LogError(tex.getExceptionCode(), tex.getErrorMessage());
        	
        }
    	
    	catch (Exception ex)
    	{
    		WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
    		WritingToFile.LogInfo("NOT SENT", msg, userName);
    	}
	}
	
	public static void Tweet (String userName, String msg, String hashtag) throws TwitterException, InterruptedException 
	{
		String tweet = "@" + userName + " " + msg + " #" + hashtag;
		
		try
		{
			if (tweet.length() <= 140)
			{		
				//Send the Tweet
				Status status = twitter.updateStatus(tweet);
		        
				//write to log file of said Tweet
				WritingToFile.LogInfo("SENT", msg + " #" + hashtag, userName);
		    
				//print a message so we know when it finishes (Debug)
				System.out.println("Done.");
			}
			else
			{
				//Parse string based on spaces and put the words into an array.
				String delims = "[ ]+";
				String[] tokens = msg.split(delims);
				//Setup new strings
				String newTweet = "";
				String newTweet2 = "..."; //Letting user know that this is from a previous tweet
				//If the new string in less then 100 characters, then add token and space
				//This way words stay intact during tweets
				for (String token : tokens)
				{
					if (newTweet.length() < 100)
						newTweet += token + " ";
					//Once over 100 characters, start new Tweet string
					else
						newTweet2 += token + " ";
				}
				
				//adding continuation at the end of tweet to let user know more tweets are coming.
				newTweet += "...";
				//Try sending new tweets
				Tweet(userName, newTweet);
				Tweet(userName, newTweet2, hashtag);
			}
		}
		 catch (TwitterException tex)
        {
        	WritingToFile.LogError(tex.getExceptionCode(), tex.getErrorMessage());
        	
        }
    	
    	catch (Exception ex)
    	{
    		WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
    		WritingToFile.LogInfo("NOT SENT", msg + " #" + hashtag, userName);
    	}
	}
	
	public static void TweetHash (String msg, String hashtag) throws TwitterException, InterruptedException 
	{
		String tweet = msg + " #" + hashtag;
		
		try
		{
			if (tweet.length() <= 140)
			{		
				//Send the Tweet
				Status status = twitter.updateStatus(tweet);
		        
				//write to log file of said Tweet
				WritingToFile.LogInfo("SENT", msg + " #" + hashtag, "LouMetroBot");
		    
				//print a message so we know when it finishes (Debug)
				System.out.println("Done.");
			}
			else
			{
				//Parse string based on spaces and put the words into an array.
				String delims = "[ ]+";
				String[] tokens = msg.split(delims);
				//Setup new strings
				String newTweet = "";
				String newTweet2 = "..."; //Letting user know that this is from a previous tweet
				//If the new string in less then 100 characters, then add token and space
				//This way words stay intact during tweets
				for (String token : tokens)
				{
					if (newTweet.length() < 100)
						newTweet += token + " ";
					//Once over 100 characters, start new Tweet string
					else
						newTweet2 += token + " ";
				}
				
				//adding continuation at the end of tweet to let user know more tweets are coming.
				newTweet += "...";
				//Try sending new tweets
				Tweet(newTweet);
				TweetHash(newTweet2, hashtag);
			}
		}
		 catch (TwitterException tex)
        {

        	WritingToFile.LogError(tex.getExceptionCode(), tex.getErrorMessage());
        	
        }
    	
    	catch (Exception ex)
    	{
    		WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
    		WritingToFile.LogInfo("NOT SENT", msg + " #" + hashtag, "LouMetroBot");
    	}
		
	}
}
