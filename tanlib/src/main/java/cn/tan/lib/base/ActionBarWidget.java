package cn.tan.lib.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.tan.lib.R;
import cn.tan.lib.util.InputUtil;

public class ActionBarWidget extends FrameLayout implements View.OnClickListener{

	private View v_title_line;
	private TextView title_text;
	private TextView left_btn,right_btn;
	private FrameLayout action_bar_bg;
	public OnLeftButtonClickListener mOnLeftButtonClickListener;
	public OnRightButtonClickListener mOnRightButtonClickListener;
	private int vId;
	public interface OnLeftButtonClickListener {
		void onClick(View button);
	}
	public interface OnRightButtonClickListener {
		void onClick(View button);
	}
	public ActionBarWidget(Context context) {
		this(context, null);
	}
	public ActionBarWidget(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public ActionBarWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_action_bar, this, true);
		v_title_line=findViewById(R.id.v_title_line);
		title_text=(TextView) findViewById(R.id.action_bar_title_text);
		right_btn=(TextView) findViewById(R.id.actionbar_right_btn);
		left_btn=(TextView) findViewById(R.id.actionbar_left_btn);
		action_bar_bg= (FrameLayout) findViewById(R.id.action_bar_fl);
		left_btn.setOnClickListener(this);
		right_btn.setOnClickListener(this);
		left_btn.setClickable(false);
		right_btn.setClickable(false);
	}
	public ActionBarWidget setTitleLine(boolean isShow){
		v_title_line.setVisibility(isShow ? VISIBLE : GONE);
		return this;
	}
	public ActionBarWidget setTitle(String title) {
		title_text.setText(title);
		return this;
	}
	public ActionBarWidget setBackground(int Color){
		action_bar_bg.setBackgroundColor(Color);
		return this;
	}
	public ActionBarWidget setLeftButton(int stringID, OnLeftButtonClickListener listener) {
		left_btn.setClickable(true);
		left_btn.setText(stringID);
		left_btn.setVisibility(View.VISIBLE);
		mOnLeftButtonClickListener = listener;
		return this;
	}
	public ActionBarWidget setLeftButton(String text, OnLeftButtonClickListener listener) {
		left_btn.setClickable(true);
		left_btn.setText(text);
		left_btn.setVisibility(View.VISIBLE);
		mOnLeftButtonClickListener = listener;
		return this;
	}
	public ActionBarWidget setLeftButtonLeftIco(int id) {
		Drawable drawable=getResources().getDrawable(id);
		left_btn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
		return this;
	}
	public ActionBarWidget setRightButton(String stringID, OnRightButtonClickListener listener) {
		right_btn.setClickable(true);
		right_btn.setText(stringID);
		right_btn.setVisibility(View.VISIBLE);
		mOnRightButtonClickListener = listener;
		return this;
	}
	public ActionBarWidget setRightButtonTxt(String txt){
		right_btn.setText(txt);
		return this;
	}
	public ActionBarWidget setRightButtonLeftIco(int id) {
		Drawable drawable=getResources().getDrawable(id);
		right_btn.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
		return this;
	}
	public TextView getRightBtn(){
		return right_btn;
	}
	public static ActionBarWidget initActionBar(final View view, String title) {
		LinearLayout actionBarLayout = (LinearLayout) view.findViewWithTag(BaseRootLayout.ACTION_BAR_TAG);
		ActionBarWidget actionBarWidget=new ActionBarWidget(view.getContext());
		actionBarLayout.removeAllViews();
		actionBarLayout.setVisibility(View.VISIBLE);
		actionBarLayout.addView(actionBarWidget,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		actionBarWidget.setTitle(title);
		return actionBarWidget;
	}
	public static ActionBarWidget initActionBar(final AppCompatActivity activity, String title) {
		LinearLayout actionBarLayout = (LinearLayout) activity.getWindow().getDecorView().findViewWithTag(BaseRootLayout.ACTION_BAR_TAG);
		ActionBarWidget actionBarWidget=new ActionBarWidget(activity);
		actionBarLayout.removeAllViews();
		actionBarLayout.setVisibility(View.VISIBLE);
		actionBarLayout.addView(actionBarWidget, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		actionBarWidget.setTitle(title);
		setDefaultLeft(actionBarWidget,activity);
		return actionBarWidget;
	}
	public static void  setDefaultLeft(ActionBarWidget action_bar, final Activity activity){
		action_bar.setLeftButton("", new OnLeftButtonClickListener() {
			public void onClick(View button) {
				InputUtil.getInstance(activity).hideKeyboard();
				activity.finish();
			}
		}).setLeftButtonLeftIco(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
	}
	public void onClick(View v) {
		vId=v.getId();
		if(vId== R.id.actionbar_left_btn){
			if(mOnLeftButtonClickListener != null)
				mOnLeftButtonClickListener.onClick(v);
		}else if(vId== R.id.actionbar_right_btn){
			if(mOnRightButtonClickListener != null)
				mOnRightButtonClickListener.onClick(v);
		}
	}
}
