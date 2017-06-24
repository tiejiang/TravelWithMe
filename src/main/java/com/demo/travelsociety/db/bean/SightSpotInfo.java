package com.demo.travelsociety.db.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 景点实体类
 */

public class SightSpotInfo implements Parcelable{
    private double latitude;//纬度
    private double longitude;//经度
    private String name;//名字
    private String desc;//描述
    private boolean isMyWant; //是否是我想去
    private int id;
    private int imageId; // 图片id
    private int position; // 下标

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isMyWant() {
        return isMyWant;
    }

    public void setMyWant(boolean myWant) {
        isMyWant = myWant;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public SightSpotInfo() {
    }

    public SightSpotInfo(Parcel in){
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in){
        latitude = in.readDouble();
        longitude = in.readDouble();
        name = in.readString();
        desc = in.readString();
        isMyWant = in.readByte()!=0;
        id = in.readInt();
        imageId = in.readInt();
        position = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeByte((byte)(isMyWant ? 1 : 0));
        dest.writeInt(id);
        dest.writeInt(imageId);
        dest.writeInt(position);
    }

    public static final Creator<SightSpotInfo> CREATOR = new Creator<SightSpotInfo>() {
        @Override
        public SightSpotInfo createFromParcel(Parcel in) {
            return new SightSpotInfo(in);
        }

        @Override
        public SightSpotInfo[] newArray(int size) {
            return new SightSpotInfo[size];
        }
    };

    @Override
    public String toString() {
        return "SightSpotInfo{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", isMyWant='" + isMyWant + '\'' +
                ", id='" + id + '\'' +
                ", imageId='" + imageId + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
