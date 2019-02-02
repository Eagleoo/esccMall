package com.yda.esccmall.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yda.esccmall.R;
import com.yda.esccmall.activity.MainActivity;
import com.yda.esccmall.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

public class PKDialog extends Dialog {

    @BindView(R.id.cancel)
    ImageView cancel;

    @BindView(R.id.pk_result)
    ImageView pk_result;

    @BindView(R.id.pk_tv)
    TextView pk_tv;

    @BindView(R.id.btn_mission)
    Button btn_mission;

    private String winorlose;
    private Context context;

    public PKDialog(@NonNull Context context, String winorlose) {
        super(context, R.style.custom_dialog);

        this.winorlose=winorlose;
        this.context=context;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.pk_dialog);

        ButterKnife.bind(this);

        if (winorlose.equals("1")){
            pk_result.setImageResource(R.drawable.pk_result_win);
            pk_tv.setText("恭喜！继续完成任务提升自己吧！");
        }
        else {
            pk_result.setImageResource(R.drawable.pk_result_fail);
            pk_tv.setText("击败不等于击倒，跌倒了，爬起来！");
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancel();
                dismiss();

            }
        });

        btn_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showIntent(context,MainActivity.class, new String[]{"meFragment"},new String[]{"meFragment"});
                cancel();
                dismiss();
            }
        });
    }


}
