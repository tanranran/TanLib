package cn.tan.lib.base;


import cn.tan.lib.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class EmptyLayout extends RelativeLayout implements OnClickListener{

	private LinearLayout ll_empty_loading;
	private LinearLayout ll_empty_error;
	private LinearLayout ll_empty_null;
	private ViewStub vs_empty_error;
	private ViewStub vs_empty_null;
	public EmptyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public EmptyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public EmptyLayout(Context context) {
		super(context);
		initView(context);
	}
	public void initView(Context context){
		inflate(context, R.layout.layout_empty, this);
		ll_empty_loading=(LinearLayout) findViewById(R.id.ll_empty_loading);
		ll_empty_error=(LinearLayout) findViewById(R.id.ll_empty_error);
		ll_empty_null=(LinearLayout) findViewById(R.id.ll_empty_null);
		vs_empty_error=(ViewStub) findViewById(R.id.vs_empty_error);
		vs_empty_null=(ViewStub) findViewById(R.id.vs_empty_null);
	}
	public EmptyLayout showError(){
		closeLoading();
		closeNull();
		showEmpty();
		vs_empty_error.inflate();
		return this;
	}
	public EmptyLayout closeError(){
		vs_empty_error.setVisibility(View.GONE);
		return this;
	}
	public EmptyLayout showLoading(){
		closeNull();
		closeError();
		showEmpty();
		ll_empty_loading.setVisibility(View.VISIBLE);
		return this;
	}
	public EmptyLayout closeLoading(){
		ll_empty_loading.setVisibility(View.GONE);
		return this;
	}
	public EmptyLayout showNull(){
		closeLoading();
		closeError();
		showEmpty();
		vs_empty_null.inflate();
		return this;
	}
	public EmptyLayout closeNull(){
		vs_empty_null.setVisibility(View.GONE);
		return this;
	}
	public EmptyLayout closeEmpty(){
		setVisibility(View.GONE);
		return this;
	}
	public EmptyLayout showEmpty(){
		setVisibility(View.VISIBLE);
		return this;
	} 
	public void onClick(View v) {
		
	}
}
