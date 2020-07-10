package com.developer_ngapak.resepkita.ui.about;

import android.os.Parcel;
import android.os.Parcelable;

public class Developer implements Parcelable {
    private String name;
    private int photo;
    private String nim;
    private String address;
    private String hobbies;
    private String quotes;
    private String githubName;
    private String igName;

    protected Developer(Parcel in) {
        name = in.readString();
        photo = in.readInt();
        nim = in.readString();
        address = in.readString();
        hobbies = in.readString();
        quotes = in.readString();
        githubName = in.readString();
        igName = in.readString();
    }

    public Developer() {
    }

    public static final Creator<Developer> CREATOR = new Creator<Developer>() {
        @Override
        public Developer createFromParcel(Parcel in) {
            return new Developer(in);
        }

        @Override
        public Developer[] newArray(int size) {
            return new Developer[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getQuotes() {
        return quotes;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    public String getGithubName() {
        return githubName;
    }

    public void setGithubName(String githubName) {
        this.githubName = githubName;
    }

    public String getIgName() {
        return igName;
    }

    public void setIgName(String igName) {
        this.igName = igName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(photo);
        parcel.writeString(nim);
        parcel.writeString(address);
        parcel.writeString(hobbies);
        parcel.writeString(quotes);
        parcel.writeString(githubName);
        parcel.writeString(igName);
    }
}
