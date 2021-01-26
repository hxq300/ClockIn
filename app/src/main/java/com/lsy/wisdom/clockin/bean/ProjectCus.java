package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/7/24
 * describe :  TODO
 */
public class ProjectCus {

//    "id":20,
//            "conglomerate_id":0,
//            "staff_id":0,
//            "items_name":"安全体验馆项目",
//            "uptimeC":0

    private int id;
    private int conglomerate_id;
    private int staff_id;
    private String items_name;
    private int uptimeC;

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

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public String getItems_name() {
        return items_name;
    }

    public void setItems_name(String items_name) {
        this.items_name = items_name;
    }

    public int getUptimeC() {
        return uptimeC;
    }

    public void setUptimeC(int uptimeC) {
        this.uptimeC = uptimeC;
    }
}
