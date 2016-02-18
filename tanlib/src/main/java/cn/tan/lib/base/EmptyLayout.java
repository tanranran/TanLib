package cn.tan.lib.base;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.tan.lib.R;
import cn.tan.lib.util.StringUtils;

public class EmptyLayout extends FrameLayout implements View.OnClickListener {

	private View ll_empty_loading;
	private TextView btn_empty_error;
	private TextView btn_empty_null;
	private ViewStub vs_empty_error;
	private ViewStub vs_empty_null;
	private OnClickBtnListener btnErrorListener,btnEmptyListener;
	private TextView tv_empty_loading;
	private TextView tv_empty_error;
	private TextView tv_empty_null;
	private ImageView iv_empty_error;
	private ImageView iv_empty_null;
	private boolean clickShowLoading=true;
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

	public void initView(Context context) {
		inflate(context, R.layout.layout_empty, this);
		if (!isInEditMode()) {
			ll_empty_loading = findViewById(R.id.ll_empty_loading);
			vs_empty_error = (ViewStub) findViewById(R.id.vs_empty_error);
			vs_empty_null = (ViewStub) findViewById(R.id.vs_empty_null);
		}
	}

	public EmptyLayout setEmptyLoading(String str) {//设置加载中的文字提示信息
		if(tv_empty_loading!=null){
			tv_empty_loading= (TextView) findViewById(R.id.tv_empty_loading);
		}
		assert tv_empty_loading != null;
		tv_empty_loading.setText(str);
		return this;
	}
	public EmptyLayout setErrorMessage(String errorMessage){//设置错误页面 错误的提示信息
		vs_empty_error.setVisibility(VISIBLE);
		if(tv_empty_error==null){
			tv_empty_error= (TextView) findViewById(R.id.tv_empty_error);
		}
		tv_empty_error.setText(errorMessage);
		return this;
	}
	public EmptyLayout setErrorIco(int res){
		vs_empty_error.setVisibility(VISIBLE);
		if(iv_empty_error==null){
			iv_empty_error= (ImageView) findViewById(R.id.iv_empty_error);
		}
		iv_empty_error.setImageResource(res);
		return  this;
	}
	public EmptyLayout setErrorBtn(String errorBtn){
		vs_empty_error.setVisibility(VISIBLE);
		if(btn_empty_error==null){
			btn_empty_error= (TextView) findViewById(R.id.btn_empty_error);
		}
		btn_empty_error.setText(errorBtn);
		return  this;
	}
	public EmptyLayout showError(OnClickBtnListener btnErrorListener) {//错误页面显示以及监听错误按钮
		try{
			vs_empty_error.setVisibility(VISIBLE);
			if(btn_empty_error==null){
				btn_empty_error= (TextView) findViewById(R.id.btn_empty_error);
			}
			this.btnErrorListener=btnErrorListener;
			btn_empty_error.setOnClickListener(this);
		}catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public EmptyLayout showError(String errorMessage,OnClickBtnListener btnErrorListener) {//错误页面显示以及监听错误按钮
		showError(true, R.mipmap.error, errorMessage, getResources().getString(R.string.empty_error_btn), btnErrorListener);
		return this;
	}
	public EmptyLayout showError(boolean clickShowLoading,int errorRes,String errorMessage,String errorBtn,OnClickBtnListener btnErrorListener) {//错误页面显示以及监听错误按钮
		this.clickShowLoading=clickShowLoading;
		vs_empty_error.setVisibility(VISIBLE);
		closeLoading();
		closeEmpty();
		showLayout();
		setErrorBtn(errorBtn);
		showError(btnErrorListener);
		setErrorIco(errorRes);
		setErrorMessage(errorMessage);
		return this;
	}
	public EmptyLayout closeError() {//关闭错误页面
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

	public EmptyLayout closeLoading() {//关闭加载中
		ll_empty_loading.setVisibility(View.GONE);
		return this;
	}
	public EmptyLayout setEmptyIco(int res){
		vs_empty_null.setVisibility(VISIBLE);
		if(iv_empty_null==null){
			iv_empty_null= (ImageView) findViewById(R.id.iv_empty_null);
		}
		iv_empty_null.setImageResource(res);
		return this;
	}
	public EmptyLayout setEmptyMessage(String txt){
		vs_empty_null.setVisibility(VISIBLE);
		if(tv_empty_null==null){
			tv_empty_null= (TextView) findViewById(R.id.tv_empty_null);
		}
		tv_empty_null.setText(txt);
		return this;
	}
	public EmptyLayout showEmpty() {//暂时没有数据
		closeLoading();
		closeError();
		showLayout();
		vs_empty_null.setVisibility(VISIBLE);
		return this;
	}

	public EmptyLayout closeEmpty() {//关闭暂时没有数据
		vs_empty_null.setVisibility(View.GONE);
		return this;
	}

	public EmptyLayout closeLayout() {//关闭整个页面
		setVisibility(View.GONE);
		return this;
	}

	public EmptyLayout showLayout() {//显示整个页面
		setVisibility(View.VISIBLE);
		return this;
	}

	public void JudgeEmpty(int count,int res,String message,String btnTxt,OnClickBtnListener btnEmptyListener) {//判断是关闭页面还是显示空页面
		if (count > 0) {
			closeLayout();
		} else {
			showEmpty();
			setEmptyIco(res);
			setEmptyMessage(message);
			this.btnEmptyListener = btnEmptyListener;
			if(!StringUtils.isEmpty(btnTxt)&&btnEmptyListener!=null){
				btn_empty_null= (TextView) findViewById(R.id.btn_empty_null);
				btn_empty_null.setVisibility(VISIBLE);
				btn_empty_null.setText(btnTxt);
				btn_empty_null.setOnClickListener(this);
			}
		}
	}
	public void JudgeEmpty(int count,int res) {//判断是关闭页面还是显示空页面
		JudgeEmpty(count,res,getResources().getString(R.string.empty_null),null,null);
	}
	public void JudgeEmpty(int count) {//判断是关闭页面还是显示空页面
		JudgeEmpty(count,R.mipmap.error,getResources().getString(R.string.empty_null),null,null);
	}
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btn_empty_error) {
			if (btnErrorListener != null) {
				if(clickShowLoading){
					showLoading();
				}
				btnErrorListener.onClick(v);
			}
		} else if(i == R.id.btn_empty_null){
			if(btnEmptyListener!=null){
				btnEmptyListener.onClick(v);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	public interface OnClickBtnListener {
		void onClick(View v);
	}
}
