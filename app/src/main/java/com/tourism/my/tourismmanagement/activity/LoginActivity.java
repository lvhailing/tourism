package com.tourism.my.tourismmanagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.utils.ToastUtil;


/**
 * 登录页面
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText et1;
    private EditText et2;
    private Button btn_login;
    private TextView tv_sign, tv_forget_psd;
    private String nickName;
    private String question;
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();
    }

    private void bindViews() {
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        btn_login = (Button) findViewById(R.id.login);
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        tv_forget_psd = (TextView) findViewById(R.id.tv_forget_psd);

        btn_login.setOnClickListener(this);
        tv_sign.setOnClickListener(this);
        tv_forget_psd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:  //登录
                String zh = et1.getText().toString().trim();
                String pwd = et2.getText().toString().trim();

                if (TextUtils.isEmpty(zh)) {
                    ToastUtil.showToast(LoginActivity.this, "账号不能为空");
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.showToast(LoginActivity.this, "密码不能为空");
                    return;
                }

                // 检查账号和密码是否正确
                if (!DBManager.checkLogin(LoginActivity.this, zh, pwd)) {
                    ToastUtil.showToast(LoginActivity.this, "学号或密码错误");
                    return;
                }
                // 登录成功去主界面
                Intent intent0 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent0);
                finish();
                break;
            case R.id.tv_sign: // 去注册界面
                Intent intent1 = new Intent(LoginActivity.this, SignActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_forget_psd: // 忘记密码
                String acount = et1.getText().toString().trim();

                if (TextUtils.isEmpty(acount)) {
                    ToastUtil.showToast(LoginActivity.this, "请先输入学号");
                    return;
                }

                if (!DBManager.checkSign(LoginActivity.this, acount)) {
                    ToastUtil.showToast(LoginActivity.this, "没有检测到这个用户");
                    return;
                }

                // 去验证密保问题界面
//                Intent intent2 = new Intent(LoginActivity.this, VerifySecurityQuestionActivity.class);
//                intent2.putExtra("acount", acount);
//                startActivity(intent2);
                break;
            default:
                break;
        }

    }
}
