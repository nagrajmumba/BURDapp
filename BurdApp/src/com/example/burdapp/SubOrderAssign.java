package com.example.burdapp;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class SubOrderAssign extends Activity {
	
	ExpandableListView exV;	
	String order_id,type,order_price,order_qty,order_deldate,order_travel_cost,order_name;
	ProgressDialog prgDialog;
	TextView txtordername;
	Database db;
	  @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_order_assign_activity);
		db = new Database(this);
	    Bundle b = getIntent().getExtras();
		order_id = b.getString(applicationConstants.ORDER_ID);
		ArrayList<Object> o_Details = db.getOrderById(order_id);
		type = (String) o_Details.get(6);
		order_price = (String) o_Details.get(4);
		order_qty = (String) o_Details.get(3);
		order_deldate = (String) o_Details.get(7);
		order_travel_cost = (String) o_Details.get(5);
		order_name = (String) o_Details.get(1);
		
		ArrayList<ArrayList<Object>> array_list = null;
    	array_list = db.getFarmersOfOrder(order_id);
    	final ArrayList<String> list_fids = new ArrayList<String>();
    	final ArrayList<String> list_fnames = new ArrayList<String>();
    	final ArrayList<String> list_fseeds_qty = new ArrayList<String>();
    	final ArrayList<String> list_fkernels_qty = new ArrayList<String>();
    	final ArrayList<String> list_ffruits_qty = new ArrayList<String>();
    	final ArrayList<String> list_fpulp_qty = new ArrayList<String>();
    	final ArrayList<String> list_focc_seeds_qty = new ArrayList<String>();
    	final ArrayList<String> list_focc_kernel_qty = new ArrayList<String>();
    	final ArrayList<String> list_focc_fruit_qty = new ArrayList<String>();
    	final ArrayList<String> list_focc_pulp_qty = new ArrayList<String>();
    	final ArrayList<String> list_forder_delivery = new ArrayList<String>();
    	final ArrayList<String> list_forder_qty = new ArrayList<String>();
    	final ArrayList<String> list_forder_price = new ArrayList<String>();
    	final ArrayList<String> list_forder_travel_cost = new ArrayList<String>();
    	final ArrayList<String> list_forder_ids = new ArrayList<String>();
    	final ArrayList<String> list_forder_status = new ArrayList<String>();
    	final ArrayList<String> list_forder_received = new ArrayList<String>();
    	
    	 for (int i = 0; i < array_list.size(); ++i) {
 	    	final ArrayList<Object> row = array_list.get(i);
 	    	list_fids.add((String) row.get(0));
 	    	list_fnames.add((String) row.get(1)); 	    	
 	    	if(type.equals("1")){
 	    		list_fkernels_qty.add(row.get(2).toString());
 	    		list_focc_kernel_qty.add(Integer.toString((Integer.valueOf(row.get(2).toString())-Integer.valueOf(row.get(13).toString()))));
 	    	}else if(type.equals("2")){
 	    		list_fseeds_qty.add(row.get(3).toString());
 	    		list_focc_seeds_qty.add(Integer.toString((Integer.valueOf(row.get(3).toString())-Integer.valueOf(row.get(14).toString()))));
 	    	}else if(type.equals("3")){
 	    		list_ffruits_qty.add(row.get(4).toString());
 	    		list_focc_fruit_qty.add(Integer.toString((Integer.valueOf(row.get(4).toString())-Integer.valueOf(row.get(15).toString()))));
 	    	}else if(type.equals("4")){
 	    		list_fpulp_qty.add(row.get(12).toString());
 	    		list_focc_pulp_qty.add(Integer.toString((Integer.valueOf(row.get(12).toString())-Integer.valueOf(row.get(16).toString()))));
 	    	}
 	    	
 	    	list_forder_delivery.add((String) row.get(5));
 	    	list_forder_qty.add((String) row.get(6));
 	    	list_forder_price.add((String) row.get(7));
 	    	list_forder_travel_cost.add((String) row.get(8));
 	    	list_forder_ids.add((String) row.get(9));
 	    	list_forder_status.add((String) row.get(10));
 	    	list_forder_received.add((String) row.get(11));
 	    }
    	
    	 ArrayList<String> list_quantity = new ArrayList<String>();
    	 ArrayList<String> list_occupied_quantity = new ArrayList<String>();
 		if(type.equals("1")){
     		list_quantity = list_fkernels_qty;
 			list_occupied_quantity = list_focc_kernel_qty;
 		}
     	else if(type.equals("2")){
     		list_quantity = list_fseeds_qty;
     		list_occupied_quantity = list_focc_seeds_qty;
     	}
     	else if(type.equals("3")){
     		list_quantity = list_ffruits_qty;
     		list_occupied_quantity = list_focc_fruit_qty;
     	}else if(type.equals("4")){
     		list_quantity = list_fpulp_qty;
     		list_occupied_quantity = list_focc_pulp_qty;
     	}
    	 
		exV= (ExpandableListView) findViewById(R.id.expandableListView1);
		exV.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		txtordername = (TextView) findViewById(R.id.txtordername);
		String temp = (String) txtordername.getText();
		txtordername.setText(temp+" : "+order_name);
		exV.setAdapter(new MyExpandbleListAdapter(this,list_fids,list_fnames,list_quantity,list_occupied_quantity,list_forder_delivery,
												list_forder_qty,list_forder_price,list_forder_travel_cost,
												list_forder_ids,list_forder_status,list_forder_received,order_qty,order_price,
												order_travel_cost,order_deldate,type));
		
	}
	  
	  public int GetPixelFromDips(float pixels) {
		     // Get the screen's density scale 
		     final float scale = getResources().getDisplayMetrics().density;
		     // Convert the dps to pixels, based on density scale
		     return (int) (pixels * scale + 0.5f);

		    }

		  @SuppressLint("NewApi") @Override
		    public void onWindowFocusChanged(boolean hasFocus) {
		        super.onWindowFocusChanged(hasFocus);
		        DisplayMetrics metrics = new DisplayMetrics();
		        getWindowManager().getDefaultDisplay().getMetrics(metrics);
		        int width = metrics.widthPixels; 
		        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
		        	exV.setIndicatorBounds(width-GetPixelFromDips(35), width-GetPixelFromDips(5));
		        	//exV.setClickable(true);
		        	//exV.expandGroup(groupPos)
				} else {
					exV.setIndicatorBoundsRelative(width-GetPixelFromDips(35), width-GetPixelFromDips(5));
		        }
		    }
}