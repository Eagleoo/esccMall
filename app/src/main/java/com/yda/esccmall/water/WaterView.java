package com.yda.esccmall.water;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;
import com.yda.esccmall.Bean.Diamond;
import com.yda.esccmall.HttpUtil.HttpManger;
import com.yda.esccmall.R;
import com.yda.esccmall.activity.StealActivity;
import com.yda.esccmall.util.DeviceUtils;
import com.yda.esccmall.util.Util;
import com.yda.esccmall.widget.toast.BamToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.annotations.Nullable;

/**
 * Created by yy on 2018/1/5.
 * 描述：有动画的水滴
 */

public class WaterView extends View {
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
    private String remain;

    public Diamond diamond;
    private boolean isNew,isCountDown=true,isSteal,isSuccess=false;
    private SharedPreferences sp;
    private int leftTime=0;
    private WaterContainer waterContainer;
    private Typeface font;

    AnimatorSet animatorSet = new AnimatorSet();

    ObjectAnimator animator,animator1;
    ObjectAnimator move1,move2,move3;

    private GetCallBack getCallBack;
    public interface GetCallBack{
        void doCallBack(Diamond diamond);
    }

    Handler handler = new Handler();

    Runnable update_thread = new Runnable() {
        @Override
        public void run() {
            --leftTime;
            isCountDown=false;

            if (leftTime >= 0) {
                //倒计时效果展示
                //String formatLongToTimeStr = Util.formatLongToTimeStr(leftTime);
                 //每一秒执行一次
                handler.postDelayed(this, 1000);
                invalidate();
            } else {//倒计时结束.
                Log.e("倒计时结束*******",leftTime+"");
                isNew=false;
                invalidate();
                //getNewDiamond();
                //发送消息，结束倒计时
                Message message = new Message();
                message.what = 1;
                handlerStop.sendMessage(message);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    final Handler handlerStop = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    handler.removeCallbacks(update_thread);
                    break;
            }
            super.handleMessage(msg);
        }

    };
    /**
     * @param context
     */
    public WaterView(Context context, GetCallBack getCallBack, Diamond diamond, boolean isNew, int leftTime,boolean isSteal,WaterContainer waterContainer) {
        super(context);
        this.context = context;
        endWidth = (int) DeviceUtils.dpToPixel(context, 10);
        endHeight = (int) DeviceUtils.dpToPixel(context, 400);
        padding = (int) DeviceUtils.dpToPixel(context, 10);
        startWidth = 0;
        startHeight = 0;
        this.getCallBack=getCallBack;
        this.waterContainer=waterContainer;
        this.diamond=diamond;
        this.isNew=isNew;
        this.leftTime=leftTime;
        this.isSteal=isSteal;
        mAlpha= AnimationUtils.loadAnimation(context, R.anim.alpha);
        sp=context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        remain=diamond.getModel().getRemain();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        animator = ObjectAnimator.ofFloat(this, "alpha", 0.3f,1);
        animator1 = ObjectAnimator.ofFloat(this, "translationY", -padding, padding);

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

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        canvas.drawBitmap(bitmap, matrix, paint);

//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(getResources().getColor(R.color.water));
//        mPaint.setStyle(Paint.Style.FILL);
//
//        canvas.drawCircle(mMWidth / 2, (float) mHeight / 2, DeviceUtils.dpToPixel(context, 30), mPaint);
//
        mTextPaint.setTextSize(DeviceUtils.dpToPixel(context, 9));
        mTextPaint.setTypeface(font);
        mTextPaint.setColor(getResources().getColor(R.color.white));
        mTextPaint.setStyle(Paint.Style.FILL);
        float width = mTextPaint.measureText(text);
        float width1 = mTextPaint.measureText(escc);

        if (isNew){
            width=mTextPaint.measureText(Util.formatLongToTimeStr((long) leftTime));
            canvas.drawText( Util.formatLongToTimeStr((long) leftTime), mMWidth / 2 - width / 2, (float) mHeight * 0.8f, mTextPaint);
            //mTextPaint.setTextSize(DeviceUtils.dpToPixel(context, 11));
            //canvas.drawText("还  剩", mMWidth / 2 - width1 , (float) mHeight * 0.5f, mTextPaint);
            if (isCountDown){
                animator.setRepeatMode(ObjectAnimator.REVERSE);
                animator.setRepeatCount(leftTime/3);
                animator.setDuration(3000);
                animator.start();
            }

        }
        else {
            animator = ObjectAnimator.ofFloat(this, "alpha", 1,1);
            animator.setDuration(3000);
            animator.start();
            canvas.drawText(Util.format2String(remain), mMWidth / 2 - 3*width / 5, (float) mHeight * 0.8f, mTextPaint);
            //mTextPaint.setTextSize(DeviceUtils.dpToPixel(context, 11));
            //canvas.drawText(escc, mMWidth / 2 - width1 , (float) mHeight * 0.5f, mTextPaint);
            doRepeatAnim();

        }


        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isFastClick(diamond)){
                    BamToast.showText(context, "请勿重复点击");
                }
                else {
                    if (leftTime==-1){
                        if (isSteal){
                            getIsTakeSuccess(diamond);
                        }
                        else {
                            doSetAnim();
                        }

                        isClick=false;
                    }
                    else {
                        BamToast.showText(context,"请"+Util.formatLongToTimeStr((long) leftTime)+"后再来收取");
                    }
                }


            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mMWidth = w;
        this.mHeight = h;
        matrix = new Matrix();
        paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.diamond);
        int bw = temp.getWidth();
        int bh = temp.getHeight();
        float scale = Math.min(1f * mMWidth / bw, 1f * mHeight / bh);
        bitmap = scaleBitmap(temp, scale);
        // compute init left, top
        int bbw = bitmap.getWidth();
        int bbh = bitmap.getHeight();
        center = new Point(mMWidth / 2, mHeight / 2);
        bmpCenter = new Point(bbw / 2, bbh / 2);
        matrix.postScale(0.9f, 0.9f, center.x, center.y); // 中心点参数是有用的
        matrix.postTranslate(center.x - bmpCenter.x, center.y - bmpCenter.y); // 移动到当前view 的中心
        oval = new RectF(center.x - bbw / 2, center.y - bbh / 2,
                center.x + bbw / 2, center.y + bbh / 2);
    }


    private void doSetAnim() {
        if (isCollect) return;
        isCollect = true;

        Log.e("endWidth",endWidth+"");
        Log.e("endHeight",endHeight+"");

        move1 = ObjectAnimator.ofFloat(this, "translationX", startWidth, endWidth);
        move2 = ObjectAnimator.ofFloat(this, "translationY", startHeight, endHeight);
        move3 = ObjectAnimator.ofFloat(this, "alpha", 1, 0);

        animatorSet.playTogether(move1, move2, move3);
        animatorSet.setDuration(1500);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("****点击时***",diamond.getModel().getId()+""+"***点击时*");
                if (getCallBack != null) {
                    getCallBack.doCallBack(diamond);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void doRepeatAnim() {
        animator1.setRepeatMode(ObjectAnimator.REVERSE);
        animator1.setRepeatCount(ObjectAnimator.INFINITE);
        animator1.setDuration(4000);
        animator1.start();
    }

    private Bitmap scaleBitmap(Bitmap origin, float scale) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    public void setDuration() {
        handler.postDelayed(update_thread,1000);
    }

    //判断是否偷取成功
    private void getIsTakeSuccess(final Diamond diamond){
        HashMap<String, String> params = new HashMap<>();
        params.put("token", sp.getString("token",""));
        params.put("Diamondid", String.valueOf(diamond.getModel().getId()));
        params.put("type", "1");
        HttpManger.postRequest(context, false,"/tools/submit_api.ashx?action=Diamond_Take", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    String model=json.optString("model");
                    String serveTime=json.optString("serveTime");
                    if (status.equals("1")){
                        Gson gson=new Gson();
                        Diamond.DiamondBean diamondBean = gson.fromJson(model, Diamond.DiamondBean.class);
                        //BamToast.showText(context, json.optString("msg")+"钻石+"+diamondBean.getValue(), false);
                        remain=json.optString("remain");
                        isSuccess=true;
                        invalidate();
                        StealActivity stealActivity=new StealActivity();
                        stealActivity.addTextView(context,waterContainer,diamond,"+"+diamondBean.getValue());
                    }
                    else {
                        BamToast.showText(context, json.optString("msg"), false);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("请求结果", "str" + t);
            }

            @Override
            public void onError(String t) {

            }
        });
    }


}
