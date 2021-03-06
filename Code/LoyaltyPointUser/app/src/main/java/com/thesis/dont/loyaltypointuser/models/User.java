package com.thesis.dont.loyaltypointuser.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dont on 2/2/2015.
 */
public class User implements Parcelable{
    String username;
    String password;
    String fullname;
    String phone;
    String email;
    String address;
    String avatar;
    String token;
    String barcode;
    private String identity_number;

    public User(String username, String password, String fullname, String phone, String email, String address, String barcode, String avatar, String token){
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.barcode = barcode;
        this.avatar = avatar;
        this.token = token;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(fullname);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(avatar);
        dest.writeString(token);
        dest.writeString(barcode);
        dest.writeString(identity_number);
    }

    public static final Creator<User> CREATOR
            = new Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        username = in.readString();
        password = in.readString();
        fullname = in.readString();
        phone = in.readString();
        email = in.readString();
        address = in.readString();
        avatar = in.readString();
        token = in.readString();
        barcode = in.readString();
        identity_number = in.readString();
    }

    public String getIdentityNumber() {
        return identity_number;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identity_number = identityNumber;
    }
}
