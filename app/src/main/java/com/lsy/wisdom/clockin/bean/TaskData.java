package com.lsy.wisdom.clockin.bean;

/**
 * Created by lsy on 2020/7/30
 * describe :  TODO
 */
public class TaskData {

//    "id":19,
//            "conglomerate_id":1,
//            "creator_id":6,
//            "creator_name":"石胜伟",
//            "principal_id":18,
//            "principal_name":"李淑英",
//            "participant":"[8,9,10,20]",
//            "task_title":"666",
//            "task_describe":"777",
//            "uptime":"2020-07-28 11:08:40",
//            "uptimeC":1595905720000,
//            "end_time":"2020-07-31 08:00:00",
//            "end_timeC":1596153600000,
//            "degree":"77",
//            "taskday":2,
//            "state":"进行中",
//            "status":"正常"
//    participant_name

    private int id;
    private int conglomerate_id;
    private int creator_id;
    private String creator_name;
    private int principal_id;
    private String principal_name;
    private String participant;
    private String task_title;
    private String task_describe;
    private String uptime;
    private String uptimeC;
    private String end_time;
    private String end_timeC;
    private String degree;
    private int taskday;
    private String state;
    private String status;
    private String start_img;
    private String participant_name;
    private String task_summarize;
    private String task_reason;

    public String getStart_img() {
        return start_img;
    }

    public void setStart_img(String start_img) {
        this.start_img = start_img;
    }

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

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public int getPrincipal_id() {
        return principal_id;
    }

    public void setPrincipal_id(int principal_id) {
        this.principal_id = principal_id;
    }

    public String getPrincipal_name() {
        return principal_name;
    }

    public void setPrincipal_name(String principal_name) {
        this.principal_name = principal_name;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_describe() {
        return task_describe;
    }

    public void setTask_describe(String task_describe) {
        this.task_describe = task_describe;
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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public int getTaskday() {
        return taskday;
    }

    public void setTaskday(int taskday) {
        this.taskday = taskday;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParticipant_name() {
        return participant_name;
    }

    public void setParticipant_name(String participant_name) {
        this.participant_name = participant_name;
    }

    public String getTask_summarize() {
        return task_summarize;
    }

    public void setTask_summarize(String task_summarize) {
        this.task_summarize = task_summarize;
    }

    public String getTask_reason() {
        return task_reason;
    }

    public void setTask_reason(String task_reason) {
        this.task_reason = task_reason;
    }


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", conglomerate_id=" + conglomerate_id +
                ", creator_id=" + creator_id +
                ", creator_name='" + creator_name + '\'' +
                ", principal_id=" + principal_id +
                ", principal_name='" + principal_name + '\'' +
                ", participant='" + participant + '\'' +
                ", task_title='" + task_title + '\'' +
                ", task_describe='" + task_describe + '\'' +
                ", uptime='" + uptime + '\'' +
                ", uptimeC='" + uptimeC + '\'' +
                ", end_time='" + end_time + '\'' +
                ", end_timeC='" + end_timeC + '\'' +
                ", degree='" + degree + '\'' +
                ", taskday=" + taskday +
                ", state='" + state + '\'' +
                ", status='" + status + '\'' +
                ", start_img='" + start_img + '\'' +
                ", participant_name='" + participant_name + '\'' +
                ", task_summarize='" + task_summarize + '\'' +
                ", task_reason='" + task_reason + '\'' +
                '}';
    }
}
