package com.example.burdapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

public class TimeAlarm extends BroadcastReceiver {
	
	 NotificationManager nm;
	 Context c;
	// Database db;
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Burd b;
		c = context;
		//db = new Database(context);	
		
		
		//Create AsycHttpClient object
				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();				
						
						params.put("mediatorJSON", composeJSONforOrder("getNewOrderNotification"));
						client.get(applicationConstants.SERVER_URL+"orders.php",params ,new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								System.out.println(response);
								
								try {
									JSONArray arr = new JSONArray(response);
									System.out.println(arr.length());
									System.out.println(arr);
									
									ArrayList<String> id_list = new ArrayList<String>();
									for(int i=0; i<arr.length()-1;i++){
										JSONObject obj = (JSONObject)arr.get(i);
										//int b=i+1;
										
										if(!obj.get("id").equals(null)){
											//db.addAndUpdateOrders(obj);
											id_list.add((String) obj.get("id"));
										}
									}
									//db.removeUnwantedOrders(id_list);
									//db.closeDb();
									if(arr.length()>0){
										nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
										  CharSequence from = " BURD App";
										  CharSequence message = arr.length()+" New Orders!!";
										  PendingIntent contentIntent = PendingIntent.getActivity(c, 0,new Intent(c, NotificationAddOrders.class), 0);
										 
						                                             
										  Notification notif = new Notification(R.drawable.ac_blue_side,
										    "New Orders!!!", System.currentTimeMillis());
										  notif.setLatestEventInfo(c, from, message, contentIntent);
										  notif.defaults |= Notification.DEFAULT_VIBRATE;
											//Set default notification sound
										  notif.defaults |= Notification.DEFAULT_SOUND;
											//Clear the status notification when the user selects it
											notif.flags|=Notification.FLAG_AUTO_CANCEL;
										  nm.notify(149, notif);
									}
									
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									
									e.printStackTrace();
								}
							}
				    
							@Override
							public void onFailure(int statusCode, Throwable error,
								String content) {
								// TODO Auto-generated method stub
								//prgDialog.hide();
								if(statusCode == 404){
									System.out.println("Requested resource not found");
								}else if(statusCode == 500){
									System.out.println("Something went wrong at server end");
								}else{
									System.out.println("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]");
								}
							}
						});
		
		
		}
	public String composeJSONforOrder(String type) {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
				//String mediatorId = ""+var+"";
	   
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	
	    		//map.put("id", mediatorId);	    
	    		map.put("type", type);	    		
	        	wordList.add(map);
	       
	    //db.close();
		Gson gson = new GsonBuilder().create();
		//Use GSON to serialize Array List to JSON
		return gson.toJson(wordList);
	}
	
	
}
