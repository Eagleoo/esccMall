package com.yda.yiyunchain.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;

import com.yda.yiyunchain.R;

/**
 * Created by Administrator on 2018/5/8.
 */

public class SelectorFactory {


    public static ShapeSelector newShapeSelector() {
        return new ShapeSelector();
    }

    public static final class ShapeSelector {

        @IntDef({GradientDrawable.RECTANGLE, GradientDrawable.OVAL,
                GradientDrawable.LINE, GradientDrawable.RING})
        private @interface Shape {
        }

        private int mShape;               //the shape of background
        private int mDefaultBgColor;      //default background color
        private int mDisabledBgColor;     //state_enabled = false
        private int mPressedBgColor;      //state_pressed = true
        private int mSelectedBgColor;     //state_selected = true
        private int mFocusedBgColor;      //state_focused = true
        private int mStrokeWidth;         //stroke width in pixel
        private int mDefaultStrokeColor;  //default stroke color
        private int mDisabledStrokeColor; //state_enabled = false
        private int mPressedStrokeColor;  //state_pressed = true
        private int mSelectedStrokeColor; //state_selected = true
        private int mFocusedStrokeColor;  //state_focused = true
        private int mCornerRadius;        //corner radius


        private boolean isOpenRipple = true; //是否开启Ripple 默认在5.0以上开启
        private boolean isMask = false;
        private Drawable maskShape;
        private int mTopLeftRadius;
        private int mTopRightRadius;
        private int mBottomRightRadius;
        private int mBottomLeftRadius;
        private int dashWidth;        //corner radius
        private int dashGap;        //corner radius


        private boolean hasDashWidth = false;
        private boolean hasDashGap = false;


        private boolean hasSetDisabledBgColor = false;
        private boolean hasSetPressedBgColor = false;
        private boolean hasSetSelectedBgColor = false;
        private boolean hasSetFocusedBgColor = false;

        private boolean hasSetDisabledStrokeColor = false;
        private boolean hasSetPressedStrokeColor = false;
        private boolean hasSetSelectedStrokeColor = false;
        private boolean hasSetFocusedStrokeColor = false;

        public ShapeSelector() {
            //initialize default values
            mShape = GradientDrawable.RECTANGLE;
            mDefaultBgColor = Color.TRANSPARENT;
            mDisabledBgColor = Color.TRANSPARENT;
            mPressedBgColor = Color.TRANSPARENT;
            mSelectedBgColor = Color.TRANSPARENT;
            mFocusedBgColor = Color.TRANSPARENT;
            mStrokeWidth = 0;
            mDefaultStrokeColor = Color.TRANSPARENT;
            mDisabledStrokeColor = Color.TRANSPARENT;
            mPressedStrokeColor = Color.TRANSPARENT;
            mSelectedStrokeColor = Color.TRANSPARENT;
            mFocusedStrokeColor = Color.TRANSPARENT;
            mCornerRadius = 0;
        }

        public ShapeSelector setShape(@Shape int shape) {
            mShape = shape;
            return this;
        }

        public ShapeSelector setMaskShape(ShapeDrawable mask) {
            maskShape = mask;
            return this;
        }

        /**
         * @param dashGap   表示之间隔开的距离
         * @param dashWidth 表示'-'这样一个横线的宽度
         * @return
         */
        public ShapeSelector setDashWidthAndDashGap(int dashGap, int dashWidth) {
            hasDashWidth = true;
            hasDashGap = true;
            this.dashGap = dashGap;
            this.dashWidth = dashWidth;
            return this;
        }

        public ShapeSelector setDefaultBgColor(@ColorInt int color) {
            mDefaultBgColor = color;
            if (!hasSetDisabledBgColor)
                mDisabledBgColor = color;
            if (!hasSetPressedBgColor)
                mPressedBgColor = color;
            if (!hasSetSelectedBgColor)
                mSelectedBgColor = color;
            if (!hasSetFocusedBgColor)
                mFocusedBgColor = color;
            return this;
        }

        public ShapeSelector setDisabledBgColor(@ColorInt int color) {
            mDisabledBgColor = color;
            hasSetDisabledBgColor = true;
            return this;
        }


        public ShapeSelector setOpenRipple(boolean openRipple) {
            isOpenRipple = openRipple;
            return this;
        }

        public ShapeSelector setIsMask(boolean mask) {
            isMask = mask;
            return this;
        }


        public ShapeSelector setPressedBgColor(@ColorInt int color) {
            mPressedBgColor = color;
            hasSetPressedBgColor = true;
            return this;
        }

        public ShapeSelector setSelectedBgColor(@ColorInt int color) {
            mSelectedBgColor = color;
            hasSetSelectedBgColor = true;
            return this;
        }


        public ShapeSelector setFocusedBgColor(@ColorInt int color) {
            mFocusedBgColor = color;
            hasSetPressedBgColor = true;
            return this;
        }

        public ShapeSelector setStrokeWidth(@Dimension int width) {
            mStrokeWidth = width;
            return this;
        }

        public ShapeSelector setDefaultStrokeColor(@ColorInt int color) {
            mDefaultStrokeColor = color;
            if (!hasSetDisabledStrokeColor)
                mDisabledStrokeColor = color;
            if (!hasSetPressedStrokeColor)
                mPressedStrokeColor = color;
            if (!hasSetSelectedStrokeColor)
                mSelectedStrokeColor = color;
            if (!hasSetFocusedStrokeColor)
                mFocusedStrokeColor = color;
            return this;
        }


        public ShapeSelector setDisabledStrokeColor(@ColorInt int color) {
            mDisabledStrokeColor = color;
            hasSetDisabledStrokeColor = true;
            return this;
        }

        public ShapeSelector setPressedStrokeColor(@ColorInt int color) {
            mPressedStrokeColor = color;
            hasSetPressedStrokeColor = true;
            return this;
        }

        public ShapeSelector setSelectedStrokeColor(@ColorInt int color) {
            mSelectedStrokeColor = color;
            hasSetSelectedStrokeColor = true;
            return this;
        }

        public ShapeSelector setFocusedStrokeColor(@ColorInt int color) {
            mFocusedStrokeColor = color;
            hasSetFocusedStrokeColor = true;
            return this;
        }

        public ShapeSelector setCornerRadius(@Dimension int radius) {
            mCornerRadius = radius;
            return this;
        }

        public ShapeSelector setCornerRadius(int topLeftRadius, int topRightRadius,
                                             int bottomRightRadius, int bottomLeftRadius) {
            mTopLeftRadius = topLeftRadius;
            mTopRightRadius = topRightRadius;
            mBottomRightRadius = bottomRightRadius;
            mBottomLeftRadius = bottomLeftRadius;
            return this;
        }

        public Drawable create(Context context) {


            StateListDrawable selector = new StateListDrawable();

            //enabled = false
            if (hasSetDisabledBgColor || hasSetDisabledStrokeColor) {
                GradientDrawable disabledShape = getItemShape(mShape,
                        mDisabledBgColor, mStrokeWidth, mDisabledStrokeColor);
                selector.addState(new int[]{-android.R.attr.state_enabled}, disabledShape);
            }

            if (hasDashGap && hasDashWidth) {
                GradientDrawable disabledShape = getItemShape(mShape, mCornerRadius,
                        mDisabledBgColor, mStrokeWidth, dashGap, dashWidth, mDisabledStrokeColor);
                selector.addState(new int[]{-android.R.attr.state_enabled}, disabledShape);
            }


            //selected = true
            if (hasSetSelectedBgColor || hasSetSelectedStrokeColor) {
                GradientDrawable selectedShape = getItemShape(mShape,
                        mSelectedBgColor, mStrokeWidth, mSelectedStrokeColor);
                selector.addState(new int[]{android.R.attr.state_selected}, selectedShape);
            }

            //focused = true
            if (hasSetFocusedBgColor || hasSetFocusedStrokeColor) {
                GradientDrawable focusedShape = getItemShape(mShape,
                        mFocusedBgColor, mStrokeWidth, mFocusedStrokeColor);
                selector.addState(new int[]{android.R.attr.state_focused}, focusedShape);
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isOpenRipple) {
                //default
                GradientDrawable defaultShape = getItemShape(mShape,
                        mDefaultBgColor, mStrokeWidth, mDefaultStrokeColor);
                selector.addState(new int[]{}, defaultShape);
                RippleDrawable drawable = new RippleDrawable(

                        ColorStateList.valueOf(mPressedBgColor != 0 ? mPressedBgColor : context.getResources().getColor(R.color.colorPrimary)),//水波纹颜色
                        selector,   //View样式
                        isMask ? (maskShape != null ? maskShape : getShape()) : null //面具 水波纹范围限制
                );

                return drawable;
            }

            //pressed = true
            if (hasSetPressedBgColor || hasSetPressedStrokeColor) {
                GradientDrawable pressedShape = getItemShape(mShape,
                        mPressedBgColor, mStrokeWidth, mPressedStrokeColor);
                selector.addState(new int[]{android.R.attr.state_pressed}, pressedShape);
            }

            //default
            GradientDrawable defaultShape = getItemShape(mShape,
                    mDefaultBgColor, mStrokeWidth, mDefaultStrokeColor);
            selector.addState(new int[]{}, defaultShape);
            return selector;
        }

        private GradientDrawable getItemShape(int shape,
                                              int solidColor, int strokeWidth, int strokeColor) {
            GradientDrawable drawable;
            if (mTopLeftRadius != 0 || mTopRightRadius != 0 || mBottomRightRadius != 0 || mBottomLeftRadius != 0) {
                drawable = new GradientDrawable();
                drawable.setShape(shape);
                drawable.setStroke(strokeWidth, strokeColor);
                drawable.setCornerRadii(new float[]{mTopLeftRadius, mTopLeftRadius, mTopRightRadius, mTopRightRadius, mBottomRightRadius, mBottomRightRadius, mBottomLeftRadius, mBottomLeftRadius});
                drawable.setColor(solidColor);

                return drawable;
            } else {
                drawable = new GradientDrawable();
                drawable.setShape(shape);
                drawable.setStroke(strokeWidth, strokeColor);
                drawable.setCornerRadius(mCornerRadius);
                drawable.setColor(solidColor);

                return drawable;
            }

        }

        //分别表示 左上 右上 右下 左下
//                gd.setCornerRadii(new
//        float[]{topLeftRadius, topLeftRadius,
// topRightRadius, topRightRadius,
// bottomRightRadius, bottomRightRadius,
// bottomLeftRadius, bottomLeftRadius});
//    }


        private GradientDrawable getItemShape(int shape, int cornerRadius,
                                              int solidColor, int strokeWidth, int dashGap, int dashWidth, int strokeColor) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(shape);
            drawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap);
            drawable.setCornerRadius(cornerRadius);
            drawable.setColor(solidColor);

            return drawable;
        }
    }

    public static GeneralSelector newGeneralSelector() {
        return new GeneralSelector();
    }

    public static final class GeneralSelector {

        private Drawable mDefaultDrawable;
        private Drawable mDisabledDrawable;
        private Drawable mPressedDrawable;
        private Drawable mSelectedDrawable;
        private Drawable mFocusedDrawable;

        private boolean hasSetDisabledDrawable = false;
        private boolean hasSetPressedDrawable = false;
        private boolean hasSetSelectedDrawable = false;
        private boolean hasSetFocusedDrawable = false;

        private GeneralSelector() {
            mDefaultDrawable = new ColorDrawable(Color.TRANSPARENT);
        }

        public GeneralSelector setDefaultDrawable(Drawable drawable) {
            mDefaultDrawable = drawable;
            if (!hasSetDisabledDrawable)
                mDisabledDrawable = drawable;
            if (!hasSetPressedDrawable)
                mPressedDrawable = drawable;
            if (!hasSetSelectedDrawable)
                mSelectedDrawable = drawable;
            if (!hasSetFocusedDrawable)
                mFocusedDrawable = drawable;
            return this;
        }

        public GeneralSelector setDisabledDrawable(Drawable drawable) {
            mDisabledDrawable = drawable;
            hasSetDisabledDrawable = true;
            return this;
        }

        public GeneralSelector setPressedDrawable(Drawable drawable) {
            mPressedDrawable = drawable;
            hasSetPressedDrawable = true;
            return this;
        }

        public GeneralSelector setSelectedDrawable(Drawable drawable) {
            mSelectedDrawable = drawable;
            hasSetSelectedDrawable = true;
            return this;
        }

        public GeneralSelector setFocusedDrawable(Drawable drawable) {
            mFocusedDrawable = drawable;
            hasSetFocusedDrawable = true;
            return this;
        }

        public StateListDrawable create() {
            StateListDrawable selector = new StateListDrawable();
            if (hasSetDisabledDrawable)
                selector.addState(new int[]{-android.R.attr.state_enabled}, mDisabledDrawable);
            if (hasSetPressedDrawable)
                selector.addState(new int[]{android.R.attr.state_pressed}, mPressedDrawable);
            if (hasSetSelectedDrawable)
                selector.addState(new int[]{android.R.attr.state_selected}, mSelectedDrawable);
            if (hasSetFocusedDrawable)
                selector.addState(new int[]{android.R.attr.state_focused}, mFocusedDrawable);
            selector.addState(new int[]{}, mDefaultDrawable);
            return selector;
        }

        //overload
        public GeneralSelector setDefaultDrawable(Context context, @DrawableRes int drawableRes) {
            return setDefaultDrawable(ContextCompat.getDrawable(context, drawableRes));
        }

        //overload
        public GeneralSelector setDisabledDrawable(Context context, @DrawableRes int drawableRes) {
            return setDisabledDrawable(ContextCompat.getDrawable(context, drawableRes));
        }

        //overload
        public GeneralSelector setPressedDrawable(Context context, @DrawableRes int drawableRes) {
            return setPressedDrawable(ContextCompat.getDrawable(context, drawableRes));
        }

        //overload
        public GeneralSelector setSelectedDrawable(Context context, @DrawableRes int drawableRes) {
            return setSelectedDrawable(ContextCompat.getDrawable(context, drawableRes));
        }

        //overload
        public GeneralSelector setFocusedDrawable(Context context, @DrawableRes int drawableRes) {
            return setFocusedDrawable(ContextCompat.getDrawable(context, drawableRes));
        }
    }

    public static ColorSelector newColorSelector() {
        return new ColorSelector();
    }

    public static final class ColorSelector {

        private int mDefaultColor;
        private int mDisabledColor;
        private int mPressedColor;
        private int mSelectedColor;
        private int mFocusedColor;

        private boolean hasSetDisabledColor = false;
        private boolean hasSetPressedColor = false;
        private boolean hasSetSelectedColor = false;
        private boolean hasSetFocusedColor = false;

        private ColorSelector() {
            mDefaultColor = Color.BLACK;
            mDisabledColor = Color.GRAY;
            mPressedColor = Color.BLACK;
            mSelectedColor = Color.BLACK;
            mFocusedColor = Color.BLACK;
        }

        public ColorSelector setDefaultColor(@ColorInt int color) {
            mDefaultColor = color;
            if (!hasSetDisabledColor)
                mDisabledColor = color;
            if (!hasSetPressedColor)
                mPressedColor = color;
            if (!hasSetSelectedColor)
                mSelectedColor = color;
            if (!hasSetFocusedColor)
                mFocusedColor = color;
            return this;
        }

        public ColorSelector setDisabledColor(@ColorInt int color) {
            mDisabledColor = color;
            hasSetDisabledColor = true;
            return this;
        }

        public ColorSelector setPressedColor(@ColorInt int color) {
            mPressedColor = color;
            hasSetPressedColor = true;
            return this;
        }

        public ColorSelector setSelectedColor(@ColorInt int color) {
            mSelectedColor = color;
            hasSetSelectedColor = true;
            return this;
        }

        public ColorSelector setFocusedColor(@ColorInt int color) {
            mFocusedColor = color;
            hasSetFocusedColor = true;
            return this;
        }

        public ColorStateList create() {
            int[] colors = new int[]{
                    hasSetDisabledColor ? mDisabledColor : mDefaultColor,
                    hasSetPressedColor ? mPressedColor : mDefaultColor,
                    hasSetSelectedColor ? mSelectedColor : mDefaultColor,
                    hasSetFocusedColor ? mFocusedColor : mDefaultColor,
                    mDefaultColor
            };
            int[][] states = new int[5][];
            states[0] = new int[]{-android.R.attr.state_enabled};
            states[1] = new int[]{android.R.attr.state_pressed};
            states[2] = new int[]{android.R.attr.state_selected};
            states[3] = new int[]{android.R.attr.state_focused};
            states[4] = new int[]{};
            return new ColorStateList(states, colors);
        }
    }


    private static Drawable getShape() {
        ShapeDrawable mask = new ShapeDrawable(new RectShape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                final float width = this.getWidth();
                final float height = this.getHeight();
                //我想在这个矩形的区域(这里是800px*800px正方形)里画一个 六芒星 ~{=.=}
                //六芒星是上下两个三角形叠加的,所以我画两个三角形就行了.

                //if width == height (为了显示好看,我们设置长宽一样,这里我就认为长宽一样了)
                final float r = width / 2;
                PointF center = new PointF(width / 2, height / 2);
                float offsetHeight = (float) (Math.sin(Math.toRadians(30)) * r);
                float offsetWidth = (float) (Math.cos(Math.toRadians(30)) * r);

                //画正三角形
                Path up = new Path();
                up.moveTo(center.x - offsetWidth, center.y + offsetHeight);
                up.lineTo(center.x - offsetWidth, center.y + offsetHeight);
                up.lineTo(center.x, 0);
                up.lineTo(center.x + offsetWidth, center.y + offsetHeight);
                up.close();
                canvas.drawPath(up, paint);

                //画倒三角形
                Path down = new Path();
                down.moveTo(center.x - offsetWidth, center.y - offsetHeight);
                down.lineTo(center.x - offsetWidth, center.y - offsetHeight);
                down.lineTo(center.x, center.y * 2);
                down.lineTo(center.x + offsetWidth, center.y - offsetHeight);
                down.close();
                canvas.drawPath(down, paint);
            }
        });
        return mask;
    }


}

