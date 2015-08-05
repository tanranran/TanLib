package cn.tan.lib.base;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.tan.lib.R;

public class EmptyLayout extends RelativeLayout implements OnClickListener{

	private LinearLayout ll_empty_loading;
	private Button btn_empty_error;
	private ViewStub vs_empty_error;
	private ViewStub vs_empty_null;
	private OnClickEmptyBtnListener btnListener;
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
		if (!isInEditMode()) {
			ll_empty_loading = (LinearLayout) findViewById(R.id.ll_empty_loading);
			vs_empty_error = (ViewStub) findViewById(R.id.vs_empty_error);
			vs_empty_null = (ViewStub) findViewById(R.id.vs_empty_null);
		}
	}

	public EmptyLayout showError() {//出错啦
		closeLoading();
		closeEmpty();
		showLayout();
		vs_empty_error.setVisibility(VISIBLE);
		if (btn_empty_error == null) {
			btn_empty_error = (Button) findViewById(R.id.btn_empty_error);
			btn_empty_error.setOnClickListener(this);
		}
		return this;
	}
	public EmptyLayout closeError(){
		vs_empty_error.setVisibility(View.GONE);
		return this;
	}

	public EmptyLayout showLoading() {//加载中
		closeEmpty();
		closeError();
		showLayout();
		ll_empty_loading.setVisibility(View.VISIBLE);
		return this;
	}
	public EmptyLayout closeLoading(){
		ll_empty_loading.setVisibility(View.GONE);
		return this;
	}

	public EmptyLayout showEmpty() {//暂时没有数据
		closeLoading();
		closeError();
		showLayout();
		vs_empty_null.setVisibility(VISIBLE);
		return this;
	}

	public EmptyLayout closeEmpty() {
		vs_empty_null.setVisibility(View.GONE);
		return this;
	}

	public EmptyLayout closeLayout() {
		setVisibility(View.GONE);
		return this;
	}

	public EmptyLayout showLayout() {
		setVisibility(View.VISIBLE);
		return this;
	}

	public void JudgeEmpty(int count) {
		if (count > 0) {
			closeEmpty();
		} else {
			showEmpty();
		}
	}

	public EmptyLayout setEmptyBtn(OnClickEmptyBtnListener btnListener) {
		this.btnListener = btnListener;
		return this;
	}

	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btn_empty_error) {
			if (btnListener != null)
				btnListener.onClick(v);

		} else {

		}
	}

	public interface OnClickEmptyBtnListener {
		void onClick(View v);
	}
}
