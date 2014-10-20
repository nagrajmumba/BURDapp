package com.example.burdapp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentOrderTab extends Fragment implements OnItemClickListener{
	private String listview_array[] = { "ONE", "TWO", "THREE", "FOUR", "FIVE",
            "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "ONE", "TWO", "THREE", "FOUR", "FIVE",
            "SIX", "SEVEN", "EIGHT", "NINE", "TEN" };
	View myView;


	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.order_tab_fragment, container, false);
		//TextView textview = (TextView) view.findViewById(R.id.tabtextview);
		//textview.setText("Orders are rising :P :P :P");
		
		//myView = getView();
		ListView l_view = (ListView) view.findViewById(R.id.list1);
		l_view.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_text_view, listview_array));
		l_view.setEnabled(true);
		l_view.setScrollbarFadingEnabled(false);
		l_view.bringToFront();
		
		l_view.setOnItemClickListener(new OnItemClickListener() {    
		    @Override
		    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		        // your code is here on item click
		    	Toast.makeText(getActivity().getApplicationContext(),"hiiiiiiiiiiiii", Toast.LENGTH_LONG).show();
		    }
		});
		return view;
	 }
	 @Override
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//setContentView(R.layout.activity_firstscreen);
			
		}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	 
	 
}
