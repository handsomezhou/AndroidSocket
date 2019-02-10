package com.handsomezhou.socketserverdemo.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.handsomezhou.socketserverdemo.R;
import com.handsomezhou.socketserverdemo.server.ServerManager;
import com.handsomezhou.socketserverdemo.util.ToastUtil;

/**
 * Created by handsomezhou on 2019/1/29.
 */

public class MainFragment extends BaseFragment {
    private TextView mDeviceInfoTv;
    private Button mStartServerBtn;
    private Button mStopServerBtn;
    private Button mRestartServerBtn;

    @Override
    protected void initData() {
        setContext(getActivity());
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mDeviceInfoTv=view.findViewById(R.id.device_info_text_view);
        mStartServerBtn=view.findViewById(R.id.start_server_btn);
        mStopServerBtn=view.findViewById(R.id.stop_server_btn);
        mRestartServerBtn=view.findViewById(R.id.restart_server_btn);
        return view;
    }

    @Override
    protected void initListener() {
        mDeviceInfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mStartServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerManager.getInstance().startServer();
                ToastUtil.toastLengthLong(getContext(),"StartServer");
            }
        });

        mStopServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerManager.getInstance().stopServer();
                ToastUtil.toastLengthLong(getContext(),"stopServer");
            }
        });

        mRestartServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerManager.getInstance().restartServer();
                ToastUtil.toastLengthLong(getContext(),"RestartServer");
            }
        });
    }
}
