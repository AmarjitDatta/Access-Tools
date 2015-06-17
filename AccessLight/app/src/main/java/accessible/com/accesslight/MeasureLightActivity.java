package accessible.com.accesslight;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MeasureLightActivity extends ActionBarActivity {
    ProgressBar lightMeter;
    TextView textMax, textReading;
    float counter;
    Button read;
    TextView display;
/*
    public final static String EXTRA_MESSAGE = "accessible.com.accessibility.light.measureLightActivity.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_light);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_measure_light, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    public void measureLightLevel(View view) {
        Intent showLightMeasurementResultIntent = new Intent(this, ShowLightMeasurementResult.class);
        Bundle bundle = new Bundle();

        try {
            double noiseLevel = 0.0d;
            bundle.putDouble(EXTRA_MESSAGE, noiseLevel);
        }
        catch (Exception ex) {
            bundle.putDouble(EXTRA_MESSAGE, 0.0d);
        }

        showLightMeasurementResultIntent.putExtras(bundle);
        startActivity(showLightMeasurementResultIntent);
    }*/

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_light);

        counter = 0;
        read = (Button) findViewById(R.id.bStart);
        display = (TextView) findViewById(R.id.tvDisplay);

        lightMeter = (ProgressBar) findViewById(R.id.lightmeter);
        textMax = (TextView) findViewById(R.id.max);
        textReading = (TextView) findViewById(R.id.reading);

        SensorManager sensorManager
                = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor
                = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(MeasureLightActivity.this,
                    "No Light Sensor! quit-",
                    Toast.LENGTH_LONG).show();
        } else {
            float max = lightSensor.getMaximumRange();
            lightMeter.setMax((int) max);
            textMax.setText("Max Reading(Lux): " + String.valueOf(max));

            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    SensorEventListener lightSensorEventListener
            = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                final float currentReading = event.values[0];
                lightMeter.setProgress((int) currentReading);
                textReading.setText("Current Reading(Lux): " + String.valueOf(currentReading));
                read.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        display.setText("" + String.valueOf(currentReading));
                    }
                });

            }
        }

    };
}
