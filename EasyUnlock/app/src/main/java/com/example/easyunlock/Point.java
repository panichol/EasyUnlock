package com.example.easyunlock;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable{
    float x;
    float y;
    float z;

    public Point(){
        x = 0;
        y = 0;
        z = 0;
    }

    public Point(float xx, float yy, float zz){
        x = xx;
        y = yy;
        z = zz;
    }



    public double getDistance(Point other){
        float xDist = x-other.x;
        float yDist = y-other.y;
        float zDist = z-other.z;
        return Math.sqrt(xDist*xDist+yDist*yDist+zDist*zDist);
    }

    public String toString(){
        return "(" + Float.toString(x) + ", " +Float.toString(y) + ", " + Float.toString(z)+ ")";
    }


    //All the stuff beneath here is so this class successfully implements Parcelable
    // Implementing Parcelable is needed to package Arraylists of points into an intent
    public Point(Parcel in){
        float[] data = new float[3];

        in.readFloatArray(data);
        this.x = data[0];
        this.y = data[1];
        this.z = data[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloatArray(new float[] {this.x, this.y, this.z});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        public Point[] newArray(int size) {
            return new Point[size];
        }
    };
}
