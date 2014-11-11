package com.example.burdapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NotificationAddOrders extends Activity{
	ProgressDialog prgDialog;
	Database db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);
		db = new Database(this);
		prgDialog = new ProgressDialog(this);
		getOrders();
	}
	
	
	public void getOrders(){
		
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		 	prgDialog.setMessage("Synching"+getString(R.string.please_wait));
			prgDialog.setCancelable(false);		
				prgDialog.show();
				params.put("mediatorJSON", composeJSONforNewOrder("getOrders"));
				client.get(applicationConstants.SERVER_URL+"orders.php",params ,new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						System.out.println(response);
						prgDialog.hide();
						if(response!=null){
							try {
								JSONArray arr = new JSONArray(response);
								ArrayList<String> id_list = new ArrayList<String>();
								//System.out.println(arr.length());
								for(int i=0; i<arr.length()-1;i++){
									JSONObject obj = (JSONObject)arr.get(i);
									//int b=i+1;
									
									if(!obj.get("id").equals(null)){
										db.addAndUpdateOrders(obj);
										id_list.add((String) obj.get("id"));
									}
								}
								db.removeUnwantedOrders(id_list);
								Toast.makeText(getApplicationContext(), getString(R.string.all_order_downloaded), Toast.LENGTH_LONG).show();
								//startActivity(new Intent(getApplicationContext(),DisplayOrders.class));
								//removeAcceptedOrders();
								//flag=1;			
								startActivity(new Intent(getApplicationContext(),DisplayOrders.class));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
								e.printStackTrace();
							}
						}else{
							prgDialog.hide();
							Toast.makeText(getApplicationContext(), "Response is null", Toast.LENGTH_SHORT).show();
						}
						
					}
		    
					@Override
					public void onFailure(int statusCode, Throwable error,
						String content) {
						// TODO Auto-generated method stub
						prgDialog.hide();
						if(statusCode == 404){
							Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
						}else if(statusCode == 500){
							Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
						}
						startActivity(new Intent(getApplicationContext(),DisplayOrders.class));
					}
				});
				//return flag;
					
	}
	
	/**
	 * Compose JSON out of SQLite records
	 * @return
	 */
	public String composeJSONforNewOrder(String type){
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> mList = db.getMediator();
		String mediatorId = mList.get(0).get(applicationConstants.MEDIATOR_ID);
	   
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	
	    		map.put("user_id", mediatorId);	    
	    		map.put("type", type);	    		
	        	wordList.add(map);
	       
	    //db.close();
		Gson gson = new GsonBuilder().create();
		//Use GSON to serialize Array List to JSON
		return gson.toJson(wordList);
	}
	


}
