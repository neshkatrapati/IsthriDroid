package apps.id;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		showStuff();

	}

	public void addStuff(LinearLayout ll) {

		TextView t = new TextView(getApplicationContext());
		t.setText("SETTINGS >> ADD Garments");
		t.setPadding(0, 10, 0, 20);
		t.setTextSize(25);

		ll.addView(t);
		
		TableLayout tb = new TableLayout(this);
		
		TableRow tr = new TableRow(this);
		
		
		TextView t1 = new TextView(getApplicationContext());
		t1.setText("Name");
		t1.setWidth(250);
		t1.setPadding(0, 10, 0, 20);
		t1.setTextSize(25);

		tr.addView(t1);

		TextView t2 = new TextView(getApplicationContext());
		t2.setText("Cost");
		t2.setPadding(0, 10, 0, 20);
		t2.setTextSize(25);

		tr.addView(t2);

		

		TableRow tr1 = new TableRow(this);
		
		final EditText et = new EditText(getApplicationContext());
		et.setTextSize(15);
		et.setEms(9);
		tr1.addView(et);

		final EditText et2 = new EditText(getApplicationContext());
		et2.setTextSize(15);
		et2.setEms(5);
		et2.setInputType(InputType.TYPE_CLASS_NUMBER);
		tr1.addView(et2);

		Button b = new Button(this);
		b.setText("Add");
		b.setEms(5);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String name = et.getText().toString();
				String cost = et2.getText().toString();
				if (name.matches("") || cost.matches("")) {

					Context context = getApplicationContext();
					CharSequence text = "Invalid Parameters!";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();

				} else {
					GarmentsController controller = new GarmentsController(
							getApplicationContext());
					controller.open();
					
					if(controller.checkIfAlreadyPresent(name)){
						
						
						AlertDialog show = new AlertDialog.Builder(SettingsActivity.this)
						.setTitle("Duplicate Insertion")
						.setMessage(name+" Exists Already!!")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setNegativeButton(android.R.string.no, null).show();
						
					}

					else{	
					controller.createGarment(name, Double.parseDouble(cost));
					}
					controller.close();
					et.setText("");
					et2.setText("");
					showStuff();
				}

			}
		});

		tr1.addView(b);

		tb.addView(tr1);
		ll.addView(tb);
		
	}

	public void showStuff() {

		ScrollView sv = new ScrollView(this);

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);

		final GarmentsController controller = new GarmentsController(
				getApplicationContext());
		controller.open();

		TextView t = new TextView(getApplicationContext());
		t.setText("SETTINGS >> Existing Garments");
		t.setPadding(0, 10, 0, 20);
		t.setTextSize(25);

		ll.addView(t);

		List<Garment> garments = controller.getAllGarments();

		final ArrayList<TextView> costs = new ArrayList<TextView>();

		TableLayout tb = new TableLayout(this);
		
		for (int i = 0; i < garments.size(); i++) {

			final String garment_name = garments.get(i).getName();
			final long garment_id = garments.get(i).getId();
			final double garment_cost = garments.get(i).getCost();

			TableRow tr = new TableRow(this);
			
			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(20);
			tv.setText(garment_name);
			tv.setPadding(0, 0, 20, 10);
			tr.addView(tv);

			TextView tc = new TextView(getApplicationContext());
			tc.setTextSize(20);
			tc.setText(Double.toString(garment_cost));
			tc.setPadding(20, 0, 50, 10);

			tr.addView(tc);

			Button b = new Button(this);
			b.setText("Edit");
			b.setPadding(10, 0, 0, 0);
			b.setEms(4);

			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					final LinearLayout lhx = new LinearLayout(SettingsActivity.this);
					lhx.setOrientation(LinearLayout.HORIZONTAL);
					
					final TextView tv = new TextView(getApplicationContext());
					tv.setTextSize(20);
					tv.setText("Edit: ");
					tv.setPadding(0, 0, 20, 10);
					lhx.addView(tv);

					
					final EditText input = new EditText(SettingsActivity.this);
					input.setText(garment_name);
					input.setEms(5);
					
					final EditText input1 = new EditText(SettingsActivity.this);
					input1.setInputType(InputType.TYPE_CLASS_NUMBER);
					input1.setText(Double.toString(garment_cost));
					input1.setEms(5);
					
					lhx.addView(input);
					lhx.addView(input1);
					
					AlertDialog show = new AlertDialog.Builder(SettingsActivity.this)
					.setTitle("Edit "+garment_name)
					.setView(lhx)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

					    public void onClick(DialogInterface dialog, int whichButton) {
					    	final GarmentsController c = new GarmentsController(
									getApplicationContext());
							c.open();
							c.updateGarment(garment_id, input.getText().toString(), input1.getText().toString());
							c.close();
							showStuff();
					    }})
					 .setNegativeButton(android.R.string.no, null).show();
					
				}

			});

			tr.addView(b);
			tb.addView(tr);
			
			

		}
		
		ll.addView(tb);
		this.setContentView(sv);
		controller.close();

		addStuff(ll);

	}

}
