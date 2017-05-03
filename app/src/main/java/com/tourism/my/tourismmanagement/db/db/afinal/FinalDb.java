package com.tourism.my.tourismmanagement.db.db.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite.CursorUtils;
import com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite.DbModel;
import com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite.ManyToOneLazyLoader;
import com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite.OneToManyLazyLoader;
import com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite.SqlBuilder;
import com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite.SqlInfo;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.KeyValue;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.ManyToOne;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.OneToMany;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.TableInfo;
import com.tourism.my.tourismmanagement.db.db.afinal.exception.DbException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class FinalDb {

	private static final String TAG = "FinalDb";

	private static HashMap<String, FinalDb> daoMap = new HashMap<String, FinalDb>();

	private SQLiteDatabase db;
	private DaoConfig config;

	private FinalDb(DaoConfig config) {
		if (config == null)
			throw new DbException("daoConfig is null");
		if (config.getContext() == null)
			throw new DbException("android context is null");
		if (config.getTargetDirectory() != null
				&& config.getTargetDirectory().trim().length() > 0) {
			this.db = createDbFileOnSDCard(config.getTargetDirectory(),
					config.getDbName());
		} else {
			this.db = new SqliteDbHelper(config.getContext()
					.getApplicationContext(), config.getDbName(),
					config.getDbVersion(), config.getDbUpdateListener())
					.getWritableDatabase();
		}
		this.config = config;
	}

	private synchronized static FinalDb getInstance(DaoConfig daoConfig) {
		FinalDb dao = daoMap.get(daoConfig.getDbName());
		if (dao == null) {
			dao = new FinalDb(daoConfig);
			daoMap.put(daoConfig.getDbName(), dao);
		}
		return dao;
	}

	/**
	 * 鍒涘缓FinalDb
	 *
	 * @param context
	 */
	public static FinalDb create(Context context) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		return create(config);
	}

	/**
	 * 鍒涘缓FinalDb
	 *
	 * @param context
	 * @param isDebug
	 *            鏄惁鏄痙ebug妯???紡锛坉ebug妯???紡杩涜鏁版嵁搴撴搷浣滅殑鏃跺?欏皢浼氭墦鍗皊ql璇彞锛??
	 */
	public static FinalDb create(Context context, boolean isDebug) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDebug(isDebug);
		return create(config);

	}

	/**
	 * 鍒涘缓FinalDb
	 *
	 * @param context
	 * @param dbName
	 *            鏁版嵁搴撳悕绉?
	 */
	public static FinalDb create(Context context, String dbName) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		return create(config);
	}

	/**
	 * 鍒涘?? FinalDb
	 *
	 * @param context
	 * @param dbName
	 *            鏁版嵁搴撳悕绉?
	 * @param isDebug
	 *            鏄惁涓篸ebug妯???紡锛坉ebug妯???紡杩涜鏁版嵁搴撴搷浣滅殑鏃跺?欏皢浼氭墦鍗皊ql璇彞锛??
	 */
	public static FinalDb create(Context context, String dbName, boolean isDebug) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		config.setDebug(isDebug);
		return create(config);
	}

	/**
	 * 鍒涘缓FinalDb
	 *
	 * @param context
	 * @param dbName
	 *            鏁版嵁搴撳悕绉?
	 */
	public static FinalDb create(Context context, String targetDirectory,
			String dbName) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		config.setTargetDirectory(targetDirectory);
		return create(config);
	}

	/**
	 * 鍒涘?? FinalDb
	 *
	 * @param context
	 * @param dbName
	 *            鏁版嵁搴撳悕绉?
	 * @param isDebug
	 *            鏄惁涓篸ebug妯???紡锛坉ebug妯???紡杩涜鏁版嵁搴撴搷浣滅殑鏃跺?欏皢浼氭墦鍗皊ql璇彞锛??
	 */
	public static FinalDb create(Context context, String targetDirectory,
			String dbName, boolean isDebug) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setTargetDirectory(targetDirectory);
		config.setDbName(dbName);
		config.setDebug(isDebug);
		return create(config);
	}

	/**
	 * 鍒涘?? FinalDb
	 *
	 * @param context
	 *            涓婁笅鏂??
	 * @param dbName
	 *            鏁版嵁搴撳悕瀛?
	 * @param isDebug
	 *            鏄惁鏄皟璇曟ā寮忥細璋冭瘯妯???紡浼歭og鍑簊ql淇℃??
	 * @param dbVersion
	 *            鏁版嵁搴撶増鏈俊鎭??
	 * @param dbUpdateListener
	 *            鏁版嵁搴撳崌绾х洃鍚櫒锛氬鏋滅洃鍚??櫒涓簄ull锛屽崌绾х殑鏃跺?欏皢浼氭竻绌烘墍鎵?鏈夌殑鏁版嵁
	 * @return
	 */
	public static FinalDb create(Context context, String dbName,
			boolean isDebug, int dbVersion, DbUpdateListener dbUpdateListener) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		config.setDebug(isDebug);
		config.setDbVersion(dbVersion);
		config.setDbUpdateListener(dbUpdateListener);
		return create(config);
	}

	/**
	 *
	 * @param context
	 *            涓婁笅鏂??
	 * @param targetDirectory
	 *            db鏂囦欢璺緞锛屽彲浠ラ厤缃负sdcard鐨勮矾寰??
	 * @param dbName
	 *            鏁版嵁搴撳悕瀛?
	 * @param isDebug
	 *            鏄惁鏄皟璇曟ā寮忥細璋冭瘯妯???紡浼歭og鍑簊ql淇℃??
	 * @param dbVersion
	 *            鏁版嵁搴撶増鏈俊鎭??
	 * @return
	 */
	public static FinalDb create(Context context, String targetDirectory,
			String dbName, boolean isDebug, int dbVersion,
			DbUpdateListener dbUpdateListener) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setTargetDirectory(targetDirectory);
		config.setDbName(dbName);
		config.setDebug(isDebug);
		config.setDbVersion(dbVersion);
		config.setDbUpdateListener(dbUpdateListener);
		return create(config);
	}

	/**
	 * 鍒涘缓FinalDb
	 *
	 * @param daoConfig
	 * @return
	 */
	public static FinalDb create(DaoConfig daoConfig) {
		return getInstance(daoConfig);
	}

	/**
	 * 鑾峰彇鏁版嵁搴?
	 *
	 * @return
	 */
	public SQLiteDatabase getDb() {
		return db;
	}

	/**
	 * 淇濆瓨鏁版嵁搴擄紝閫熷害瑕佹瘮save蹇?
	 *
	 * @param entity
	 */
	public void save(Object entity) {
		checkTableExist(entity.getClass());
		exeSqlInfo(SqlBuilder.buildInsertSql(entity));
	}

	/**
	 * 淇濆瓨鏁版嵁搴擄紝閫熷害瑕佹瘮save蹇?
	 *
	 * @param entity
	 */
	public void saveEntity(Object entity) {
		// exeSqlInfo(SqlBuilder.buildInsertSql(entity));
		lhlExeSqlInfo(SqlBuilder.buildInsertSql(entity));
	}

	/**
	 * 淇濆瓨鏁版嵁鍒版暟鎹簱<br />
	 * <b>娉ㄦ剰锛??</b><br />
	 * 淇濆瓨鎴愬姛鍚庯紝entity鐨勪富閿皢琚祴鍊硷紙鎴栨洿鏂帮級涓烘暟鎹簱鐨勪富閿紝 鍙拡???硅嚜澧為暱鐨刬d鏈夋??
	 *
	 * @param entity
	 *            瑕佷繚???樼殑鏁版嵁
	 * @return ture锛? 淇濆瓨鎴愬姛 false:淇濆瓨澶辫触
	 */
	public boolean saveBindId(Object entity) {
		checkTableExist(entity.getClass());
		List<KeyValue> entityKvList = SqlBuilder
				.getSaveKeyValueListByEntity(entity);
		if (entityKvList != null && entityKvList.size() > 0) {
			TableInfo tf = TableInfo.get(entity.getClass());
			ContentValues cv = new ContentValues();
			insertContentValues(entityKvList, cv);
			Long id = db.insert(tf.getTableName(), null, cv);
			if (id == -1)
				return false;
			tf.getId().setValue(entity, id);
			return true;
		}
		return false;
	}

	/**
	 * 鎶奓ist<KeyValue>鏁版嵁???樺偍鍒癈ontentValues
	 *
	 * @param list
	 * @param cv
	 */
	private void insertContentValues(List<KeyValue> list, ContentValues cv) {
		if (list != null && cv != null) {
			for (KeyValue kv : list) {
				cv.put(kv.getKey(), kv.getValue().toString());
			}
		} else {
			Log.w(TAG,
					"insertContentValues: List<KeyValue> is empty or ContentValues is empty!");
		}

	}

	/**
	 * 鏇存柊鏁版嵁 锛堜富閿??D蹇呴』涓嶈兘涓虹┖锛??
	 *
	 * @param entity
	 */
	public void update(Object entity) {
		checkTableExist(entity.getClass());
		exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity));
	}

	/**
	 * 鏍规嵁鏉′欢鏇存柊鏁版嵁
	 *
	 * @param entity
	 * @param strWhere
	 *            鏉???欢涓虹┖鐨勬椂鍊欙紝灏嗕細鏇存柊鎵?鏈夌殑鏁版??
	 */
	public void update(Object entity, String strWhere) {
		checkTableExist(entity.getClass());
		exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity, strWhere));
	}

	/**
	 * 鍒犻櫎鏁版嵁
	 *
	 * @param entity
	 *            entity鐨勪富閿笉鑳戒负绌??
	 */
	public void delete(Object entity) {
		checkTableExist(entity.getClass());
		exeSqlInfo(SqlBuilder.buildDeleteSql(entity));
	}

	/**
	 * 鏍规嵁涓婚敭鍒犻櫎鏁版嵁
	 *
	 * @param clazz
	 *            瑕佸垹闄ょ殑瀹炰綋绫??
	 * @param id
	 *            涓婚敭鍊??
	 */
	public void deleteById(Class<?> clazz, Object id) {
		checkTableExist(clazz);
		exeSqlInfo(SqlBuilder.buildDeleteSql(clazz, id));
	}

	/**
	 * 鏍规嵁鏉′欢鍒犻櫎鏁版嵁
	 *
	 * @param clazz
	 * @param strWhere
	 *            鏉???欢涓虹┖鐨勬椂鍊? 灏嗕細鍒犻櫎鎵?鏈夌殑鏁版嵁
	 */
	public void deleteByWhere(Class<?> clazz, String strWhere) {
		checkTableExist(clazz);
		String sql = SqlBuilder.buildDeleteSql(clazz, strWhere);
		// debugSql(sql);
		db.execSQL(sql);
	}

	/**
	 * 鍒犻櫎琛ㄧ殑鎵?鏈夋暟鎹??
	 *
	 * @param clazz
	 */
	public void deleteAll(Class<?> clazz) {
		checkTableExist(clazz);
		String sql = SqlBuilder.buildDeleteSql(clazz, null);
		// debugSql(sql);
		db.execSQL(sql);
	}

	/**
	 * 鍒犻櫎鎸囧畾鐨勮??
	 *
	 * @param clazz
	 */
	public void dropTable(Class<?> clazz) {
		checkTableExist(clazz);
		TableInfo table = TableInfo.get(clazz);
		String sql = "DROP TABLE " + table.getTableName();
		// debugSql(sql);
		db.execSQL(sql);
	}

	/**
	 * 鍒犻櫎鎵?鏈夋暟鎹??
	 */
	public void dropDb(SQLiteDatabase db) {
		Cursor cursor = db
				.rawQuery(
						"SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'",
						null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				db.execSQL("DROP TABLE IF EXISTS " + cursor.getString(0));
			}
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}

	public void exeSqlInfo(SqlInfo sqlInfo) {
		if (sqlInfo != null) {
			// debugSql(sqlInfo.getSql());
			db.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
		} else {
			Log.e(TAG, "sava error:sqlInfo is null");
		}
	}

	public void lhlExeSqlInfo(SqlInfo sqlInfo) {
		if (sqlInfo != null) {
			db.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
		} else {
			Log.e(TAG, "sava error:sqlInfo is null");
		}
	}

	/**
	 * 鏍规嵁涓婚敭鏌ユ壘鏁版嵁锛堥粯璁や笉鏌ヨ澶氬涓?鎴栬?呬竴???瑰鐨勫叧鑱旀暟鎹級
	 *
	 * @param id
	 * @param clazz
	 */
	public <T> T findById(Object id, Class<T> clazz) {
		checkTableExist(clazz);
		SqlInfo sqlInfo = SqlBuilder.getSelectSqlAsSqlInfo(clazz, id);
		if (sqlInfo != null) {
			debugSql(sqlInfo.getSql());
			Cursor cursor = db.rawQuery(sqlInfo.getSql(),
					sqlInfo.getBindArgsAsStringArray());
			try {
				if (cursor.moveToNext()) {
					return CursorUtils.getEntity(cursor, clazz, this);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return null;
	}

	/**
	 * 鏍规嵁涓婚敭鏌ユ壘锛屽悓鏃舵煡鎵锯?滃瀵???竴鈥濈殑鏁版嵁锛堝鏋滄湁澶氫釜鈥滃瀵???竴鈥濆睘鎬э紝鍒欐煡鎵炬墍鏈夌殑鈥滃??????竴鈥濆睘鎬э??
	 *
	 * @param id
	 * @param clazz
	 */
	public <T> T findWithManyToOneById(Object id, Class<T> clazz) {
		checkTableExist(clazz);
		String sql = SqlBuilder.getSelectSQL(clazz, id);
		// debugSql(sql);
		DbModel dbModel = findDbModelBySQL(sql);
		if (dbModel != null) {
			T entity = CursorUtils.dbModel2Entity(dbModel, clazz);
			return loadManyToOne(dbModel, entity, clazz);
		}

		return null;
	}

	/**
	 * 鏍规嵁鏉′欢鏌ユ壘锛屽悓鏃舵煡鎵锯?滃瀵???竴鈥濈殑鏁版嵁锛堝彧鏌ユ壘findClass涓殑绫荤殑鏁版嵁锛??
	 *
	 * @param id
	 * @param clazz
	 * @param findClass
	 *            瑕佹煡鎵剧殑绫?
	 */
	public <T> T findWithManyToOneById(Object id, Class<T> clazz,
			Class<?>... findClass) {
		checkTableExist(clazz);
		String sql = SqlBuilder.getSelectSQL(clazz, id);
		// debugSql(sql);
		DbModel dbModel = findDbModelBySQL(sql);
		if (dbModel != null) {
			T entity = CursorUtils.dbModel2Entity(dbModel, clazz);
			return loadManyToOne(dbModel, entity, clazz, findClass);
		}
		return null;
	}

	/**
	 * 灏唀ntity涓殑鈥滃瀵???竴鈥濈殑鏁版嵁濉厖婊?? 濡傛灉鏄噿鍔犺浇濉厖锛屽垯dbModel鍙傛暟鍙负null
	 *
	 * @param clazz
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> T loadManyToOne(DbModel dbModel, T entity, Class<T> clazz,
			Class<?>... findClass) {
		if (entity != null) {
			try {
				Collection<ManyToOne> manys = TableInfo.get(clazz).manyToOneMap
						.values();
				for (ManyToOne many : manys) {

					Object id = null;
					if (dbModel != null) {
						id = dbModel.get(many.getColumn());
					} else if (many.getValue(entity).getClass() == ManyToOneLazyLoader.class
							&& many.getValue(entity) != null) {
						id = ((ManyToOneLazyLoader) many.getValue(entity))
								.getFieldValue();
					}

					if (id != null) {
						boolean isFind = false;
						if (findClass == null || findClass.length == 0) {
							isFind = true;
						}
						for (Class<?> mClass : findClass) {
							if (many.getManyClass() == mClass) {
								isFind = true;
								break;
							}
						}
						if (isFind) {

							@SuppressWarnings("unchecked")
							T manyEntity = (T) findById(
									Integer.valueOf(id.toString()),
									many.getManyClass());
							if (manyEntity != null) {
								if (many.getValue(entity).getClass() == ManyToOneLazyLoader.class) {
									if (many.getValue(entity) == null) {
										many.setValue(
												entity,
												new ManyToOneLazyLoader(entity,
														clazz,
														many.getManyClass(),
														this));
									}
									((ManyToOneLazyLoader) many
											.getValue(entity)).set(manyEntity);
								} else {
									many.setValue(entity, manyEntity);
								}

							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

	/**
	 * 鏍规嵁涓婚敭鏌ユ壘锛屽悓鏃舵煡鎵锯???竴???瑰鈥濈殑鏁版嵁锛堝鏋滄湁澶氫釜鈥滀竴???瑰鈥濆睘鎬э紝鍒欐煡鎵炬墍鏈夌殑涓????瑰鈥濆睘鎬э級
	 *
	 * @param id
	 * @param clazz
	 */
	public <T> T findWithOneToManyById(Object id, Class<T> clazz) {
		checkTableExist(clazz);
		String sql = SqlBuilder.getSelectSQL(clazz, id);
		// debugSql(sql);
		DbModel dbModel = findDbModelBySQL(sql);
		if (dbModel != null) {
			T entity = CursorUtils.dbModel2Entity(dbModel, clazz);
			return loadOneToMany(entity, clazz);
		}

		return null;
	}

	/**
	 * 鏍规嵁涓婚敭鏌ユ壘锛屽悓鏃舵煡鎵锯???竴???瑰鈥濈殑鏁版嵁锛堝彧鏌ユ壘findClass涓殑鈥??竴???瑰鈥濓級
	 *
	 * @param id
	 * @param clazz
	 * @param findClass
	 */
	public <T> T findWithOneToManyById(Object id, Class<T> clazz,
			Class<?>... findClass) {
		checkTableExist(clazz);
		String sql = SqlBuilder.getSelectSQL(clazz, id);
		// debugSql(sql);
		DbModel dbModel = findDbModelBySQL(sql);
		if (dbModel != null) {
			T entity = CursorUtils.dbModel2Entity(dbModel, clazz);
			return loadOneToMany(entity, clazz, findClass);
		}

		return null;
	}

	/**
	 * 灏唀ntity涓殑鈥??竴???瑰鈥濈殑鏁版嵁濉厖婊?
	 *
	 * @param entity
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T loadOneToMany(T entity, Class<T> clazz, Class<?>... findClass) {
		if (entity != null) {
			try {
				Collection<OneToMany> ones = TableInfo.get(clazz).oneToManyMap
						.values();
				Object id = TableInfo.get(clazz).getId().getValue(entity);
				for (OneToMany one : ones) {
					boolean isFind = false;
					if (findClass == null || findClass.length == 0) {
						isFind = true;
					}
					for (Class<?> mClass : findClass) {
						if (one.getOneClass() == mClass) {
							isFind = true;
							break;
						}
					}

					if (isFind) {
						List<?> list = findAllByWhere(one.getOneClass(),
								one.getColumn() + "=" + id);
						if (list != null) {
							/* 濡傛灉鏄疧neToManyLazyLoader娉涘瀷锛屽垯鎵ц鐏屽叆鎳掑姞杞芥暟鎹?? */
							if (one.getDataType() == OneToManyLazyLoader.class) {
								OneToManyLazyLoader oneToManyLazyLoader = one
										.getValue(entity);
								oneToManyLazyLoader.setList(list);
							} else {
								one.setValue(entity, list);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

	/**
	 * 鏌ユ壘鎵?鏈夌殑鏁版??
	 *
	 * @param clazz
	 */
	public <T> List<T> findAll(Class<T> clazz) {
		checkTableExist(clazz);
		return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz));
	}

	/**
	 * 鏌ユ壘鎵?鏈夋暟鎹?
	 *
	 * @param clazz
	 * @param orderBy
	 *            鎺掑簭鐨勫瓧娈?
	 */
	public <T> List<T> findAll(Class<T> clazz, String orderBy) {
		checkTableExist(clazz);
		return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz)
				+ " ORDER BY " + orderBy);
	}

	/**
	 * 鏌ユ壘鎵?鏈夋暟鎹?
	 *
	 * @param clazz
	 * @param orderBy
	 *            鎺掑簭鐨勫瓧娈?
	 */
	public <T> List<T> findAll(Class<T> clazz, String orderBy, String limit) {
		checkTableExist(clazz);
		return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz)
				+ " ORDER BY " + orderBy + " " + limit);
	}

	/**
	 * 鏍规嵁鏉′欢鏌ユ壘鎵?鏈夋暟鎹?
	 *
	 * @param clazz
	 * @param strWhere
	 *            鏉???欢涓虹┖鐨勬椂鍊欐煡鎵炬墍鏈夋暟鎹??
	 */
	public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere) {
		checkTableExist(clazz);
		return findAllBySql(clazz,
				SqlBuilder.getSelectSQLByWhere(clazz, strWhere));
	}

	/**
	 * 鏍规嵁鏉′欢鏌ユ壘鎵?鏈夋暟鎹?
	 *
	 * @param clazz
	 * @param strWhere
	 *            鏉???欢涓虹┖鐨勬椂鍊欐煡鎵炬墍鏈夋暟鎹??
	 * @param orderBy
	 *            鎺掑簭???楁??
	 */
	public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere,
			String orderBy) {
		checkTableExist(clazz);
		return findAllBySql(clazz,
				SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
						+ orderBy);
	}

	/**
	 * 鏍规嵁鏉′欢鏌ユ壘鎵?鏈夋暟鎹?
	 *
	 * @param clazz
	 * @param strWhere
	 *            鏉???欢涓虹┖鐨勬椂鍊欐煡鎵炬墍鏈夋暟鎹??
	 * @param orderBy
	 *            鎺掑簭???楁??
	 */
	public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere,
			String orderBy, String limit) {
		checkTableExist(clazz);
		return findAllBySql(clazz,
				SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
						+ orderBy + " " + limit);
	}

	/**
	 * 鏍规嵁鏉′欢鏌ユ壘鎵?鏈夋暟鎹?
	 *
	 * @param clazz
	 * @param strSQL
	 */
	private <T> List<T> findAllBySql(Class<T> clazz, String strSQL) {
		Cursor cursor = null;
		try {
			checkTableExist(clazz);
			debugSql(strSQL);
			cursor = db.rawQuery(strSQL, null);
			List<T> list = new ArrayList<T>();
			while (cursor.moveToNext()) {
				T t = CursorUtils.getEntity(cursor, clazz, this);
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			cursor = null;
		}
		return null;
	}

	/**
	 * 鏍规嵁sql璇彞鏌ユ壘鏁版嵁锛岃繖涓竴鑸敤浜庢暟鎹粺璁?
	 *
	 * @param strSQL
	 */
	public DbModel findDbModelBySQL(String strSQL) {
		debugSql(strSQL);
		Cursor cursor = db.rawQuery(strSQL, null);
		try {
			if (cursor.moveToNext()) {
				return CursorUtils.getDbModel(cursor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return null;
	}

	public List<DbModel> findDbModelListBySQL(String strSQL) {
		debugSql(strSQL);
		Cursor cursor = db.rawQuery(strSQL, null);
		List<DbModel> dbModelList = new ArrayList<DbModel>();
		try {
			while (cursor.moveToNext()) {
				dbModelList.add(CursorUtils.getDbModel(cursor));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return dbModelList;
	}

	public void checkTableExist(Class<?> clazz) {
		if (!tableIsExist(TableInfo.get(clazz))) {
			String sql = SqlBuilder.getCreatTableSQL(clazz);
			// debugSql(sql);
			db.execSQL(sql);
		}
	}

	public boolean tableIsExist(TableInfo table) {
		// if (table.isCheckDatabese())
		// return true;

		Cursor cursor = null;
		try {
			String sql = "SELECT COUNT(*) AS c FROM sqlite_master "
					+ "WHERE type ='table' AND name ='" + table.getTableName()
					+ "' ";
			// debugSql(sql);
			cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					table.setCheckDatabese(true);
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			cursor = null;
		}

		return false;
	}

	private void debugSql(String sql) {
		if (config != null && config.isDebug())
			Log.d("Debug SQL", ">>>>>>  " + sql);
	}

	public static class DaoConfig {
		private Context mContext = null; // android涓婁笅鏂??
		private String mDbName = "afinal.db"; // 鏁版嵁搴撳悕瀛?
		private int dbVersion = 1; // 鏁版嵁搴撶増鏈?
		private boolean debug = true; // 鏄惁鏄皟璇曟ā寮忥紙璋冭瘯妯???紡
										// 澧炲垹鏀规煡鐨勬椂鍊欐樉??篠QL璇彞锛??
		private DbUpdateListener dbUpdateListener;
		// private boolean saveOnSDCard = false;//鏄惁淇濆瓨鍒癝D鍗?
		private String targetDirectory;// 鏁版嵁搴撴枃浠跺湪sd鍗???腑鐨勭洰褰??

		public Context getContext() {
			return mContext;
		}

		public void setContext(Context context) {
			this.mContext = context;
		}

		public String getDbName() {
			return mDbName;
		}

		public void setDbName(String dbName) {
			this.mDbName = dbName;
		}

		public int getDbVersion() {
			return dbVersion;
		}

		public void setDbVersion(int dbVersion) {
			this.dbVersion = dbVersion;
		}

		public boolean isDebug() {
			return debug;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
		}

		public DbUpdateListener getDbUpdateListener() {
			return dbUpdateListener;
		}

		public void setDbUpdateListener(DbUpdateListener dbUpdateListener) {
			this.dbUpdateListener = dbUpdateListener;
		}

		// public boolean isSaveOnSDCard() {
		// return saveOnSDCard;
		// }
		//
		// public void setSaveOnSDCard(boolean saveOnSDCard) {
		// this.saveOnSDCard = saveOnSDCard;
		// }

		public String getTargetDirectory() {
			return targetDirectory;
		}

		public void setTargetDirectory(String targetDirectory) {
			this.targetDirectory = targetDirectory;
		}
	}

	/**
	 * 鍦⊿D鍗＄殑鎸囧畾鐩綍涓婂垱寤烘枃浠??
	 *
	 * @param sdcardPath
	 * @param dbfilename
	 * @return
	 */
	private SQLiteDatabase createDbFileOnSDCard(String sdcardPath,
			String dbfilename) {
		File dbf = new File(sdcardPath, dbfilename);
		if (!dbf.exists()) {
			try {
				if (dbf.createNewFile()) {
					return SQLiteDatabase.openOrCreateDatabase(dbf, null);
				}
			} catch (IOException ioex) {
				throw new DbException("鏁版嵁搴撴枃浠跺垱寤哄け璐?", ioex);
			}
		} else {
			return SQLiteDatabase.openOrCreateDatabase(dbf, null);
		}

		return null;
	}

	class SqliteDbHelper extends SQLiteOpenHelper {

		private DbUpdateListener mDbUpdateListener;

		public SqliteDbHelper(Context context, String name, int version,
				DbUpdateListener dbUpdateListener) {
			super(context, name, null, version);
			this.mDbUpdateListener = dbUpdateListener;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (mDbUpdateListener != null) {
				mDbUpdateListener.onUpgrade(db, oldVersion, newVersion);
			} else { // 娓呯┖鎵?鏈夌殑鏁版嵁淇℃伅
				dropDb(db);
			}
		}

	}

	public interface DbUpdateListener {
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}

	/**
	 * 鏍规嵁鏉′欢鏇存柊鏁版嵁
	 * 
	 * @param clazz
	 * @param strWhere
	 */
	public void updateByWhere(Class<?> clazz, String strWhere) {
		checkTableExist(clazz);
		db.execSQL(strWhere);
	}

}
