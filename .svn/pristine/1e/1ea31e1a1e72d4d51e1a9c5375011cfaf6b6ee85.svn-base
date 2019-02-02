package com.yda.yiyunchain.PopWindowHelp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/4/19.
 */

public class DropPopLayout extends LinearLayout {

    private TriangleIndicatorView mTriangleUpIndicatorView;
    private TriangleIndicatorView mTriangleDownIndicatorView;
    private LinearLayout mContainerLayout;


    public DropPopLayout(Context context) {
        super(context);
        initView();
    }

    public DropPopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        setGravity(Gravity.LEFT);

        mTriangleUpIndicatorView = new TriangleIndicatorView(getContext());
        mTriangleUpIndicatorView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT
                , LayoutParams.WRAP_CONTENT));
        addView(mTriangleUpIndicatorView);

        mContainerLayout = new LinearLayout(getContext());
        mContainerLayout.setOrientation(VERTICAL);

        addView(mContainerLayout);

        mTriangleDownIndicatorView = new TriangleIndicatorView(getContext());
        mTriangleDownIndicatorView.setOrientation(false);
        mTriangleUpIndicatorView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT
                , LayoutParams.WRAP_CONTENT));
        addView(mTriangleDownIndicatorView);
        mTriangleDownIndicatorView.setVisibility(GONE);
    }

    /**
     * 设置弹窗显示位置
     *
     * @param isUp true、在上面
     */
    public void setOrientation(boolean isUp) {
        if (isUp) {
            mTriangleUpIndicatorView.setVisibility(GONE);
            mTriangleDownIndicatorView.setVisibility(VISIBLE);
        } else {
            mTriangleUpIndicatorView.setVisibility(VISIBLE);
            mTriangleDownIndicatorView.setVisibility(GONE);
        }
    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//
//        int maxWidth = meathureWidthByChilds() + getPaddingLeft() + getPaddingRight();
//        int maxHight = meathureHightByChilds() + getPaddingTop() + getPaddingBottom();
//        Log.e("重新测量", "maxWidth" + maxWidth + "maxHight" + maxHight);
//        super.onMeasure(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
//    }
//
//    private int meathureHightByChilds() {
//        int maxWidth = 0;
//        View view = null;
//        for (int i = 0; i < getChildCount(); i++) {
//            view =
//                    getChildAt(i);
//            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//            if (view.getMeasuredHeight() > maxWidth) {
//                Log.e("view.getMea", "view.getMeasuredHeight()" + view.getMeasuredHeight());
//                maxWidth = view.getMeasuredHeight();
//            }
//        }
//        return maxWidth;
//    }
//
//    public int meathureWidthByChilds() {
//        int maxWidth = 0;
//        View view = null;
//        for (int i = 0; i < getChildCount(); i++) {
//            view =
//                    getChildAt(i);
//            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//            if (view.getMeasuredWidth() > maxWidth) {
//                maxWidth = view.getMeasuredWidth();
//            }
//        }
//        return maxWidth;
//    }

    public void setTriangleIndicatorViewColor(int color) {
        mTriangleUpIndicatorView.setColor(color);
        mTriangleDownIndicatorView.setColor(color);
    }

    public void setBackgroundColor(int color) {
        mContainerLayout.setBackgroundColor(color);
    }

    public TriangleIndicatorView getTriangleUpIndicatorView() {
        return mTriangleUpIndicatorView;
    }

    public TriangleIndicatorView getTriangleDownIndicatorView() {
        return mTriangleDownIndicatorView;
    }

    public LinearLayout getContainerLayout() {
        return mContainerLayout;
    }
}
