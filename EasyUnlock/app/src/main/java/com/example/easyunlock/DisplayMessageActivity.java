package com.example.easyunlock;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class DisplayMessageActivity extends AppCompatActivity {
    ArrayList<Point> gyrData;
    ArrayList<Point> accData;
    private final String TAG = "displayMessage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        accData = intent.getParcelableArrayListExtra(DataCollectionActivity.EXTRA_ACC_DATA);
        gyrData = intent.getParcelableArrayListExtra(DataCollectionActivity.EXTRA_GYR_DATA);
        TextView textView = findViewById(R.id.textView);
        textView.setText("DTW Distance: "+Float.toString(getDTWTotal()));
    }

    public float getDTWTotal(){
        //Extract data from files
        //compare it to incoming signal
        //average the results
        float ret = 0;
        for (int i = 1; i < 10; i++) {
            String fileName = "ForTheApp/Patrick" + Integer.toString(i) + ".txt";
            ArrayList<Point> fileACC = getFileData(fileName,"ACC:");
            ArrayList<Point> fileGYR = getFileData(fileName,"GY:");
            ret += DTWA.DTWDistance(accData,fileACC);
            ret += DTWA.DTWDistance(gyrData,fileGYR);
        }
        return ret/10;
    }
    //Pulls the pickup data from the files in ForTheApp in the resources folder
    public ArrayList<Point> getFileData(String infile, String matcher){
        String line;
        ArrayList<Point> fileData = new ArrayList<>();
        try{
            InputStream in = getAssets().open(infile);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while ((line = br.readLine()) != null){
                line.replaceAll("\\s+","");
                String[] stuff = line.split("\\s+");
                if (stuff[0].equals(matcher)){
                    try {

                        fileData.add(new Point(Float.parseFloat(stuff[1]),
                                Float.parseFloat(stuff[2]),
                                Float.parseFloat(stuff[3])));
                    }
                    catch(NumberFormatException ex){
                        Log.w(TAG,ex.toString());
                    }
                }
            }
        }
        catch(FileNotFoundException ex){
            Log.w(TAG, ex.toString());
        }
        catch(IOException ex) {
            Log.w(TAG, ex.toString());
        }
        return fileData;
    }
}
