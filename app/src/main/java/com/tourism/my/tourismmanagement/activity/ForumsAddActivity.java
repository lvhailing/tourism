package com.tourism.my.tourismmanagement.activity;

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
import com.tourism.my.tourismmanagement.db.db.model.Forums;
import com.tourism.my.tourismmanagement.db.db.model.NotesDetail;
import com.tourism.my.tourismmanagement.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *  论坛添加
 */
public class ForumsAddActivity extends Activity implements View.OnClickListener {
	private ImageView iv_back;
	private TextView tv_save;
	private EditText et_title, et_content;
	private ProgressDialog progDialog = null;
	private long id = 0;
	private NotesDeatilAdapter adapter;
	private List<NotesDetail> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forum_add);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		et_title = (EditText) findViewById(R.id.et_title);
		et_content = (EditText) findViewById(R.id.et_content);

		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);

	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_save:  // 保存
			String title = et_title.getText().toString().trim();
			String content = et_content.getText().toString().trim();
			if (TextUtils.isEmpty(title)) {
				ToastUtil.showToast(ForumsAddActivity.this, "请输入标题");
				return;
			} else if (TextUtils.isEmpty(content)) {
				ToastUtil.showToast(ForumsAddActivity.this, "请输入内容");
				return;
			}
			showProgressDialog();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					ToastUtil.showToast(ForumsAddActivity.this, "保存成功");
					dissmissProgressDialog();
					finish();
				}
			}, 1500);
			// 保存到数据库
			String time = times();
			if (id == 0) {
				id = System.currentTimeMillis();
			}
			DBManager.saveForums(this, new Forums(id, time, title, content, new Date().getTime() + ""));
			break;
		}
	}

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
