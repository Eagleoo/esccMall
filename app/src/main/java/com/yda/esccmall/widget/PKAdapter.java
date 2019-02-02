package com.yda.esccmall.widget;

import android.support.annotation.LayoutRes;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yda.esccmall.Bean.PKBean;
import com.yda.esccmall.R;
import com.yda.esccmall.activity.PKActivity;

import java.util.List;

import io.reactivex.annotations.Nullable;


public class PKAdapter extends BaseQuickAdapter<PKBean, BaseViewHolder> {
    public PKAdapter(@LayoutRes int layoutResId, @Nullable List<PKBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PKBean item) {

        switch (item.getTv1()) {
            case "提尔城等级":
                helper.setMax(R.id.pro_me, 8);
                helper.setMax(R.id.pro_other, 8);
                helper.setImageResource(R.id.pk_icon,R.drawable.pk_lv_icon);
                break;
            case "累计签到":
                helper.setMax(R.id.pro_me, 31);
                helper.setMax(R.id.pro_other, 31);
                helper.setImageResource(R.id.pk_icon,R.drawable.pk_sign_icon);
                break;
            case "钻石值":
                helper.setMax(R.id.pro_me, item.getMax_diamond());
                helper.setMax(R.id.pro_other, item.getMax_diamond());
                helper.setImageResource(R.id.pk_icon,R.drawable.pk_diamond_icon);
                break;
            case "魅力值":
                helper.setMax(R.id.pro_me, item.getMax_exp());
                helper.setMax(R.id.pro_other, item.getMax_exp());
                helper.setImageResource(R.id.pk_icon,R.drawable.pk_exp_icon);
                break;
        }
        helper.setText(R.id.tv1,item.getTv1());
        helper.setText(R.id.lv_me,item.getLv_me());
        helper.setText(R.id.lv_other,item.getLv_other());
//        helper.setProgress(R.id.pro_me,item.getPro_me());
//        helper.setProgress(R.id.pro_other,item.getPro_ohter());
        ProgressBar pro_me= (ProgressBar) helper.getView(R.id.pro_me);
        ProgressBar pro_other= (ProgressBar) helper.getView(R.id.pro_other);
        PKActivity.setAnimation(pro_me,0,item.getPro_me());
        PKActivity.setAnimation(pro_other,0,item.getPro_ohter());

    }


}
