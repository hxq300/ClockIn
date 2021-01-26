package com.lsy.wisdom.clockin.bean;

import java.util.List;

/**
 * Created by lsy on 2020/5/8
 * todo : 记录通用实体
 */
public class RecordData {

//        "itmes_id":0,
//        "":"2020-05-25 16:34:23",
//        "":"2020-05-25 16:34:09",
//        "":1590395649335,
//        "":"2020-05-31 23:52:26",
//        "":1590940346000,
//        "":"内蒙古自治区 乌兰察布市 商都县 提了提哦无聊猛龙",
//    "":"2020-07-26",
//            "":"2020-05-30",

//            "procurement_type":"活动用品",
//            "procurement_sum":999996


//            "entry_time":"2020-01-11 16:41:00",
//            "entry_timeC":1578732060000,
//            "promotion_time":"2020-04-30 16:41:04",
//            "promotion_timeC":1588236064000,
//            "operating_post":"开发"

    private int id;
    private int staff_id;
    private int company_id;
    private String examine_type;
    private String start_time;
    private String end_time;
    private String content;
    private String uptime;
    private String expenses_type;
    private String expenses_sum;
    private String expenses_picture;
    private String state;
    private int conglomerate_id;
    private String company_name;
    private String start_timeC;
    private String end_timeC;
    private String staff_name;
    private int itmes_id;
    private String outtime;
    private long outtimeC;
    private long intimeC;
    private String intime;
    private String outaddress;
    private String entry_time;
    private String promotion_time;
    private String operating_post;
    private String procurement_img;
    private String project_name;

    private String procurement_type;
    private double procurement_sum;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getItmes_id() {
        return itmes_id;
    }

    public void setItmes_id(int itmes_id) {
        this.itmes_id = itmes_id;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public long getOuttimeC() {
        return outtimeC;
    }

    public void setOuttimeC(long outtimeC) {
        this.outtimeC = outtimeC;
    }

    public long getIntimeC() {
        return intimeC;
    }

    public void setIntimeC(long intimeC) {
        this.intimeC = intimeC;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOutaddress() {
        return outaddress;
    }

    public void setOutaddress(String outaddress) {
        this.outaddress = outaddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getExamine_type() {
        return examine_type;
    }

    public void setExamine_type(String examine_type) {
        this.examine_type = examine_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getExpenses_type() {
        return expenses_type;
    }

    public void setExpenses_type(String expenses_type) {
        this.expenses_type = expenses_type;
    }

    public String getExpenses_sum() {
        return expenses_sum;
    }

    public void setExpenses_sum(String expenses_sum) {
        this.expenses_sum = expenses_sum;
    }

    public String getExpenses_picture() {
        return expenses_picture;
    }

    public void setExpenses_picture(String expenses_picture) {
        this.expenses_picture = expenses_picture;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getConglomerate_id() {
        return conglomerate_id;
    }

    public void setConglomerate_id(int conglomerate_id) {
        this.conglomerate_id = conglomerate_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getStart_timeC() {
        return start_timeC;
    }

    public void setStart_timeC(String start_timeC) {
        this.start_timeC = start_timeC;
    }

    public String getEnd_timeC() {
        return end_timeC;
    }

    public void setEnd_timeC(String end_timeC) {
        this.end_timeC = end_timeC;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }


    public String getProcurement_type() {
        return procurement_type;
    }

    public void setProcurement_type(String procurement_type) {
        this.procurement_type = procurement_type;
    }

    public double getProcurement_sum() {
        return procurement_sum;
    }

    public void setProcurement_sum(double procurement_sum) {
        this.procurement_sum = procurement_sum;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(String entry_time) {
        this.entry_time = entry_time;
    }

    public String getPromotion_time() {
        return promotion_time;
    }

    public void setPromotion_time(String promotion_time) {
        this.promotion_time = promotion_time;
    }

    public String getOperating_post() {
        return operating_post;
    }

    public void setOperating_post(String operating_post) {
        this.operating_post = operating_post;
    }

    public String getProcurement_img() {
        return procurement_img;
    }

    public void setProcurement_img(String procurement_img) {
        this.procurement_img = procurement_img;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", staff_id=" + staff_id +
                ", company_id=" + company_id +
                ", examine_type='" + examine_type + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", content='" + content + '\'' +
                ", uptime='" + uptime + '\'' +
                ", expenses_type='" + expenses_type + '\'' +
                ", expenses_sum='" + expenses_sum + '\'' +
                ", expenses_picture='" + expenses_picture + '\'' +
                ", state='" + state + '\'' +
                ", conglomerate_id=" + conglomerate_id +
                ", company_name='" + company_name + '\'' +
                ", start_timeC='" + start_timeC + '\'' +
                ", end_timeC='" + end_timeC + '\'' +
                ", staff_name='" + staff_name + '\'' +
                ", itmes_id=" + itmes_id +
                ", outtime='" + outtime + '\'' +
                ", outtimeC=" + outtimeC +
                ", intimeC=" + intimeC +
                ", intime='" + intime + '\'' +
                ", outaddress='" + outaddress + '\'' +
                ", entry_time='" + entry_time + '\'' +
                ", promotion_time='" + promotion_time + '\'' +
                ", operating_post='" + operating_post + '\'' +
                ", procurement_img='" + procurement_img + '\'' +
                ", project_name='" + project_name + '\'' +
                ", procurement_type='" + procurement_type + '\'' +
                ", procurement_sum=" + procurement_sum +
                '}';
    }
}
