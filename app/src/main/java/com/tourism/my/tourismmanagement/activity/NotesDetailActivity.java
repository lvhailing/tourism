package com.tourism.my.tourismmanagement.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.adapter.NotesDeatilAdapter;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Notes;
import com.tourism.my.tourismmanagement.db.db.model.NotesDetail;
import com.tourism.my.tourismmanagement.utils.SPUtil;
import com.tourism.my.tourismmanagement.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 游记详情页
 */
public class NotesDetailActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private EditText et_title, et_content;
    private GridView gridView;
    private TextView tv_edit, tv_time, tv_name;
    private TextView tv_del;
    private TextView tv_right;
    private LinearLayout ll_btm;

    private NotesDeatilAdapter adapter;
    private Notes notes;
    private List<NotesDetail> list;
    private long forumId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detail);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_name = (TextView) findViewById(R.id.tv_name);
        gridView = (GridView) findViewById(R.id.gv);
        ll_btm = (LinearLayout) findViewById(R.id.ll_btm);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_del = (TextView) findViewById(R.id.tv_del);
        tv_right = (TextView) findViewById(R.id.tv_right);

        iv_back.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        forumId = getIntent().getLongExtra("forumId", 0);
        notes = (Notes) getIntent().getSerializableExtra("notes");

        //判断身份 1游客 2管理员
        role = SPUtil.get(this, "role");
        if (role.equals("2")) {
            //2管理员
            tv_right.setVisibility(View.VISIBLE);
            ll_btm.setVisibility(View.GONE);
        } else if (notes.getAccount().equals(SPUtil.get(this, "account"))) {
            //是游客，且是自己
            ll_btm.setVisibility(View.VISIBLE);
            tv_right.setVisibility(View.GONE);
        } else {
            //是游客，不是自己的游记
            ll_btm.setVisibility(View.GONE);
            tv_right.setVisibility(View.GONE);
        }

        initLv();
    }

    private void initLv() {
        if (notes == null) {
            ToastUtil.showToast(this, "出现错误");
            return;
        }
        et_title.setText(notes.getTitle());
        et_content.setText(notes.getCotent());
        tv_time.setText(notes.getTime().substring(5));
        tv_name.setText("创建人：" + notes.getAccount());

        // 刷新lv
        list = DBManager.getNotesDetailById(this, notes.getId());
        if (adapter == null) {
            adapter = new NotesDeatilAdapter(this, list, notes.getAccount());
            gridView.setAdapter(adapter);
        } else {
            adapter.setData(list);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:   //删除游记
            case R.id.tv_del:   //删除游记
                dialog();
                break;
            case R.id.tv_edit:   //编辑游记
                String name = tv_edit.getText().toString();
                if (name.equals("编辑")) {
                    tv_edit.setText("保存");
                    et_title.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
                    et_content.setBackground(getResources().getDrawable(R.drawable.et_rect_bg));
//				et_title.setText("");
//				et_content.setText("");
                    et_title.setEnabled(true);
                    et_content.setEnabled(true);

                    gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog2(position);
                            return false;
                        }
                    });
                    return;
                }
                String title = et_title.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.showToast(NotesDetailActivity.this, "请输入标题");
                    return;
                } else if (TextUtils.isEmpty(content)) {
                    ToastUtil.showToast(NotesDetailActivity.this, "请输入内容");
                    return;
                }
                showProgressDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(NotesDetailActivity.this, "保存成功");
                        dissmissProgressDialog();
                        finish();
                    }
                }, 1500);
                // 保存到数据库
                String time = times();
                DBManager.delNotes(this, notes); // 先删除原先的 防止重复
                DBManager.saveNotes(this, new Notes(notes.getId(), forumId, SPUtil.get(this, "account"), time, title, content, new Date().getTime() + "", notes.getFilePath()));
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

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除这篇游记记吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 从数据库删除这一项
                DBManager.delNotes(NotesDetailActivity.this, notes);
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    protected void dialog2(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除这张照片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 从数据库删除这一项
                DBManager.delNotesDetail(NotesDetailActivity.this, list.get(pos));
                dialog.dismiss();
                // 刷新lv
                list = DBManager.getNotesDetailById(NotesDetailActivity.this, notes.getId());
                if (adapter == null) {
                    adapter = new NotesDeatilAdapter(NotesDetailActivity.this, list, notes.getAccount());
                    gridView.setAdapter(adapter);
                } else {
                    adapter.setData(list);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
