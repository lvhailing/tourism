package com.tourism.my.tourismmanagement.db.db;

import android.content.Context;
import android.text.TextUtils;

import com.tourism.my.tourismmanagement.db.db.DbUtil.DbUpgradeListener;

/**
 * 
 * @author kaka 数据库配�?
 * 
 */
public class DbConfig {

	private Context context;
	private String dbName = "homelink.db"; // default db name
	private int dbVersion = 1;
	private DbUpgradeListener dbUpgradeListener;

	private String dbDir;

	public DbConfig(Context context) {
		this.context = context.getApplicationContext();
	}

	public Context getContext() {
		return context;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		if (!TextUtils.isEmpty(dbName)) {
			this.dbName = dbName;
		}
	}

	public int getDbVersion() {
		return dbVersion;
	}

	public void setDbVersion(int dbVersion) {
		this.dbVersion = dbVersion;
	}

	public DbUpgradeListener getDbUpgradeListener() {
		return dbUpgradeListener;
	}

	public void setDbUpgradeListener(DbUpgradeListener dbUpgradeListener) {
		this.dbUpgradeListener = dbUpgradeListener;
	}

	public String getDbDir() {
		return dbDir;
	}

	/**
	 * set database dir
	 * 
	 * @param dbDir
	 *            If dbDir is null or empty, use the app default db dir.
	 */
	public void setDbDir(String dbDir) {
		this.dbDir = dbDir;
	}

}
