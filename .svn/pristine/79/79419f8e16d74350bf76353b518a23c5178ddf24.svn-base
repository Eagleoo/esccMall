package com.yda.yiyunchain.PopWindowHelp;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 通用 popupwindow
 */

public class BasePopWindow extends PopupWindow

        implements PopupWindow.OnDismissListener {


    private executionPopdis mListener;

    private int interval = 10;

    public interface executionPopdis {
        public void onExecution();
    }


    private static final int INDICATOR_TO_CONTAINER_MIN_MARGIN = 5;
    private static final int MARGIN_SCREEN = 5;

    private final View mContentView;
    private final Context mContext;

    private TriangleIndicatorView mTriangleUpIndicatorView;
    private TriangleIndicatorView mTriangleDownIndicatorView;
    private LinearLayout mContainerLayout;
    private DropPopLayout mDropPopLayout;

    private int mWidth;
    private int mHight;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mleftcenterright = 0; //0 left 1 center  2right
    private boolean isShowTriangle;

    @Override
    public void onDismiss() {
        if (mListener != null) {
            mListener.onExecution();
        }
        Log.e("onDismiss", "onDismiss");
        darkenBackground(1f, mContext);
    }


    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public void showLeft() {
        showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.LEFT, 0, 0);
    }

    public void showRight() {
        showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.RIGHT, 0, 0);
    }

    public void showBottom() {
        showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public void showTop() {
        showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.TOP, 0, 0);
    }

    public void showCenter() {
        showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private boolean mIsShowAtUp = false;


    public void show(final View parent) {


        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int screenWidth = getScreenWidth(mContext);
        int screenHeight = getScreenHeight(mContext);
        //

//        Log.e("View坐标", "X" + x + "Y" + y);
//        Log.e("View的宽高", "View的宽" + parent.getWidth() + "View的高度" + parent.getHeight());
//        Log.e("屏幕大小", "宽度" + screenWidth + "高度" + screenHeight);
//        Log.e("popwin的宽高", "popwin的宽" + mWidth + "popwin的高" + mHight);


        int toLeft = (x + parent.getWidth() / 2); //中心坐标到左边的距离
        int toRight = screenWidth - toLeft;//中心坐标到右边的距离


//        Log.e("View的中心坐标", "View_X" + toLeft + "View_Y" + (y + parent.getHeight() / 2));
        int arrowLeftMargin = 0;
//        Log.e("左右", "toLeft" + (toLeft > mWidth / 2) + "toright" + (toRight > mWidth / 2));

        if (toLeft > mWidth / 2 && toRight > mWidth / 2) { //popwind到左边，到右边都可完全显示
//            Log.e("显示位置", "居中显示");
            mleftcenterright = 1;
            arrowLeftMargin = mWidth / 2 - (isShowTriangle ? mTriangleUpIndicatorView.getRealWidth() / 2 : 0);
        } else if (toLeft > mWidth / 2 && toRight < mWidth / 2) {//popwind到左边可完全显示
//            Log.e("显示位置", "居左边显示" + (parent.getWidth() / 2));
            mleftcenterright = 0;
            arrowLeftMargin = mWidth - (isShowTriangle ? mTriangleUpIndicatorView.getRealWidth() : 0);
        } else if (toLeft < mWidth / 2 && toRight > mWidth / 2) {//popwind到右边可完全显示
            mleftcenterright = 2;

        }

        if (mHight > screenHeight - y - parent.getHeight()) {
//            Log.e("显示位置", "顶部");
            if (isShowTriangle) {
                mTriangleUpIndicatorView.setVisibility(View.INVISIBLE);
                mTriangleDownIndicatorView.setVisibility(View.VISIBLE);
            }

            mIsShowAtUp = true;
        } else {
//            Log.e("显示位置", "底部");
            if (isShowTriangle) {
                mTriangleUpIndicatorView.setVisibility(View.VISIBLE);
                mTriangleDownIndicatorView.setVisibility(View.INVISIBLE);
            }

            mIsShowAtUp = false;
        }

        if (isShowTriangle) {
            LinearLayout.LayoutParams upArrowParams = (LinearLayout.LayoutParams) mTriangleUpIndicatorView.getLayoutParams();
            LinearLayout.LayoutParams downArrowParams = (LinearLayout.LayoutParams) mTriangleDownIndicatorView.getLayoutParams();
            upArrowParams.leftMargin = arrowLeftMargin;
            downArrowParams.leftMargin = arrowLeftMargin;
        }


//        Log.e("比较", "mHight" + mHight + "screenHeight - y - parent.getHeight()" + (screenHeight - y - parent.getHeight()));
//        Log.e("比较", "" + (mHight > screenHeight - y - parent.getHeight()));


        int xoff1 = 0;
        if (mIsShowAtUp) {
            if (mleftcenterright == 1) {
                xoff1 = -(mWidth / 2 - parent.getWidth() / 2);
            } else if (mleftcenterright == 2) {
                xoff1 = parent.getWidth() / 2 - (isShowTriangle ? mTriangleUpIndicatorView.getRealWidth() / 2 : 0);
            } else if (mleftcenterright == 0) {
                xoff1 = -mWidth + (parent.getWidth() / 2 + (isShowTriangle ? mTriangleUpIndicatorView.getRealWidth() / 2 : 0));
            }
//            Log.e("-mHight", "-mHight" + -mHight);
            showAsDropDown(parent, xoff1, -mHight - parent.getHeight() - interval);
        } else {
            if (mleftcenterright == 2) {
                xoff1 = parent.getWidth() / 2 - (isShowTriangle ? mTriangleUpIndicatorView.getRealWidth() / 2 : 0);
            } else if (mleftcenterright == 1) {
                xoff1 = x - (mWidth / 2 - parent.getWidth() / 2);
            } else if (mleftcenterright == 0) {
                xoff1 = (x - mWidth) + parent.getWidth() / 2 + (isShowTriangle ? mTriangleUpIndicatorView.getRealWidth() / 2 : 0);
            }
            showAtLocation(parent, Gravity.NO_GRAVITY, xoff1, y + parent.getHeight() + interval);
        }


    }


    /**
     * 改变背景颜色
     */
    private static void darkenBackground(Float alpha, Context context) {
//        Log.e("改变背景颜色", "改变背景颜色1");
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) context).getWindow().setAttributes(lp);

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private BasePopWindow(Builder builder) {
//        Log.e("MyPopWindow", "0");
        isShowTriangle = builder.isShowTriangle;
        mContentView = builder.mContentView;
        mContext = builder.mContext;
        mListener = builder.ex;

        mDropPopLayout = new DropPopLayout(mContext);
        mTriangleUpIndicatorView = mDropPopLayout.getTriangleUpIndicatorView();
        mTriangleDownIndicatorView = mDropPopLayout.getTriangleDownIndicatorView();

        if (!isShowTriangle) {
            mDropPopLayout.removeView(mTriangleUpIndicatorView);
            mDropPopLayout.removeView(mTriangleDownIndicatorView);
        }

        mContainerLayout = mDropPopLayout.getContainerLayout();
        mContainerLayout.addView(mContentView);

        mScreenWidth = getScreenWidth(mContext);
        mScreenHeight = getScreenHeight(mContext);
        setContentView(mDropPopLayout);


//        Log.e("builder.mWidth2", "builder.mWidth" + builder.mWidth + "builder.mHeight" + builder.mHeight + "KK" + mContentView.getMeasuredHeight());


        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        Log.e("builder.mWidth1", "builder.mWidth" + getContentView().getMeasuredWidth() + "builder.mHeight" + getContentView().getMeasuredHeight());

        if (builder.mWidth == -1) {
            mWidth = getContentView().getMeasuredWidth();
        } else if (builder.mWidth == -2) {
            mWidth = builder.mWidth;
        } else {
            mWidth = builder.mWidth;
        }

        if (builder.mHeight == -1) {
            mHight = getContentView().getMeasuredHeight();
        } else if (builder.mHeight == -2) {
            mHight = builder.mHeight;
        } else {
            mHight = builder.mHeight;
        }
        setWidth(mWidth);
        setHeight(mHight);

        if (builder.mDrawable == null) {
            setBackgroundDrawable(new BitmapDrawable());
        } else {
            setBackgroundDrawable(builder.mDrawable);
        }

        setFocusable(true);
        setOutsideTouchable(true);
        setFocusable(true);
//        Log.e("动画", "builder.animationStyle" + builder.animationStyle);
        if (builder.animationStyle != 0) {
            setAnimationStyle(builder.animationStyle);
        }
//        Log.e("是否显示", "ischagebackcolor" + builder.ischagebackcolor);

        // 如果想要popupWindow 遮挡住状态栏可以加上这句代码

        setOnDismissListener(this);
        if (builder.ischagebackcolor) {
            darkenBackground(0.45f, mContext);
        }
    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int maxWidth = meathureWidthByChilds() + getPaddingLeft() + getPaddingRight();
//        super.onMeasure(View.MeasureSpec.makeMeasureSpec(maxWidth, View.MeasureSpec.EXACTLY), heightMeasureSpec);
//    }
//
//    public int meathureWidthByChilds() {
//        int maxWidth = 0;
//        View view = null;
//        for (int i = 0; i < getAdapter().getCount(); i++) {
//            view = getAdapter().getView(i, view, this);
//            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//            if (view.getMeasuredWidth() > maxWidth) {
//                maxWidth = view.getMeasuredWidth();
//            }
//        }
//        return maxWidth;
//    }

    public static class Builder {
        private final Context mContext;
        private final View mContentView;  //布局view
        private int mWidth;
        private int mHeight;
        private Drawable mDrawable;
        private int animationStyle;
        private boolean ischagebackcolor;
        private executionPopdis ex;
        private boolean isShowTriangle;

        //写一个设置接口监听的方法
        public void setExecutionPopdisListener(executionPopdis listener) {
            ex = listener;
        }

        public Builder(Context context, View contentview) {
            this.mContext = context;
            this.mContentView = contentview;
        }


        public Builder setIschagebackcolor(Boolean ischagebackcolor) {
            this.ischagebackcolor = ischagebackcolor;
            return this;
        }


        public Builder setAnimationStyle(int animationStyle) {
            this.animationStyle = animationStyle;
            return this;
        }

        public Builder isShowTriangle(boolean isShowTriangle) {
            this.isShowTriangle = isShowTriangle;
            return this;
        }

        public Builder setWidth(int width, int hight) {
//            Log.e("Builder", "width" + width + "hight" + hight);
            mWidth = width;
            mHeight = hight;
            return this;
        }


        public Builder setDrawable(Drawable drawable) {
            mDrawable = drawable;
            return this;
        }


        public BasePopWindow build() {
            return new BasePopWindow(this);
        }

    }

}

