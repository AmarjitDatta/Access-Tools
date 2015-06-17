package accessible.com.accesslight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MeasureLightActivity extends Activity {
    public final static String EXTRA_MESSAGE = "access.light.measureLightActivity.MESSAGE";

    ProgressBar lightMeter;
    float counter;
    Button read;
    TextView display;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_light);

        measureLightIntensity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void measureLightIntensity() {
        counter = 0;
        read = (Button) findViewById(R.id.bStart);
        display = (TextView) findViewById(R.id.tvDisplay);

        lightMeter = (ProgressBar) findViewById(R.id.lightmeter);

        SensorManager sensorManager
                = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor
                = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(MeasureLightActivity.this,
                    "No Light Sensor! quit-",
                    Toast.LENGTH_LONG).show();
        } else {
            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    SensorEventListener lightSensorEventListener
            = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                final float currentReading = event.values[0];
                lightMeter.setProgress((int) currentReading);
                read.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    showMeasuredResult(String.valueOf(currentReading));
                    }
                });
            }
        }
    };

    private void showMeasuredResult(String lightValue) {
        Intent showSoundMeasurementResultIntent = new Intent(this, ShowLightMeasurementResult.class);
        showSoundMeasurementResultIntent.putExtra(EXTRA_MESSAGE, lightValue);
        startActivity(showSoundMeasurementResultIntent);
    }
}
