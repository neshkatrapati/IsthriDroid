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

		LinearLayout lh1 = new LinearLayout(this);
		lh1.setOrientation(LinearLayout.HORIZONTAL);

		TextView t1 = new TextView(getApplicationContext());
		t1.setText("Name");
		t1.setWidth(250);
		t1.setPadding(0, 10, 0, 20);
		t1.setTextSize(25);

		lh1.addView(t1);

		TextView t2 = new TextView(getApplicationContext());
		t2.setText("Cost");
		t2.setPadding(0, 10, 0, 20);
		t2.setTextSize(25);

		lh1.addView(t2);

		ll.addView(lh1);

		LinearLayout lh = new LinearLayout(this);
		lh.setOrientation(LinearLayout.HORIZONTAL);

		final EditText et = new EditText(getApplicationContext());
		et.setTextSize(15);
		et.setEms(9);
		lh.addView(et);

		final EditText et2 = new EditText(getApplicationContext());
		et2.setTextSize(15);
		et2.setEms(5);
		et2.setInputType(InputType.TYPE_CLASS_NUMBER);
		lh.addView(et2);

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

					controller.createGarment(name, Double.parseDouble(cost));

					controller.close();
					et.setText("");
					et2.setText("");
					showStuff();
				}

			}
		});

		lh.addView(b);

		ll.addView(lh);

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

		for (int i = 0; i < garments.size(); i++) {

			final String garment_name = garments.get(i).getName();
			final long garment_id = garments.get(i).getId();
			final double garment_cost = garments.get(i).getCost();

			LinearLayout lh = new LinearLayout(this);
			lh.setOrientation(LinearLayout.HORIZONTAL);

			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(20);
			tv.setText(garment_name);
			tv.setPadding(0, 0, 20, 10);
			lh.addView(tv);

			TextView tc = new TextView(getApplicationContext());
			tc.setTextSize(20);
			tc.setText(Double.toString(garment_cost));
			tc.setPadding(20, 0, 50, 10);

			lh.addView(tc);

			Button b = new Button(this);
			b.setText("X");
			b.setPadding(10, 0, 0, 0);
			b.setBackgroundColor(Color.RED);
			b.setEms(1);

			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					AlertDialog show = new AlertDialog.Builder(SettingsActivity.this)
					.setTitle("Delete Confirmation")
					.setMessage("Do you really want to delete "+garment_name+" ?")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

					    public void onClick(DialogInterface dialog, int whichButton) {
					    	final GarmentsController c = new GarmentsController(
									getApplicationContext());
							c.open();

							Garment g = new Garment();
							g.setId((int) garment_id);
							c.deleteGarment(g);
							c.close();
							showStuff();
					    }})
					 .setNegativeButton(android.R.string.no, null).show();
					
					
				}

			});

			//lh.addView(b);

			ll.addView(lh);

		}
		this.setContentView(sv);
		controller.close();

		addStuff(ll);

	}

}
