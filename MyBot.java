import java.util.Calendar;
import java.util.Timer;

import Hashtags.LouCrimeZip;
import Hashtags.LouWeather;
import TableUpdate.CrimeData;

public class MyBot{

    //if something goes wrong, we might see a TwitterException
    public static void main(String... args) throws InterruptedException
    {
    	//Timer updateCrimeTime = new Timer();
    	//Calendar updateCrimeTable = Calendar.getInstance();
    	
    	//Schedule to update Crime table every Sunday
    	updateCrimeTable.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    	updateCrimeTable.set(Calendar.HOUR, 0);
    	updateCrimeTable.set(Calendar.MINUTE, 0);
    	updateCrimeTable.set(Calendar.SECOND, 0);
    	updateCrimeTable.set(Calendar.MILLISECOND, 0);
   	    	
    	updateCrimeTime.schedule(new CrimeData(), updateCrimeTable.getTime(), 1000 * 60 * 60 * 24 * 7);
    	
    	Timer queryWeatherTime = new Timer();
    	
    	//Schedule to release weather everyday @ 8am
    	queryWeatherTime.schedule(new LouWeather(), 0, 1000 * 60);		
    	
    	Timer queryCrimeTime = new Timer();
    	    	
    	//Schedule to run every minute
    	queryCrimeTime.schedule(new LouCrimeZip(), 0, 1000 * 60);		
    }
}