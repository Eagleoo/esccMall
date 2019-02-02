package com.yda.esccmall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yda.esccmall.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Test extends AppCompatActivity {

    @BindView(R.id.gif)
    ImageView gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.mission).into(gif);
//        try {
//            GifDrawable gifFromAssets = new GifDrawable(getResources(), R.drawable.mission);
//            gif.setImageDrawable(gifFromAssets);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Resources file


    }

}
