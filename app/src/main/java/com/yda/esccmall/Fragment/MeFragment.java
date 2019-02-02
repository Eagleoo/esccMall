package com.yda.esccmall.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yda.esccmall.R;

public class MeFragment extends Fragment{
    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "DefaultValue";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(BUNDLE_TITLE);
        }

        View rootView= inflater.inflate(R.layout.frag_load, container, false);

        return rootView;
    }


    public static MeFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        MeFragment fragment = new MeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
