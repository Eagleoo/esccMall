package com.yda.yiyunchain.Text;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.yda.yiyunchain.R;

public class VDHLinearLayout extends LinearLayout {
    ViewDragHelper viewDragHelper;

    public VDHLinearLayout(Context context) {
        this(context, null);
    }

    public VDHLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        creatViewDragHelper();
    }

    private void creatViewDragHelper() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == oneView || child == twoView;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == twoView) {
                    Log.e("释放", "twoView");
                    viewDragHelper.settleCapturedViewAt(autoBackViewOriginLeft, autoBackViewOriginTop);
                    invalidate();
                }


            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (left > getWidth() - child.getMeasuredWidth()) // 右侧边界
                {
                    left = getWidth() - child.getMeasuredWidth();
                } else if (left < 0) // 左侧边界
                {
                    left = 0;
                }
                return left;

            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }


        });


    }

    View oneView;
    View twoView;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        oneView = findViewById(R.id.one);
        twoView = findViewById(R.id.two);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("ViewGrop", "onInterceptTouchEvent");
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    int autoBackViewOriginLeft;
    int autoBackViewOriginTop;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        autoBackViewOriginLeft = twoView.getLeft();
        autoBackViewOriginTop = twoView.getTop();
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
