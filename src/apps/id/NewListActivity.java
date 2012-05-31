package apps.id;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class NewListActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		ScrollView sv = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);

		GarmentsController controller = new GarmentsController(
				getApplicationContext());
		controller.open();
		
		
		TextView t = new TextView(getApplicationContext());
		t.setText("CREATE A NEW LIST");
		t.setPadding(0, 10, 0, 20);
		t.setTextSize(30);
		ll.addView(t);
		
		
		List<Garment> garments = controller.getAllGarments();
		
		
	
		final TextView tt = new TextView(getApplicationContext());
		tt.setTextSize(20);
		tt.setText("Total");
		tt.setPadding(0, 20, 0, 10);
		
		final ArrayList<TextView> costs = new ArrayList<TextView>();
		
		final String[][] bill = new String[garments.size()][3];
		
		
		for (int i = 0; i < garments.size(); i++) {

			String garment_name = garments.get(i).getName(); 
			final double garment_cost = garments.get(i).getCost();
			final int garment_id = garments.get(i).getId();
			final int index = i;
			
			
			bill[index][0] = Integer.toString(garment_id);
		
			
			
			LinearLayout lh = new LinearLayout(this);
			lh.setOrientation(LinearLayout.HORIZONTAL);
			
			
			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(20);
			tv.setText(garment_name);
			tv.setPadding(0, 0, 20, 10);
			lh.addView(tv);
			
		    final EditText et = new EditText(getApplicationContext());
		    et.setTextSize(15);
		    et.setEms(5);
		    et.setInputType(InputType.TYPE_CLASS_NUMBER);
		    lh.addView(et);
		    
			TextView tc = new TextView(getApplicationContext());
			tc.setTextSize(20);
			tc.setText(Double.toString(garment_cost));
			tc.setPadding(20, 0, 0, 10);
			
			lh.addView(tc);
			

			final TextView cost = new TextView(getApplicationContext());
			cost.setTextSize(20);
			cost.setText("Cost");
			cost.setPadding(20, 0, 0, 10);
			costs.add(cost);
			lh.addView(cost);
			

		    et.addTextChangedListener(new TextWatcher(){
		        public void afterTextChanged(Editable s) {
		        	
		        	if(et.getText().toString().matches("")){
		        		
		        		cost.setText("Cost");

			        	bill[index][1] = "";
			        	bill[index][2] = "";
		        		
		        	}
		        	else{

			        	double ct = garment_cost * Double.parseDouble(et.getText().toString());
			        	cost.setText(Double.toString(ct));
			        	tt.setText(Double.toString(getTotal(costs)));
			        	bill[index][1] = et.getText().toString();
			        	bill[index][2] = Double.toString(ct);
			        	
		        	}
		        }
		        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		        public void onTextChanged(CharSequence s, int start, int before, int count){}
		    });
		    
		    
		    
			ll.addView(lh);

		}
		

		LinearLayout lo = new LinearLayout(this);
		lo.setOrientation(LinearLayout.HORIZONTAL);
		
		tt.setPadding(0, 20,50,0);
		
		Button b = new Button(getApplicationContext());
		b.setText("Save");
		b.setEms(5);
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog show = new AlertDialog.Builder(NewListActivity.this)
				.setTitle("Add Confirmation")
				.setMessage("Do you want to Save ?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				    public void onClick(DialogInterface dialog, int whichButton) {
				    	putTransactions(bill);
				    	toaster("Added List Succesfully");
				    }})
				 .setNegativeButton(android.R.string.no, null).show();
				
			}
		});
		
		
		lo.addView(tt);
		lo.addView(b);
		
		
		
		ll.addView(lo);
		
		this.setContentView(sv);
		controller.close();

	}

	public void putTransactions(String[][] bill) {

		TransactionsHelper t = new TransactionsHelper(getApplicationContext());
		t.open();
		int tid = t.getLastTransaction() + 1;
		for (int i = 0; i < bill.length; i++) {

			int gid = Integer.parseInt(bill[i][0]);
			if (!bill[i][1].matches("")) {

				int qty = Integer.parseInt(bill[i][1]);
				double cost = Double.parseDouble(bill[i][2]);
				
				t.createTransaction(gid, tid, qty, cost);
			}
		}
		t.close();

	}
	public void toaster(String text) {

		Context context = getApplicationContext();

		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

	}
	public double getTotal(ArrayList<TextView> a){
		
		double total = 0;
		for(int i= 0; i < a.size(); i++){
			
			if (!a.get(i).getText().equals("Cost")){
				
				//toaster(a.get(i).getText().toString());
				String text =  a.get(i).getText().toString();
				total += Double.parseDouble(a.get(i).getText().toString());
				
			}
			
		}
		return total;
		
		
	}
}

