package com.tourism.my.tourismmanagement.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.utils.SPUtil;

/**
 * 底部导航---我的
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout rl_cancellation; // 注销


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        initView(view);
        return view;
    }


    private void initView(View view) {
        rl_cancellation = (RelativeLayout) view.findViewById(R.id.rl_cancellation);
        rl_cancellation.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_cancellation: // 注销
                dialog();
                break;

        }
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认注销此用户吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String account = SPUtil.get(getActivity(), "account");
                DBManager.delUserByCount(getActivity(), account);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

}
