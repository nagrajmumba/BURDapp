package com.example.burdapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewOrderDetails extends Activity implements OnClickListener{
	String order_id;
	Database db;
	ProgressDialog prgDialog;
	TextView orderName, orderType, orderQuantity, orderPrice, orderDelivery;
	Button btnAccept, btnReject;
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_decide_order);
	    db = new Database(this);
	    Bundle b = getIntent().getExtras();
		order_id = b.getString(applicationConstants.ORDER_ID);
		//Toast.makeText(this, order_id+" yes yes yes", Toast.LENGTH_SHORT).show();
		ArrayList<Object> o_Details = db.getOrderById(order_id);
		Toast.makeText(this, (CharSequence) o_Details.get(1), Toast.LENGTH_SHORT).show();
		orderName = (TextView) findViewById(R.id.orderName);
		orderType = (TextView) findViewById(R.id.orderType);
		orderQuantity = (TextView) findViewById(R.id.orderQuantity);
		orderPrice = (TextView) findViewById(R.id.orderPrice);
		orderDelivery = (TextView) findViewById(R.id.orderDelivery);
		
		btnAccept = (Button) findViewById(R.id.btnConfirm);
		btnReject = (Button) findViewById(R.id.btnReject);
		
		btnAccept.setOnClickListener(this);
		btnReject.setOnClickListener(this);
		
		orderName.setText((CharSequence) o_Details.get(1));
		orderType.setText((CharSequence) o_Details.get(6));
		orderQuantity.setText((CharSequence) o_Details.get(3));
		orderPrice.setText((CharSequence) o_Details.get(4));
		orderDelivery.setText((CharSequence) o_Details.get(7));
		
		prgDialog = new ProgressDialog(this);
 	    prgDialog.setMessage("Please wait...");
		prgDialog.setCancelable(false);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.btnConfirm:	
					
				AlertDialog dBox = AskOption(getString(R.string.accept_confirmation_for_order_msg),1,getString(R.string.confirm_it), getString(R.string.yes), getString(R.string.no));
				dBox.show();				
					
			break;
		case R.id.btnReject:
			
				AlertDialog dBox1 = AskOption(getString(R.string.reject_confirmation_for_order_msg),0,getString(R.string.confirm_it), getString(R.string.yes), getString(R.string.no));
				dBox1.show();			
		
			break;
		default:
			break;
		}
	}
	
	private AlertDialog AskOption(String msg,final int flag ,String title, String btnYesText, String btnNoText)
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
	                if(flag == 1){
	                	Toast.makeText(getApplicationContext(), "swiikar- yes the order", Toast.LENGTH_SHORT).show();
	                	confirmOrderOnServer();
	                	//accept order and upsync
	                }else if(flag==0){
	                	Toast.makeText(getApplicationContext(), "radh -  yes the order", Toast.LENGTH_SHORT).show();
	                	//reject order and upsync
	                }
	            }			  

	        })

	        .setNegativeButton(btnNoText, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	dialog.dismiss();
	            	/*if(flag == 1){
	                	Toast.makeText(getApplicationContext(), "swiikar- no the order", Toast.LENGTH_SHORT).show();
	                }else if(flag==0){
	                	Toast.makeText(getApplicationContext(), "radh -  no the order", Toast.LENGTH_SHORT).show();
	                }*/
	            }
	        })
	        .create();
	        return myQuittingDialogBox;

	    }
	
	
	public void confirmOrderOnServer(){
		
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
						
				prgDialog.show();
				params.put("mediatorJSON", composeJSONforConfirmOrder("confirmOrder"));
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
									Toast.makeText(getApplicationContext(), "yes confirmed", Toast.LENGTH_LONG).show();
								}else if(obj.get("current_status").equals("no")){
									//remove order from the table and go back to orders page.
									Toast.makeText(getApplicationContext(), "No !! sorry you missed the order", Toast.LENGTH_LONG).show();
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
	public String composeJSONforConfirmOrder(String type){
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		//ArrayList<HashMap<String, String>> mList = db.getMediator();
		//String mediatorId = mList.get(0).get(applicationConstants.MEDIATOR_ID);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String m_id = prefs.getString(applicationConstants.MEDIATOR_ID, null);
		
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	
	    		map.put(applicationConstants.ORDER_ID, order_id);
	    		if(!m_id.equals(null)){
	    			map.put(applicationConstants.MEDIATOR_ID, order_id);
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
}