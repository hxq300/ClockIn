package com.lsy.wisdom.clockin.mvp.append;

/**
 * Created by lsy on 2020/5/25
 * todo :
 */
public interface AddInterface {

    interface Model {

//        审批:staff_id(登录返回id),conglomerate_id,company_id,examine_type(审批),content(内容)
//        报销:staff_id(登录返回id),conglomerate_id,company_id,examine_type(报销),content,expenses_type(报销类型),expenses_sum(报销金额),expenses_picture(报销图片)
//        出差:staff_id(登录返回id),conglomerate_id,company_id,examine_type(出差),content,outaddress(出差地址),outtimeC(出发时间戳),intimeC(返程时间戳 )
//        请假:staff_id(登录返回id),conglomerate_id,company_id,examine_type(请假),content,start_timeC(开始时间戳),end_timeC(结束时间戳)
//        加班:staff_id(登录返回id),conglomerate_id,company_id,examine_type(加班),content

        void addCoutentSP(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content);//审批

        void addCoutentBX(int itmes_id, int project_id,String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String expenses_type, double expenses_sum, String expenses_picture);//报销

        void addCoutentCG(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String procurement_type, double procurement_sum,String procurement_img,int project_id);//采购

        void addCoutentCC(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String outaddress, long outtimeC, long intimeC);//出差

        void addCoutentQJ(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, long start_timeC, long end_timeC);//请假

        void addCoutentJB(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content);//加班

        void addCoutentZZ(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, long start_timeC, long end_timeC, String station);//转正

    }

    interface View {
        //setData方法是为了 Activity实现view接口以后，重写这个方法就可以得到数据，为View赋值

        void setFailure(String message);//添加失败

        void setSuccess();//添加成功
    }

    interface Presenter {

        void addCoutentSP(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content);//审批

        void addCoutentBX(int itmes_id, int project_id, String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String expenses_type, double expenses_sum, String expenses_picture);//报销

        void addCoutentCG(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String procurement_type, double procurement_sum,String procurement_img,int project_id);//采购

        void addCoutentCC(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String outaddress, long outtimeC, long intimeC);//出差

        void addCoutentQJ(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, long start_timeC, long end_timeC);//请假

        void addCoutentJB(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content);//加班

        void addCoutentZZ(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, long start_timeC, long end_timeC, String station);//转正


        void responseFailure(String message);//添加失败

        void responseSuccess();

        void distory();
    }

}
