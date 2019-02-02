package com.yda.esccmall.widget.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.yda.esccmall.R;
import com.yda.esccmall.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */

public class ArticleAdapter extends GroupRecyclerAdapter<String, Article> {


    private RequestManager mLoader;
    private Context context;

    public ArticleAdapter(Context context, List<Article> list) {
        super(context);
        this.context=context;
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<Article>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("签到历史", list);
        titles.add("签到历史");
        resetGroups(map,titles);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ArticleViewHolder(mInflater.inflate(R.layout.item_list_article, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, Article item, int position) {
        ArticleViewHolder h = (ArticleViewHolder) holder;
        h.mTextTitle.setText("签到收入");
        h.mTextContent.setText(getDate(item.getAddtime()));
        h.tv_diamond.setText("+"+item.getDiamond()+"钻石");
        h.tv_exp.setText("+"+item.getExp()+"魅力值");
    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTitle,
                mTextContent,tv_diamond,tv_exp;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            mTextTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTextContent = (TextView) itemView.findViewById(R.id.tv_content);
            tv_diamond = (TextView) itemView.findViewById(R.id.tv_diamond);
            tv_exp = (TextView) itemView.findViewById(R.id.tv_exp);
        }
    }


    private static Article create(String title, String content, String imgUrl) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setImgUrl(imgUrl);
        return article;
    }

    private static List<Article> create(int p) {
        List<Article> list = new ArrayList<>();
        if (p == 0) {
            list.add(create("签到收入",
                    "2018-12-25",
                    "+5钻石"));
            list.add(create("签到收入",
                    "2018-12-25",
                    "+5钻石"));
            list.add(create("签到收入",
                    "2018-12-25",
                    "+5钻石"));
            list.add(create("签到收入",
                    "2018-12-25",
                    "+5钻石"));
            list.add(create("签到收入",
                    "2018-12-25",
                    "+5钻石"));
        }


        return list;
    }

    private String getDate(String s){

        String str= Util.cutDateString(s);
        Date d = new Date(Long.valueOf(str));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        return sf.format(d);
    }


}
