package com.yda.esccmall.widget.calendar;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class ColorfulMonthView extends MonthView {

    private int mRadius;
    private Paint mSchemeBasicPaint = new Paint();
    private Paint mTextPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private int size;
    private float mSchemeBaseLine;
    public ColorfulMonthView(Context context) {
        super(context);

        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));

        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemePaint);
        mSchemePaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));
        size = dipToPx(getContext(), 9);
        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setTextSize(size);
        mSchemeBasicPaint.setColor(0xFFFF9415);
        mRadio = dipToPx(getContext(), 7);
        mPadding = dipToPx(getContext(), 8);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
    }

    /**
     * 如果需要点击Scheme没有效果，则return true
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return false 则不绘制onDrawScheme，因为这里背景色是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);

        mSchemeBasicPaint.setColor(calendar.getSchemeColor());


        canvas.drawText(calendar.getScheme(),
                x + mItemWidth - mPadding - mRadio - getTextWidth(calendar.getScheme()) / 2,
                y + mPadding + mSchemeBaseLine, mSchemeBasicPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 8;
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSchemeLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mCurMonthLunarTextPaint);
        }


    }

    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }
}
