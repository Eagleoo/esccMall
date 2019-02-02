package com.yda.yiyunchain.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.yda.yiyunchain.R;


/**
 * Created by Administrator on 2017/8/8.
 */

public class MoreTextView extends android.support.v7.widget.AppCompatTextView {
    public static final int LEFT = 0, TOP = 1, RIGHT = 2, BOTTOM = 3;
    private int mHight, mWidth, mLocation;
    private Bitmap mImage;


    public MoreTextView(Context context) {
        this(context, null);
    }

    public MoreTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MoreTextView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {

                case R.styleable.MoreTextView_imageWidth:
                    mWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 0, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MoreTextView_imageHight:
                    mHight = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 0, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MoreTextView_drawable_src:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.MoreTextView_imageLocation:
                    mLocation = a.getInt(attr, LEFT);
                    break;
            }
        }
        a.recycle();

        drawPicture();//设置图片方法


    }

    public void setImag(int drw){
        mImage = BitmapFactory.decodeResource(getResources(), drw);
        drawPicture();//设置图片方法
    }

    private void drawPicture() {
        if (mImage != null) {
            Drawable mDrawable;
            if (mHight != 0 && mWidth != 0) {
                mDrawable = new BitmapDrawable(getResources(), getRealBitmap(mImage));
            } else {
                mDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(mImage, mImage.getWidth(), mImage.getHeight(), true));
            }
            switch (mLocation) {
                case LEFT:
                    this.setCompoundDrawablesWithIntrinsicBounds(mDrawable, null,
                            null, null);
                    break;
                case TOP:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable,
                            null, null);
                    break;
                case RIGHT:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            mDrawable, null);
                    break;
                case BOTTOM:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            mDrawable);
                    break;
            }

        }


    }

    private Bitmap getRealBitmap(Bitmap image) {
        //根据需要Drawable原来的大小和目标宽高进行裁剪（缩放）
        int width = image.getWidth();// 获得图片的宽高
        int height = image.getHeight();
        // 取得想要缩放的matrix参数
        float scaleWidth = (float) mWidth / width;
        float scaleHeight = (float) mHight / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 返回新的Bitmap
        return Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);

    }
}

