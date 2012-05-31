package apps.id;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GarmentsController {
	
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	private Context context;
	
	public GarmentsController(Context context) {
		super();
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public void createGarment(String garment_name,double cost) {
		ContentValues values = new ContentValues();
		values.put("name", garment_name);
		values.put("cost", cost);
		values.put("cdate", now());
		values.put("mdate", now());
		long insertId = database.insert("garments", null,values);
		
		/*String[] columns = {"_id","name","cost","cdate","mdate"};
		Cursor cursor = database.query("garments",columns, "_id" + " = " + insertId, null,null, null, null);
		cursor.moveToFirst();
		Comment newComment = cursorToComment(cursor);
		cursor.close();
		return newComment;*/
	}
	public List<Garment> getAllGarments() {
		List<Garment> garments = new ArrayList<Garment>();

		String[] columns = {"_id","name","cost","cdate","mdate"};
		Cursor cursor = database.query("garments",columns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Garment garment = cursorToGarment(cursor);
			garments.add(garment);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return garments;
	}
	private Garment cursorToGarment(Cursor cursor) {
		Garment garment = new Garment();
		garment.setId(cursor.getInt(0));
		garment.setName(cursor.getString(1));
		garment.setCost(cursor.getDouble(2));
		return garment;
	}
	
	public String now() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return sdf.format(cal.getTime());

	  }
	
	public void deleteGarment(Garment garment) {
		long id = garment.getId();
		
		database.delete("garments", "_id"
				+ " = " + id, null);
	}
	public Garment getGarment(int g_id ){
		
		//String[] columns = {"_id","g_id","t_id","qty","tcost","cdate"};
		String[] columns = {"_id","name","cost","cdate","mdate"};
		Cursor cursor = database.query("garments", columns, "_id" + " = "+ g_id, null,null,null,null);
		
		cursor.moveToFirst();
		
		Garment g = cursorToGarment(cursor);
		// Make sure to close the cursor
		cursor.close();
		
		return g;

		
		
	}
	public void updateGarment(long _id,String name,String cost){
		ContentValues c =  new ContentValues();
		c.put("name", name);
		c.put("cost", cost);
	    database.beginTransaction();

		int rows = database.update("garments", c , "_id = "+Long.toString(_id) , null);
		
		database.setTransactionSuccessful();
		database.endTransaction();
		
		
	}

	public boolean checkIfAlreadyPresent(String garment_name) {

		// String[] columns = {"_id","g_id","t_id","qty","tcost","cdate"};
		String[] columns = { "_id", "name", "cost", "cdate", "mdate" };
		Cursor cursor = database.query("garments", columns, "name" + " like '"
				+ garment_name + "'", null, null, null, null);

		cursor.moveToFirst();
		
		int rows = cursor.getCount();
		
		cursor.close();
		
		if(rows > 0)
			return true;
		else
			return false;
		
		
	}
	
}
