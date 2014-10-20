package com.example.burdapp;

import java.util.ArrayList;

import com.example.burdapp.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class FarmerDetailsPage extends Activity implements OnClickListener{
	Database db;
	String farmerId;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {	
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.farmer_details_page);
    	db = new Database(getApplicationContext());
    	
    	Intent it = getIntent();
		Bundle b= it.getExtras();
		farmerId = b.getString("farmer_id");
    	farmerDetailsPage(farmerId);
    	
    	
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.edit_menu_action, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_edit:
	            //openSearch();
	        	Intent in = new Intent(getApplicationContext(),FarmerRegistration.class);
	        	Bundle b_param = new Bundle();
	        	b_param.putString("farmer_id", farmerId);
	        	in.putExtras(b_param);
	        	startActivity(in);
	            return true;	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public void farmerDetailsPage(final String f_id) {
		String order_no = null;
		
		ArrayList<ArrayList<Object>> data = db.getFarmerHistory(f_id);
		final ArrayList<Object> row;
		row = db.getFarmerAsArray(f_id);
		ActionBar actionBar = getActionBar();
    	actionBar.setTitle(row.get(0).toString());	
    	
    	Button call =(Button)findViewById(R.id.call);
    	Button msg =(Button)findViewById(R.id.msg);
    	Button edit =(Button)findViewById(R.id.edit);
    	Button order=(Button)findViewById(R.id.order_id);
    	TextView pay=(TextView)findViewById(R.id.payment);
    	
    	for (int position=0; position < data.size(); position++)
    	{
    		 final ArrayList<Object> f_order = data.get(position);
    		 order.setText(f_order.get(0).toString());
    		 order_no=f_order.get(0).toString();
    		 pay.setText(f_order.get(1).toString());
    	}
    	
    	final String order_id= order_no;
    	TextView textMobile =(TextView)findViewById(R.id.mobile_no);
    	SpannableString textM = new SpannableString(getString(R.string.mobile) +"\n"+row.get(7).toString());  
    	textM.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 6, 0);  
    	textMobile.setText(textM, BufferType.SPANNABLE);
    	textMobile.setGravity(Gravity.CENTER_VERTICAL);
    	
    	TextView textAddress =(TextView)findViewById(R.id.address);
    	SpannableString textA = new SpannableString(getString(R.string.home_address) +"\n"+ row.get(8).toString());  
    	textA.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 9, 0);  
    	textAddress.setText(textA, BufferType.SPANNABLE);
    	textAddress.setGravity(Gravity.CENTER_VERTICAL);
    	
    	TextView textKernel =(TextView)findViewById(R.id.kernel);
    	textKernel.setText(row.get(5).toString());
    	textKernel.setGravity(Gravity.CENTER_VERTICAL);
    	
    	TextView textSeed =(TextView)findViewById(R.id.seed);
    	textSeed.setText(row.get(1).toString());
    	textSeed.setGravity(Gravity.CENTER_VERTICAL);
    	
    	TextView textFruit =(TextView)findViewById(R.id.fruit);
    	textFruit.setText(row.get(6).toString());
    	textFruit.setGravity(Gravity.CENTER_VERTICAL);
    	
    	
    	
    	call.setOnClickListener(new OnClickListener() {
	       	 
	   	     @Override
	   	     public void onClick(View v) {
	   	    	//Log.v(fname,row.get(7).toString() );
	   	    	call("tel:+91"+row.get(7).toString());
	   	 }
   	   });
    	
    	order.setOnClickListener(new OnClickListener() {
	       	 
	   	     @Override
	   	     public void onClick(View v) {
	   	    	//backStack.push("farmerDetailsPage()");
	   	    	//onAddFarmer(order_id);
	   	    	//TODO ADD onBackPress
	   	 }
  	   });
    	
    	
    	msg.setOnClickListener(new OnClickListener() {
	       	 
	   	     @Override
	   	     public void onClick(View v) {
	   	    	//Log.v(fname,row.get(7).toString() );
	   	    	sendSMS("tel:+91"+row.get(7).toString());
	   	 }
  	   });
    	
    	edit.setOnClickListener(new OnClickListener() {
	       	 
	   	     @Override
	   	     public void onClick(View v) {
	   	    	//Log.v(fname,row.get(7).toString() );
	   	    	//Log.v("push", "farmerDetailsPage()");
	   	    	//backStack.push("farmerDetailsPage()");
	   	    	//editFarmer(fname);
	   	    	
	   	 }
  	   });
		
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
	    
	  
	    
	    private void sendSMS(String phoneNumber)
	    {        
	    	String message = "Hello World!";
	        PendingIntent pi = PendingIntent.getActivity(this, 0,new Intent(this, Farmers.class), 0);                
	        SmsManager sms = SmsManager.getDefault();
	        //sms.sendTextMessage(phoneNumber, null, message, pi, null);        
	    }
}
