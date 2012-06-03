package apps.id;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.UserDictionary.Words;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

		
		setListAdapter(new ArrayAdapter<Spannable>(this, R.layout.history,getData()));

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
	public Spannable[] getData(){
		
		TransactionsHelper t = new TransactionsHelper(this);
		t.open();
		
		List<Integer> transactions = t.getTransactionList();
		
		Spannable[] data = new Spannable[transactions.size()];
		
		for (int i = 0; i < transactions.size(); i++) {

			final int t_id = transactions.get(i);
			Transaction tr = t.getTransaction(t_id);
			String cdate = tr.getCdate();
			String Total = Double.toString(tr.getTotal());
			
			String txt = Integer.toString(t_id)+" - " + cdate + " - "+ Total + " Rs BAL: " + tr.hasPaid();
			
			Spannable WordtoSpan = new SpannableString(txt);
			String Status = tr.hasPaid();
			
			if(Status.equals("Paid")){
				
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), txt.length()-4, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				
			}     
			else if (Status.startsWith("-")){
				
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), txt.length()-Status.length(), txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			else{
				
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), txt.length()-Status.length(), txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			data[i] = WordtoSpan;
			
		}
		
		
		t.close();
		
		return data;
		
	}
}
