package com.lsy.wisdom.clockin.fragment.index;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.NoticeActivity;
import com.lsy.wisdom.clockin.activity.SysMessageActivity;
import com.lsy.wisdom.clockin.activity.SysSettingActivity;
import com.lsy.wisdom.clockin.fragment.MyFragment;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

/**
 * Created by lsy on 2020/5/6
 * todo :
 */
public class MessageFragment extends MyFragment {

    @BindView(R.id.message_toolbar)
    IToolbar messageToolbar;
    private View view;

    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_message, container, false);
            ((AppCompatActivity) getActivity()).setSupportActionBar(messageToolbar);
        }
        unbinder = ButterKnife.bind(this, view);
        initBar();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initBar() {

        Menu menu = messageToolbar.getMenu();
        menu.clear();

        messageToolbar.inflateMenu(R.menu.message_menu);
        messageToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        Log.v("TTT", "返回");
                        break;
                    case 1:
                        Log.v("TTT", "公告");
                        break;
                    case 2:
                        Log.v("TTT", "菜单2");
                        break;
                    case 3:
                        Log.v("TTT", "菜单3");
                        break;

                    default:
                        Log.v("TTT", "菜单333");
                        break;
                }
            }
        });
    }


    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.message_notice, R.id.message_wait_approval, R.id.message_sys})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.message_notice://公告
                Intent notice = new Intent(getActivity(), NoticeActivity.class);
                startActivity(notice);
                break;
            case R.id.message_wait_approval://等待审批
                break;
            case R.id.message_sys://系统消息
                Intent sysMessage = new Intent(getActivity(), SysMessageActivity.class);
                startActivity(sysMessage);
                break;
        }
    }
}
