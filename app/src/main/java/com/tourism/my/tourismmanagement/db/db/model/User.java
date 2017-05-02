package com.tourism.my.tourismmanagement.db.db.model;

import com.example.educationalsystem.db.db.afinal.annotation.sqlite.Id;
import com.example.educationalsystem.db.db.afinal.annotation.sqlite.Table;

/**
 * 用户实体类
 */
@Table(name = "tb_user")
public class User {
    @Id
    private int _id;

    private String account;    //账号
    private String password;    //密码
    private String nickName;    //昵称
    private String question;  //密保问题
    private String answer;    //密保答案

    public User() {

    }

    public User(String account, String password,String nickName, String question, String answer) {
        this.account = account;
        this.password = password;
        this.nickName = nickName;
        this.question = question;
        this.answer = answer;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
