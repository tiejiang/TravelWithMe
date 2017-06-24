package com.demo.travelsociety.db.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 动态实体类
 */

public class DynamicsItem implements Parcelable{
    private String name; //名称
    private String icon; //头像
    private String date; //日期
    private String desc; //描述
    private List<String> pictureList; //相片组
    private String address; //地址
    private int commentCount; //评论数
    private int praiseCount; //点赞数

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public DynamicsItem() {
    }

    public DynamicsItem(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in){
        name = in.readString();
        icon = in.readString();
        date = in.readString();
        desc = in.readString();
        in.readStringList(pictureList);
        address = in.readString();
        commentCount = in.readInt();
        praiseCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(icon);
        parcel.writeString(date);
        parcel.writeString(desc);
        parcel.writeStringList(pictureList);
        parcel.writeString(address);
        parcel.writeInt(commentCount);
        parcel.writeInt(praiseCount);
    }

    public static final Creator<DynamicsItem> CREATOR = new Creator<DynamicsItem>() {
        @Override
        public DynamicsItem createFromParcel(Parcel in) {
            return new DynamicsItem(in);
        }

        @Override
        public DynamicsItem[] newArray(int size) {
            return new DynamicsItem[size];
        }
    };
}
