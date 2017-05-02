package com.tourism.my.tourismmanagement.db.db.model;


import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Id;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Table;

@Table(name = "tb_me")
public class Me {

	@Id
	private int _id;

	private long id;
	private String sex;
	private String nickName;
	private String photoPath;
	private String age;

	public Me() {
	}

	public Me(String sex, String nickName, String photoPath, String age) {
		super();
		this.sex = sex;
		this.nickName = nickName;
		this.photoPath = photoPath;
		this.age = age;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

}
