package com.lsy.wisdom.clockin.activity.task;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.lsy.wisdom.clockin.activity.add.AddCustomerlActivity;
import com.lsy.wisdom.clockin.activity.add.CheckPrincipalActivity;
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

/**
 * Created by lsy on 2020/7/28
 * todo : 新建日志
 */
public class AddTaskActivity extends AppCompatActivity implements QuanXian.OnPermission, OssService.OssCallback {

    @BindView(R.id.atask_toolbar)
    IToolbar ataskToolbar;
    @BindView(R.id.atask_title)
    EditText ataskTitle;
    @BindView(R.id.atask_desc)
    EditText ataskDesc;
    @BindView(R.id.add_recycler_task)
    RecyclerView addRecyclerTask;
    @BindView(R.id.principal_name)
    TextView principalName;
    @BindView(R.id.join_people)
    TextView joinPeople;
    @BindView(R.id.finish_time)
    TextView finishTime;
    @BindView(R.id.level_text)
    TextView levelText;


    private CommonAdapter listAdapter;

    private SharedUtils sharedUtils;

    //
    private File cameraSavePath;//拍照照片路径
    private Uri uri;

    //上传图片部分
    private List<String> imageList = new ArrayList<>();
    private List<Uri> picPahts = new ArrayList<>();

    private int principalId = 0;//负责人Id
    private String peopleId;
    private long timeFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setSupportActionBar(ataskToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        sharedUtils = new SharedUtils(AddTaskActivity.this, SharedUtils.CLOCK);

        principalId = sharedUtils.getIntData(SharedUtils.PRINCIPALID);

        String pName = sharedUtils.getData(SharedUtils.PRINCIPAL, null);
        if (pName != null) {
            principalName.setText("" + pName);
            principalName.setTextColor(0xff333333);
        }

        peopleId = sharedUtils.getData(SharedUtils.JOINID, "[]");

        String peoName = sharedUtils.getData(SharedUtils.JOINNAME, null);
        L.log("task", "+peoName");
        if (peoName != null && peoName.length() > 2) {
            joinPeople.setText("" + peoName.substring(1, peoName.length() - 1));
            joinPeople.setTextColor(0xff333333);
        }


        //判断权限是否全部打开
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_APN_SETTINGS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }

        initView();

    }

    private void initView() {
        picPahts.clear();
        picPahts.add(0, uri);
        //===添加图片部分
        addRecyclerTask.setItemViewCacheSize(100);
        addRecyclerTask.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        addRecyclerTask.setNestedScrollingEnabled(false);

        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");

        listAdapter = new CommonAdapter<Uri>(getApplicationContext(), R.layout.view_reimburse_image, picPahts) {
            @Override
            protected void convert(ViewHolder holder, Uri uri, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
                if (picPahts.size() - 1 == position) {
                    image.setImageResource(R.mipmap.add_image);
                } else {
                    Glide.with(AddTaskActivity.this).load(uri).into(image);
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

        addRecyclerTask.setAdapter(listAdapter);
    }


    /**
     * 请求权限
     */
    private void requestPermissions() {
        QuanXian quanXian = new QuanXian(AddTaskActivity.this, AddTaskActivity.this);
        quanXian.setOnPermission_isok(this);
        quanXian.requestPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        principalId = sharedUtils.getIntData(SharedUtils.PRINCIPALID);

        String pName = sharedUtils.getData(SharedUtils.PRINCIPAL, null);
        if (pName != null) {
            principalName.setText("" + pName);
            principalName.setTextColor(0xff333333);
        }

        peopleId = sharedUtils.getData(SharedUtils.JOINID, "[]");

        String peoName = sharedUtils.getData(SharedUtils.JOINNAME, null);
        if (peoName != null && peoName.length() > 2) {
            joinPeople.setText("" + peoName.substring(1, peoName.length() - 1));
            joinPeople.setTextColor(0xff333333);
        }

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
                Log.d("相机返回真实路径:", RealPathFromUriUtils.compressImage(photoPath, AddTaskActivity.this));
                picPahts.add(0, uri);

                L.log("picture", "======" + picPahts.toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                // 设置图片名字
                String key = "reimburse/images/" + sdf.format(new Date());

                //上传的文件名filename，上传的文件路径filePath
                updateOss(key, RealPathFromUriUtils.compressImage(photoPath, AddTaskActivity.this));
                listAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showBottomToast(AddTaskActivity.this, "获取图片失败-----" + resultCode);
            }


        } else if (requestCode == 234) {//相册
            if (null != data && data.getData() != null) {
                photoPath = RealPathFromUriUtils.getRealPathFromUri(AddTaskActivity.this, data.getData());
                uri = data.getData();

                Log.d("相册图片路径:", photoPath);
                Log.d("相册返真实:", RealPathFromUriUtils.compressImage(photoPath, AddTaskActivity.this));

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
                ToastUtils.showBottomToast(AddTaskActivity.this, "获取图片失败");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //自定义底部弹出框
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(AddTaskActivity.this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(AddTaskActivity.this, R.layout.dialog_custom_layout, null);
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


    private void initBar() {
        ataskToolbar.inflateMenu(R.menu.toolbar_menu);
        ataskToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void addTask() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        //传参:conglomerate_id(集团id),creator_id(创建人id),principal_id(负责人id),participant(参与人id数组),
        // task_title(任务标题),task_describe(任务描述),start_img(任务图片),end_timeC(结束时间戳),degree(紧急程度)
        listcanshu.put("creator_id", OKHttpClass.getUserId(AddTaskActivity.this));
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(AddTaskActivity.this));
        listcanshu.put("principal_id", principalId);
        listcanshu.put("participant", "" + peopleId);
        listcanshu.put("task_title", "" + ataskTitle.getText().toString());
        listcanshu.put("task_describe", "" + ataskDesc.getText().toString());
        listcanshu.put("start_img", "" + imageList.toString());//
        listcanshu.put("end_timeC", timeFinish);//
        listcanshu.put("degree", "" + levelText.getText().toString());//

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(AddTaskActivity.this, RequestURL.addTask, listcanshu);
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
                        ToastUtils.showBottomToast(AddTaskActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(AddTaskActivity.this, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
                return dataString;
            }
        });
    }

    @OnClick({R.id.principal_line, R.id.join_line, R.id.finish_line, R.id.log_commit, R.id.level_line})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.principal_line://负责人
                Intent intent = new Intent(AddTaskActivity.this, CheckPrincipalActivity.class);
                startActivity(intent);
                break;

            case R.id.join_line://选择参与人
                Intent people = new Intent(AddTaskActivity.this, PlayersActivity.class);
                startActivity(people);
                break;


            case R.id.finish_line://完成时间

                if (GeneralMethod.isFastClick()) {
                    TimePickerView pickerView = new TimePickerBuilder(AddTaskActivity.this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {//选中事件回调
                            timeFinish = date.getTime();
                            finishTime.setText("" + TimeUtils.getTimeYMD(date));
                        }
                    }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                            L.log("pvTime", "onTimeSelectChanged");
                        }
                    })
                            .setType(new boolean[]{true, true, true, false, false, false})
//                            .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                            .build();
                    pickerView.show();
                }

                break;

            case R.id.log_commit:
                if (GeneralMethod.isFastClick()) {
                    if (isInput()) {
                        addTask();
                    } else {
                        ToastUtils.showBottomToast(AddTaskActivity.this, "提交内容不能为空");
                    }
                }
                break;


            case R.id.level_line://紧急程度
                if (GeneralMethod.isFastClick()) {
                    initSelector();
                }
                break;


            default:
                break;
        }
    }

    /**
     * 判断输入完成情况
     */
    private boolean isInput() {

        if (ataskTitle.getText().toString().trim().length() < 1) {
            Toast.makeText(AddTaskActivity.this, "请输入任务标题", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private List<String> gradeData; //自行添加一些string

    private OptionsPickerView pvOptions;


    private void initSelector() {

        gradeData = new ArrayList<>();
        gradeData.add("重要且紧急");
        gradeData.add("重要不紧急");
        gradeData.add("紧急");
        gradeData.add("普通");

        pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            levelText.setText(gradeData.get(options1));
            // gradeData.get(options1)
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("紧急程度")
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
            uri = FileProvider.getUriForFile(AddTaskActivity.this, "com.lsy.wisdom.clockin.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1500);
    }


    //上传图片到oss
    private void updateOss(String filename, String filePath) {

        //初始化OssService类，参数分别是Content，accessKeyId，accessKeySecret，endpoint，bucketName（后4个参数是您自己阿里云Oss中参数）
        OssService ossService = new OssService(AddTaskActivity.this, "LTAI4Fjcn7J9c5aCVFTYabqE", OssService.ACCESS_KEY_SECRET, "http://oss-cn-shanghai.aliyuncs.com", "jjjt");
        //初始化OSSClient
        ossService.initOSSClient();

        ossService.setCallback(this);

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
    public void sucess(String backUrl) {
        imageList.add(backUrl);
        L.log("callback", "backUrl===" + backUrl);
    }

    @Override
    public void failure(String message) {
        ToastUtils.showBottomToast(this, "" + message);
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
}
