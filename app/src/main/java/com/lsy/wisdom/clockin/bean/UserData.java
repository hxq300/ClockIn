package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/3/16
 * todo : 登录时用户数据
 */
public class UserData {
    //    "id":1,
//            "staff_name":"asd",
//            "staff_age":12,
//            "staff_img":"sd",
//            "staff_sex":"男",
//            "staff_card":"340000000000000",
//            "staff_nation":"阿斯顿",
//            "staff_address":"啊实打实的",
//            "staff_phone":"17521223323",
//            "sos_name":"阿斯顿",
//            "sos_ship":"阿斯顿",
//            "sos_phone":"17521223323",
//            "entry_time":"2020-05-18",
//            "company_id":1,
//            "department_id":1,
//            "position_id":1,
//            "password":"123456",
//            "picture":"default.jpg",
//            "nickname":"啊实打实的",
//            "signature":"啊实打实大苏打实打实",
//            "state":"1",
//            "clockstatus":"1",
//            "company_name":"晋铁科技集团",
//            "department_name":"开发部",
//            "position_name":"主管"
//    "conglomerate_id":1,
//            "conglomerate_name":"晋铁智能科技集团"

    private int conglomerate_id;
    private String conglomerate_name;
    private int id;
    private String staff_name;
    private int staff_age;
    private String staff_img;
    private String staff_sex;
    private String staff_card;
    private String staff_nation;
    private String staff_address;
    private String staff_phone;
    private String sos_name;
    private String sos_phone;
    private String entry_time;
    private int company_id;
    private int department_id;
    private int position_id;
    private String password;
    private String picture;
    private String nickname;
    private String signature;
    private String state;
    private String clockstatus;
    private String company_name;
    private String department_name;
    private String position_name;

    public int getConglomerate_id() {
        return conglomerate_id;
    }

    public void setConglomerate_id(int conglomerate_id) {
        this.conglomerate_id = conglomerate_id;
    }

    public String getConglomerate_name() {
        return conglomerate_name;
    }

    public void setConglomerate_name(String conglomerate_name) {
        this.conglomerate_name = conglomerate_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public int getStaff_age() {
        return staff_age;
    }

    public void setStaff_age(int staff_age) {
        this.staff_age = staff_age;
    }

    public String getStaff_img() {
        return staff_img;
    }

    public void setStaff_img(String staff_img) {
        this.staff_img = staff_img;
    }

    public String getStaff_sex() {
        return staff_sex;
    }

    public void setStaff_sex(String staff_sex) {
        this.staff_sex = staff_sex;
    }

    public String getStaff_card() {
        return staff_card;
    }

    public void setStaff_card(String staff_card) {
        this.staff_card = staff_card;
    }

    public String getStaff_nation() {
        return staff_nation;
    }

    public void setStaff_nation(String staff_nation) {
        this.staff_nation = staff_nation;
    }

    public String getStaff_address() {
        return staff_address;
    }

    public void setStaff_address(String staff_address) {
        this.staff_address = staff_address;
    }

    public String getStaff_phone() {
        return staff_phone;
    }

    public void setStaff_phone(String staff_phone) {
        this.staff_phone = staff_phone;
    }

    public String getSos_name() {
        return sos_name;
    }

    public void setSos_name(String sos_name) {
        this.sos_name = sos_name;
    }

    public String getSos_phone() {
        return sos_phone;
    }

    public void setSos_phone(String sos_phone) {
        this.sos_phone = sos_phone;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(String entry_time) {
        this.entry_time = entry_time;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public int getPosition_id() {
        return position_id;
    }

    public void setPosition_id(int position_id) {
        this.position_id = position_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getClockstatus() {
        return clockstatus;
    }

    public void setClockstatus(String clockstatus) {
        this.clockstatus = clockstatus;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "conglomerate_id=" + conglomerate_id +
                ", conglomerate_name='" + conglomerate_name + '\'' +
                ", id=" + id +
                ", staff_name='" + staff_name + '\'' +
                ", staff_age=" + staff_age +
                ", staff_img='" + staff_img + '\'' +
                ", staff_sex='" + staff_sex + '\'' +
                ", staff_card='" + staff_card + '\'' +
                ", staff_nation='" + staff_nation + '\'' +
                ", staff_address='" + staff_address + '\'' +
                ", staff_phone='" + staff_phone + '\'' +
                ", sos_name='" + sos_name + '\'' +
                ", sos_phone='" + sos_phone + '\'' +
                ", entry_time='" + entry_time + '\'' +
                ", company_id=" + company_id +
                ", department_id=" + department_id +
                ", position_id=" + position_id +
                ", password='" + password + '\'' +
                ", picture='" + picture + '\'' +
                ", nickname='" + nickname + '\'' +
                ", signature='" + signature + '\'' +
                ", state='" + state + '\'' +
                ", clockstatus='" + clockstatus + '\'' +
                ", company_name='" + company_name + '\'' +
                ", department_name='" + department_name + '\'' +
                ", position_name='" + position_name + '\'' +
                '}';
    }
}
