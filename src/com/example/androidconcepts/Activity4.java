package com.example.androidconcepts;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Activity4 extends Activity {
	
	EditText etName, etId, etDept;
	String name, id, dept;
	Button btnSave, btnA5;
	private SQLiteDatabase database;
	private static final String sdcard_dbfolder_path = (Environment.getExternalStorageDirectory()).getAbsolutePath()+"/MyTestApp";
	private static final String database_name = "MyTestAppDB";
	private static final String table_name = "employees";
	
	File root_dir;
	
	Map<String, String> values = new HashMap<String, String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity4);
		
		root_dir = new File(sdcard_dbfolder_path);
		if(!root_dir.exists()) 
		{	
			root_dir.mkdir();
		}
		
		etName  = (EditText) findViewById(R.id.etName);
		etId  = (EditText) findViewById(R.id.etId);
		etDept  = (EditText) findViewById(R.id.etDept);
				
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				saveToDb();
				
				DisplayFragment recFragment = (DisplayFragment) getFragmentManager().findFragmentById(R.id.fragment1);
				if(null != recFragment  && recFragment.isInLayout())
				{
					recFragment.queryDB();
				}
				
				etName.setText("");
				etId.setText("");
				etDept.setText("");
			}
		});		
		
		btnA5 = (Button) findViewById(R.id.btnA5);
		btnA5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(Activity4.this, Activity5.class);
				startActivity(i);
			}
		});
	}
	
	public void saveToDb() {
		
		name = etName.getText().toString();
		id = etId.getText().toString();
		dept = etDept.getText().toString();
		
		try {
			database = SQLiteDatabase.openOrCreateDatabase(sdcard_dbfolder_path + File.separator + database_name, null);
			String sqlstr = "create table if not exists "+ table_name +"(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR[50] NOT NULL, emp_id VARCHAR[10] NOT NULL, dept VARCHAR[50] NOT NULL)";
			database.execSQL(sqlstr);
			
			String insert_sql = "insert into "+ table_name + "(name, emp_id, dept) values ('"+name+"','"+id+"','"+dept+"');";
			database.execSQL(insert_sql);
			database.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			if(database!=null)
				database = null;
		}
		
	}

}
