package com.example.burdapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;



public class applicationConstants {
	//------------------DATABASE CONSTANTS-----------
	public static final String DB_NAME = "burd_db";
	public static final int DB_VERSION = 1;
	
	//public static public static final String DATABASE_NAME = "burd_db";
	//public static public static final String FARMER_TABLE = "user_farmer";
	//public static public static final String MEDIATOR_TABLE = "user_mediator";
	public static final String SATELLITE_TABLE = "user_satelite_unit";
	
	//------------ORDER TABLE -------------------------
	  public static final String ORDER_TABLE = "order_table";
	  public static final String ORDER_ID = "order_id";
	  public static final String ORDER_NAME = "order_name";
	  public static final String ORDER_CREATED_BY = "order_created_by_id";
	  public static final String ORDER_AMOUNT="order_amount";
	  public static final String ORDER_PRICE= "order_price";
	  public static final String ORDER_TRANSPORTATION_COST= "order_transportation_cost";
	  public static final String ORDER_TYPE= "order_type";
	  public static final String ORDER_DELIVERY_DATE ="order_delivery_date"  ;
	  public static final String ORDER_ACCEPTED_BY ="order_accepted_by"  ;
	  public static final String ORDER_STATUS = "order_status";
	  public static final String ORDER_BALANCE_QUANTITY = "order_balance_quantity";
	  public static final String ORDER_NEW = "order_new";
	  public static final String ORDER_VIEW = "order_view";
	  public static final String ORDER_SYNCHED = "order_synched";
	  public static final String ORDER_SENT_BY_MEDIATOR = "order_sent_by_mediator";
	  public static final String ORDER_WILL_BE_REACHED_BY = "order_will_be_reached_by";
	//------------ORDER ACCEPTED TABLE -------------------------
		/* public static final String ORDER_ACCEPTED_TABLE = "order_accepted_table";
		  public static final String ORDER_ACCEPTED_ID = "accepted_id";
		  public static final String ORDER_NAME = "order_name";
		  public static final String ORDER_CREATED_BY = "order_created_by_id";
		  public static final String ORDER_AMOUNT="order_amount";
		  public static final String ORDER_PRICE= "order_price";
		  public static final String ORDER_TRANSPORTATION_COST= "order_transportation_cost";
		  public static final String ORDER_TYPE= "order_type";
		  public static final String ORDER_DELIVERY_DATE ="order_delivery_date"  ;
		  public static final String ORDER_STATUS = "order_status";*/
	 
	 //--------------------FARMERS TABLE----------------------------
	  public static final String FARMER_TABLE = "farmer_table";
	  public static final String FARMER_ID = "farmer_id";
	  public static final String FARMER_NAME= "farmer_name";
	  public static final String FARMER_SEED="farmer_seed";
	  public static final String FARMER_KERNEL="farmer_kernel";
	  public static final String FARMER_FRUIT="farmer_fruit";
	  public static final String FARMER_PULP="farmer_pulp";
	  public static final String FARMER_OCCUPIED_KERNEL = "farmer_occupied_kernel";
	  public static final String FARMER_OCCUPIED_SEED = "farmer_occupied_seed";
	  public static final String FARMER_OCCUPIED_FRUIT = "farmer_occupied_fruit";
	  public static final String FARMER_OCCUPIED_PULP = "farmer_occupied_pulp";
	 // public static final String FARMER_TRAVEL_COST= "farmer_travel_cost";
	  public static final String FARMER_CREATED_ON ="farmer_created_on"  ;
	  public static final String FARMER_STREET= "farmer_street";
	  public static final String FARMER_LANDMARK= "farmer_landmark";
	  public static final String FARMER_CITY= "farmer_city";
	  public static final String FARMER_STATE= "farmer_state";
	  public static final String FARMER_PINCODE= "farmer_pincode";
	  public static final String FARMER_ADDRESS= "farmer_address";
	  public static final String FARMER_MOBILE= "farmer_mobile";
	  public static final String FARMER_SYNCHED= "farmer_synched";
	 
	 
	//--------------------FARMERS-ORDERS TABLE-----------------------------------
	  public static final String FARMER_ORDER_TABLE = "farmer_order_table";
	  public static final String FORDER_ID = "forder_id";
	  public static final String FORDER_ORDER_ID = "forder_order_id";
	  public static final String FORDER_FARMER_ID= "forder_farmer_id";
	  public static final String FORDER_DELIVERY_DATE = "forder_delivery_date";
	  public static final String FORDER_PRICE = "forder_price";
	  public static final String FORDER_QUANTITY = "forder_quantity";
	  public static final String FORDER_TRAVEL_COST = "forder_travel_cost";
	  public static final String FORDER_PAYMENT = "forder_payment";
	  public static final String FORDER_ASSIGNED = "forder_assigned";
	  public static final String FORDER_RECEIVED = "forder_received";
	  public static final String FORDER_STATUS="forder_status";
	  public static final String FORDER_SYNCHED="forder_synched";
	  public static final String FORDER_CONFIRMED="forder_confirmed";
	 
	 
	 //------------------------MEDIATOR_TABLE------------------------------------
	 	 public static final String MEDIATOR_TABLE = "mediator_table";		
		 public static final String MEDIATOR_ID  = "mediator_id";
		 public static final String MEDIATOR_NAME = "mediator_name";
		 public static final String MEDIATOR_PHONE = "mediator_phone";
		 public static final String MEDIATOR_PLACE  = "mediator_place";
		 public static final String MEDIATOR_USERNAME  = "mediator_username";
		 public static final String MEDIATOR_PASSWORD  = "mediator_password";
		 public static final String MEDIATOR_CREATED_DATE  = "mediator_created_date";
		 public static final String MEDIATOR_SYNCSTATUS  = "mediator_syncstatus";
		// public static final String MEDIATOR_SERVER_ID  = "mediator_sserver_id";
	 
		 
		//--------------general functions------------------------
		 public static final String SERVER_URL  = "http://ec2-54-254-179-10.ap-southeast-1.compute.amazonaws.com/burd/phpservice/new/";
}
