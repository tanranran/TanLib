package cn.tan.lib.util;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.apache.http.impl.client.BasicCookieStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.tan.lib.base.BaseApplication;
import cn.tan.lib.common.Constant;


@SuppressWarnings("deprecation")
public class HttpUtils {
	private static final SyncHttpClient syncHttpClient = new SyncHttpClient();
	public final String GET = "GET";
	public final String POST = "POST";
	public RangeFileAsyncHttpResponseHandler handler;
	private String BASE_URL;
	private int timeout = 30000;
	private RequestParams params=new RequestParams();
	private String url;
	private boolean isParamsEncrypt = true;//是否加密 默认是true
	private String request_type = GET;
	private String request_text = "";
	private onDownLoader downLoader;
	public HttpUtils(String url) {
		super();
		syncHttpClient.setCookieStore(new BasicCookieStore());
		syncHttpClient.setConnectTimeout(3000);
		syncHttpClient.setResponseTimeout(6000);
		syncHttpClient.setMaxRetriesAndTimeout(3, 200);
		syncHttpClient.addHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING, "identity");//关闭Gzip
		this.url = url;
	}

	public HttpUtils setUrl(String url) {
		this.url = BASE_URL+url;
		return this;
	}

	public HttpUtils addParam(String name, String value) {
		params.put(name, value);
		return this;
	}

	public HttpUtils addParam(String name, int value) {
		params.put(name, value);
		return this;
	}

	public HttpUtils addParam(String name, InputStream stream) {
		params.put(name, stream);
		return this;
	}

	public HttpUtils addParam(String name, File stream) {
		try {
			params.put(name, stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return this;
	}

	public HttpUtils addParam(RequestParams params) {
		this.params = params;
		return this;
	}

	public HttpUtils setParamsEncrypt(boolean isParamsEncrypt) {
		this.isParamsEncrypt = isParamsEncrypt;
		return this;
	}

	public String getResponse() {
		if (url == null) {
			request_text=Constant.HTTP_URL_NULL;
		}else if (!NetUtils.isConnected(BaseApplication.getInstance())) {
			request_text=Constant.HTTP_NET_ERROR;
		}else{
			if (GET.equalsIgnoreCase(request_type)) {
				request_text = syncGet();
			} else {
				request_text = syncPost();
			}
		}
		Logger.d("HTPP请求结果" + request_text);
		return request_text;
	}

	private String syncGet() {
		syncHttpClient.get(url, syncRequest(), new AsyncHttpResponseHandler(Looper.getMainLooper()) {
			// 请求开始之前被调用
			public void onStart() {
				super.onStart();
			}

			// 当响应的HTTP状态为 200 OK
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				request_text = new String(response);
			}

			// 调用时，响应的HTTP状态为“4XX”（如401，403，404）
			public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
				Logger.e("HTPP请求失败statusCode:" + statusCode + " errorResponse:" + errorResponse + " Throwable:" + e.getMessage());
				request_text = statusCode + "";
			}

			// 调用时，请求重试
			public void onRetry(int retryNo) {
				super.onRetry(retryNo);
			}

			// 已经 总共
			public void onProgress(long bytesWritten, long totalSize) {
				Logger.d(String.format("Progress %d from %d (%2.0f%%)", bytesWritten, totalSize, (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1));
			}

			public void onCancel() {
				super.onCancel();
			}

			// 请求结束 成功/失败后的方法
			public void onFinish() {
				super.onFinish();
			}
		});
		return request_text;
	}

	/**
	 * 同步的post请求
	 *
	 */
	private String syncPost() {
		syncHttpClient.post(url, syncRequest(), new AsyncHttpResponseHandler(Looper.getMainLooper()) {
			// 请求开始之前被调用
			public void onStart() {
				super.onStart();
			}
			// 当响应的HTTP状态为 200 OK
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				request_text = new String(response);
			}

			// 调用时，响应的HTTP状态为“4XX”（如401，403，404）
			public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
				Logger.e("HTPP请求失败statusCode:" + statusCode + " errorResponse:" + errorResponse + " Throwable:" + e.getMessage());
				request_text = statusCode + "";
			}

			// 调用时，请求重试
			public void onRetry(int retryNo) {
				super.onRetry(retryNo);
			}

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				if (downLoader != null) {
					Logger.d(String.format("Progress %d from %d (%2.0f%%)", bytesWritten, totalSize, (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1));
					if (bytesWritten <= totalSize) {
						downLoader.onProgres(bytesWritten, totalSize);
					}
				}
			}
			// 请求结束 成功/失败后的方法
			public void onFinish() {
				super.onFinish();
			}
		});
		return request_text;
	}

	public void setOnDownLoader(onDownLoader downLoader) {
		this.downLoader = downLoader;
	}
	private RequestParams syncRequest() {
		RequestParams p = null;
		if (isParamsEncrypt) {
			p = paramsEncrypt(params);
		} else {
			p = params;
		}
		syncHttpClient.setTimeout(timeout);
		return p;
	}
	public HttpUtils setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}
	public RequestParams paramsEncrypt(RequestParams params) {
		RequestParams p = new RequestParams();
		p = params;
		p.put("platform", "Android");
		return p;
	}

	public AsyncHttpClient downloader(String url, String name) {
		Logger.d("下载地址" + url + "下载名称" + name);
		AsyncHttpClient client = new AsyncHttpClient();
		handler = new RangeFileAsyncHttpResponseHandler(new File(name)) {
			public void onStart() {
				if (downLoader != null) {
					downLoader.onStart();
				}
			}
			public void onSuccess(int statusCode, Header[] headers, File file) {
				if (downLoader != null) {
					downLoader.onSuccess(file);
				}
			}

			public void onFailure(int statusCode, Header[] headers, Throwable e, File file) {
				if (downLoader != null) {
					downLoader.onFailure(statusCode,headers,e,file);
				}
				Logger.d("下载失败" + "");
			}

			public void onProgress(long bytesWritten, long totalSize) {
				if (downLoader != null) {
					super.onProgress(bytesWritten, totalSize);
					if (bytesWritten <= totalSize) {
						downLoader.onProgres(bytesWritten, totalSize);
					}
				}
				Logger.d("下载进度" + bytesWritten + "/" + totalSize);
			}

			public void onCancel() {
				super.onCancel();
				if (downLoader != null) {
					downLoader.onCancel();
				}
				Logger.d("下载取消" + "");
			}

			public void onFinish() {
				super.onFinish();
				if (downLoader != null) {
					downLoader.onFinish();
				}
				Logger.d("下载结束" + "");
			}
		};
		client.get(url, handler);
		return client;
	}

	public interface onDownLoader {
		void onStart();

		void onSuccess(File file);

		void onProgres(long bytesWritten, long totalSize);

		void onFailure(int statusCode, Header[] headers, Throwable e, File file);

		void onCancel();

		void onFinish();
	}
}
