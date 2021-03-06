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
	
	ContentValues ret = new ContentValues();
	ret.put("qty", 0);
	
	long insertId = database.insert("transactions", null,values);
	
	long insertId2 = database.insert("returns", null,ret);
	
}
public Transaction getTransaction(int t_id){
	
	
	List<Transaction> garments = new ArrayList<Transaction>();
	
	//String[] columns = {"_id","g_id","t_id","qty","tcost","cdate"};
	Cursor cursor = database.rawQuery("select distinct(cdate) from transactions where t_id = ?",new String[] {Integer.toString(t_id)});
	cursor.moveToFirst();
	
	Transaction t = new Transaction();
	t.setId(t_id);
	t.setCdate(cursor.getString(0));
	
	cursor = database.rawQuery("select sum(tcost) from transactions where t_id = ?",new String[] {Integer.toString(t_id)});
	cursor.moveToFirst();

		
	t.setTotal(cursor.getDouble(0));
	
	
	cursor = database.rawQuery("select distinct(paid) from transactions where t_id = ?",new String[] {Integer.toString(t_id)});
	cursor.moveToFirst();
	
	t.setPaid(cursor.getDouble(0));
	
	// Make sure to close the cursor
	cursor.close();
	
	return t;

	
	
}
public List<Integer> getTransactionIds(){
	
	Cursor cursor = database.rawQuery("select distinct(t_id) as tid from transactions", null);
	cursor.moveToFirst();
	List<Integer> ids = new ArrayList<Integer>();
	int curr = 0;
	while (!cursor.isAfterLast()) {
		curr = cursor.getInt(0);
		ids.add(curr);
		cursor.moveToNext();
	}
	cursor.close();
	return ids;
	
} 
public int[] getAbuttingTransactions(int id){
	
	int[] abutting = {-1,-1};
	int curr = 0;
	boolean found = false;
	Cursor cursor = database.rawQuery("select distinct(t_id) as tid from transactions", null);
	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
		curr = cursor.getInt(0);
		if (found) {
			
			abutting[1] = curr;
			break;
		}
		if (curr == id){
			
			found = true;
			
		}
		if (!found){
			abutting[0] = curr;
		}
		
		cursor.moveToNext();
	}
	cursor.close();
	return abutting;	
	
	
}
public int getLastTransaction(){
	
	Cursor cursor = database.rawQuery("select max(t_id) as tid from transactions", null);
	cursor.moveToFirst();
	int item = cursor.getInt(0);
	cursor.close();
	return item;	
	
}
public List<Transaction> getAllTransactions(int t_id) {
	List<Transaction> garments = new ArrayList<Transaction>();

	//String[] columns = {"_id","g_id","t_id","qty","tcost","cdate"};
	Cursor cursor = database.rawQuery("select *,(select r.qty from returns r where r._id=t._id) as rqty,(select g.cost from garments g where g._id=t.g_id) as icost from transactions t where t_id = ?",new String[] {Integer.toString(t_id)});

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
	t.setRqty(cursor.getInt(7));
	t.setIcost(cursor.getDouble(8));
	return t;
}

public String now() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    return sdf.format(cal.getTime());

  }
public void updatePaid(int t_id,double paid){
	ContentValues c =  new ContentValues();
	c.put("paid", Double.toString(paid));
    database.beginTransaction();

	int rows = database.update("transactions", c , "t_id = "+Integer.toString(t_id) , null);
	
	database.setTransactionSuccessful();
	database.endTransaction();
	
	
}
public void updateReturns(int id,double qty){
	ContentValues c =  new ContentValues();
	c.put("qty",qty);
    database.beginTransaction();

	int rows = database.update("returns", c , "_id = "+Integer.toString(id) , null);
	
	database.setTransactionSuccessful();
	database.endTransaction();
	
	
}
public void deleteTransaction(Transaction transaction) {
	long id = transaction.getTid();
	
	database.delete("transactions", "t_id"
			+ " = " + id, null);
}

public String getFullTotals(){
	
	Cursor cursor = database.rawQuery("select sum(tcost) from transactions",null);
	cursor.moveToFirst();
	double data = cursor.getDouble(0);
	cursor.close();
	return Double.toString(data);
	
	}
public String getFullPaid(){
	
	Cursor cursor = database.rawQuery("select distinct(paid) from transactions",null);
	cursor.moveToFirst();
	double paid = 0;
	while (!cursor.isAfterLast()) {
		paid += cursor.getDouble(0);
		cursor.moveToNext();
	}
	cursor.close();
	return Double.toString(paid);
	
}
public String getNumberOfTransactions(){
	
	Cursor cursor = database.rawQuery("select distinct(t_id) from transactions",null);
	cursor.moveToFirst();
	int data = cursor.getCount();
	cursor.close();
	return Integer.toString(data);
	
}

public String getReturnable(){

	Cursor cursor = database.rawQuery("select sum(t.qty - (select r.qty from returns r where r._id=t._id)) as sum from transactions t",null);
	cursor.moveToFirst();
	int data = cursor.getInt(0);
	cursor.close();
	return Integer.toString(data);
	
	
}

public String getNumberOfGarments(){
	
	Cursor cursor = database.rawQuery("select sum(qty) from transactions",null);
	cursor.moveToFirst();
	int data = cursor.getInt(0);
	cursor.close();
	return Integer.toString(data);
	
}
public String getNumberOfTypesOfGarments(){
	
	Cursor cursor = database.rawQuery("select distinct(_id) from garments",null);
	cursor.moveToFirst();
	int data = cursor.getCount();
	cursor.close();
	return Integer.toString(data);
	
}
public String getTransactionsInDebt(){
	
	Cursor cursor = database.rawQuery("select distinct(t_id) from transactions",null);
	cursor.moveToFirst();
	int data = 0;
	while (!cursor.isAfterLast()) {
		int t_id = cursor.getInt(0);
		Transaction t = getTransaction(cursor.getInt(0));
		double tot = t.getTotal();
		Cursor cs = database.rawQuery("select paid from transactions where t_id = ?",new String[] {Integer.toString(t_id)});
		cs.moveToFirst();
		double paid = cs.getDouble(0);
		if((tot - paid) > 0)
			data++;
		cursor.moveToNext();
	}
	cursor.close();
	
	return Integer.toString(data);
}
public String getTransactionsInDebit(){
	
	Cursor cursor = database.rawQuery("select distinct(t_id) from transactions",null);
	cursor.moveToFirst();
	int data = 0;
	while (!cursor.isAfterLast()) {
		int t_id = cursor.getInt(0);
		Transaction t = getTransaction(cursor.getInt(0));
		double tot = t.getTotal();
		Cursor cs = database.rawQuery("select paid from transactions where t_id = ?",new String[] {Integer.toString(t_id)});
		cs.moveToFirst();
		double paid = cs.getDouble(0);
		if((paid - tot) >= 1)
			data++;
		cursor.moveToNext();
	}
	cursor.close();
	
	return Integer.toString(data);
}
public String getTransactionsPaid(){
	
	Cursor cursor = database.rawQuery("select distinct(t_id) from transactions",null);
	cursor.moveToFirst();
	int data = 0;
	while (!cursor.isAfterLast()) {
		int t_id = cursor.getInt(0);
		Transaction t = getTransaction(cursor.getInt(0));
		double tot = t.getTotal();
		Cursor cs = database.rawQuery("select paid from transactions where t_id = ?",new String[] {Integer.toString(t_id)});
		cs.moveToFirst();
		double paid = cs.getDouble(0);
		if((tot - paid) == 0)
			data++;
		cursor.moveToNext();
	}
	cursor.close();
	
	return Integer.toString(data);
}

}