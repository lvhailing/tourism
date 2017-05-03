package com.tourism.my.tourismmanagement.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.User;
import com.tourism.my.tourismmanagement.utils.ToastUtil;


/**
 * 注册页面
 */
public class SignActivity extends Activity implements View.OnClickListener {
    private EditText et1, et2, et3;
    private Button btn_sign;
    private ImageView iv_back;
    private TextView tv_title;
    //    private RadioButton rbtn_tourist, rbtn_administrator;
    private RadioGroup rg;
    private RadioButton radioButton;
    private String role = ""; // 游客，管理员

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        bindViews();
    }

    private void bindViews() {
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        rg = (RadioGroup) findViewById(R.id.rg);
//        rbtn_tourist = (RadioButton) findViewById(R.id.rbtn_tourist);
//        rbtn_administrator = (RadioButton) findViewById(R.id.rbtn_administrator);
        btn_sign = (Button) findViewById(R.id.btn_sign);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText("注册");

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRadioBtn(group);
            }
        });
        btn_sign.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void selectRadioBtn(RadioGroup group) {
        radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
        String selectText = radioButton.getText().toString();
        Log.i("dd", "当前选中的是：" + role);
        //角色 1 游客  2管理员
        if (selectText.equals("游客")) {
            role = "1";
        } else {
            role = "2";
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: //返回的点击监听
                finish();
                break;
            case R.id.btn_sign: // 注册
                String zh = et1.getText().toString().trim();
                String pwd = et2.getText().toString().trim();
                String pwd2 = et3.getText().toString().trim();

                if (TextUtils.isEmpty(zh)) {
                    ToastUtil.showToast(SignActivity.this, "帐号不能为空");
                    return;
                }

                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd2)) {
                    ToastUtil.showToast(SignActivity.this, "密码不能为空");
                    return;
                }

                if (!pwd.equals(pwd2)) {
                    ToastUtil.showToast(SignActivity.this, "两次输入的密码不一致");
                    return;
                }

                if (TextUtils.isEmpty(role)) {
                    ToastUtil.showToast(SignActivity.this, "请选择注册身份");
                    return;
                }

                if (DBManager.checkSign(SignActivity.this, zh)) {
                    // 如果已经注册过提示
                    ToastUtil.showToast(SignActivity.this, "已经注册过，请登录，或修改密码");
                    return;
                }

                // 保存到数据库
                DBManager.saveUser(SignActivity.this, new User(zh, pwd, role));
                ToastUtil.showToast(SignActivity.this, "注册成功");
                finish();
            default:
                break;
        }

    }
}
