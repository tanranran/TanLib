package com.tan.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import cn.tan.lib.base.BaseFragment;
import cn.tan.lib.base.EmptyLayout;

/**
 * Created by tan on 2015/8/4.
 */
public class FragmentS extends BaseFragment {

    private boolean mHasLoadedOnce = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return createView(inflater, container, R.layout.activity_main2);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        setUserVisibleHint(true);
        initView();
        super.onActivityCreated(savedInstanceState);
    }

    public void initView() {
        Logger.d("测试");
        loadingStart();
        emptyLayout.showEmpty();
        emptyLayout.showError();
        emptyLayout.setEmptyBtn(new EmptyLayout.OnClickEmptyBtnListener() {
            public void onClick(View v) {
                emptyLayout.showLoading();
                emptyLayout.postDelayed(new Runnable() {
                    public void run() {
                        emptyLayout.showError();
                    }
                }, 2000);
            }
        });
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (this.isVisible()) {
            if (isVisibleToUser && !mHasLoadedOnce) {
                initView();
                mHasLoadedOnce = true;
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
