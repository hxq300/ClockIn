package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/5/31
 * todo :
 */
public class ClientData {
//    "id":1,
//            "items_id":3,
//            "client_name":"阿斯顿",
//            "client_sex":"",
//            "client_position":"",
//            "client_department":"",
//            "client_phone":

    private int id;
    private int items_id;
    private String client_name;
    private String client_sex;
    private String client_position;
    private String client_department;
    private String client_phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItems_id() {
        return items_id;
    }

    public void setItems_id(int items_id) {
        this.items_id = items_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_sex() {
        return client_sex;
    }

    public void setClient_sex(String client_sex) {
        this.client_sex = client_sex;
    }

    public String getClient_position() {
        return client_position;
    }

    public void setClient_position(String client_position) {
        this.client_position = client_position;
    }

    public String getClient_department() {
        return client_department;
    }

    public void setClient_department(String client_department) {
        this.client_department = client_department;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }
}
