package com.demo.travelsociety.db.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 相册实体类
 */
public class AlbumItem implements Parcelable{
    private String id; // 相册id
    private String name; //相册名称
    private String icon; // 相册封面图片url
    private int count; // 相册里相片的数量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public AlbumItem() {
    }

    public AlbumItem(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in){
        id = in.readString();
        name = in.readString();
        icon = in.readString();
        count = in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(icon);
        parcel.writeInt(count);
    }

    public static final Creator<AlbumItem> CREATOR = new Creator<AlbumItem>() {
        @Override
        public AlbumItem createFromParcel(Parcel in) {
            return new AlbumItem(in);
        }

        @Override
        public AlbumItem[] newArray(int size) {
            return new AlbumItem[size];
        }
    };
}
