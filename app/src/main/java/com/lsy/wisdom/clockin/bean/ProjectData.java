package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/7/7
 * describe :  项目数据实体
 */
public class ProjectData {
//     "id":4,
//             "items_id":9,
//             "conglomerate_id":1,
//             "items_name":"吧",
//             "staff_id":21,
//             "staff_name":"测试账号",
//             "start_time":"2020-01-07",
//             "start_timeC":1578382754000,
//             "end_time":"2020-12-07",
//             "end_timeC":1607326765000,
//             "amount":86654,
//             "uptime":"2020-07-07 15:39:50",
//             "uptimeC":

//        "project_name":"测试项目",

    private int id;
    private int items_id;
    private int conglomerate_id;
    private String items_name;
    private int staff_id;
    private String staff_name;
    private String start_time;
    private String start_timeC;
    private String end_time;
    private String end_timeC;
    private double amount;
    private String uptime;
    private String uptimeC;
    private String project_name;
    private String participant_name;

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_timeC() {
        return start_timeC;
    }

    public void setStart_timeC(String start_timeC) {
        this.start_timeC = start_timeC;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_timeC() {
        return end_timeC;
    }

    public void setEnd_timeC(String end_timeC) {
        this.end_timeC = end_timeC;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public String getParticipant_name() {
        return participant_name;
    }

    public void setParticipant_name(String participant_name) {
        this.participant_name = participant_name;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", items_id=" + items_id +
                ", conglomerate_id=" + conglomerate_id +
                ", items_name='" + items_name + '\'' +
                ", staff_id=" + staff_id +
                ", staff_name='" + staff_name + '\'' +
                ", start_time='" + start_time + '\'' +
                ", start_timeC='" + start_timeC + '\'' +
                ", end_time='" + end_time + '\'' +
                ", end_timeC='" + end_timeC + '\'' +
                ", amount=" + amount +
                ", uptime='" + uptime + '\'' +
                ", uptimeC='" + uptimeC + '\'' +
                ", project_name='" + project_name + '\'' +
                ", participant_name='" + participant_name + '\'' +
                '}';
    }
}
