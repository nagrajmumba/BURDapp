package com.example.burdapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Mediator extends Activity{
	EditText m_Name, m_Mobile, m_Place, m_Username,m_Password,m_Confirmpass;
	Button submitButton;
	Boolean isEmpty=false;
	Database db ;
	HomeActivity home;
	ProgressDialog prgDialog;
	Bundle bundle= new Bundle();
	public static ArrayList<HashMap<String, String>> mediatorList;
	
	
	  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {	
    	
    	super.onCreate(savedInstanceState);
    	
    	
    	db = new Database(getApplicationContext());
    	//mediatorList = db.getMediator();
    	
    	
    	prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Synching SQLite Data with Remote MySQL DB. Please wait...");
		prgDialog.setCancelable(false);
		try{
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		int mediatorList=db.getMediatorCount();
		//int mediatorList=0;
		
		//System.out.println(mediatorList+":::::::::::::::::::::::::");
		if(mediatorList > 0){
    		System.out.println(mediatorList);
    		startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    	}
    	else
    	{
    		user_registration_form();
    	
    	}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
		}
		
    	
    	
    }
    public void user_registration_form(){

    	
    	// this try catch block returns better error reporting to the log
    	try
    	{
	           		
        	//	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    		
    		
        		setContentView(R.layout.register_user);
        		m_Name = (EditText)findViewById(R.id.fname);
        		m_Mobile = (EditText)findViewById(R.id.mobile);
        		m_Username = (EditText)findViewById(R.id.username);
        		m_Password = (EditText)findViewById(R.id.password);
        		m_Confirmpass = (EditText)findViewById(R.id.confirm_password);
        		
        		//-------------validations for individual fields---------------//
        		
        	    db = new Database(this);
        	    //backStack= new Stack<String>();
        	    
        	    submitButton=(Button)findViewById(R.id.button2);
        	     
        	    submitButton.setOnClickListener(new OnClickListener() {
	     	       	 
	     	   	     @Override
	     	   	     public void onClick(View v) {
	     	   	     switch (v.getId()){
			     	   	 case R.id.button2:
		
			     			String M_NAME = m_Name.getText().toString();
			     			String M_MOBILE = m_Mobile.getText().toString();
			     			String M_USERNAME = m_Username.getText().toString();			     			
			     			String M_PASSWORD = m_Password.getText().toString();			     			
			     			String M_CONFIRMPASS = m_Confirmpass.getText().toString();
			     						     			
			     			if (M_NAME.length() == 0 || M_NAME.isEmpty()) {		
			     				m_Name.setError("Mandatory field");
			     				isEmpty = true;
			     			}if (!M_NAME.matches("[a-zA-Z ]+")) {
			     				m_Name.setError("Not a valid name");
			     				isEmpty = true;			     				
			     			}if (M_MOBILE.length() == 0 || M_MOBILE.isEmpty()) {
			     				m_Mobile.setError("Mandatory field");
			     				isEmpty = true;			     				
			     			}if (!M_MOBILE.matches("[0-9]+")) {
			     				m_Mobile.setError("Numbers Only");
			     				isEmpty = true;			     				
			     			}if (M_MOBILE.length()!=10) {
			     				m_Mobile.setError("invalid mobile number");
			     				isEmpty = true;			     				
			     			}if (M_USERNAME.length() == 0 || M_USERNAME.isEmpty()) {
			     				m_Username.setError("Mandatory field");
			     				isEmpty = true;			     				
			     			}if (!M_USERNAME.matches("[a-zA-Z0-9]+")) {
			     				m_Username.setError("Invalid Username");
			     				isEmpty = true;			     				
			     			}if (M_PASSWORD.length() == 0 || M_PASSWORD.isEmpty()) {
			     				m_Password.setError("Mandatory field");
			     				isEmpty = true;
			     			}if (M_CONFIRMPASS.length() == 0 || M_CONFIRMPASS.isEmpty()) {
			     				m_Confirmpass.setError("Mandatory field");
			     				isEmpty = true;
			     			}if (!M_CONFIRMPASS.equals(M_PASSWORD)) {
			     				m_Confirmpass.setError("Password Mismatch");
			     				isEmpty = true;
			     			}			     			
			     			if(isEmpty ==true){
			     				isEmpty=false;
			     				return;
			     			}
			     			
			     	   	     String testDate = new String();
			     	   	     SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss a");
					     	   	 try {
					                 testDate = formatter.format(new Date());
					             }catch(Exception ex){
					                 ex.printStackTrace();
					             }
			     	   	     	if(m_Name.getError()==null && m_Mobile.getError()==null && m_Username.getError()==null && m_Password.getError()==null && m_Confirmpass.getError()==null){
			     	   	     		
			     	   	     		sendMediatorToServer(m_Name.getText().toString(),m_Mobile.getText().toString(),m_Username.getText().toString(),m_Password.getText().toString(),testDate);
			     	   	     		
			     	   	     		
			     	   	     	}
			     			
			     		break;
	     	   	     }
	     	   	     
	     	   	     	//Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
	     	   	     }
       	   });	   
        	    
        	    
        	    
    	
    	}
    	catch (Exception e)
    	{
    		Log.e("ERROR", e.toString());
    		e.printStackTrace();
    	}
    	
    	//getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);    
    	
    }
    public Void Is_Valid_Person_Name(EditText edt) throws NumberFormatException {
		String valid_name;
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Name cannot be empty.");
			valid_name = null;
		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Accept Alphabets Only.");
			valid_name = null;
		} else {
			edt.setError(null);
			valid_name = edt.getText().toString();
		}
		//return valid_name;
		return null;
	}
    public Void Is_Valid_Username(EditText edt) throws NumberFormatException {
		String valid_name;
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Username cannot be empty");
			valid_name = null;
		} else if (!edt.getText().toString().matches("[a-zA-Z0-9]+")) {
			edt.setError("Accept Alphabets Only.");
			valid_name = null;
		} else if (edt.getText().toString().length() > 8){
			edt.setError("Username cannont be greater than 8 characters");
			valid_name = edt.getText().toString();
		} else {
			edt.setError(null);
			valid_name = edt.getText().toString();
		}
		//return valid_name;
		return null;
	}
    public Void Is_Valid_Mobile(EditText edt) throws NumberFormatException {
		String valid_name;
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Mobile number cannot be empty.");
			valid_name = null;
		} else if (edt.getText().toString().length() > 10) {
			edt.setError("Invalid Mobile Number");
			valid_name = null;
		} else if (!edt.getText().toString().matches("[0-9]+")) {
			edt.setError("Accepts number only.");
			valid_name = null;
		}else {
			edt.setError(null);
			valid_name = edt.getText().toString();
		}
		
		return null;
	}
    
    
    public void sendMediatorToServer(final String m_Name,final String m_Mobile,final String m_Username,final String m_Password,final String m_Created_date){
		//Create AsycHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
				
				prgDialog.show();
				params.put("mediatorJSON", composeJSONforMediator(m_Name,m_Mobile,m_Username,m_Password,m_Created_date));
				client.post(applicationConstants.SERVER_URL+"mediator.php",params ,new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						System.out.println(response);
						prgDialog.hide();
						try {
							String m_id = new String();
							JSONArray arr = new JSONArray(response);
							System.out.println(arr.length());
							//for(int i=0; i<arr.length();i++){
								JSONObject obj = (JSONObject)arr.get(0);
								System.out.println(obj.get("id"));
								System.out.println(obj.get("status"));
								m_id = obj.get("id").toString();
								System.out.println(m_id+m_Name+m_Mobile+m_Username+m_Password+m_Created_date);
								if(!m_id.equals("null")){
									db.insertMediator(m_id,m_Name,m_Mobile,m_Username,m_Password,m_Created_date);
									System.out.println(m_id);	

									SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
									Editor editor = pre.edit();									
									editor.putString(applicationConstants.MEDIATOR_ID, m_id);									
									editor.commit();	
									
									startActivity(new Intent(getApplicationContext(),HomeActivity.class));														
									Toast.makeText(getApplicationContext(), "User Login Created", Toast.LENGTH_LONG).show();
								}else{
									Intent intent = getIntent();
								    finish();								   
								    startActivity(intent);
								    Toast.makeText(getApplicationContext(), "Username already in use!", Toast.LENGTH_LONG).show();
								}
							//}
							
							//Order_list();
							
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
					}
				});
					
	}
    
    /**
	 * Compose JSON out of SQLite records
	 * @return
	 */
    public String composeJSONforMediator(String m_Name,String m_Mobile,String m_Username, String m_Password, String m_Created_date){
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
	
	   
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	
	    		map.put("name", m_Name);
	    		map.put("username", m_Username);
	    		map.put("password", m_Password);
	    		map.put("phone", m_Mobile);
	    		map.put("type", "3");	    		
	        	wordList.add(map);
	       
	    //db.close();
		Gson gson = new GsonBuilder().create();
		//Use GSON to serialize Array List to JSON
		return gson.toJson(wordList);
	}
	
	/**
	 * Get SQLite records that are yet to be Synced
	 * @return
	 */
	/*public int dbSyncCount(){
		int count = 0;
		String selectQuery = "SELECT  * FROM "+FARMER_TABLE+" where "+UPDATESTATUS+" = '"+"no"+"'";
	    //SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    count = cursor.getCount();
	    //db.close();
		return count;
	}*/
	
	/**
	 * Update Sync status against each User ID
	 * @param id
	 * @param status
	 */
	/*public void updateSyncStatus(String id, String status){
		//SQLiteDatabase database = this.getWritableDatabase();	 
		String updateQuery = "Update "+FARMER_TABLE+" set "+UPDATESTATUS+" = '"+ status +"' where "+FARMER_ID+"="+"'"+ id +"'";
		Log.d("query",updateQuery);		
		db.execSQL(updateQuery);
		
	}*/
 
	
} 