package com.lsy.wisdom.clockin.request;

/**
 * Create by lsy on 2019/09/19
 * MODO :
 */
public class RequestURL {
        // 测试服
//        public static String RequestUrl = "http://192.168.1.103:8188";
    //正式服
    public static String RequestUrl = "http://101.132.154.10:8188";
    //    public static String RequestImg = "http://192.168.1.103:8188/img/download_img?img_url=";
    public static String RequestImg = "http://101.132.154.10:8188/img/download_img?img_url=";
    public static String OssUrl = "https://jjjt.oss-cn-shanghai.aliyuncs.com/";

    //Login部分
    public static String login = RequestUrl + "/AppLogin/AppLogin?";//登录

    //打卡接口
    public static String findClockstatus = RequestUrl + "/Staff/FindClockstatus?";//打卡状态（1签到   0签退）
    public static String insertRegistration = RequestUrl + "/Registration/InsertRegistration?";//签到
    public static String updateRegistration = RequestUrl + "/Registration/UpdateRegistration?";//签退
    public static String outId = RequestUrl + "/Storage/FindStorageByStaffId?";// 获取下班打卡 所需id

    public static String getRecordHistory = RequestUrl + "/RegistrationRecord/FindRegistrationRecordByStaffId?";//获取打卡记录
    public static String getAllCard = RequestUrl + "/RegistrationRecord/FindAll?";//查看全部打卡记录

    //员工信息接口
    public static String findStaffById = RequestUrl + "/Staff/FindStaffById?";//根据id查询员工信息
    public static String updateStaffApp = RequestUrl + "/Staff/UpdateStaffApp?";//app修改员工信息
    public static String getStaff = RequestUrl + "/Staff/FindStaff?";//分页查询员工信息
    public static String getAllStaff = RequestUrl + "/Staff/FindAllApp?";//查询所有员工

    //日志
    public static String addLog = RequestUrl + "/Log/InsertLog?";//添加日志
    public static String getLog = RequestUrl + "/Log/FindLogByStaffId?";//查询个人日志
    public static String getStaffLog = RequestUrl + "/Log/FindLogByIdY?";//查询个人日志
    public static String deleteData = RequestUrl + "/Log/DeleteLog?"; // 删除


    //公告
    public static String getNotice = RequestUrl + "/Announcement/FindAnnouncement?";//分页模糊查询公告
    public static String addCount = RequestUrl + "/Announcement/UpdateReadCount?";//添加阅读人数

    //记录
    public static String getRecord = RequestUrl + "/Examine/FindExamineByStaffId?";//查询审批记录
    public static String getStaffRecord = RequestUrl + "/Examine/FindExamineByY?";//查询由我审批的审批信息

    //添加部分
    public static String addCoutent = RequestUrl + "/Examine/InsertExamine?";//新建内容


    //意见反馈
    public static String addFeed = RequestUrl + "/Feedback/InsertFeedback?";//意见反馈

    //客户
    public static String customerQuery = RequestUrl + "/Items/FindItemsApp?";//客户查询
    public static String addCustomer = RequestUrl + "/Items/InsertItems?";//添加客户
    public static String addPush = RequestUrl + "/Items/PushItems?";//推送客户
    public static String updateMessage = RequestUrl + "/Items/UpdateItems?"; // 修改用户信息

    //跟进信息
    public static String addProcess = RequestUrl + "/Schedule/InsertSchedule?";//推送客户
    public static String getStaffProcess = RequestUrl + "/Schedule/FindScheduleByStaffId?";//分页查询个人跟进信息

    //联系人
    public static String getClient = RequestUrl + "/Client/FindClientByItemsId?";//获取联系人信息
    public static String addClient = RequestUrl + "/Client/InsertClient?";//添加项目联系人

    //审批
    public static String approvalPass = RequestUrl + "/Examine/UpdateStateT?";//通过审批
    public static String approvalNoPass = RequestUrl + "/Examine/UpdateStateW?";//不通过审批
    public static String spDel = RequestUrl + "/Examine/DeleteExamine?";//审批删除

    //项目
    public static String addProject = RequestUrl + "/Project/InsertProject?";//增加项目
    public static String updateProject = RequestUrl + "/Project/UpdateProject?";//修改项目
    public static String getProjectList = RequestUrl + "/Project/FindProjectByStaffId?";//获取项目列表

    //项目日志
    public static String getProjectLog = RequestUrl + "/ProjectLog/FindProjectLogByProjectId?";//查找项目对应的项目日志
    public static String addProjectLog = RequestUrl + "/ProjectLog/InsertProjectLog?";//增加项目日志
    public static String removeProjectLog = RequestUrl + "/ProjectLog/InsertProjectLog?";//删除日志

    //关联
    public static String bindingProject = RequestUrl + "/Project/FindProjectByStaffIdB?";//关联项目绑定
    public static String bindingCus = RequestUrl + "/Items/FindItemsAppB?";//关联客户

    //任务
    public static String addTask = RequestUrl + "/Task/InsertTask?";//发布任务

    public static String getPermission = RequestUrl + "/Task/FindPermission?";//权限查看

    public static String getMyCreate = RequestUrl + "/Task/FindTaskByCreatorId?";//查看我派发的任务
    public static String getMyJoin = RequestUrl + "/Task/FindTaskByParticipant?";//查询我参与的任务
    public static String getMyResponsible = RequestUrl + "/Task/FindTaskByPrincipalId?";//查询我负责的任务
    public static String cancelTask = RequestUrl + "/Task/UpdateTaskState?";//撤销任务
    public static String taskSuc = RequestUrl + "/Task/UpdateTaskStateW?";//任务完成
    public static String taskPass = RequestUrl + "/Task/UpdateTaskT?";//任务通过
    public static String taskReject = RequestUrl + "/Task/UpdateTaskStateW?";//任务驳回

    //收支
    public static String payApplyFor = RequestUrl + "/Budget/FindBudget?";//费用收支明细
    public static String payRecord = RequestUrl + "/Balance/FindBalance?";//付款申请明细
    public static String deleteRecord = RequestUrl + "/Balance/DeleteBalance?";//删除 付款申请明细
    public static String deleteApply = RequestUrl + "/Budget/DeleteBudget?";//删除 费用收支明细
}
