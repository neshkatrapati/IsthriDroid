package apps.id;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShowTransactionActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		String t_id = bundle.getString("t_id");
		initiate(Integer.parseInt(t_id)+1);
		

	}
	
	public void initiate(int t_id) {

		ScrollView sv = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);

		TransactionsHelper controller = new TransactionsHelper(this);
		controller.open();

		Transaction tr = controller.getTransaction(t_id);

		TextView t = new TextView(getApplicationContext());
		t.setText("DATE : " + tr.getCdate());
		t.setPadding(0, 10, 0, 20);
		t.setTextSize(30);
		ll.addView(t);

		List<Transaction> list = controller.getAllTransactions(t_id);

		final TextView tt = new TextView(getApplicationContext());
		tt.setTextSize(20);
		tt.setText("Total : " + Double.toString(tr.getTotal()));
		tt.setPadding(0, 20, 0, 10);
		
		
		GarmentsController gc = new GarmentsController(getApplicationContext());
		gc.open();
		for (int i = 0; i < list.size(); i++) {

			Transaction it = list.get(i);
			int g_id = list.get(i).getGid();
			final double garment_cost = list.get(i).getCost();
			final int qty = list.get(i).getQty();
			final int index = i;

			String garment_name = gc.getGarment(g_id).getName();

			LinearLayout lh = new LinearLayout(this);
			lh.setOrientation(LinearLayout.HORIZONTAL);

			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(20);
			tv.setText(garment_name);
			tv.setPadding(0, 0, 20, 10);
			lh.addView(tv);

			TextView tc = new TextView(getApplicationContext());
			tc.setTextSize(20);
			tc.setText(Integer.toString(qty));
			tc.setPadding(20, 0, 0, 10);

			lh.addView(tc);

			final TextView cost = new TextView(getApplicationContext());
			cost.setTextSize(20);
			cost.setText(Double.toString(garment_cost));
			cost.setPadding(20, 0, 0, 10);

			lh.addView(cost);

			ll.addView(lh);

		}
		LinearLayout lh = new LinearLayout(this);
		lh.setOrientation(LinearLayout.HORIZONTAL);
		
		final TextView paid = new TextView(getApplicationContext());
		paid.setTextSize(20);
		paid.setText("Paid Amt: ");
		paid.setPadding(0, 0, 0, 10);

		
		EditText e = new EditText(getApplicationContext());
		e.setEms(10);
		
		Button b = new Button(getApplicationContext());
		b.setText("Update");
		
		lh.addView(paid);
		lh.addView(e);
		lh.addView(b);
		
		ll.addView(tt);
		ll.addView(lh);
		this.setContentView(sv);
		controller.close();
		gc.close();
	}

}
