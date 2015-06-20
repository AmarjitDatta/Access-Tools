package accessible.com.accessslope;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import accessible.com.accessslope.utils.NoiseRecorder;

public class MeasureSlopeActivity extends Activity {
    private NoiseRecorder mNoiseRecorder;
    public final static String EXTRA_MESSAGE = "accessible.com.accessibility.slope.measureSlopeActivity.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_slope);
        mNoiseRecorder = new NoiseRecorder();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_measure_slope, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void measureNoise(View view) {
        Intent showMeasurementResultIntent = new Intent(this, ShowMeasurementResult.class);
        Bundle bundle = new Bundle();

        try {
            double noiseLevel = mNoiseRecorder.getNoiseLevel();
            bundle.putDouble(EXTRA_MESSAGE, noiseLevel);
        }
        catch (Exception ex) {
            bundle.putDouble(EXTRA_MESSAGE, 0.0d);
        }

        showMeasurementResultIntent.putExtras(bundle);
        startActivity(showMeasurementResultIntent);
    }
}
