package accessible.com.accesslight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import accessible.com.utils.DataAdapter;
import accessible.com.utils.Light;

public class ListSavedMeasurementsActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ShareActionProvider mShareActionProvider;
    ListView mListView;
    DataAdapter mDataAdapter;
    public final static String EXTRA_MESSAGE_TIMESTAMP = "access.light.load.old.intensity.MESSAGE.timestamp";
    public final static String EXTRA_MESSAGE_INTENSITY_LEVEL = "access.light.load.old.intensity.MESSAGE.intensity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_saved_measurements);
        mDataAdapter = new DataAdapter(this, getLayoutInflater());
        mListView = (ListView) findViewById(R.id.main_listview);
        mListView.setAdapter(mDataAdapter);
        mListView.setOnItemClickListener(this);
        mDataAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_saved_measurements, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.homeMenuBtn) {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
            return true;
        }
        else if (id == R.id.showSavedLogsMenuBtn) {
            return true;
        }
        else if (id == R.id.measureLightMenuBtn) {
            Intent measureLightActivityIntent = new Intent(this, MeasureLightActivity.class);
            startActivity(measureLightActivityIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Access Light");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Measure room light intensity level with Access light app.");

            // Make sure the provider knows
            // it should work with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Light selectedLightIntensityItem = mDataAdapter.getItem(position);

        if (selectedLightIntensityItem != null) {
            Intent loadOldMeasurementIntent = new Intent(this, LoadOldMeasurement.class);

            loadOldMeasurementIntent.putExtra(EXTRA_MESSAGE_INTENSITY_LEVEL, selectedLightIntensityItem.getLightIntensity());
            loadOldMeasurementIntent.putExtra(EXTRA_MESSAGE_TIMESTAMP, selectedLightIntensityItem.getTimeStamp());

            startActivity(loadOldMeasurementIntent);
        }
    }
}
