package com.lsy.wisdom.clockin.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.fadai.particlesmasher.ParticleSmasher;
import com.fadai.particlesmasher.SmashAnimator;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.add.AddReimburseActivity;
import com.lsy.wisdom.clockin.mvp.PunchCardInterface;
import com.lsy.wisdom.clockin.mvp.PunchCardPresent;
import com.lsy.wisdom.clockin.mvp.append.AddInterface;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/8
 * todo : 打卡
 */
public class ClockInActivity extends AppCompatActivity implements PunchCardInterface.View, QuanXian.OnPermission, OssService.OssCallback {

    @BindView(R.id.clock_in_toolbar)
    IToolbar clockInToolbar;
    @BindView(R.id.bmapView)
    MapView bmapView;
    @BindView(R.id.tex_location_name)
    TextView texLocationName;

    private static final int BAIDU_READ_PHONE_STATE = 100;
    @BindView(R.id.edit_remark)
    EditText editRemark;
    @BindView(R.id.clock_click)
    Button clockClick;
    @BindView(R.id.add_recycler)
    RecyclerView addRecycler;

    private BaiduMap mBaiduMap;
    private LocationClient mlocationClient;
    private MylocationListener mlistener;
    private Context context;

    private double mLatitude;
    private double mLongitude;
    private float mCurrentX;

    PopupMenu popup = null;

    //自定义图标
    private BitmapDescriptor mIconLocation;

    //    private MyOrientationListener myOrientationListener;
    //定位图层显示方式
    private MyLocationConfiguration.LocationMode locationMode;

    //=====

    private SharedUtils sharedUtils;

    private PunchCardInterface.Presenter presenter;

    private String clockstatus = "10";

    private ParticleSmasher smasher;

    private CommonAdapter listAdapter;
    //
    private File cameraSavePath;//拍照照片路径
    private Uri uri;
    private ImageView openFile;

    //上传图片部分
    private List<String> imageList = new ArrayList<>();
    private List<Uri> picPahts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_clock_in);
        setSupportActionBar(clockInToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        try {
            initView();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //判断是否为Android 6.0 以上的系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            showLocMap();
        } else {
            initLocation();//initLocation为定位方法
        }

        //==========

        sharedUtils = new SharedUtils(ClockInActivity.this, SharedUtils.CLOCK);

        presenter = new PunchCardPresent(this, ClockInActivity.this);

        presenter.getStatus("" + OKHttpClass.getUserId(ClockInActivity.this));

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


    /**
     * 请求权限
     */
    private void requestPermissions() {
        QuanXian quanXian = new QuanXian(ClockInActivity.this, ClockInActivity.this);
        quanXian.setOnPermission_isok(this);
        quanXian.requestPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    private void initView() throws ExecutionException, InterruptedException {
        mBaiduMap = bmapView.getMap();
        mBaiduMap.clear();

        smasher = new ParticleSmasher(this);
        //根据给定增量缩放地图级别
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        mBaiduMap.setMapStatus(msu);
        MapStatus mMapStatus;//地图当前状态
        MapStatusUpdate mMapStatusUpdate;//地图将要变化成的状态
        mMapStatus = new MapStatus.Builder().overlook(-45).build();
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);


        getMyLocation();



        //===添加图片部分
        addRecycler.setItemViewCacheSize(100);
        addRecycler.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        addRecycler.setNestedScrollingEnabled(false);

        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");

        listAdapter = new CommonAdapter<Uri>(getApplicationContext(), R.layout.view_card_image, picPahts) {
            @Override
            protected void convert(ViewHolder holder, Uri uri, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
//                if (picPahts.size() - 1 == position) {
//                    image.setImageResource(R.mipmap.add_image);
//                } else {
                Glide.with(ClockInActivity.this).load(uri).into(image);
//                }
            }
        };

        addRecycler.setAdapter(listAdapter);

    }


    /**
     * 定位方法
     */
    private void initLocation() {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;

        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        mlocationClient = new LocationClient(this);
        mlistener = new MylocationListener();

        //注册监听器
        mlocationClient.registerLocationListener(mlistener);
        //配置定位SDK各配置参数，比如定位模式、定位时间间隔、坐标系类型等
        LocationClientOption mOption = new LocationClientOption();
        /**
         * 默认高精度，设置定位模式
         * LocationMode.Hight_Accuracy 高精度定位模式：这种定位模式下，会同时使用
         * LocationMode.Battery_Saving 低功耗定位模式：这种定位模式下，不会使用GPS，只会使用网络定位。
         * LocationMode.Device_Sensors 仅用设备定位模式：这种定位模式下，
         */
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        /**
         * 默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
         * 目前国内主要有以下三种坐标系：
         1. wgs84：目前广泛使用的GPS全球卫星定位系统使用的标准坐标系；
         2. gcj02：经过国测局加密的坐标；
         3. bd09：为百度坐标系，其中bd09ll表示百度经纬度坐标，bd09mc表示百度墨卡托米制坐标；
         * 在国内获得的坐标系类型可以是：国测局坐标、百度墨卡托坐标 和 百度经纬度坐标。
         在海外地区，只能获得WGS84坐标。请在使用过程中注意选择坐标。
         */
        //设置坐标类型
        mOption.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
        /**
         * 默认false，设置是否需要地址信息
         * 返回省、市、区、街道等地址信息，这个api用处很大，
         很多新闻类app会根据定位返回的市区信息推送用户所在市的新闻
         */
        mOption.setIsNeedAddress(true);
        /**
         * 默认false，设置是否需要位置语义化结果
         * 可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
         */
        mOption.setIsNeedLocationDescribe(true);
        //周边POI信息
        //获取位置附近的一些商场、饭店、银行等信息
        mOption.setIsNeedLocationPoiList(true);

        //设置是否打开gps进行定位
        mOption.setOpenGps(true);
        //设置扫描间隔，单位是毫秒，当<1000(1s)时，定时定位无效
        int span = 1000;
        mOption.setScanSpan(span);
        //设置 LocationClientOption
        mlocationClient.setLocOption(mOption);

        //初始化图标,BitmapDescriptorFactory是bitmap 描述信息工厂类.
        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_launcher_foreground);

//        myOrientationListener = new MyOrientationListener(context);
//        //通过接口回调来实现实时方向的改变
//        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
//            @Override
//            public void onOrientationChanged(float x) {
//                mCurrentX = x;
//            }
//        });

    }

    private void initBar() {

        Menu menu = clockInToolbar.getMenu();
        menu.clear();

        clockInToolbar.inflateMenu(R.menu.record_menu);
        clockInToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1://打卡记录
                        Intent clockIn = new Intent(ClockInActivity.this, PunchCardRecordActivity.class);
                        startActivity(clockIn);

                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (mlocationClient != null) {
            if (!mlocationClient.isStarted()) {
                mlocationClient.start();
            }
        }

//        myOrientationListener.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        if (mlocationClient != null) {
            mlocationClient.stop();
        }

//        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        bmapView.onDestroy();
        presenter.distory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        bmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        bmapView.onPause();
    }


    public void getMyLocation() {
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }


    @Override
    public void setStatus(String clockstatus) {
        this.clockstatus = clockstatus;
    }
    // 签到成功回调
    @Override
    public void setInId(int registration_id) {
        sharedUtils.setData(SharedUtils.CLOCKID, registration_id);
        playFromRawFile(this);
        smasher.with(clockClick)
                .setStyle(SmashAnimator.STYLE_DROP)    // 设置动画样式
                .setDuration(1500)                     // 设置动画时间
                .setStartDelay(300)                    // 设置动画前延时
                .setHorizontalMultiple(2)              // 设置横向运动幅度，默认为3
                .setVerticalMultiple(2)                // 设置竖向运动幅度，默认为4
                .addAnimatorListener(new SmashAnimator.OnAnimatorListener() {
                    @Override
                    public void onAnimatorStart() {
                        super.onAnimatorStart();
                        // 回调，动画开始
                    }

                    @Override
                    public void onAnimatorEnd() {
                        super.onAnimatorEnd();
                        // 回调，动画结束
                        smasher.reShowView(clockClick);
                    }
                })
                .start();

        clockClick.setText("打卡成功");
        presenter.getStatus("" + OKHttpClass.getUserId(ClockInActivity.this));
    }

    @Override
    public void setSuccess() {
        playFromRawFile(this);

        smasher.with(clockClick)
                .setStyle(SmashAnimator.STYLE_DROP)    // 设置动画样式
                .setDuration(1500)                     // 设置动画时间
                .setStartDelay(300)                    // 设置动画前延时
                .setHorizontalMultiple(2)              // 设置横向运动幅度，默认为3
                .setVerticalMultiple(2)                // 设置竖向运动幅度，默认为4
                .addAnimatorListener(new SmashAnimator.OnAnimatorListener() {
                    @Override
                    public void onAnimatorStart() {
                        super.onAnimatorStart();
                        // 回调，动画开始
                    }

                    @Override
                    public void onAnimatorEnd() {
                        super.onAnimatorEnd();
                        // 回调，动画结束
                        smasher.reShowView(clockClick);
                    }
                })
                .start();

        clockClick.setText("打卡成功");
//        ToastUtils.showBottomToast(this, "打卡成功啦");
    }

    /**
     * 播放来电和呼出铃声
     *
     * @param mContext
     */
    private void playFromRawFile(Context mContext) {
        //1.获取模式
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        final int ringerMode = am.getRingerMode();
        //2.普通模式可以呼叫普通模式： AudioManager.RINGER_MODE_NORMAL 静音模式：AudioManager.RINGER_MODE_VIBRATE 震动模式：AudioManager.RINGER_MODE_SILENT
        if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
            try {
                mPlayer = new MediaPlayer();
                AssetFileDescriptor file = mContext.getResources().openRawResourceFd(R.raw.success);
                try {
                    mPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                    file.close();
                    if (!mPlayer.isPlaying()) {
                        mPlayer.prepare();
                        mPlayer.start();
                        mPlayer.setLooping(false);//循环播放
                    }
                } catch (IOException e) {
                    mPlayer = null;
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

    }

    /**
     * 结束播放来电和呼出铃声
     */
    private void stopPlayFromRawFile() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = null;
    }

    private MediaPlayer mPlayer;

    /**
     * //     * 播放铃声
     * //
     */
//    private void startRing() {
//        if (mRingPlayer != null) {
//            mRingPlayer.stop();
//            mRingPlayer.release();
//            mRingPlayer = null;
//        }
//        mRingPlayer = MediaPlayer.create(this, R.raw.success);
//        mRingPlayer.setLooping(false);
//        mRingPlayer.start();
//
//    }
    @OnClick({R.id.get_location, R.id.clock_click, R.id.image_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_location:
                if (GeneralMethod.isFastClick()) {
                    texLocationName.setText("");
                    //判断是否为Android 6.0 以上的系统版本，如果是，需要动态添加权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        showLocMap();
                    } else {
                        initLocation();//initLocation为定位方法
                    }
                }

                break;
            case R.id.clock_click:

                L.log("commit", "" + texLocationName.getText().toString() + "----" + "" + mLongitude + "---" + mLatitude);
//                if (GeneralMethod.isFastClick()) {
//                    //（1签到   0签退）
//                    if (clockstatus.equals("1")) {
//                        //id(员工id),company_id(登录返回信息),in_address(打卡地址),remarkD(备注),longitude(经度),latitude(纬度)
//                        presenter.signIn(OKHttpClass.getUserId(ClockInActivity.this), sharedUtils.getIntData(SharedUtils.JITUANID), sharedUtils.getIntData(SharedUtils.COMPANYID), "俞泾港路  在金赢108金座附近",
//                                editRemark.getText().toString(), "" + mLongitude, "" + mLatitude, "" + imageList.toString());
//                    } else if (clockstatus.equals("0")) {
//                        //id(员工id),company_id(登录返回信息),out_address(打卡地址),remarkT(备注),longitude(经度),latitude(纬度),registration_id(打卡id,签到返回)
//                        presenter.signOut(OKHttpClass.getUserId(ClockInActivity.this), sharedUtils.getIntData(SharedUtils.JITUANID), sharedUtils.getIntData(SharedUtils.COMPANYID), texLocationName.getText().toString(),
//                                editRemark.getText().toString(), "121.472738", "31.272563", sharedUtils.getIntData(SharedUtils.CLOCKID), "" + imageList.toString());
//                    }

                if (GeneralMethod.isFastClick()) {
                    if (imageList.size() >= 1) {

                        L.log("上传数据");
                        if (clockstatus.equals("1")) {
                            L.log("打卡");
                            //id(员工id),company_id(登录返回信息),in_address(打卡地址),remarkD(备注),longitude(经度),latitude(纬度)
                            presenter.signIn(OKHttpClass.getUserId(ClockInActivity.this), sharedUtils.getIntData(SharedUtils.JITUANID), sharedUtils.getIntData(SharedUtils.COMPANYID), texLocationName.getText().toString(),
                                    editRemark.getText().toString(), "" + mLongitude, "" + mLatitude, "" + imageList.toString());
                        } else if (clockstatus.equals("0")) {
                            L.log("打卡");
                            //id(员工id),company_id(登录返回信息),out_address(打卡地址),remarkT(备注),longitude(经度),latitude(纬度),registration_id(打卡id,签到返回)
                            presenter.signOut(OKHttpClass.getUserId(ClockInActivity.this), sharedUtils.getIntData(SharedUtils.JITUANID), sharedUtils.getIntData(SharedUtils.COMPANYID), texLocationName.getText().toString(),
                                    editRemark.getText().toString(), "" + mLongitude, "" + mLatitude, sharedUtils.getIntData(SharedUtils.CLOCKID), "" + imageList.toString());
                        } else {
                            ToastUtils.showBottomToast(this, "状态不正确" + clockstatus);
                        }
                    } else {
                        ToastUtils.showBottomToast(this, "签到需要拍照");
                    }

                }

                break;
            case R.id.image_add:

                if (GeneralMethod.isFastClick()) {
                    goCamera();
                }

                break;

            default:
                break;
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
                Log.d("相机返回真实路径:", RealPathFromUriUtils.compressImage(photoPath, ClockInActivity.this));
                picPahts.add(0, uri);

                L.log("picture", "======" + picPahts.toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                // 设置图片名字
                String key = "reimburse/images/" + sdf.format(new Date());

                //上传的文件名filename，上传的文件路径filePath
                updateOss(key, RealPathFromUriUtils.compressImage(photoPath, ClockInActivity.this));
                listAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showBottomToast(ClockInActivity.this, "获取图片失败-----" + resultCode);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //激活相机操作
    private void goCamera() {
        uri = null;
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(ClockInActivity.this, "com.lsy.wisdom.clockin.fileprovider", cameraSavePath);
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
        OssService ossService = new OssService(ClockInActivity.this, "LTAI4Fjcn7J9c5aCVFTYabqE", "EuufkpKHommuLDd6EawJQac8togdPn", "http://oss-cn-shanghai.aliyuncs.com", "jjjt");
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
    }

    @Override
    public void failure(String message) {

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


    /**
     * 所有的定位信息都通过接口回调来实现
     */
    public class MylocationListener implements BDLocationListener {
        //定位请求回调接口
        private boolean isFirstIn = true;

        //定位请求回调函数,这里面会得到定位信息
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //BDLocation 回调的百度坐标类，内部封装了如经纬度、半径等属性信息
            //MyLocationData 定位数据,定位数据建造器
            /**
             * 可以通过BDLocation配置如下参数
             * 1.accuracy 定位精度
             * 2.latitude 百度纬度坐标
             * 3.longitude 百度经度坐标
             * 4.satellitesNum GPS定位时卫星数目 getSatelliteNumber() gps定位结果时，获取gps锁定用的卫星数
             * 5.speed GPS定位时速度 getSpeed()获取速度，仅gps定位结果时有速度信息，单位公里/小时，默认值0.0f
             * 6.direction GPS定位时方向角度
             * */
            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mCurrentX)//设定图标方向
                    .accuracy(bdLocation.getRadius())//getRadius 获取定位精度,默认值0.0f
                    .latitude(mLatitude)//百度纬度坐标
                    .longitude(mLongitude)//百度经度坐标
                    .build();
            //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
            mBaiduMap.setMyLocationData(data);
            //配置定位图层显示方式,三个参数的构造器
            /**
             * 1.定位图层显示模式
             * 2.是否允许显示方向信息
             * 3.用户自定义定位图标
             * */
            MyLocationConfiguration configuration
                    = new MyLocationConfiguration(locationMode, true, mIconLocation);
            //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
            mBaiduMap.setMyLocationConfigeration(configuration);
            //判断是否为第一次定位,是的话需要定位到用户当前位置
            if (isFirstIn) {
                //地理坐标基本数据结构
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //改变地图状态
                mBaiduMap.setMapStatus(msu);
                isFirstIn = false;

//                Toast.makeText(context, "您当前的位置为：" + bdLocation.getAddrStr(), Toast.LENGTH_LONG).show();
            }

            String addrStr = bdLocation.getAddrStr();
            int locationWhere = bdLocation.getLocationWhere();
            String buildingName = bdLocation.getBuildingName();
            Address address = bdLocation.getAddress();
            String street = bdLocation.getStreet();
            String describe = bdLocation.getLocationDescribe();
            List<Poi> poiList = bdLocation.getPoiList();

            if (describe != null) {
                texLocationName.setText("" + street + "  " + describe);
            } else if (poiList.size() > 0) {
                texLocationName.setText("" + poiList.get(0).getName());
            } else if (addrStr != null) {
                texLocationName.setText("" + addrStr);
            } else if (address != null) {
                texLocationName.setText("" + address);
            } else if (buildingName != null) {
                texLocationName.setText("" + address + buildingName);
            } else {
                texLocationName.setText("未知");
            }

//            String poiStr = "";
//            for (int i = 0; i < poiList.size(); i++) {
//                poiStr += "\n" + i + poiList.get(i).getName();
//            }
//
//
//            texLocationName.setText("定位回调:  addrStr: " + addrStr + ", \nlocationWhere: " + locationWhere + ", " +
//                    "\nbuildingName: " + buildingName + ", \ntaddress: " + address.address + ", \nstreet: " + street + ", \npoiList: " + poiStr + ",\n describe: " + describe);
//
//            Log.e("TAG", "定位回调:  addrStr: " + addrStr + ", locationWhere: " + locationWhere + ", " +
//                    "buildingName: " + buildingName + ", address: " + address + ", street: " + street + ", poiList: " + poiList.toString() + ", describe: " + describe);
//
//            for (int i = 0; i < poiList.size(); i++) {
//                L.log("location", "poiList" + i + "==" + poiList.get(i).getName());
//            }
//
//            L.log("lication", "bdLocation.getAddrStr()===" + bdLocation.getAddrStr() +
//                    "\tbdLocation.getBuildingName()" + bdLocation.getBuildingName() +
//                    "\tbdLocation.getFloor()" + bdLocation.getFloor()
//            );
        }
    }

    /**
     * Android 6.0 以上的版本的定位方法
     */
    public void showLocMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(ClockInActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE
            }, BAIDU_READ_PHONE_STATE);
        } else {
            initLocation();
        }
    }

    //Android 6.0 以上的版本申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initLocation();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}
