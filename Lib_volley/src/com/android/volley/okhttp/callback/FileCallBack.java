package com.android.volley.okhttp.callback;

import com.android.volley.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

public abstract class FileCallBack extends Callback<File> {
	/**
	 * 目标文件存储的文件夹路径
	 */
	private String destFileDir;
	/**
	 * 目标文件存储的文件名
	 */
	private String destFileName;


	public FileCallBack(String destFileDir, String destFileName) {
		this.destFileDir = destFileDir;
		this.destFileName = destFileName;
	}


	@Override
	public File parseNetworkResponse(Response response, int id) throws Exception {
		return saveFile(response, id);
	}


	public File saveFile(Response response, final int id) throws IOException {
		InputStream is = null;

		FileOutputStream fos = null;
		try {
			is = response.body().byteStream();
			final long total = response.body().contentLength();
			int bufSize = 2048;
			//根据文件大小改变数组大小
			if (total > 1024 * 1024 * 5) {//5M
				bufSize = 1024 * 8;
			} else if (total > 1024 * 1024) {//1M
				bufSize = 1024 * 4;
			}
			byte[] buf = new byte[bufSize];
			int len = 0;
			long sum = 0;
			File dir = new File(destFileDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, destFileName);
			fos = new FileOutputStream(file);
			while ((len = is.read(buf)) != -1) {
				sum += len;
				fos.write(buf, 0, len);
				final long finalSum = sum;
				OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
					@Override
					public void run() {
						inProgress(finalSum * 1.0f / total, total, id);
					}
				});
			}
			fos.flush();
			return file;
		} finally {
			try {
				response.body().close();
				if (is != null) is.close();
			} catch (IOException e) {
			}
			try {
				if (fos != null) fos.close();
			} catch (IOException e) {
			}

		}
	}


}
