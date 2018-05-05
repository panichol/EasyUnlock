package com.example.easyunlock;
/*
An implementation of a dynamic time-warping algorithm used to compare signals
*/

import java.util.ArrayList;

public class DTWA {
    public static float DTWDistance(ArrayList<Point> arr1, ArrayList<Point> arr2){
        float[][] DTWMatrix = new float[arr1.size()][arr2.size()];
        for (int i = 0; i<arr1.size();i++){
            //Initializing our array values
            DTWMatrix[i][0] = 999999;
        }
        for (int i = 0; i<arr2.size();i++){
            //Initializing our array values
            DTWMatrix[0][i] = 999999;
        }
        DTWMatrix[0][0] = 0;
        for (int i = 1; i < arr1.size(); i++) {
            for (int j = 1; j < arr2.size(); j++) {
                float cost = (float) arr1.get(i).getDistance(arr2.get(j));
                DTWMatrix[i][j] = min(DTWMatrix[i-1][j],DTWMatrix[i][j-1],DTWMatrix[i-1][j-1]) + cost;
            }
        }


        return DTWMatrix[arr1.size()-1][arr2.size()-1];
    }

    private static float min(float a, float b, float c){
        return Math.min(Math.min(a,b),c);
    }
}
