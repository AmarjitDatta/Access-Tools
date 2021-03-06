package accessible.com.accessslope;

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

import accessible.com.accessslope.utils.Constants;
import accessible.com.accessslope.utils.DataAdapter;
import accessible.com.accessslope.utils.Slope;


public class ListSavedMeasurementsActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ShareActionProvider mShareActionProvider;
    ListView mListView;
    DataAdapter mDataAdapter;

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
        // Access the Share Item defined in menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        // Access the object responsible for
        // putting together the sharing submenu
        if (shareItem != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }

        // Create an Intent to share your content
        setShareIntent();
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
        else if (id == R.id.measureSoundMenuBtn) {
            Intent MeasureSlopeActivityIntent = new Intent(this, MeasureSlopeActivity.class);
            startActivity(MeasureSlopeActivityIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Accessibility Sound");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Measure room slope level with Accessibility slope app.");

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
        Slope selectedSlopeItem = mDataAdapter.getItem(position);

        if (selectedSlopeItem != null) {
            Intent loadOldMeasurementIntent = new Intent(this, LoadOldMeasurement.class);

            /*Pass slope level*/
            Bundle bundle = new Bundle();
            bundle.putDouble(Constants.EXTRA_MESSAGE_AZIMUTH, selectedSlopeItem.getAzimuth());
            bundle.putDouble(Constants.EXTRA_MESSAGE_PITCH, selectedSlopeItem.getPitch());
            bundle.putDouble(Constants.EXTRA_MESSAGE_ROLL, selectedSlopeItem.getRoll());
            loadOldMeasurementIntent.putExtras(bundle);

            /*Pass time stamp*/
            loadOldMeasurementIntent.putExtra(Constants.EXTRA_MESSAGE_TIMESTAMP, selectedSlopeItem.getTimeStamp());

            startActivity(loadOldMeasurementIntent);
        }

    }
}
