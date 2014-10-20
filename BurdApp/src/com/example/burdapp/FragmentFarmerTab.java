package com.example.burdapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentFarmerTab extends Fragment {
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.farmer_tab_fragment, container, false);
		TextView textview = (TextView) view.findViewById(R.id.tabtextview);
		textview.setText("Farmers are back :P");
		return view;
}
}
