package com.example.burdapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AssignFarmers extends Activity {
	String order_id,type,order_qty;
	ProgressDialog prgDialog;
	Database db;
	Button btnOk;
	TextView ord_qty, left_qty, selected_qty;
	int flag ;
	List<String> SelectedBox = new ArrayList<String>();
	int totalQty=0;
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_select_farmers);
	    prgDialog = new ProgressDialog(this);
	    flag=0;
	    db = new Database(this);
	    Bundle b = getIntent().getExtras();
		order_id = b.getString(applicationConstants.ORDER_ID);
		final ArrayList<Object> o_Details = db.getOrderById(order_id);
		// type of produce
		type = (String) o_Details.get(6);
		order_qty = (String) o_Details.get(3);
	    ArrayList<ArrayList<Object>> array_list = null;	    
	    
	    	array_list = db.getAllFarmersAsArrays();
	    	if(array_list!=null){
	    	System.out.println("array list:" + array_list);
	    	ArrayList<ArrayList<Object>> array_forder_farmer_id_list = null;
	    	array_forder_farmer_id_list = db.getFarmersOfOrder(order_id);
	    	
	    	ord_qty = (TextView) findViewById(R.id.orderQuantity);
	    	left_qty = (TextView) findViewById(R.id.quantityLeft);
	    	selected_qty = (TextView) findViewById(R.id.selectedQuantity);
	    	
	    	ord_qty.setText(order_qty);
	    	left_qty.setText(order_qty);
	    	selected_qty.setText("000");
	    	
	    	
	    	btnOk = (Button) findViewById(R.id.btnNext);	
	    	btnOk.setText(getResources().getString(R.string.go_ahead));
	    	btnOk.setOnClickListener(new View.OnClickListener() {
	    	    @Override
	    	    public void onClick(View v) {
	    	        //assign farmers to the order and proceed to next page
	    	    	
	    	    	
	    	    	if(SelectedBox.size()>0){
	    	    		int divisor = SelectedBox.size();
	    	    		int dividend = Integer.valueOf(o_Details.get(3).toString()); 
	    	    		int remainder = 0,quotient = 0;
	    	    		if(dividend>divisor){
	    	    			remainder = dividend%divisor;
	    	    			quotient = dividend/divisor;
	    	    		}
		    	    	int sub_order_price= Integer.valueOf(o_Details.get(4).toString());
		    	    	
	    	    		//make date
		    	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");
		    			Date strDate=null;
		    			
		    				try {
								strDate = sdf.parse(o_Details.get(7).toString());
								//System.out.print(strDate+": date");
							} catch (java.text.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    			
		    	    	Calendar cal = Calendar.getInstance();
		    	    	cal.setTime(strDate);
		    	    	cal.add(Calendar.DATE, -3);
		    	    	Date new_date = cal.getTime();
		    	    	String stringDay = (String) android.text.format.DateFormat.format("dd", new_date);
		    	    	String stringMonth = (String) android.text.format.DateFormat.format("MM", new_date);
		    	    	String stringYear = (String) android.text.format.DateFormat.format("yyyy", new_date);
		    	    	String sub_order_date = stringDay+":"+stringMonth+":"+stringYear;
		    	    	
		    	    	
		    	    	for(int i=0; i < SelectedBox.size(); i++){
		    	    		
		    	    		ArrayList<Object> f_det = db.getFarmerAsArray(SelectedBox.get(i));
		    	    		
		    	    		int f_qty = 0;
		    	    		if(type.equals("1"))
		    	    			f_qty= Integer.valueOf(f_det.get(5).toString());
		    		    	else if(type.equals("2"))
		    		    		f_qty= Integer.valueOf(f_det.get(1).toString());
		    		    	else if(type.equals("3"))
		    		    		f_qty= Integer.valueOf(f_det.get(6).toString());
		    		    	else if(type.equals("4"))
		    		    		f_qty= Integer.valueOf(f_det.get(13).toString());
		    	    		
		    	    		
		    	    		int qty_distribution = quotient;
		    	    		if(i==0 && remainder!=0){
		    	    			qty_distribution = qty_distribution+remainder;
		    	    			
		    	    		}
		    	    		if(f_qty < qty_distribution){
		    	    			qty_distribution = f_qty;
	    	    			}
		    	    		int price_distribution = sub_order_price - 3;
		    	    		db.assignFarmersToOrder(order_id,SelectedBox.get(i),Integer.toString(qty_distribution),Integer.toString(price_distribution),sub_order_date);
		    	    		int deducted_farmer_qty = f_qty - qty_distribution;
		    	    		db.updateFarmersQty(SelectedBox.get(i),type,Integer.toString(deducted_farmer_qty));
		    	    	}
		    	    	//update status
		    	    	//long rowId = db.updateOrderStatus("2",order_id);
		    	    	db.updateOrderStatus("3", order_id);
		    	    	Bundle b = new Bundle();
			   	    	b.putString(applicationConstants.ORDER_ID,order_id);
			   	    	Intent in = new Intent(getApplicationContext(), SubOrderAssign.class);			   	    	
			   	    	in.putExtras(b);
			   	    	startActivity(in);
		    	    	
	    	    	}
	    	        
	    	    }
	    	});
	    	btnOk.setEnabled(false);
	    	
	    	
	    final ListView listview = (ListView) findViewById(R.id.display_select_farmers);
	   // final ListView listIds = (ListView) findViewById(R.id.display_list);
	    final ArrayList<String> list_ids = new ArrayList<String>();
	    final ArrayList<String> list_mobile = new ArrayList<String>();
	    final ArrayList<String> list_names = new ArrayList<String>();
	    final ArrayList<String> list_kernel = new ArrayList<String>();
	    final ArrayList<String> list_seed = new ArrayList<String>();
	    final ArrayList<String> list_fruit = new ArrayList<String>();
	    final ArrayList<String> list_pulp = new ArrayList<String>();
	   // btnOk.setOnClickListener((OnClickListener) this);
	   // listA = new List<String>();
	    for (int i = 0; i < array_list.size(); ++i) {
	    	final ArrayList<Object> row = array_list.get(i);
	    	list_ids.add((String) row.get(5));
	    	list_mobile.add((String) row.get(6));
	    	list_names.add((String) row.get(0));
	    	if(type.equals("1"))
	    		list_kernel.add(row.get(2).toString());
	    		//list_kernel.add(Integer.toString((Integer.valueOf(row.get(2).toString())-Integer.valueOf(row.get(9).toString()))));
	    	else if(type.equals("2"))
	    		list_seed.add(row.get(1).toString());
	    		//list_seed.add(Integer.toString((Integer.valueOf(row.get(1).toString())-Integer.valueOf(row.get(10).toString()))));
	    	else if(type.equals("3"))
	    		list_fruit.add(row.get(3).toString());
	    		//list_fruit.add(Integer.toString((Integer.valueOf(row.get(3).toString())-Integer.valueOf(row.get(11).toString()))));
	    	else if(type.equals("4"))
	    		list_pulp.add(row.get(8).toString());
	    		//list_pulp.add(Integer.toString((Integer.valueOf(row.get(8).toString())-Integer.valueOf(row.get(12).toString()))));
	    }
	    
	    ArrayList<String> list_quantity = new ArrayList<String>();
		if(type.equals("1"))
    		list_quantity = list_kernel;
    	else if(type.equals("2"))
    		list_quantity = list_seed;
    	else if(type.equals("3"))
    		list_quantity = list_fruit;
    	else if(type.equals("4"))
    		list_quantity = list_pulp;
	    
	    final StableArrayAdapter lAdapter = new StableArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,
	    		list_ids,list_names,list_quantity, list_mobile,array_forder_farmer_id_list);
	    listview.setAdapter((ListAdapter) lAdapter);
	    
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	     @Override
			public void onItemClick(AdapterView<?> AdptView, final View view, int position,
					long id) {
				// TODO Auto-generated method stub
				//final String item = (String) AdptView.getItemAtPosition(position);
				final String id1 = list_ids.get(position);
				final String mobile = list_mobile.get(position);
				System.out.println("this is mob no. "+list_mobile);
				Toast.makeText(getApplicationContext(), mobile+"--"+ id1 , Toast.LENGTH_SHORT).show();
				/*Bundle b = new Bundle();
	   	    	b.putString(applicationConstants.ORDER_ID,list_ids.get(position));
	   	    	Intent in = null;*/
	   	    	/*if(status.equals("0")){
	   	    		in = new Intent(getApplicationContext(), NewOrderDetails.class);
	   	    	}else if(status.equals("1")){
	   	    		in = new Intent(getApplicationContext(), ConfirmedOrder.class);
	   	    	}
	   	    	in.putExtras(b);
	   	    	startActivity(in);*/			
				
			}

	    });
	 }
	   // View v = listview.getChildAt(1);
	  }
	
	
	
	
	  private class StableArrayAdapter extends ArrayAdapter<String> {

	    //HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	    Context context;
	    ArrayList<String> listId,listNames,listQuantity,listMobile;
	    ArrayList<ArrayList<Object>> array_forder_farmer_id_list;
	    public StableArrayAdapter(Context context, int textViewResourceId,
	    		ArrayList<String> listId,ArrayList<String> listN, ArrayList<String> listQ,
	    		ArrayList<String> listMobile,ArrayList<ArrayList<Object>> array_forder_farmer_id_list) {
	      super(context, textViewResourceId, listN);
	      this.context = context;
	      this.listId = listId;
	      this.listNames = listN;
	      this.listQuantity = listQ;
	      this.listMobile = listMobile;
	      this.array_forder_farmer_id_list = array_forder_farmer_id_list;
	    }

	    /*public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			
		}*/
	    
	   
	    
	    class MyViewHolder{
	    	TextView txtName, txtQuantity;
	    	ImageView im;
	    	CheckBox cb;
	    	public MyViewHolder(View v) {
				// TODO Auto-generated constructor stub
	    		txtName = (TextView) v.findViewById(R.id.farmerName);
	    		txtQuantity = (TextView) v.findViewById(R.id.farmerQuantity);
	    		im = (ImageView) v.findViewById(R.id.imageView1);
	    		cb = (CheckBox) v.findViewById(R.id.checkBox1);
			}
	    }
	    public View getView(final int position, View convertView, ViewGroup parent){
	    	View row = convertView;
	    	MyViewHolder holder = null;
	    	if(row == null){
	    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    		row = inflater.inflate(R.layout.single_select_farmer_row,parent,false);
	    		holder = new MyViewHolder(row);
	    		row.setTag(holder);
	    	}else{
	    		holder = (MyViewHolder) row.getTag();
	    		
	    	}
	    	
	    	holder.txtName.setText(listNames.get(position));
	    	holder.txtQuantity.setText(listQuantity.get(position)+" Kgs");
	    	holder.im.setOnClickListener(new View.OnClickListener() {
	    	    @Override
	    	    public void onClick(View v) {
	    	        //Inform the user the button has been clicked
	    	       // Toast.makeText(getApplicationContext(), "Button1 clicked.", Toast.LENGTH_SHORT).show();  
	    	        call("tel:+91"+listMobile.get(position).toString());
	    	    }
	    	});
	    	if(array_forder_farmer_id_list!=null)
	    	if(array_forder_farmer_id_list.contains(listId.get(position))){
	    		holder.cb.setChecked(true);
	    	}else{
	    		holder.cb.setChecked(false);
	    	}
	    	holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener()
	    	{
	    		@Override
	    	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	    	    {
	    	        if(isChecked)
	    	            {
	    	                SelectedBox.add(listId.get(position).toString());
	    	                totalQty += Integer.valueOf(listQuantity.get(position).toString());
	    	            }
	    	            else
	    	            {
	    	                SelectedBox.remove(listId.get(position).toString());
	    	                totalQty -= Integer.valueOf(listQuantity.get(position).toString());
	    	            }
	    	        
	    	        if(SelectedBox.isEmpty() || totalQty<Integer.valueOf(order_qty)){
	    	        	btnOk.setEnabled(false);
	    	        }else{
	    	        	btnOk.setEnabled(true);
	    	        }
	    	        int l_qty = Integer.valueOf(order_qty)-totalQty;
	    	        if(l_qty<=0){
	    	        	left_qty.setText("00");
	    	        }else{
	    	        	left_qty.setText(Integer.toString(l_qty));
	    	        }
	    	        
	    	         selected_qty.setText(Integer.toString(totalQty));
	    	    }

				
	    	});
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
	  
	  private void call(String phone_no) {
		    try {
		    	Intent callIntent = new Intent(Intent.ACTION_CALL);
		        callIntent.setData(Uri.parse(phone_no));
		        startActivity(callIntent);
		    } catch (Exception activityException) {
		         Log.e("helloandroid dialing example", "Call failed");
		    }
		}
}
