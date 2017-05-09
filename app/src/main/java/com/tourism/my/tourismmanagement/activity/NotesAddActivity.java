package com.tourism.my.tourismmanagement.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.adapter.NotesDeatilAdapter;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Notes;
import com.tourism.my.tourismmanagement.db.db.model.NotesDetail;
import com.tourism.my.tourismmanagement.utils.PhotoUtils;
import com.tourism.my.tourismmanagement.utils.ToastUtil;
import com.tourism.my.tourismmanagement.widget.MyListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NotesAddActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_send, tv_save;
    private EditText et_title, et_content;
    private ProgressDialog progDialog = null;
    private MyListView lv;
    private long id = 0;
    private NotesDeatilAdapter adapter;
    private List<NotesDetail> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_add);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        lv = (MyListView) findViewById(R.id.lv);

        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_send.setOnClickListener(this);

    }

    private void initLv() {
        // 刷新lv
        list = DBManager.getNotesDetailById(this, id);
        if (adapter == null) {
            adapter = new NotesDeatilAdapter(this, list);
            lv.setAdapter(adapter);
        } else {
            adapter.setData(list);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Map<String, String> map = PhotoUtils.getData(this, data);
            String imgPath = map.get("path");
            String fileName = map.get("name");

            if (id == 0) {
                id = System.currentTimeMillis();
            }

            DBManager.saveNotesDetail(getApplicationContext(), new NotesDetail(id, times(), fileName, imgPath, new Date().getTime()));

            initLv();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send: //上传文件
                // 打开系统文件
                PhotoUtils.startImageActivity(this);
                break;
            case R.id.tv_save:  // 保存
                String title = et_title.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.showToast(NotesAddActivity.this, "请输入标题");
                    return;
                } else if (TextUtils.isEmpty(content)) {
                    ToastUtil.showToast(NotesAddActivity.this, "请输入内容");
                    return;
                }
                showProgressDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(NotesAddActivity.this, "保存成功");
                        dissmissProgressDialog();
                        finish();
                    }
                }, 1500);
                // 保存到数据库
                String time = times();
                if (id == 0) {
                    id = System.currentTimeMillis();
                }

                // 拿到第一张照片
                String filePath = "";
                if (list != null && list.size() > 0) {
                    filePath = list.get(0).getFilePath();
                }
                DBManager.saveNotes(this, new Notes(id, time, title, content, new Date().getTime() + "", filePath));
                break;
        }
    }

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
}
