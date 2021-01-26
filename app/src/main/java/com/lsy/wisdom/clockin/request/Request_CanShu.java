package com.lsy.wisdom.clockin.request;

/**
 * Create by lsy on 2019/8/8
 */
public class Request_CanShu {
    private String key;
    private String value;
    private int intvalue;

    public Request_CanShu() {

    }

    public Request_CanShu(String key, String value) {
        this.key = key;
        this.value = value;
    }

//    public Request_CanShu(String key, int intvalue) {
//        this.key = key;
//        this.intvalue = intvalue;
//    }


    public int getIntvalue() {
        return intvalue;
    }

    public void setIntvalue(int intvalue) {
        this.intvalue = intvalue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", intvalue=" + intvalue +
                '}';
    }
}
