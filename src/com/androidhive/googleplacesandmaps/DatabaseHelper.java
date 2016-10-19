package com.androidhive.googleplacesandmaps;



import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "employee_database";
	public static final int DB_VERSION = 1;

	public static final String EMPLOYEE_TABLE = "employee";
	public static final String ID_FIELD = "_id";
	public static final String NAME_FIELD = "name";
	public static final String EMAIL_FIELD = "email";
	public static final String PHONE_FIELD = "phone";
	public static final String Sex_FIELD = "sex";
	public static final String DESIGNATION_FIELD = "designation";


	public static final String EMPLOYEE_TABLE_SQL = "CREATE TABLE "
			+ EMPLOYEE_TABLE + " (" + ID_FIELD + " INTEGER, "
			+ NAME_FIELD + " TEXT PRIMARY KEY, " + EMAIL_FIELD + " TEXT, " + PHONE_FIELD
			+ " TEXT, " + Sex_FIELD + " TEXT, " + DESIGNATION_FIELD + " TEXT)";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// create tables
		db.execSQL(EMPLOYEE_TABLE_SQL);
		Log.e("TABLE CREATE", EMPLOYEE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// upgrade logic

	}
	public long insertEmployee(String name, String email , String phn,String desig , String sax)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(NAME_FIELD, name);
		values.put(EMAIL_FIELD, email);
		values.put(PHONE_FIELD, phn);
		values.put(Sex_FIELD, sax);
		values.put(DESIGNATION_FIELD, desig);

		long inserted=db.insert(EMPLOYEE_TABLE, null, values);
		db.close();
		
		return inserted;
		
		
	}

	public Cursor getUser(String naamdtehbe){

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT * FROM employee WHERE name=?";

		return db.rawQuery(sql, new String[] { naamdtehbe } );
	}

//	public ArrayList<Employee>getAllEmployees()
//	{
//
//		ArrayList<Employee>allEmployees= new ArrayList<Employee>();
//		SQLiteDatabase db=this.getReadableDatabase();
//
//		//String []columns={NAME_FIELD,PHONE_FIELD};
//		Cursor cursor=db.query(EMPLOYEE_TABLE, null, null,null, null, null, null);
//		if(cursor!=null && cursor.getCount()>0 )
//		{
//			cursor.moveToFirst();
//			for(int i=0;i<cursor.getCount();i++)
//			{
//				int id=cursor.getInt(cursor.getColumnIndex(ID_FIELD));
//				String name=cursor.getString(cursor.getColumnIndex(NAME_FIELD));
//				String email=cursor.getString(cursor.getColumnIndex(EMAIL_FIELD));
//				String phone=cursor.getString(cursor.getColumnIndex(PHONE_FIELD));
//				String designation=cursor.getString(cursor.getColumnIndex(DESIGNATION_FIELD));
//				Employee e=new Employee(id,name,email,phone,designation);
//
//				allEmployees.add(e);
//				cursor.moveToNext();
//
//
//			}
//		}
//
//		cursor.close();
//		db.close();
//		return allEmployees;
//
//	}
	
}
