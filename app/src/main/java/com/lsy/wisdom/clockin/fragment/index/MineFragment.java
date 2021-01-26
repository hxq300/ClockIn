package com.lsy.wisdom.clockin.fragment.index;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.DocActivity;
import com.lsy.wisdom.clockin.activity.SysSettingActivity;
import com.lsy.wisdom.clockin.activity.add.FeedbackActivity;
import com.lsy.wisdom.clockin.activity.desc.PersonalDataActivity;
import com.lsy.wisdom.clockin.bean.UserData;
import com.lsy.wisdom.clockin.fragment.MyFragment;
import com.lsy.wisdom.clockin.mvp.means.InformationInterface;
import com.lsy.wisdom.clockin.mvp.means.InformationPresent;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

/**
 * Created by lsy on 2020/5/6
 * todo :
 */
public class MineFragment extends MyFragment implements InformationInterface.View {

    @BindView(R.id.mine_image)
    ImageView mineImage;
    @BindView(R.id.mine_name)
    TextView mineName;
    @BindView(R.id.mine_remark)
    TextView mineRemark;
    private View view;

    private Unbinder unbinder;

    //=======
    private InformationInterface.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //========
        presenter = new InformationPresent(this, getActivity());
        presenter.getInformation("" + OKHttpClass.getUserId(getActivity()));
    }


    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.mine_message, R.id.mine_information, R.id.mine_feedback, R.id.mine_settings, R.id.mine_doc})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.mine_message://个人资料

            case R.id.mine_information://个人资料
                Intent personal = new Intent(getActivity(), PersonalDataActivity.class);
                startActivity(personal);
                break;
            case R.id.mine_feedback://意见反馈
                Intent feedBack = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(feedBack);
                break;
            case R.id.mine_settings:
                Intent setting = new Intent(getActivity(), SysSettingActivity.class);
                startActivity(setting);
                break;
            case R.id.mine_doc://文档
                Intent doc = new Intent(getActivity(), DocActivity.class);
                startActivity(doc);
                break;
        }
    }

    @Override
    public void setInformation(UserData userData) {

        if (userData != null) {
            if (userData.getPicture() != null) {
                Glide.with(getActivity()).load(RequestURL.OssUrl + userData.getPicture())
                        .error(R.mipmap.people_icon)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(mineImage);
            }

            if (userData.getStaff_name() != null) {
                mineName.setText("" + userData.getStaff_name());
            }

            if (userData.getSignature() != null) {
                mineRemark.setText("" + userData.getSignature());
            }
        }

    }

    @Override
    public void success() {

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getInformation("" + OKHttpClass.getUserId(getActivity()));
    }
}
