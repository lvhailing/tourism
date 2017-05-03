package com.tourism.my.tourismmanagement.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.utils.ToastUtil;

public class ForgetPwdActivity extends Activity implements View.OnClickListener {
	private EditText et2;
	private EditText et3;
	private Button btn_save;
	private ImageView iv_back;
	private TextView tv_title;

	 private String acount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd);

		acount = getIntent().getStringExtra("acount");
		bindViews();
	}

	private void bindViews() {
		et2 = (EditText) findViewById(R.id.et2);
		et3 = (EditText) findViewById(R.id.et3);
		btn_save = (Button) findViewById(R.id.save);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);

		tv_title.setText("重置密码");

		btn_save.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.save:
			String pwd = et2.getText().toString().trim();
			String pwd2 = et3.getText().toString().trim();

			if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd2)) {
				ToastUtil.showToast(ForgetPwdActivity.this, "密码不能为空");
				return;
			}

			if (!pwd.equals(pwd2)) {
				ToastUtil.showToast(ForgetPwdActivity.this, "两次输入的密码不一致");
				return;
			}

			// 修改数据库
			DBManager.changePwdByAccount(this, acount, pwd);
			ToastUtil.showToast(ForgetPwdActivity.this, "重置密码成功");
			finish();
			break;
		default:
			break;
		}

	}
}
