package com.example.one_click;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Showuser extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView list = getListView();
		ArrayList<String> users = MainActivity.selectedContact;
		if (users != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.activity_list_item, android.R.id.text1,
					users);
			list.setAdapter(adapter);
		}
	}

}
