package com.tourism.my.tourismmanagement.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.adapter.RouteSpotAdapter;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Route;
import com.tourism.my.tourismmanagement.db.db.model.Spot;
import com.tourism.my.tourismmanagement.utils.ToastUtil;

import java.util.List;

/**
 * 景点添加页面
 */
public class RouteAddActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private ImageView iv;
    private TextView tv_route_lines;
    private EditText et_code;
    private EditText et_title;
    private EditText et_content;
    private com.tourism.my.tourismmanagement.widget.MyListView lv;
    private TextView tv_photo;
    private TextView tv_send;
    private TextView tv_save;

    private String routeId;//当前路线的ID  取时间戳
    private List<Spot> spotList;
    private RouteSpotAdapter adapter;
    private String imgPath;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_add);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_route_lines = (TextView) findViewById(R.id.tv_route_lines);
        et_code = (EditText) findViewById(R.id.et_code);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        lv = (com.tourism.my.tourismmanagement.widget.MyListView) findViewById(R.id.lv);
        iv = (ImageView) findViewById(R.id.iv);
        tv_photo = (TextView) findViewById(R.id.tv_photo);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_save = (TextView) findViewById(R.id.tv_save);

        iv_back.setOnClickListener(this);
        tv_photo.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        tv_save.setOnClickListener(this);

        et_code.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
        et_title.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
        et_content.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
        et_code.setEnabled(true);
        et_title.setEnabled(true);
        et_content.setEnabled(true);

        //当前路线的ID  取时间戳
        if (TextUtils.isEmpty(routeId)) {
            routeId = System.currentTimeMillis() + "";
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                // 添加景点图片
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();

                imgPath = cursor.getString(1); // 图片文件路径

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);

                iv.setImageBitmap(bitmap);
            } else if (requestCode == 200) {
                // 添加景点 回来刷新界面
                initLv();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initLv() {
        // 刷新lv
        spotList = DBManager.getSpotByRouteId(this, routeId);
        if (adapter == null) {
            adapter = new RouteSpotAdapter(this, spotList);
            lv.setAdapter(adapter);
        } else {
            adapter.setData(spotList);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_photo: // 添加景点图片
                // 打开系统文件
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent1, 100);
                break;
            case R.id.tv_send: // 添加景点
                Intent intent2 = new Intent(this, RouteChooseSpotActivity.class);
                startActivityForResult(intent2, 200);
                break;
            case R.id.tv_save:  // 保存
                String code = et_code.getText().toString().trim();
                String title = et_title.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast(this, "请输入编号");
                    return;
                } else if (TextUtils.isEmpty(title)) {
                    ToastUtil.showToast(this, "请输入名称");
                    return;
                } else if (TextUtils.isEmpty(content)) {
                    ToastUtil.showToast(this, "请输入简介");
                    return;
                }
                showProgressDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(RouteAddActivity.this, "保存成功");
                        dissmissProgressDialog();
                        finish();
                    }
                }, 1500);

                String filePath = "";
                if (!TextUtils.isEmpty(imgPath)) {
                    filePath = imgPath;
                }
                // 保存到数据库
                //Route(String name, String jianjie, String routeId, String filePath, String code, String routeLines)
                DBManager.saveRoute(this, new Route(title, content, routeId, filePath, code, tv_route_lines.getText().toString()));
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
