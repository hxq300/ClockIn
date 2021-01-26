package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/6/19
 * todo : 跟进信息
 */
public class ProcessData {

//    "id":2,
//            "conglomerate_id":1,
//            "items_id":10,
//            "items_name":"中铁",
//            "staff_id":18,
//            "staff_name":"李淑英",
//            "uptime":"2020-06-19 15:47:27",
//            "uptimeC":1592552847000,
//            "principal":"lisay",
//            "content":"项目跟进",
//            "schedule_type":"已签约"

    private int id;
    private int conglomerate_id;
    private int items_id;
    private String items_name;
    private int staff_id;
    private String staff_name;
    private String uptime;
    private String uptimeC;
    private String principal;
    private String content;
    private String schedule_type;

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

    public int getItems_id() {
        return items_id;
    }

    public void setItems_id(int items_id) {
        this.items_id = items_id;
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

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSchedule_type() {
        return schedule_type;
    }

    public void setSchedule_type(String schedule_type) {
        this.schedule_type = schedule_type;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", conglomerate_id=" + conglomerate_id +
                ", items_id=" + items_id +
                ", items_name='" + items_name + '\'' +
                ", staff_id=" + staff_id +
                ", staff_name='" + staff_name + '\'' +
                ", uptime='" + uptime + '\'' +
                ", uptimeC='" + uptimeC + '\'' +
                ", principal='" + principal + '\'' +
                ", content='" + content + '\'' +
                ", schedule_type='" + schedule_type + '\'' +
                '}';
    }
}
