package com.example.easyunlock;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/*
A class that collects the pickup signal. In future this should happen
passively in some service.
 */
public class DataCollectionActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor accSensor;
    private Sensor gyrSensor;
    public ArrayList<Point> accData = new ArrayList<>();
    public ArrayList<Point> gyrData = new ArrayList<>();
    public static final String EXTRA_ACC_DATA = "com.example.easyunlock.acc";
    public static final String EXTRA_GYR_DATA = "com.example.easyunlock.gyr";
    private static final String TAG = "datacollection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collection);
        // enabling acc and gyr sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyrSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this,accSensor,SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this,gyrSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void processData(View view){
        Intent intent  = new Intent(this, DisplayMessageActivity.class);
        Log.d(TAG,"GYRlen: " + Integer.toString(gyrData.size()));
        Log.d(TAG,"Acclen: " + Integer.toString(accData.size()));
        // packaging up our data to send it to the Display Message part
        intent.putExtra(EXTRA_ACC_DATA,accData);
        intent.putExtra(EXTRA_GYR_DATA,gyrData);
        startActivity(intent);
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "Got ACC");
            accData.add(new Point(event.values[0], event.values[1], event.values[2]));
        }
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Log.d(TAG, "Got GYR");
            gyrData.add(new Point(event.values[0], event.values[1], event.values[2]));
        }
        return;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        return;
    }

}
