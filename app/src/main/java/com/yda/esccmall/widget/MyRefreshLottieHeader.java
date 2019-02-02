package com.yda.esccmall.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.yda.esccmall.R;

/**
 * Lottie动画
 *
 * @author wapchief
 * @date 2018 /1/30
 */
public class MyRefreshLottieHeader extends LinearLayout implements RefreshHeader {

    /**
     * The M animation view.
     */
    LottieAnimationView mAnimationView;
    TextView refresh_tv;

    /**
     * Instantiates a new My refresh lottie header.
     *
     * @param context the context
     */
    public MyRefreshLottieHeader(Context context) {
        super(context);
        initView(context);
    }

    /**
     * 注意不能为null
     * @return
     */
    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }


    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

        if (isDragging){
            mAnimationView.playAnimation();
            if (percent<1){
                refresh_tv.setText("下拉刷新");
            }else {
                refresh_tv.setText("释放刷新");
            }
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        mAnimationView.playAnimation();
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        mAnimationView.playAnimation();
        refresh_tv.setText("刷新中...");
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        mAnimationView.cancelAnimation();
        refresh_tv.setText("刷新完成");
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.loading_lottie, this);
        mAnimationView = (LottieAnimationView) view.findViewById(R.id.loading_lottie);
        refresh_tv = (TextView) view.findViewById(R.id.refresh_tv);
        mAnimationView.setAnimation("427-happy-birthday.json");
        mAnimationView.loop(true);
    }

    /**
     * Set animation view json.
     *
     * @param animName json文件名
     */
    public void setAnimationViewJson(String animName){
        mAnimationView.setAnimation(animName);
    }

    /**
     * Set animation view json.
     *
     * @param anim the anim
     */
    public void setAnimationViewJson(Animation anim){
        mAnimationView.setAnimation(anim);
    }

}
