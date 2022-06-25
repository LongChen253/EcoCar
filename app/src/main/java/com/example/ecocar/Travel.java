package com.example.ecocar;

import android.os.Parcel;
import android.os.Parcelable;

public class Travel implements Parcelable {
    private String travelID, driver, address_ori, address_des, city_ori, city_des, ini_date, ini_time, end_date, end_time, travelCar;
    private double latitude_ori, longitude_ori, latitude_des, longitude_des, distance;
    private float distanceORI, distanceDES;
    private int numPassangers, price, duration;
    private boolean pet, luggage, smoke, face_mask, music, talk, finished;

    protected Travel(Parcel in) {
        travelID = in.readString();
        driver = in.readString();
        address_ori = in.readString();
        address_des = in.readString();
        city_ori = in.readString();
        city_des = in.readString();
        ini_date = in.readString();
        ini_time = in.readString();
        end_date = in.readString();
        end_time = in.readString();
        travelCar = in.readString();
        distance = in.readDouble();
        duration = in.readInt();
        price = in.readInt();
        latitude_ori = in.readDouble();
        longitude_ori = in.readDouble();
        latitude_des = in.readDouble();
        longitude_des = in.readDouble();
        distanceORI = in.readFloat();
        distanceDES = in.readFloat();
        numPassangers = in.readInt();
        pet = in.readByte() != 0;
        luggage = in.readByte() != 0;
        smoke = in.readByte() != 0;
        face_mask = in.readByte() != 0;
        music = in.readByte() != 0;
        talk = in.readByte() != 0;
        finished = in.readByte() != 0;
    }

    public static final Creator<Travel> CREATOR = new Creator<Travel>() {
        @Override
        public Travel createFromParcel(Parcel in) {
            return new Travel(in);
        }

        @Override
        public Travel[] newArray(int size) {
            return new Travel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(travelID);
        parcel.writeString(driver);
        parcel.writeString(address_ori);
        parcel.writeString(address_des);
        parcel.writeString(city_ori);
        parcel.writeString(city_des);
        parcel.writeString(ini_date);
        parcel.writeString(ini_time);
        parcel.writeString(end_date);
        parcel.writeString(end_time);
        parcel.writeString(travelCar);
        parcel.writeDouble(distance);
        parcel.writeInt(duration);
        parcel.writeInt(price);
        parcel.writeDouble(latitude_ori);
        parcel.writeDouble(longitude_ori);
        parcel.writeDouble(latitude_des);
        parcel.writeDouble(longitude_des);
        parcel.writeFloat(distanceORI);
        parcel.writeFloat(distanceDES);
        parcel.writeInt(numPassangers);
        parcel.writeByte((byte) (pet ? 1 : 0));
        parcel.writeByte((byte) (luggage ? 1 : 0));
        parcel.writeByte((byte) (smoke ? 1 : 0));
        parcel.writeByte((byte) (face_mask ? 1 : 0));
        parcel.writeByte((byte) (music ? 1 : 0));
        parcel.writeByte((byte) (talk ? 1 : 0));
        parcel.writeByte((byte) (finished ? 1 : 0));
    }

    public Travel () {

    }

    public Travel(String travelID, String driver, String address_ori, String address_des, String city_ori, String city_des, String ini_date, String ini_time, String end_date, String end_time, String travelCar, double latitude_ori, double longitude_ori, double latitude_des, double longitude_des, double distance, float distanceORI, float distanceDES, int numPassangers, int price, int duration, boolean pet, boolean luggage, boolean smoke, boolean face_mask, boolean music, boolean talk, boolean finished) {
        this.travelID = travelID;
        this.driver = driver;
        this.address_ori = address_ori;
        this.address_des = address_des;
        this.city_ori = city_ori;
        this.city_des = city_des;
        this.ini_date = ini_date;
        this.ini_time = ini_time;
        this.end_date = end_date;
        this.end_time = end_time;
        this.travelCar = travelCar;
        this.latitude_ori = latitude_ori;
        this.longitude_ori = longitude_ori;
        this.latitude_des = latitude_des;
        this.longitude_des = longitude_des;
        this.distance = distance;
        this.distanceORI = distanceORI;
        this.distanceDES = distanceDES;
        this.numPassangers = numPassangers;
        this.price = price;
        this.duration = duration;
        this.pet = pet;
        this.luggage = luggage;
        this.smoke = smoke;
        this.face_mask = face_mask;
        this.music = music;
        this.talk = talk;
        this.finished = finished;
    }

    public void setTravelID(String travelID) {
        this.travelID = travelID;
    }

    public void setDistanceORI(float distanceORI) {
        this.distanceORI = distanceORI;
    }

    public void setDistanceDES(float distanceDES) {
        this.distanceDES = distanceDES;
    }

    public String getTravelID() {
        return travelID;
    }

    public String getDriver() {
        return driver;
    }

    public String getAddress_ori() {
        return address_ori;
    }

    public String getAddress_des() {
        return address_des;
    }

    public String getCity_ori() {
        return city_ori;
    }

    public String getCity_des() {
        return city_des;
    }

    public String getIni_date() {
        return ini_date;
    }

    public String getIni_time() {
        return ini_time;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getTravelCar() {
        return travelCar;
    }

    public double getLatitude_ori() {
        return latitude_ori;
    }

    public double getLongitude_ori() {
        return longitude_ori;
    }

    public double getLatitude_des() {
        return latitude_des;
    }

    public double getLongitude_des() {
        return longitude_des;
    }

    public double getDistance() {
        return distance;
    }

    public float getDistanceORI() {
        return distanceORI;
    }

    public float getDistanceDES() {
        return distanceDES;
    }

    public int getNumPassangers() {
        return numPassangers;
    }

    public int getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isPet() {
        return pet;
    }

    public boolean isLuggage() {
        return luggage;
    }

    public boolean isSmoke() {
        return smoke;
    }

    public boolean isFace_mask() {
        return face_mask;
    }

    public boolean isMusic() {
        return music;
    }

    public boolean isTalk() {
        return talk;
    }

    public boolean isFinished() {
        return finished;
    }
}
