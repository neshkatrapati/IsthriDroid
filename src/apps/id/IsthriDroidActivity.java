package apps.id;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class IsthriDroidActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) findViewById(R.id.newlist);
      
    	GarmentsController controller = new GarmentsController(
				getApplicationContext());
		controller.open();
		
        
		
		controller.close();

        
    }
    public void newList(View v) {
    	
    	Context context = getApplicationContext();
    	CharSequence text = "Opening New List";
    	int duration = Toast.LENGTH_SHORT;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    	
    	Intent newlist = new Intent(this,NewListActivity.class);
    	startActivity(newlist);
    	
		
	}

	public void settings(View v) {

		Context context = getApplicationContext();
		CharSequence text = "Opening Settings";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		Intent newlist = new Intent(this, SettingsActivity.class);
		startActivity(newlist);

	}
	public void history(View v) {

		Context context = getApplicationContext();
		CharSequence text = "Opening History";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		Intent newlist = new Intent(this, HistoryActivity.class);
		startActivity(newlist);

	}
}
