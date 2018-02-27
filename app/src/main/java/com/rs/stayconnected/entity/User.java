package com.rs.stayconnected.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static com.rs.stayconnected.entity.User.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "user";
    @ColumnInfo(name = "name")
    @NonNull
    private String name;
    @PrimaryKey
    @ColumnInfo(name = "emailId")
    @NonNull
    private String emailId;

    @ColumnInfo(name = "phoneNo")
    private String phoneNo;

    @ColumnInfo(name = "facebookUrl")
    private String facebookUrl;

    @ColumnInfo(name = "linkedinUrl")
    private String linkedinUrl;


    @ColumnInfo(name = "twitterUrl")
    private String twitterUrl;


    @ColumnInfo(name = "userType")
    private boolean userType;


    @ColumnInfo(name = "photoURL")
    private String photoURL;

    public User(@NonNull String name, @NonNull String emailId, String phoneNo, String facebookUrl,
                String linkedinUrl, String twitterUrl, boolean userType, String photoURL) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.facebookUrl = facebookUrl;
        this.emailId = emailId;
        this.linkedinUrl = linkedinUrl;
        this.twitterUrl = twitterUrl;
        this.userType = userType;
        this.photoURL = photoURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public boolean isUserType() {
        return userType;
    }

    public void setUserType(boolean userType) {
        this.userType = userType;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }


    @Override
    public String toString() {
        return "{ name:" + name + "," +
                "phoneNo:" + phoneNo + "," +
                "emailId:" + emailId + "," +
                "facebookUrl:" + facebookUrl + "," +
                "twitterUrl:" + twitterUrl + "," +
                "linkedInUrl:" + linkedinUrl + "," +
                "photoURL:" + photoURL + "}";
    }

}
