package cn.tan.lib.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.event.EventBus;

/**
 * Created by tan on 2015/8/4.
 */
public class BaseFragment extends Fragment {
    public Activity context;
    public boolean isUserEvent = false;
    protected BaseRootLayout rootLayout;
    protected EmptyLayout emptyLayout;
    protected View rootView = null;

    public void onCreate(Bundle savedInstanceState) {
        context = getActivity();
        rootLayout = new BaseRootLayout(context);
        if (isUserEvent)
            EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    protected View createView(LayoutInflater inflater, ViewGroup container, int xml_layout) {
        if (rootView == null) {
            rootView = inflater.inflate(xml_layout, container, false);
            rootLayout.contentFrameLayout.removeAllViews();
            rootLayout.contentFrameLayout.addView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootLayout;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isUserEvent)
            EventBus.getDefault().register(this);
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (isUserEvent)
            EventBus.getDefault().unregister(this);
    }

    public void loadingStart() {
        if (emptyLayout == null) {
            emptyLayout = new EmptyLayout(context);
            rootLayout.contentFrameLayout.addView(emptyLayout);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    public void loadingComplete() {
        if (emptyLayout != null) {
            emptyLayout.closeLayout();
        }
    }

    public void setUserEvent(boolean isUserEvent) {
        this.isUserEvent = isUserEvent;
    }

}
