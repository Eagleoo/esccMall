package com.yda.esccmall.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.yda.esccmall.Bean.Info;
import com.yda.esccmall.HttpUtil.HttpManger;
import com.yda.esccmall.R;
import com.yda.esccmall.activity.StealActivity;
import com.yda.esccmall.util.Util;
import com.yda.esccmall.widget.CustomViewPager;
import com.yda.esccmall.widget.InfoAdapter;
import com.yda.esccmall.widget.ListViewForScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class InfoFragment1 extends BaseLazyFragment {

    @BindView(R.id.listview)
    ListViewForScrollView listview;
    @BindView(R.id.steal_none)
    ImageView steal_none;
    private InfoAdapter infoAdapter;
    private Activity context;
    private String nick_name;
    private List<Info> list=new ArrayList<>();
    private String[] nameDatas = new String[]{"001用户","SKS98K用户","0065用户","MNSPX","艺术黑白"};
    private String[] diamondDatas = new String[]{"钻石120","钻石12","钻石150","钻石220","钻石180"};

    private int fragmentID=0;

    private  View rootView=null;

    CustomViewPager vp;

    public InfoFragment1() {
    }

    public InfoFragment1(CustomViewPager vp, int fragmentID)
    {
        this.vp = vp;
        this.fragmentID =fragmentID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.info_frg1, container, false);
        ButterKnife.bind(this,rootView);
        context=getActivity();
        if (vp!=null){
            vp.setObjectForPosition(rootView,fragmentID);
        }

        for (int i=0;i<nameDatas.length;i++){
            Info info=new Info();
            info.setUser_name(nameDatas[i]);
            info.setDiamond(diamondDatas[i]);
            list.add(info);
        }


        getInfo();



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list.get(position).getNick_name().equals("")){
                    nick_name="Tec"+list.get(position).getUser_name();
                }
                else {
                    nick_name=list.get(position).getNick_name();
                }
                Util.showIntent(context, StealActivity.class,new String[]{"id","portrait","username","nick_name"},new String[]{list.get(position).getId(),list.get(position).getAvatar(),list.get(position).getUser_name(),nick_name});
            }
        });

        return rootView;
    }

    private void getInfo(){
        HashMap<String, String> params = new HashMap<>();
        list=new ArrayList<>();
        params.put("top", "5");
        HttpManger.postRequest(context, false,"/tools/submit_api.ashx?action=GetThiefablePerson", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {
                if (vp!=null){
                    vp.setObjectForPosition(rootView,fragmentID);
                }
                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("1")){
                        list=Util.getInfoArrays("list",t);
                        infoAdapter=new InfoAdapter(list,context);
                        listview.setAdapter(infoAdapter);
                        infoAdapter.notifyDataSetChanged();

                        if (list.size()==0){
                            steal_none.setVisibility(View.VISIBLE);
                        }

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
