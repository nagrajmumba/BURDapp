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
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_display_orders);
	   
	    db = new Database(this);
	    prgDialog = new ProgressDialog(this);
	    flag=0;
	    ArrayList<ArrayList<Object>> array_list = null;	    
	    
	    	array_list = db.getAllOrdersAsArrays();
	    	System.out.println("array list:" + array_list);
	   
	    final ListView listview = (ListView) findViewById(R.id.display_list);
	   // final ListView listIds = (ListView) findViewById(R.id.display_list);
	    final ArrayList<String> list_ids = new ArrayList<String>();
	    final ArrayList<String> list_names = new ArrayList<String>();
	    
	    //listA = new List<String>();
	    for (int i = 0; i < array_list.size(); ++i) {
	    	final ArrayList<Object> row = array_list.get(i);
	    	list_ids.add((String) row.get(0));
	    	list_names.add((String) row.get(1));
	    	
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
			final String item = (String) AdptView.getItemAtPosition(position);
			final String id1 = list_ids.get(position);
			Toast.makeText(getApplicationContext(), id1, Toast.LENGTH_SHORT).show();
			Bundle b = new Bundle();
   	    	b.putString(applicationConstants.ORDER_ID,list_ids.get(position));
   	    	Intent in = new Intent(getApplicationContext(), NewOrderDetails.class);			     	   	    
   	    	in.putExtras(b);
   	    	startActivity(in);			
			
		}

	    });
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
