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
import com.tourism.my.tourismmanagement.db.db.model.User;
import com.tourism.my.tourismmanagement.utils.ToastUtil;


/**
 * 注册页面
 */
public class SignActivity extends Activity implements View.OnClickListener {
    private EditText et1;
    private EditText et_nickname;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private Button btn_sign;
    private ImageView iv_back;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        bindViews();
    }

    private void bindViews() {
        et1 = (EditText) findViewById(R.id.et1);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);
        et5 = (EditText) findViewById(R.id.et5);
        btn_sign = (Button) findViewById(R.id.btn_sign);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText("注册");

        btn_sign.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: //返回的点击监听
                finish();
                break;
            case R.id.btn_sign: // 注册
                String zh = et1.getText().toString().trim();
                String nickName = et_nickname.getText().toString().trim();
                String pwd = et2.getText().toString().trim();
                String pwd2 = et3.getText().toString().trim();
                String question = et4.getText().toString().trim();
                String answer = et5.getText().toString().trim();

                if (TextUtils.isEmpty(zh)) {
                    ToastUtil.showToast(SignActivity.this, "学号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(nickName)) {
                    ToastUtil.showToast(SignActivity.this, "昵称不能为空");
                    return;
                }

               /* if (!zh.startsWith("14011")) {
                    ToastUtil.showToast(SignActivity.this, "学号不规范");
                    return;
                }*/

                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd2)) {
                    ToastUtil.showToast(SignActivity.this, "密码不能为空");
                    return;
                }

                if (!pwd.equals(pwd2)) {
                    ToastUtil.showToast(SignActivity.this, "两次输入的密码不一致");
                    return;
                }
                if (TextUtils.isEmpty(question)) {
                    ToastUtil.showToast(SignActivity.this, "密保问题不能为空");
                    return;
                }

                if (TextUtils.isEmpty(answer)) {
                    ToastUtil.showToast(SignActivity.this, "密保问题答案不能为空");
                    return;
                }

                if (DBManager.checkSign(SignActivity.this, zh)) {
                    // 如果已经注册过提示
                    ToastUtil.showToast(SignActivity.this, "已经注册过，请登录，或修改密码");
                    return;
                }

                // 保存到数据库
                DBManager.saveUser(SignActivity.this, new User(zh, pwd, nickName, question, answer));
                ToastUtil.showToast(SignActivity.this, "注册成功");
                finish();
            default:
                break;
        }

    }
}
