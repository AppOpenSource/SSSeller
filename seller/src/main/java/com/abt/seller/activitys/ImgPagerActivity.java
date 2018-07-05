package com.abt.seller.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.abt.seller.R;
import com.abt.seller.utils.ImageUtil;
import com.abt.seller.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class ImgPagerActivity extends Activity {
	private static final String STATE_POSITION = "STATE_POSITION";
	private int pagerPosition;
	private DisplayImageOptions options;
	private ImageLoaderConfiguration config;
	private ImageLoader imgLoader;
	private String[] imgUrls;
	private ViewPager pager;
	private String imgType;
	private ProgressBar spinner;
	private ImageView imgView;
	private Map<Integer, Bitmap> dateCache = new HashMap<Integer, Bitmap>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);
		initDatas(savedInstanceState, getIntent().getExtras());
		initViews();
	}
	
	private void initViews() {
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(imgUrls));
		pager.setCurrentItem(pagerPosition);
	}
	
	private void initDatas(Bundle state, Bundle bd) {
		if (state != null) pagerPosition = state.getInt(STATE_POSITION);
		imgUrls = bd.getStringArray("IMAGES");
		pagerPosition = bd.getInt("IMAGE_POSITION", 0);
		imgType = bd.getString("IMAGE_TYPE");
		
		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.resetViewBeforeLoading(true).cacheOnDisc(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new FadeInBitmapDisplayer(300)).build();
		
		config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		imgLoader = ImageLoader.getInstance();
		imgLoader.init(config);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {
		private String[] images;
		public ImagePagerAdapter(String[] images) {
			this.images = images;
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup group, int position) {
			View view = getLayoutInflater().inflate(R.layout.pager_image, group, false);
			imgView = (ImageView) view.findViewById(R.id.image);
			spinner = (ProgressBar) view.findViewById(R.id.loading);
			displayImages(images, position);
			((ViewPager) group).addView(view, 0);
			return view;
		}
	}
	
	private void displayImages(String[] images, int ps) {
		if (imgType.equalsIgnoreCase("REMOTE")) {
			displayImg(images, imgView, ps, spinner);
		} else if (imgType.equalsIgnoreCase("LOCAL")) {
			displayLocalImg(ps, imgView, images[ps % images.length]);
		}
	}

	private void displayLocalImg(int ps, ImageView iv, String imgPath) {
		Bitmap bmp = (Bitmap) dateCache.get(ps);
		if (bmp != null) {
			iv.setImageBitmap(bmp);
		} else {
			bmp = ImageUtil.getBitmap(imgPath, 4);
			iv.setImageBitmap(bmp);
			dateCache.put(ps, bmp);
		}
	}

	private void displayImg(String[] images, ImageView iv, int ps, final ProgressBar spinner) {
		imgLoader.displayImage(images[ps], iv, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				spinner.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				handleMsg(failReason);
				spinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				spinner.setVisibility(View.GONE);
			}
		});
	}

	private void handleMsg(FailReason reason) {
		String msg = null;
		switch (reason.getType()) {
		case IO_ERROR:
			msg = "Input/Output error";
			break;
		case DECODING_ERROR:
			msg = "Image can't be decoded";
			break;
		case NETWORK_DENIED:
			msg = "Downloads are denied";
			break;
		case OUT_OF_MEMORY:
			msg = "Out Of Memory error";
			break;
		case UNKNOWN:
			msg = "Unknown error";
			break;
		}
		ToastUtil.show(this, msg);
	}
}