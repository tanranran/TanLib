package cn.tan.lib.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.tan.lib.R;
import cn.tan.lib.util.InputUtil;

/**
 * Created by tan on 2015-06-11.
 */
public class ToolBarWidget extends FrameLayout {

    public Toolbar tool_bar;
    public ToolBarWidget(Context context) {
        this(context, null);
    }

    public ToolBarWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolBarWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.layout_tool_bar,this);
        tool_bar= (Toolbar) findViewById(R.id.tool_bar);
    }
    public ToolBarWidget setTitle(String title) {
        tool_bar.setTitle(title);
        return this;
    }
    public static ToolBarWidget initToolBar(final AppCompatActivity activity, String title) {
        LinearLayout actionBarLayout = (LinearLayout) activity.getWindow().getDecorView().findViewWithTag(BaseRootLayout.ACTION_BAR_TAG);
        ToolBarWidget toolBarWidget=new ToolBarWidget(activity);
        actionBarLayout.removeAllViews();
        actionBarLayout.setVisibility(View.VISIBLE);
        actionBarLayout.addView(toolBarWidget,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        toolBarWidget.tool_bar.setTitle(title);
        activity.setSupportActionBar(toolBarWidget.tool_bar);
        toolBarWidget.tool_bar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolBarWidget.tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputUtil.getInstance(activity).hideKeyboard();
                activity.finish();
            }
        });
        return toolBarWidget;
    }
}
