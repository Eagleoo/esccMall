package com.yda.esccmall.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.yanzhenjie.sofia.Sofia;
import com.yda.esccmall.Fragment.MainFragment;
import com.yda.esccmall.Fragment.MeFragment;
import com.yda.esccmall.Fragment.UseFragment;
import com.yda.esccmall.Fragment.WebViewFragment;
import com.yda.esccmall.R;
import com.yda.esccmall.util.ActivityCollector;
import com.yda.esccmall.util.Util;
import com.yda.esccmall.widget.toast.BamToast;
import com.yinglan.alphatabs.AlphaTabsIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FrgMainActivity extends BaseActivity {

    private MainFragment mainFragment;

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @BindView(R.id.alphaIndicator)
    AlphaTabsIndicator alphaTabsIndicator;

    private MainAdapter mainAdapter;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    private String[] titles = {"微信", "WebViewFragment", "应用页面", "我"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frg_main);
        ButterKnife.bind(this);


        mainFragment=MainFragment.newInstance("主页");

        initView();

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
                    .statusBarBackgroundAlpha(0);

        }

    }

    private void initView(){

        fragments.add(MainFragment.newInstance(titles[0]));
        fragments.add(WebViewFragment.newInstance(titles[1]));
        fragments.add(UseFragment.newInstance(titles[2]));
        fragments.add(MeFragment.newInstance(titles[3]));
        mainAdapter = new MainAdapter(getSupportFragmentManager(),fragments);

        mViewPager.setAdapter(mainAdapter);
        mViewPager.addOnPageChangeListener(mainAdapter);

        alphaTabsIndicator.setViewPager(mViewPager);

    }


    private class MainAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {


        private ArrayList<Fragment> mFragments;
        private FragmentManager fm;
        public MainAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
            super(fm);
            this.mFragments = fragments;
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (0 == position) {
                alphaTabsIndicator.getTabView(0).showNumber(alphaTabsIndicator.getTabView(0).getBadgeNumber() - 1);
            } else if (1 == position) {
                Util.showIntent(context,MainActivity.class, new String[]{"mallFragment"},new String[]{"mallFragment"});
            } else if (3 == position) {
                Util.showIntent(context,MainActivity.class, new String[]{"meFragment"},new String[]{"meFragment"});
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (0 == position) {
                alphaTabsIndicator.getTabView(0).showNumber(alphaTabsIndicator.getTabView(0).getBadgeNumber() - 1);
            } else if (1 == position) {
                Util.showIntent(context,MainActivity.class, new String[]{"mallFragment"},new String[]{"mallFragment"});
            } else if (3 == position) {
                Util.showIntent(context,MainActivity.class, new String[]{"meFragment"},new String[]{"meFragment"});
            }
            else if (2 == position) {
                Util.showIntent(context,MainActivity.class, new String[]{"useFragment"},new String[]{"useFragment"});
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    private long exitTime=0;

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void exit(){

        if((System.currentTimeMillis()-exitTime)>2000) {
            BamToast.showText(context,"再按一次退出提尔城");
            exitTime = System.currentTimeMillis();
        }
        else{
            ActivityCollector.finishAll();
            System.exit(0);
            }
        }
}
