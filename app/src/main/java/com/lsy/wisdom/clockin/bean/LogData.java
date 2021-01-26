package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/5/21
 * todo : 日志记录
 */
public class LogData {

//"id":12,
//        "staff_id":1,
//        "company_id":1,
//        "company_name":"上海交捷交通智能科技有限公司",
//        "content":"HK你更好您看彼此密子君那里聊",
//        "tomorrow_plan":"刺激知我命",
//        "uptime":"2020-05-28 11:12:35",
//        "uptimeC":1590635555000,
//        "staff_name":"石头",
//        "conglomerate_id":0


    private int id;
    private int staff_id;
    private int company_id;
    private String company_name;
    private String content;
    private String tomorrow_plan;
    private String uptime;
    private String uptimeC;
    private String staff_name;
    private String conglomerate_id;
    private String url;

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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public String getUptimeC() {
        return uptimeC;
    }

    public void setUptimeC(String uptimeC) {
        this.uptimeC = uptimeC;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getConglomerate_id() {
        return conglomerate_id;
    }

    public void setConglomerate_id(String conglomerate_id) {
        this.conglomerate_id = conglomerate_id;
    }

    public String getTomorrow_plan() {
        return tomorrow_plan;
    }

    public void setTomorrow_plan(String tomorrow_plan) {
        this.tomorrow_plan = tomorrow_plan;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", staff_id=" + staff_id +
                ", company_id=" + company_id +
                ", company_name='" + company_name + '\'' +
                ", content='" + content + '\'' +
                ", tomorrow_plan='" + tomorrow_plan + '\'' +
                ", uptime='" + uptime + '\'' +
                ", uptimeC='" + uptimeC + '\'' +
                ", staff_name='" + staff_name + '\'' +
                ", conglomerate_id='" + conglomerate_id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
