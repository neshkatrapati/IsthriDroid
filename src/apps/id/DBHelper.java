package apps.id;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "isdroid.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String QUERY_GARMENTS = "create table garments ( _id integer primary key autoincrement, name text not null, cost real, cdate string, mdate string);";
	private static final String QUERY_TRANSACTIONS = "create table transactions ( _id integer primary key autoincrement, t_id integer, g_id integer, qty integer, tcost real, paid real, cdate string);";
	private static final String QUERY_RETURNS = "create table returns ( _id integer primary key autoincrement, qty integer, cdate string);";
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(QUERY_GARMENTS);
		database.execSQL(QUERY_TRANSACTIONS);
		database.execSQL(QUERY_RETURNS);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(DBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
		onCreate(db);
	}
	
	
}
