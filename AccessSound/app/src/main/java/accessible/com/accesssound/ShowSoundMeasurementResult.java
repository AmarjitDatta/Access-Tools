package accessible.com.accesssound;

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

import accessible.com.accesssound.utils.Noise;

public class ShowSoundMeasurementResult extends Activity {
    Intent mMeasureSoundActivityIntent;
    private static final String PREFS = "soundPref";
    private static final String PREF_NAME = "soundLogList";
    private SharedPreferences mSharedPreferences;
    Noise mNoise;
    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sound_measurement_result);
        mMeasureSoundActivityIntent = getIntent();
        showResultInTextField();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_sound_measurement_result, menu);
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
            Intent measureSoundActivityIntent = new Intent(this, MeasureSoundActivity.class);
            startActivity(measureSoundActivityIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showResultInTextField() {
        Bundle bundle = mMeasureSoundActivityIntent.getExtras();
        if (mNoise != null) {
            mNoise = null;
        }
        mNoise = new Noise(bundle.getDouble(MeasureSoundActivity.EXTRA_MESSAGE));
        TextView resultSoundTextField = (TextView) findViewById(R.id.resultSoundTextField);
        resultSoundTextField.setText("Noise Level: " + mNoise.getRoundedNoise() + "db");
    }

    public void openParentActivity(View view) {
        Intent measureSoundActivityIntent = new Intent(this, MeasureSoundActivity.class);
        startActivity(measureSoundActivityIntent);
    }

    public void saveResult(View view) {
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mSharedPreferences.getString(PREF_NAME, "");
        Type collectionType = new TypeToken<List<Noise>>(){}.getType();
        List<Noise> obj = gson.fromJson(json, collectionType);

        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();

        /*First check if the List array is null. If null, need to initialize a new list
        * Else, we will add in the existing list*/
        if (obj == null) {
            List<Noise> noiseList = new ArrayList<>();
            noiseList.add(mNoise);
            String jsonEntry = gson.toJson(noiseList);
            prefsEditor.putString(PREF_NAME, jsonEntry);
        }
        else {
            obj.add(mNoise);
            String jsonEntry = gson.toJson(obj);
            prefsEditor.putString(PREF_NAME, jsonEntry);
        }
        prefsEditor.apply();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Success!");
        alert.setMessage("Noise log successfully saved");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();

        /*Disable save button after successfully saving data*/
        view.setEnabled(false);

        Log.d("Save Result", "Finished Saving the Noise Object");
    }

    public void shareResultsAction(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Accessibility Sound");
        String shareMessage = String.format("Sound level of the room is %sdb at %s time",mNoise.getRoundedNoise(),mNoise.getTimeStamp());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(sharingIntent, "Share"));
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Accessibility Sound");
            if (mNoise != null) {
                String shareMessage = String.format("Sound level of the room is %sdb at %s time",mNoise.getRoundedNoise(),mNoise.getTimeStamp());
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            }

            // Make sure the provider knows
            // it should work with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
