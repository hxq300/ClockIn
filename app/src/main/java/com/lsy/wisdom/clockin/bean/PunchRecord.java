package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/5/21
 * todo : 打卡记录
 */
public class PunchRecord {
//    "id":27,
//            "company_id":1,
//            "staff_id":1,
//            "time":"2020-05-21 11:49:01",
//            "timeC":1590032941000,
//            "address":"在金赢108金座附近",
//            "longitude":121,
//            "latitude":31,
//            "remark":"JS您lol哦咯哦了了咯⊙∀⊙！类",
//            "conglomerate_id":1
//            "url":"[reimburse/images/20200629155111]"

    private int id;
    private int company_id;
    private int staff_id;
    private String time;
    private long timeC;
    private String address;
    private double longitude;
    private double latitude;
    private String remark;
    private int conglomerate_id;
    private String staff_name;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimeC() {
        return timeC;
    }

    public void setTimeC(long timeC) {
        this.timeC = timeC;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getConglomerate_id() {
        return conglomerate_id;
    }

    public void setConglomerate_id(int conglomerate_id) {
        this.conglomerate_id = conglomerate_id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
