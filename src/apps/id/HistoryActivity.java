package apps.id;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryActivity extends ListActivity {

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.history,getData()));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			
		
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				Intent newlist = new Intent(getApplicationContext(), ShowTransactionActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("t_id", Integer.toString(arg2));
				newlist.putExtras(bundle);
				startActivity(newlist);
				
				
				//toaster(Integer.toString(arg2));
				
			}
		});


	}
	public void toaster(String text) {

		Context context = getApplicationContext();

		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

	}
	public String[] getData(){
		
		TransactionsHelper t = new TransactionsHelper(this);
		t.open();
		
		List<Integer> transactions = t.getTransactionList();
		
		String[] data = new String[transactions.size()];
		
		for (int i = 0; i < transactions.size(); i++) {

			final int t_id = transactions.get(i);
			Transaction tr = t.getTransaction(t_id);
			String cdate = tr.getCdate();
			String Total = Double.toString(tr.getTotal());
			
			data[i] = Integer.toString(t_id)+" - " + cdate + " - "+ Total + " Rs";
			
		}
		
		
		t.close();
		
		return data;
		
	}
}
