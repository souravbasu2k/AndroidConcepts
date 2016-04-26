package com.example.androidconcepts;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Activity6 extends ListActivity {
	
	List<String> contacts = new ArrayList<String>();
	ListView listView;
	SharedPreferences mPrefs;
	Editor editor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity6);
		
		///-- how to use sharedpreference		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String author = mPrefs.getString("author", "");
		Toast.makeText(this, author, Toast.LENGTH_LONG).show();
		
		editor = mPrefs.edit();
		editor.remove("author");
		editor.putString("author", "Already visited once");
		editor.commit();
		
		//--how to use content provider		
		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		if(cursor.getCount()>0) {
			while(cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				
				contacts.add(name);						
			}
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, contacts);
		listView = getListView();
		listView.setAdapter(adapter); 
		
		// ListView Item Click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {              

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// ListView Clicked item value
				String  itemValue    = (String) listView.getItemAtPosition(position);

				// Show Alert 
				Toast.makeText(getApplicationContext(),
						"Position :"+position+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();

			}

		});
			
	
	}

}
