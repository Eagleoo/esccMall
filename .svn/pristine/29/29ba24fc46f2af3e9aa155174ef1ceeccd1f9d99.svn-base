package com.yda.yiyunchain;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/5/11.
 */

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static CustomProgressDialog createDialog(Context context) {
        CustomProgressDialog customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.view_progress_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        onWindowFocusChanged(customProgressDialog, true);
        customProgressDialog.setCancelable(false);
        return customProgressDialog;
    }

    private static void onWindowFocusChanged(CustomProgressDialog pd, boolean hasFocus) {
        if (pd == null) {
            return;
        }
        ImageView imageView = (ImageView) pd.findViewById(R.id.c_loading_img);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    public CustomProgressDialog setMessage(CustomProgressDialog pd, String strMessage) {
        TextView tvMsg = (TextView) pd.findViewById(R.id.c_tv_loadingmsg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return pd;
    }

    // 显示滚动进度
    public static CustomProgressDialog showProgressDialog(Context context, String strMessage) {
        CustomProgressDialog pd = createDialog(context);
        pd.setMessage(pd, strMessage);
        try {
            pd.show();
        } catch (Exception ee) {

        }
        return pd;
    }


}
