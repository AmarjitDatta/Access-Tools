package accessible.com.accesslight;

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

import accessible.com.utils.Light;

public class ShowLightMeasurementResult extends Activity {
    Intent mMeasureLightActivityIntent;
    private static final String PREFS = "lightPref";
    private static final String PREF_NAME = "lightLogList";
    private SharedPreferences mSharedPreferences;
    Light mLight;
    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_light_measurement_result);
        mMeasureLightActivityIntent = getIntent();
        showResultInTextField();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_light_measurement_result, menu);
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

    public void showResultInTextField() {
        if (mLight != null) {
            mLight = null;
        }
        mLight = new Light(mMeasureLightActivityIntent.getStringExtra(MeasureLightActivity.EXTRA_MESSAGE));
        TextView resultIntensityTextField = (TextView) findViewById(R.id.resultIntensityTextField);
        resultIntensityTextField.setText("Light Level: " + mLight.getLightIntensity() + "Lux");
    }

    public void openParentActivity(View view) {
        Intent measureLightActivityIntent = new Intent(this, MeasureLightActivity.class);
        startActivity(measureLightActivityIntent);
    }

    public void saveResult(View view) {
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mSharedPreferences.getString(PREF_NAME, "");
        Type collectionType = new TypeToken<List<Light>>(){}.getType();
        List<Light> obj = gson.fromJson(json, collectionType);

        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();

        /*First check if the List array is null. If null, need to initialize a new list
        * Else, we will add in the existing list*/
        if (obj == null) {
            List<Light> measureIntensityList = new ArrayList<>();
            measureIntensityList.add(mLight);
            String jsonEntry = gson.toJson(measureIntensityList);
            prefsEditor.putString(PREF_NAME, jsonEntry);
        }
        else {
            obj.add(mLight);
            String jsonEntry = gson.toJson(obj);
            prefsEditor.putString(PREF_NAME, jsonEntry);
        }
        prefsEditor.apply();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Success!");
        alert.setMessage("Intensity log successfully saved");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();

        /*Disable save button after successfully saving data*/
        view.setEnabled(false);

        Log.d("Save Result", "Finished Saving the Intensity Object");
    }

    public void shareResultsAction(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Access Light");
        String shareMessage = String.format("Light intensity of the room is %sLux at %s time", mLight.getLightIntensity(), mLight.getTimeStamp());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(sharingIntent, "Share"));
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Access Sound");
            if (mLight != null) {
                String shareMessage = String.format("Light level of the room is %sLux at %s time", mLight.getLightIntensity(),mLight.getTimeStamp());
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            }

            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
