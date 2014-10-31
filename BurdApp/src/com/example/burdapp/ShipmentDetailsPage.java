package com.example.burdapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.burdapp.MyExpandbleListAdapter.DatePickerFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class ShipmentDetailsPage extends Activity{
	
	String order_id,order_status;
	Button btnOrderClosed;
	Database db;
	ProgressDialog prgDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shipment_details_page);
		prgDialog = new ProgressDialog(this);
	    int flag=0;
	    db = new Database(this);
	    Bundle b = getIntent().getExtras();
		order_id = b.getString(applicationConstants.ORDER_ID);
		final ArrayList<Object> o_Details = db.getOrderById(order_id);
		
		order_status = (String) o_Details.get(9);
		if(order_status.equals("4")){
			Button btnSentDate, btnApproxDelDate;
			final TextView tvSentDate, tvApproxDelDate;
			
			btnSentDate = (Button) findViewById(R.id.btn_sent_date);
			btnApproxDelDate = (Button) findViewById(R.id.btn_approximate_delivery_date);
			btnOrderClosed = (Button) findViewById(R.id.btnOrderClose);
			
			tvSentDate = (TextView) findViewById(R.id.delivery_sent_date_text);
			tvApproxDelDate = (TextView) findViewById(R.id.approximate_delivery_date_text);
			
			btnOrderClosed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(tvSentDate.getText().length()>0 && tvApproxDelDate.getText().length()>0){
						db.updateOrderShipment(order_id,tvSentDate.toString(),tvApproxDelDate.toString());
						db.updateOrderStatus("5", order_id);
						finish();
						Bundle b = new Bundle();
			   	    	b.putString(applicationConstants.ORDER_ID,order_id);
			   	    	Intent in = new Intent(getApplicationContext(), ShipmentDetailsPage.class);			   	    	
			   	    	in.putExtras(b);
			   	    	startActivity(in);
					}else{
						if(tvSentDate.getText().length()<=0){
							tvSentDate.setError("error");
							Toast.makeText(getApplicationContext(), "Bhejne ki tareek chune", Toast.LENGTH_SHORT).show();
						}
						if(tvApproxDelDate.getText().length()<=0){
							tvSentDate.setError("error");
							Toast.makeText(getApplicationContext(), "pahunch ki tareek chune", Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
			
			btnSentDate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DialogFragment newFragment = new DatePickerFragment(tvSentDate);
			        newFragment.show(getFragmentManager(), "datePicker");
				}
			});
			
			btnApproxDelDate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DialogFragment newFragment = new DatePickerFragment(tvApproxDelDate);
			        newFragment.show(getFragmentManager(), "datePicker");
				}
			});
		}else if(order_status.equals("5")){
			TextView heading = (TextView) findViewById(R.id.statusText);
			heading.setText("Order has been closed");
		}
		
	}
	
	 @SuppressLint("ValidFragment")
		public class DatePickerFragment extends DialogFragment
		 implements DatePickerDialog.OnDateSetListener {
			 int pos;
			 TextView t;
			 String subdeldate;
			 String order_dDate;
			 public DatePickerFragment(TextView tv) {
			// TODO Auto-generated constructor stub
				 /*this.pos = arg0;
				 this.subdeldate = sub_order_deldate;
				 this.order_dDate = order_deldate;*/
				 this.t = tv;
		}

			@Override
		 public Dialog onCreateDialog(Bundle savedInstanceState) {
		 // Use the current date as the default date in the picker
		 
		 final Calendar c = Calendar.getInstance();
		 int year =  c.get(Calendar.YEAR);
		 int month = c.get(Calendar.MONTH);
		 int day = c.get(Calendar.DAY_OF_MONTH);
		 
		/*SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
			Date strDate=null;
			//System.out.println(subdeldate);
			try {
				strDate = sdf.parse(subdeldate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(strDate);
			//cal.add(Calendar.DATE, -30);
			Date dateBefore30Days = cal.getTime();
			//String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", strDate);//Thursday
			//String stringMonth = (String) android.text.format.DateFormat.format("MMM", strDate); //Jun
			int stringMonth = cal.get(Calendar.MONTH); //06
			int stringYear =  cal.get(Calendar.YEAR); //2013
			int stringDay = cal.get(Calendar.DAY_OF_MONTH); //20
*/			
			
	 // Create a new instance of DatePickerDialog and return it
		 return new DatePickerDialog(getActivity(), this, year, month, day);

		 }
			public void onDateChanged(DatePicker view, int year, int month, int stringDay){
				
			}
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				// TODO Auto-generated method stub
				String m=null;
				 String month_num="";
				String[] monthArray = this.getResources().getStringArray(R.array.month_array);
				 switch(month){
				 
					 case 0: m=monthArray[0]; month_num="01"; break;
					 case 1: m=monthArray[1];month_num="02";break;
					 case 2: m=monthArray[2];month_num="03";break; 
					 case 3: m=monthArray[3];month_num="04";break;
					 case 4: m=monthArray[4];month_num="05";break;
					 case 5: m=monthArray[5];month_num="06";break;
					 case 6: m=monthArray[6];month_num="07";break;
					 case 7: m=monthArray[7];month_num="08";break; 
					 case 8: m=monthArray[8];month_num="09";break;
					 case 9: m=monthArray[9];month_num="10";break;
					 case 10: m=monthArray[10];month_num="11";break;
					 default: m=monthArray[11];month_num="12";break;
					 
				 }
				 String viewDateStr = day+" "+m+" "+year;
				 t.setText(viewDateStr);
				
				/*final Calendar c = Calendar.getInstance();
				
				
				 int cyear =  c.get(Calendar.YEAR);
				 int cmonth = c.get(Calendar.MONTH);
				 int cday = c.get(Calendar.DAY_OF_MONTH);
				 int chours = c.get(Calendar.HOUR);
				 int cminutes = c.get(Calendar.MINUTE);
				 int cseconds = c.get(Calendar.SECOND);
				 int cam_pm = c.get(Calendar.AM_PM);
				 String m=null;
				 String month_num="";
				 //"dd:MM:yyyy HH:mm:ss a" 
				 String[] monthArray = this.getResources().getStringArray(R.array.month_array);
				 switch(month){
				 
					 case 0: m=monthArray[0]; month_num="01"; break;
					 case 1: m=monthArray[1];month_num="02";break;
					 case 2: m=monthArray[2];month_num="03";break; 
					 case 3: m=monthArray[3];month_num="04";break;
					 case 4: m=monthArray[4];month_num="05";break;
					 case 5: m=monthArray[5];month_num="06";break;
					 case 6: m=monthArray[6];month_num="07";break;
					 case 7: m=monthArray[7];month_num="08";break; 
					 case 8: m=monthArray[8];month_num="09";break;
					 case 9: m=monthArray[9];month_num="10";break;
					 case 10: m=monthArray[10];month_num="11";break;
					 default: m=monthArray[11];month_num="12";break;
					 
				 }
				 String dateStr = day+":"+month_num+":"+year;
				 String viewDateStr = day+" "+m+" "+year;
				// list_forder_delivery.set(pos, dateStr);
				
				// db.updateFarmerOrderDeliveryDate(list_forder_ids.get(pos), dateStr);
				 t.setText(viewDateStr);*/
			}
		 

	 }
}
