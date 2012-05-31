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

public class TransactionsHelper {

private SQLiteDatabase database;
private DBHelper dbHelper;
private Context context;

public TransactionsHelper(Context context) {
	super();
	dbHelper = new DBHelper(context);
}

public void open() throws SQLException {
	database = dbHelper.getWritableDatabase();
}

public void close() {
	dbHelper.close();
}

public void createTransaction(int g_id,int tid,int qty,double cost) {
	ContentValues values = new ContentValues();
	values.put("g_id", g_id);
	values.put("t_id", tid);
	values.put("qty", qty);
	values.put("tcost", cost);
	values.put("paid", 0);
	values.put("cdate", now());
	
	long insertId = database.insert("transactions", null,values);
	
}
public Transaction getTransaction(int t_id){
	
	List<Transaction> garments = new ArrayList<Transaction>();

	//String[] columns = {"_id","g_id","t_id","qty","tcost","cdate"};
	Cursor cursor = database.rawQuery("select distinct(cdate) from transactions where t_id = ?",new String[] {Integer.toString(t_id)});
	cursor.moveToFirst();
	
	Transaction t = new Transaction();
	t.setCdate(cursor.getString(0));
	
	cursor = database.rawQuery("select sum(tcost) from transactions where t_id = ?",new String[] {Integer.toString(t_id)});
	cursor.moveToFirst();
	
	t.setTotal(cursor.getDouble(0));
	
	// Make sure to close the cursor
	cursor.close();
	
	return t;

	
	
}
public int getLastTransaction(){
	
	Cursor cursor = database.rawQuery("select max(t_id) as tid from transactions", null);
	cursor.moveToFirst();
	return cursor.getInt(0);
	
	
	
	
}
public List<Transaction> getAllTransactions(int t_id) {
	List<Transaction> garments = new ArrayList<Transaction>();

	//String[] columns = {"_id","g_id","t_id","qty","tcost","cdate"};
	Cursor cursor = database.rawQuery("select * from transactions where t_id = ?",new String[] {Integer.toString(t_id)});

	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
		Transaction garment = cursorToTransaction(cursor);
		garments.add(garment);
		cursor.moveToNext();
	}
	// Make sure to close the cursor
	cursor.close();
	return garments;
}
public List<Integer> getTransactionList() {
	List<Integer> garments = new ArrayList<Integer>();

	//String[] columns = {"_id","g_id","t_id","qty","tcost","paid","cdate"};
	Cursor cursor = database.rawQuery("select distinct t_id from transactions", null);

	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
		garments.add(cursor.getInt(0));
		cursor.moveToNext();
	}
	// Make sure to close the cursor
	cursor.close();
	return garments;
}

private Transaction cursorToTransaction(Cursor cursor) {
	Transaction t = new Transaction();
	t.setId(cursor.getInt(0));
	t.setGid(cursor.getInt(2));
	t.setTid(cursor.getInt(1));
	t.setQty(cursor.getInt(3));
	t.setCost(cursor.getDouble(4));
	t.setPaid(cursor.getDouble(5));
	t.setCdate(cursor.getString(6));
	return t;
}

public String now() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    return sdf.format(cal.getTime());

  }

public void deleteGarment(Garment garment) {
	long id = garment.getId();
	
	database.delete("garments", "_id"
			+ " = " + id, null);
	}
}