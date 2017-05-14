package com.tourism.my.tourismmanagement.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.adapter.NotesAdapter;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Forums;
import com.tourism.my.tourismmanagement.db.db.model.Notes;
import com.tourism.my.tourismmanagement.utils.SPUtil;
import com.tourism.my.tourismmanagement.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 主题详情页
 */
public class ForumsDetailActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private EditText et_title, et_content;
    private ListView lv;
    private TextView tv_edit, tv_add, tv_del, tv_no;
    private LinearLayout ll_btm;

    private List<Notes> list;
    private NotesAdapter adapter;
    private Forums forum;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums_detail);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_del = (TextView) findViewById(R.id.tv_del);
        tv_add = (TextView) findViewById(R.id.tv_add);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
//        tv_time = (TextView) findViewById(tv_time);
        lv = (ListView) findViewById(R.id.lv);
        ll_btm = (LinearLayout) findViewById(R.id.ll_btm);
        ;
        tv_no = (TextView) findViewById(R.id.tv_no);

        iv_back.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        tv_add.setOnClickListener(this);

        initData();
    }

    private void initData() {
        forum = (Forums) getIntent().getSerializableExtra("forumDetail");
        et_title.setText(forum.getTitle());
        et_content.setText(forum.getCotent());
//        tv_time.setText("创建时间：" + forum.getTime());

        //判断身份 1游客 2管理员
        role = SPUtil.get(this, "role");
        if (role.equals("2")) {
            //2管理员
            tv_no.setText("暂无游记");
            tv_add.setVisibility(View.GONE);
            ll_btm.setVisibility(View.VISIBLE);
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                    // 长安删除
                    dialog(pos);
                    return true;
                }
            });
        } else {
            tv_add.setVisibility(View.VISIBLE);
            ll_btm.setVisibility(View.GONE);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // 去详情
                Intent intent = new Intent(ForumsDetailActivity.this, NotesDetailActivity.class);
                intent.putExtra("notes", list.get(arg2));
                intent.putExtra("forumId",forum.getId());
                startActivity(intent);
            }
        });

        // 刷新lv
        initLv();
    }

    private void initLv() {
        list = DBManager.getNotesByForum(this, forum.getId());
        if (list == null || list.size() <= 0) {
            tv_no.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            return;
        }
        tv_no.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new NotesAdapter(this, list);
            lv.setAdapter(adapter);
        } else {
            adapter.setData(list);
        }
    }

    @Override
    public void onResume() {
        initLv();
        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_del:   //删除此主题
                dialog2();
                break;
            case R.id.tv_add:   //添加游记
                Intent intent = new Intent(this, NotesAddActivity.class);
                intent.putExtra("forumId",forum.getId());
                startActivity(intent);
                break;
            case R.id.tv_edit:
                String name = tv_edit.getText().toString();
                if (name.equals("编辑")) {
                    tv_edit.setText("保存");
                    et_title.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
                    et_content.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
//				et_title.setText("");
//				et_content.setText("");
                    et_title.setEnabled(true);
                    et_content.setEnabled(true);
                    return;
                }
                String title = et_title.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.showToast(ForumsDetailActivity.this, "请输入标题");
                    return;
                } else if (TextUtils.isEmpty(content)) {
                    ToastUtil.showToast(ForumsDetailActivity.this, "请输入内容");
                    return;
                }
                showProgressDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(ForumsDetailActivity.this, "保存成功");
                        dissmissProgressDialog();
                        finish();
                    }
                }, 1500);
                // 保存到数据库
                String time = times();
                DBManager.delForums(this, forum); // 先删除原先的 防止重复
                DBManager.saveForums(this, new Forums(forum.getId(), time, title, content, new Date().getTime() + ""));
                break;
        }
    }

    private ProgressDialog progDialog = null;

    /**
     * 开启对话框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
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

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分"）
     *
     * @return
     */
    public static String times() {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        String times = sdr.format(new Date());
        return times;
    }

    protected void dialog(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除这篇游记记吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 从数据库删除这一项
                DBManager.delNotes(ForumsDetailActivity.this, list.get(pos));
                // 刷新界面
                initData();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    protected void dialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除这个主题吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 从数据库删除这一项
                DBManager.delForums(ForumsDetailActivity.this, forum);
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
