package com.yda.esccmall.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yda.esccmall.Bean.Rank;
import com.yda.esccmall.R;
import com.yda.esccmall.util.Web;

import java.util.List;


public class RankAdapter extends BaseAdapter {
    private List<Rank> mData;
    private Context mContext;
    private int state;

    public RankAdapter(List<Rank> mData, Context mContext,int state) {
        this.mData = mData;
        this.mContext = mContext;
        this.state=state;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;

        if(view==null) {
            //创建缓冲布局界面，获取界面上的组件
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.list_frg_item, viewGroup, false);
            //  Log.v("AnimalAdapter","改进后调用一次getView方法");
            holder=new ViewHolder();
            holder.rank_num=(TextView) view.findViewById(R.id.rank_num);
            holder.rank_name=(TextView)view.findViewById(R.id.rank_name);
            holder.rank_diamond=(TextView)view.findViewById(R.id.rank_diamond);
            holder.portrait=(RoundedImageView) view.findViewById(R.id.rank_portrait);

            view.setTag(holder);
        }
        else {
            //用原有组件
            holder=(ViewHolder)view.getTag();
        }
        holder.rank_num.setText(mData.get(i).getPh());
        holder.rank_name.setText(mData.get(i).getUser_name());
        if (state==1){
            holder.rank_diamond.setText(mData.get(i).getExp());
        }
        else {
            holder.rank_diamond.setText(mData.get(i).getDiamond());
        }

        if (mData.get(i).getAvatar().equals("")){
            Glide.with(mContext).load(Web.url+"/images/defaultHeadtp/headtp.jpg").asBitmap().placeholder(R.drawable.portrait).into(holder.portrait);

        }
        else {
            Glide.with(mContext).load(Web.url+mData.get(i).getAvatar()).asBitmap().placeholder(R.drawable.portrait).into(holder.portrait);
        }



        return view;
    }
    public static final class ViewHolder {

        TextView rank_num;
        TextView rank_name;
        TextView rank_diamond;
        RoundedImageView portrait;
    }
}