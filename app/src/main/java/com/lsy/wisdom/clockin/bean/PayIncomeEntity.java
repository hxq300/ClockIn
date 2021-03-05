package com.lsy.wisdom.clockin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hxq on 2021/3/4
 * describe :  TODO 收支明细实体类
 */
public class PayIncomeEntity implements Serializable {

    /**
     * items : [{"id":2,"conglomerate_id":0,"company_id":1,"company_name":"上海交捷交通智能科技有限公司","items_id":22,"items_name":"测试","project_id":19,"project_name":"地铁沙盘项目","supplier_id":1,"supplier_name":"测试公司","cost_types":"测试","payment_amount":"1300000000","payment_time":"2021-03-01","explain":"测试1","picture":"[reimburse/images/20201229072132,reimburse/images/20201229072132,reimburse/images/20210225101700]","state":"0","check":"通过"},{"id":1,"conglomerate_id":0,"company_id":1,"company_name":"上海交捷交通智能科技有限公司","items_id":22,"items_name":"测试","project_id":19,"project_name":"地铁沙盘项目","supplier_id":1,"supplier_name":"测试公司","cost_types":"测试","payment_amount":"1200000000","payment_time":"2021-02-25","explain":"测试","picture":"[reimburse/images/20201229072132,reimburse/images/20201229072132,reimburse/images/20210225101700]","state":"0","check":"未通过"}]
     * pageNo : 1
     * pageSize : 20
     * total : 2
     */

    private int pageNo;
    private int pageSize;
    private int total;
    private List<ItemsBean> items;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean implements Serializable{
        /**
         * id : 2
         * conglomerate_id : 0
         * company_id : 1
         * company_name : 上海交捷交通智能科技有限公司
         * items_id : 22
         * items_name : 测试
         * project_id : 19
         * project_name : 地铁沙盘项目
         * supplier_id : 1
         * supplier_name : 测试公司
         * cost_types : 测试
         * payment_amount : 1300000000
         * payment_time : 2021-03-01
         * explain : 测试1
         * picture : [reimburse/images/20201229072132,reimburse/images/20201229072132,reimburse/images/20210225101700]
         * state : 0
         * check : 通过
         */

        private int id;
        private int conglomerate_id;
        private int company_id;
        private String company_name;
        private int items_id;
        private String items_name;
        private int project_id;
        private String project_name;
        private int supplier_id;
        private String supplier_name;
        private String cost_types;
        private String payment_amount;
        private String payment_time;
        private String explain;
        private String picture;
        private String state;
        private String check;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public int getSupplier_id() {
            return supplier_id;
        }

        public void setSupplier_id(int supplier_id) {
            this.supplier_id = supplier_id;
        }

        public String getSupplier_name() {
            return supplier_name;
        }

        public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
        }

        public String getCost_types() {
            return cost_types;
        }

        public void setCost_types(String cost_types) {
            this.cost_types = cost_types;
        }

        public String getPayment_amount() {
            return payment_amount;
        }

        public void setPayment_amount(String payment_amount) {
            this.payment_amount = payment_amount;
        }

        public String getPayment_time() {
            return payment_time;
        }

        public void setPayment_time(String payment_time) {
            this.payment_time = payment_time;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }
}
