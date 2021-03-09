package com.lsy.wisdom.clockin.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.add.SubmitToActivity;
import com.lsy.wisdom.clockin.bean.CompanyEntity;
import com.lsy.wisdom.clockin.bean.ProjectC;
import com.lsy.wisdom.clockin.bean.ProjectCus;
import com.lsy.wisdom.clockin.bean.SupplierEntity;
import com.lsy.wisdom.clockin.mvp.budding.BuddingInterface;
import com.lsy.wisdom.clockin.mvp.budding.BuddingPresent;
import com.lsy.wisdom.clockin.oss.OssService;
import com.lsy.wisdom.clockin.permission.QuanXian;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.RealPathFromUriUtils;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.TimeUtils;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddIncomeActivity extends AppCompatActivity implements QuanXian.OnPermission, OssService.OssCallback, BuddingInterface.View {

    @BindView(R.id.check_line)
    LinearLayout checkLine;
    @BindView(R.id.add_approval_submit)
    Button addApprovalSubmit;
    @BindView(R.id.add_approval_toolbar)
    IToolbar addApprovalToolbar;
    @BindView(R.id.add_to)
    TextView addTo;
    @BindView(R.id.reimburse_type)
    TextView reimburseType;
    @BindView(R.id.line_type)
    LinearLayout lineType;
    @BindView(R.id.reimburse_money)
    EditText reimburseMoney;
    @BindView(R.id.reimburse_content)
    EditText reimburseContent;
    @BindView(R.id.reimburse_num)
    TextView reimburseNum;
    @BindView(R.id.add_recycler_reimburse)
    RecyclerView addRecyclerReimburse;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.company_line)
    LinearLayout companyLine;
    @BindView(R.id.budding_cus)
    TextView buddingCus;
    @BindView(R.id.supplier_line)
    LinearLayout supplierLine;
    @BindView(R.id.supplier)
    TextView supplier;
    @BindView(R.id.budding_line)
    LinearLayout buddingLine;
    @BindView(R.id.budding_util)
    TextView buddingUtil;
    @BindView(R.id.util_line)
    LinearLayout utilLine;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.time_line)
    LinearLayout timeLine;
    @BindView(R.id.pay)
    TextView pay;
    @BindView(R.id.pay_line)
    LinearLayout payLine;
    @BindView(R.id.state)
    TextView tv_state;
    @BindView(R.id.state_line)
    LinearLayout state_line;

    private TimePickerView pickerView;
    private String mTime = "";

    private int type;
    private SharedUtils sharedUtils;

    private CommonAdapter listAdapter;

    private List<ProjectC> pList = null;
    private List<ProjectCus> cList = null;

    private List<String> gradeData; //自行添加一些string

    private OptionsPickerView pvOptions;
    private OptionsPickerView pvOptions1;
    private OptionsPickerView pvOptions2;


    private BuddingInterface.Presenter buddingP;
    private String listIds;

    //
    private File cameraSavePath;//拍照照片路径
    private Uri uri;

    //上传图片部分
    private List<String> imageList = new ArrayList<>();
    private List<Uri> picPahts = new ArrayList<>();

    // 提交数据
//    private String

    @OnClick({R.id.check_line, R.id.add_approval_submit, R.id.line_type, R.id.util_line, R.id.budding_line
            , R.id.pay_line, R.id.time_line, R.id.company_line, R.id.supplier_line,R.id.state_line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_line:
                Intent intent = new Intent(AddIncomeActivity.this, SubmitToActivity.class);
                startActivity(intent);
                break;
            case R.id.add_approval_submit: // 提交表单
                if (type == 0) {
                    if (condition()) {
                        companyApply(); // 支付申请
                    }
                } else if (type == 1) {
                    if (condition() && status != null) { // 收支明细
                        companyPay();
                    }
                }

                break;
            case R.id.line_type: // 选择费用类型
                initSelector();
                break;
            case R.id.util_line: // 项目名称
                if (pList != null) {
                    initBuddingProject();
                } else {
                    buddingUtil.setText("暂无");
                }
                break;
            case R.id.budding_line:// 客户名称
                if (cList != null) {
                    initBuddingCus();
                } else {
                    buddingCus.setText("暂无");
                }
                break;
            case R.id.pay_line: // 支付状态
                initPayType();
                break;
            case R.id.time_line: // 支付时间
                if (GeneralMethod.isFastClick()) {
                    initTime();
                }
                break;
            case R.id.company_line: // 公司
                if (mCompanyEntities != null) {
                    initCompany();
                } else {
                    company.setText("暂无");
                }
                break;
            case R.id.supplier_line: // 供应商
                if (mSupplierEntities != null) {
                    initSupplier();
                } else {
                    supplier.setText("暂无");
                }
                break;
            case R.id.state_line: // 有无发票
                initState();
        }
    }

    private void initState() {


        if (gradeData == null) {
            gradeData = new ArrayList<>();
        }
        gradeData.clear();
        gradeData.add("有");
        gradeData.add("无");


        pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            pay.setText(gradeData.get(options1));
            state = gradeData.get(options1);
            // gradeData.get(options1)
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
//                .setTitleText("请选择分享的客户")
                .setSubCalSize(16)//确定和取消文字大小
                .setSubmitColor(0xff3B73FF)//确定按钮文字颜色
                .setCancelColor(0xff999999)//取消按钮文字颜色
                .setTitleBgColor(0xffF4F4F4)//标题背景颜色 Night mode
                .setTitleSize(15)
                .setContentTextSize(18)//滚轮文字大小
                .setTextColorCenter(0xff333333)
                .setTextColorOut(0x999999)
                .setDividerColor(0xffEEEEEE)
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLineSpacingMultiplier((float) 2.5) //设置item的高度
                .build();

        pvOptions.setPicker(gradeData);
        pvOptions.show();
    }

    private boolean condition() {
        if (company_id == 999 || items_id == 999 || project_id == 999 || supplier_id == 999 || cost_types == null
                || payment_time == null || "".equals(reimburseContent)) {
            return false;
        }
        return true;
    }

    private int company_id = 999;
    private int items_id = 999;
    private int project_id = 999;
    private int supplier_id = 999;
    private String cost_types; // 费用类型
    private String payment_time;
    private String status; // 收支状态
    private String state; // 有无 发票

    /**
     * 添加 付款申请
     */
    private void companyApply() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        //传参:staff_id(员工id),company_id(公司id),content(日志内容)
        listcanshu.put("staff_id", OKHttpClass.getUserId(AddIncomeActivity.this));
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(AddIncomeActivity.this));

        listcanshu.put("company_id", company_id);
        listcanshu.put("items_id", items_id);
        listcanshu.put("project_id", project_id);
        listcanshu.put("supplier_id", supplier_id);


        listcanshu.put("payment_amount", reimburseMoney.getText().toString() + "");

        listcanshu.put("cost_types", cost_types);
        listcanshu.put("payment_time", payment_time);
        listcanshu.put("state", state);
        listcanshu.put("explain", reimburseContent.getText().toString());


        listcanshu.put("list", sharedUtils.getData(SharedUtils.LISTID, "[]"));//选择提交人
        listcanshu.put("picture", "" + imageList.toString());//图片地址

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(AddIncomeActivity.this, RequestURL.AddInsertBalance, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {

                //请求成功数据回调
                L.log("log", "addLog==" + dataString);

                //{"message":"上传成功!","data":null,"code":200}
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        finish();
                    } else {
                        ToastUtils.showBottomToast(AddIncomeActivity.this, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
                return dataString;
            }
        });
    }

    /**
     * 添加 收支明细
     */
    private void companyPay() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        //传参:staff_id(员工id),company_id(公司id),content(日志内容)
        listcanshu.put("staff_id", OKHttpClass.getUserId(AddIncomeActivity.this));
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(AddIncomeActivity.this));

        listcanshu.put("company_id", company_id);
        listcanshu.put("items_id", items_id);
        listcanshu.put("project_id", project_id);
        listcanshu.put("supplier_id", supplier_id);

        listcanshu.put("payment_amount", reimburseMoney.getText().toString() + "");

        listcanshu.put("cost_types", cost_types);
        listcanshu.put("payment_time", payment_time);
        listcanshu.put("status", status);
        listcanshu.put("state", state);
        listcanshu.put("explain", reimburseContent.getText().toString());


        listcanshu.put("list", sharedUtils.getData(SharedUtils.LISTID, "[]"));//选择提交人
        listcanshu.put("picture", "" + imageList.toString());//图片地址

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(AddIncomeActivity.this, RequestURL.AddInsertBudget, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {

                //请求成功数据回调
                L.log("log", "addLog==" + dataString);

                //{"message":"上传成功!","data":null,"code":200}
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        finish();
                    } else {
                        ToastUtils.showBottomToast(AddIncomeActivity.this, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
                return dataString;
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        setSupportActionBar(addApprovalToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();
        initView();

        sharedUtils = new SharedUtils(AddIncomeActivity.this, SharedUtils.CLOCK);

        listIds = sharedUtils.getData(SharedUtils.LISTID, "[]");

        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (nameIds != null) {
            addTo.setText("" + nameIds.substring(1, nameIds.length() - 1));
            addTo.setTextColor(0xff333333);
        }

        buddingP = new BuddingPresent(this, AddIncomeActivity.this);


        //判断权限是否全部打开
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_APN_SETTINGS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }

        buddingP.getProject();
        buddingP.getCus();
        buddingP.getFindCompany();
        buddingP.getSelectSupplier();
    }

    private void initView() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 10);
        if (type == 0) {
            addApprovalToolbar.setTitleText("付款申请");
            payLine.setVisibility(View.GONE);
        } else if (type == 1) {
            addApprovalToolbar.setTitleText("费用收支");
            payLine.setVisibility(View.VISIBLE);
        }

        picPahts.clear();
        picPahts.add(0, uri);
        //===添加图片部分
        addRecyclerReimburse.setItemViewCacheSize(100);
        addRecyclerReimburse.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        addRecyclerReimburse.setNestedScrollingEnabled(false);

        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");

        listAdapter = new CommonAdapter<Uri>(getApplicationContext(), R.layout.view_reimburse_image, picPahts) {
            @Override
            protected void convert(ViewHolder holder, Uri uri, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
                if (picPahts.size() - 1 == position) {
                    image.setImageResource(R.mipmap.add_image);
                } else {
                    Glide.with(AddIncomeActivity.this).load(uri).into(image);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (GeneralMethod.isFastClick()) {

                            if (picPahts.size() - 1 == position) {
                                showBottomDialog();
                            } else {
                                notifyDataSetChanged();
                            }
                        }
                    }

                });
            }
        };

        addRecyclerReimburse.setAdapter(listAdapter);


        reimburseContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                reimburseNum.setText(editable.length() + "/200");
            }
        });
    }

    private void initTime() {
        if (pickerView != null) {
            pickerView.dismiss();
        }

        pickerView = new TimePickerBuilder(AddIncomeActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mTime = TimeUtils.getTime(date);
                AddIncomeActivity.this.time.setText(mTime);
                payment_time = mTime;
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                L.log("pvTime", "onTimeSelectChanged");
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})
//                            .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();
        pickerView.show();
    }

    //自定义底部弹出框
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(AddIncomeActivity.this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(AddIncomeActivity.this, R.layout.dialog_custom_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        //拍照
        dialog.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goCamera();
            }
        });

        //从相册中选择
        dialog.findViewById(R.id.tv_take_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goPhotoAlbum();
            }
        });

        //取消
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 234);
    }

    //激活相机操作
    private void goCamera() {
        uri = null;
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(AddIncomeActivity.this, "com.lsy.wisdom.clockin.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1500);
    }

    private void initBar() {
        addApprovalToolbar.inflateMenu(R.menu.toolbar_menu);
        addApprovalToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void initCompany() {
        if (gradeData == null) {
            gradeData = new ArrayList<>();
        }
        gradeData.clear();
        for (int i = 0; i < mCompanyEntities.size(); i++) {
            gradeData.add(mCompanyEntities.get(i).getCompany_name());
        }

        pvOptions1 = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            company.setText(mCompanyEntities.get(options1).getCompany_name());
            company_id = mCompanyEntities.get(options1).getConglomerate_id();
            // gradeData.get(options1)
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
//                .setTitleText("请选择分享的客户")
                .setSubCalSize(16)//确定和取消文字大小
                .setSubmitColor(0xff3B73FF)//确定按钮文字颜色
                .setCancelColor(0xff999999)//取消按钮文字颜色
                .setTitleBgColor(0xffF4F4F4)//标题背景颜色 Night mode
                .setTitleSize(15)
                .setContentTextSize(18)//滚轮文字大小
                .setTextColorCenter(0xff333333)
                .setTextColorOut(0x999999)
                .setDividerColor(0xffEEEEEE)
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLineSpacingMultiplier((float) 2.5) //设置item的高度
                .build();

        pvOptions1.setPicker(gradeData);
        pvOptions1.show();
    }


    private void initSupplier() {
        if (gradeData == null) {
            gradeData = new ArrayList<>();
        }
        gradeData.clear();
        for (int i = 0; i < mSupplierEntities.size(); i++) {
            gradeData.add(mSupplierEntities.get(i).getCompany());
        }

        pvOptions1 = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            supplier.setText(mSupplierEntities.get(options1).getCompany());
            supplier_id = mSupplierEntities.get(options1).getId();
            // gradeData.get(options1)
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
//                .setTitleText("请选择分享的客户")
                .setSubCalSize(16)//确定和取消文字大小
                .setSubmitColor(0xff3B73FF)//确定按钮文字颜色
                .setCancelColor(0xff999999)//取消按钮文字颜色
                .setTitleBgColor(0xffF4F4F4)//标题背景颜色 Night mode
                .setTitleSize(15)
                .setContentTextSize(18)//滚轮文字大小
                .setTextColorCenter(0xff333333)
                .setTextColorOut(0x999999)
                .setDividerColor(0xffEEEEEE)
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLineSpacingMultiplier((float) 2.5) //设置item的高度
                .build();

        pvOptions1.setPicker(gradeData);
        pvOptions1.show();
    }

    private void initBuddingProject() {

        if (gradeData == null) {
            gradeData = new ArrayList<>();
        }
        gradeData.clear();
        for (int i = 0; i < pList.size(); i++) {
            gradeData.add(pList.get(i).getProject_name());
        }

        pvOptions1 = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            buddingUtil.setText(pList.get(options1).getProject_name());
            project_id = pList.get(options1).getId();
            // gradeData.get(options1)
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
//                .setTitleText("请选择分享的客户")
                .setSubCalSize(16)//确定和取消文字大小
                .setSubmitColor(0xff3B73FF)//确定按钮文字颜色
                .setCancelColor(0xff999999)//取消按钮文字颜色
                .setTitleBgColor(0xffF4F4F4)//标题背景颜色 Night mode
                .setTitleSize(15)
                .setContentTextSize(18)//滚轮文字大小
                .setTextColorCenter(0xff333333)
                .setTextColorOut(0x999999)
                .setDividerColor(0xffEEEEEE)
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLineSpacingMultiplier((float) 2.5) //设置item的高度
                .build();

        pvOptions1.setPicker(gradeData);
        pvOptions1.show();
    }


    /**
     * 请求权限
     */
    private void requestPermissions() {
        QuanXian quanXian = new QuanXian(AddIncomeActivity.this, AddIncomeActivity.this);
        quanXian.setOnPermission_isok(this);
        quanXian.requestPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    private void initPayType() {

        if (gradeData == null) {
            gradeData = new ArrayList<>();
        }
        gradeData.clear();
        gradeData.add("支出");
        gradeData.add("收入");


        pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            pay.setText(gradeData.get(options1));
            status = gradeData.get(options1);
            // gradeData.get(options1)
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
//                .setTitleText("请选择分享的客户")
                .setSubCalSize(16)//确定和取消文字大小
                .setSubmitColor(0xff3B73FF)//确定按钮文字颜色
                .setCancelColor(0xff999999)//取消按钮文字颜色
                .setTitleBgColor(0xffF4F4F4)//标题背景颜色 Night mode
                .setTitleSize(15)
                .setContentTextSize(18)//滚轮文字大小
                .setTextColorCenter(0xff333333)
                .setTextColorOut(0x999999)
                .setDividerColor(0xffEEEEEE)
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLineSpacingMultiplier((float) 2.5) //设置item的高度
                .build();

        pvOptions.setPicker(gradeData);
        pvOptions.show();
    }

    private void initSelector() {

        if (gradeData == null) {
            gradeData = new ArrayList<>();
        }
        gradeData.clear();
        gradeData.add("差旅费");
        gradeData.add("交通费");
        gradeData.add("招待费");
        gradeData.add("团建费");
        gradeData.add("采购费");
        gradeData.add("其他");

        pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            reimburseType.setText(gradeData.get(options1));
            cost_types = gradeData.get(options1);
            // gradeData.get(options1)
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
//                .setTitleText("请选择分享的客户")
                .setSubCalSize(16)//确定和取消文字大小
                .setSubmitColor(0xff3B73FF)//确定按钮文字颜色
                .setCancelColor(0xff999999)//取消按钮文字颜色
                .setTitleBgColor(0xffF4F4F4)//标题背景颜色 Night mode
                .setTitleSize(15)
                .setContentTextSize(18)//滚轮文字大小
                .setTextColorCenter(0xff333333)
                .setTextColorOut(0x999999)
                .setDividerColor(0xffEEEEEE)
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLineSpacingMultiplier((float) 2.5) //设置item的高度
                .build();

        pvOptions.setPicker(gradeData);
        pvOptions.show();
    }

    /**
     * 判断输入完成情况
     */
    private boolean is_input() {

        if (reimburseType.getText().toString().trim().length() < 1) {
            Toast.makeText(AddIncomeActivity.this, "请选择报销类型", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (reimburseMoney.getText().toString().trim().length() < 1) {
            Toast.makeText(AddIncomeActivity.this, "请输入报销金额", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (reimburseContent.getText().toString().trim().length() < 1) {
            Toast.makeText(AddIncomeActivity.this, "请输入报销事由", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void initBuddingCus() {

        if (gradeData == null) {
            gradeData = new ArrayList<>();
        }
        gradeData.clear();
        for (int i = 0; i < cList.size(); i++) {
            gradeData.add(cList.get(i).getItems_name());
        }
        pvOptions2 = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            buddingCus.setText(cList.get(options1).getItems_name());
            items_id = cList.get(options1).getId();
            // gradeData.get(options1)
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
//                .setTitleText("请选择分享的客户")
                .setSubCalSize(16)//确定和取消文字大小
                .setSubmitColor(0xff3B73FF)//确定按钮文字颜色
                .setCancelColor(0xff999999)//取消按钮文字颜色
                .setTitleBgColor(0xffF4F4F4)//标题背景颜色 Night mode
                .setTitleSize(15)
                .setContentTextSize(18)//滚轮文字大小
                .setTextColorCenter(0xff333333)
                .setTextColorOut(0x999999)
                .setDividerColor(0xffEEEEEE)
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLineSpacingMultiplier((float) 2.5) //设置item的高度
                .build();

        pvOptions2.setPicker(gradeData);
        pvOptions2.show();
    }


    //上传图片到oss
    private void updateOss(String filename, String filePath) {

        //初始化OssService类，参数分别是Content，accessKeyId，accessKeySecret，endpoint，bucketName（后4个参数是您自己阿里云Oss中参数）
        OssService ossService = new OssService(AddIncomeActivity.this, OssService.ACCESS_KEY_ID, OssService.ACCESS_KEY_SECRET, "http://oss-cn-shanghai.aliyuncs.com", "jjjt");
        //初始化OSSClient
        ossService.initOSSClient();

        ossService.setCallback(this);

//        ossService.setCallback(new OssService.OssCallback() {
//            @Override
//            public void sucess(String backUrl) {
//                L.log("callback", "backUrl===" + backUrl);
//            }
//
//            @Override
//            public void failure(String message) {
//                L.log("callback", "message===" + message);
//            }
//        });
        ossService.getProgressCallback();
        String time = "" + System.currentTimeMillis();
        //开始上传，参数分别为content，上传的文件名filename，上传的文件路径filePath
        ossService.beginupload(this, filename, filePath);
        //上传的进度回调
        ossService.setProgressCallback(new OssService.ProgressCallback() {
            @Override
            public void onProgressCallback(final double progress) {
                L.log("上传进度：" + progress);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (nameIds != null) {
            addTo.setText("" + nameIds.substring(1, nameIds.length() - 1));
            addTo.setTextColor(0xff333333);
        }
    }


    @Override
    public void setProject(List<ProjectC> pList) {
        this.pList = pList;
    }

    @Override
    public void setCustom(List<ProjectCus> cList) {
        this.cList = cList;
    }

    private List<CompanyEntity> mCompanyEntities;
    private List<SupplierEntity> mSupplierEntities;

    @Override
    public void responseCompany(List<CompanyEntity> companyEntities) {
        mCompanyEntities = companyEntities;
    }

    @Override
    public void responseSupplier(List<SupplierEntity> supplierEntities) {
        mSupplierEntities = supplierEntities;
    }

    @Override
    public void sucess(String backUrl) {
        imageList.add(backUrl);
    }

    @Override
    public void failure(String message) {

    }

    @Override
    public void one_permission_isok(String permission_name) {

    }

    @Override
    public void one_permission_is_refuse(String permission_name) {
        requestPermissions();
    }


    @Override
    public void one_permission_is_refuse_no_prompt(String permission_name) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String photoPath;

        L.log("resultCode", "==" + resultCode);

//        if (uri != null) {
        if (requestCode == 1500) {//相机
//            uri = data.toUri(MediaStore.EXTRA_OUTPUT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
            } else {
                photoPath = uri.getEncodedPath();
            }

            if (uri != null) {
                Log.d("相机:", "requestCode=" + requestCode + "resultCode=" + resultCode);
                Log.d("相机返回真实路径:", RealPathFromUriUtils.compressImage(photoPath, AddIncomeActivity.this));
                picPahts.add(0, uri);

                L.log("picture", "======" + picPahts.toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                // 设置图片名字
                String key = "reimburse/images/" + sdf.format(new Date());

                //上传的文件名filename，上传的文件路径filePath
                updateOss(key, RealPathFromUriUtils.compressImage(photoPath, AddIncomeActivity.this));
                listAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showBottomToast(AddIncomeActivity.this, "获取图片失败-----" + resultCode);
            }


        } else if (requestCode == 234) {//相册
            if (null != data && data.getData() != null) {
                photoPath = RealPathFromUriUtils.getRealPathFromUri(AddIncomeActivity.this, data.getData());
                uri = data.getData();

                Log.d("相册图片路径:", photoPath);
                Log.d("相册返真实:", RealPathFromUriUtils.compressImage(photoPath, AddIncomeActivity.this));

                picPahts.add(0, uri);

                L.log("picture", "======" + picPahts.toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                // 设置图片名字
                String key = "reimburse/images/" + sdf.format(new Date());

                //上传的文件名filename，上传的文件路径filePath
                updateOss(key, photoPath);
                listAdapter.notifyDataSetChanged();
                //上传的文件名filename，上传的文件路径filePath
//                        updateOss(key, photoPath);
            } else {
                ToastUtils.showBottomToast(AddIncomeActivity.this, "获取图片失败");
            }


        }
//        } else {
//            picPahts.clear();
//            picPahts.add(0, uri);
//            ToastUtils.showBottomToast(AddReimburseActivity.this, "获取图片失败");
//        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
