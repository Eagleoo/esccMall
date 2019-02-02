package com.yda.yiyunchain.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yda.yiyunchain.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    AutoPollRecyclerView recyclerView;
    List<String> strings = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recyclerView = findViewById(R.id.recycle);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
        for (int i = 0; i < 20; i++) {
            strings.add("第" + (i + 1) + "项");
        }

//        int resId = R.anim.layout_animation_fall_down;
//        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
//        recyclerView.setLayoutAnimation(animation);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(defaultItemAnimator);


//        MyAdapter myAdapter = new MyAdapter();
        NewMyAdapter newMyAdapter = new NewMyAdapter(R.layout.item_comment, strings);
        newMyAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
//        recyclerView.setAdapter(myAdapter);
        recyclerView.setAdapter(newMyAdapter);
//        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//
//            }
//        });
        recyclerView.start();


    }


    class NewMyAdapter extends BaseQuickAdapter<String, NewMyAdapter.MyViewHolder> {

        public NewMyAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(MyViewHolder helper, String item) {
            //            String data = strings.get(position % strings.size());
            helper.textView.setText(item);
        }

        class MyViewHolder extends BaseViewHolder {
            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.item_tx);
            }
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String data = strings.get(position % strings.size());
            holder.textView.setText(data);
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }


        class MyViewHolder extends BaseViewHolder {
            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.item_tx);
            }
        }
    }

}
