package com.lsy.wisdom.clockin.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;

import com.lsy.wisdom.clockin.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsy on 2020/5/7
 * todo :
 */
public class IToolbar extends Toolbar {

    private Context context;
    private TextView mContent;
    private IToolbarCallback iToolbarCallback;
    //是否有返回键
    private boolean haseBack;
    //总MenuId
    private List<Integer> menuId;
    //显示MenuPos
    private List<Integer> showMenuPos;


    public interface IToolbarCallback {
        void onClickListener(int pos);
    }

    public void setIToolbarCallback(IToolbarCallback iToolbarCallback) {
        this.iToolbarCallback = iToolbarCallback;
    }

    public IToolbar(Context context) {
        this(context, null);
    }

    public IToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    @SuppressLint("RestrictedApi")
    public IToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        menuId = new ArrayList<>();
        showMenuPos = new ArrayList<>();
        if (getNavigationIcon() != null) {
            haseBack = true;
        }
        initView();
        //这里直接拿的系统的styleable
        TintTypedArray tta = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.Toolbar, defStyleAttr, 0);
        mContent.setTextColor(tta.getColor(R.styleable.Toolbar_titleTextColor, 0xff000000));
        mContent.setText(tta.getText(R.styleable.Toolbar_title));
        tta.recycle();
        initListener();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.itoolbar, this, false);
        mContent = view.findViewById(R.id.itoolbar_content);
        addView(view);
        setTitle("");//设置Toolbar自带的标题为空
        Toolbar.LayoutParams lp = (LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        this.setLayoutParams(lp);
    }

    private void initListener() {

        if (haseBack) {
            setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iToolbarCallback != null) {
                        iToolbarCallback.onClickListener(0);
                    }
                }
            });
        }
        if (getMenu() != null) {
            //如果设置了返回，则postion从1开始，否则从0开始
            final int menCount = haseBack ? 1 : 0;
            setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    for (int i = 0; i < menuId.size(); i++) {
                        for (int j = 0; j < showMenuPos.size(); j++) {
                            //判断id值和位置设置点击事件;
                            if ((i == showMenuPos.get(j)) && (item.getItemId() == menuId.get(i)) && (iToolbarCallback != null)) {
                                iToolbarCallback.onClickListener(j + menCount);
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public final void inflateMenu(int resId) {
        menuId.clear();
        showMenuPos.clear();
        xmlParser(resId);
        for (int i = 0; i < menuId.size(); i++) {
            showMenuPos.add(i);
        }
        super.inflateMenu(resId);
    }

    /**
     * 切换menu 输入的参数是显示的Menu文件中的从上之下的postion
     *
     * @param posArry
     */
    public final void initMenuView(int... posArry) {
        showMenuPos.clear();
        if (posArry != null && posArry.length > 0) {
            for (int i = 0; i < menuId.size(); i++) {
                for (int j = 0; j < posArry.length; j++) {
                    getMenu().findItem(menuId.get(i)).setVisible(false);
                    if (i == posArry[j]) {
                        getMenu().findItem(menuId.get(i)).setVisible(true);
                        showMenuPos.add(i);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 解析menu文件，获取idList;
     *
     * @param resId
     */
    private void xmlParser(int resId) {
        XmlResourceParser parser = getResources().getXml(resId);
        try {
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        int number = parser.getAttributeCount();
                        if (parser.getName().equals("item")) {
                            for (int i = 0; i < number; i++) {
                                if (parser.getAttributeName(i).equals("id")) {
                                    menuId.add(parser.getAttributeResourceValue(i, 0));
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否显示系统的Title,配合AppBarLayout做动画
     */
    public void showNativeTitle() {
        String content = mContent.getText().toString().trim();
        if (content != null) {
            setTitle(content);
            mContent.setVisibility(GONE);
        }
    }


    @Override
    public void setTitle(int resId) {
        super.setTitle(resId);
        if (mContent != null)
            mContent.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        if (mContent != null)
            mContent.setText(title);
    }

    @Override
    public void setTitleTextColor(int color) {
        super.setTitleTextColor(color);
        if (mContent != null)
            mContent.setTextColor(color);
    }

    public void removeTitleText() {
        if (mContent != null)
            mContent.setVisibility(GONE);
    }


}
