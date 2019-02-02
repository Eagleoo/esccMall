package com.yda.esccmall.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.superluo.textbannerlibrary.TextBannerView;
import com.yda.esccmall.Bean.Diamond;
import com.yda.esccmall.Bean.Work;
import com.yda.esccmall.HttpUtil.HttpManger;
import com.yda.esccmall.R;
import com.yda.esccmall.activity.CalenderActivity;
import com.yda.esccmall.activity.FrgMainActivity;
import com.yda.esccmall.activity.LoginActivity;
import com.yda.esccmall.activity.MainActivity;
import com.yda.esccmall.activity.PkReadyActivity;
import com.yda.esccmall.activity.StealActivity;
import com.yda.esccmall.activity.StepActivity;
import com.yda.esccmall.util.DeviceUtils;
import com.yda.esccmall.util.IndicatorLineUtil;
import com.yda.esccmall.util.ListDataSave;
import com.yda.esccmall.util.Util;
import com.yda.esccmall.util.Web;
import com.yda.esccmall.water.WaterContainer;
import com.yda.esccmall.water.WaterView;
import com.yda.esccmall.widget.CustomViewPager;
import com.yda.esccmall.widget.FmPagerAdapter;
import com.yda.esccmall.widget.HotGameAdapter;
import com.yda.esccmall.widget.JudgeNestedScrollView;
import com.yda.esccmall.widget.ListViewForScrollView;
import com.yda.esccmall.widget.MyRefreshLottieHeader;
import com.yda.esccmall.widget.WorkAdapter;
import com.yda.esccmall.widget.toast.BamToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class MainFragment extends BaseLazyFragment implements WaterView.GetCallBack,ListView.OnItemClickListener,WorkAdapter.AdapterCallBck{
    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "DefaultValue";

    @BindView(R.id.main_linear)
    RelativeLayout main_linear;

    @BindView(R.id.relative)
    WaterContainer relative;

    @BindView(R.id.linear1)
    LinearLayout linear1;

    @BindView(R.id.linear4)
    LinearLayout linear4;

    @BindView(R.id.linear_shimer)
    LinearLayout linear_shimer;

    @BindView(R.id.diamond_bag)
    ImageView diamond_bag;

    @BindView(R.id.portrait)
    RoundedImageView portrait;

    @BindView(R.id.portrait_status)
    RoundedImageView portrait_status;

    @BindView(R.id.countdown)
    TextView countdown;

    @BindView(R.id.tv_banner)
    TextBannerView tv_banner;

    @BindView(R.id.d_relative)
    RelativeLayout d_relative;

    @BindView(R.id.scroll_to_rel)
    RelativeLayout scroll_to_rel;

    @BindView(R.id.nick_name)
    TextView nick_name;

    @BindView(R.id.username)
    TextView username;

    @BindView(R.id.tablayout)
    TabLayout tablayout;

    @BindView(R.id.viewpager)
    CustomViewPager viewPager;

    @BindView(R.id.slid_viewpager)
    ViewPager slid_viewpager;

    @BindView(R.id.slid)
    SlidingTabLayout slid;

    @BindView(R.id.listviewFS)
    ListViewForScrollView listviewFS;

    @BindView(R.id.scrollview)
    JudgeNestedScrollView scrollview;

//    @BindView(R.id.recycler_view)
//    RecyclerView recycler_view;

    @BindView(R.id.gv_horizontal_gridview_line)
    GridView gv_horizontal_gridview_line;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.escc_mall)
    ImageView escc_mall;
    @BindView(R.id.mission_more)
    TextView mission_more;

    @BindView(R.id.shimmer_layout)
    ShimmerLayout shimmerLayout;

    private Animation shake;
    private Animation mAlpha;
    private long leftTime=10;
    int posx;
    int posy;
    int count=0;

    int tag=0;

    MyRefreshLottieHeader mRefreshLottieHeader;

    private List<Integer> xCanChooseList;
    private List<Integer> yCanChooseList;
    private List<Integer> random_list;
    private List<Diamond> list=new ArrayList<>();
    private List<WaterView> waterViewList=new ArrayList<>();
    private List<Diamond.DiamondBean> json_list;
    private List<Work> list_work=new ArrayList<>();
    private ListDataSave listDataSave;
    private String[] titles = new String[]{"可偷取名单","常来偷取我的人"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Activity context;
    private boolean flag=true;
    private SharedPreferences sp;
    private int mScrollY = 0;
    private WorkAdapter workAdapter;
    private WorkAdapter.AdapterCallBck adapterCallBck;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DeviceUtils.setCustomDensity(getActivity(), getActivity().getApplication());
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this,view);
        context=getActivity();
        adapterCallBck=this;

        sp=context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);

        mRefreshLottieHeader = new MyRefreshLottieHeader(context);

        posx = (int) DeviceUtils.dpToPixel(context, 50);
        posy = (int) DeviceUtils.dpToPixel(context, 50);

        flag=false;

        //这两个x,y坐标是固定的16个不会重合的位置，后面产生的10个钻石会随机的分布在这16个位置上，达到随机且不会重合的效果
        xCanChooseList	= Arrays.asList(9*posx/2,posx/2, 2*posx, 3* posx, 4*posx, 6* posx, posx/2, 3* posx, 5* posx, posx, 5* posx/2,9*posx/2,11*posx/2,7*posx/2, 2*posx,11*posx/2);
        yCanChooseList	= Arrays.asList(posy/3,posy, 2*posy, posy, 2*posy, posy, 3*posy, 3*posy, 3*posy, 9*posy/2,9*posy/2,5*posy,21*posy/5,13*posy/2,31*posy/5,31*posy/5);

        if (flag){
            //initView();
        }

        mRefreshLayout.setEnableHeaderTranslationContent(true);
        mRefreshLayout.setPrimaryColorsId(R.color.refresh, android.R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

//                Util.showIntent(context, FrgMainActivity.class);
//                getActivity().finish();
                getInfo();
                getNews(false);
                getList();
                getNewWork(false,adapterCallBck);
                //getNetDiamond();
//                FrgMainActivity frgMainActivity= (FrgMainActivity) getActivity();
//                frgMainActivity.reLoadFragView(MainFragment.this);
                flag=false;
//                relative.removeAllViewsInLayout();
//                initView();
            }
        });
        shake = AnimationUtils.loadAnimation(context, R.anim.shaker);
        mAlpha= AnimationUtils.loadAnimation(context, R.anim.alpha);
        listDataSave=new ListDataSave(context,"diamond");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                int lastScrollY = 0;
                int h = DensityUtil.dp2px(170);
                int color = ContextCompat.getColor(getActivity(), R.color.colorAccent) & 0x00ffffff;

                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(scrollview.getScrollY()>=100){
                        if (lastScrollY < h) {
                            scrollY = Math.min(h, scrollY);
                            mScrollY = scrollY > h ? h : scrollY;
                            toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                        }
                        if (scrollview.getScrollY()==0){
                            toolbar.setVisibility(View.GONE);
                        }
                        else {
                            toolbar.setVisibility(View.VISIBLE);
                        }

                        lastScrollY = scrollY;
                    }
                    else {
                        toolbar.setVisibility(View.GONE);
                    }

                }
            });

            mRefreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
                @Override
                public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                    toolbar.setAlpha(1 - Math.min(percent, 1));
                }

                @Override
                public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {
                    toolbar.setAlpha(1 - Math.min(headerHeight, 1));
                }

                @Override
                public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {

                }

                @Override
                public void onHeaderFinish(RefreshHeader header, boolean success) {

                }

                @Override
                public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

                }

                @Override
                public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

                }

                @Override
                public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {

                }

                @Override
                public void onFooterFinish(RefreshFooter footer, boolean success) {

                }

                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                }

                @Override
                public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

                }
            });

        }

        toolbar.setBackgroundColor(0);
        workAdapter=new WorkAdapter(list_work,context,this);
        listviewFS.setOnItemClickListener(this);
        scroll_to_rel.setFocusable(true);
        scroll_to_rel.setFocusableInTouchMode(true);
        scroll_to_rel.requestFocus();
        json_list=new ArrayList<>();
        random_list=new ArrayList<>();
        setHeader(mRefreshLottieHeader);
        getNews(false);
        getInfo();
        getNewWork(false,this);
        getHotGame();
        getList();
        setUserInfo();


        return view;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);

        Log.e("--------2","-------2");

    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();

        Log.e("--------1","-------1");
        shimmerLayout.setVisibility(View.VISIBLE);
        linear_shimer.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmerAnimation();
        //mRefreshLayout.autoRefresh();
        getNetDiamond(false);
    }

    private void setHeader(RefreshHeader header) {
        mRefreshLayout.setRefreshHeader(header);
    }

    @OnClick({R.id.linear1,R.id.linear4,R.id.linear2,R.id.escc_mall,R.id.mission_more,R.id.ib_calendar
            ,R.id.ib_calendar_status,R.id.esccshop,R.id.guide,R.id.tv_look,R.id.more_game})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.linear1:
                Util.showIntent(context,MainActivity.class, new String[]{"diamondBag"},new String[]{"diamondBag"});
                break;
            case R.id.linear4:
                goEscc(2);
                break;
            case R.id.linear2:
                Util.showIntent(context,MainActivity.class, new String[]{"diamondMall"},new String[]{"diamondMall"});
                break;
            case R.id.escc_mall:
                Util.showIntent(context,MainActivity.class, new String[]{"mallFragment"},new String[]{"mallFragment"});
                break;
            case R.id.guide:
                Util.showIntent(context,MainActivity.class, new String[]{"guide"},new String[]{"guide"});
                break;
            case R.id.tv_look:
                Util.showIntent(context,MainActivity.class, new String[]{"tv_look"},new String[]{"tv_look"});
                break;
            case R.id.more_game:
                Util.showIntent(context,MainActivity.class, new String[]{"useFragment"},new String[]{"useFragment"});
                break;
            case R.id.esccshop:
                goEscc(1);
                break;
            case R.id.mission_more:
                Util.showIntent(context,MainActivity.class, new String[]{"meFragment"},new String[]{"meFragment"});
                break;
            case R.id.ib_calendar:
                Util.showIntent(context, CalenderActivity.class);
                break;
            case R.id.ib_calendar_status:
                Util.showIntent(context, CalenderActivity.class);
                break;

        }
    }

    private void setUserInfo(){
        username.setText(sp.getString("username",""));
        if(sp.getString("nick_name","").equals("")){
            nick_name.setText("Tec"+sp.getString("username",""));
        }
        else {
            nick_name.setText(sp.getString("nick_name",""));
        }
        if (sp.getString("portrait","").equals("default")){
            Glide.with(context).load(Web.url+"/images/defaultHeadtp/headtp.jpg").asBitmap().placeholder(R.drawable.portrait).into(portrait);
            Glide.with(context).load(Web.url+"/images/defaultHeadtp/headtp.jpg").asBitmap().placeholder(R.drawable.portrait).into(portrait_status);

        }
        else {
            Glide.with(context).load(Web.url+sp.getString("portrait","")).asBitmap().placeholder(R.drawable.portrait).into(portrait);
            Glide.with(context).load(Web.url+sp.getString("portrait","")).asBitmap().placeholder(R.drawable.portrait).into(portrait_status);
        }

        //不同时间段显示背景
        if (Util.isCurrentInTimeScope(19,0,6,0)){
            d_relative.setBackgroundResource(R.drawable.diamond_night);
        }
        else {
            d_relative.setBackgroundResource(R.drawable.launcher_bg);
        }

    }

    //获取钻石
    private void getNetDiamond(boolean isShow){
        HashMap<String, String> params = new HashMap<>();
        list=new ArrayList<>();
        HttpManger.postRequest(context, isShow,"/tools/submit_api.ashx?action=GetUserDayDiamond", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("1")){
                        String string=json.optString("serveTime");
                        String serveTime=string.substring(6,string.length()-2);
                        json_list=Util.getDiamondArrays("list",t);
                        random_list=new ArrayList<>();
                        Random random = new Random();
                        int s;
                        while (random_list.size()<10){
                            s = random.nextInt(16);//产生10个0-16的随机数
                            if (!random_list.contains(s)){
                                random_list.add(s);
                            }
                        }

                        test();
                        //用这10个随机数去取对应的坐标
                        for (int i=0;i<json_list.size();i++){
                            Diamond diamond=new Diamond(random_list.get(i),xCanChooseList.get(random_list.get(i)),yCanChooseList.get(random_list.get(i)),json_list.get(i));
                            if (Long.valueOf(Util.cutDateString(diamond.getModel().getEndtime()))<=Long.valueOf(serveTime)){
                                addChildView(context, relative,diamond,false,-1,false);
                            }
                            else {
                                leftTime=(Long.valueOf(Util.cutDateString(diamond.getModel().getEndtime()))-Long.valueOf(serveTime))/1000;
                                addChildView(context, relative,diamond,true,(int)leftTime,false);
                            }

                        }
                    }
                    else {
                        Util.showIntent(context, LoginActivity.class);
                        context.finish();
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

    //获取偷取信息
    private void getNews(boolean isShow){

        HashMap<String, String> params = new HashMap<>();
        list=new ArrayList<>();
        params.put("top", "5");
        HttpManger.postRequest(context, isShow,"/tools/submit_api.ashx?action=GetThiefInfoFromMeList", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("1")){
                         List<String> list_info=new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(t);
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String str1=jsonObject1.getString("toUser_name");
                                String str2=jsonObject1.getString("value");
                                String str3=Util.cutDateString(jsonObject1.getString("addtime"));
                                Date d = new Date(Long.valueOf(str3));
                                SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
                                String info=str1+"收取了你"+str2+"钻石   "+sf.format(d);
                                //String info=getResources().getString(R.string.app_info);
                                list_info.add(info);
                            }
                            //调用setDatas(List<String>)方法后,TextBannerView自动开始轮播
                            Drawable drawable = getResources().getDrawable(R.drawable.portrait);
                            tv_banner.setDatas(list_info);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        BamToast.showText(context, json.optString("msg"));
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

    //获取可偷取名单
    private void getInfo(){
        fragments.clear();
        fragments.add(new InfoFragment1(viewPager,0));
        fragments.add(new InfoFragment2(viewPager,1));
        tablayout.addTab(tablayout.newTab());
        tablayout.setupWithViewPager(viewPager,false);
        FmPagerAdapter pagerAdapter = new FmPagerAdapter(fragments,getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);

        for(int i=0;i<titles.length;i++){
            tablayout.getTabAt(i).setText(titles[i]);
            viewPager.resetHeight(0);
        }

        tablayout.post(new Runnable() {
            @Override
            public void run() {
                IndicatorLineUtil.setIndicator(tablayout, 40, 40);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.resetHeight(0);

    }

    //获取任务列表
    public void getNewWork(boolean isShow, final WorkAdapter.AdapterCallBck callBck){
        String[] nameDatas = new String[]{"【新手】上传头像","【新手】实名认证","【新手】完善昵称","【新手】完善所在地区","【新手】阅读会员升级攻略"};
        String[] diamondDatas = new String[]{"+30钻石  +60魅力值","+30钻石  +60魅力值","+30钻石  +60魅力值","+30钻石  +60魅力值","+30钻石  +60魅力值"};
        int[] icon=new int[]{R.drawable.new_portrait,R.drawable.new_confirm,R.drawable.new_name,R.drawable.new_adress,R.drawable.new_guide};
        List<Work> list=new ArrayList<>();
        for (int i=0;i<nameDatas.length;i++){
            Work work=new Work();
            work.setTitle(nameDatas[i]);
            work.setDiamond(diamondDatas[i]);
            list.add(work);
        }

        HashMap<String, String> params = new HashMap<>();
        list_work=new ArrayList<>();
        params.put("top", "5");
        HttpManger.postRequest(context, isShow,"/tools/submit_api.ashx?action=TakeableMissionList", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("1")){
                        list_work=Util.getWorkArrays("list",t);
                        workAdapter=new WorkAdapter(list_work,context,callBck);
                        listviewFS.setAdapter(workAdapter);
                        workAdapter.notifyDataSetChanged();
                    }

                    linear_shimer.setVisibility(View.GONE);
                    shimmerLayout.stopShimmerAnimation();
                    mRefreshLayout.finishRefresh();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("-----请求结果---", "str" + t);
            }

            @Override
            public void onError(String t) {

            }
        });

    }

    //获取游戏
    private void getHotGame(){
        String[] diamondDatas = new String[]{"转轮抽奖","行走赛","我要PK","转轮抽奖","行走赛"};
        List<Work> list=new ArrayList<>();
        for (int i=0;i<diamondDatas.length;i++){
            Work work=new Work();
            work.setTitle(diamondDatas[i]);
            list.add(work);
        }
        // item宽度
        int itemWidth = (int)DeviceUtils.dpToPixel(context, 120);
        // item之间的间隔
        int itemPaddingH = (int)DeviceUtils.dpToPixel(context, 1);
        int size = list.size();
        // 计算GridView宽度
        int gridviewWidth = size * (itemWidth + itemPaddingH);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gv_horizontal_gridview_line.setLayoutParams(params);
        gv_horizontal_gridview_line.setColumnWidth(itemWidth);
        gv_horizontal_gridview_line.setHorizontalSpacing(itemPaddingH);
        gv_horizontal_gridview_line.setStretchMode(GridView.NO_STRETCH);
        gv_horizontal_gridview_line.setNumColumns(size);
        HotGameAdapter adapter = new HotGameAdapter(context,list);
        gv_horizontal_gridview_line.setAdapter(adapter);

        gv_horizontal_gridview_line.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==1){
                    Util.showIntent(context, StepActivity.class);
                }else if (position==0){
                    Util.showIntent(context,MainActivity.class, new String[]{"circle"},new String[]{"circle"});
                }
                else if (position==2){
                    Util.showIntent(context, PkReadyActivity.class);
                    //BamToast.showText(context,"敬请期待");
                }
            }
        });


    }

    //获取排行榜
    private void getList(){
        mFragments.clear();
        String[] mTitles = {"魅力值排行", "钻石排行"};
        mFragments.add(new RankingFragment1());
        mFragments.add(new RankingFragment2());
        FmPagerAdapter pagerAdapter = new FmPagerAdapter(mFragments,getChildFragmentManager());
        slid_viewpager.setAdapter(pagerAdapter);
        slid.setViewPager(slid_viewpager,mTitles);
    }

    /**
     * 添加子钻石
     *
     * @param relative
     * @param diamond
     */
    private void addChildView(final Context context, final WaterContainer relative, final Diamond diamond, boolean isNew, int leftTime,boolean isSteal) {

        int width = (int) DeviceUtils.dpToPixel(context, 60);
        int height = (int) DeviceUtils.dpToPixel(context, 60);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        final WaterView waterView = new WaterView(context,this,diamond,isNew,leftTime,isSteal,relative);
        waterView.setTag(tag);
        tag++;
        waterView.setPosition(diamond.getIndex(), diamond.getX(), diamond.getY());
        waterView.setLayoutParams(layoutParams);
        if (isNew){
            relative.setChildPosition(diamond.getX(), diamond.getY());
            relative.addView(waterView);
            //waterView.startAnimation(mAlpha);
            count++;
            waterView.setDuration();
            //isCountDown();
        }//
        else {
            relative.setChildPosition(diamond.getX(), diamond.getY());
            relative.addView(waterView);
            count=relative.getChildCount();
            Log.e("*****",diamond.getX()+"*"+diamond.getY());
        }
        waterViewList.add(waterView);


    }

    /**
     获取新的钻石，先将点击的钻石从random_list中删除，再判断新产生的1-17随机数不存在于random_list中
     且不等于删除的钻石的index，这样每点击一个钻石都会在不同位置生成
     **/
    private void getDiamond(Diamond.DiamondBean diamondBean,Diamond diamond){
        List<Integer> temp_list=new ArrayList<>();//用一个新temp_list去装新产生的钻石，方便后面产生对应数量的钻石
        for (int i=0;i<random_list.size();i++){
            if (diamond.getIndex()==random_list.get(i)){
                random_list.remove(i);
                Random random = new Random();
                int s;
                while (random_list.size()<10){
                    s = random.nextInt(16);
                    if (!random_list.contains(s)&&!random_list.contains(diamond.getIndex())){
                        temp_list.add(s);
                        random_list.addAll(temp_list);//将新产生的钻石装入
                    }
                }
                break;
            }
        }

        //这里去判断新temp_list中的元素即为需要产生的钻石数量
        for (int i=0;i<temp_list.size();i++){
            Diamond new_diamond=new Diamond(temp_list.get(i),xCanChooseList.get(temp_list.get(i)),yCanChooseList.get(temp_list.get(i)),diamondBean);
            addChildView(context, relative, new_diamond,true,(int)leftTime,false);
        }
    }


    //判断是否领取成功
    private void getIsTakeSuccess(final Diamond diamond){
        HashMap<String, String> params = new HashMap<>();
        list=new ArrayList<>();
        Log.e("*******",diamond.getModel().getId()+""+"****");
        params.put("Diamondid", String.valueOf(diamond.getModel().getId()));
        params.put("type", "0");
        HttpManger.postRequest(context, false,"/tools/submit_api.ashx?action=Diamond_Take", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    String model=json.optString("model");
                    String serveTime=json.optString("serveTime");
                    if (status.equals("0")){
                        BamToast.showText(context, json.optString("msg"));
                    }
                    else {
                        //getCountDownMillis(diamond.getModel().getId(),diamond);
                        StealActivity stealActivity=new StealActivity();
                        stealActivity.addTextView(context,relative,diamond,"+"+diamond.getModel().getValue());
                        Gson gson=new Gson();
                        Diamond.DiamondBean diamondBean = gson.fromJson(model, Diamond.DiamondBean.class);

                        leftTime=diamondBean.getCache_mini()*60;
                        getDiamond(diamondBean,diamond);
                        //BamToast.showText(context, json.optString("msg"), true);
                        //Util.show(context, json.optString("msg"));
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


    private void doScaleAnim(){
        //拉伸
        ScaleAnimation scaleAnimation=new ScaleAnimation(1,0.8f,1,0.8f);//默认从（0,0）
        scaleAnimation.setDuration(200);
        diamond_bag.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                diamond_bag.startAnimation(shake);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    //钻石领取回调
    @Override
    public void doCallBack(final Diamond diamond) {
        count--;
        //将点击的钻石信息存在本地
        list.add(diamond);
        listDataSave.setDataList("diamond_list",list);
        list=listDataSave.getDataList("diamond_list");

        getIsTakeSuccess(diamond);

        //addChildView(context, relative, list.get(0),true,(int)leftTime);
       // list.remove(0);//把第一个删除
        listDataSave.setDataList("diamond_list",list);
        doScaleAnim();
    }

    public static MainFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Util.showIntent(context,MainActivity.class, new String[]{"meFragment"},new String[]{"meFragment"});
        //new MissionGetDialog(context, "+200", "+30").show();

    }

    //任务领取回调
    @Override
    public void doCallBack(String state) {
        if (state.equals("1")){
           getNewWork(false,this);
        }
    }

    private void goEscc(int type) {
        final PackageManager pm = context.getPackageManager();
        Intent intent = new Intent();
        if (null == pm.getLaunchIntentForPackage("com.yda.yiyunchain")) {//没有获取到intent
            Toast.makeText(context, "请先安装易云链", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://down.yda360.com/Mall.apk")));
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.yda.yiyunchain", "com.yda.yiyunchain.activity.MainActivity");
            Bundle bundle = new Bundle();
            if (type==1){
                bundle.putString("url", "/user_downlineshop_applay.aspx?action=shopseachList");
            }
            else if (type==2){
                bundle.putString("url", "/register.aspx?tj="+sp.getString("username",""));
            }
            bundle.putString("username", sp.getString("username",""));
            bundle.putString("password", sp.getString("password",""));
            bundle.putString("token", sp.getString("token",""));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart","onStart");


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate","onCreate");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause","onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume","onResume");
        if (flag){
//            d_relative.removeView(relative);
//            d_relative.addView(relative);
//            d_relative.invalidate();
//            getNetDiamond(false);
            Log.e("来了","来了");
            Util.showIntent(context, FrgMainActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(getActivity()).finish();
            }
        }
        flag=true;

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("onStop","onStop");
//        relative.removeAllViews();
//        relative.invalidate();
//        d_relative.removeView(relative);
//        random_list.clear();
//        json_list.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("onDetach","onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("onDestroyView","onDestroyView");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("onViewCreated","onViewCreated");
    }

    private void test(){
        for (int i=0;i<random_list.size();i++){
            Log.e("位置",random_list.get(i)+"");
        }
    }
}
