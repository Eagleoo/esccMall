package com.yda.esccmall.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yda.esccmall.Bean.Info;
import com.yda.esccmall.R;
import com.yda.esccmall.util.Web;

import java.util.List;

public class InfoAdapter extends BaseAdapter {

    private List<Info> mData;
    private Context mContext;

    public InfoAdapter(List<Info> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
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
                    R.layout.info_frg_item, viewGroup, false);
            //  Log.v("AnimalAdapter","改进后调用一次getView方法");
            holder=new ViewHolder();
            holder.portrait=(RoundedImageView) view.findViewById(R.id.portrait);
            holder.diamond_num=(TextView)view.findViewById(R.id.diamond_num);
            holder.username=(TextView)view.findViewById(R.id.username);

            view.setTag(holder);
        }
        else {
            //用原有组件
            holder=(ViewHolder)view.getTag();
        }
        if (mData.get(i).getAvatar().equals("")){
            Glide.with(mContext).load(Web.url+"/images/defaultHeadtp/headtp.jpg").asBitmap().placeholder(R.drawable.portrait).into(holder.portrait);

        }
        else {
            Glide.with(mContext).load(Web.url+mData.get(i).getAvatar()).asBitmap().placeholder(R.drawable.portrait).into(holder.portrait);
        }

        holder.diamond_num.setText(mData.get(i).getDiamond());
        holder.username.setText(mData.get(i).getUser_name());


        return view;
    }
    public static final class ViewHolder {

        RoundedImageView portrait;
        TextView diamond_num;
        TextView username;
    }
}
