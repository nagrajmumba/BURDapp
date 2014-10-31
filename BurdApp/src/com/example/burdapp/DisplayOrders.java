package com.example.burdapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayOrders extends Activity {
	ProgressDialog prgDialog;
	Database db;
	int flag ;
	String m_id;
	
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_display_orders);
	   
	    db = new Database(this);
	    prgDialog = new ProgressDialog(this);
	    flag=0;
	    ArrayList<ArrayList<Object>> array_list = null;	    
	    db.updateFarmerOrderSynched();
	    	array_list = db.getAllOrdersAsArrays();
	    	System.out.println("array list:" + array_list);
	    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			m_id = prefs.getString(applicationConstants.MEDIATOR_ID, null);
	    final ListView listview = (ListView) findViewById(R.id.display_list);
	   // final ListView listIds = (ListView) findViewById(R.id.display_list);
	    final ArrayList<String> list_ids = new ArrayList<String>();
	    final ArrayList<String> list_status = new ArrayList<String>();
	    final ArrayList<String> list_names = new ArrayList<String>();
	    
	    //listA = new List<String>();
	    for (int i = 0; i < array_list.size(); ++i) {
	    	final ArrayList<Object> row = array_list.get(i);
	    	list_ids.add((String) row.get(0));//order id
	    	list_status.add((String) row.get(9));//order status
	    	list_names.add((String) row.get(1));//order names
	    	
	    	if(row.get(11).equals("1")){
	    		
	    	}
	    }
	   
	    
	    final StableArrayAdapter lAdapter = new StableArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,list_names);
	    listview.setAdapter((ListAdapter) lAdapter);
	    
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	     @Override
		public void onItemClick(AdapterView<?> AdptView, final View view, int position,
				long id) {
			// TODO Auto-generated method stub
			//final String item = (String) AdptView.getItemAtPosition(position);
	    	 
			final String id1 = list_ids.get(position);
			final String status = list_status.get(position);
			System.out.println("this is status "+status);
			Toast.makeText(getApplicationContext(), id1, Toast.LENGTH_SHORT).show();
			Bundle b = new Bundle();
   	    	b.putString(applicationConstants.ORDER_ID,list_ids.get(position));
   	    	Intent in = null;
   	    	if(status.equals("0")){
   	    		confirmOrderOnServer(list_ids.get(position));
   	    		//in = new Intent(getApplicationContext(), NewOrderDetails.class);
   	    	}else if(status.equals("1")){
   	    		in = new Intent(getApplicationContext(), ConfirmedOrder.class);
   	    		in.putExtras(b);
   	   	    	startActivity(in);	
   	    	}else if(status.equals("2")){
   	    		in = new Intent(getApplicationContext(), AssignFarmers.class);
   	    		in.putExtras(b);
   	   	    	startActivity(in);	
   	    	}else if(status.equals("3")){
   	    		in = new Intent(getApplicationContext(), SubOrderAssign.class);
   	    		in.putExtras(b);
   	   	    	startActivity(in);	
   	    	}else if(status.equals("4")){
   	    		in = new Intent(getApplicationContext(), ShipmentDetailsPage.class);
   	    		in.putExtras(b);
   	   	    	startActivity(in);	
   	    	}else if(status.equals("5")){
   	    		in = new Intent(getApplicationContext(), ShipmentDetailsPage.class);
   	    		in.putExtras(b);
   	   	    	startActivity(in);	
   	    	}
   	    			
			
		}

	    });
	  }
	
public void confirmOrderOnServer(final String order_id){
		
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
						
				prgDialog.show();
				params.put("mediatorJSON", composeJSONforConfirmOrder("checkOrderAvailable",order_id));
				client.get(applicationConstants.SERVER_URL+"orders.php",params ,new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String res) {
						System.out.println(res);
						prgDialog.hide();
						try {
								//JSONObject obj = (JSONObject)arr.get(i);
								JSONObject obj = new JSONObject(res);
								//int b=i+1;
								//System.out.println("HI--"+obj.get("status"));
								if(obj.get("current_status").equals("yes")){
									//add status locally....
									Toast.makeText(getApplicationContext(), "order available take it fast", Toast.LENGTH_LONG).show();
									Bundle b = new Bundle();
						   	    	b.putString(applicationConstants.ORDER_ID,order_id);
						   	    	Intent in = new Intent(getApplicationContext(), NewOrderDetails.class);			     	   	    
						   	    	in.putExtras(b);
						   	    	startActivity(in);
								}else if(obj.get("current_status").equals("no")){
									//remove order from the table and go back to orders page.
									int row = db.deleteOrder(order_id);
									Toast.makeText(getApplicationContext(), getString(R.string.order_already_taken), Toast.LENGTH_LONG).show();
									
						   	    	Intent in = new Intent(getApplicationContext(), DisplayOrders.class);	
						   	    	startActivity(in);
									
								}else if(obj.get("current_status").equals("error")){
									Toast.makeText(getApplicationContext(), "Unexpected System Error", Toast.LENGTH_LONG).show();
								}
							
							
													
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
							e.printStackTrace();
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
	public String composeJSONforConfirmOrder(String type, String order_id){
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		//ArrayList<HashMap<String, String>> mList = db.getMediator();
		//String mediatorId = mList.get(0).get(applicationConstants.MEDIATOR_ID);
		
		
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	
	    		map.put(applicationConstants.ORDER_ID, order_id);
	    		if(m_id!=null){
	    			map.put(applicationConstants.MEDIATOR_ID, m_id);
	    		}else{
	    			map.put(applicationConstants.MEDIATOR_ID, "0");
	    		}
	    		map.put("type", type);	    		
	        	wordList.add(map);
	       
	    //db.close();
		Gson gson = new GsonBuilder().create();
		//Use GSON to serialize Array List to JSON
		return gson.toJson(wordList);
	}
	
	
	  private class StableArrayAdapter extends ArrayAdapter<String> {

	    //HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	    Context context;
	    ArrayList<String> listNames;
	    public StableArrayAdapter(Context context, int textViewResourceId,
	    		ArrayList<String> listN) {
	      super(context, textViewResourceId, listN);
	      this.context = context;
	      this.listNames = listN;
	    }

	    /*public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			
		}*/
	    class MyViewHolder{
	    	TextView textV;
	    	public MyViewHolder(View v) {
				// TODO Auto-generated constructor stub
	    		textV = (TextView) v.findViewById(R.id.titleView);
			}
	    }
	    public View getView(int position, View convertView, ViewGroup parent){
	    	View row = convertView;
	    	MyViewHolder holder = null;
	    	if(row == null){
	    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    		row = inflater.inflate(R.layout.single_row,parent,false);
	    		holder = new MyViewHolder(row);
	    		row.setTag(holder);
	    	}else{
	    		holder = (MyViewHolder) row.getTag();
	    		
	    	}
	    	
	    	holder.textV.setText(listNames.get(position));
	    	return row;
	    }
	    
		@Override
	    public long getItemId(int position) {
			return position;
	      
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	  }
}
