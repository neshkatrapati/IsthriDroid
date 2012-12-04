package apps.id;

import java.util.List;
import java.util.logging.LogManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class ShowTransactionActivity extends Activity implements
		 android.view.GestureDetector.OnGestureListener {

	GestureDetector gDetector;
	int present_id;
	int[] abutting;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		String t_id = bundle.getString("t_id");
		initiate(Integer.parseInt(t_id));
		gDetector = new GestureDetector(getApplicationContext(),this); 
		present_id = Integer.parseInt(t_id) + 1;
		
				

	}

	public void initiate(final int t_id) {

		present_id = t_id;
		ScrollView sv = new ScrollView(this);

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);

		final TransactionsHelper controller = new TransactionsHelper(this);
		controller.open();
		abutting = controller.getAbuttingTransactions(t_id);
		Context context = getApplicationContext();
    	CharSequence text = abutting[0] + " " + t_id + " " +  abutting[1];
    	int duration = Toast.LENGTH_SHORT;

    	Toast toast = Toast.makeText(context, text, duration);
    	//toast.show();
		final Transaction tr = controller.getTransaction(t_id);

		final double paidamt = tr.getPaid();
		final String Paid = Double.toString(paidamt);

		TextView t = new TextView(getApplicationContext());
		t.setText("DATE : " + tr.getCdate());
		t.setGravity(Gravity.CENTER_HORIZONTAL);
		t.setBackgroundColor(Color.DKGRAY);
		t.setPadding(0, 10, 0, 20);
		t.setTextSize(20);
		ll.addView(t);

		List<Transaction> list = controller.getAllTransactions(t_id);

		final TextView tt = new TextView(getApplicationContext());
		tt.setTextSize(20);
		tt.setText("Total : " + Double.toString(tr.getTotal()));
		tt.setGravity(Gravity.CENTER_HORIZONTAL);
		tt.setPadding(0, 20, 0, 10);

		GarmentsController gc = new GarmentsController(getApplicationContext());
		gc.open();
		HorizontalScrollView sc2 = new HorizontalScrollView(
				getApplicationContext());

		TableLayout tb = new TableLayout(this);

		sc2.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_INSET);
		TableRow th = new TableRow(this);

		TextView h1 = new TextView(this);
		h1.setText("Garment");
		h1.setPadding(0, 0, 20, 10);
		h1.setTextSize(20);

		th.addView(h1);

		h1 = new TextView(this);
		h1.setText("Quantity");
		h1.setPadding(0, 0, 20, 10);
		h1.setTextSize(20);

		th.addView(h1);

		h1 = new TextView(this);
		h1.setText("Cost");
		h1.setPadding(0, 0, 20, 10);
		h1.setTextSize(20);

		th.addView(h1);

		h1 = new TextView(this);
		h1.setText("Returned");
		h1.setPadding(0, 0, 20, 10);
		h1.setTextSize(20);

		th.addView(h1);

		h1 = new TextView(this);
		h1.setText("Returned Cost");
		h1.setPadding(0, 0, 20, 10);
		h1.setTextSize(20);

		th.addView(h1);

		tb.addView(th);
		double return_total = 0.0;
		
		final TextView rt = new TextView(getApplicationContext());
		rt.setTextSize(20);
		rt.setGravity(Gravity.CENTER_HORIZONTAL);
		rt.setPadding(0, 0, 0, 10);

		for (int i = 0; i < list.size(); i++) {

			Transaction it = list.get(i);
			int g_id = list.get(i).getGid();
			final double garment_cost = list.get(i).getCost();
			final int qty = list.get(i).getQty();
			final int index = i;
			final int rqty = list.get(i).getRqty();
			final int trid = list.get(i).getId();
			double icost = list.get(i).getIcost();

			String garment_name = gc.getGarment(g_id).getName();

			TableRow lh = new TableRow(this);

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

			// Button rcb = new Button(getApplicationContext());

			TextView rc = new TextView(getApplicationContext());
			rc.setTextSize(20);
			rc.setClickable(true);
			rc.setPaintFlags(rc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			// rc.setTextColor(Color.BLUE);
			rc.setText(Integer.toString(rqty));
			rc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LinearLayout lx = new LinearLayout(getApplicationContext());
					lx.setOrientation(lx.HORIZONTAL);
					final NumberPicker spinner_returns = new NumberPicker(
							getApplicationContext());
					spinner_returns.mStart = 0;
					spinner_returns.mCurrent = 0;
					spinner_returns.mEnd = qty;
					lx.addView(spinner_returns);
					lx.setGravity(Gravity.CENTER_HORIZONTAL);
					// final EditText input = new
					// EditText(ShowTransactionActivity.this);
					// input.setInputType(InputType.TYPE_CLASS_NUMBER);

					new AlertDialog.Builder(ShowTransactionActivity.this)
							.setTitle("Update Status")
							.setMessage("Enter Returned Qty")
							.setView(lx)
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											
											int value = spinner_returns
													.getCurrent();

											controller.open();
											int id = trid;
											controller.updateReturns(id, value);
											controller.close();
											
											//double return_total2 = value * rqty;
											//rt.setText("Returned Total : " + Double.toString(return_total2));
											
											startActivity(getIntent());
											finish();

										}
									})
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// Do nothing.
										}
									}).show();

				}
			});

			rc.setPadding(20, 0, 0, 10);

			lh.addView(rc);

			final TextView ixcost = new TextView(getApplicationContext());
			ixcost.setTextSize(20);
			ixcost.setText(Double.toString(icost * rqty));
			return_total += icost * rqty;
			rt.setText("Returned Total : " + Double.toString(return_total));
			ixcost.setPadding(20, 0, 0, 10);

			lh.addView(ixcost);

			tb.addView(lh);

		}
		sc2.addView(tb);
		ll.addView(sc2);
		gc.close();
		LinearLayout lh = new LinearLayout(this);
		lh.setOrientation(LinearLayout.HORIZONTAL);

		final TextView paid = new TextView(getApplicationContext());
		paid.setTextSize(20);
		paid.setText("Paid Amt: ");
		paid.setPadding(0, 0, 0, 10);

		/*
		 * final TextView topay = new TextView(getApplicationContext());
		 * topay.setTextSize(20);
		 * topay.setText("To Pay: "+Double.toString(tr.getTotal() -
		 * tr.getPaid())); topay.setPadding(0, 0, 0, 10);
		 */

		final TextView status = new TextView(getApplicationContext());
		status.setTextSize(20);
		status.setText("Balance: " + tr.hasPaid());
		status.setPadding(0, 0, 0, 10);
		status.setGravity(Gravity.CENTER_HORIZONTAL);

		final TextView e = new TextView(getApplicationContext());
		e.setTextSize(20);
		e.setText(Double.toString(tr.getPaid()));
		e.setPadding(0, 0, 0, 10);

		final TextView e1 = new TextView(getApplicationContext());
		e1.setTextSize(20);
		e1.setText(" Rs");
		e1.setPadding(0, 0, 20, 10);

		Button b = new Button(getApplicationContext());
		b.setText("Update");

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final EditText input = new EditText(
						ShowTransactionActivity.this);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				input.setGravity(Gravity.CENTER_HORIZONTAL);
				input.setPadding(5, 0, 5, 5);

				new AlertDialog.Builder(ShowTransactionActivity.this)
						.setTitle("Update Status")
						.setMessage("Enter Paid Amt")
						.setView(input)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										String value = input.getText()
												.toString();

										controller.open();
										int id = tr.getId();
										controller.updatePaid(id,
												Double.parseDouble(value));
										controller.close();

										startActivity(getIntent());
										finish();

									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// Do nothing.
									}
								}).show();

			}
		});

		lh.addView(paid);

		lh.addView(e);
		lh.addView(e1);
		

		lh.setGravity(Gravity.CENTER_HORIZONTAL);
		ll.addView(tt);
		ll.addView(rt);
		// ll.addView(topay);
		ll.addView(lh);
		LinearLayout lh2 = new LinearLayout(getApplicationContext());
		lh2.setOrientation(LinearLayout.HORIZONTAL);
		
		lh2.addView(status);
		lh2.addView(b);
		lh2.setGravity(Gravity.CENTER_HORIZONTAL);
		
		ll.addView(lh2);
		

		this.setContentView(sv);
		controller.close();

	}

	@Override


	public boolean onDown(MotionEvent arg0) {

		// TODO Auto-generated method stub

		return false;

	}

	
	public boolean onFling(MotionEvent start, MotionEvent finish,
			float xVelocity, float yVelocity) {
		Log.d(null, "Flinged");

		if ((start.getRawY() < finish.getRawY()) && (Math.abs(xVelocity) < Math.abs(yVelocity))) {
			
			Context context = getApplicationContext();
	    	CharSequence text = "Down Swipe " + abutting[0];
	    	int duration = Toast.LENGTH_SHORT;

	    	Toast toast = Toast.makeText(context, text, duration);
	    	//toast.show();
	    	if (abutting[0] != -1){
	    		initiate(abutting[0] );
	    	}
	    	

		} else if ((Math.abs(xVelocity) < Math.abs(yVelocity))) {

			Context context = getApplicationContext();
	    	CharSequence text = "Up Swipe " + abutting[1];
	    	int duration = Toast.LENGTH_SHORT;

	    	Toast toast = Toast.makeText(context, text, duration);
	    	//toast.show();
	    	
	    	if (abutting[1] != -1){
	    		Log.v("Uptag", "Next : "+abutting[1]);
	    		initiate(abutting[1]);
	    	}
	    	
		}

		return true;

	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {

		Log.v(null, "Touched");
		super.dispatchTouchEvent(me); 
		return gDetector.onTouchEvent(me);

	}

	
	public void onLongPress(MotionEvent arg0) {

		// TODO Auto-generated method stub

	}

	
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {

		// TODO Auto-generated method stub

		return false;

	}

	
	public void onShowPress(MotionEvent arg0) {

		// TODO Auto-generated method stub

	}

	
	public boolean onSingleTapUp(MotionEvent arg0) {

		// TODO Auto-generated method stub

		return false;

	}

}
