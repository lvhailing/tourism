package com.tourism.my.tourismmanagement.db.db.model;

import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Id;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Table;

import java.io.Serializable;


/**
 * 路线类
 */
@Table(name = "tb_route")
public class Route implements Serializable {
    @Id
    private int _id;

    private String routeId;    //路线id
    private String name;    //名称
    private String jianjie;    //简介
    private String filePath;    //路线图片
    private String code;    //路线编号
    private String routeLines;    //路线编号

    public Route() {
    }

    public Route(String routeId, String name, String jianjie, String filePath, String code, String routeLines) {
        this.routeId = routeId;
        this.name = name;
        this.jianjie = jianjie;
        this.filePath = filePath;
        this.code = code;
        this.routeLines = routeLines;
    }

    public String getRouteLines() {
        return routeLines;
    }

    public void setRouteLines(String routeLines) {
        this.routeLines = routeLines;
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
