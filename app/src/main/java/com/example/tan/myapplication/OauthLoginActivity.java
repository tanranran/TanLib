package com.example.tan.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import cn.tan.lib.base.BaseActivity;
import cn.tan.lib.util.HttpUtils;

/**
 * Created by tan on 2015-06-01.
 */
public class OauthLoginActivity extends BaseActivity {
    private int mThemeId = R.style.Theme_Basic;
    private TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(mThemeId);
        setContentView(R.layout.activity_oauth_login);
        toorBar("4214124");
        initView();
        initData();
    }
    public void initData(){
        textView.setText("fsafasfasfsafsa");
    }
    public void initView() {
        this.setTheme(mThemeId);
        rootLayout.invalidate();
        textView=getView(R.id.tv_qq);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_qq:
                new Thread(new Runnable() {
                    public void run() {
                        long a=System.currentTimeMillis();
                        new HttpUtils("http://www.baidu.com").getResponse();
                        Logger.d("时间耗时1:" + (System.currentTimeMillis() - a));
                    }
                }).start();
                break;
            case R.id.tv_sina:
                if(mThemeId==R.style.Theme_Basic){
                    mThemeId=R.style.Theme_Basic_Night;
                }else{
                    mThemeId=R.style.Theme_Basic;
                }
                initView();
                break;
        }
    }
}
