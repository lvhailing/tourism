package com.tourism.my.tourismmanagement.db.db.model;

import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Id;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * 论坛实体类
 * 
 */
@Table(name = "tb_forum")
public class Forums implements Serializable{
	@Id
	private int _id;

	private long id;
	private String time;
	private String title;
	private String cotent;
	private String myorder;

	public Forums() {
	}

	public Forums(long id, String time, String title, String cotent, String myorder) {
		this.id = id;
		this.time = time;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
