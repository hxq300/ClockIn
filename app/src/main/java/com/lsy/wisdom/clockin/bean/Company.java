package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/5/29
 * todo : 客户公司
 */
public class Company {

//    "id":1,
//            "conglomerate_id":1,
//            "items_name":"安全体验馆",
//            "bloc_name":"中铁十二局",
//            "type":"合作",
//            "uptime":"2020-05-25 16:34:09",
//            "uptimeC":1590395649335

//            "staff_id": 4,
//            "staff_name": "张长城",

    private int id;
    private int staff_id;
    private int conglomerate_id;
    private String items_name;
    private String bloc_name;
    private String type;
    private String uptime;
    private String uptimeC;
    private String staff_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConglomerate_id() {
        return conglomerate_id;
    }

    public void setConglomerate_id(int conglomerate_id) {
        this.conglomerate_id = conglomerate_id;
    }

    public String getItems_name() {
        return items_name;
    }

    public void setItems_name(String items_name) {
        this.items_name = items_name;
    }

    public String getBloc_name() {
        return bloc_name;
    }

    public void setBloc_name(String bloc_name) {
        this.bloc_name = bloc_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getUptimeC() {
        return uptimeC;
    }

    public void setUptimeC(String uptimeC) {
        this.uptimeC = uptimeC;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", staff_id=" + staff_id +
                ", conglomerate_id=" + conglomerate_id +
                ", items_name='" + items_name + '\'' +
                ", bloc_name='" + bloc_name + '\'' +
                ", type='" + type + '\'' +
                ", uptime='" + uptime + '\'' +
                ", uptimeC='" + uptimeC + '\'' +
                ", staff_name='" + staff_name + '\'' +
                '}';
    }
}
