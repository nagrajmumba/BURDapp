package com.example.burdapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class serverDatabase {
Context context;
	
	// a reference to the database used by this application/object
	private SQLiteDatabase db;
		
	public serverDatabase(Context context)
	{
		try{
		this.context = context;
 
		// create or open the database
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
		this.db = helper.getWritableDatabase();
		
		}catch(Exception e){
			e.printStackTrace();		
		}
	}
	private class CustomSQLiteOpenHelper extends SQLiteOpenHelper
	{
		public CustomSQLiteOpenHelper(Context context)
		{
			super(context, applicationConstants.DB_NAME, null, applicationConstants.DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public String onUpSync(int flag){
		
		
		//String localres = null;
		String l_url = "";
		Gson gson = new GsonBuilder().create();
		String returnResult = null;
		try{
		ArrayList<HashMap<String, String>> wordListOrder,wordListFarmerOrder,wordListFarmerTable;
		wordListOrder = new ArrayList<HashMap<String, String>>();
		wordListFarmerOrder = new ArrayList<HashMap<String, String>>();
		wordListFarmerTable = new ArrayList<HashMap<String, String>>();
		
		
		String selectOrderTable = "SELECT * FROM "+applicationConstants.ORDER_TABLE+" where "+applicationConstants.ORDER_SYNCHED+" = '0'";
		String selectFarmerTable = "SELECT * FROM "+applicationConstants.FARMER_TABLE+" where "+applicationConstants.FARMER_SYNCHED+" = '0'";
		String selectFarmerOrderTable = "SELECT * FROM "+applicationConstants.FARMER_ORDER_TABLE+" where "+applicationConstants.FORDER_SYNCHED+" = '0'";
		//String selectMediatorTable = "SELECT * FROM "+applicationConstants.MEDIATOR_TABLE+" where "+applicationConstants.MEDIATOR_SYNCSTATUS+" = '0'";
		//String selectSessionDetailsQuery = "SELECT * FROM session_details_table where synched = '0'";
		//String selectSessionTableQuery = "SELECT * FROM session_table where synched = '0'";
		
		
		Cursor cursorOrder = db.rawQuery(selectOrderTable, null);
		Cursor cursorFarmerOrder = db.rawQuery(selectFarmerOrderTable, null);
		Cursor cursorFarmerTable = db.rawQuery(selectFarmerTable, null);
		//Cursor cursorMediatorTable = db.rawQuery(selectFarmerTable, null);
		
		//cursorMediatorTable.moveToFirst();
		//String mediator_id = cursorMediatorTable.getString(0);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String mediator_id = prefs.getString(applicationConstants.MEDIATOR_ID, null);
		
		int totalOrders=0, totalFarmerOrders = 0, totalFarmerTable =0;
		if(cursorOrder.getCount()>0 ||cursorFarmerOrder.getCount()>0||cursorFarmerTable.getCount()>0){
			
		    if (cursorOrder.moveToFirst()) {
		    	System.out.print(cursorOrder.getString(0));
		        do {
		        	HashMap<String, String> map = new HashMap<String, String>();
		        	//map.put("userId", cursor.getString(0));
		        	map.put("id", cursorOrder.getString(0));
		        	map.put("order_name", cursorOrder.getString(1));	    		
		    		map.put("created_by_id", cursorOrder.getString(2));	    		
		    		map.put("amount", cursorOrder.getString(3));	
		    		map.put("price", cursorOrder.getString(4));
		    		map.put("transportation_cost", cursorOrder.getString(5));
		    		map.put("type", cursorOrder.getString(6));
		    		map.put("deadline", cursorOrder.getString(7));
		    		map.put("accepted_by_id", cursorOrder.getString(8));
		    		map.put("status", cursorOrder.getString(9));		    		
		    		map.put("sent_by_mediator_date", cursorOrder.getString(14));
		    		map.put("will_be_reaching_date", cursorOrder.getString(15));
		    				    		
		    		wordListOrder.add(map);
		        } while (cursorOrder.moveToNext());
		        //indexess of local database
		        //0.ORDER_ID 
				//1.ORDER_NAME 
				//2.ORDER_CREATED_BY 
				//3.ORDER_AMOUNT 
				//4.ORDER_PRICE
				//5.ORDER_TRANSPORTATION_COST 
				//6.ORDER_TYPE
				//7.ORDER_DELIVERY_DATE
				//8.ORDER_ACCEPTED_BY
				//9.ORDER_STATUS
				//10.ORDER_BALANCE_QUANTITY
				//11.ORDER_NEW
				//12.ORDER_VIEW 
				//13.ORDER_SYNCHED 
				//14.ORDER_SENT_BY_MEDIATOR
				//15.ORDER_WILL_BE_REACHED_BY 
		        totalOrders=cursorOrder.getCount();
		    }
		    
		    if (cursorFarmerOrder.moveToFirst()) {
		    	System.out.print(cursorFarmerOrder.getString(0));
		        do {
		        	HashMap<String, String> map = new HashMap<String, String>();
		        	//map.put("userId", cursor.getString(0));
		        	map.put("id", cursorFarmerOrder.getString(0));
		        	map.put("order_id", cursorFarmerOrder.getString(1));	    		
		    		map.put("farmer_id", cursorFarmerOrder.getString(2));	    		
		    		map.put("assigned_by", mediator_id);	
		    		map.put("amount", cursorFarmerOrder.getString(5));
		    		map.put("forder_delivery_date", cursorFarmerOrder.getString(3));
		    		map.put("forder_price", cursorFarmerOrder.getString(4));
		    		map.put("forder_travel_cost", cursorFarmerOrder.getString(6));
		    		map.put("forder_assigned", cursorFarmerOrder.getString(7));
		    		map.put("forder_received", cursorFarmerOrder.getString(8));		    		
		    		map.put("forder_payment", cursorFarmerOrder.getString(9));
		    		map.put("forder_status", cursorFarmerOrder.getString(10));
		    		map.put("forder_confirmed", cursorFarmerOrder.getString(12));
		    				    		
		    		wordListFarmerOrder.add(map);
		        } while (cursorFarmerOrder.moveToNext());
		        //indexess of local database
		        //(0).FORDER_ID
				//(1).FORDER_ORDER_ID 
				//(2).FORDER_FARMER_ID 
				//(3).FORDER_DELIVERY_DATE
				//(4).FORDER_PRICE
				//(5).FORDER_QUANTITY
				//(6).FORDER_TRAVEL_COST
				//(7).FORDER_ASSIGNED
				//(8).FORDER_RECEIVED
				//(9).FORDER_PAYMENT 
				//(10).FORDER_STATUS 
				//(11).FORDER_SYNCHED 
				//(12).FORDER_CONFIRMED 
		        totalFarmerOrders=cursorFarmerOrder.getCount();
		    }
		    if (cursorFarmerTable.moveToFirst()) {
		    	System.out.print(cursorFarmerTable.getString(0));
		        do {
		        	HashMap<String, String> map = new HashMap<String, String>();
		        	//map.put("userId", cursor.getString(0));
		        	map.put("id", cursorFarmerTable.getString(0));
		        	map.put("name", cursorFarmerTable.getString(1));		    		    		
		    		map.put("mediator_id", mediator_id);	
		    		map.put("qty_seed", cursorFarmerTable.getString(3));
		    		map.put("qty_kernel", cursorFarmerTable.getString(4));
		    		map.put("qty_fruit", cursorFarmerTable.getString(5));
		    		map.put("phno", cursorFarmerTable.getString(2));
		    		map.put("qty_pulp", cursorFarmerTable.getString(6));
		    		map.put("farmer_street", cursorFarmerTable.getString(7));		    		
		    		map.put("farmer_landmark", cursorFarmerTable.getString(8));
		    		map.put("farmer_city", cursorFarmerTable.getString(9));
		    		map.put("farmer_state", cursorFarmerTable.getString(10));
		    		map.put("farmer_pincode", cursorFarmerTable.getString(11));
		    		map.put("farmer_address", cursorFarmerTable.getString(12));
		    		map.put("farmer_created_on", cursorFarmerTable.getString(13));
		    				    		
		    		wordListFarmerTable.add(map);
		        } while (cursorFarmerTable.moveToNext());
		        //indexess of local database
		        //(0).FARMER_ID 
				//(1).FARMER_NAME
				//(2).FARMER_MOBILE
				//(3).FARMER_SEED
				//(4).FARMER_KERNEL 
				//(5).FARMER_FRUIT 
				//(6).FARMER_PULP
				//(7).FARMER_STREET
				//(8).FARMER_LANDMARK 
				//(9).FARMER_CITY 
				//(10).FARMER_STATE
				//(11).FARMER_PINCODE
				//(12).FARMER_ADDRESS
				//(13).FARMER_CREATED_ON 
				//(14).FARMER_OCCUPIED_KERNEL
				//(15).FARMER_OCCUPIED_SEED 
				//(16).FARMER_OCCUPIED_FRUIT 
				//(17).FARMER_OCCUPIED_PULP 		
				//(18).FARMER_SYNCHED 
		        totalFarmerTable=cursorFarmerTable.getCount();
		    }
		
			//Use GSON to serialize Array List to JSON
		    if(wordListOrder.size()>0){
		    	String l_url1 = applicationConstants.SERVER_URL+"ordersSync.php";
		    	String jsonOrderTable = gson.toJson(wordListOrder);
		    	//System.out.print(jsonOrderTable);
		    	postDataWithAsyncHttpAndUpdateResponse(l_url1,jsonOrderTable,1,totalOrders,flag);
		    }
		    if(wordListFarmerOrder.size()>0){
		    	String l_url2 = applicationConstants.SERVER_URL+"farmerOrderSync.php";
				String jsonFarmerOrder = gson.toJson(wordListFarmerOrder);
				System.out.println("this is farmer table json:start---" + jsonFarmerOrder+"---end");
				postDataWithAsyncHttpAndUpdateResponse(l_url2,jsonFarmerOrder,2,totalFarmerOrders,flag);
		    }
		    if(wordListFarmerTable.size()>0){
		    	String l_url3  = applicationConstants.SERVER_URL+"farmersSync.php";
		    	String jsonFarmerTable = gson.toJson(wordListFarmerTable);
		    	
		    	postDataWithAsyncHttpAndUpdateResponse(l_url3,jsonFarmerTable,3,totalFarmerTable,flag);
		    }
			
			 returnResult = "Sync in progress...";
			 Toast.makeText(context, returnResult, Toast.LENGTH_SHORT).show();
		}else{
			returnResult = "No records to sync";
			Toast.makeText(context, returnResult, Toast.LENGTH_SHORT).show();
		}
		}catch(Exception ex){
			ex.printStackTrace();
		} 
		return returnResult;
	}
	
	public void postDataWithAsyncHttpAndUpdateResponse(String uri,String json, final int func, final int count, final int flag){
		
		final int DEFAULT_TIMEOUT = 500 * 1000;
		
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(DEFAULT_TIMEOUT);
		System.out.println("this is func::"+func);
		RequestParams params = new RequestParams();
				if(func==1)
					params.put("orderTableJSON", json);
				else if(func ==2)
					params.put("farmerOrderTableJSON", json);
				else if(func ==3)
					params.put("farmersTableJSON", json);
				
				client.post(uri,params ,new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						Boolean res;
						int f = func;
						//System.out.println(func+"::this is response from the server:"+ response);
						if(func==1){
							res = handlePostResponseOrderTable(response,f,count);
							if(res){
								Toast.makeText(context, "Order Sync completed", Toast.LENGTH_SHORT).show();
							}else{
								
								Toast.makeText(context, "Order Sync Interrupted !!!", Toast.LENGTH_SHORT).show();
							}
						}
						else if(f==2){
							res = handlePostResponseFarmerOrderTable(response,f,count);
							if(res){
								
									Toast.makeText(context, "Farmer Order  Sync completed", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(context, "Farmer Order Sync Interrupted !!!", Toast.LENGTH_SHORT).show();
							}
						}else if(f==3){
							res = handlePostResponseFarmersTable(response,f,count);
							if(res){
								
									Toast.makeText(context, "Farmer Sync completed", Toast.LENGTH_SHORT).show();								
							}else{
									Toast.makeText(context, "Farmer Sync Interrupted !!!", Toast.LENGTH_SHORT).show();								
							}
						}
						
					}
					
					@Override
					public void onFailure(int statusCode, Throwable error,String content) {
						// TODO Auto-generated method stub
						System.out.println("Failure:::::"+ statusCode +" err:::"+error+" content::::" + content);
						
					}
					@Override
				     public void onFinish() {
				         // Completed the request (either success or failure)
						
				     }
					
				});
				
				client.getHttpClient().getConnectionManager().closeExpiredConnections();
					
				
		
	}
	protected boolean handlePostResponseOrderTable(String response, int f, int count) {
		// TODO Auto-generated method stub
		 String[] status=null,ID=null,sess_id=null,sess_type=null,phr_no=null;
		 int total = 0;
			//response = response.substring(response.indexOf('['));
			try {
				response = response.substring(response.indexOf('['));
				JSONArray arr = new JSONArray(response);
				
				
				status = new String[arr.length()];
				ID = new String[arr.length()];
				//sess_id = new String[arr.length()];
				//sess_type = new String[arr.length()];
				//phr_no = new String[arr.length()];
				
				for(int i=0;i<arr.length();i++){
					
					JSONObject obj = (JSONObject)arr.get(i);
					status[i] = obj.get("status").toString();
					ID[i] = obj.get("id").toString();
					String stat = status[i].toString();
					if(stat.equals("yes")){
						//String updateQuery = "UPDATE userdetails SET synched = '1' where _id = '"+ID[i]+"'";
						//Log.d("users","query:"+updateQuery);
						
						ContentValues cv = new ContentValues();
						cv.put(applicationConstants.ORDER_SYNCHED, "1");
						String[] tempID = {String.valueOf(ID[i])};
						db.update(applicationConstants.ORDER_TABLE,cv,applicationConstants.ORDER_ID+"=?",tempID);
						total ++;
						tempID=null;
						cv.clear();
						
					}else{
						System.out.println("Did not insert locally in order table"+status.toString()+"::::ID:"+ID.toString());
					}
					
				}
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//FileOperations.write("Syncing exception:"+e.getMessage());
			}
		
		if(total==count){
			total = 0;
			return true;
		}else{
			total = 0;
			return false;
		}
	}
	
	protected boolean handlePostResponseFarmerOrderTable(String response, int f, int count) {
		// TODO Auto-generated method stub
		 String[] status=null,ID=null,sess_id=null,sess_type=null,phr_no=null;
		 int total = 0;
			//response = response.substring(response.indexOf('['));
			try {
				response = response.substring(response.indexOf('['));
				JSONArray arr = new JSONArray(response);
				
				
				status = new String[arr.length()];
				ID = new String[arr.length()];
				//sess_id = new String[arr.length()];
				//sess_type = new String[arr.length()];
				//phr_no = new String[arr.length()];
				
				for(int i=0;i<arr.length();i++){
					
					JSONObject obj = (JSONObject)arr.get(i);
					status[i] = obj.get("status").toString();
					ID[i] = obj.get("id").toString();
					String stat = status[i].toString();
					if(stat.equals("yes")){
						//String updateQuery = "UPDATE userdetails SET synched = '1' where _id = '"+ID[i]+"'";
						//Log.d("users","query:"+updateQuery);
						
						ContentValues cv = new ContentValues();
						cv.put(applicationConstants.FORDER_SYNCHED, "1");
						String[] tempID = {String.valueOf(ID[i])};
						db.update(applicationConstants.FARMER_ORDER_TABLE,cv,applicationConstants.FORDER_ID+"=?",tempID);
						total ++;
						tempID=null;
						cv.clear();
						
					}else{
						System.out.println("Did not insert locally in farmer order table"+status.toString()+"::::ID:"+ID.toString());
					}
					
				}
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//FileOperations.write("Syncing exception:"+e.getMessage());
			}
		
		if(total==count){
			total = 0;
			return true;
		}else{
			total = 0;
			return false;
		}
	}
	
	protected boolean handlePostResponseFarmersTable(String response, int f, int count) {
		// TODO Auto-generated method stub
		 String[] status=null,ID=null,sess_id=null,sess_type=null,phr_no=null;
		 int total = 0;
			//response = response.substring(response.indexOf('['));
			try {
				response = response.substring(response.indexOf('['));
				JSONArray arr = new JSONArray(response);
				
				
				status = new String[arr.length()];
				ID = new String[arr.length()];
				//sess_id = new String[arr.length()];
				//sess_type = new String[arr.length()];
				//phr_no = new String[arr.length()];
				
				for(int i=0;i<arr.length();i++){
					
					JSONObject obj = (JSONObject)arr.get(i);
					status[i] = obj.get("status").toString();
					
					ID[i] = obj.get("id").toString();
					String stat = status[i].toString();
					
					System.out.println(status[i]+" "+ID[i]+obj.names().toString()+obj.get("response").toString());
					if(status[i].equals("yes")){
						//String updateQuery = "UPDATE userdetails SET synched = '1' where _id = '"+ID[i]+"'";
						//Log.d("users","query:"+updateQuery);
						
						ContentValues cv = new ContentValues();
						cv.put(applicationConstants.FARMER_SYNCHED, "1");
						String[] tempID = {String.valueOf(ID[i])};
						db.update(applicationConstants.FARMER_TABLE,cv,applicationConstants.FARMER_ID+"=?",tempID);
						total ++;
						tempID=null;
						cv.clear();
						
					}else{
						System.out.println("Did not insert locally in farmer order table"+status.toString()+"::::ID:"+ID.toString());
					}
					
				}
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//FileOperations.write("Syncing exception:"+e.getMessage());
			}
		
		if(total==count){
			total = 0;
			return true;
		}else{
			total = 0;
			return false;
		}
	}
}
