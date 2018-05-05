package mili.easyunlock;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;


public class SensorActivity extends Activity implements SensorEventListener {
    private final static String TAG = "SensorActivity";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyro;
    private MediaRecorder mRecorder;

    private Button mButton;
    private File mFile;
    private FileOutputStream mStream;

    private String soundFileName;


    Short[] mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mButton = (Button) findViewById(R.id.stop_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mStream.close();
                    stopRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                finish();

                MediaPlayer mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(soundFileName);
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    Log.e(TAG, "play prepare() failed");
                }
            }
        });



        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);

        Log.i(TAG, path.toString());
        try {
            mFile = new File(path, MainActivity.fileName + ".txt");
            mStream = new FileOutputStream(mFile);
            mStream.write(TAG.getBytes());
        } catch (Exception ex) {
            Log.i(TAG, "Creating File Failed.");
        }


        try {
            URL url = new URL("10.10.1.1/AndroidData/test.txt");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write("It Worked!".getBytes(Charset.forName("UTF-8")));
        }catch(Exception e) {
            Toast toast = Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.LEFT,0,0);
        }

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        soundFileName = path + "/" + MainActivity.fileName + ".3gp";
        Log.i(TAG, soundFileName);
        mRecorder.setOutputFile(soundFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


        startRecording();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String msg = "";

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            msg ="ACC: x=" + event.values[0] + " y="
                    + event.values[1] + " z=" + event.values[2] + "\n";
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            msg ="GY: rotation around x axis=" + event.values[0]
                    + " around y axis=" + event.values[1] + " around z axis="
                    + event.values[2]+ "\n";
        }
//        Log.i(TAG,msg);
        try {
            mStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startRecording() {

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}

