package com.handsomezhou.socketserverdemo.activity;

import android.support.v4.app.Fragment;

import com.handsomezhou.socketserverdemo.fragment.MainFragment;

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
