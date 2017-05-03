package com.tourism.my.tourismmanagement.db.db.model;

import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Id;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Table;

import java.io.Serializable;


/**
 * 景点类
 */
@Table(name = "tb_diary")
public class Diary implements Serializable {
    @Id
    private int _id;

    private String name;    //景点名称
    private String jianjie;    //简介
    private String addr;    //地址
    private String routeId;    //路线id
    private String filePath;    //代表图片
    private String code;    //景点编号

    public Diary() {
    }

    public Diary(String name, String jianjie, String addr, String routeId, String filePath, String code) {
        this.name = name;
        this.jianjie = jianjie;
        this.addr = addr;
        this.routeId = routeId;
        this.filePath = filePath;
        this.code = code;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
