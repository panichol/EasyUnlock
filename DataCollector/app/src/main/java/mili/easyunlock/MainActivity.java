package mili.easyunlock;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private final static int MY_PERMISSIONS_REQUEST = 100;
    private RadioGroup mRadioGroup;
    private Spinner mSpinner;
    private Button mRecordButton;

    private String userID;
    private String unlockType;
    private String currentTime;
    public static String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET},
                MY_PERMISSIONS_REQUEST);

        mRadioGroup = (RadioGroup) findViewById(R.id.group);
        mRadioGroup.check(R.id.default_button);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        mRecordButton = (Button) findViewById(R.id.record_button);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recordData();
//                if (mRecordButton.getText().equals("Record")) {
//                    recordData();
//                    mRecordButton.setText("Stop");
//                } else {
//                    mRecordButton.setText("Record");
//                }
            }
        });
    }

    private void recordData() {
        userID = String.valueOf(mSpinner.getSelectedItem());
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        unlockType = String.valueOf(radioButton.getText());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        currentTime = sdf.format(new Date());
        fileName = userID + unlockType + currentTime;

        Toast.makeText(this, fileName, Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, SensorActivity.class);
        startActivity(i);
    }


}
