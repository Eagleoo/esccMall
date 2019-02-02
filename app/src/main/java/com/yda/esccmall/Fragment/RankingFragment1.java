package com.yda.esccmall.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yda.esccmall.Bean.Rank;
import com.yda.esccmall.HttpUtil.HttpManger;
import com.yda.esccmall.R;
import com.yda.esccmall.util.Util;
import com.yda.esccmall.widget.ListViewForScrollView;
import com.yda.esccmall.widget.RankAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankingFragment1 extends Fragment {

    @BindView(R.id.listview)
    ListViewForScrollView listview;
    private RankAdapter rankAdapter;
    private Activity context;
    private List<Rank> list=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_frg1, container, false);
        ButterKnife.bind(this,view);
        context=getActivity();

        getRank(false);

        return view;
    }

    private void getRank(boolean isShow){
        HashMap<String, String> params = new HashMap<>();
        list=new ArrayList<>();
        params.put("top", "6");
        params.put("type", "exp");
        HttpManger.postRequest(context, isShow,"/tools/submit_api.ashx?action=PaiHang", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("1")){
                        list= Util.getRankArrays("list",t);
                        rankAdapter=new RankAdapter(list,context,1);
                        listview.setAdapter(rankAdapter);
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
