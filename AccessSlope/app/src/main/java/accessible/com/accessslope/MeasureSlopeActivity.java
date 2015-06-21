package accessible.com.accessslope;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import accessible.com.accessslope.utils.Constants;

public class MeasureSlopeActivity extends Activity {
    private TextView mAzimuth;
    private TextView mPitch;
    private TextView mRoll;

    private float mAngle0_azimuth = 0;
    private float mAngle1_pitch = 0;
    private float mAngle2_roll = 0;
    private float mAngle0_filtered_azimuth = 0;
    private float mAngle1_filtered_pitch = 0;
    private float mAngle2_filtered_roll = 0;

    private SensorManager sensorManager;
    //sensor calculation values
    float[] mGravity = null;
    float[] mGeomagnetic = null;
    float Rmat[] = new float[9];
    float Imat[] = new float[9];
    float orientation[] = new float[3];
    SensorEventListener mAccelerometerListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity = event.values.clone();
                processSensorData();
            }
        }
    };
    SensorEventListener mMagnetometerListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic = event.values.clone();
                processSensorData();
                update();
            }
        }
    };

    private float restrictAngle(float tmpAngle) {
        while (tmpAngle >= 180) tmpAngle -= 360;
        while (tmpAngle < -180) tmpAngle += 360;
        return tmpAngle;
    }

    //x is a raw angle value from getOrientation(...)
    //y is the current filtered angle value
    private float calculateFilteredAngle(float x, float y) {
        final float alpha = 0.3f;
        float diff = x - y;

        //here, we ensure that abs(diff)<=180
        diff = restrictAngle(diff);

        y += alpha * diff;
        //ensure that y stays within [-180, 180[ bounds
        y = restrictAngle(y);

        return y;
    }


    public void processSensorData() {
        if (mGravity != null && mGeomagnetic != null) {
            boolean success = SensorManager.getRotationMatrix(Rmat, Imat, mGravity, mGeomagnetic);
            if (success) {
                SensorManager.getOrientation(Rmat, orientation);
                mAngle0_azimuth = (float) Math.toDegrees((double) orientation[0]); // orientation contains: azimut, pitch and roll
                mAngle1_pitch = (float) Math.toDegrees((double) orientation[1]); //pitch
                mAngle2_roll = -(float) Math.toDegrees((double) orientation[2]); //roll
                mAngle0_filtered_azimuth = calculateFilteredAngle(mAngle0_azimuth, mAngle0_filtered_azimuth);
                mAngle1_filtered_pitch = calculateFilteredAngle(mAngle1_pitch, mAngle1_filtered_pitch);
                mAngle2_filtered_roll = calculateFilteredAngle(mAngle2_roll, mAngle2_filtered_roll);
            }
            mGravity = null; //oblige full new refresh
            mGeomagnetic = null; //oblige full new refresh
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_slope);

        mAzimuth = (TextView) findViewById(R.id.azimuth);
        mPitch = (TextView) findViewById(R.id.pitch);
        mRoll = (TextView) findViewById(R.id.roll);

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (accelerometerSensor == null || magneticFieldSensor == null) {
            if (accelerometerSensor == null) {
                Toast.makeText(MeasureSlopeActivity.this,
                        "No Accelerometer Sensor! quit-",
                        Toast.LENGTH_LONG).show();
            }
            if (magneticFieldSensor == null) {
                Toast.makeText(MeasureSlopeActivity.this,
                        "No Magnetic Field Sensor! quit-",
                        Toast.LENGTH_LONG).show();
            }
            findViewById(R.id.measureSlopeLevel).setEnabled(false);
        }
        else {
            sensorManager.registerListener(mAccelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(mMagnetometerListener, magneticFieldSensor, SensorManager.SENSOR_DELAY_UI);
            update();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        sensorManager.unregisterListener(mAccelerometerListener);
        sensorManager.unregisterListener(mMagnetometerListener);
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
        } else if (id == R.id.showSavedLogsMenuBtn) {
            Intent listSavedMeasurementsActivityIntent = new Intent(this, ListSavedMeasurementsActivity.class);
            startActivity(listSavedMeasurementsActivityIntent);
            return true;
        } else if (id == R.id.measureSoundMenuBtn) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void measureSlope(View view) {
        Log.d("Measuring slope","Measuring slope information for the device");
        Intent showMeasurementResultIntent = new Intent(this, ShowMeasurementResult.class);
        Bundle bundle = new Bundle();

        try {
            double azimuth = Double.valueOf(String.valueOf(mAngle0_azimuth));
            double pitch = Double.valueOf(String.valueOf(mAngle1_pitch));
            double roll = Double.valueOf(String.valueOf(mAngle2_roll));
            bundle.putDouble(Constants.EXTRA_MESSAGE_AZIMUTH, azimuth);
            bundle.putDouble(Constants.EXTRA_MESSAGE_PITCH, pitch);
            bundle.putDouble(Constants.EXTRA_MESSAGE_ROLL, roll);
        }
        catch (Exception ex) {
            bundle.putDouble(Constants.EXTRA_MESSAGE_AZIMUTH, 0.0d);
            bundle.putDouble(Constants.EXTRA_MESSAGE_PITCH, 0.0d);
            bundle.putDouble(Constants.EXTRA_MESSAGE_ROLL, 0.0d);
        }

        showMeasurementResultIntent.putExtras(bundle);
        startActivity(showMeasurementResultIntent);
    }

    private void update() {
        mAzimuth.setText("Azimuth: " + String.valueOf(mAngle0_azimuth));
        mPitch.setText("Pitch: " + String.valueOf(mAngle1_pitch));
        mRoll.setText("Roll: " + String.valueOf(mAngle2_roll));
    }
}
