package accessible.com.accesslight;

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
    String intensityLevel;
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
        else if (id == R.id.measureLightMenuBtn) {
            Intent measureLightActivityIntent = new Intent(this, MeasureLightActivity.class);
            startActivity(measureLightActivityIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showOldResultInTextFields() {
        intensityLevel = mLoadOldMeasurementActivityIntent.getStringExtra(ListSavedMeasurementsActivity.EXTRA_MESSAGE_INTENSITY_LEVEL);
        timeStamp = mLoadOldMeasurementActivityIntent.getStringExtra(ListSavedMeasurementsActivity.EXTRA_MESSAGE_TIMESTAMP);
        Log.d("Load old measurements", "Light Intensity Level " + intensityLevel);
        Log.d("Load old measurements", "Time stamp " + timeStamp);
        TextView oldMeasurementTime = (TextView) findViewById(R.id.oldMeasurementTime);
        oldMeasurementTime.setText("Time: " + timeStamp);
        TextView oldMeasurementValue = (TextView) findViewById(R.id.oldMeasurementValue);
        oldMeasurementValue.setText("Light Intensity Level: " + intensityLevel + "Lux");
    }

    public void measureDataAgainActivity(View view) {
        Intent measureLightActivityIntent = new Intent(this, MeasureLightActivity.class);
        startActivity(measureLightActivityIntent);
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Access Light");
            String shareMessage = String.format("Light Intensity level of the room is %sLux at %s time", intensityLevel, timeStamp);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

            // Make sure the provider knows
            // it should work with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    public void shareOldResultsAction(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Access Light");
        String shareMessage = String.format("Light Intensity level of the room is %sLux at %s time", intensityLevel, timeStamp);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(sharingIntent, "Share"));
    }
}
