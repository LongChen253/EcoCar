package com.example.ecocar;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentUser implements Parcelable {
    private String uid, username, email, birthday, address, biography, photoURL, gender, preferences, carSelected;
    private Integer phone, numComments;
    private float star;

    private static CurrentUser currentUser;

    protected CurrentUser(Parcel in) {
        uid = in.readString();
        username = in.readString();
        email = in.readString();
        birthday = in.readString();
        address = in.readString();
        biography = in.readString();
        photoURL = in.readString();
        gender = in.readString();
        preferences = in.readString();
        carSelected = in.readString();
        if (in.readByte() == 0) {
            phone = null;
        } else {
            phone = in.readInt();
        }
        if (in.readByte() == 0) {
            numComments = null;
        } else {
            numComments = in.readInt();
        }
        star = in.readFloat();
    }

    public static final Creator<CurrentUser> CREATOR = new Creator<CurrentUser>() {
        @Override
        public CurrentUser createFromParcel(Parcel in) {
            return new CurrentUser(in);
        }

        @Override
        public CurrentUser[] newArray(int size) {
            return new CurrentUser[size];
        }
    };

    public static CurrentUser getInstance() {
        if (currentUser == null) currentUser = new CurrentUser();
        return currentUser;
    }

    public CurrentUser(String uid, String username, String email, String birthday, String address, String biography, String photoURL, String gender, String preferences, Integer phone, Integer numComments, float star, String carSelected) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.birthday = birthday;
        this.address = address;
        this.biography = biography;
        this.photoURL = photoURL;
        this.gender = gender;
        this.preferences = preferences;
        this.phone = phone;
        this.numComments = numComments;
        this.star = star;
        this.carSelected = carSelected;
    }

    public CurrentUser () {

    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getBiography() {
        return biography;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getGender() {
        return gender;
    }

    public String getPreferences() {
        return preferences;
    }

    public Integer getPhone() {
        return phone;
    }

    public Integer getNumComments() {
        return numComments;
    }

    public String getUid() {
        return uid;
    }

    public float getStar() {
        return star;
    }

    public String getCarSelected() {
        return carSelected;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public void setNumComments(Integer numComments) {
        this.numComments = numComments;
    }

    public void setUid(String uid) { this.uid = uid;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public void setCarSelected(String carSelected) {
        this.carSelected = carSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(birthday);
        parcel.writeString(address);
        parcel.writeString(biography);
        parcel.writeString(photoURL);
        parcel.writeString(gender);
        parcel.writeString(preferences);
        parcel.writeString(carSelected);
        if (phone == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(phone);
        }
        if (numComments == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(numComments);
        }
        parcel.writeFloat(star);
    }
}
