package com.lsy.wisdom.clockin.bean;

/**
 * Created by hxq on 2021/3/8
 * describe :  TODO
 */
public class CompanyEntity {

    /**
     * id : 1
     * conglomerate_id : 1
     * company_name : 上海交捷交通智能科技有限公司
     * person_count : 9
     * in_time : 09:15:00
     * out_time : 18:00:00
     */

    private int id;
    private int conglomerate_id;
    private String company_name;
    private int person_count;
    private String in_time;
    private String out_time;

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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getPerson_count() {
        return person_count;
    }

    public void setPerson_count(int person_count) {
        this.person_count = person_count;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }
}
