package com.handsomezhou.socketserverdemo.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handsomezhou.socketserverdemo.R;

/**
 * Created by handsomezhou on 2019/1/29.
 */

public class MainFragment extends BaseFragment {
    @Override
    protected void initData() {
        setContext(getActivity());
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

    @Override
    protected void initListener() {

    }
}
