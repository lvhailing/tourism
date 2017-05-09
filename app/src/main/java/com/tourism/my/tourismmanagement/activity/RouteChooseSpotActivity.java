package com.tourism.my.tourismmanagement.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.adapter.RouteChooseSpotAdapter;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Spot;
import com.tourism.my.tourismmanagement.utils.ToastUtil;

import java.util.List;

/**
 * 景点添加页面
 */
public class RouteChooseSpotActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private ListView lv;
    private RouteChooseSpotAdapter adapter;
    private TextView tv_no;
    private List<Spot> list;

    private String routeId;//当前路线的ID  取时间戳

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_choose_spot);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        TextView tv_add = (TextView) findViewById(R.id.tv_add);
        tv_no = (TextView) findViewById(R.id.tv_no);
        lv = (ListView) findViewById(R.id.lv);

        tv_add.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        routeId = getIntent().getStringExtra("routeId");

        initData();
    }

    public void initData() {
        list = DBManager.getSpotByRouteId(this, "");
        if (list == null || list.size() <= 0) {
            tv_no.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            return;
        }
        tv_no.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
        adapter = new RouteChooseSpotAdapter(this, list);
        lv.setAdapter(adapter);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add: // 添加景点
                List<Spot> addList = adapter.getAddList();
                if (addList == null || addList.size() <= 0) {
                    ToastUtil.showToast(RouteChooseSpotActivity.this, "请选择景点");
                    return;
                }
                showProgressDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(RouteChooseSpotActivity.this, "保存成功");
                        dissmissProgressDialog();
                        finish();
                    }
                }, 1000);
                // 保存到数据库

                for (Spot item : addList) {
                    item.setRouteId(routeId);
                }
                DBManager.saveSpotList(this, addList);
                break;
        }
    }

    private ProgressDialog progDialog = null;

    /**
     * 开启对话框
     */
    private void showProgressDialog() {
        if (progDialog == null) progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("保存中...");
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
