package com.lsy.wisdom.clockin.activity.add;

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
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.ProjectC;
import com.lsy.wisdom.clockin.bean.ProjectCus;
import com.lsy.wisdom.clockin.mvp.append.AddInterface;
import com.lsy.wisdom.clockin.mvp.append.AddPresent;
import com.lsy.wisdom.clockin.mvp.budding.BuddingInterface;
import com.lsy.wisdom.clockin.mvp.budding.BuddingPresent;
import com.lsy.wisdom.clockin.oss.OssService;
import com.lsy.wisdom.clockin.permission.QuanXian;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.RealPathFromUriUtils;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/13
 * todo : 添加报销
 */
public class AddReimburseActivity extends AppCompatActivity implements QuanXian.OnPermission, AddInterface.View, OssService.OssCallback, BuddingInterface.View {

    @BindView(R.id.add_reimburse_toolbar)
    IToolbar addReimburseToolbar;
    @BindView(R.id.add_recycler_reimburse)
    RecyclerView addRecyclerReimburse;
    @BindView(R.id.reimburse_type)
    TextView reimburseType;
    @BindView(R.id.reimburse_money)
    EditText reimburseMoney;
    @BindView(R.id.reimburse_content)
    EditText reimburseContent;
    @BindView(R.id.reimburse_num)
    TextView reimburseNum;
    @BindView(R.id.add_to)
    TextView addTo;
    @BindView(R.id.budding_cus)
    TextView buddingCus;
    @BindView(R.id.budding_util)
    TextView buddingUtil;

    private CommonAdapter listAdapter;

    //
    private File cameraSavePath;//拍照照片路径
    private Uri uri;

    //上传图片部分
    private List<String> imageList = new ArrayList<>();
    private List<Uri> picPahts = new ArrayList<>();

    private AddInterface.Presenter presenter;
    private BuddingInterface.Presenter buddingP;

    private SharedUtils sharedUtils;

    private String listIds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reimburse);
        setSupportActionBar(addReimburseToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        initView();

        sharedUtils = new SharedUtils(AddReimburseActivity.this, SharedUtils.CLOCK);

        listIds = sharedUtils.getData(SharedUtils.LISTID, "[]");

        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (nameIds != null) {
            addTo.setText("" + nameIds.substring(1, nameIds.length() - 1));
            addTo.setTextColor(0xff333333);
        }

        presenter = new AddPresent(this, AddReimburseActivity.this);
        buddingP = new BuddingPresent(this, AddReimburseActivity.this);


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
    }


    /**
     * 请求权限
     */
    private void requestPermissions() {
        QuanXian quanXian = new QuanXian(AddReimburseActivity.this, AddReimburseActivity.this);
        quanXian.setOnPermission_isok(this);
        quanXian.requestPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    private void initView() {

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
                    Glide.with(AddReimburseActivity.this).load(uri).into(image);
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
                Log.d("相机返回真实路径:", RealPathFromUriUtils.compressImage(photoPath, AddReimburseActivity.this));
                picPahts.add(0, uri);

                L.log("picture", "======" + picPahts.toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                // 设置图片名字
                String key = "reimburse/images/" + sdf.format(new Date());

                //上传的文件名filename，上传的文件路径filePath
                updateOss(key, RealPathFromUriUtils.compressImage(photoPath, AddReimburseActivity.this));
                listAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showBottomToast(AddReimburseActivity.this, "获取图片失败-----" + resultCode);
            }


        } else if (requestCode == 234) {//相册
            if (null != data && data.getData() != null) {
                photoPath = RealPathFromUriUtils.getRealPathFromUri(AddReimburseActivity.this, data.getData());
                uri = data.getData();

                Log.d("相册图片路径:", photoPath);
                Log.d("相册返真实:", RealPathFromUriUtils.compressImage(photoPath, AddReimburseActivity.this));

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
                ToastUtils.showBottomToast(AddReimburseActivity.this, "获取图片失败");
            }


        }
//        } else {
//            picPahts.clear();
//            picPahts.add(0, uri);
//            ToastUtils.showBottomToast(AddReimburseActivity.this, "获取图片失败");
//        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    private void initBar() {
        addReimburseToolbar.inflateMenu(R.menu.toolbar_menu);
        addReimburseToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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


    //自定义底部弹出框
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(AddReimburseActivity.this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(AddReimburseActivity.this, R.layout.dialog_custom_layout, null);
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
            uri = FileProvider.getUriForFile(AddReimburseActivity.this, "com.lsy.wisdom.clockin.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1500);
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


    /**
     * 判断输入完成情况
     */
    private boolean is_input() {

        if (reimburseType.getText().toString().trim().length() < 1) {
            Toast.makeText(AddReimburseActivity.this, "请选择报销类型", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (reimburseMoney.getText().toString().trim().length() < 1) {
            Toast.makeText(AddReimburseActivity.this, "请输入报销金额", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (reimburseContent.getText().toString().trim().length() < 1) {
            Toast.makeText(AddReimburseActivity.this, "请输入报销事由", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private List<String> gradeData; //自行添加一些string

    private OptionsPickerView pvOptions;
    private OptionsPickerView pvOptions1;
    private OptionsPickerView pvOptions2;


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

    @Override
    public void setFailure(String message) {
        ToastUtils.showBottomToast(this, "" + message);
    }

    @Override
    public void setSuccess() {
        ToastUtils.showBottomToast(this, "添加成功");
        finish();
    }


    //上传图片到oss
    private void updateOss(String filename, String filePath) {

        //初始化OssService类，参数分别是Content，accessKeyId，accessKeySecret，endpoint，bucketName（后4个参数是您自己阿里云Oss中参数）
        OssService ossService = new OssService(AddReimburseActivity.this, OssService.ACCESS_KEY_ID, OssService.ACCESS_KEY_SECRET, "http://oss-cn-shanghai.aliyuncs.com", "jjjt");
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
    public void sucess(String backUrl) {
        imageList.add(backUrl);
        L.log("callback", "backUrl===" + backUrl);
//        ToastUtils.showBottomToast(this, "上传图片成功");
//        setUpdatePersionData(backUrl, null);
    }

    @Override
    public void failure(String message) {
        L.log("callback", "failure essage===" + message);
    }

    @OnClick({R.id.line_type, R.id.check_line, R.id.reimburse_btn, R.id.budding_line, R.id.util_line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.line_type:
                initSelector();
                break;
            case R.id.check_line:
                Intent intent = new Intent(AddReimburseActivity.this, SubmitToActivity.class);
                startActivity(intent);
                break;
            case R.id.reimburse_btn:
                if (is_input()) {

                    L.log("budding", cId + "+" + pId);

                    presenter.addCoutentBX(cId, pId, sharedUtils.getData(SharedUtils.LISTID, "[]"), OKHttpClass.getUserId(this), OKHttpClass.getConglomerate(this), OKHttpClass.getToken(this), "报销",
                            reimburseContent.getText().toString(), reimburseType.getText().toString(),
                            Double.parseDouble(reimburseMoney.getText().toString()), "" + imageList.toString());
                }
                break;
            case R.id.budding_line://关联客户
                if (cList != null) {
                    initBuddingCus();
                } else {
                    buddingCus.setText("暂无");
                }
                break;
            case R.id.util_line://关联项目
                if (pList != null) {
                    initBuddingProject();
                } else {
                    buddingUtil.setText("暂无");
                }
                break;
            default:
                break;
        }
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
    protected void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.distory();
        }

        if (buddingP != null) {
            buddingP.distory();
        }
    }

    private List<ProjectC> pList = null;
    private List<ProjectCus> cList = null;

    private int pId = 0;
    private int cId = 0;

    @Override
    public void setProject(List<ProjectC> pList) {
        this.pList = pList;
    }

    @Override
    public void setCustom(List<ProjectCus> cList) {
        this.cList = cList;

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
            pId = pList.get(options1).getId();
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
            cId = cList.get(options1).getId();
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

}
