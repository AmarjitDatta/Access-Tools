package accessible.com.accessslope;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import accessible.com.accessslope.utils.Constants;
import accessible.com.accessslope.utils.Slope;

public class ShowMeasurementResult extends Activity {
    Intent mMeasureSlopeActivityIntent;
    private static final String PREFS = "slopePref";
    private static final String PREF_NAME = "slopeLogList";
    private SharedPreferences mSharedPreferences;
    Slope mSlope;
    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_measurement_result);
        mMeasureSlopeActivityIntent = getIntent();
        showResultInTextField();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_slope_measurement_result, menu);
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

    public void showResultInTextField() {
        Bundle bundle = mMeasureSlopeActivityIntent.getExtras();
        if (mSlope != null) {
            mSlope = null;
        }
        double azimuth = bundle.getDouble(Constants.EXTRA_MESSAGE_AZIMUTH);
        double pitch = bundle.getDouble(Constants.EXTRA_MESSAGE_PITCH);
        double roll = bundle.getDouble(Constants.EXTRA_MESSAGE_ROLL);

        mSlope = new Slope(azimuth, pitch, roll);
        TextView resultAzimuthTextField = (TextView) findViewById(R.id.resultAzimuthTextField);
        TextView resultPitchTextField = (TextView) findViewById(R.id.resultPitchTextField);
        TextView resultRollTextField = (TextView) findViewById(R.id.resultRollTextField);
        resultAzimuthTextField.setText("Azimuth: " + mSlope.getAzimuth() + " Degree");
        resultPitchTextField.setText("Pitch: " + mSlope.getPitch() + " Degree");
        resultRollTextField.setText("Roll: " + mSlope.getRoll() + " Degree");
    }

    public void openParentActivity(View view) {
        Intent measureSlopeActivityIntent = new Intent(this, MeasureSlopeActivity.class);
        startActivity(measureSlopeActivityIntent);
    }

    public void saveResult(View view) {
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mSharedPreferences.getString(PREF_NAME, "");
        Type collectionType = new TypeToken<List<Slope>>(){}.getType();
        List<Slope> obj = gson.fromJson(json, collectionType);

        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();

        /*First check if the List array is null. If null, need to initialize a new list
        * Else, we will add in the existing list*/
        if (obj == null) {
            List<Slope> noiseList = new ArrayList<>();
            noiseList.add(mSlope);
            String jsonEntry = gson.toJson(noiseList);
            prefsEditor.putString(PREF_NAME, jsonEntry);
        }
        else {
            obj.add(mSlope);
            String jsonEntry = gson.toJson(obj);
            prefsEditor.putString(PREF_NAME, jsonEntry);
        }
        prefsEditor.apply();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Success!");
        alert.setMessage("Slope log successfully saved");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();

        /*Disable save button after successfully saving data*/
        view.setEnabled(false);

        Log.d("Save Result", "Finished Saving the Slope Object");
    }

    public void shareResultsAction(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Access Slope");
        String shareMessage = String.format("Slope level of the plain is %s at %s time",mSlope.getAzimuth(),mSlope.getTimeStamp());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(sharingIntent, "Share"));
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Access Slope");
            if (mSlope != null) {
                String shareMessage = String.format("Slope level of the plain is %s at %s time",mSlope.getAzimuth(),mSlope.getTimeStamp());
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            }

            // Make sure the provider knows
            // it should work with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
