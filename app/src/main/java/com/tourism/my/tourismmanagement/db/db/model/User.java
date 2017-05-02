package com.tourism.my.tourismmanagement.db.db.model;


import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Id;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Table;

/**
 * 用户实体类
 */
@Table(name = "tb_user")
public class User {
    @Id
    private int _id;

    private String account;    //账号
    private String password;    //密码
    private String role;    //角色 1 游客  2管理员

    public User() {

    }

    public User(String account, String password,String role) {
        this.account = account;
        this.password = password;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
