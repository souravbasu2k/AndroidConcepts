package com.example.androidconcepts;

import java.io.File;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayFragment extends Fragment {
	
	private SQLiteDatabase database;
	private static final String sdcard_dbfolder_path = (Environment.getExternalStorageDirectory()).getAbsolutePath()+"/MyTestApp";
	private static final String database_name = "MyTestAppDB";
	private static final String table_name = "employees";
	TextView tvFName, tvFId, tvFDept;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.display_fragment, container, false);
		
		tvFName  = (TextView) view.findViewById(R.id.tvFName);
		tvFId  = (TextView) view.findViewById(R.id.tvFId);
		tvFDept  = (TextView) view.findViewById(R.id.tvFDept);
		
		return view;
	}
	
	public void queryDB()
	{
		Cursor cursorobj=null;
		try {
			database = SQLiteDatabase.openOrCreateDatabase(sdcard_dbfolder_path + File.separator + database_name, null);
			String sqlstr = "select name, emp_id, dept from "+ table_name + " order by id desc limit 1";
			cursorobj = database.rawQuery(sqlstr, null);
			cursorobj.moveToFirst();
			
			tvFName.setText("Name: "+ cursorobj.getString(0));
			tvFId.setText("Emp Id: "+ cursorobj.getString(1));
			tvFDept.setText("Dept: "+ cursorobj.getString(2));
			
			database.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			if(database!=null)
				database = null;
		}
	}

}
