package com.tourism.my.tourismmanagement.db.db.model;

import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Id;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Table;

/**
 * 游记详情实体类
 * 
 */
@Table(name = "tb_notes_detail")
public class NotesDetail {
	@Id
	private int _id;

	private long id;
	private String time;
	private String fileName;
	private String filePath;
	private long myorder;

	public NotesDetail() {
	}

	public NotesDetail(long id, String time, String fileName, String filePath, long myorder) {
		this.id = id;
		this.filePath = filePath;
		this.time = time;
		this.fileName = fileName;
		this.myorder = myorder;
	}

	public int get_id() {
		return _id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getMyorder() {
		return myorder;
	}

	public void setMyorder(long myorder) {
		this.myorder = myorder;
	}
	
}
