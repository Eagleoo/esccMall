package com.yda.esccmall.widget;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yda.esccmall.Bean.StepRank;
import com.yda.esccmall.R;
import com.yda.esccmall.util.Web;

import java.util.List;

import io.reactivex.annotations.Nullable;


public class StepAdapter extends BaseQuickAdapter<StepRank, BaseViewHolder> {
    public StepAdapter(@LayoutRes int layoutResId,@Nullable List<StepRank> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StepRank item) {
        if (item.getId().equals("1")){
            helper.setImageResource(R.id.img_rank,R.drawable.num1);
            helper.setVisible(R.id.rank_num,false);
            helper.setVisible(R.id.img_rank,true);
        }
        else if (item.getId().equals("2")){
            helper.setImageResource(R.id.img_rank,R.drawable.num2);
            helper.setVisible(R.id.rank_num,false);
            helper.setVisible(R.id.img_rank,true);
        }
        else if (item.getId().equals("3")){
            helper.setImageResource(R.id.img_rank,R.drawable.num3);
            helper.setVisible(R.id.rank_num,false);
            helper.setVisible(R.id.img_rank,true);
        }
        else {
            helper.setText(R.id.rank_num,item.getId());
            helper.setVisible(R.id.rank_num,true);
            helper.setVisible(R.id.img_rank,false);
        }

        Glide.with(mContext).load(Web.url+item.getAvatar()).crossFade().into((ImageView) helper.getView(R.id.portrait));
        helper.setText(R.id.username,item.getUser_name());
        helper.setText(R.id.step_num,item.getSteps());
        helper.setText(R.id.step,"步");

    }


}
