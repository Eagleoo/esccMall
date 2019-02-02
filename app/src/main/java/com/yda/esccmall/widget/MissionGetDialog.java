package com.yda.esccmall.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.yda.esccmall.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

/**
 * Created by Administrator on 2017/10/31.
 */

public class MissionGetDialog extends Dialog {
    private Context context;
    private String diamond_num;
    private String exp_num;
    private Animation animation;

    @BindView(R.id.gif)
    ImageView gif;

    @BindView(R.id.close)
    ImageView close;

    @BindView(R.id.mission_ok)
    ImageView mission_ok;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.tv3)
    TextView tv3;

    @BindView(R.id.tv4)
    ImageView tv4;

    @BindView(R.id.linear)
    LinearLayout linear;

    @BindView(R.id.mission_btn)
    Button mission_btn;

    public MissionGetDialog(@NonNull Context context, String diamond_num, String exp_num) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.diamond_num = diamond_num;
        this.exp_num = exp_num;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.mission_dialog);

        ButterKnife.bind(this);
        Glide.with(getContext()).load(R.drawable.mission).into(new GlideDrawableImageViewTarget(gif, 1));
        //Glide.with(getContext()).load(R.drawable.tv_gif).placeholder(R.drawable.tv_gif).into(tv4);

        animation = AnimationUtils.loadAnimation(context, R.anim.dialog_enter);
        ObjectAnimator move3 = ObjectAnimator.ofFloat(linear, "alpha", 0, 1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(move3);
        animatorSet.setDuration(2000);
        animatorSet.start();

        tv2.setText(diamond_num);
        tv3.setText(exp_num);

        linear.startAnimation(animation);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancel();
                dismiss();

            }
        });

        mission_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                dismiss();

            }
        });
    }


}
