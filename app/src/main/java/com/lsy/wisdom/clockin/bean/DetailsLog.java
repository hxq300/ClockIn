package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/7/15
 * describe :  TODO
 */
public class DetailsLog {

//     "id":1,
//             "conglomerate_id":1,
//             "project_id":1,
//             "project_name":"安全体验馆项目",
//             "content":"憨憨0",
//             "url":"[]",
//             "uptime":"2020-07-07 09:46:34",
//             "uptimeC":1594086424057

    private int id;
    private int conglomerate_id;
    private int project_id;
    private String project_name;
    private String content;
    private String url;
    private String uptime;
    private String uptimeC;


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

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", conglomerate_id=" + conglomerate_id +
                ", project_id=" + project_id +
                ", project_name='" + project_name + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", uptime='" + uptime + '\'' +
                ", uptimeC='" + uptimeC + '\'' +
                '}';
    }
}
