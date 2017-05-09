package com.tourism.my.tourismmanagement.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import com.tourism.my.tourismmanagement.utils.ToastUtil;
import com.tourism.my.tourismmanagement.widget.MyListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 游记详情页
 */
public class NotesDetailActivity extends Activity implements View.OnClickListener {
	private ImageView iv_back;
	private EditText et_title, et_content;
	private MyListView lv;
	private TextView tv_save, tv_time;
	private NotesDeatilAdapter adapter;
	private Notes notes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes_detail);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		et_title = (EditText) findViewById(R.id.et_title);
		et_content = (EditText) findViewById(R.id.et_content);
		tv_time = (TextView) findViewById(R.id.tv_time);
		lv = (MyListView) findViewById(R.id.lv);

		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);

		initLv();
	}

	private void initLv() {
		notes = (Notes) getIntent().getSerializableExtra("notes");
		if (notes == null) {
			ToastUtil.showToast(this, "出现错误");
			return;
		}
		et_title.setText(notes.getTitle());
		et_content.setText(notes.getCotent());
		tv_time.setText("创建时间：" + notes.getTime());

		// 刷新lv
		List<NotesDetail> list = DBManager.getNotesDetailById(this, notes.getId());
		if (adapter == null) {
			adapter = new NotesDeatilAdapter(this,list);
			lv.setAdapter(adapter);
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
		case R.id.tv_save:
			String name = tv_save.getText().toString();
			if (name.equals("编辑")) {
				tv_save.setText("保存");
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
			DBManager.saveNotes(this,
					new Notes(notes.getId(), time, title, content, new Date().getTime() + "", notes.getFilePath()));
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
}
