package com.example.ecocar;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private String date, iniTime, endTime, taskName, taskDescription;
    private Boolean finished;

    public Event(String date, String iniTime, String endTime, String taskName, String taskDescription, Boolean finished) {
        this.date = date;
        this.iniTime = iniTime;
        this.endTime = endTime;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.finished = finished;
    }

    public Event() {}

    protected Event(Parcel in) {
        date = in.readString();
        iniTime = in.readString();
        endTime = in.readString();
        taskName = in.readString();
        taskDescription = in.readString();
        byte tmpFinished = in.readByte();
        finished = tmpFinished == 0 ? null : tmpFinished == 1;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getDate() {
        return date;
    }

    public String getIniTime() {
        return iniTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIniTime(String iniTime) {
        this.iniTime = iniTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(iniTime);
        parcel.writeString(endTime);
        parcel.writeString(taskName);
        parcel.writeString(taskDescription);
        parcel.writeByte((byte) (finished == null ? 0 : finished ? 1 : 2));
    }
}
