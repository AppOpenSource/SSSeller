package com.abt.seller.imgload;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class ImgLoad {
	private ExecutorService exeService;
	private ImgMemCache memCache;
	private ImgFileCache fileCache;
	private Map<String, ImageView> taskMap;
	private static ImgLoad instance;

	private ImgLoad() {
		// 初始化线程池有5条线程
		exeService = Executors.newFixedThreadPool(5);
		memCache = new ImgMemCache();
		fileCache = new ImgFileCache();
		taskMap = new HashMap<String, ImageView>();
	}

	public static ImgLoad getInstance() {
		if (instance == null) {
			instance = new ImgLoad();
		} // 单例模式
		return instance;
	}

	public void addTask(String url, ImageView img) {
		// 首先从内存中取出图片看是否为空
		Bitmap bmp = memCache.getBitmapFromCache(url);
		if (bmp != null) {
			img.setImageBitmap(bmp);
		} else {
			synchronized (taskMap) {
				taskMap.put(Integer.toString(img.hashCode()), img);
			}
		}
	}

	public void doTask() {
		synchronized (taskMap) {
			Collection<ImageView> con = taskMap.values();
			for (ImageView iv : con) {
				if (iv != null) {
					if (iv.getTag() != null) {
						loadImage((String) iv.getTag(), iv);
					}
				}
			}
			taskMap.clear();
		}
	}

	// 准备任务，防止前几张可视图片不显示
	public void prepare(int firstVisibleItem, int visibleItemCount) {
		if (firstVisibleItem >= visibleItemCount) return;
		int count = 0;
		synchronized (taskMap) {
			Collection<ImageView> con = taskMap.values();
			for (ImageView iv : con) {
				if (iv != null && count < visibleItemCount) {
					if (iv.getTag() != null) {
						loadImage((String) iv.getTag(), iv);
					}
				}
				count++;
			}
			taskMap.clear();
		}
	}

	private void loadImage(String url, ImageView img) {
		// 加入新的任务
		exeService.submit(new TaskWithResult(new TaskHandler(url, img), url));
	}

	private class TaskHandler extends Handler {
		String url;
		ImageView img;

		public TaskHandler(String url, ImageView img) {
			this.url = url;
			this.img = img;
		}

		@Override
		public void handleMessage(Message msg) {
			if (img.getTag().equals(url)) {
				if (msg.obj != null) {
					Bitmap bmp = (Bitmap) msg.obj;
					img.setImageBitmap(bmp);

					Animation anim = new AlphaAnimation(0, 1);
					anim.setDuration(1000);
					img.startAnimation(anim);
				}
			}
		}
	}

	private class TaskWithResult implements Callable<String> {
		private String url;
		private Handler handler;

		public TaskWithResult(Handler handler, String url) {
			this.url = url;
			this.handler = handler;
		}

		public String call() throws Exception {
			Message msg = handler.obtainMessage();
			msg.obj = getBitmap(url);
			if (msg.obj != null) {
				msg.sendToTarget();
			}
			return url;
		}
	}

	public Bitmap getBitmap(String url) {
		// 从内存缓存中获取图片
		Bitmap res = memCache.getBitmapFromCache(url);
		if (res == null) {
			// 文件缓存中获取
			res = fileCache.getImage(url);
			if (res == null) {
				// 从网络获取
				res = ImgFromHttp.downloadBitmap(url);
				if (res != null) {
					// 从网络获取成功后添加至内存缓存
					memCache.addBitmapToCache(url, res);
					// 将图片保存到SD卡
					fileCache.saveBmpToSd(res, url);
				}
			} else {
				// 从文件中读取成功后添加到内存缓存
				memCache.addBitmapToCache(url, res);
			}
		}
		return res;
	}
}