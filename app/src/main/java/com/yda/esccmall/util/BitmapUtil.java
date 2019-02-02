package com.yda.esccmall.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sym on 2016/1/18.
 */
public class BitmapUtil {

    /**
     * 调整图片的大小 (先将图片缩小或者放大，然后对图片进行裁剪，让不同的图片都能占满不同的手机尺寸的屏幕)
     * @param boxWidth
     * @param boxHeight
     * @param bitmap 原图
     * @return
     */
    public static Bitmap resizeBitmap(int boxWidth, int boxHeight, Bitmap bitmap) {

        float scaleX = ((float) boxWidth) / ((float) bitmap.getWidth());
        float scaleY = ((float) boxHeight) / ((float) bitmap.getHeight());
        float scale = 1.0f;

        if ((scaleX >= scaleY && scaleY >= 1.0f) || (scaleX > scaleY && scaleX < 1.0f) || (scaleX >= 1.0f && scaleY < 1.0f)) {
            scale = scaleX;
        }
        if ((scaleY > scaleX && scaleX >= 1.0f) || (scaleY > scaleX && scaleY < 1.0f) || (scaleX < 1.0f && scaleY >= 1.0f)) {
            scale = scaleY;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        Bitmap alterBitmap = Bitmap.createBitmap(newBitmap, 0, 0, boxWidth, boxHeight);
        newBitmap = null;
        return alterBitmap;
    }


    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "易云链";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap createBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.1f);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }

}
