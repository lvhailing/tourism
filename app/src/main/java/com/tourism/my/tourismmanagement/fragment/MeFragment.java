package com.tourism.my.tourismmanagement.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.activity.ForgetPwdActivity;
import com.tourism.my.tourismmanagement.activity.LoginActivity;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.utils.SPUtil;
import com.tourism.my.tourismmanagement.utils.ToastUtil;

/**
 * 底部导航---我的
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout rl_change; // 注销
    private RelativeLayout rl_cancellation; // 注销
    private RelativeLayout rl_check; // 检查更新


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        initView(view);
        return view;
    }


    private void initView(View view) {
        rl_cancellation = (RelativeLayout) view.findViewById(R.id.rl_cancellation);
        rl_check = (RelativeLayout) view.findViewById(R.id.rl_check);
        rl_change = (RelativeLayout) view.findViewById(R.id.rl_change);

        rl_change.setOnClickListener(this);
        rl_cancellation.setOnClickListener(this);
        rl_check.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_cancellation: // 注销
                dialog();
                break;
            case R.id.rl_change: // 检查更新
                // 去重置密码界面
                Intent intent = new Intent(getActivity(), ForgetPwdActivity.class);
                intent.putExtra("acount", SPUtil.get(getActivity(), "account"));
                startActivity(intent);
                break;
            case R.id.rl_check: // 检查更新
                showProgressDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getActivity(), "已是最新版本");
                        dissmissProgressDialog();
                    }
                }, 1000);
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
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    private ProgressDialog progDialog = null;

    /**
     * 开启对话框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(getActivity());
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("检查中...");
        progDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

}
