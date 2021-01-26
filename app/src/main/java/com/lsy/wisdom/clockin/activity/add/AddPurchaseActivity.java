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
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.ProjectC;
import com.lsy.wisdom.clockin.mvp.append.AddInterface;
import com.lsy.wisdom.clockin.mvp.append.AddPresent;
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

import org.json.JSONArray;
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
 * Created by lsy on 2020/5/13
 * todo : 添加采购
 */
public class AddPurchaseActivity extends AppCompatActivity implements AddInterface.View, QuanXian.OnPermission, OssService.OssCallback {


    @BindView(R.id.add_purchase_toolbar)
    IToolbar addPurchaseToolbar;
    @BindView(R.id.reimburse_type)
    TextView reimburseType;
    @BindView(R.id.purchase_content)
    EditText purchaseContent;
    @BindView(R.id.purchase_num)
    TextView purchaseNum;
    @BindView(R.id.purchase_money)
    EditText purchaseMoney;
    @BindView(R.id.add_to)
    TextView addTo;
    @BindView(R.id.add_recycler_purchase)
    RecyclerView addRecyclerPurchase;
    @BindView(R.id.check_line)
    LinearLayout checkLine;
    @BindView(R.id.purchase_btn)
    Button purchaseBtn;
    @BindView(R.id.budding_util)
    TextView buddingUtil;
    private AddInterface.Presenter presenter;

    private SharedUtils sharedUtils;

    private String listIds;

    private CommonAdapter listAdapter;

    //
    private File cameraSavePath;//拍照照片路径
    private Uri uri;

    //上传图片部分
    private List<String> imageList = new ArrayList<>();
    private List<Uri> picPahts = new ArrayList<>();

    private List<ProjectC> pList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);
        setSupportActionBar(addPurchaseToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

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

        sharedUtils = new SharedUtils(AddPurchaseActivity.this, SharedUtils.CLOCK);

        listIds = sharedUtils.getData(SharedUtils.LISTID, "[]");

        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (nameIds != null) {
            addTo.setText("" + nameIds.substring(1, nameIds.length() - 1));
            addTo.setTextColor(0xff333333);
        }

        presenter = new AddPresent(this, AddPurchaseActivity.this);
        getProject();


    }


    /**
     * 请求权限
     */
    private void requestPermissions() {
        QuanXian quanXian = new QuanXian(AddPurchaseActivity.this, AddPurchaseActivity.this);
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
        addRecyclerPurchase.setItemViewCacheSize(100);
        addRecyclerPurchase.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        addRecyclerPurchase.setNestedScrollingEnabled(false);

        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");

        listAdapter = new CommonAdapter<Uri>(getApplicationContext(), R.layout.view_reimburse_image, picPahts) {
            @Override
            protected void convert(ViewHolder holder, Uri uri, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
                if (picPahts.size() - 1 == position) {
                    image.setImageResource(R.mipmap.add_image);
                } else {
                    Glide.with(AddPurchaseActivity.this).load(uri).into(image);
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

        addRecyclerPurchase.setAdapter(listAdapter);


        purchaseContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                purchaseNum.setText(editable.length() + "/200");
            }
        });

    }


    private void initBar() {
        addPurchaseToolbar.inflateMenu(R.menu.toolbar_menu);
        addPurchaseToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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


    /**
     * 判断输入完成情况
     */
    private boolean is_input() {

        if (reimburseType.getText().toString().trim().length() < 1) {
            Toast.makeText(AddPurchaseActivity.this, "请选择报销类型", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (purchaseMoney.getText().toString().trim().length() < 1) {
            Toast.makeText(AddPurchaseActivity.this, "请输入报销金额", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (purchaseContent.getText().toString().trim().length() < 1) {
            Toast.makeText(AddPurchaseActivity.this, "请输入报销事由", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private List<String> gradeData; //自行添加一些string

    private OptionsPickerView pvOptions;


    private void initSelector() {

        gradeData = new ArrayList<>();
        gradeData.add("机器设备");
        gradeData.add("办公用品");
        gradeData.add("活动用品");
        gradeData.add("材料项");
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
                Log.d("相机返回真实路径:", RealPathFromUriUtils.compressImage(photoPath, AddPurchaseActivity.this));
                picPahts.add(0, uri);

                L.log("picture", "======" + picPahts.toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                // 设置图片名字
                String key = "reimburse/images/" + sdf.format(new Date());

                //上传的文件名filename，上传的文件路径filePath
                updateOss(key, RealPathFromUriUtils.compressImage(photoPath, AddPurchaseActivity.this));
                listAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showBottomToast(AddPurchaseActivity.this, "获取图片失败-----" + resultCode);
            }


        } else if (requestCode == 234) {//相册
            if (null != data && data.getData() != null) {
                photoPath = RealPathFromUriUtils.getRealPathFromUri(AddPurchaseActivity.this, data.getData());
                uri = data.getData();

                Log.d("相册图片路径:", photoPath);
                Log.d("相册返真实:", RealPathFromUriUtils.compressImage(photoPath, AddPurchaseActivity.this));

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
                ToastUtils.showBottomToast(AddPurchaseActivity.this, "获取图片失败");
            }


        }
//        } else {
//            picPahts.clear();
//            picPahts.add(0, uri);
//            ToastUtils.showBottomToast(AddReimburseActivity.this, "获取图片失败");
//        }


        super.onActivityResult(requestCode, resultCode, data);
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

    @OnClick({R.id.line_type, R.id.check_line, R.id.purchase_btn, R.id.util_line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.line_type:
                initSelector();
                break;
            case R.id.check_line:
                Intent intent = new Intent(AddPurchaseActivity.this, SubmitToActivity.class);
                startActivity(intent);
                break;
            case R.id.purchase_btn:
                if (is_input()) {
                    presenter.addCoutentCG(sharedUtils.getData(SharedUtils.LISTID, "[]"), OKHttpClass.getUserId(this), OKHttpClass.getConglomerate(this), OKHttpClass.getToken(this), "采购",
                            purchaseContent.getText().toString(), reimburseType.getText().toString(),
                            Double.parseDouble(purchaseMoney.getText().toString()), "" + imageList.toString(), pId);
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

    private OptionsPickerView pvOptions1;
    private int pId = 0;

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


    @Override
    public void sucess(String backUrl) {
        imageList.add(backUrl);
        L.log("callback", "backUrl===" + backUrl);
    }

    @Override
    public void failure(String message) {
        L.log("callback", "failure essage===" + message);
    }


    //自定义底部弹出框
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(AddPurchaseActivity.this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(AddPurchaseActivity.this, R.layout.dialog_custom_layout, null);
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
            uri = FileProvider.getUriForFile(AddPurchaseActivity.this, "com.lsy.wisdom.clockin.fileprovider", cameraSavePath);
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


    //上传图片到oss
    private void updateOss(String filename, String filePath) {

        //初始化OssService类，参数分别是Content，accessKeyId，accessKeySecret，endpoint，bucketName（后4个参数是您自己阿里云Oss中参数）
        OssService ossService = new OssService(AddPurchaseActivity.this, "LTAI4Fjcn7J9c5aCVFTYabqE", "EuufkpKHommuLDd6EawJQac8togdPn", "http://oss-cn-shanghai.aliyuncs.com", "jjjt");
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


    public void getProject() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(AddPurchaseActivity.this));
        listcanshu.put("staff_id", OKHttpClass.getUserId(AddPurchaseActivity.this));

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(AddPurchaseActivity.this, RequestURL.bindingProject, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("buddingProject", "" + dataString);
                Gson gson = new Gson();

                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    pList = new ArrayList<>();

                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");

                    jsonArray = new JSONArray(data);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProjectC projectC = gson.fromJson(jsonArray.get(i).toString(), ProjectC.class);
                        pList.add(projectC);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }


}
