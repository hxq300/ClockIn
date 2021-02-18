package com.lsy.wisdom.clockin.activity.desc;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.UserData;
import com.lsy.wisdom.clockin.mvp.means.InformationInterface;
import com.lsy.wisdom.clockin.mvp.means.InformationPresent;
import com.lsy.wisdom.clockin.oss.OssService;
import com.lsy.wisdom.clockin.permission.QuanXian;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.RealPathFromUriUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/12
 * todo : 个人资料
 */
public class PersonalDataActivity extends AppCompatActivity implements InformationInterface.View, QuanXian.OnPermission, OssService.OssCallback {

    @BindView(R.id.personal_image)
    ImageView personalImage;
    @BindView(R.id.personal_name)
    TextView personalName;
    @BindView(R.id.personal_sex)
    TextView personalSex;
    @BindView(R.id.personal_age)
    TextView personalAge;
    @BindView(R.id.personal_section)
    TextView personalSection;
    @BindView(R.id.personal_phone)
    TextView personalPhone;
    @BindView(R.id.personal_signature)
    TextView personalSignature;
    @BindView(R.id.personal_toolbar)
    IToolbar personalToolbar;

    private boolean isEdit = false;

    //=======
    private InformationInterface.Presenter presenter;

    private String signature = null;

    //====
    //
    private File cameraSavePath;//拍照照片路径
    private Uri uri;
    private ImageView openFile;

    private String headImage = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        setSupportActionBar(personalToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        //========
        presenter = new InformationPresent(this, PersonalDataActivity.this);
        presenter.getInformation("" + OKHttpClass.getUserId(PersonalDataActivity.this));

        personalSignature.addTextChangedListener(textWatcher);


        //判断权限是否全部打开
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_APN_SETTINGS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getInformation("" + OKHttpClass.getUserId(PersonalDataActivity.this));
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        QuanXian quanXian = new QuanXian(PersonalDataActivity.this, PersonalDataActivity.this);
        quanXian.setOnPermission_isok(this);
        quanXian.requestPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.e("TextWatcher", "输入之前");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("TextWatcher", "正在输入");
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("TextWatcher", "输入之后，一般就用这个！！！");
            isEdit = true;
        }
    };

    private void initBar() {
        personalToolbar.inflateMenu(R.menu.toolbar_menu);
        personalToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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

    private String picture = "";

    @Override
    public void setInformation(UserData userData) {
        L.log("information", "information=" + userData.toString());

        if (!userData.getPicture().equals(picture)) {
            picture = userData.getPicture();
            Glide.with(PersonalDataActivity.this).load(RequestURL.OssUrl + userData.getPicture())
                    .error(R.mipmap.people_icon)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(personalImage);
        }


        if (userData.getStaff_name() != null) {
            personalName.setText("" + userData.getStaff_name());
        }

        if (userData.getStaff_sex() != null) {
            personalSex.setText("" + userData.getStaff_sex());
        }

        personalAge.setText("" + userData.getStaff_age());

        if (userData.getDepartment_name() != null) {
            personalSection.setText("" + userData.getDepartment_name());
        }

        if (userData.getStaff_phone() != null) {
            personalPhone.setText("" + userData.getStaff_phone());
        }

        if (userData.getSignature() != null) {
            personalSignature.setText("" + userData.getSignature());
            signature = userData.getSignature();
        }

    }

    @Override
    public void success() {
        L.log("information", "信息更改成功");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isEdit && personalSignature.getText() != null) {
            if (headImage != null) {
                presenter.updateInformation(OKHttpClass.getUserId(PersonalDataActivity.this), "" + headImage, null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.distory();
    }


    //自定义底部弹出框
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(PersonalDataActivity.this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(PersonalDataActivity.this, R.layout.dialog_custom_layout, null);
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
            uri = FileProvider.getUriForFile(PersonalDataActivity.this, "com.lsy.wisdom.clockin.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String photoPath;

        if (requestCode == 1500) {//相机
//            uri = data.toUri(MediaStore.EXTRA_OUTPUT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
            } else {
                photoPath = uri.getEncodedPath();
            }

            if (uri != null) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                // 设置图片名字
                String key = "people/headImage/" + sdf.format(new Date());
                Glide.with(PersonalDataActivity.this).load(photoPath)
                        .error(R.mipmap.people_icon)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(personalImage);
                updateOss(key, RealPathFromUriUtils.compressImage(photoPath, PersonalDataActivity.this));

            } else {
                ToastUtils.showBottomToast(PersonalDataActivity.this, "获取图片失败-----" + resultCode);
            }

        } else if (requestCode == 234) {//相册
            if (null != data && data.getData() != null) {
                photoPath = RealPathFromUriUtils.getRealPathFromUri(PersonalDataActivity.this, data.getData());
                uri = data.getData();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                // 设置图片名字
                String key = "people/headImage/" + sdf.format(new Date());
                Glide.with(PersonalDataActivity.this).load(photoPath)
                        .error(R.mipmap.people_icon)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(personalImage);
                updateOss(key, photoPath);
            } else {
                ToastUtils.showBottomToast(PersonalDataActivity.this, "获取图片失败");
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void one_permission_isok(String permission_name) {

    }

    @Override
    public void one_permission_is_refuse(String permission_name) {

    }

    @Override
    public void one_permission_is_refuse_no_prompt(String permission_name) {

    }


    //上传图片到oss
    private void updateOss(String filename, String filePath) {

        //初始化OssService类，参数分别是Content，accessKeyId，accessKeySecret，endpoint，bucketName（后4个参数是您自己阿里云Oss中参数）
        OssService ossService = new OssService(PersonalDataActivity.this, "LTAI4Fjcn7J9c5aCVFTYabqE", OssService.ACCESS_KEY_SECRET, "http://oss-cn-shanghai.aliyuncs.com", "jjjt");
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
        headImage = backUrl;
//        ToastUtils.showBottomToast(this, "图片上传成功" + backUrl);
        L.log("callback", "backUrl===" + backUrl);
        presenter.updateInformation(OKHttpClass.getUserId(PersonalDataActivity.this), "" + backUrl, "");

        Glide.with(PersonalDataActivity.this).load(RequestURL.OssUrl + backUrl)
                .error(R.mipmap.people_icon)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(personalImage);
    }

    @Override
    public void failure(String message) {
        L.log("callback", "failure essage===" + message);
    }

    @OnClick({R.id.personal_head, R.id.personal_signature})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.personal_head:
                showBottomDialog();
                break;
            case R.id.personal_signature:
                Intent intent = new Intent(PersonalDataActivity.this, EditInfoActivity.class);
                startActivity(intent);
                break;
        }
    }


//    @Override
//    public void sucess(String backUrl) {
//        headImage = backUrl;
//        ToastUtils.showBottomToast(PersonalDataActivity.this, "上传成功" + backUrl);

//    }
//
//    @Override
//    public void failure(String message) {
//        ToastUtils.showBottomToast(PersonalDataActivity.this, "图片上传失败" + message);
//    }
}
