package com.yda.esccmall.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.yda.esccmall.HttpUtil.HttpManger;
import com.yda.esccmall.R;
import com.yda.esccmall.util.Util;
import com.yda.esccmall.widget.calendar.Article;
import com.yda.esccmall.widget.calendar.ArticleAdapter;
import com.yda.esccmall.widget.calendar.GroupItemDecoration;
import com.yda.esccmall.widget.calendar.GroupRecyclerView;
import com.yda.esccmall.widget.toast.BamToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yda.esccmall.widget.calendar.ColorfulMonthView.dipToPx;

public class CalenderActivity extends BaseActivity implements CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, View.OnClickListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    ImageView down;

    CalendarView mCalendarView;

    private int mCalendarHeight;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    GroupRecyclerView mRecyclerView;

    private List<Article> list;
    private List<String> list_time=new ArrayList<>();//已签到时间戳列表
    private Map<String, Calendar> map = new HashMap<>();
    int year;
    int month;
    int day=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        mTextMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mTextLunar = (TextView) findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) findViewById(R.id.tv_current_day);
        down = (ImageView) findViewById(R.id.down);

        findViewById(R.id.relative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!mCalendarLayout.isExpand()) {
//                    mCalendarView.showYearSelectLayout(mYear);
//                    return;
//                }
//                mCalendarView.showYearSelectLayout(mYear);
//                mTextLunar.setVisibility(View.GONE);
//                mTextYear.setVisibility(View.GONE);
//                down.setVisibility(View.GONE);
//                mTextMonthDay.setText(String.valueOf(mYear)+"签到历史");
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCalendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
        mCalendarLayout.expand();
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setMonthViewScrollable(false);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        init();
    }

    private void init(){
        year = mCalendarView.getCurYear();
        month = mCalendarView.getCurMonth();
        qiandao(0,"");
        mCalendarHeight = dipToPx(this, 60);
        mCalendarView.setCalendarItemHeight(mCalendarHeight);
        mRecyclerView = (GroupRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CalenderActivity.this));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());

    }

    private void getNoFlag(){
        String str=mCalendarView.getCurYear()+"-"+mCalendarView.getCurMonth()+"-"+"01";
        String times=Util.getStringToDate(str,"yyyy-MM-dd");
        int days=1;
        if (Long.valueOf(times)>=Long.valueOf(Util.cutDateString(sp.getString("reg_time","0")))){
            for (int i=0;i<mCalendarView.getCurDay();i++){//此时的day为当月最大签到时间,遍历之前未签到的天
                if (!list_time.contains(getFormatDate(times))){
                    String str_day=getFormatDate(times);//截取最后两位天数用于日历上绘制
                    if (Integer.valueOf(str_day)<10){
                        days=Integer.valueOf(str_day.substring(1,2));
                    }
                    else {
                        days=Integer.valueOf(str_day);
                    }
                    Log.e("补签日期",days+"****"+day);
                    map.put(getSchemeCalendar(year, month, days, 0xFF13acf0, "点击补签").toString(),
                            getSchemeCalendar(year, month, days, 0xFF13acf0, "点击补签"));
                }
                times=String.valueOf(Long.valueOf(times)+86400000);//往后加一天
            }

        }
        else {
            String sp_day=Util.cutDateString(sp.getString("reg_time","0"));
            String str_day= getFormatDate(sp_day);
            if (Integer.valueOf(str_day)<10){
                days=Integer.valueOf(str_day.substring(1,2));
                day=day-days;
            }
            else {
                days=Integer.valueOf(str_day);
                day=day-days;
            }

            for (int i=0;i<day;i++){

                if (!list_time.contains(getFormatDate(sp_day))){
                    String str_day1=getFormatDate(sp_day);
                    if (Integer.valueOf(str_day1)<10){
                        days=Integer.valueOf(str_day1.substring(1,2));
                    }
                    else {
                        days=Integer.valueOf(str_day1);
                    }
                    map.put(getSchemeCalendar(year, month, days, 0xFF13acf0, "点击补签").toString(),
                            getSchemeCalendar(year, month, days, 0xFF13acf0, "点击补签"));
                }
                sp_day=String.valueOf(Long.valueOf(sp_day)+86400000);//往后加一天
            }

        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        if (calendar.getDay()<mCalendarView.getCurDay()){
            if (calendar.getDay()<10){
                if (!list_time.contains("0"+calendar.getDay())){
                    qiandao(1,calendar.getYear()+"/"+calendar.getMonth()+"/"+ calendar.getDay());
                }
            }
            else {
                if (!list_time.contains(calendar.getDay()+"")){
                    qiandao(1,calendar.getYear()+"/"+calendar.getMonth()+"/"+ calendar.getDay());
                }
            }
        }


    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onClick(View v) {

    }

    private void qiandao(final int state, String date){
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        HashMap<String, String> params = new HashMap<>();
//        if (state==1){
//            params.put("addtime",date+" "+sf.format(d));
//        }
        if (state==1){
            params.put("addtime",date);
        }
        HttpManger.postRequest(context, false,"/tools/submit_api.ashx?action=UserSign", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("0")){
                        BamToast.showText(context, json.optString("msg"));
                    }
                    else {
                        BamToast.showText(context, json.optString("msg"), true);
                    }
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
        HttpManger.postRequest(context, false,"/tools/submit_api.ashx?action=GetUserSignList", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("1")){
                        list=Util.getSignArrays("list",t);
                        String str3;

                        for (int i=0;i<list.size();i++){
                            str3=Util.cutDateString(list.get(i).getAddtime());
                            String str_day=getFormatDate(str3);//截取最后两位天数用于日历上绘制
                            list_time.add(str_day);
                            if (Integer.valueOf(str_day)<10){
                                day=Integer.valueOf(str_day.substring(1,2));
                            }
                            else {
                                day=Integer.valueOf(str_day);
                            }

                            map.put(getSchemeCalendar(year, month, day, 0xFFFF9415, "已签").toString(),
                                    getSchemeCalendar(year, month, day, 0xFFFF9415, "已签"));

                        }

                        getNoFlag();
                        //此方法在巨大的数据量上不影响遍历性能，推荐使用
                        mCalendarView.setSchemeDate(map);
                        mRecyclerView.setAdapter(new ArticleAdapter(CalenderActivity.this,list));
                        mRecyclerView.notifyDataSetChanged();

                    }
                    else {
                        BamToast.showText(context, json.optString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("******", "str" + t);
            }

            @Override
            public void onError(String t) {

            }
        });
    }

    private String getFormatDate(String s){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(Long.valueOf(s));
        String str_day=sf.format(d).substring(8,sf.format(d).length());//截取最后两位天数用于日历上绘制

        return str_day;
    }
}
