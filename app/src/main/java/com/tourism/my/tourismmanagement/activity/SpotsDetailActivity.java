package com.tourism.my.tourismmanagement.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Spot;
import com.tourism.my.tourismmanagement.utils.PhotoUtils;
import com.tourism.my.tourismmanagement.utils.SPUtil;
import com.tourism.my.tourismmanagement.utils.ToastUtil;

import java.util.Map;

/**
 * 景点详情页面
 */
public class SpotsDetailActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back, iv;
    private EditText et_code, et_title, et_addr, et_content;
    private TextView tv_edit, tv_send, tv_del;
    private Spot spotDetail;
    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spots_detail);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv = (ImageView) findViewById(R.id.iv);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_del = (TextView) findViewById(R.id.tv_del);
        et_code = (EditText) findViewById(R.id.et_code);
        et_title = (EditText) findViewById(R.id.et_title);
        et_addr = (EditText) findViewById(R.id.et_addr);
        et_content = (EditText) findViewById(R.id.et_content);

        iv_back.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        tv_del.setOnClickListener(this);


        //判断身份 1游客 2管理员
        String role = SPUtil.get(this, "role");

        if (role.equals("1")) {
            LinearLayout ll_btm = (LinearLayout) findViewById(R.id.ll_btm);
            ll_btm.setVisibility(View.GONE);
            tv_send.setVisibility(View.GONE);
        }

        initLv();
    }

    private void initLv() {
        //景点编号，景点名称，景点简介，景点地址，代表图片
        spotDetail = (Spot) getIntent().getSerializableExtra("spotDetail");
        if (spotDetail == null) {
            ToastUtil.showToast(this, "没有获取到数据");
            return;
        }
        if (getIntent().getBooleanExtra("fromRoute", false)) {
            tv_edit.setVisibility(View.GONE);
            tv_del.setVisibility(View.GONE);
        }
        et_code.setText(spotDetail.getCode());
        et_title.setText(spotDetail.getName());
        et_addr.setText(spotDetail.getAddr());
        et_content.setText(spotDetail.getJianjie());
        tv_send.setVisibility(View.GONE);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 6;
        Bitmap bitmap = BitmapFactory.decodeFile(spotDetail.getFilePath(), options);
        iv.setImageBitmap(bitmap);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Map<String, String> map = PhotoUtils.getData(this, data);
            imgPath = map.get("path");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 6;
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
            iv.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send: // 修改图片
                // 打开系统文件
                PhotoUtils.startImageActivity(this);
                break;
            case R.id.tv_del:
                dialog();
                break;
            case R.id.tv_edit:  // 编辑,保存
                String name = tv_edit.getText().toString();
                if (name.equals("编辑")) {
                    tv_edit.setText("保存");
                    et_code.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
                    et_title.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
                    et_addr.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
                    et_content.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
                    et_code.setEnabled(true);
                    et_title.setEnabled(true);
                    et_addr.setEnabled(true);
                    et_content.setEnabled(true);
                    tv_send.setVisibility(View.VISIBLE);
                    return;
                }
                String code = et_code.getText().toString().trim();
                String title = et_title.getText().toString().trim();
                String addr = et_addr.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast(this, "请输入编号");
                    return;
                } else if (TextUtils.isEmpty(title)) {
                    ToastUtil.showToast(this, "请输入名称");
                    return;
                } else if (TextUtils.isEmpty(addr)) {
                    ToastUtil.showToast(this, "请输入地址");
                    return;
                } else if (TextUtils.isEmpty(content)) {
                    ToastUtil.showToast(this, "请输入简介");
                    return;
                }
                showProgressDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(SpotsDetailActivity.this, "保存成功");
                        dissmissProgressDialog();
                        finish();
                    }
                }, 1500);
                // 保存到数据库
                // 拿到第一张照片
                String filePath = "";
                if (!TextUtils.isEmpty(imgPath)) {
                    filePath = imgPath;
                } else {
                    filePath = spotDetail.getFilePath();
                }

                DBManager.delSpot(this, spotDetail); // 先删除原先的 防止重复
                //String name, String jianjie, String addr, String routeId, String filePath, String code
                DBManager.saveSpot(this, new Spot(title, content, addr, "", filePath, code));
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

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除此景点吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 从数据库删除这一项
                DBManager.delSpot(SpotsDetailActivity.this, spotDetail);
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
