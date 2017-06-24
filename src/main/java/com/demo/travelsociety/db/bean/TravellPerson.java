package com.demo.travelsociety.db.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  旅行的人
 *  实体类
 */
public class TravellPerson implements Parcelable{
    private String icon; // 头像url
    private String name; // 名字
    private String sex; // 性别
    private String age; // 年龄
    private String grade; // 评分
    private String profession; // 职业
    private String desc; // 描述

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public TravellPerson() {
    }

    public TravellPerson(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in){
        icon = in.readString();
        name = in.readString();
        sex = in.readString();
        age = in.readString();
        grade = in.readString();
        profession = in.readString();
        desc = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(icon);
        parcel.writeString(name);
        parcel.writeString(sex);
        parcel.writeString(age);
        parcel.writeString(grade);
        parcel.writeString(profession);
        parcel.writeString(desc);

    }

    public static final Creator<TravellPerson> CREATOR = new Creator<TravellPerson>() {
        @Override
        public TravellPerson createFromParcel(Parcel parcel) {
            return new TravellPerson(parcel);
        }

        @Override
        public TravellPerson[] newArray(int i) {
            return new TravellPerson[i];
        }
    };
}
