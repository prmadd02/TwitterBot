package Utility;

import org.json.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.TimerTask;

public class WeatherData extends TimerTask {
	
	public static String result;
	
	private static String readAll(Reader rd) throws IOException{
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp = rd.read()) != -1){
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try{
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		}
		finally{
			is.close();
		}
	}
	
	public static String requestedWeather(){
		
		 try
	        {
	            JSONObject data = new JSONObject();
	            JSONArray weather = new JSONArray();
	            JSONObject description = new JSONObject();

	            data = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?q=Louisville&APPID=f1261d1617b2b63c278b6a3f584c67b8&units=metric");
	            weather = data.getJSONArray("weather");
	            double d = data.getJSONObject("main").getDouble("temp");
	            description = weather.getJSONObject(0);

	            String result = "The weather in Louisville is currently " + Math.round(d * 9/5 + 32) + " degrees Fahrenheit"
	                    + " with " + description.get("description");

	            return result;

	        }
	        catch (Exception ex)
	        {
	            WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));

	            return "LouWeather Data is not working right now. Please try again later.";
	        }	
	}
	
	public void run()
	{
		System.out.println("WeatherData Start");
		try
		{
			JSONObject data = new JSONObject();
			JSONArray weather = new JSONArray();
			JSONObject description = new JSONObject();
		
			data = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?q=Louisville&APPID=f1261d1617b2b63c278b6a3f584c67b8&units=metric");
			weather = data.getJSONArray("weather");
			double d = data.getJSONObject("main").getDouble("temp");
			description = weather.getJSONObject(0);
		
			result = "The weather in Louisville is currently " + Math.round(d * 9/5 + 32) + " degrees Fahrenheit"
					+ " with " + description.get("description");
			
			SendTweet.Tweet(result);
			
			System.out.println("WeatherData Done");
		}
		catch (Exception ex)
		{
			WritingToFile.LogError(ex.toString(), WritingToFile.exceptionStacktraceToString(ex));
		}
	}
}