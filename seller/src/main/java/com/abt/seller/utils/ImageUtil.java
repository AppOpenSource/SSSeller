package com.abt.seller.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtil {
	
	public static Bitmap drawableToBitmap(Drawable drawable) { 
		if (drawable != null) {
			int w = drawable.getIntrinsicWidth();  
	        int h = drawable.getIntrinsicHeight();  
	        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
	                : Bitmap.Config.RGB_565;  
	        Bitmap bitmap = Bitmap.createBitmap(w, h, config);  
	        Canvas canvas = new Canvas(bitmap);  
	        drawable.setBounds(0, 0, w, h);  
	        drawable.draw(canvas);  
	        return bitmap;
		} else {
			return null;
		}
    }  
	
	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		if (bitmap != null) {
			return new BitmapDrawable(bitmap);
		} else {
			return null;
		}
	}
	
	public static Bitmap inputStreamToBitmap(InputStream inputStream)
			throws Exception {
		if (inputStream != null) {
			return BitmapFactory.decodeStream(inputStream);
		} else {
			return null;
		}
	}

	public static Bitmap bytesToBitmap(byte[] byteArray) {
		if (byteArray != null && byteArray.length != 0) {
			return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	public static Drawable byteToDrawable(byte[] byteArray) {
		if (byteArray != null && byteArray.length > 0) {
			ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
			return Drawable.createFromStream(ins, null);
		} else {
			return null;
		}
	}
	
	public static Bitmap getBitmap(String path, int size) {
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = size;
		return BitmapFactory.decodeFile(path, options);
	}
	
	public static Bitmap getBitmapFromSDCard(String path, String photoName) {
		Bitmap photo = BitmapFactory.decodeFile(path + "/" + photoName);
		if (photo == null) {
			return null;
		} else {
			return photo;
		}
	}

	public static byte[] bitmapToBytes(Bitmap bm) {
		if (bm != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} else {
			return null;
		}
	}

	public static byte[] drawableToBytes(Drawable drawable) {
		if (drawable != null) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			byte[] bytes = bitmapToBytes(bitmap);
			return bytes;
		} else {
			return null;
		}
	}

	public static byte[] base64ToBytes(String base64) throws IOException {
		if (base64 != null && !base64.equals("")) {
			byte[] bytes = Base64.decode(base64);
			return bytes;
		} else {
			return null;
		}
	}

	public static String bytesToBase64(byte[] bytes) {
		if (bytes != null && bytes.length > 0) {
			String base64 = Base64.encode(bytes);
			return base64;
		} else {
			return null;
		}
	}
	
	public static Bitmap getReflectionImageWithBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			final int reflectionGap = 4;  
		    int w = bitmap.getWidth();  
		    int h = bitmap.getHeight();  
		    Matrix matrix = new Matrix();  
		    matrix.preScale(1, -1);  
		    Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,  
		            h / 2, matrix, false);  
		    Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),  
		            Config.ARGB_8888);  
		  
		    Canvas canvas = new Canvas(bitmapWithReflection);  
		    canvas.drawBitmap(bitmap, 0, 0, null);  
		    Paint deafalutPaint = new Paint();  
		    canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);  
		    canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);  
		  
		    Paint paint = new Paint();  
		    LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,  
		            bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,  
		            0x00ffffff, TileMode.CLAMP);  
		    paint.setShader(shader);  
		    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));  
		    canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()  
		            + reflectionGap, paint);  
		    return bitmapWithReflection; 
		} else {
			return null;
		}
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		if (bitmap != null) {
			 int w = bitmap.getWidth();  
			    int h = bitmap.getHeight();  
			    Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);  
			    Canvas canvas = new Canvas(output);  
			    final int color = 0xff424242;  
			    final Paint paint = new Paint();  
			    final Rect rect = new Rect(0, 0, w, h);  
			    final RectF rectF = new RectF(rect);  
			    paint.setAntiAlias(true);  
			    canvas.drawARGB(0, 0, 0, 0);  
			    paint.setColor(color);  
			    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
			    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
			    canvas.drawBitmap(bitmap, rect, rect, paint);  
			    return output;  
		} else {
			return null;
		}
	}
	
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		if (bitmap != null) {
			int w = bitmap.getWidth();  
		    int h = bitmap.getHeight();  
		    Matrix matrix = new Matrix();  
		    float scaleWidth = ((float) width / w);  
		    float scaleHeight = ((float) height / h);  
		    matrix.postScale(scaleWidth, scaleHeight);  
		    Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);  
		    return newbmp;  
		} else {
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		if (drawable != null) {
			int width = drawable.getIntrinsicWidth();  
		    int height = drawable.getIntrinsicHeight();  
		    Bitmap oldbmp = drawableToBitmap(drawable);  
		    Matrix matrix = new Matrix();  
		    float sx = ((float) w / width);  
		    float sy = ((float) h / height);  
		    matrix.postScale(sx, sy);  
		    Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,  
		            matrix, true);  
		    return new BitmapDrawable(newbmp); 
		} else {
			return null;
		}
	} 
	
	public static void deleteAllPictures(String path) {
		if (SDCardUtil.isSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
	}
	
	public static boolean deletePicture(String path, String fileName) {
		if (SDCardUtil.isSDCardAvailable()) {
			File file = new File(path + "/" + fileName);
			if (file == null || !file.exists() || file.isDirectory())
				return false;
			return file.delete();
		} else {
			return false;
		}
	}
	
	public static boolean savePictureToSDCard(Bitmap photoBitmap, String path, String photoName) {
		boolean flag = false;
		if (SDCardUtil.isSDCardAvailable()) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			File photoFile = new File(path , photoName);
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null ) {
					if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
						fileOutputStream.flush();
						fileOutputStream.close();
						flag = true;
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			}
		} 
		return flag;
	}
	
	public static boolean isPictureExistsInSDCard(String path, String photoName) {
		boolean flag = false;
		if (SDCardUtil.isSDCardAvailable()) {
			File dir = new File(path);
			if (dir.exists()) {
				File folders = new File(path);
				File photoFile[] = folders.listFiles();
				for (int i = 0; i < photoFile.length; i++) {
					String fileName = photoFile[i].getName();
					if (fileName.equals(photoName)) {
						flag = true;
						break;
					}
				}
			} else {
				flag = false;
			}
		} else {
			flag = false;
		}
		return flag;
	}
}