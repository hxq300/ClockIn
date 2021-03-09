package com.lsy.wisdom.clockin.bean;

/**
 * Created by hxq on 2021/3/8
 * describe :  TODO
 */
public class SupplierEntity {

    /**
     * id : 1
     * conglomerate_id : 1
     * contacts : 测试
     * contacts_phone : 13000000000
     * company : 测试公司
     * account : 开户账户
     * city : 所属城市
     * address : 地址
     * commodity : 供应商品
     * blacklist : 黑名单
     * uptime : null
     */

    private int id;
    private int conglomerate_id;
    private String contacts;
    private String contacts_phone;
    private String company;
    private String account;
    private String city;
    private String address;
    private String commodity;
    private String blacklist;
    private Object uptime;

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

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContacts_phone() {
        return contacts_phone;
    }

    public void setContacts_phone(String contacts_phone) {
        this.contacts_phone = contacts_phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }

    public Object getUptime() {
        return uptime;
    }

    public void setUptime(Object uptime) {
        this.uptime = uptime;
    }
}
