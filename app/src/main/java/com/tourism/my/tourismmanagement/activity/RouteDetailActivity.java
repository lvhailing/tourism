package com.tourism.my.tourismmanagement.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.adapter.RouteSpotAdapter;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Route;
import com.tourism.my.tourismmanagement.db.db.model.Spot;
import com.tourism.my.tourismmanagement.utils.PhotoUtils;
import com.tourism.my.tourismmanagement.utils.SPUtil;
import com.tourism.my.tourismmanagement.utils.ToastUtil;

import java.util.List;
import java.util.Map;

/**
 * 景点添加页面
 */
public class RouteDetailActivity extends Activity implements View.OnClickListener {
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
    private TextView tv_right;
    private LinearLayout ll_btm;

    private String routeId;//当前路线的ID  取时间戳
    private List<Spot> spotList;
    private RouteSpotAdapter adapter;
    private String imgPath;
    private Route routeDetail;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

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
        tv_right = (TextView) findViewById(R.id.tv_right);
        ll_btm = (LinearLayout) findViewById(R.id.ll_btm);

        iv_back.setOnClickListener(this);
        tv_photo.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_right.setOnClickListener(this);


        routeDetail = (Route) getIntent().getSerializableExtra("routeDetail");
        routeId = routeDetail.getRouteId();


        //判断身份 1游客 2管理员
        String role = SPUtil.get(this, "role");

        if (role.equals("1")) {
            tv_right.setVisibility(View.GONE);
        } else {
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //点了footer
                    if (position != spotList.size() - 1) {
                        dialog(spotList.get(position));
                    }
                    return true;
                }
            });
        }

        tv_route_lines.setText(routeDetail.getRouteLines());
        et_code.setText(routeDetail.getCode());
        et_title.setText(routeDetail.getName());
        et_content.setText(routeDetail.getJianjie());

        initLv();

        imgPath = routeDetail.getFilePath(); // 图片文件路径
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 6;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);

        iv.setImageBitmap(bitmap);

        View footer = LayoutInflater.from(this).inflate(R.layout.lv_footer, null);
        lv.addFooterView(footer);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //点了footer
                if (arg2 == spotList.size() - 1) {
                    return;
                }
                // 去详情
                Intent intent = new Intent(RouteDetailActivity.this, SpotsDetailActivity.class);
                intent.putExtra("spotDetail", spotList.get(arg2));
                intent.putExtra("fromRoute", true);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
        if (requestCode == 100) {
            Map<String, String> map = PhotoUtils.getData(this, data);
            imgPath = map.get("path");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 6;
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);

            iv.setImageBitmap(bitmap);
        } else if (requestCode == 200) {
            // 添加景点 回来刷新界面
            initLv();
        }
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initLv() {
        // 刷新lv
        spotList = DBManager.getSpotByRouteId(this, routeId);

        //设置景点线路连接
        String lines = getLines();
        tv_route_lines.setText(lines);

        if (adapter == null) {
            adapter = new RouteSpotAdapter(this, spotList);
            lv.setAdapter(adapter);
        } else {
            adapter.setData(spotList);
        }
    }

    private String getLines() {
        String lines = "";
        for (int i = 0; i < spotList.size(); i++) {
            if (i == spotList.size() - 1) {
                lines += spotList.get(i).getName();
            } else {
                lines += spotList.get(i).getName() + " - ";
            }
        }
        return lines;
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right: // 添加景点
                String name = tv_right.getText().toString();
                if (name.equals("编辑")) {
                    tv_right.setText("");

                    et_code.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
                    et_title.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
                    et_content.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
                    et_code.setEnabled(true);
                    et_title.setEnabled(true);
                    et_content.setEnabled(true);

                    ll_btm.setVisibility(View.VISIBLE);

                    tv_photo.setText("修改景点照片");
                    return;
                }
                break;
            case R.id.tv_photo: // 添加景点图片
                // 打开系统文件
                PhotoUtils.startImageActivity(this);
                break;
            case R.id.tv_send: // 添加景点
                Intent intent2 = new Intent(this, RouteChooseSpotActivity.class);
                intent2.putExtra("routeId", routeId);
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
                        ToastUtil.showToast(RouteDetailActivity.this, "保存成功");
                        dissmissProgressDialog();
                        finish();
                    }
                }, 1500);

                String filePath = "";
                if (!TextUtils.isEmpty(imgPath)) {
                    filePath = imgPath;
                }

                DBManager.delRouteById(this, routeId);//防止重复

                // 保存到数据库
                //Route(String routeId, String name, String jianjie, String filePath, String code, String routeLines)
                DBManager.saveRoute(this, new Route(routeId, title, content, filePath, code, tv_route_lines.getText().toString()));
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


    protected void dialog(final Spot spot) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除此景点吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 从数据库删除这一项
                DBManager.delSpot(RouteDetailActivity.this, spot);
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
