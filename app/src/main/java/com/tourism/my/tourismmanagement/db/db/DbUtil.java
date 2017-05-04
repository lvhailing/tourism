package com.tourism.my.tourismmanagement.db.db;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tourism.my.tourismmanagement.db.db.exception.DbException;
import com.tourism.my.tourismmanagement.db.db.afinal.IOUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * 
 * @author kaka 閺佺増宓佹惔鎾额吀閻炲棛琚?
 *
 */
public class DbUtil {

	// *************************************** create instance
	// ****************************************************

	/**
	 * key: dbName
	 */
	private static HashMap<String, DbUtil> daoMap = new HashMap<String, DbUtil>();

	private SQLiteDatabase database;
	private DaoConfig daoConfig;
	private boolean debug = false;
	private boolean allowTransaction = false;

	private DbUtil(DaoConfig config) {
		if (config == null) {
			throw new IllegalArgumentException("daoConfig may not be null");
		}
		this.database = createDatabase(config);
		this.daoConfig = config;
	}

	private synchronized static DbUtil getInstance(DaoConfig daoConfig) {
		DbUtil dao = daoMap.get(daoConfig.getDbName());
		if (dao == null) {
			dao = new DbUtil(daoConfig);
			daoMap.put(daoConfig.getDbName(), dao);
		} else {
			dao.daoConfig = daoConfig;
		}

		// update the database if needed
		SQLiteDatabase database = dao.database;
		int oldVersion = database.getVersion();
		int newVersion = daoConfig.getDbVersion();
		if (oldVersion != newVersion) {
			if (oldVersion != 0) {
				DbUpgradeListener upgradeListener = daoConfig
						.getDbUpgradeListener();
				if (upgradeListener != null) {
					upgradeListener.onUpgrade(dao, oldVersion, newVersion);
				} else {
					// try {
					// dao.dropDb();
					// } catch (DbException e) {
					// LogUtils.e(e.getMessage(), e);
					// }
				}
			}
			database.setVersion(newVersion);
		}

		return dao;
	}

	public static DbUtil create(Context context) {
		DaoConfig config = new DaoConfig(context);
		return getInstance(config);
	}

	public static DbUtil create(Context context, String dbName) {
		DaoConfig config = new DaoConfig(context);
		config.setDbName(dbName);
		return getInstance(config);
	}

	public static DbUtil create(Context context, String dbDir, String dbName) {
		DaoConfig config = new DaoConfig(context);
		config.setDbDir(dbDir);
		config.setDbName(dbName);
		return getInstance(config);
	}

	public static DbUtil create(Context context, String dbName, int dbVersion,
			DbUpgradeListener dbUpgradeListener) {
		DaoConfig config = new DaoConfig(context);
		config.setDbName(dbName);
		config.setDbVersion(dbVersion);
		config.setDbUpgradeListener(dbUpgradeListener);
		return getInstance(config);
	}

	public static DbUtil create(Context context, String dbDir, String dbName,
			int dbVersion, DbUpgradeListener dbUpgradeListener) {
		DaoConfig config = new DaoConfig(context);
		config.setDbDir(dbDir);
		config.setDbName(dbName);
		config.setDbVersion(dbVersion);
		config.setDbUpgradeListener(dbUpgradeListener);
		return getInstance(config);
	}

	public static DbUtil create(DaoConfig daoConfig) {
		return getInstance(daoConfig);
	}

	public DbUtil configDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	public DbUtil configAllowTransaction(boolean allowTransaction) {
		this.allowTransaction = allowTransaction;
		return this;
	}

	public SQLiteDatabase getDatabase() {

		if (!database.isOpen()) {
			this.database = createDatabase(this.daoConfig);
		}

		return database;
	}

	public DaoConfig getDaoConfig() {
		return daoConfig;
	}

	// ******************************************** config
	// ******************************************************

	public static class DaoConfig {
		private Context context;
		private String dbName = "homelink.db"; // default db name
		private int dbVersion = 1;
		private DbUpgradeListener dbUpgradeListener;

		private String dbDir;

		public DaoConfig(Context context) {
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

	public interface DbUpgradeListener {
		public void onUpgrade(DbUtil db, int oldVersion, int newVersion);
	}

	private SQLiteDatabase createDatabase(DaoConfig config) {
		SQLiteDatabase result = null;

		String dbDir = config.getDbDir();
		if (!TextUtils.isEmpty(dbDir)) {
			File dir = new File(dbDir);
			if (dir.exists() || dir.mkdirs()) {
				File dbFile = new File(dbDir, config.getDbName());
				result = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
			}
		} else {
			result = config.getContext().openOrCreateDatabase(
					config.getDbName(), 0, null);
		}
		return result;
	}

	// ************************************************ tools
	// ***********************************

	private long getLastAutoIncrementId(String tableName) throws DbException {
		long id = -1;
		Cursor cursor = rawQuery("SELECT seq FROM sqlite_sequence WHERE name='"
				+ tableName + "'", null);
		if (cursor != null) {
			try {
				if (cursor.moveToNext()) {
					id = cursor.getLong(0);
				}
			} catch (Throwable e) {
				throw new DbException(e);
			} finally {
				IOUtils.closeQuietly(cursor);
			}
		}
		return id;
	}

	// 閺佺増宓佹惔鎾圭箾閹恒儱鍙ч梻顓熸閺堢尨绱濋悽鐔锋嚒閸涖劍婀?
	public void close() {
		String dbName = this.daoConfig.getDbName();
		if (daoMap.containsKey(dbName)) {
			daoMap.remove(dbName);
			this.database.close();
		}
	}

	// ----------------------------------------------------閺佺増宓佹惔鎾存惙娴ｏ拷
	// 瀵拷婵拷----------------------------------------------------//
	private void debugSql(String sql) {
		if (debug) {
			DBlog.d(sql);
		}
	}

	private void checkConnection() {

		if (!database.isOpen()) {
			database = createDatabase(daoConfig);
		}

	}

	private Lock writeLock = new ReentrantLock();
	private volatile boolean writeLocked = false;

	private void beginTransaction() {
		if (allowTransaction) {
			database.beginTransaction();
		} else {
			writeLock.lock();
			writeLocked = true;
		}
	}

	private void setTransactionSuccessful() {
		if (allowTransaction) {
			database.setTransactionSuccessful();
		}
	}

	private void endTransaction() {
		if (allowTransaction) {
			database.endTransaction();
		}
		if (writeLocked) {
			writeLock.unlock();
			writeLocked = false;
		}
	}

	// public void execSQL(SqlInfo sqlInfo) throws DbException {
	// debugSql(sqlInfo.getSql());
	// try {
	// if (sqlInfo.getBindArgs() != null) {
	// database.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
	// } else {
	// database.execSQL(sqlInfo.getSql());
	// }
	// } catch (Throwable e) {
	// throw new DbException(e);
	// }
	// }

	public void execSQL(String sql) {
		checkConnection();
		debugSql(sql);
		try {
			database.execSQL(sql);
		} catch (Throwable e) {
			// throw new DbException(e);
		}
	}

	public void execSQL(String sql, Object[] bindArgs) {
		checkConnection();
		debugSql(sql);
		try {
			database.execSQL(sql, bindArgs);
		} catch (Throwable e) {
			// throw new DbException(e);
		}
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		checkConnection();
		debugSql(sql);
		return database.rawQuery(sql, selectionArgs);
		// try {
		// } catch (Throwable e) {
		// throw new DbException(e);
		// }
	}

	// 閸掔娀娅?
	public int delete(String table, String whereClause, String[] whereArgs) {
		checkConnection();

		int result = -1;

		result = database.delete(table, whereClause, whereArgs);

		return result;

	}

	// 閹绘帒鍙嗛弫鐗堝祦
	public long insert(String table, String nullColumnHack, ContentValues values) {
		checkConnection();

		long result = -1;

		result = database.insert(table, nullColumnHack, values);

		return result;
	}

	// 閺囧瓨鏌?
	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		checkConnection();

		int result = -1;

		result = database.update(table, values, whereClause, whereArgs);

		return result;

	}

	// ----------------------------------------------------閺佺増宓佹惔鎾存惙娴ｏ拷
	// 缂佹挻娼?----------------------------------------------------//

	// ///////////////////// temp cache
	// ////////////////////////////////////////////////////////////////
	private final FindTempCache findTempCache = new FindTempCache();

	private class FindTempCache {
		private FindTempCache() {
		}

		/**
		 * key: sql; value: find result
		 */
		private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();

		private long seq = 0;

		public void put(String sql, Object result) {
			if (sql != null && result != null) {
				cache.put(sql, result);
			}
		}

		public Object get(String sql) {
			return cache.get(sql);
		}

		public void setSeq(long seq) {
			if (this.seq != seq) {
				cache.clear();
				this.seq = seq;
			}
		}
	}

}
