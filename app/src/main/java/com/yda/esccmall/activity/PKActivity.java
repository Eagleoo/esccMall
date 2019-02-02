package com.yda.esccmall.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yda.esccmall.Bean.PKBean;
import com.yda.esccmall.R;
import com.yda.esccmall.util.BitmapUtil;
import com.yda.esccmall.util.DownLoadPicUtil;
import com.yda.esccmall.util.Web;
import com.yda.esccmall.widget.PKAdapter;
import com.yda.esccmall.widget.PKDialog;
import com.yda.esccmall.widget.calendar.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PKActivity extends BaseActivity{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_other)
    ProgressBar progress_other;
    @BindView(R.id.progress_me)
    ProgressBar progress_me;

    @BindView(R.id.load_linear)
    LinearLayout load_linear;

    @BindView(R.id.portrait_me)
    ImageView portrait_me;
    @BindView(R.id.portrait_other)
    ImageView portrait_other;

    @BindView(R.id.rocket_me)
    ImageView rocket_me;

    @BindView(R.id.rocket_other)
    ImageView rocket_other;
    @BindView(R.id.smoke_m_me)
    ImageView smoke_m_me;

    @BindView(R.id.smoke_m_other)
    ImageView smoke_m_other;
    @BindView(R.id.smoke_t_me)
    ImageView smoke_t_me;

    @BindView(R.id.smoke_t_other)
    ImageView smoke_t_other;

    @BindView(R.id.pk_fail_me)
    ImageView pk_fail_me;
    @BindView(R.id.pk_fail_other)
    ImageView pk_fail_other;
    @BindView(R.id.pk_win_other)
    ImageView pk_win_other;
    @BindView(R.id.pk_win_me)
    ImageView pk_win_me;

    @BindView(R.id.relative_other)
    RelativeLayout relative_other;
    @BindView(R.id.relative_me)
    RelativeLayout relative_me;

    @BindView(R.id.lv_other)
    TextView lv_other;
    @BindView(R.id.lv_me)
    TextView lv_me;
    @BindView(R.id.username_me)
    TextView username_me;
    @BindView(R.id.username_other)
    TextView username_other;

    int x=0 ;
    int progress=0 ;
    int width;
    private  Handler handler = new Handler();
    private AnimatorSet animatorSetsuofang;
    private ObjectAnimator scaleX;
    private ObjectAnimator scaleY;
    private Animation shake;
    private Vibrator mVibrator;  //声明一个振动器对象
    private long[] longs=new long[]{0,200};
    private PKAdapter pkAdapter;
    private List<PKBean> list_work=new ArrayList<>();
    private List<Article> list_sign;
    String sign="";//本月签到天数
    private String winlose;
    private String imgurl;
    private String username;
    private String lv,lv1;
    private String portrait;
    private Bitmap bitmap;

    @SuppressLint("HandlerLeak")
    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (bitmap!=null){
                        if (winlose.equals("1")){
                            portrait_other.setImageBitmap(bitmap);
                        }
                        else {
                            portrait_me.setImageBitmap(bitmap);
                        }

                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk);

        ButterKnife.bind(this);



        init();

        //getInfo("1");
        //getSign();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWith();
    }

    private void init(){

        Intent intent=getIntent();
        winlose=intent.getStringExtra("winlose");
        setAnimation(progress_other, 0,100);
        setAnimation(progress_me, 0,100);
        animatorSetsuofang = new AnimatorSet();//组合动画
        shake = AnimationUtils.loadAnimation(this, R.anim.shaker_pk);
        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        imgurl=intent.getStringExtra("portrait");
        username=intent.getStringExtra("username");
        lv="等级：Lv"+intent.getStringExtra("grade_id");
        lv1="等级："+sp.getString("group_id","");
        portrait=sp.getString("portrait","");
        if (imgurl.equals("")){
            imgurl="/images/defaultHeadtp/headtp.jpg";
        }
        if (portrait.equals("default")){
            portrait="/images/defaultHeadtp/headtp.jpg";
        }
        Glide.with(context).load(Web.url+imgurl).asBitmap().placeholder(R.drawable.portrait).into(portrait_other);
        Glide.with(context).load(Web.url+portrait).asBitmap().placeholder(R.drawable.portrait).into(portrait_me);
        username_other.setText(username);
        username_me.setText(sp.getString("nick_name",""));
        lv_other.setText(lv);
        lv_me.setText(lv1);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pkAdapter = new PKAdapter(R.layout.item_pk, list_work);
                pkAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

                mRecyclerView.setAdapter(pkAdapter);
                load_linear.setVisibility(View.GONE);
                if (winlose.equals("1")){
                    PK_Other();
                }
                else {
                    PK_Me();
                }

            }
        }, 1000);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {

                return false;
            }
        });
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        relative_other.setFocusable(true);
        relative_other.setFocusableInTouchMode(true);
        relative_other.requestFocus();
        list_work=getPK_list(intent.getStringExtra("exp"),intent.getStringExtra("diamond"),intent.getStringExtra("grade"),
                intent.getStringExtra("grade_id"),intent.getStringExtra("signshu"),intent.getStringExtra("sign"));

    }


    @SuppressLint("SetTextI18n")
    @OnClick({R.id.back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }

    }

    public static void setAnimation(final ProgressBar view, final int start, final int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end).setDuration(1000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setProgress((int) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }

    private void getWith(){
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density1 = dm.density;
        width = dm.widthPixels;
    }

    //我方攻击
    private void PK_Me(){
        ObjectAnimator move1 = ObjectAnimator.ofFloat(rocket_me, "translationX", x, -width);
        ObjectAnimator move3 = ObjectAnimator.ofFloat(rocket_me, "alpha", 1, 0);
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(move1, move3);
        animatorSet.setDuration(1200);
        if (winlose.equals("1")){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rocket_me.setVisibility(View.VISIBLE);
                    animatorSet.start();
                }
            },1000);
        }
        else {
            rocket_me.setVisibility(View.VISIBLE);
            animatorSet.start();
        }

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rocket_me.setVisibility(View.GONE);
                smoke_m_other.setVisibility(View.VISIBLE);
                smoke_t_other.setVisibility(View.VISIBLE);
                showSmoken(smoke_m_other,smoke_t_other);
                relative_other.startAnimation(shake);
                mVibrator.vibrate(longs, -1);
                if (winlose.equals("1")){
                    setAnimation(progress_other, 100,0);
                    showFail();
                    showWin();
                    show_shape();
                }
                else {
                    setAnimation(progress_other, 100,20);
                    PK_Other();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    //轮到对方攻击
    private void PK_Other(){

        ObjectAnimator move1 = ObjectAnimator.ofFloat(rocket_other, "translationX", x, width);
        ObjectAnimator move3 = ObjectAnimator.ofFloat(rocket_other, "alpha", 1, 0);
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(move1, move3);
        animatorSet.setDuration(1200);
        if (winlose.equals("1")){
            rocket_other.setVisibility(View.VISIBLE);
            animatorSet.start();
        }
        else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rocket_other.setVisibility(View.VISIBLE);
                    animatorSet.start();
                }
            },1000);
        }


        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rocket_other.setVisibility(View.GONE);
                smoke_m_me.setVisibility(View.VISIBLE);
                smoke_t_me.setVisibility(View.VISIBLE);
                showSmoken(smoke_m_me,smoke_t_me);
                mVibrator.vibrate(longs, -1);
                relative_me.startAnimation(shake);
                if (winlose.equals("1")){
                    setAnimation(progress_me, 100,20);
                    PK_Me();
                }
                else {
                    setAnimation(progress_me, 100,0);
                    showFail();
                    showWin();
                    show_shape();
                }


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void showSmoken(View view1,View view2){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(1000);
        view1.startAnimation(alphaAnimation);
        view2.startAnimation(alphaAnimation);
    }

    private void showFail(){
        if (winlose.equals("1")){
            pk_fail_other.setVisibility(View.VISIBLE);
            scaleX = ObjectAnimator.ofFloat(pk_fail_other, "scaleX", 0, 1f);
            scaleY = ObjectAnimator.ofFloat(pk_fail_other, "scaleY", 0, 1f);
            ObjectAnimator move3 = ObjectAnimator.ofFloat(pk_fail_other, "alpha", 0, 1);
            animatorSetsuofang.setDuration(2000);
            animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
            animatorSetsuofang.play(scaleX).with(scaleY).with(move3);//两个动画同时开始
            animatorSetsuofang.start();
        }
        else {
            pk_fail_me.setVisibility(View.VISIBLE);
            scaleX = ObjectAnimator.ofFloat(pk_fail_me, "scaleX", 0, 1f);
            scaleY = ObjectAnimator.ofFloat(pk_fail_me, "scaleY", 0, 1f);
            ObjectAnimator move3 = ObjectAnimator.ofFloat(pk_fail_me, "alpha", 0, 1);
            animatorSetsuofang.setDuration(2000);
            animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
            animatorSetsuofang.play(scaleX).with(scaleY).with(move3);//两个动画同时开始
            animatorSetsuofang.start();
        }

    }

    private void showWin(){
        if (winlose.equals("1")){
            pk_win_me.setVisibility(View.VISIBLE);
            scaleX = ObjectAnimator.ofFloat(pk_win_me, "scaleX", 0, 1f);
            scaleY = ObjectAnimator.ofFloat(pk_win_me, "scaleY", 0, 1f);
            ObjectAnimator move3 = ObjectAnimator.ofFloat(pk_win_me, "alpha", 0, 1);
            animatorSetsuofang.setDuration(2000);
            animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
            animatorSetsuofang.play(scaleX).with(scaleY).with(move3);//两个动画同时开始
            animatorSetsuofang.start();
        }
        else {
            pk_win_other.setVisibility(View.VISIBLE);
            scaleX = ObjectAnimator.ofFloat(pk_win_other, "scaleX", 0, 1f);
            scaleY = ObjectAnimator.ofFloat(pk_win_other, "scaleY", 0, 1f);
            ObjectAnimator move3 = ObjectAnimator.ofFloat(pk_win_other, "alpha", 0, 1);
            animatorSetsuofang.setDuration(2000);
            animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
            animatorSetsuofang.play(scaleX).with(scaleY).with(move3);//两个动画同时开始
            animatorSetsuofang.start();
        }

    }

    private void show_img(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (winlose.equals("1")){
                        bitmap=BitmapUtil.createBitmap(Objects.requireNonNull(DownLoadPicUtil.getPic(Web.url+imgurl)));
                    }else {
                        bitmap=BitmapUtil.createBitmap(Objects.requireNonNull(DownLoadPicUtil.getPic(Web.url+portrait)));
                    }

                }
                Message message=new Message();
                message.what=1;
                handler1.sendMessage(message);
            }
        }).start();

    }

    //PK结果双方背景渐变
    private void show_shape(){
        ValueAnimator colorAnim,colorAnim1;
        ArgbEvaluator argbEvaluator=new ArgbEvaluator();
        if (winlose.equals("1")){
            colorAnim1 = ObjectAnimator.ofInt(relative_me,"backgroundColor", Color.parseColor("#f98426"), Color.parseColor("#FF0000"));
            colorAnim = ObjectAnimator.ofInt(relative_other,"backgroundColor", Color.parseColor("#f98426"), Color.parseColor("#808080"));
        }else {
            colorAnim1 = ObjectAnimator.ofInt(relative_other,"backgroundColor", Color.parseColor("#f98426"), Color.parseColor("#FF0000"));
            colorAnim = ObjectAnimator.ofInt(relative_me,"backgroundColor", Color.parseColor("#28B5FC"), Color.parseColor("#808080"));
        }
        colorAnim.setEvaluator(argbEvaluator);
        colorAnim1.setEvaluator(argbEvaluator);
        animatorSetsuofang.setDuration(3000);
        animatorSetsuofang.playTogether(colorAnim,colorAnim1);
        animatorSetsuofang.start();
        animatorSetsuofang.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                show_img();
                if (context!=null){
                    new PKDialog(context,winlose).show();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    //后台数据乱给，自己弄一个list给适配器，然后返回的数据又啥子都有
    private List<PKBean> getPK_list(String exp,String diamond,String grade,String grade_id,String sign_shu,String sign_me){
        String group=sp.getString("group_id","");
        int diamond_me=Integer.valueOf(sp.getString("diamond","").substring(0,sp.getString("diamond","").lastIndexOf(".")));
        int diamond_other=Integer.valueOf(diamond.substring(0,diamond.lastIndexOf(".")));
        int exp_me=Integer.valueOf(sp.getString("exp","").substring(0,sp.getString("exp","").lastIndexOf(".")));
        int exp_other=Integer.valueOf(exp.substring(0,exp.lastIndexOf(".")));
        List<PKBean> list=new ArrayList<>();
        PKBean pkBean1=new PKBean();
        PKBean pkBean2=new PKBean();
        PKBean pkBean3=new PKBean();
        PKBean pkBean4=new PKBean();
        pkBean1.setLv_me(group);
        pkBean1.setLv_other("Lv"+grade_id);
        pkBean1.setPro_me(Integer.valueOf(group.substring(2,group.length())));
        pkBean1.setPro_ohter(Integer.valueOf(grade_id));
        pkBean1.setTv1("提尔城等级");
        list.add(pkBean1);
        pkBean2.setLv_me(sign_me+"天");
        pkBean2.setLv_other(sign_shu+"天");
        pkBean2.setPro_me(Integer.valueOf(sign_me));
        pkBean2.setPro_ohter(Integer.valueOf(sign_shu));
        pkBean2.setTv1("累计签到");
        list.add(pkBean2);
        if (diamond_me<1000&&diamond_other<1000){
            pkBean3.setMax_diamond(1000);
        }
        else {
            if (diamond_me>=diamond_other){
                pkBean3.setMax_diamond(diamond_me);
            }
            else {
                pkBean3.setMax_diamond(diamond_other);
            }
        }

        pkBean3.setLv_me(sp.getString("diamond",""));
        pkBean3.setLv_other(diamond);
        pkBean3.setPro_me(diamond_me);
        pkBean3.setPro_ohter(diamond_other);
        pkBean3.setTv1("钻石值");
        list.add(pkBean3);
        if (exp_me<1000&&exp_other<1000){
            pkBean4.setMax_exp(1000);
        }
        else {
            if (exp_me>=exp_other){
                pkBean4.setMax_exp(exp_me);
            }
            else {
                pkBean4.setMax_exp(exp_other);
            }
        }

        pkBean4.setLv_me(sp.getString("exp",""));
        pkBean4.setLv_other(exp);
        pkBean4.setPro_me(exp_me);
        pkBean4.setPro_ohter(exp_other);
        pkBean4.setTv1("魅力值");
        list.add(pkBean4);

        return list;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context=null;
    }
}
