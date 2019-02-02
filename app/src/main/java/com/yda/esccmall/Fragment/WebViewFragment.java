package com.yda.esccmall.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yda.esccmall.R;

public class WebViewFragment extends Fragment{
    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "DefaultValue";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(BUNDLE_TITLE);
        }

        View rootView= inflater.inflate(R.layout.frag_load, container, false);

        return rootView;
    }

    public static WebViewFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
