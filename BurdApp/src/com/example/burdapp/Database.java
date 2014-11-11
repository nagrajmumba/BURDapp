package com.example.burdapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class Database {
	// the Activity or Application that is creating an object from this class.
	Context context;
	
	// a reference to the database used by this application/object
	private SQLiteDatabase db;
		
	public Database(Context context)
	{
		try{
		this.context = context;
 
		// create or open the database
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
		this.db = helper.getWritableDatabase();
		
		}catch(Exception e){
			e.printStackTrace();		
		}
	}
	public void closeDb(){
		this.db.close();
	}
	
	
	
	

	
	///////-------------------- Functions for mediator --------------------//////////
	//This function inserts a mediator into the table
	public void insertMediator(String m_Id,String m_Name,String m_Mobile,String m_Username, String m_Password, String m_Created_date)
	{
		
		// this is a key value pair holder used by android's SQLite functions
		
		ContentValues values = new ContentValues(8);
		values.put(applicationConstants.MEDIATOR_ID, m_Id);
		values.put(applicationConstants.MEDIATOR_NAME, m_Name);
		values.put(applicationConstants.MEDIATOR_PHONE,m_Mobile);
		values.put(applicationConstants.MEDIATOR_PLACE, "uttarakhand");
		values.put(applicationConstants.MEDIATOR_USERNAME, m_Username);
		values.put(applicationConstants.MEDIATOR_PASSWORD, m_Password);		
		values.put(applicationConstants.MEDIATOR_CREATED_DATE, m_Created_date);
		values.put(applicationConstants.MEDIATOR_SYNCSTATUS, "1");
		//values.put(TRAVEL_EXPENSE,Travel_expense);
		//values.put("serverid",serverid);
 
		// ask the database object to insert the new data 
		try{db.insert(applicationConstants.MEDIATOR_TABLE, null, values);}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
	/**
	 * Get details of Mediator from SQLite DB as Array List
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getMediator() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM "+ applicationConstants.MEDIATOR_TABLE;
	    //SQLiteDatabase database = this.db.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	    	
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	//map.put("userId", cursor.getString(0));
	        	map.put(applicationConstants.MEDIATOR_ID, cursor.getString(0));
	        	map.put(applicationConstants.MEDIATOR_NAME, cursor.getString(1));	    		
	    		map.put(applicationConstants.MEDIATOR_PHONE, cursor.getString(2));	    		
	    		map.put(applicationConstants.MEDIATOR_PLACE, cursor.getString(3));
	    		map.put(applicationConstants.MEDIATOR_USERNAME, cursor.getString(4));
	    		map.put(applicationConstants.MEDIATOR_PASSWORD, cursor.getString(5));
	    		map.put(applicationConstants.MEDIATOR_CREATED_DATE, cursor.getString(6));
	    		//map.put(M_SERVER_ID, cursor.getString(7));
	    		map.put(applicationConstants.MEDIATOR_SYNCSTATUS, cursor.getString(7));
                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	   // db.close();
	   // Log.d("TAG",wordList.get(0).);
	    	return wordList;
	    
	}
	
	///get count of mediators
	
	public int getMediatorCount() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT * FROM mediator_table";
	    //SQLiteDatabase database = this.db.getWritableDatabase();
	    Cursor cursor = null;
	    cursor = db.rawQuery(selectQuery, null);
	    if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	//map.put("userId", cursor.getString(0));
	        	map.put("mediator_id", cursor.getString(0));
	        	/*map.put(M_NAME, cursor.getString(1));	    		
	    		map.put(M_PHONE, cursor.getString(2));	    		
	    		map.put(M_PLACE, cursor.getString(3));
	    		map.put(M_USERNAME, cursor.getString(4));
	    		map.put(M_PASSWORD, cursor.getString(5));
	    		map.put(M_CREATED_DATE, cursor.getString(6));
	    		map.put(M_SYNCSTATUS, cursor.getString(7));
	    		map.put(M_SERVER_ID, cursor.getString(8));*/
                wordList.add(map);
	        } while (cursor.moveToNext());
	        return (wordList.size());
	    }else{
	    	return -1;
	    }
	   // db.close();
	    
	    
	    
	}
	
	///-----------------------Functions for farmers ------------------------------
	/* get all the farmers*/
	public ArrayList<ArrayList<Object>> getAllFarmersAsArrays()
	{
		
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
 
		Cursor cursor=null;
 
		try
		{
			
			cursor = db.query(
					applicationConstants.FARMER_TABLE,
					new String[]{applicationConstants.FARMER_NAME,applicationConstants.FARMER_SEED,
							applicationConstants.FARMER_KERNEL,applicationConstants.FARMER_FRUIT,
							applicationConstants.FARMER_CREATED_ON,applicationConstants.FARMER_ID,
							applicationConstants.FARMER_MOBILE,applicationConstants.FARMER_ADDRESS,
							applicationConstants.FARMER_PULP,applicationConstants.FARMER_OCCUPIED_KERNEL,
							applicationConstants.FARMER_OCCUPIED_SEED,applicationConstants.FARMER_OCCUPIED_FRUIT,
							applicationConstants.FARMER_OCCUPIED_PULP},
					null, null, null, null, applicationConstants.FARMER_NAME
			);
 
			System.out.println(cursor.getColumnNames());
			
 
			if (cursor != null && cursor.moveToFirst())
			{
				do
				{
					ArrayList<Object> dataList = new ArrayList<Object>();
 
					dataList.add(cursor.getString(0));
					//Log.v("name", cursor.getString(0));
					dataList.add(cursor.getString(1));
					//Log.v("seed", cursor.getString(1));
					dataList.add(cursor.getString(2));
					//Log.v("kernel", cursor.getString(2));
					dataList.add(cursor.getString(3));
					//Log.v("fruit", cursor.getString(3));
					dataList.add(cursor.getString(4));
					//Log.v("created on", cursor.getString(4));
					dataList.add(cursor.getString(5));
					//Log.v("mobile", cursor.getString(5));
					dataList.add(cursor.getString(6));
					//Log.v("address", cursor.getString(6));	
					dataList.add(cursor.getString(7));
					//Log.v("address", cursor.getString(6));
					dataList.add(cursor.getString(8));
					//Log.v("address", cursor.getString(6));
					dataList.add(cursor.getString(9));
					//Log.v("address", cursor.getString(6));
					dataList.add(cursor.getString(10));
					//Log.v("address", cursor.getString(6));
					dataList.add(cursor.getString(11));
					//Log.v("address", cursor.getString(6));
					dataList.add(cursor.getString(12));
					//Log.v("address", cursor.getString(6));
					
					
					
					
					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}else{
				return null;
			}
			
		}
		catch (SQLException e)
		{
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
 
		// return the ArrayList that holds the data collected from
		// the database.
		return dataArrays;
	}
	
	public ArrayList<ArrayList<Object>> getFarmerHistory(String f_id)
	{
		
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
 
		/*Cursor cursor;
 
		try
		{
			
			cursor = db.query(
					ORDERS_FARMERS,
					new String[]{F_ORDER, F_PAYED},
					F_NAME + "= '" + name+"'", null, null, null, null
			);
 
			
			cursor.moveToFirst();
 
			if (!cursor.isAfterLast())
			{
				do
				{
					ArrayList<Object> dataList = new ArrayList<Object>();
 
					dataList.add(cursor.getString(0));
					Log.v("order", cursor.getString(0));
					dataList.add(cursor.getString(1));
					Log.v("payment", cursor.getString(1));
					
					
					
 
					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		}
		catch (SQLException e)
		{
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
 
		// return the ArrayList that holds the data collected from
		// the database.
		*/
		return dataArrays;
	}

	
	public ArrayList<Object> getFarmerAsArray(String f_id)
	{
		
		ArrayList<Object> rowArray = new ArrayList<Object>();
		Cursor cursor;
 
		try
		{
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			cursor = db.query
			(
					applicationConstants.FARMER_TABLE,
					new String[] { applicationConstants.FARMER_NAME,applicationConstants.FARMER_SEED,
							applicationConstants.FARMER_PINCODE,applicationConstants.FARMER_CREATED_ON,
							applicationConstants.FARMER_ID,applicationConstants.FARMER_KERNEL,
							applicationConstants.FARMER_FRUIT,applicationConstants.FARMER_MOBILE,
							applicationConstants.FARMER_ADDRESS,applicationConstants.FARMER_STREET,
							applicationConstants.FARMER_LANDMARK,applicationConstants.FARMER_CITY,
							applicationConstants.FARMER_STATE,applicationConstants.FARMER_PULP,
							applicationConstants.FARMER_OCCUPIED_KERNEL,applicationConstants.FARMER_OCCUPIED_SEED,
							applicationConstants.FARMER_OCCUPIED_FRUIT,applicationConstants.FARMER_OCCUPIED_PULP},
					applicationConstants.FARMER_ID + "= '" + f_id+"'",
					null, null, null, null, null
			);
 
			// move the pointer to position zero in the cursor.
			cursor.moveToFirst();
 
			// if there is data available after the cursor's pointer, add
			// it to the ArrayList that will be returned by the method.
			if (!cursor.isAfterLast())
			{
				do
				{
					rowArray.add(cursor.getString(0));
					rowArray.add(cursor.getString(1));
					rowArray.add(cursor.getString(2));
					rowArray.add(cursor.getString(3));
					rowArray.add(cursor.getString(4));
					rowArray.add(cursor.getString(5));
					rowArray.add(cursor.getString(6));
					rowArray.add(cursor.getString(7));
					rowArray.add(cursor.getString(8));
					rowArray.add(cursor.getString(9));
					rowArray.add(cursor.getString(10));
					rowArray.add(cursor.getString(11));
					rowArray.add(cursor.getString(12));
					rowArray.add(cursor.getString(13));
					rowArray.add(cursor.getString(12));
					rowArray.add(cursor.getString(14));
					rowArray.add(cursor.getString(15));
					rowArray.add(cursor.getString(16));
					
				}
				while (cursor.moveToNext());
			}
 
			// let java know that you are through with the cursor.
			cursor.close();
		}
		catch (SQLException e) 
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
 
		// return the ArrayList containing the given row from the database.
		System.out.println(rowArray);
		return rowArray;
	}
	
	
	public long insertFarmer(String name,String mobile, String seed,String fruit,String kernel,String pulp,String street, String landmark, String city, String state,String pincode,String address)
	{							
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues(12);
	//	values.put(FARMER_ID, farmer_id);
		values.put(applicationConstants.FARMER_NAME, name);
		values.put(applicationConstants.FARMER_MOBILE,mobile);
//		values.put(DELIVERY_DATE,"");
		values.put(applicationConstants.FARMER_SEED,seed);
		values.put(applicationConstants.FARMER_FRUIT,fruit);
		values.put(applicationConstants.FARMER_KERNEL,kernel);
		values.put(applicationConstants.FARMER_PULP,pulp);
		
		values.put(applicationConstants.FARMER_STREET,street);
		values.put(applicationConstants.FARMER_LANDMARK,landmark);
		values.put(applicationConstants.FARMER_CITY,city);
		values.put(applicationConstants.FARMER_STATE,state);
		values.put(applicationConstants.FARMER_PINCODE,pincode);
		values.put(applicationConstants.FARMER_ADDRESS,address);
		values.put(applicationConstants.FARMER_SYNCHED,"0");
		
		long rowId=0;
 
		// ask the database object to insert the new data 
		try{
			rowId = db.insert(applicationConstants.FARMER_TABLE, null, values);
			
		}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return rowId;
	}
	
	public long updateFarmer(String name,String mobile, String seed,String fruit,String kernel,String pulp,String street, String landmark, String city, String state,String pincode,String address,String f_id)
	{							
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues(12);
	//	values.put(FARMER_ID, farmer_id);
		values.put(applicationConstants.FARMER_NAME, name);
		values.put(applicationConstants.FARMER_MOBILE,mobile);
//		values.put(DELIVERY_DATE,"");
		values.put(applicationConstants.FARMER_SEED,seed);
		values.put(applicationConstants.FARMER_FRUIT,fruit);
		values.put(applicationConstants.FARMER_KERNEL,kernel);
		values.put(applicationConstants.FARMER_PULP,pulp);
		
		values.put(applicationConstants.FARMER_STREET,street);
		values.put(applicationConstants.FARMER_LANDMARK,landmark);
		values.put(applicationConstants.FARMER_CITY,city);
		values.put(applicationConstants.FARMER_STATE,state);
		values.put(applicationConstants.FARMER_PINCODE,pincode);
		values.put(applicationConstants.FARMER_ADDRESS,address);
		//values.put(applicationConstants.FARMER_STATUS,"no");
		values.put(applicationConstants.FARMER_SYNCHED,"0");
		
		long rowId=0;
		String whereClause = applicationConstants.FARMER_ID+"="+f_id;
		// ask the database object to insert the new data 
		try{
			rowId = db.update(applicationConstants.FARMER_TABLE,values,whereClause,null);
			
		}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return rowId;
	}
	
	public long updateFarmersQty(String farmer_id, String type, String qty)
	{							
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues(3);	
		if(type.equals("1")){
			values.put(applicationConstants.FARMER_KERNEL,qty);
		}else if(type.equals("2")){
			values.put(applicationConstants.FARMER_SEED,qty);
		}else if(type.equals("3")){
			values.put(applicationConstants.FARMER_FRUIT,qty);
		}else if(type.equals("4")){
			values.put(applicationConstants.FARMER_PULP,qty);
		}
		values.put(applicationConstants.FARMER_SYNCHED,"0");
	
		//values.put(applicationConstants.FARMER_STATUS,"no");
		
		long rowId=0;
		String whereClause = applicationConstants.FARMER_ID+"="+farmer_id;
		// ask the database object to insert the new data 
		try{
			rowId = db.update(applicationConstants.FARMER_TABLE,values,whereClause,null);
			
		}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return rowId;
	}
	
	///-----------------------Functions for order_farmers ------------------------------
		/* get all the farmers*/
		public ArrayList<ArrayList<Object>> getFarmersOfOrder(String order_id)
		{
			
			ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
	 
			Cursor cursor=null;
			String  sqlQuery= "Select ";
			sqlQuery += applicationConstants.FARMER_ID+", "+applicationConstants.FARMER_NAME+","+
					applicationConstants.FARMER_KERNEL+","+applicationConstants.FARMER_SEED+","+
					applicationConstants.FARMER_FRUIT+","+applicationConstants.FORDER_DELIVERY_DATE+","+
					applicationConstants.FORDER_QUANTITY+","+applicationConstants.FORDER_PRICE+","+
					applicationConstants.FORDER_TRAVEL_COST+","+applicationConstants.FORDER_ID+","+
					applicationConstants.FORDER_STATUS+","+applicationConstants.FORDER_RECEIVED+","+
					applicationConstants.FARMER_PULP+","+applicationConstants.FARMER_OCCUPIED_KERNEL+","+
					applicationConstants.FARMER_OCCUPIED_SEED+","+applicationConstants.FARMER_OCCUPIED_FRUIT+","+
					applicationConstants.FARMER_OCCUPIED_PULP+","+applicationConstants.FORDER_CONFIRMED;
			
			sqlQuery += " from "+ applicationConstants.FARMER_TABLE +","+ applicationConstants.FARMER_ORDER_TABLE;
			sqlQuery += " where "+ applicationConstants.FORDER_ORDER_ID+"="+order_id+
					" and "+applicationConstants.FORDER_FARMER_ID+"="+applicationConstants.FARMER_ID;
			try
			{
				
				cursor = db.rawQuery(sqlQuery, null);
	 
				System.out.println(cursor.getColumnNames());
				
	 
				if (cursor != null && cursor.moveToFirst())
				{
					do
					{
						ArrayList<Object> dataList = new ArrayList<Object>();
	 
						dataList.add(cursor.getString(0));
						//Log.v("name", cursor.getString(0));
						dataList.add(cursor.getString(1));
						//Log.v("seed", cursor.getString(1));
						dataList.add(cursor.getString(2));
						//Log.v("kernel", cursor.getString(2));
						dataList.add(cursor.getString(3));
						//Log.v("fruit", cursor.getString(3));
						dataList.add(cursor.getString(4));
						//Log.v("created on", cursor.getString(4));
						dataList.add(cursor.getString(5));
						//Log.v("mobile", cursor.getString(5));
						dataList.add(cursor.getString(6));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(7));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(8));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(9));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(10));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(11));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(12));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(13));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(14));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(15));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(16));
						//Log.v("address", cursor.getString(6));
						dataList.add(cursor.getString(17));
						//Log.v("address", cursor.getString(6));
												
						dataArrays.add(dataList);
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}else{
					return null;
				}
				
			}
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList that holds the data collected from
			// the database.
			return dataArrays;
		}
		
	
	///--------------------------functions for orders------------------------------
	
	public long updateOrderStatus(String status, String order_id)
	{							
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues(1);	
		values.put(applicationConstants.ORDER_STATUS,status);
		values.put(applicationConstants.ORDER_SYNCHED,"0");
	
		//values.put(applicationConstants.FARMER_STATUS,"no");
		
		long rowId=0;
		String whereClause = applicationConstants.ORDER_ID+"="+order_id;
		// ask the database object to insert the new data 
		try{
			rowId = db.update(applicationConstants.ORDER_TABLE,values,whereClause,null);
			
		}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return rowId;
	}
	public long updateOrderAccepted(String order_id, String mediator_id)
	{							
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues(1);	
		values.put(applicationConstants.ORDER_ACCEPTED_BY,mediator_id);
		values.put(applicationConstants.ORDER_NEW,"0");
		values.put(applicationConstants.ORDER_VIEW,"1");
		values.put(applicationConstants.ORDER_SYNCHED,"0");
		//values.put(applicationConstants.FARMER_STATUS,"no");
		
		long rowId=0;
		String whereClause = applicationConstants.ORDER_ID+"="+order_id;
		// ask the database object to insert the new data 
		try{
			rowId = db.update(applicationConstants.ORDER_TABLE,values,whereClause,null);
			
		}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return rowId;
	}
	/*public void updateOccupiedQuantity(String f_id, String type, String qty, String add_or_sub_flag){
		Cursor cursor = null, fcursor =null;
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
		cursor = db.rawQuery("SELECT "+applicationConstants.FORDER_ID+","+applicationConstants.FORDER_ORDER_ID+","+
								applicationConstants.FORDER_FARMER_ID+","+applicationConstants.FORDER_QUANTITY+","+
								applicationConstants.ORDER_TYPE+ " from " +applicationConstants.ORDER_TABLE+ " and " +applicationConstants.FARMER_ORDER_TABLE+ " where "+
								applicationConstants.ORDER_ID +" = "+applicationConstants.FORDER_ORDER_ID
								,null);		
		cursor.moveToFirst();
		if(cursor.getCount()>0){
			do{
				ArrayList<Object> dataList = new ArrayList<Object>();
				dataList.add(cursor.getString(0));
				dataList.add(cursor.getString(1));
				dataList.add(cursor.getString(2));
				dataList.add(cursor.getString(3));
				dataList.add(cursor.getString(3));
				dataArrays.add(dataList);
			}
			while(cursor.moveToNext());
		}
		
		for(int i=0; i<dataArrays.size();i++){
			
			ArrayList<Object> row = dataArrays.get(i);
			String far_ord_id = row.get(0).toString();
			String ord_id = row.get(1).toString();
			String farmer_id = row.get(2).toString();
			String ford_qty = row.get(3).toString();
			String raw_type = row.get(4).toString();
			String farmer_raw_type = null;
			if(raw_type.equals("1")){
				farmer_raw_type= applicationConstants.FARMER_OCCUPIED_KERNEL;
			}else if(raw_type.equals("2")){
				farmer_raw_type= applicationConstants.FARMER_OCCUPIED_SEED;
			}else if(raw_type.equals("3")){
				farmer_raw_type= applicationConstants.FARMER_OCCUPIED_FRUIT;
			}else if(raw_type.equals("4")){
				farmer_raw_type= applicationConstants.FARMER_OCCUPIED_PULP;
			}
			
			fcursor = db.rawQuery("SELECT "+farmer_raw_type+" from "+ applicationConstants.FARMER_TABLE+" where "+
						applicationConstants.FARMER_ID+" = '"+farmer_id+"'"
					,null);	
			fcursor.moveToFirst();
			int fqty = Integer.valueOf(fcursor.getString(0));
			
			
			
		}
		int farmerqty = Integer.valueOf(fcursor.getString(0));
		int finalQty = 0;
		if(add_or_sub_flag.equals("add")){
			finalQty = farmerqty + Integer.valueOf(qty);
		}else if(add_or_sub_flag.equals("subtract")){
			if(farmerqty!=0 && farmerqty > Integer.valueOf(qty))
				finalQty = farmerqty - Integer.valueOf(qty);				
		}
		
		ContentValues value = new ContentValues();		
		
		value.put(applicationConstants.FARMER_SYNCHED, "0");
		
		if(type.contentEquals("1"))
			value.put(applicationConstants.FARMER_OCCUPIED_KERNEL, finalQty);		
        else if(type.contentEquals("2"))
        	value.put(applicationConstants.FARMER_OCCUPIED_SEED, finalQty);
        else if(type.contentEquals("3"))
        	value.put(applicationConstants.FARMER_OCCUPIED_FRUIT, finalQty);
        else if(type.contentEquals("4"))
        	value.put(applicationConstants.FARMER_OCCUPIED_PULP, finalQty);
		
				//Cursor Fcursor = null;		
		try {
	       		String whereClause=applicationConstants.FARMER_ID+"='"+f_id+"'";
				int rows = db.update(applicationConstants.FARMER_TABLE, value, whereClause, null);
				System.out.print(rows+" this no.of rows updated on qty update");
			
			}catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
		
	}*/
	public int deleteOrder(String order_id){
		
		String whereClause = applicationConstants.ORDER_ID +"="+order_id;
		int rows = db.delete(applicationConstants.ORDER_TABLE, whereClause, null);
		return rows;
		
	}
	//get total available quantity as per the type
	public String getTotalAvailableQuantity(String type){
		Cursor cursor = null;
		/*if(type.contentEquals("1"))
			cursor=db.rawQuery("SELECT SUM(CAST("+ applicationConstants.FARMER_KERNEL+ " AS UNSIGNED))- SUM(CAST("+applicationConstants.FARMER_OCCUPIED_KERNEL+" AS UNSIGNED))  FROM "+applicationConstants.FARMER_TABLE+"  ",null);		
        else if(type.contentEquals("2"))
        	cursor=db.rawQuery("SELECT SUM(CAST("+ applicationConstants.FARMER_SEED+ " AS UNSIGNED))- SUM(CAST("+applicationConstants.FARMER_OCCUPIED_SEED+" AS UNSIGNED)) FROM "+applicationConstants.FARMER_TABLE+"  ",null);
        else if(type.contentEquals("3"))
        	cursor=db.rawQuery("SELECT SUM(CAST("+ applicationConstants.FARMER_FRUIT+ " AS UNSIGNED))- SUM(CAST("+applicationConstants.FARMER_OCCUPIED_FRUIT+" AS UNSIGNED)) FROM "+applicationConstants.FARMER_TABLE+"  ",null);
        else if(type.contentEquals("4"))
        	cursor=db.rawQuery("SELECT SUM(CAST("+ applicationConstants.FARMER_PULP+ " AS UNSIGNED))- SUM(CAST("+applicationConstants.FARMER_OCCUPIED_PULP+" AS UNSIGNED)) FROM "+applicationConstants.FARMER_TABLE+"  ",null);
		*/
		if(type.contentEquals("1"))
			cursor=db.rawQuery("SELECT SUM(CAST("+ applicationConstants.FARMER_KERNEL+ " AS UNSIGNED))  FROM "+applicationConstants.FARMER_TABLE+"  ",null);		
        else if(type.contentEquals("2"))
        	cursor=db.rawQuery("SELECT SUM(CAST("+ applicationConstants.FARMER_SEED+ " AS UNSIGNED)) FROM "+applicationConstants.FARMER_TABLE+"  ",null);
        else if(type.contentEquals("3"))
        	cursor=db.rawQuery("SELECT SUM(CAST("+ applicationConstants.FARMER_FRUIT+ " AS UNSIGNED)) FROM "+applicationConstants.FARMER_TABLE+"  ",null);
        else if(type.contentEquals("4"))
        	cursor=db.rawQuery("SELECT SUM(CAST("+ applicationConstants.FARMER_PULP+ " AS UNSIGNED)) FROM "+applicationConstants.FARMER_TABLE+"  ",null);
		
		
		cursor.moveToFirst();
		//System.out.println(cursor.getColumnName(0)+": this is sum of avail qty"+cursor.getCount()+ cursor.getDouble(0));
		
		return cursor.getString(0);
		
	}
	public void removeUnwantedOrders(ArrayList<String> id_list){
		Cursor cursor = null;
		cursor=db.rawQuery("SELECT "+applicationConstants.ORDER_ID+" FROM "+applicationConstants.ORDER_TABLE+" where "+applicationConstants.ORDER_NEW+"='1'",null);
		//ArrayList<String> del_list = new ArrayList<String>();
		if(cursor!=null && cursor.getCount()>0){
			if(cursor.moveToFirst()){
				do{
					if(id_list.contains(cursor.getString(0))){
						
					}else{
						deleteOrder(cursor.getString(0));
					}
				}while(cursor.moveToNext());
			}
		}
	}
	public void addAndUpdateOrders(JSONObject obj)
	{
		try {
			int count = checkDuplicate(obj.get("id").toString());
			
			if(count>0){
				System.out.println("don't insert"+obj.get("id"));
				
			}else{
				String order_id = obj.get("id").toString();
				String order_name = obj.get("order_name").toString();
				String price = obj.get("price").toString();
				String amount = obj.get("amount").toString();
				String delivery_date = obj.get("deadline").toString();
				String transportation_cost = obj.get("transportation_cost").toString();
				String type = obj.get("type").toString();
				String accepted_by = obj.get("accepted_by_id").toString();
				String order_status = obj.get("status").toString();
				
				ContentValues cv = new ContentValues(10);
				cv.put(applicationConstants.ORDER_ID, order_id);
				cv.put(applicationConstants.ORDER_NAME, order_name);
				cv.put(applicationConstants.ORDER_CREATED_BY, "0");
				cv.put(applicationConstants.ORDER_AMOUNT, amount);
				cv.put(applicationConstants.ORDER_PRICE, price);
				cv.put(applicationConstants.ORDER_TRANSPORTATION_COST, transportation_cost);
				cv.put(applicationConstants.ORDER_TYPE, type);
				cv.put(applicationConstants.ORDER_DELIVERY_DATE, delivery_date);
				cv.put(applicationConstants.ORDER_ACCEPTED_BY, accepted_by);
				cv.put(applicationConstants.ORDER_STATUS, order_status);
				cv.put(applicationConstants.ORDER_BALANCE_QUANTITY, amount);
				cv.put(applicationConstants.ORDER_NEW, "1");
				cv.put(applicationConstants.ORDER_VIEW, "0");
				cv.put(applicationConstants.ORDER_SYNCHED, "1");
				
				
				
				addRow(cv, applicationConstants.ORDER_TABLE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public ArrayList<Object> getOrderById(String order_id)
	{
		ArrayList<Object> rowArray = new ArrayList<Object>();
		Cursor cursor;
		System.out.print(order_id+"---in");
		try
		{
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			cursor = db.query
			(
					applicationConstants.ORDER_TABLE,
					new String[] { applicationConstants.ORDER_ID,applicationConstants.ORDER_NAME,
							applicationConstants.ORDER_CREATED_BY ,applicationConstants.ORDER_AMOUNT,
							applicationConstants.ORDER_PRICE,applicationConstants.ORDER_TRANSPORTATION_COST,
							applicationConstants.ORDER_TYPE,applicationConstants.ORDER_DELIVERY_DATE,
							applicationConstants.ORDER_ACCEPTED_BY,	applicationConstants.ORDER_STATUS,
							applicationConstants.ORDER_BALANCE_QUANTITY,applicationConstants.ORDER_NEW,
							applicationConstants.ORDER_VIEW,applicationConstants.ORDER_SYNCHED
							},
							applicationConstants.ORDER_ID + "=" + order_id,
					null, null, null, null, null
			);
 
			// move the pointer to position zero in the cursor.
			
 
			// if there is data available after the cursor's pointer, add
			// it to the ArrayList that will be returned by the method.
			if (cursor.moveToFirst())
			{
				do
				{
					rowArray.add(cursor.getString(0));
					rowArray.add(cursor.getString(1));
					rowArray.add(cursor.getString(2));
					rowArray.add(cursor.getString(3));
					rowArray.add(cursor.getString(4));
					rowArray.add(cursor.getString(5));
					rowArray.add(cursor.getString(6));
					rowArray.add(cursor.getString(7));
					rowArray.add(cursor.getString(8));
					rowArray.add(cursor.getString(9));
					rowArray.add(cursor.getString(10));
					rowArray.add(cursor.getString(11));
					rowArray.add(cursor.getString(12));
					rowArray.add(cursor.getString(13));
				}
				while (cursor.moveToNext());
			}
 
			// let java know that you are through with the cursor.
			cursor.close();
		}
		catch (SQLException e) 
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
 
		// return the ArrayList containing the given row from the database.
		return rowArray;
	}
	
	/**
	 * Get details of Mediator from SQLite DB as Array List
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getOrderIdsOfNewOrders(String type) {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  "+applicationConstants.ORDER_ID+" FROM "+ applicationConstants.ORDER_TABLE+" where "+applicationConstants.ORDER_NEW+"= '1'";
	    //SQLiteDatabase database = this.db.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	    	
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	//map.put("userId", cursor.getString(0));
	        	map.put(applicationConstants.ORDER_ID, cursor.getString(0));
	        	map.put("type",type);
                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	   // db.close();
	   // Log.d("TAG",wordList.get(0).);
	    	return wordList;
	    
	}
	public void addRow(ContentValues cv,String table_name)
	{
		// ask the database object to insert the new data 
		try{
		db.insert(table_name, null, cv);}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
	public void deleteRow(String tablename, String whereclause)
	{
		// ask the database manager to delete the row of given id
		try {db.delete(tablename, whereclause, null);}
		catch (Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
	public int checkDuplicate(String id){
		int count = 0;
		String selectQuery = "SELECT  * FROM "+applicationConstants.ORDER_TABLE+" where "+applicationConstants.ORDER_ID+" = '"+id+"'";
	    //SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    count = cursor.getCount();
	    //db.close();
		return count;
	}
	
	public ArrayList<ArrayList<Object>> getAllOrdersAsArrays()
	{
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
 
		// this is a database call that creates a "cursor" object.
		// the cursor object store the information collected from the
		// database and is used to iterate through the data.
		Cursor cursor;
 
		try
		{
			// ask the database object to create the cursor.
			cursor = db.query(
					applicationConstants.ORDER_TABLE,
					new String[]{applicationConstants.ORDER_ID,applicationConstants.ORDER_NAME,
							applicationConstants.ORDER_CREATED_BY,applicationConstants.ORDER_AMOUNT,
							applicationConstants.ORDER_PRICE,applicationConstants.ORDER_TRANSPORTATION_COST,
							applicationConstants.ORDER_TYPE,applicationConstants.ORDER_DELIVERY_DATE,
							applicationConstants.ORDER_ACCEPTED_BY,applicationConstants.ORDER_STATUS,
							applicationConstants.ORDER_BALANCE_QUANTITY,applicationConstants.ORDER_NEW,
							applicationConstants.ORDER_VIEW,applicationConstants.ORDER_SYNCHED},
					null, null, null, null, applicationConstants.ORDER_ID
			);
 
			// move the cursor's pointer to position zero.
			cursor.moveToFirst();
 
			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast())
			{
				do
				{
					ArrayList<Object> dataList = new ArrayList<Object>();
 
					dataList.add(cursor.getString(0));
					//Log.v("id", cursor.getString(0));
					dataList.add(cursor.getString(1));
					//Log.v("cost", cursor.getString(1));
					dataList.add(cursor.getString(2));
					//Log.v("AMT", cursor.getString(2));
					dataList.add(cursor.getString(3));
					//Log.v("date", cursor.getString(2));
					dataList.add(cursor.getString(4));
					//Log.v("status", cursor.getString(3));
					dataList.add(cursor.getString(5));
					dataList.add(cursor.getString(6));
					dataList.add(cursor.getString(7));
					dataList.add(cursor.getString(8));
					dataList.add(cursor.getString(9));
					dataList.add(cursor.getString(10));
					dataList.add(cursor.getString(11));
					dataList.add(cursor.getString(12));
					dataList.add(cursor.getString(13));
					
					
 
					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		}
		catch (SQLException e)
		{
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
 
		// return the ArrayList that holds the data collected from
		// the database.
		return dataArrays;
	}
	
	///--------------------------functions for farmer_orders------------------------------
	
	public void assignFarmersToOrder(String order_id, String f_id,String qty, String price, String delDate)
	{
		//ContentValues values = new ContentValues();
		
		/*Cursor cursor = null, Fcursor=null;
		try {
			cursor = db.rawQuery("SELECT "+ DATE+ " FROM "+TABLE_NAME+" WHERE "+ORDER_ID+" LIKE ? ",new String[]{ order_id});
         	cursor.moveToFirst();
			Log.v("DATE", cursor.getString(0));
            
		}
		catch (Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}*/
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss a");
		String strDate = sdf.format(c.getTime());
		
		ContentValues value = new ContentValues();
		value.put(applicationConstants.FORDER_ORDER_ID, order_id);
		value.put(applicationConstants.FORDER_FARMER_ID, f_id);
		value.put(applicationConstants.FORDER_QUANTITY, qty);
		value.put(applicationConstants.FORDER_PRICE, price);
		value.put(applicationConstants.FORDER_DELIVERY_DATE, delDate);
		value.put(applicationConstants.FORDER_SYNCHED, "0");
		value.put(applicationConstants.FORDER_ASSIGNED,strDate);
		/*value.put(F_DELIVERY_DATE, cursor.getString(0));
		value.put(F_QUANTITY,"100");
		value.put(F_RATE, "10");
		value.put(F_TOTAL,"");
		value.put(F_TRAVEL,"20");
		value.put(F_ASSIGNED,"0");
		value.put(F_PAYED,"--");
		value.put(F_RECEIVED,"0");*/
		Cursor Fcursor = null;		
		try {
		Fcursor = db.rawQuery("SELECT "+applicationConstants.FORDER_ID+" FROM "+applicationConstants.FARMER_ORDER_TABLE+" WHERE "+applicationConstants.FORDER_FARMER_ID+" = ? and "+applicationConstants.FORDER_ORDER_ID+" = ? ",new String[]{ f_id,order_id});
       	       
	       	if(Fcursor != null && Fcursor.moveToFirst()){
	       		String whereClause=applicationConstants.FORDER_ID+"='"+Fcursor.getString(0)+"'";
				int rows = db.update(applicationConstants.FARMER_ORDER_TABLE, value, whereClause, null);
				System.out.print(rows+" this no.of row updated");
				
			}else{
				
				long rowid = db.insert(applicationConstants.FARMER_ORDER_TABLE, null, value);
				System.out.print(rowid+" this is insert id");
				
			}
		}
		
		catch (Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
	public void updateFarmerOrderSynched()
	{
		ContentValues value = new ContentValues();		
		//value.put(applicationConstants.FORDER_QUANTITY, qty);
		value.put(applicationConstants.FORDER_SYNCHED, "0");
		
		
		//Cursor Fcursor = null;		
		try {
	       		//String whereClause=applicationConstants.FORDER_ID+"='"+forder_id+"'";
				int rows = db.update(applicationConstants.FARMER_ORDER_TABLE, value, null, null);
				
				Cursor cursor = db.rawQuery("select * from "+applicationConstants.FARMER_ORDER_TABLE, null);
				System.out.print(rows+" this no.of rows updated on qty update:::"+cursor.getCount());
				if(cursor.moveToFirst()){
					do{
						System.out.println("id:"+cursor.getString(0)+"--synche:"+cursor.getString(11));
					}while(cursor.moveToNext());
				}
			
			}catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	}
	
	public void updateOrderShipment(String order_id, String sent_by_mediator_date, String will_be_reacched_by_date)
	{
		ContentValues value = new ContentValues();		
		//value.put(applicationConstants.FORDER_QUANTITY, qty);
		value.put(applicationConstants.ORDER_SENT_BY_MEDIATOR, sent_by_mediator_date);
		value.put(applicationConstants.ORDER_WILL_BE_REACHED_BY, will_be_reacched_by_date);
		value.put(applicationConstants.ORDER_SYNCHED, "0");
		
		
		//Cursor Fcursor = null;		
		try {
	       		String whereClause=applicationConstants.ORDER_ID+"='"+order_id+"'";
				int rows = db.update(applicationConstants.ORDER_TABLE, value, null, null);
				
				
			
			}catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	}
	public void updateFarmerOrderQty(String forder_id, String qty)
	{
		ContentValues value = new ContentValues();		
		value.put(applicationConstants.FORDER_QUANTITY, qty);
		value.put(applicationConstants.FORDER_SYNCHED, "0");
		
		
		//Cursor Fcursor = null;		
		try {
	       		String whereClause=applicationConstants.FORDER_ID+"='"+forder_id+"'";
				int rows = db.update(applicationConstants.FARMER_ORDER_TABLE, value, whereClause, null);
				System.out.print(rows+" this no.of rows updated on qty update");
			
			}catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	}
	
	public void updateFarmerOrderPrice(String forder_id, String price)
	{
		ContentValues value = new ContentValues();		
		value.put(applicationConstants.FORDER_PRICE, price);
		value.put(applicationConstants.FORDER_SYNCHED, "0");
		
		
	//	Cursor Fcursor = null;		
		try {
			String whereClause=applicationConstants.FORDER_ID+"='"+forder_id+"'";
				int rows = db.update(applicationConstants.FARMER_ORDER_TABLE, value, whereClause, null);
				System.out.print(rows+" this no.of rows updated on price update");
			
			}catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	}
	public void updateFarmerOrderTravelCost(String forder_id, String travel_cost)
	{
		ContentValues value = new ContentValues();		
		value.put(applicationConstants.FORDER_TRAVEL_COST, travel_cost);
		value.put(applicationConstants.FORDER_SYNCHED, "0");
		
		
		//Cursor Fcursor = null;		
		try {
			String whereClause=applicationConstants.FORDER_ID+"='"+forder_id+"'";
				int rows = db.update(applicationConstants.FARMER_ORDER_TABLE, value, whereClause, null);
				System.out.print(rows+" this no.of rows updated on travel update");
			
			}catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	}
	
	public void updateFarmerOrderDeliveryDate(String forder_id, String subOrd_del_date)
	{
		ContentValues value = new ContentValues();		
		value.put(applicationConstants.FORDER_DELIVERY_DATE, subOrd_del_date);
		value.put(applicationConstants.FORDER_SYNCHED, "0");
		
		
		//Cursor Fcursor = null;		
		try {
			String whereClause=applicationConstants.FORDER_ID+"='"+forder_id+"'";
				int rows = db.update(applicationConstants.FARMER_ORDER_TABLE, value, whereClause, null);
				System.out.print(rows+" this no.of rows updated on suborder_deldate update");
			
			}catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	}
	
	public void updateFarmerOrderReceived(String forder_id, String received_date, String status)
	{
		ContentValues value = new ContentValues();	
		//if(!received_date.equals(""))
			value.put(applicationConstants.FORDER_RECEIVED, received_date);
			value.put(applicationConstants.FORDER_STATUS, status);
		//else
			//value.put(applicationConstants.FORDER_RECEIVED, "");
		
		value.put(applicationConstants.FORDER_SYNCHED, "0");
		
		
		//Cursor Fcursor = null;		
		try {
			String whereClause=applicationConstants.FORDER_ID+"='"+forder_id+"'";
				int rows = db.update(applicationConstants.FARMER_ORDER_TABLE, value, whereClause, null);
				System.out.print(rows+" this no.of rows updated on "+ received_date +" received_date update");
			
			}catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	}
	public void updateFarmerOrderConfirmed(String forder_id, String confirmed_date, String status)
	{
		ContentValues value = new ContentValues();	
		//if(!received_date.equals(""))
			value.put(applicationConstants.FORDER_CONFIRMED, confirmed_date);
			value.put(applicationConstants.FORDER_STATUS, status);
		//else
			//value.put(applicationConstants.FORDER_RECEIVED, "");
		
		value.put(applicationConstants.FORDER_SYNCHED, "0");
		
		
		//Cursor Fcursor = null;		
		try {
			String whereClause=applicationConstants.FORDER_ID+"='"+forder_id+"'";
				int rows = db.update(applicationConstants.FARMER_ORDER_TABLE, value, whereClause, null);
				//System.out.print(rows+" this no.of rows updated on "+ received_date +" received_date update");
			
			}catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	}
	///--------------------------General functions---------------------------------
	
	public int getRowsCountOfTable(String tableName){
		String countQuery = "SELECT  * FROM " + tableName;
		Cursor cursor=null;
		cursor = db.rawQuery(countQuery, null);
	    int cnt = cursor.getCount();
	    cursor.close();
	    return cnt;
	}
	///--------------------SQLITE HELPER CLASS -------------------------------------
	private class CustomSQLiteOpenHelper extends SQLiteOpenHelper
	{
		public CustomSQLiteOpenHelper(Context context)
		{
			super(context, applicationConstants.DB_NAME, null, applicationConstants.DB_VERSION);
		}
		
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// This string is used to create the ordertable. 
			String newTableQueryString = "create table " +
										applicationConstants.ORDER_TABLE +
										" (" +
										applicationConstants.ORDER_ID + " string not null," +
										applicationConstants.ORDER_NAME + " string not null," +
										applicationConstants.ORDER_CREATED_BY + " string," +
										applicationConstants.ORDER_AMOUNT + " string," +
										applicationConstants.ORDER_PRICE + " string," +
										applicationConstants.ORDER_TRANSPORTATION_COST + " string," +
										applicationConstants.ORDER_TYPE + " string," +
										applicationConstants.ORDER_DELIVERY_DATE + " string," +
										applicationConstants.ORDER_ACCEPTED_BY + " string," +
										applicationConstants.ORDER_STATUS + " string," +
										applicationConstants.ORDER_BALANCE_QUANTITY + " string, " +
										applicationConstants.ORDER_NEW + " INTEGER DEFAULT 0, " +
										applicationConstants.ORDER_VIEW + " INTEGER DEFAULT 0, " +
										applicationConstants.ORDER_SYNCHED + " INTEGER DEFAULT 0, " +
										applicationConstants.ORDER_SENT_BY_MEDIATOR + " string, " +
										applicationConstants.ORDER_WILL_BE_REACHED_BY + " string " +										
										");";
			// execute the query string to the database.
			db.execSQL(newTableQueryString);
			
			String FarmerTableQueryString = "create table " +
			applicationConstants.FARMER_TABLE +
			" (" +
			applicationConstants.FARMER_ID + "  integer primary key autoincrement not null," +
			applicationConstants.FARMER_NAME + " string," +
			applicationConstants.FARMER_MOBILE + " string," +
			applicationConstants.FARMER_SEED + " string DEFAULT '0'," +
			applicationConstants.FARMER_KERNEL + " string DEFAULT '0'," +
			applicationConstants.FARMER_FRUIT + " string DEFAULT '0'," +
			applicationConstants.FARMER_PULP + " string DEFAULT '0'," +
			applicationConstants.FARMER_STREET + " string," +
			applicationConstants.FARMER_LANDMARK + " string," +
			applicationConstants.FARMER_CITY + " string," +
			applicationConstants.FARMER_STATE + " string," +
			applicationConstants.FARMER_PINCODE + " string," +
			applicationConstants.FARMER_ADDRESS + " string," +				
			applicationConstants.FARMER_CREATED_ON + " string," +
			applicationConstants.FARMER_OCCUPIED_KERNEL + " string DEFAULT '0'," +
			applicationConstants.FARMER_OCCUPIED_SEED + " string DEFAULT '0'," +
			applicationConstants.FARMER_OCCUPIED_FRUIT + " string DEFAULT '0'," +
			applicationConstants.FARMER_OCCUPIED_PULP + " string DEFAULT '0'," +			
			applicationConstants.FARMER_SYNCHED + " INTEGER DEFAULT 0" +
			");";
			
			//Log.e("TAG",FarmerTableQueryString);


			db.execSQL(FarmerTableQueryString);
			
			String OrderFarmerQueryString = "create table " +
			applicationConstants.FARMER_ORDER_TABLE +
			" (" +
			applicationConstants.FORDER_ID + " integer primary key autoincrement not null," +
			applicationConstants.FORDER_ORDER_ID + " string," +
			applicationConstants.FORDER_FARMER_ID + " string," +
			applicationConstants.FORDER_DELIVERY_DATE + " string," +
			applicationConstants.FORDER_PRICE + " string," +
			applicationConstants.FORDER_QUANTITY + " string," +
			applicationConstants.FORDER_TRAVEL_COST + " string," +			
			applicationConstants.FORDER_ASSIGNED + " string," +
			applicationConstants.FORDER_RECEIVED + " string," +
			applicationConstants.FORDER_PAYMENT + " string," +
			applicationConstants.FORDER_STATUS + " string," +	
			applicationConstants.FORDER_SYNCHED + " INTEGER DEFAULT 0, " +
			applicationConstants.FORDER_CONFIRMED + " string " +
			");";

			
			db.execSQL(OrderFarmerQueryString);			
			
			
			
			String mediatorTableQueryString = "create table " +
					applicationConstants.MEDIATOR_TABLE +
					" (" +
					applicationConstants.MEDIATOR_ID + " string not null," +
					applicationConstants.MEDIATOR_NAME + " string," +
					applicationConstants.MEDIATOR_PHONE + " string," +
					applicationConstants.MEDIATOR_PLACE + " string," +
					applicationConstants.MEDIATOR_USERNAME + " string," +
					applicationConstants.MEDIATOR_PASSWORD + " string," +
					applicationConstants.MEDIATOR_CREATED_DATE + " string," +					
					applicationConstants.MEDIATOR_SYNCSTATUS + " INTEGER DEFAULT 0" +					
					");";


					db.execSQL(mediatorTableQueryString);
			
	
		}
 
 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			}
		
		
		

	}
	
	
}
