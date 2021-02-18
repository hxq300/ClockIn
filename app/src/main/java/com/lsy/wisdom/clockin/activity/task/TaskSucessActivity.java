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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.oss.OssService;
import com.lsy.wisdom.clockin.permission.QuanXian;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.RealPathFromUriUtils;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
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
 * Created by lsy on 2020/7/31
 * describe :  TODO
 */
public class TaskSucessActivity extends AppCompatActivity implements QuanXian.OnPermission, OssService.OssCallback {

    @BindView(R.id.suc_toolbar)
    IToolbar sucToolbar;
    @BindView(R.id.suc_zongjie)
    EditText sucZongjie;
    @BindView(R.id.suc_reason)
    EditText sucReason;
    @BindView(R.id.suc_recycler)
    RecyclerView sucRecycler;

    private int taskId = 0;


    private CommonAdapter listAdapter;

    private SharedUtils sharedUtils;

    //
    private File cameraSavePath;//拍照照片路径
    private Uri uri;

    //上传图片部分
    private List<String> imageList = new ArrayList<>();
    private List<Uri> picPahts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_sucess);
        setSupportActionBar(sucToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        Intent intent = getIntent();
        taskId = intent.getIntExtra("task_id", 0);

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
        sucRecycler.setItemViewCacheSize(100);
        sucRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        sucRecycler.setNestedScrollingEnabled(false);

        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");

        listAdapter = new CommonAdapter<Uri>(getApplicationContext(), R.layout.view_reimburse_image, picPahts) {
            @Override
            protected void convert(ViewHolder holder, Uri uri, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
                if (picPahts.size() - 1 == position) {
                    image.setImageResource(R.mipmap.add_image);
                } else {
                    Glide.with(TaskSucessActivity.this).load(uri).into(image);
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

        sucRecycler.setAdapter(listAdapter);
    }


    /**
     * 请求权限
     */
    private void requestPermissions() {
        QuanXian quanXian = new QuanXian(TaskSucessActivity.this, TaskSucessActivity.this);
        quanXian.setOnPermission_isok(this);
        quanXian.requestPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    @OnClick(R.id.suc_submit)
    public void onViewClicked() {
        if (GeneralMethod.isFastClick()) {
            sendSuc();
        }

    }


    private void sendSuc() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:id(任务id),task_summarize(任务总结),task_reason(申请理由(申请结束)),end_img(结束图片)
        listcanshu.put("id", "" + taskId);
        listcanshu.put("task_summarize", "" + sucZongjie.getText().toString());
        listcanshu.put("task_reason", "" + sucReason.getText().toString());
        listcanshu.put("end_img", "" + imageList.toString());

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(TaskSucessActivity.this, RequestURL.taskSuc, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
//                L.log("getPermission", OKHttpClass.getUserId(MyTaskActivity.this) + "----" + dataString);

                //{"message":"打卡成功","data":null,"code":200}
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        ToastUtils.showBottomToast(TaskSucessActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(TaskSucessActivity.this, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });

    }

    private void initBar() {
        Menu menu = sucToolbar.getMenu();
        menu.clear();

        sucToolbar.inflateMenu(R.menu.toolbar_menu);

        sucToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1:
                        break;

                    default:
                        L.log("munu", "新建跟进" + pos);
                        break;
                }
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
                Log.d("相机返回真实路径:", RealPathFromUriUtils.compressImage(photoPath, TaskSucessActivity.this));
                picPahts.add(0, uri);

                L.log("picture", "======" + picPahts.toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                // 设置图片名字
                String key = "reimburse/images/" + sdf.format(new Date());

                //上传的文件名filename，上传的文件路径filePath
                updateOss(key, RealPathFromUriUtils.compressImage(photoPath, TaskSucessActivity.this));
                listAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showBottomToast(TaskSucessActivity.this, "获取图片失败-----" + resultCode);
            }


        } else if (requestCode == 234) {//相册
            if (null != data && data.getData() != null) {
                photoPath = RealPathFromUriUtils.getRealPathFromUri(TaskSucessActivity.this, data.getData());
                uri = data.getData();

                Log.d("相册图片路径:", photoPath);
                Log.d("相册返真实:", RealPathFromUriUtils.compressImage(photoPath, TaskSucessActivity.this));

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
                ToastUtils.showBottomToast(TaskSucessActivity.this, "获取图片失败");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //自定义底部弹出框
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(TaskSucessActivity.this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(TaskSucessActivity.this, R.layout.dialog_custom_layout, null);
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
            uri = FileProvider.getUriForFile(TaskSucessActivity.this, "com.lsy.wisdom.clockin.fileprovider", cameraSavePath);
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
        OssService ossService = new OssService(TaskSucessActivity.this, "LTAI4Fjcn7J9c5aCVFTYabqE", OssService.ACCESS_KEY_SECRET, "http://oss-cn-shanghai.aliyuncs.com", "jjjt");
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
