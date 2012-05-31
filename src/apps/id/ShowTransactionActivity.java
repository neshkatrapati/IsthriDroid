package apps.id;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	public void initiate(final int t_id) {

		ScrollView sv = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);

		final TransactionsHelper controller = new TransactionsHelper(this);
		controller.open();

		final Transaction tr = controller.getTransaction(t_id);
		
		final double paidamt = tr.getPaid();
		final String Paid = Double.toString(paidamt);

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
		gc.close();
		LinearLayout lh = new LinearLayout(this);
		lh.setOrientation(LinearLayout.HORIZONTAL);
		
		final TextView paid = new TextView(getApplicationContext());
		paid.setTextSize(20);
		paid.setText("Paid Amt: ");
		paid.setPadding(0, 0, 0, 10);
		
		/* final TextView topay = new TextView(getApplicationContext());
		topay.setTextSize(20);
		topay.setText("To Pay: "+Double.toString(tr.getTotal() - tr.getPaid()));
		topay.setPadding(0, 0, 0, 10); */
		
		final TextView status = new TextView(getApplicationContext());
		status.setTextSize(20);
		status.setText("Status: "+tr.hasPaid());
		status.setPadding(0, 0, 0, 10);

		
		final TextView e = new TextView(getApplicationContext());
		e.setTextSize(20);
		e.setText(Double.toString(tr.getPaid()));
		e.setPadding(0, 0, 0, 10);
 
		final TextView e1 = new TextView(getApplicationContext());
		e1.setTextSize(20);
		e1.setText(" Rs");
		e1.setPadding(0, 0, 0, 10);

		Button b = new Button(getApplicationContext());
		b.setText("Update");
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final EditText input = new EditText(ShowTransactionActivity.this);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				
				

				new AlertDialog.Builder(ShowTransactionActivity.this)
			    .setTitle("Update Status")
			    .setMessage("Enter Paid Amt")
			    .setView(input)
			    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            String value = input.getText().toString();
			            if (Double.parseDouble(value) > tr.getTotal()){
			            	
			            	AlertDialog show = new AlertDialog.Builder(ShowTransactionActivity.this)
							.setTitle("Wtong Input")
							.setMessage("Paid amount cannot be greater than Actual amount")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

							 public void onClick(DialogInterface dialog, int whichButton) {
							 
							 }})
							 .setNegativeButton(android.R.string.no, null).show();
			            	
			            }
			            else{
			            	controller.open();
			            	int id = tr.getId();
			            	controller.updatePaid(id, Double.parseDouble(value));
			            	controller.close();
			            	
			            	startActivity(getIntent()); finish();

			            }
			            
			        }
			    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            // Do nothing.
			        }
			    }).show();

			}
		});
		
		
		
		lh.addView(paid);
		
		lh.addView(e);
		lh.addView(e1);
		lh.addView(b);
		
		ll.addView(tt);
		//ll.addView(topay);
		ll.addView(status);
		ll.addView(lh);
		
		this.setContentView(sv);
		controller.close();
		
	}

}
