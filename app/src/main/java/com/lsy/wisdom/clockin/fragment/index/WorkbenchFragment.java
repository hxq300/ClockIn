package com.lsy.wisdom.clockin.fragment.index;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.ClockInActivity;
import com.lsy.wisdom.clockin.activity.CustomerActivity;
import com.lsy.wisdom.clockin.activity.LogActivity;
import com.lsy.wisdom.clockin.activity.PunchCardRecordActivity;
import com.lsy.wisdom.clockin.activity.RecordActivity;
import com.lsy.wisdom.clockin.activity.add.AddReimburseActivity;
import com.lsy.wisdom.clockin.activity.project.ProjectActivity;
import com.lsy.wisdom.clockin.activity.task.MyTaskActivity;
import com.lsy.wisdom.clockin.fragment.MyFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

/**
 * Created by lsy on 2020/5/6
 * todo :
 */
public class WorkbenchFragment extends MyFragment {

    @BindView(R.id.banner_pic)
    Banner bannerPic;
    private View view;

    private List<Integer> images = new ArrayList<>();

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_workbench, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBanner();
    }

    private void initBanner() {
        images.clear();
        images.add(R.mipmap.banner);
//        images.add(R.mipmap.banner0);
//        images.add(R.mipmap.banner1);
//        images.add(R.mipmap.banner2);
//        images.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2237069434,3717949658&fm=26&gp=0.jpg");
//        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=343743878,3804103777&fm=26&gp=0.jpg");
//        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2703222355,318514214&fm=26&gp=0.jpg");
//        images.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3183964165,3759807205&fm=26&gp=0.jpg");

        bannerPic.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                Intent intent =new Intent(StoreClassfityActivity.this, OtherActivity.class);
//                intent.putExtra("url",homeData.getData().getBanner().get(position).getLink_url());
//                startActivity(intent);
            }
        });

        //设置图片加载器
        bannerPic.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setImageResource((int) path);
            }
        });
        //banner指示器显示位置
//        bannerPic.setIndicatorGravity(BannerConfig.RIGHT);
        //设置图片集合
        bannerPic.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        bannerPic.start();
    }


    public static WorkbenchFragment newInstance() {
        Bundle args = new Bundle();
        WorkbenchFragment fragment = new WorkbenchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.message_voice, R.id.message_log, R.id.message_clock_in, R.id.message_approval,
            R.id.message_evection, R.id.message_extra_work, R.id.message_customer, R.id.message_leave,
            R.id.message_reimburse, R.id.message_purchase, R.id.message_conversion, R.id.message_project, R.id.message_task})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.message_voice://语音
                Toast.makeText(getContext(), "正在语音录入", Toast.LENGTH_SHORT).show();
                break;
            case R.id.message_log://日志
                Intent log = new Intent(getActivity(), LogActivity.class);
                getActivity().startActivity(log);
                break;
            case R.id.message_clock_in://打卡
                Intent clockIn = new Intent(getActivity(), ClockInActivity.class);
                getActivity().startActivity(clockIn);
                break;
            case R.id.message_approval://审批
                Intent shenpi = new Intent();
                shenpi.putExtra("type", 0);
                shenpi.setClass(getActivity(), RecordActivity.class);
                getActivity().startActivity(shenpi);
                break;
            case R.id.message_evection://出差
                Intent chucai = new Intent();
                chucai.putExtra("type", 1);
                chucai.setClass(getActivity(), RecordActivity.class);
                getActivity().startActivity(chucai);
                break;
            case R.id.message_extra_work://加班
                Intent jiaban = new Intent();
                jiaban.putExtra("type", 2);
                jiaban.setClass(getActivity(), RecordActivity.class);
                getActivity().startActivity(jiaban);
                break;
            case R.id.message_customer://客户
                Intent kehu = new Intent();
                kehu.putExtra("type", 3);
                kehu.setClass(getActivity(), CustomerActivity.class);
                getActivity().startActivity(kehu);
                break;
            case R.id.message_leave://请假
                Intent qingjia = new Intent();
                qingjia.putExtra("type", 4);
                qingjia.setClass(getActivity(), RecordActivity.class);
                getActivity().startActivity(qingjia);
                break;

            case R.id.message_reimburse://报销

                Intent baoxiao = new Intent();
                baoxiao.putExtra("type", 5);
                baoxiao.setClass(getActivity(), RecordActivity.class);
                getActivity().startActivity(baoxiao);
                startActivity(baoxiao);

                break;

            case R.id.message_purchase://采购

                Intent caigou = new Intent();
                caigou.putExtra("type", 6);
                caigou.setClass(getActivity(), RecordActivity.class);
                getActivity().startActivity(caigou);
                startActivity(caigou);

                break;

            case R.id.message_conversion://转正
                Intent zhuanzheng = new Intent();
                zhuanzheng.putExtra("type", 7);
                zhuanzheng.setClass(getActivity(), RecordActivity.class);
                getActivity().startActivity(zhuanzheng);
                startActivity(zhuanzheng);
                break;


            case R.id.message_project://项目
                Intent project = new Intent(getActivity(), ProjectActivity.class);
                startActivity(project);
                break;

            case R.id.message_task://任务
                Intent task = new Intent(getActivity(), MyTaskActivity.class);
                startActivity(task);
                break;


            default:
                break;
        }
    }
}
