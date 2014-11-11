package com.example.burdapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import java.util.Calendar;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.DatePicker;

public class MyExpandbleListAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private String order_qty,order_price,order_travel_cost,order_deldate,type;
	final Activity activity;
	Database db;
	TextView order_left, order_selected;
	
	Button btnNext;
	ArrayList<String> list_fids,list_fnames,list_quantity,list_occupied_quantity,list_forder_delivery,list_forder_delivery_view,list_forder_qty,list_forder_price,
						list_forder_travel_cost,list_forder_ids,list_forder_status,list_forder_received,list_assigned_qty,list_farmers_qty,list_forder_confirmed;
	public MyExpandbleListAdapter(Context c,ArrayList<String> list_fids,ArrayList<String> list_fnames,
					ArrayList<String> list_quantity,ArrayList<String> list_occupied_quantity,ArrayList<String> list_forder_delivery,ArrayList<String> list_forder_qty,
					ArrayList<String> list_forder_price,ArrayList<String> list_forder_travel_cost,ArrayList<String> list_forder_ids,
					ArrayList<String> list_forder_status,ArrayList<String> list_forder_received,ArrayList<String> list_forder_confirmed,String order_qty,String order_price,
					String order_travel_cost,String order_deldate, String type, TextView order_left, TextView order_selected,Button btnNext) {
		// TODO Auto-generated constructor stub
		this.context = c;
		this.list_fids=list_fids;
		this.list_fnames=list_fnames;
		this.list_quantity=list_quantity;
		this.list_occupied_quantity=list_occupied_quantity;
		this.list_forder_delivery=list_forder_delivery;
		//this.list_forder_delivery_view=list_forder_delivery;
		this.list_forder_qty=list_forder_qty;
		this.list_forder_price=list_forder_price;
		this.list_forder_travel_cost=list_forder_travel_cost;
		this.list_forder_ids=list_forder_ids;
		this.list_forder_status =list_forder_status;
		this.list_forder_received =list_forder_received;
		this.list_forder_confirmed =list_forder_confirmed;
		this.order_qty=order_qty;
		this.order_price=order_price;
		this.order_travel_cost=order_travel_cost;
		this.order_deldate = order_deldate;
		this.type = type;
		this.list_assigned_qty = list_forder_qty;
		this.list_farmers_qty = list_quantity;
		this.order_left = order_left;
		this.order_selected= order_selected;
		this.btnNext = btnNext;
		db = new Database(this.context);
		
		
		this.activity = (Activity) c;
		updateButtonState();
		updateQuantityInParent();
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
	class MyViewHolder{
    	TextView txtName,txtQuantity,txtDelDate,txtFarmerTotPrice;
    	EditText ed_qty,ed_price,ed_travel;
    	CheckBox cnf_it,received_it;
    	Button btnCalendar;
    	public MyViewHolder(View v) {
			// TODO Auto-generated constructor stub
    		ed_qty = (EditText) v.findViewById(R.id.qty);
    		ed_price = (EditText) v.findViewById(R.id.price);
    		ed_travel = (EditText) v.findViewById(R.id.travelprice);
    		txtDelDate = (TextView) v.findViewById(R.id.delivery_date_text);
    		txtFarmerTotPrice = (TextView) v.findViewById(R.id.farmerTotalPrice);
    		cnf_it = (CheckBox) v.findViewById(R.id.confirmed_it);
    		received_it = (CheckBox) v.findViewById(R.id.received_it);
    		btnCalendar = (Button) v.findViewById(R.id.btn_delivery_date);
		}
    }
	public void checkAllTextBoxes(Button btnCal,EditText qty,EditText price,EditText travel,TextView delDate, CheckBox cnf_it, CheckBox received_it,String str_confirmed, String str_received){
    	if(qty.getText().length()>0 && price.getText().length()>0 && travel.getText().length()>0 && (delDate.getText()!=null || !delDate.getText().equals("0"))){
    		cnf_it.setEnabled(true);
    	}else{
    		cnf_it.setEnabled(false);
    		
    	}
    	
    	
    	if(str_confirmed!=null){
    		cnf_it.setChecked(true);
    		btnCal.setEnabled(false);
    		qty.setEnabled(false);
    		price.setEnabled(false);
    		travel.setEnabled(false);
	    	if(str_received!=null){
	    		cnf_it.setEnabled(false);
	    		received_it.setChecked(true);
	    	}else{
	    		cnf_it.setEnabled(true);
	    		received_it.setChecked(false);
	    	}
    	}else{
    		cnf_it.setChecked(false);
    		btnCal.setEnabled(true);
    		qty.setEnabled(true);
    		price.setEnabled(true);
    		travel.setEnabled(true);
    		received_it.setChecked(false);
    		received_it.setEnabled(false);
    	}
    	
    	if(cnf_it.isChecked()){
    		received_it.setEnabled(true);
    	}else{
    		received_it.setEnabled(false);
    	}
	}
	public void updateFarmerTotPrice(TextView far_tot_view, Editable price, Editable qty, Editable travel_cost){
		if(price.length()>0 && qty.toString().length()>0){
			int p = Integer.valueOf(price.toString());
			int q = Integer.valueOf(qty.toString());
			int t = 0;
			if(travel_cost.toString().length()>0){
			  t = Integer.valueOf(travel_cost.toString());
			}
			far_tot_view.setText(Integer.toString(p*q+t));
		}else{
			far_tot_view.setText("0");
		}
    	//far_tot_View.setText(Integer.valueOf(price.toString())*Integer.valueOf(qty.toString()));
	}
	public String getFormatedDate(String delDate){
		if(delDate!=null && delDate.contains(":")){
			String[] strArray = delDate.split(":");
			System.out.println(strArray.toString()+delDate);
			int dday = Integer.valueOf(strArray[0].trim());
			int mmonth = Integer.valueOf(strArray[1].trim());
			int yyear = Integer.valueOf(strArray[2].trim());
			
			String d = null, m=null;
			switch(dday){
				case 1: d = "01";break;
				case 2: d = "02";break;
				case 3: d = "03";break;
				case 4: d = "04";break;
				case 5: d = "05";break;
				case 6: d = "06";break;
				case 7: d = "07";break;
				case 8: d = "08";break;
				case 9: d = "09";break;
				
			}
			
			String[] monthArray = activity.getResources().getStringArray(R.array.month_array);
			System.out.println(monthArray.toString()+": month array");
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
			if (d!=null)
			    return(d+" "+m+" "+yyear);
			else
				return(dday+" "+m+" "+yyear);
		}else{
			return "0";
		}			
		
	}
	public void updateQuantityInParent(){
		int order_int_qty = Integer.valueOf(order_qty);
		int selected_qty=0,left_qty=0;
		for(int i=0; i<list_forder_qty.size();i++){
			selected_qty += Integer.valueOf(list_forder_qty.get(i));
			left_qty =order_int_qty - Integer.valueOf(list_forder_qty.get(i));
			order_int_qty -= Integer.valueOf(list_forder_qty.get(i));
		}
		if(selected_qty==Integer.valueOf(order_qty)){
			order_selected.setTextColor(Color.BLACK);
			order_selected.setText(""+selected_qty+activity.getString(R.string.kilo));			
		}else{
			order_selected.setTextColor(Color.parseColor("#CC3333"));
			order_selected.setText(""+selected_qty+activity.getString(R.string.kilo));
		}
		if(left_qty>0){
			order_left.setTextColor(Color.parseColor("#CC3333"));
			order_left.setText(""+left_qty+activity.getString(R.string.kilo));
		}else{
			order_left.setTextColor(Color.BLACK);			
			order_left.setText("000"+activity.getString(R.string.kilo));
		}
	}
	public void updateButtonState(){
		int incr = 0;
		for(int j=0;j<list_forder_received.size();j++){
			if(list_forder_received.get(j)!=null)
				incr++;
		}
		System.out.println(incr+" incr value");
		if(incr == list_forder_received.size()){
			btnNext.setEnabled(true);
		}else{
			btnNext.setEnabled(false);
		}
			
	}
	
	public void setSubOrderStatusText(MyParentViewHolder v, int pos){
		String statusText = "";
    	if(list_forder_status.get(pos)==null || list_forder_status.get(pos).equals("0") || list_forder_status.get(pos).equals("")|| list_forder_status.get(pos).length()==0){
    		v.txtStatus.setTextColor(Color.RED);
    		statusText = activity.getString(R.string.sub_order_not_completed);
    	}else if(list_forder_status.get(pos).equals("1")){
    		v.txtStatus.setTextColor(Color.BLUE);
    		statusText = activity.getString(R.string.sub_order_confirmed);
    	}else if(list_forder_status.get(pos).equals("2")){
    		v.txtStatus.setTextColor(Color.YELLOW);
    		statusText = activity.getString(R.string.sub_order_received);
    	}
    	v.txtStatus.setText(statusText);
	}
	@Override
	public View getChildView(final int arg0, int arg1, boolean arg2, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
    	
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		row = inflater.inflate(R.layout.suborder_hidden_single_row,parent,false);
    		final MyViewHolder holder = new MyViewHolder(row);
    		final MyParentViewHolder parentHolder = new MyParentViewHolder(parent);
    		row.setTag(holder);
    	//int f_tot_price = Integer.valueOf(list_forder_qty.get(arg0)) * Integer.valueOf(list_forder_price.get(arg0));
    	holder.ed_qty.setText(list_forder_qty.get(arg0));
    	holder.ed_price.setText(list_forder_price.get(arg0));
    	if(list_forder_travel_cost.get(arg0)!=null){
    		holder.ed_travel.setText(list_forder_travel_cost.get(arg0));
    	}else{
    		holder.ed_travel.setText("0");
    	}
    	
    	
    	checkAllTextBoxes(holder.btnCalendar,holder.ed_qty,holder.ed_price,holder.ed_travel,
    						holder.txtDelDate,holder.cnf_it,holder.received_it,list_forder_confirmed.get(arg0),list_forder_received.get(arg0));
    	updateFarmerTotPrice(holder.txtFarmerTotPrice,holder.ed_price.getText(),holder.ed_qty.getText(),holder.ed_travel.getText());
    	    	
    	
    	holder.cnf_it.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				if(isChecked){
					holder.received_it.setEnabled(true);
					holder.ed_qty.setEnabled(false);
					holder.ed_price.setEnabled(false);
					holder.ed_travel.setEnabled(false);
					holder.btnCalendar.setEnabled(false);
					
				}else{
					holder.received_it.setEnabled(false);
					holder.ed_qty.setEnabled(true);
					holder.ed_price.setEnabled(true);
					holder.ed_travel.setEnabled(true);
					holder.btnCalendar.setEnabled(true);
				}
				
				Calendar cur = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss a");
				String strDate = sdf.format(cur.getTime());
				if(isChecked){
					String statusCnf = "1";
					list_forder_status.set(arg0, statusCnf);
					list_forder_confirmed.set(arg0,strDate);
					db.updateFarmerOrderConfirmed(list_forder_ids.get(arg0),strDate,"1");
					//holder.btnCalendar.
				}else{
					String statusCnf = "0";
					list_forder_status.set(arg0, statusCnf);
					list_forder_confirmed.set(arg0,null);
					db.updateFarmerOrderConfirmed(list_forder_ids.get(arg0),null,"0");
					db.updateFarmerOrderReceived(list_forder_ids.get(arg0),null,"0");
				}
				setSubOrderStatusText(parentHolder, arg0);
			}
		});
    	
    	holder.received_it.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				if(isChecked){
					holder.cnf_it.setEnabled(false);
					
				}else{
					holder.cnf_it.setEnabled(true);
					
				}
				
				
				Calendar cur = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss a");
				String strDate = sdf.format(cur.getTime());
				if(isChecked){
					String statusRec = "2";
					list_forder_status.set(arg0, statusRec);
					list_forder_received.set(arg0,strDate);
					db.updateFarmerOrderReceived(list_forder_ids.get(arg0),strDate,"2");
					//holder.btnCalendar.
				}else{
					String statusRec = "1";
					list_forder_status.set(arg0, statusRec);
					list_forder_received.set(arg0,null);
					db.updateFarmerOrderReceived(list_forder_ids.get(arg0),null,"1");
				}
				
				updateButtonState();
				setSubOrderStatusText(parentHolder, arg0);
			}
		});
    	
    	//getting the proper text for the date
    	String strDelDate = getFormatedDate(list_forder_delivery.get(arg0));    	
    	holder.txtDelDate.setText(strDelDate);
    	
    	holder.btnCalendar.setOnClickListener(new OnClickListener() {
	       	 
	   	     @Override
	   	     public void onClick(View v) {
	   	    //	showDatePickerDialog(v);
	   	    	 //order=order_id;
	   	    	// datepick=1;
	   	    	DialogFragment newFragment = new DatePickerFragment(arg0,list_forder_delivery.get(arg0),order_deldate,holder.txtDelDate);
		        newFragment.show(activity.getFragmentManager(), "datePicker");
	   	 }
  	   });
    	holder.ed_qty.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.toString().length()>0){
					int assQty = Integer.valueOf(list_assigned_qty.get(arg0));//Integer.valueOf(list_quantity.get(arg0));
					int typedQty = Integer.valueOf(s.toString());
					int farmersQty = Integer.valueOf(list_farmers_qty.get(arg0));
					
					int diffQty = assQty - typedQty;
					int updtQty = farmersQty + diffQty;
					
					//
					//System.out.println(updtQty+"=new qty:::"+assQty+"=assigned qty:::"+diffQty+"=diff between ass and typed qty:::"+farmersQty+"=farmers qty");
					int totalqty = Integer.valueOf(list_forder_qty.get(arg0)) + Integer.valueOf(list_quantity.get(arg0));
					if(Integer.valueOf(s.toString())>totalqty)
					{
						holder.ed_qty.setError(activity.getResources().getText(R.string.available_quantities)+" "+totalqty);
					}else{
						list_quantity.set(arg0,Integer.toString(updtQty));
						db.updateFarmersQty(list_fids.get(arg0),type,Integer.toString(updtQty));
						list_forder_qty.set(arg0, s.toString());
						db.updateFarmerOrderQty(list_forder_ids.get(arg0),s.toString());					
						
					}
					
				}
				checkAllTextBoxes(holder.btnCalendar,holder.ed_qty,holder.ed_price,holder.ed_travel,holder.txtDelDate,
						holder.cnf_it,holder.received_it,list_forder_confirmed.get(arg0),list_forder_received.get(arg0));
				updateFarmerTotPrice(holder.txtFarmerTotPrice,holder.ed_price.getText(),holder.ed_qty.getText(),holder.ed_travel.getText());
				//System.out.print("FOcus changed"+list_forder_ids.set(arg0, this.toString()));
				
				updateQuantityInParent();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
    	holder.ed_price.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.toString().length()>0)
				if(Integer.valueOf(s.toString())>Integer.valueOf(order_price))
				{
					holder.ed_price.setError(activity.getResources().getText(R.string.order_price)+order_price);
				}else{
					list_forder_price.set(arg0, s.toString());
					db.updateFarmerOrderPrice(list_forder_ids.get(arg0),s.toString());
				}
				checkAllTextBoxes(holder.btnCalendar,holder.ed_qty,holder.ed_price,holder.ed_travel,holder.txtDelDate,
						holder.cnf_it,holder.received_it,list_forder_confirmed.get(arg0),list_forder_received.get(arg0));
				updateFarmerTotPrice(holder.txtFarmerTotPrice,holder.ed_price.getText(),holder.ed_qty.getText(),holder.ed_travel.getText());
				//System.out.print("FOcus changed"+list_forder_ids.set(arg0, this.toString()));
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	holder.ed_travel.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				list_forder_travel_cost.set(arg0, s.toString());
				db.updateFarmerOrderTravelCost(list_forder_ids.get(arg0),s.toString());	
				checkAllTextBoxes(holder.btnCalendar,holder.ed_qty,holder.ed_price,holder.ed_travel,holder.txtDelDate,
						holder.cnf_it,holder.received_it,list_forder_confirmed.get(arg0),list_forder_received.get(arg0));
				updateFarmerTotPrice(holder.txtFarmerTotPrice,holder.ed_price.getText(),holder.ed_qty.getText(),holder.ed_travel.getText());
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	
    	/*if(){
    		
    	}else{
    		
    	}*/
		return row;
	}
	
	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list_fnames.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	class MyParentViewHolder{
    	TextView txtName, txtQuantity, txtStatus;
    	ImageView im;
    	CheckBox cb;
    	public MyParentViewHolder(View v) {
			// TODO Auto-generated constructor stub
    		txtName = (TextView) v.findViewById(R.id.textView1);
    		txtQuantity = (TextView) v.findViewById(R.id.textView2);
    		txtStatus = (TextView) v.findViewById(R.id.textView3);
    		/*txtQuantity = (TextView) v.findViewById(R.id.farmerQuantity);
    		im = (ImageView) v.findViewById(R.id.imageView1);
    		cb = (CheckBox) v.findViewById(R.id.checkBox1);*/
		}
    }	
	
	@Override
	public View getGroupView(int arg0, boolean arg1, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		//ViewGroup parentGroup = parent;
		MyParentViewHolder holder = null;
    	//if(row == null){
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		row = inflater.inflate(R.layout.suborder_single_row,parent,false);
    		holder = new MyParentViewHolder(row);
    		row.setTag(holder);
    		
    		//MyParentGroupViewHolder pgV = new MyParentGroupViewHolder(parent);
    		//holder.txtStatus.setText(Integer.toString(parent.getChildCount()));
    		//TextView tv = (TextView) parent.getChildAt(R.id.selectedQuantity);
    		//tv.setText("hallo");
    		
    	//}else{
    	//	holder = (MyParentViewHolder) row.getTag();
    		
    	//}
    	holder.txtName.setText(list_fnames.get(arg0));
    	   	int totalqty = Integer.valueOf(list_forder_qty.get(arg0)) + Integer.valueOf(list_quantity.get(arg0));
    	String stmtqty = list_forder_qty.get(arg0)+activity.getString(R.string.kilo)+"/"+totalqty+activity.getString(R.string.kilo);
    	holder.txtQuantity.setText(stmtqty);
    	//Toast.makeText(activity, list_forder_status.get(arg0).toString()+": status", Toast.LENGTH_LONG).show();
    	setSubOrderStatusText(holder, arg0);
    	
    	
		return row;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	//class 
	 @SuppressLint("ValidFragment")
		public class DatePickerFragment extends DialogFragment
		 implements DatePickerDialog.OnDateSetListener {
			 int pos;
			 TextView t;
			 String subdeldate;
			 String order_dDate;
			 public DatePickerFragment(int arg0, String sub_order_deldate, String order_deldate, TextView t) {
			// TODO Auto-generated constructor stub
				 this.pos = arg0;
				 this.subdeldate = sub_order_deldate;
				 this.order_dDate = order_deldate;
				 this.t = t;
		}

			@Override
		 public Dialog onCreateDialog(Bundle savedInstanceState) {
		 // Use the current date as the default date in the picker
		 
		 final Calendar c = Calendar.getInstance();
		 int year =  c.get(Calendar.YEAR);
		 int month = c.get(Calendar.MONTH);
		 int day = c.get(Calendar.DAY_OF_MONTH);
		 
		SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
			Date strDate=null;
			System.out.println(subdeldate);
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
			
			
	 // Create a new instance of DatePickerDialog and return it
		 return new DatePickerDialog(getActivity(), this, stringYear, stringMonth, stringDay);

		 }
			public void onDateChanged(DatePicker view, int year, int month, int stringDay){
				
			}
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				// TODO Auto-generated method stub
				final Calendar c = Calendar.getInstance();
				
				
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
				 String[] monthArray = activity.getResources().getStringArray(R.array.month_array);
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
				 list_forder_delivery.set(pos, dateStr);
				
				 db.updateFarmerOrderDeliveryDate(list_forder_ids.get(pos), dateStr);
				 t.setText(viewDateStr);
			}
		 

		 }

}
