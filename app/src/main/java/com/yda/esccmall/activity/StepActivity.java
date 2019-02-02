package com.yda.esccmall.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.sofia.Sofia;
import com.yda.esccmall.Bean.StepEntity;
import com.yda.esccmall.Bean.StepRank;
import com.yda.esccmall.DBHelper.DBOpenHelper;
import com.yda.esccmall.HttpUtil.HttpManger;
import com.yda.esccmall.R;
import com.yda.esccmall.Service.StepService;
import com.yda.esccmall.util.Constant;
import com.yda.esccmall.util.LocationUtil;
import com.yda.esccmall.util.TimeUtil;
import com.yda.esccmall.util.Util;
import com.yda.esccmall.util.Web;
import com.yda.esccmall.widget.BottomSheetBehavior2;
import com.yda.esccmall.widget.QQCustomView;
import com.yda.esccmall.widget.StepAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class StepActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.tv_km)
    TextView tv_km;

    @BindView(R.id.coordinat)
    CoordinatorLayout coordinat;

    @BindView(R.id.shimmer_linear)
    ShimmerLayout shimmer_linear;

    private QQCustomView qqcustomview;
    private BottomSheetBehavior2 mBottomSheetBehavior;

    private StepAdapter mAnimationAdapter;
    private int mFirstPageItemCount = 3;

    private DBOpenHelper db;
    private boolean isBind = false;
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    private List<StepRank> list_work=new ArrayList<>();

    private int total_steps=0;
    private String rank="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        qqcustomview = (QQCustomView) findViewById(R.id.qqcustomview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = new DBOpenHelper(getApplicationContext());
        Intent intent = new Intent(this, StepService.class);
        isBind=bindService(intent, conn, Context.BIND_AUTO_CREATE);

        //判断全面屏手势是否启动
        if (Settings.Global.getInt(context.getContentResolver(),"force_fsg_nav_bar",0)!=0){
            Sofia.with(context)
                    .invasionStatusBar()
                    .statusBarBackground(Color.TRANSPARENT)
                    .statusBarBackgroundAlpha(0)
                    .invasionNavigationBar()
                    .navigationBarBackground(Color.TRANSPARENT)
                    .navigationBarBackgroundAlpha(0);
        }else {
            Sofia.with(context)
                    .invasionStatusBar()
                    .statusBarBackground(Color.TRANSPARENT)
                    .statusBarBackgroundAlpha(0)
                    .navigationBarBackground(Color.parseColor("#438B48"));
        }

        initAdapter();
        setData();
        updateStep();
        Show_Step();

        qqcustomview.setListener(new QQCustomView.OnViewProgressListener() {
            @Override
            public void onProgress(int progress) {

            }
        });

    }


    private void initAdapter() {

        mAnimationAdapter = new StepAdapter(R.layout.item_step, list_work);
        mAnimationAdapter.openLoadAnimation();
        mAnimationAdapter.setNotDoAnimationCount(mFirstPageItemCount);
        mAnimationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String content = null;
            }
        });
        mBottomSheetBehavior = BottomSheetBehavior2.from(mRecyclerView);
        mBottomSheetBehavior.setState(BottomSheetBehavior2.STATE_COLLAPSED);
        mAnimationAdapter.addHeaderView(getHeaderView(1), 0);
        mRecyclerView.setAdapter(mAnimationAdapter);
        Log.e("列表高度",mRecyclerView.getHeight()+"");
//        ViewGroup.LayoutParams linearParams =coordinat.getLayoutParams(); //取控件textView当前的布局参数
//        linearParams.height = mRecyclerView.getHeight();// 控件的高强制设成20
//
//        coordinat.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }

    private void setData(){

        StepEntity entity = db.getCurDataByDate(TimeUtil.getCurrentDate());
        total_steps=Integer.parseInt(entity.getSteps());
        shimmer_linear.startShimmerAnimation();

        qqcustomview.setSteps(total_steps);
        qqcustomview.setMaxSteps(2000);

    }

    @OnClick({R.id.back,R.id.record})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.record:
                Util.showIntent(context,MainActivity.class, new String[]{"record"},new String[]{"record"});
                break;
        }
    }

    private View getHeaderView(int type) {
        View view = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        if (type == 1) {
            ImageView imageView = (ImageView) view.findViewById(R.id.portrait);
            TextView tv1 = (TextView) view.findViewById(R.id.username);
            TextView tv2 = (TextView) view.findViewById(R.id.rank_num);
            TextView tv3 = (TextView) view.findViewById(R.id.step_num);
            TextView rank_num_tv = (TextView) view.findViewById(R.id.rank_num_tv);

//            if(sp.getString("nick_name","").equals("")){
//                tv1.setText("Tec"+sp.getString("username",""));
//            }
//            else {
//                tv1.setText(sp.getString("nick_name",""));
//            }
            tv1.setText(sp.getString("username",""));
            tv2.setText("第"+rank+"名");
            tv3.setText(total_steps+"");
            rank_num_tv.setText(rank);

            // You cannot start a load for a destroyed activity
            Glide.with(getApplicationContext()).load(Web.url + sp.getString("portrait", "")).crossFade().into(imageView);


        }
        return view;
    }

    /**
     * 定时任务
     */
    private TimerTask timerTask;
    private Timer timer;


    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            /**
             * 设置定时器，每个三秒钟去更新一次运动步数
             */

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        Messenger messenger = new Messenger(service);
                        Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                        msg.replyTo = mGetReplyMessenger;
                        messenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            };

            timer = new Timer();
            timer.schedule(timerTask, 0, 3000);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

    };

    @SuppressLint("HandlerLeak")
    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {
                //这里用来获取到Service发来的数据
                case Constant.MSG_FROM_SERVER:

                    DecimalFormat df=new DecimalFormat("#.##");
                    int steps = msg.getData().getInt("steps");
                    //设置的步数
                    tv_km.setText( "累计行走："+df.format(steps*0.0007)+"Km");
                    qqcustomview.setSteps(steps);
                    setData();
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) this.unbindService(conn);
    }

    //步数上传
    private void updateStep(){
        String s=LocationUtil.getLngAndLat(context);
        DecimalFormat df=new DecimalFormat("#.####");
        HashMap<String, String> params = new HashMap<>();
        params.put("steps", String.valueOf(total_steps));
        params.put("distance", String.valueOf(total_steps*0.0007));
        params.put("longitude", s.substring(0,s.indexOf(",")-1));//经度
        params.put("latitude", s.substring(s.indexOf(",")+1,s.length()));//纬度
        Log.e("****上传步数****",total_steps+"");
        params.put("currentTime", TimeUtil.getCurTime());
        HttpManger.postRequest(context, false,"/tools/submit_api.ashx?action=walkingMatch", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    getInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("-----请求结果------", "str" + t);
            }

            @Override
            public void onError(String t) {

            }
        });
    }

    private void getInfo(){
        HashMap<String, String> params = new HashMap<>();
        params.put("top", "50");
        HttpManger.postRequest(context, false,"/tools/submit_api.ashx?action=WalkingPH", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("1")){
                        list_work= Util.getStepRankArrays("list",t);
                        for (int i=0;i<list_work.size();i++){
                            if (list_work.get(i).getUser_name().equals(sp.getString("username",""))){
                                rank=list_work.get(i).getId();
                            }
                        }
                        shimmer_linear.stopShimmerAnimation();
                        shimmer_linear.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        initAdapter();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("排行榜请求结果", "str" + t);
            }

            @Override
            public void onError(String t) {

            }
        });
    }

    private List<StepEntity> Show_Step(){
        List<StepEntity> mData=new LinkedList<>();
        db=new DBOpenHelper(this);


        Cursor cursor=db.mquery();
        while (cursor.moveToNext()) {

            String sDate = cursor.getString(cursor.getColumnIndex("curDate"));
            String step = cursor.getString(cursor.getColumnIndex("totalSteps"));
            String totalStepsKm = cursor.getString(cursor.getColumnIndex("totalStepsKm"));
            String totalStepsKa = cursor.getString(cursor.getColumnIndex("totalStepsKa"));

            Log.e("***每日步数****",step);
            StepEntity stepEntity=new StepEntity();
            stepEntity.setCurDate(sDate);
            stepEntity.setSteps(step);
            stepEntity.setTotalStepsKm(totalStepsKm);
            stepEntity.setTotalStepsKa(totalStepsKa);
            mData.add(stepEntity);
        }
        return mData;
    }


}
