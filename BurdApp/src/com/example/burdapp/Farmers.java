package com.example.burdapp;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Farmers extends Activity implements OnClickListener {
	
	TableLayout farmerTable;
	Button newFarmer;
	Database db;
	 @Override
	    public void onCreate(Bundle savedInstanceState)
	    {	
	    	
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.display_farmers);
	    	db = new Database(getApplicationContext());
	    	newFarmer = (Button)findViewById(R.id.register_button);
	    	newFarmer.setOnClickListener(this);
	    	
	    	ActionBar actionBar = getActionBar();
	    	actionBar.setTitle(R.string.list_of_farmers);
	    	farmerTable=(TableLayout)findViewById(R.id.display_farmer_list);
	    	int rowCount = db.getRowsCountOfTable(applicationConstants.FARMER_TABLE);
	    	if(rowCount>0){
	    		ArrayList<ArrayList<Object>> data = db.getAllFarmersAsArrays();	    	
	    		disp_farmer_list(data);
	    	}else{
	    		Toast.makeText(getApplicationContext(), R.string.add_new_farmers, Toast.LENGTH_LONG).show();
	    	}
	    }
	 
	 @Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.register_button:
				startActivity(new Intent(getApplicationContext(),FarmerRegistration.class));
				break;
			default:
				break;
			}
		}
	 
	 public void disp_farmer_list(ArrayList<ArrayList<Object>> data){
		 System.out.println(data);
	    	final Button [] ch;
	    	ch = new Button[100];
	    	
	    	int positionr=1;
	    	try{
	    	
	    	final ArrayList<Object> row1 = data.get(0);
	 	   String heading=row1.get(0).toString().substring(0, 1);
	 	     TableRow tr3= new TableRow(this);
	      	 TableRow.LayoutParams fletterParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,54, 1f);
		             TextView tw= new TextView(this);
		             tw.setLayoutParams(fletterParams);
		             tw.setText(heading);
		             tw.setTextSize(21);
		             tw.setTextColor(Color.parseColor("#33b5e5"));
		             tw.setGravity(Gravity.CENTER_VERTICAL);
		             tr3.addView(tw);
		             
		             TableRow.LayoutParams blineParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,4, 1f);
		             TableRow tr4= new TableRow(this);
		             
		             TextView ftl= new TextView(this);
		             ftl.setLayoutParams(blineParams);
		             ftl.setBackgroundResource(R.drawable.blue_line);
		             tr4.addView(ftl);
		             farmerTable.addView(tr3);
		             farmerTable.addView(tr4);	
	    	
	    	
	    	
	    	for (int position=0; position < data.size(); position++,positionr++)
	    	{
	    		 final ArrayList<Object> row = data.get(position);
	 	    	
		            
		           
		            
		            TableRow.LayoutParams boxParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,100, 1f);
		    		 
		            String s=row.get(0).toString().substring(0, 1);
		             if(heading.contentEquals(s)){
		            	 TableRow tr1= new TableRow(this);
		             
		    		 ch[positionr] = new Button(this);
		    		 ch[positionr].setLayoutParams(boxParams);
		             ch[positionr].setId(positionr+1);
		             
		            
		             if(row.get(3).toString().contentEquals("कोई भुगतान नही")||row.get(3).toString().contentEquals(""))
		            	 ch[positionr].setText(row.get(0).toString()) ;//farmer name
		             else
		            	 ch[positionr].setText(row.get(0).toString()+"\n"+row.get(2).toString()+" | "+row.get(3).toString()) ;//farmer name
			            
		             
		            ch[positionr].setGravity(Gravity.CENTER_VERTICAL);
		            ch[positionr].setTextSize(21);
		            tr1.addView(ch[positionr]);
	               ch[positionr].setBackgroundColor(Color.WHITE);
	               ch[positionr].setTextColor(Color.parseColor("#646464"));  
	               
		             tr1.setLayoutParams(boxParams);
		             
		             TableRow.LayoutParams nParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT, 0.01f);
		    		 
		             TextView next= new TextView(this);
		             next.setLayoutParams(nParams);
		             next.setBackgroundResource(R.drawable.next);
		             next.setGravity(Gravity.CENTER_VERTICAL);
		             tr1.addView(next);
		             
		             
		             TableRow.LayoutParams lineParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,2, 1f);
		             TableRow tr2= new TableRow(this);
		             
		             TextView t= new TextView(this);
		             t.setLayoutParams(lineParams);
		             t.setBackgroundResource(R.drawable.grey_line);
		             tr2.addView(t);
		             
		             
		           
		    		
		    		
		    		final int j=positionr;
		    		  
		    		  ch[positionr].setOnClickListener(new OnClickListener() {
			     	       	 
			     	   	     @Override
			     	   	     public void onClick(View v) {
			     	       	 
		              
			     	   	    	String[] name;
			                   	
			                   	String series= ch[j].getText().toString();
			                   	name= series.split("\n");
			                   	/*Log.v("NAME", name[0]);
			                    farmer_Name=name[0];
			                   	Log.v("push", "disp_farmer_list()");
			                   	backStack.push("disp_farmer_list()");*/
			     	   	    	//farmerDetailsPage(name[0]);
			     	   	    	Bundle b = new Bundle();
			     	   	    	b.putString("farmer_id", row.get(5).toString());
			     	   	    	Intent in = new Intent(getApplicationContext(), FarmerDetailsPage.class);			     	   	    
			     	   	    	in.putExtras(b);
			     	   	    	startActivity(in);
			     	   	    	
			     	   	    	 Toast.makeText(getApplicationContext(), name[0], Toast.LENGTH_LONG).show();
			     	   	 }
		        	   });
		    		  
			    		  	 farmerTable.addView(tr1);
				             farmerTable.addView(tr2);
		    	    	}
		             
		             else{
		            	 TableRow tr1= new TableRow(this);
		            	 TableRow.LayoutParams letterParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,54, 1f);
			             TextView t= new TextView(this);
			             t.setLayoutParams(letterParams);
			             t.setTextSize(21);
			             t.setTextColor(Color.parseColor("#33b5e5"));
			             t.setGravity(Gravity.CENTER_VERTICAL);
			             heading=row.get(0).toString().substring(0, 1);
			             t.setText(heading);
			             tr1.addView(t);
			             
			             TableRow.LayoutParams lineParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,4, 1f);
			             TableRow tr2= new TableRow(this);
			             
			             TextView tl= new TextView(this);
			             tl.setLayoutParams(lineParams);
			             tl.setBackgroundResource(R.drawable.blue_line);
			             tr2.addView(tl);
			             position--;
			             positionr--;
			             farmerTable.addView(tr1);
			             farmerTable.addView(tr2);
		             }

	    	}
	    	}catch(Exception e)
	    	{
	    		Log.e("Retrieve Error", e.toString());
	    		e.printStackTrace();
	    	}

		}
	
	
}
