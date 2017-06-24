package com.demo.travelsociety.db.bean;

/**
 *  赛选实体类
 */

public class OrgBean {
    private int _id;
    private String name;
    private String code;
    private String OptType ;

    public OrgBean(int _id, String name, String code, String OptType)
    {
        this._id = _id;
        this.name = name;
        this.code=code;
        this.OptType=OptType;
    }

    public OrgBean() {
        // TODO Auto-generated constructor stub
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOptType() {
        return OptType;
    }

    public void setOptType(String optType) {
        OptType = optType;
    }

    @Override
    public String toString() {
        return "OrgBean{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", OptType='" + OptType + '\'' +
                '}';
    }
}
