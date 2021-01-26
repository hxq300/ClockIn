package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/5/7
 * todo :
 */
public class NoticeData {


//    "id":1,
//            "company_id":1,
//            "title":"1",
//            "content":"1",
//            "uptime":"2020-05-21 11:49:01",
//            "uptimeC":1590032941000,
//            "read_count":0,
//            "conglomerate_id":1,
//            "company_name":"上海交捷交通智能科技有限公司"

    private int id;
    private int company_id;
    private String title;
    private String content;
    private String uptime;
    private String uptimeC;
    private String company_name;
    private int read_count;
    private int conglomerate_id;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getRead_count() {
        return read_count;
    }

    public void setRead_count(int read_count) {
        this.read_count = read_count;
    }

    public int getConglomerate_id() {
        return conglomerate_id;
    }

    public void setConglomerate_id(int conglomerate_id) {
        this.conglomerate_id = conglomerate_id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", company_id=" + company_id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", uptime='" + uptime + '\'' +
                ", uptimeC='" + uptimeC + '\'' +
                ", company_name='" + company_name + '\'' +
                ", read_count=" + read_count +
                ", conglomerate_id=" + conglomerate_id +
                '}';
    }
}
