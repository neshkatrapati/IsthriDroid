package apps.id;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class StatsActivity extends ListActivity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.history,getData()));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		
		

	}
	public void toaster(String text) {

		Context context = getApplicationContext();

		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

	}
	public String[] getData(){
		
		TransactionsHelper th = new TransactionsHelper(getApplicationContext());
		th.open();
		
		
		String diff = Double.toString((Double.parseDouble(th.getFullTotals()) - Double.parseDouble(th.getFullPaid())));
		
		String[] t = new String[]{"Statistics","Total Amount: "+ th.getFullTotals(),"Total Paid: "+ th.getFullPaid(),"Garments be Returned: "+ th.getReturnable(),"To be Paid Amount: "+ diff,"Number Of Transactions: "+ th.getNumberOfTransactions(),"Number Of Garments Ironed: "+ th.getNumberOfGarments(),"Number Of Types of Garments: "+ th.getNumberOfTypesOfGarments(),"Debit Transactions : "+ th.getTransactionsInDebt(),"Paid Transactions : "+ th.getTransactionsPaid(),"Credit Transactions : "+ th.getTransactionsInDebit()};
		th.close();
		
		return t;
		
		
	}

}
