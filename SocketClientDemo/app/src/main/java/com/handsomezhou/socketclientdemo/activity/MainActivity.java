package com.handsomezhou.socketclientdemo.activity;

import android.support.v4.app.Fragment;

import com.handsomezhou.socketclientdemo.fragment.MainFragment;

/**
 * Created by handsomezhou on 2019/1/29.
 */

public class MainActivity extends BaseSingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }

    @Override
    protected boolean isRealTimeLoadFragment() {
        return false;
    }
}
