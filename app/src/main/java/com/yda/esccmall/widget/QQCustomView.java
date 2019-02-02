package com.yda.esccmall.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.yda.esccmall.R;
import com.yda.esccmall.util.DeviceUtils;

public class QQCustomView extends View {
    /**
     * 画笔
     * */
    private Paint mPaint;
    /**
     * 文字画笔
     * */
    private Paint mTextPaint;
    /**
     * 文字画笔
     * */
    private Paint mSmallTextPaint;
    /**
     * 填充进度颜色
     * */
    private int insideColor;
    /**
     * 背景颜色
     * */
    private int outsideColor;
    /**
     * 文字颜色
     * */
    private int textColor;
    /**
     * 宽
     * */
    private int width;
    /**
     * 高
     * */
    private int height;
    /**
     * 当前进度条
     * */
    private int currentProgress = 0;
    /**
     * 进度的百分比
     * */
    private int progress;
    /**
     * 文字的高度
     * */
    private int textheight;
    /**
     * 步数
     * */
    private int steps;
    private int tem_steps=0;
    /**
     * 最大步数
     * */
    private int stepsCount = 5000;
    /**
     * 单位
     * */
    private String company;
    /**
     * 弧线的宽度
     * */
    private int arcStrokeWidth = 20;

    /**
     * 弧线的间距
     * */
    private float spacingWidth = 2;

    private int size=0;

    private int stroke_size=0;

    private Context context;
    /**
     * 进度监听
     * */
    private OnViewProgressListener listener;
    public QQCustomView(Context context) {
        this(context,null);
    }

    public QQCustomView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ty = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView,0,0);
        try {
            insideColor = ty.getColor(R.styleable.CustomView_inside_color,0xFFFE6B15);
            outsideColor = ty.getColor(R.styleable.CustomView_outside_color,0xFF737171);
            textColor = ty.getColor(R.styleable.CustomView_text_colors,0xFFFA9C33);
            steps = ty.getInt(R.styleable.CustomView_step,0);
            stepsCount = ty.getInt(R.styleable.CustomView_step_count,5000);
            company = ty.getString(R.styleable.CustomView_company);
            arcStrokeWidth = (int) DeviceUtils.dpToPixel(context, ty.getInt(R.styleable.CustomView_arc_stroke_widths,10));
            if(TextUtils.isEmpty(company)){
                company = "步";
            }
        }finally {
            ty.recycle();
        }
        this.context=context;
        init();
    }
    /**
     * 初始化属性
     *  <attr name="inside_color" format="color"></attr>
     *  <attr name="outside_color" format="color"></attr>
     *  <attr name="text_color" format="color"></attr>
     * */
    private void init() {
        size=(int) DeviceUtils.dpToPixel(context,35);
        stroke_size=(int) DeviceUtils.dpToPixel(context,2);
        spacingWidth=(int) DeviceUtils.dpToPixel(context,1);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(stroke_size);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(size);
        mTextPaint.setColor(textColor);
        textheight = (int)(mTextPaint.ascent()+mTextPaint.descent());
        mSmallTextPaint = new Paint();
        mSmallTextPaint.setAntiAlias(true);
        mSmallTextPaint.setTextSize(size/2);
        mSmallTextPaint.setColor(textColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = (int) DeviceUtils.dpToPixel(context,200);
        height =(int) DeviceUtils.dpToPixel(context,200);
        setMeasuredDimension(width,height);
    }
    private int measureSize(int measureSpec) {
        int length;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if(mode == MeasureSpec.EXACTLY){
            length = size;
        }else{
            length = 500;
            if(mode == MeasureSpec.AT_MOST){
                length = Math.min(length,size);
            }
        }
        return length;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(listener!=null){
            listener.onProgress(currentProgress);
        }
        drawInside(canvas);
        drawOutside(canvas,currentProgress);
        drawText(canvas,currentProgress);
    }
    /**
     * 画字
     * */
    private void drawText(Canvas canvas,int steps) {
        String str = steps+"";
        int textWidth = (int)mTextPaint.measureText(str);
        int textsWidth = (int)mSmallTextPaint.measureText(company);
        int countWidth = textsWidth +textWidth;
        canvas.drawText(str,0,str.length(),(width-countWidth)/2,(height-textheight)/2,mTextPaint);
        canvas.drawText(company,0,1,(width+textWidth-textsWidth)/2,(height-textheight)/2,mSmallTextPaint);
    }

    /**
     * 画背景
     * */
    private void drawInside(Canvas canvas) {
        mPaint.setColor(outsideColor);
        canvas.save();
        //这里270的意思是旋转270的弧线的意思
        //(float) (width/2 + width/2 * Math.cos(135f*Math.PI/180))
        // 计算的是 135度的情况下 圆上的点位置  计算两个同一半径直线上的两个点画线 旋转画布角度  重复画  就达到了所有的刻度
        for (float i = 0; i < 270/spacingWidth; i++) {
            canvas.drawLine((float) (width/2 + width/2 * Math.cos(135f*Math.PI/180)),(float) (height/2 + (width/2) * Math.sin(135f*Math.PI/180)),
                    (float) (width/2 + (width/2-arcStrokeWidth) * Math.cos(135f*Math.PI/180)),(float) (height/2 + (width/2-arcStrokeWidth) * Math.sin(135f*Math.PI/180)), mPaint);
            canvas.rotate(spacingWidth, getWidth() / 2, getHeight() / 2);
        }
        canvas.restore();
    }

    /**
     * 画进度
     * */
    private void drawOutside(Canvas canvas,int progress) {
        float angle;
        if((progress/(stepsCount*1f))*270>270){
            angle = 270;
        }else {
            angle =  (progress/(stepsCount*1f))*270;
        }
        mPaint.setColor(insideColor);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.save();
        for (float i = 0; i < angle/spacingWidth; i++) {
            canvas.drawLine((float) (width/2 + width/2 * Math.cos(135f*Math.PI/180)),(float) (height/2 + (width/2) * Math.sin(135f*Math.PI/180)),
                    (float) (width/2 + (width/2-arcStrokeWidth) * Math.cos(135f*Math.PI/180)),(float) (height/2 + (width/2-arcStrokeWidth) * Math.sin(135f*Math.PI/180)), mPaint);
            canvas.rotate(spacingWidth, getWidth() / 2, getHeight() / 2);
        }
        canvas.restore();
    }
    /**
     * 设置当前进度
     * */
    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        invalidate();
    }
    /**
     * 属性动画
     * */
    ValueAnimator anim;
    private void animatorMethod(){
        if(anim!=null&&anim.isRunning()){
            return;
        }
        anim = ValueAnimator.ofInt(tem_steps,steps);
        tem_steps=steps;
        anim.setDuration(1000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentProgress = (int)animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();

    }

    public void setSteps(int steps) {
        this.steps = steps;
        animatorMethod();
    }
    public void setMaxSteps(int steps) {
        this.stepsCount = steps;
        animatorMethod();
    }

    /**
     * 设置线条宽度
     * */
    public void setArcStrokeWidth(int arcStrokeWidth) {
        if(arcStrokeWidth<10){
            return;
        }
        this.arcStrokeWidth = arcStrokeWidth;
        invalidate();
    }
    /**
     * 设置线条间距
     * */
    public void setSpacingWidth(float spacingWidth) {
        if(spacingWidth<1){
            return;
        }
        this.spacingWidth = spacingWidth;
        invalidate();
    }

    public interface OnViewProgressListener{
        void onProgress(int progress);
    }
    /**
     * 设置监听
     * */
    public void setListener(OnViewProgressListener listener) {
        this.listener = listener;
    }
}
