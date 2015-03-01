package ie.johndoyle.beerme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.view.View.OnClickListener;

public class MyAsyncTask extends AsyncTask<Void, Void, String>  {

    ResultsListener listener;
    
    String url = "";

    HashMap<String, Item> orders = new HashMap<String, Item>();
    
    public MyAsyncTask(ResultsListener resultsListener) {
		this.listener = listener;
	}

	public void setOrders(HashMap<String, Item> orders) {
    	this.orders = orders;
    }
    
    public void setUrl(String url) {
    	this.url = url;
    }
    
    public void setOnResultsListener(ResultsListener listener) {
        this.listener = listener;
    }

    protected String doInBackground(Void... stuff) {
    	Iterator<Entry<String, Item>> itemIterator = orders.entrySet()
				.iterator();
    	
    	String orders = "";
    	
    	while (itemIterator.hasNext()) {
			Entry<String, Item> entry = (Entry<String, Item>) itemIterator
					.next();
			Item item = (Item) entry.getValue();
			
			orders += item.getName() + "|" + item.getQuantity() + "|" + item.getPrice() + ":";
    	}

        return POST(url,orders);

    }

    @Override
    protected void onPostExecute(String result) {
       listener.onResultsSucceeded(result);
    }
    
    public static String POST(String url, String orders){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
 
            String json = "";
 
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", "John");
            jsonObject.accumulate("order", orders);
            jsonObject.accumulate("bar", "Banshee");
 
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
 
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib 
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person); 
 
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
 
            // 6. set httpPost Entity
            httpPost.setEntity(se);
 
            // 7. Set some headers to inform server about the type of the content   
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
 
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
 
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            
        }
 
        // 11. return result
        return result;
    }
    
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }


}