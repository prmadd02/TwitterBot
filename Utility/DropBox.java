package Utility;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.sharing.RequestedVisibility;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.dropbox.core.v2.sharing.SharedLinkSettings;
//import com.dropbox.core.v2.users.FullAccount;


public class DropBox {
	
	private static final String ACCESS_TOKEN = "HSPTH6VtILAAAAAAAAAADla47tjSLeZhc0Xi2SBxZ9iuZxgJDoS242EoYucMHs8U";

    @SuppressWarnings({ "deprecation", "unused" })
	public static String getLink(String fileName) throws DbxException {
    	
    	//If PDF report didn't report or nothing came from the search
    	if (fileName == "No Data")
    	{
    		return fileName;
    	}
    	
        // Create Dropbox client
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        
        FileInputStream in = null;
        
        //Upload file to Dropbox
        try  
        {
        	in = new FileInputStream(fileName);
        	
            FileMetadata metadata = client.files().uploadBuilder("/" + fileName)
                .uploadAndFinish(in);
        }
        
        catch (Exception e)
        {
        	
        }
        
        finally
        {
        	if (in != null)
        	{
        		try
        		{
        			in.close();	
        		}
        		catch (IOException ioe)
        		{ /* ignored */}
        	}
        }
        
        //Create Sharable Link
        SharedLinkMetadata slm = client.sharing().createSharedLinkWithSettings("/" + fileName, SharedLinkSettings.newBuilder().withRequestedVisibility(RequestedVisibility.PUBLIC).build());
        String link = slm.getUrl();
        
        //FullAccount account = client.users().getCurrentAccount();
        //System.out.println(account.getName().getDisplayName());
        //System.out.println(link);
        
        //return link
        return link;
    }
}
	
	
