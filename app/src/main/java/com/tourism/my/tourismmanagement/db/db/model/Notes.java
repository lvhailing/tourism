package com.tourism.my.tourismmanagement.db.db.model;

import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Id;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * 游记实体类
 */
@Table(name = "tb_notes")
public class Notes implements Serializable {
    @Id
    private int _id;

    private long id;
    private long forumId;    //主题id  肯和主题绑定
    private String account;    //编写者账号
    private String time;
    private String title;
    private String cotent;
    private String myorder;
    private String filePath;

    public Notes() {
    }

    public Notes(long id, long forumId, String account, String time, String title, String cotent, String myorder, String filePath) {
        this.id = id;
        this.forumId = forumId;
        this.account = account;
        this.time = time;
        this.filePath = filePath;
        this.title = title;
        this.cotent = cotent;
        this.myorder = myorder;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getForumId() {
        return forumId;
    }

    public void setForumId(long forumId) {
        this.forumId = forumId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCotent() {
        return cotent;
    }

    public void setCotent(String cotent) {
        this.cotent = cotent;
    }

    public String getMyorder() {
        return myorder;
    }

    public void setMyorder(String myorder) {
        this.myorder = myorder;
    }

}
