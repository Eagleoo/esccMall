package com.yda.esccmall.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yda.esccmall.Bean.Work;
import com.yda.esccmall.HttpUtil.HttpManger;
import com.yda.esccmall.R;
import com.yda.esccmall.util.Web;
import com.yda.esccmall.widget.toast.BamToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class WorkAdapter extends BaseAdapter {

    private List<Work> mData;
    private Context mContext;
    int[] icon=new int[]{R.drawable.new_portrait,R.drawable.new_confirm,R.drawable.new_name,R.drawable.new_adress,R.drawable.new_guide};
    private AdapterCallBck getCallBack;


    public interface AdapterCallBck{
        void doCallBack(String state);
    }

    public WorkAdapter(List<Work> mData, Context mContext,AdapterCallBck getCallBack) {
        this.mData = mData;
        this.mContext = mContext;
        this.getCallBack=getCallBack;

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;

        if(view==null) {
            //创建缓冲布局界面，获取界面上的组件
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.work_list_item, viewGroup, false);
            //  Log.v("AnimalAdapter","改进后调用一次getView方法");
            holder=new ViewHolder();
            holder.new_icon=(ImageView) view.findViewById(R.id.new_icon);
            holder.tv1=(TextView)view.findViewById(R.id.tv1);
            holder.tv2=(TextView)view.findViewById(R.id.tv2);
            holder.tv3=(TextView)view.findViewById(R.id.tv3);
            holder.btn_get=(ShimmerLayout) view.findViewById(R.id.btn_get);
            holder.relativeLayout=(ImageView) view.findViewById(R.id.relative);

            view.setTag(holder);
        }
        else {
            //用原有组件
            holder=(ViewHolder)view.getTag();
        }
        if (mData.get(i).getState().equals("2")){
            holder.btn_get.setVisibility(View.VISIBLE);
            holder.btn_get.setShimmerColor(R.color.result_point_color);
            holder.btn_get.startShimmerAnimation();

        }
        else {
            holder.btn_get.setVisibility(View.GONE);
        }
        if (mData.get(i).getImg_url().equals("")){
            Glide.with(mContext).load(Web.url+"/images/defaultHeadtp/headtp.jpg").asBitmap().placeholder(R.drawable.logo1).into(holder.new_icon);
        }
        else {
            Glide.with(mContext).load(Web.url+mData.get(i).getImg_url()).asBitmap().placeholder(R.drawable.logo1).into(holder.new_icon);
        }

        holder.tv1.setText(mData.get(i).getTitle());
        holder.tv2.setText("+"+mData.get(i).getDiamond());
        holder.tv3.setText("+"+mData.get(i).getExp());

        final ViewHolder finalHolder = holder;
        holder.btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.relativeLayout.setImageResource(R.drawable.mission_none);
                finalHolder.btn_get.setClickable(false);
                finalHolder.btn_get.stopShimmerAnimation();
                getInfo(i);
            }
        });


        return view;
    }

    public static final class ViewHolder {

        ImageView new_icon;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        ShimmerLayout btn_get;
        ImageView relativeLayout;
    }

    private void getInfo(final int position){
        HashMap<String, String> params = new HashMap<>();
        params.put("call_index", mData.get(position).getCall_index());
        HttpManger.postRequest(mContext,false,"/tools/submit_api.ashx?action=TakeMissionAward", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("1")){
                        new MissionGetDialog(mContext, "+"+mData.get(position).getDiamond(), "+"+mData.get(position).getExp()).show();
                        if (getCallBack != null) {
                            getCallBack.doCallBack("1");
                        }
                    }
                    else {
                        BamToast.showText(mContext, json.optString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("请求结果", "str" + t);
            }

            @Override
            public void onError(String t) {

            }
        });
    }


}
