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

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class HomeActivity extends Activity implements OnClickListener{
	
	ActionBar.Tab orderTab, farmerTab;
	Fragment fragment_Order_Tab = new FragmentOrderTab();
	Fragment fragment_Farmer_Tab = new FragmentFarmerTab();
	Database db;
	serverDatabase sDB;
	int flag;
	Button orderButton, farmerButton, syncButton;
	ProgressDialog prgDialog;
	
	//-----------alarm manager ---------//
		AlarmManager am;
		public void setRepeatingAlarm() {
			System.out.println("inside alarm");
			  Intent intent = new Intent(this, TimeAlarm.class);
			  PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
			    intent, PendingIntent.FLAG_CANCEL_CURRENT);
			  am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
			    (15 * 60 * 1000), pendingIntent);
			 }
		
		
		//--variable for notification manager----///
		//Notification message ID
		 private static final int NOTIFY_ME_ID=1337;
		//Counter
		 private int count=0;
		//Create NotificationManager  object
		 private NotificationManager notifyMgr=null;
		
		 //-trigger for notifiction function.............
		 
		
		 
		public void clearNotification(View v) {
			 //Clear the notification 
			notifyMgr.cancel(NOTIFY_ME_ID);
		}
	 @Override
	    public void onCreate(Bundle savedInstanceState)
	    {	
	    	
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.activity_home);
	    	
	    	//setting alarm for notifications
	    	notifyMgr=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	    	getApplicationContext();
			am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    	setRepeatingAlarm();
	    	
	    	
	    	orderButton = (Button)findViewById(R.id.order_button);
	    	farmerButton = (Button)findViewById(R.id.farmer_button);
	    	syncButton = (Button)findViewById(R.id.sync_button);
	    	sDB = new serverDatabase(getApplicationContext());
	    	
	    	orderButton.setOnClickListener(this);
	    	farmerButton.setOnClickListener(this);
	    	syncButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					sDB.onUpSync(1);
				}
			});
	    	
	    	//code related to action bar and the tabs 
	    	 /*ActionBar actionBar = getActionBar();
	         actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	         
	         orderTab = actionBar.newTab().setText(getString(R.string.list_of_orders));
	         farmerTab = actionBar.newTab().setText(R.string.list_of_farmers);
	         
	         
	         orderTab.setTabListener(new MyTabListener(fragment_Order_Tab));
	         farmerTab.setTabListener(new MyTabListener(fragment_Farmer_Tab));	         
	         
	         actionBar.addTab(orderTab);
	         actionBar.addTab(farmerTab);*/
	         
	         
	         db = new Database(this);
	 	    prgDialog = new ProgressDialog(this);
	 	   
			
	 	    flag=0;
	 	    
	 	    
	 	  
	    }
	 
	 public int getOrders(){
						
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			 	prgDialog.setMessage("Synching "+getString(R.string.please_wait));
				prgDialog.setCancelable(false);		
					prgDialog.show();
					params.put("mediatorJSON", composeJSONforMediator("getOrders"));
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
									flag=1;			
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
					return flag;
						
		}
		
		/**
		 * Compose JSON out of SQLite records
		 * @return
		 */
		public String composeJSONforMediator(String type){
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
		
		/*public String composeJSONforCheckingOrders(String type){
			//ArrayList<HashMap<String, String>> wordList;
			//wordList = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> mList = db.getOrderIdsOfNewOrders(type);
			String mediatorId = mList.get(0).get(applicationConstants.ORDER_ID);
		   
		        	HashMap<String, String> map = new HashMap<String, String>();
		        	
		    		map.put("user_id", mediatorId);	    
		    		map.put("type", type);	    		
		        	wordList.add(map);
		       
		    //db.close();
			Gson gson = new GsonBuilder().create();
			//Use GSON to serialize Array List to JSON
			if(mList.size()>0)
			return gson.toJson(mList);
			else
			return null;
		}*/
	/*public void removeAcceptedOrders(){
		
		AsyncHttpClient client1 = new AsyncHttpClient();
		RequestParams params1 = new RequestParams();
					
			//prgDialog.show();
		String postString = composeJSONforCheckingOrders("checkAcceptedOrders");
		if(postString!=null){
					params1.put("mediatorJSON", postString);
					client1.post(applicationConstants.SERVER_URL+"orders.php",params1 ,new AsyncHttpResponseHandler() {
						
						@Override
						public void onSuccess(String response1) {
							System.out.println("cccccccccccccccccccccccc"+response1);
							prgDialog.hide();
							try {
								JSONArray arr = new JSONArray(response1);
								//System.out.println(arr.length());
								for(int i=0; i<arr.length();i++){
									
									JSONObject obj = (JSONObject)arr.get(i);
									
									System.out.println("Order ID--"+obj);
									String whereclause = " "+applicationConstants.ORDER_ID+" = '"+obj.get("id")+"' ";
									db.deleteRow(applicationConstants.ORDER_TABLE,whereclause);
									
								}
								Toast.makeText(getApplicationContext(), "order id check success", Toast.LENGTH_LONG).show();
								startActivity(new Intent(getApplicationContext(),DisplayOrders.class));
								
								flag=1;						
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								Toast.makeText(getApplicationContext(), "2.Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
								e.printStackTrace();
							}
							
						}
				
						@Override
						public void onFailure(int statusCode, Throwable error,
							String content) {
							// TODO Auto-generated method stub
							prgDialog.hide();
							if(statusCode == 404){
								Toast.makeText(getApplicationContext(), "2.Requested resource not found", Toast.LENGTH_LONG).show();
							}else if(statusCode == 500){
								Toast.makeText(getApplicationContext(), "2.Something went wrong at server end", Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(getApplicationContext(), "2.Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
							}
							startActivity(new Intent(getApplicationContext(),DisplayOrders.class));
						}
					});
			}else{
				prgDialog.hide();
				Toast.makeText(getApplicationContext(), "No new records to check", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(),DisplayOrders.class));
			}
				
		}*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.order_button:			
			getOrders();			
			break;
		case R.id.farmer_button:
			System.out.println("pressed farmer button");			
			startActivity(new Intent(getApplicationContext(),Farmers.class));
			break;
		default:
			break;
		}
	}
	
   
    
}
