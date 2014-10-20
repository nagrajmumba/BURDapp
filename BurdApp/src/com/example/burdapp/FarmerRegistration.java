package com.example.burdapp;

import java.util.ArrayList;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

public class FarmerRegistration extends Activity implements OnClickListener {
	public static String F_NAME="",F_MOB="",F_STREET="",F_LANDMARK="",F_CITY="",F_STATE="",F_PINCODE="",F_KERNEL="",F_SEED="",F_FRUIT="",F_ADDRESS="";
	Button btnAccept, btnReject;
	Database db;
	EditText f_name, f_mobile,f_city, f_state, f_pincode,f_seed,f_fruit,f_kernel,f_street,f_landmark;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {	
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.new_farmer_registration);
    	db = new Database(this);
    	f_name = (EditText)findViewById(R.id.farmerName);
    	f_mobile = (EditText)findViewById(R.id.mobile);
    	f_street = (EditText)findViewById(R.id.street);
    	f_landmark = (EditText)findViewById(R.id.landmark);
    	f_state = (EditText)findViewById(R.id.state);
    	f_city = (EditText)findViewById(R.id.city);
    	f_pincode = (EditText)findViewById(R.id.pincode);
    	f_seed = (EditText)findViewById(R.id.seed);
    	f_fruit = (EditText)findViewById(R.id.fruit);
    	f_kernel = (EditText)findViewById(R.id.kernel);
    	
    	
    	
    	
    	btnAccept = (Button)findViewById(R.id.registerfarmer);
    	btnReject = (Button)findViewById(R.id.cancelfarmer);
    	
    	
    	//-----------------for editing the farmer details 
    	Intent editIn = getIntent();
    	Bundle b= editIn.getExtras();
		if(b!=null){
			String editFarmerId = b.getString("farmer_id");
			Toast.makeText(getApplicationContext(), editFarmerId, Toast.LENGTH_LONG).show();
			final ArrayList<Object> row;
			row = db.getFarmerAsArray(editFarmerId);
			ActionBar actionBar = getActionBar();
	    	actionBar.setTitle(row.get(0).toString());
	    	f_name.setText(row.get(0).toString());
	    	f_mobile.setText(row.get(7).toString());
	    	f_street.setText(row.get(9).toString());
	    	f_landmark.setText(row.get(10).toString());
	    	f_city.setText(row.get(11).toString());
	    	f_state.setText(row.get(12).toString());
	    	f_pincode.setText(row.get(2).toString());
	    	f_seed.setText(row.get(1).toString());
	    	f_kernel.setText(row.get(5).toString());
	    	f_fruit.setText(row.get(6).toString());
	    	
	    	btnAccept.setText(getString(R.string.change_it));
	    	btnReject.setText(getString(R.string.reject));
		}
		
		
    	btnAccept.setOnClickListener(this);
    	btnReject.setOnClickListener(this);
    	
    	 Spinner spinner = (Spinner) findViewById(R.id.telephone_spinner);
         // Create an ArrayAdapter using the string array and a default spinner layout
         ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                 R.array.tel_array, android.R.layout.simple_spinner_item);
         // Specify the layout to use when the list of choices appears
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         // Apply the adapter to the spinner
         spinner.setAdapter(adapter);
    	
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent editIn = getIntent();
    	Bundle b= editIn.getExtras();
		switch(v.getId()){
		case R.id.registerfarmer:			
				Boolean isEmpty = validateAllFields();
				if(!isEmpty){
					if(b!=null){
						String f_id = b.getString("farmer_id");
						AlertDialog dBox = AskOption(getString(R.string.accept_confirmation_for_edit_msg),1,f_id,getString(R.string.confirm_it), getString(R.string.yes), getString(R.string.no));
						dBox.show();				
					}else{
						AlertDialog dBox = AskOption(getString(R.string.accept_confirmation_msg),1,"0",getString(R.string.confirm_it), getString(R.string.yes), getString(R.string.no));
						dBox.show();
					}
				}else{
					return;
				}			
			break;
		case R.id.cancelfarmer:
			if(b!=null){
				String f_id = b.getString("farmer_id");
				AlertDialog dBox1 = AskOption(getString(R.string.reject_confirmation_for_edit_msg),2,f_id,getString(R.string.confirm_it), getString(R.string.yes), getString(R.string.no));
				dBox1.show();
			}else{
				AlertDialog dBox1 = AskOption(getString(R.string.no_change),2,"0",getString(R.string.confirm_it),  getString(R.string.yes), getString(R.string.no));
				dBox1.show();
			}
			break;
		default:
			break;
		}
	}
	 private AlertDialog AskOption(String msg, final int flag, final String f_id,String title, String btnYesText, String btnNoText)
	 {
	    AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this) 
	        //set message, title, and icon
	        .setTitle(title) 
	        .setMessage(msg) 
	        //.setIcon(R.drawable.delete)

	        .setPositiveButton(btnYesText, new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int whichButton) { 
	                //your deleting code
	                dialog.dismiss();
	                if(flag==1){
	                	if(f_id.equals("0")){
		                	//Toast.makeText(getApplicationContext(), "swiikaar", Toast.LENGTH_LONG).show();
		                	//------code to add the farmer to the database on positive confirmation
		                	registerTheFarmer();
		                	finish();
		                	startActivity(new Intent(getApplicationContext(),Farmers.class));
	                	}else{
	                		//------code to update the farmer details to the database on positive confirmation
	                		updateTheFarmer(f_id);
	                		finish();
	                		Intent in = new Intent(getApplicationContext(),FarmerDetailsPage.class);
	        	        	Bundle b_param = new Bundle();
	        	        	b_param.putString("farmer_id", f_id);
	        	        	in.putExtras(b_param);
	        	        	startActivity(in);
	                	}
	                }else if(flag==2){
	                	if(f_id.equals("0")){
		                	//Toast.makeText(getApplicationContext(), "raadh", Toast.LENGTH_LONG).show();
		                	//-------Go back to previous page------
		                	finish();
		                	startActivity(new Intent(getApplicationContext(),Farmers.class));
	                	}else{
	                		//-------Go back to previous page------
	                		finish();
	                		Intent in = new Intent(getApplicationContext(),FarmerDetailsPage.class);
	        	        	Bundle b_param = new Bundle();
	        	        	b_param.putString("farmer_id", f_id);
	        	        	in.putExtras(b_param);
	        	        	startActivity(in);
		                	
	                	}
	                }
	            }			  

	        })



	        .setNegativeButton(btnNoText, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	if(f_id.equals("0")){
		                dialog.dismiss();
		                //Toast.makeText(getApplicationContext(), "this is negative button", Toast.LENGTH_LONG).show();
	            	}else{
	            		dialog.dismiss();
		                //Toast.makeText(getApplicationContext(), "this is negative button + edit farmer", Toast.LENGTH_LONG).show();
	            		/*finish();
                		Intent in = new Intent(getApplicationContext(),FarmerDetailsPage.class);
        	        	Bundle b_param = new Bundle();
        	        	b_param.putString("farmer_id", f_id);
        	        	in.putExtras(b_param);
        	        	startActivity(in);*/
	            	}
	            }
	        })
	        .create();
	        return myQuittingDialogBox;

	    }
	 
	 
	 private boolean validateAllFields() {
			// TODO Auto-generated method stub
		 Boolean isEmpty=false;
		 
		 String F_NAME = f_name.getText().toString();
		 String F_MOB = f_mobile.getText().toString();
		 String F_CITY = f_city.getText().toString();
		 String F_STATE = f_state.getText().toString();
		 String F_PINCODE = f_pincode.getText().toString();
		 
		 if (F_NAME.length() == 0 || F_NAME.isEmpty()) {		
			 f_name.setError(getString(R.string.mandatory_field));
				isEmpty = true;
		 }if (F_MOB.length() == 0 || F_MOB.isEmpty()) {		
			 f_mobile.setError(getString(R.string.mandatory_field));
				isEmpty = true;
		 }if (F_CITY.length() == 0 || F_CITY.isEmpty()) {		
			 f_city.setError(getString(R.string.mandatory_field));
				isEmpty = true;
		 }if (F_STATE.length() == 0 || F_STATE.isEmpty()) {		
			 f_state.setError(getString(R.string.mandatory_field));
				isEmpty = true;
		 }if (F_PINCODE.length() == 0 || F_PINCODE.isEmpty()) {		
			 f_pincode.setError(getString(R.string.mandatory_field));
				isEmpty = true;
		 }
		  
		 return isEmpty;
			
		}
	 private void registerTheFarmer() {
			// TODO Auto-generated method stub

	    	String F_NAME = f_name.getText().toString();
			 String F_MOB = f_mobile.getText().toString();
			 String F_ADDRESS = f_street.getText().toString()+":"+f_landmark.getText().toString()+":"+f_city.getText().toString()+":"+f_state.getText().toString()+":"+f_pincode.getText().toString();
			 String F_STREET = f_street.getText().toString();
			 String F_LANDMARK = f_landmark.getText().toString();
			 String F_STATE = f_state.getText().toString();
			 String F_CITY = f_city.getText().toString();
			 String F_PINCODE = f_pincode.getText().toString();
			 String F_SEED = f_seed.getText().toString();
			 String F_FRUIT = f_fruit.getText().toString();
			 String F_KERNEL = f_kernel.getText().toString();
		 
		long id = db.insertFarmer(F_NAME,F_MOB,F_SEED,F_FRUIT,F_KERNEL,F_STREET,F_LANDMARK,F_CITY,F_STATE,F_PINCODE,F_ADDRESS);
		if(id>0){
			//----------do server synching 
			Toast.makeText(getApplicationContext(), getString(R.string.farmer_added_successfully) ,Toast.LENGTH_SHORT).show();
		}else{
			//Toast.makeText(getApplicationContext(), getString(R.string.accept_confirmation_msg),Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), getString(R.string.farmer_not_added) ,Toast.LENGTH_SHORT).show();
		}
	 } 
	 
	 private void updateTheFarmer(String f_id) {
			// TODO Auto-generated method stub

	    	String F_NAME = f_name.getText().toString();
			 String F_MOB = f_mobile.getText().toString();
			 String F_ADDRESS = f_street.getText().toString()+":"+f_landmark.getText().toString()+":"+f_city.getText().toString()+":"+f_state.getText().toString()+":"+f_pincode.getText().toString();
			 String F_STREET = f_street.getText().toString();
			 String F_LANDMARK = f_landmark.getText().toString();
			 String F_STATE = f_state.getText().toString();
			 String F_CITY = f_city.getText().toString();
			 String F_PINCODE = f_pincode.getText().toString();
			 String F_SEED = f_seed.getText().toString();
			 String F_FRUIT = f_fruit.getText().toString();
			 String F_KERNEL = f_kernel.getText().toString();
		 
		long id = db.updateFarmer(F_NAME,F_MOB,F_SEED,F_FRUIT,F_KERNEL,F_STREET,F_LANDMARK,F_CITY,F_STATE,F_PINCODE,F_ADDRESS,f_id);
		if(id>0){
			//----------do server synching 
			Toast.makeText(getApplicationContext(), getString(R.string.farmer_updated_successfully) ,Toast.LENGTH_SHORT).show();
		}else{
			//Toast.makeText(getApplicationContext(), getString(R.string.accept_confirmation_msg),Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), getString(R.string.farmer_not_updated) ,Toast.LENGTH_SHORT).show();
		}
	 } 
	 
}
