package com.example.ecocar;

import android.os.Parcel;
import android.os.Parcelable;

public class MultiDate implements Parcelable {
    private String type, iniTime, endTime;

    public MultiDate () {}

    public MultiDate(String type, String iniTime, String endTime) {
        this.type = type;
        this.iniTime = iniTime;
        this.endTime = endTime;
    }

    protected MultiDate(Parcel in) {
        type = in.readString();
        iniTime = in.readString();
        endTime = in.readString();
    }

    public static final Creator<MultiDate> CREATOR = new Creator<MultiDate>() {
        @Override
        public MultiDate createFromParcel(Parcel in) {
            return new MultiDate(in);
        }

        @Override
        public MultiDate[] newArray(int size) {
            return new MultiDate[size];
        }
    };

    public String getType() {
        return type;
    }

    public String getIniTime() {
        return iniTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIniTime(String iniTime) {
        this.iniTime = iniTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(iniTime);
        parcel.writeString(endTime);
    }
}
