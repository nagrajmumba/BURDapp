package com.example.burdapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmedOrder extends Activity implements OnClickListener{
	String order_id;
	Database db;
	ProgressDialog prgDialog;
	TextView orderNum,orderName, orderType, orderQuantity, orderPrice, orderDelivery, availableQuantity;
	Button btnSelectFarmer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmed_order);
	    db = new Database(this);
	    Bundle b = getIntent().getExtras();
		order_id = b.getString(applicationConstants.ORDER_ID);
		
		ArrayList<Object> o_Details = db.getOrderById(order_id);		
		String total = db.getTotalAvailableQuantity(o_Details.get(6).toString());
		orderNum	= (TextView) findViewById(R.id.orderNum);
		orderName = (TextView) findViewById(R.id.orderName);
		orderType = (TextView) findViewById(R.id.orderType);
		orderQuantity = (TextView) findViewById(R.id.orderQuantity);
		orderPrice = (TextView) findViewById(R.id.orderPrice);
		orderDelivery = (TextView) findViewById(R.id.orderDelivery);
		availableQuantity = (TextView) findViewById(R.id.availableAmount);
		
		btnSelectFarmer = (Button) findViewById(R.id.btnSelectFarmer);	
		btnSelectFarmer.setText(getString(R.string.select_farmer));
		btnSelectFarmer.setOnClickListener(this);
		
		orderNum.setText((CharSequence) o_Details.get(0));
		orderName.setText((CharSequence) o_Details.get(1));
		
		String strTyp="";
		if(o_Details.get(6).equals("1")){
    		strTyp = getString(R.string.kernel);
    	}else if(o_Details.get(6).equals("2")){
    		strTyp = getString(R.string.seed);
    	}else if(o_Details.get(6).equals("3")){
    		strTyp = getString(R.string.fruit);
    	}else if(o_Details.get(6).equals("4")){
    		strTyp = getString(R.string.pulp);
    	}
		orderType.setText(strTyp);
		
		orderQuantity.setText((CharSequence) o_Details.get(3)+" "+getString(R.string.kilo));
		orderPrice.setText((CharSequence) o_Details.get(4)+" "+getString(R.string.rupee_per_kg));
		//orderDelivery.setText((CharSequence) o_Details.get(7));
		if(total==null || total.equals("0") || total.equals("")){
			total="000";
			btnSelectFarmer.setEnabled(false);
		}else{
			btnSelectFarmer.setEnabled(true);
		}
		availableQuantity.setText(total+" "+getString(R.string.kilo));
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
    	//cal.add(Calendar.DATE, -3);
    	Date new_date = cal.getTime();
    	String stringDay = (String) android.text.format.DateFormat.format("dd", new_date);
    	String stringMonth = (String) android.text.format.DateFormat.format("MM", new_date);
    	String stringYear = (String) android.text.format.DateFormat.format("yyyy", new_date);
    	
    	int mmonth = Integer.valueOf(stringMonth);
    	String m="";
    	String[] monthArray = getResources().getStringArray(R.array.month_array);
    	
    	switch(mmonth){
		case 1: m=monthArray[0];break;
		 case 2: m=monthArray[1];break;
		 case 3: m=monthArray[2];break; 
		 case 4: m=monthArray[3];break;
		 case 5: m=monthArray[4];break;
		 case 6: m=monthArray[5];break;
		 case 7: m=monthArray[6];break;
		 case 8: m=monthArray[7];break; 
		 case 9: m=monthArray[8];break;
		 case 10: m=monthArray[9];break;
		 case 11: m=monthArray[10];break;
		 case 12: m=monthArray[11];break;
		}
    	
    	String sub_order_date = stringDay+" "+m+" "+stringYear;
    	orderDelivery.setText(sub_order_date);
		
		prgDialog = new ProgressDialog(this);
 	    prgDialog.setMessage(getString(R.string.please_wait));
		prgDialog.setCancelable(false);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.btnSelectFarmer:
			db.updateOrderStatus("2", order_id);
			Bundle b = new Bundle();
   	    	b.putString(applicationConstants.ORDER_ID,order_id);
   	    	Intent in = new Intent(getApplicationContext(), AssignFarmers.class);
   	    	in.putExtras(b);
   	    	startActivity(in);	
			break;		
		default:
			break;
		}
	}
}
