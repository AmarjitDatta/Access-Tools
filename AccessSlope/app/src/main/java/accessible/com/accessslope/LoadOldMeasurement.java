package accessible.com.accessslope;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class LoadOldMeasurement extends Activity {
    Intent mLoadOldMeasurementActivityIntent;
    ShareActionProvider mShareActionProvider;
    /*Old data*/
    double slopeLevel;
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_old_measurement);
        mLoadOldMeasurementActivityIntent = getIntent();
        showOldResultInTextFields();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_load_old_measurement, menu);
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
            Intent listSavedMeasurementsActivityIntent = new Intent(this, ListSavedMeasurementsActivity.class);
            startActivity(listSavedMeasurementsActivityIntent);
            return true;
        }
        else if (id == R.id.measureSoundMenuBtn) {
            Intent measureSlopeActivityIntent = new Intent(this, MeasureSlopeActivity.class);
            startActivity(measureSlopeActivityIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showOldResultInTextFields() {
        Bundle bundle = mLoadOldMeasurementActivityIntent.getExtras();
        slopeLevel = bundle.getDouble(ListSavedMeasurementsActivity.EXTRA_MESSAGE_SOUND_LEVEL);
        timeStamp = mLoadOldMeasurementActivityIntent.getStringExtra(ListSavedMeasurementsActivity.EXTRA_MESSAGE_TIMESTAMP);
        Log.d("Load old measurements", "Sound Level " + slopeLevel);
        Log.d("Load old measurements", "Time stamp " + timeStamp);
        TextView oldMeasurementTime = (TextView) findViewById(R.id.oldMeasurementTime);
        oldMeasurementTime.setText("Time: " + timeStamp);
        TextView oldMeasurementValue = (TextView) findViewById(R.id.oldMeasurementValue);
        oldMeasurementValue.setText("Sound Level: " + slopeLevel + "db");
    }

    public void measureDataAgainActivity(View view) {
        Intent measureSlopeActivityIntent = new Intent(this, MeasureSlopeActivity.class);
        startActivity(measureSlopeActivityIntent);
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Accessibility Sound");
            String shareMessage = String.format("Sound level of the room is %sdb at %s time",slopeLevel,timeStamp);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

            // Make sure the provider knows
            // it should work with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    public void shareOldResultsAction(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Accessibility Sound");
        String shareMessage = String.format("Sound level of the room is %sdb at %s time",slopeLevel,timeStamp);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(sharingIntent, "Share"));
    }
}
