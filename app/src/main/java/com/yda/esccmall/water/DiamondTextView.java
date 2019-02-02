package com.yda.esccmall.water;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.yda.esccmall.Bean.Diamond;
import com.yda.esccmall.R;
import com.yda.esccmall.util.DeviceUtils;

/**
 * Created by yy on 2018/1/5.
 * 描述：有动画的水滴
 */

public class DiamondTextView extends View {
    Paint mPaint;
    Paint mTextPaint;
    String text = "0.012";
    String escc = "ESCC";
    private int mMWidth;
    private int mHeight;
    Context context;
    int startWidth, startHeight, endWidth, endHeight;
    int padding = 0;
    int index = 1;
    boolean isCollect = false;
    boolean isClick = true;
    private Matrix matrix;
    private Paint paint;
    private Bitmap bitmap;
    private Point center;
    private Point bmpCenter;
    private RectF oval;
    private Animation mAlpha;
    private String value;

    public Diamond diamond;
    private SharedPreferences sp;
    private int leftTime=0;
    AnimatorSet animatorSet;
    ObjectAnimator move2,move3;

    private GetCallBack getCallBack;
    public interface GetCallBack{
        void doCallBack(Diamond diamond);
    }

    /**
     * @param context
     */
    public DiamondTextView(Context context, Diamond diamond,String value) {
        super(context);
        this.context = context;
        this.diamond=diamond;
        this.value=value;
        endWidth = (int) DeviceUtils.dpToPixel(context, 10);
        endHeight = (int) DeviceUtils.dpToPixel(context, 400);
        padding = (int) DeviceUtils.dpToPixel(context, 10);
        startWidth = 0;
        startHeight = 0;
        mAlpha= AnimationUtils.loadAnimation(context, R.anim.alpha);
        sp=context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        animatorSet = new AnimatorSet();
    }

    /**
     * @param index
     * @param startWidth  开始坐标 X
     * @param startHeight 开始坐标 Y
     */
    public void setPosition(int index, int startWidth, int startHeight) {
        this.index = index;
        endWidth = endWidth - startWidth;
        endHeight = endHeight - startHeight;
    }

    public DiamondTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DiamondTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawBitmap(bitmap, matrix, paint);
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(getResources().getColor(R.color.water));
//        mPaint.setStyle(Paint.Style.FILL);
//
//        canvas.drawCircle(mMWidth / 2, (float) mHeight / 2, DeviceUtils.dpToPixel(context, 30), mPaint);
//
        mTextPaint.setTextSize(DeviceUtils.dpToPixel(context, 12));
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mTextPaint.setTypeface(font);
        mTextPaint.setColor(getResources().getColor(R.color.number_color));
        mTextPaint.setStyle(Paint.Style.FILL);
        float width = mTextPaint.measureText(text);
        width=mTextPaint.measureText(value);
        canvas.drawText(value, mMWidth / 2 - width / 2, (float) mHeight * 0.8f, mTextPaint);
        doSetAnim();
    }

    private void doSetAnim() {
        if (isCollect) return;
        isCollect = true;
        move2 = ObjectAnimator.ofFloat(this, "translationY", startHeight, -endHeight);
        move3 = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        animatorSet.playTogether(move2, move3);
        animatorSet.setDuration(4000);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

}
