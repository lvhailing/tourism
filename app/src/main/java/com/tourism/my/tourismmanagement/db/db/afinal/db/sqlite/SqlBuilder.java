package com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite;

import android.text.TextUtils;

import com.tourism.my.tourismmanagement.db.db.afinal.db.table.Id;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.KeyValue;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.ManyToOne;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.Property;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.TableInfo;
import com.tourism.my.tourismmanagement.db.db.afinal.exception.DbException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlBuilder {

	/**
	 * 閼惧嘲褰囬幓鎺戝弳閻ㄥ墕ql鐠囶厼褰?
	 *
	 * @return
	 */
	public static SqlInfo buildInsertSql(Object entity) {

		List<KeyValue> keyValueList = getSaveKeyValueListByEntity(entity);

		StringBuffer strSQL = new StringBuffer();
		SqlInfo sqlInfo = null;
		if (keyValueList != null && keyValueList.size() > 0) {

			sqlInfo = new SqlInfo();

			strSQL.append("INSERT INTO ");
			strSQL.append(TableInfo.get(entity.getClass()).getTableName());
			strSQL.append(" (");
			for (KeyValue kv : keyValueList) {
				strSQL.append(kv.getKey()).append(",");
				sqlInfo.addValue(kv.getValue());
			}
			strSQL.deleteCharAt(strSQL.length() - 1);
			strSQL.append(") VALUES ( ");

			int length = keyValueList.size();
			for (int i = 0; i < length; i++) {
				strSQL.append("?,");
			}
			strSQL.deleteCharAt(strSQL.length() - 1);
			strSQL.append(")");

			sqlInfo.setSql(strSQL.toString());
		}

		return sqlInfo;
	}

	public static List<KeyValue> getSaveKeyValueListByEntity(Object entity) {

		List<KeyValue> keyValueList = new ArrayList<KeyValue>();

		TableInfo table = TableInfo.get(entity.getClass());
		Object idvalue = table.getId().getValue(entity);

		if (!(idvalue instanceof Integer)) { // 閻€劋绨￠棃鐐跺殰婢х偤鏆?,濞ｈ濮瀒d ,
												// 闁插洨鏁ら懛顏勵杻闂?鍨皑娑撳秹娓剁憰浣瑰潑閸旂垊d娴滐拷
			if (idvalue instanceof String && idvalue != null) {
				KeyValue kv = new KeyValue(table.getId().getColumn(), idvalue);
				keyValueList.add(kv);
			}
		}

		// 濞ｈ濮炵仦鐐达拷锟?
		Collection<Property> propertys = table.propertyMap.values();
		for (Property property : propertys) {
			KeyValue kv = property2KeyValue(property, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		// 濞ｈ濮炴径鏍暛閿涘牆顦跨?甸?涚閿涳拷
		Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
		for (ManyToOne many : manyToOnes) {
			KeyValue kv = manyToOne2KeyValue(many, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		return keyValueList;
	}

	private static String getDeleteSqlBytableName(String tableName) {
		return "DELETE FROM " + tableName;
	}

	public static SqlInfo buildDeleteSql(Object entity) {
		TableInfo table = TableInfo.get(entity.getClass());

		Id id = table.getId();
		Object idvalue = id.getValue(entity);

		if (idvalue == null) {
			throw new DbException("getDeleteSQL:" + entity.getClass()
					+ " id value is null");
		}
		StringBuffer strSQL = new StringBuffer(
				getDeleteSqlBytableName(table.getTableName()));
		strSQL.append(" WHERE ").append(id.getColumn()).append("=?");

		SqlInfo sqlInfo = new SqlInfo();
		sqlInfo.setSql(strSQL.toString());
		sqlInfo.addValue(idvalue);

		return sqlInfo;
	}

	public static SqlInfo buildDeleteSql(Class<?> clazz, Object idValue) {
		TableInfo table = TableInfo.get(clazz);
		Id id = table.getId();

		if (null == idValue) {
			throw new DbException("getDeleteSQL:idValue is null");
		}

		StringBuffer strSQL = new StringBuffer(
				getDeleteSqlBytableName(table.getTableName()));
		strSQL.append(" WHERE ").append(id.getColumn()).append("=?");

		SqlInfo sqlInfo = new SqlInfo();
		sqlInfo.setSql(strSQL.toString());
		sqlInfo.addValue(idValue);

		return sqlInfo;
	}

	/**
	 * 閺嶈宓侀弶鈥叉閸掔娀娅庨弫鐗堝祦 閿涘本娼禒鏈佃礋缁岃櫣娈戦弮璺猴拷娆忕殺娴兼艾鍨归梽銈嗗閺堝娈戦弫鐗堝祦
	 *
	 * @param clazz
	 * @param strWhere
	 * @return
	 */
	public static String buildDeleteSql(Class<?> clazz, String strWhere) {
		TableInfo table = TableInfo.get(clazz);
		StringBuffer strSQL = new StringBuffer(
				getDeleteSqlBytableName(table.getTableName()));

		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ");
			strSQL.append(strWhere);
		}

		return strSQL.toString();
	}

	// //////////////////////////select sql
	// start///////////////////////////////////////

	private static String getSelectSqlByTableName(String tableName) {
		return new StringBuffer("SELECT * FROM ").append(tableName).toString();
	}

	public static String getSelectSQL(Class<?> clazz, Object idValue) {
		TableInfo table = TableInfo.get(clazz);

		StringBuffer strSQL = new StringBuffer(
				getSelectSqlByTableName(table.getTableName()));
		strSQL.append(" WHERE ");
		strSQL.append(getPropertyStrSql(table.getId().getColumn(), idValue));

		return strSQL.toString();
	}

	public static SqlInfo getSelectSqlAsSqlInfo(Class<?> clazz, Object idValue) {
		TableInfo table = TableInfo.get(clazz);

		StringBuffer strSQL = new StringBuffer(
				getSelectSqlByTableName(table.getTableName()));
		strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");

		SqlInfo sqlInfo = new SqlInfo();
		sqlInfo.setSql(strSQL.toString());
		sqlInfo.addValue(idValue);

		return sqlInfo;
	}

	public static String getSelectSQL(Class<?> clazz) {
		return getSelectSqlByTableName(TableInfo.get(clazz).getTableName());
	}

	public static String getSelectSQLByWhere(Class<?> clazz, String strWhere) {
		TableInfo table = TableInfo.get(clazz);

		StringBuffer strSQL = new StringBuffer(
				getSelectSqlByTableName(table.getTableName()));

		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ").append(strWhere);
		}

		return strSQL.toString();
	}

	// ////////////////////////////update sql
	// start/////////////////////////////////////////////

	public static SqlInfo getUpdateSqlAsSqlInfo(Object entity) {

		TableInfo table = TableInfo.get(entity.getClass());
		Object idvalue = table.getId().getValue(entity);

		if (null == idvalue) {// 娑撳鏁崐闂寸瑝閼虫垝璐焠ull閿涘苯鎯侀崚娆庣瑝閼宠姤娲块弬锟?
			throw new DbException("this entity[" + entity.getClass()
					+ "]'s id value is null");
		}

		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		// 濞ｈ濮炵仦鐐达拷锟?
		Collection<Property> propertys = table.propertyMap.values();
		for (Property property : propertys) {
			KeyValue kv = property2KeyValue(property, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		// 濞ｈ濮炴径鏍暛閿涘牆顦跨?甸?涚閿涳拷
		Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
		for (ManyToOne many : manyToOnes) {
			KeyValue kv = manyToOne2KeyValue(many, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		if (keyValueList == null || keyValueList.size() == 0)
			return null;

		SqlInfo sqlInfo = new SqlInfo();
		StringBuffer strSQL = new StringBuffer("UPDATE ");
		strSQL.append(table.getTableName());
		strSQL.append(" SET ");
		for (KeyValue kv : keyValueList) {
			strSQL.append(kv.getKey()).append("=?,");
			sqlInfo.addValue(kv.getValue());
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
		sqlInfo.addValue(idvalue);
		sqlInfo.setSql(strSQL.toString());
		return sqlInfo;
	}

	public static SqlInfo getUpdateSqlAsSqlInfo(Object entity, String strWhere) {

		TableInfo table = TableInfo.get(entity.getClass());

		List<KeyValue> keyValueList = new ArrayList<KeyValue>();

		// 濞ｈ濮炵仦鐐达拷锟?
		Collection<Property> propertys = table.propertyMap.values();
		for (Property property : propertys) {
			KeyValue kv = property2KeyValue(property, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		// 濞ｈ濮炴径鏍暛閿涘牆顦跨?甸?涚閿涳拷
		Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
		for (ManyToOne many : manyToOnes) {
			KeyValue kv = manyToOne2KeyValue(many, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		if (keyValueList == null || keyValueList.size() == 0) {
			throw new DbException("this entity[" + entity.getClass()
					+ "] has no property");
		}

		SqlInfo sqlInfo = new SqlInfo();
		StringBuffer strSQL = new StringBuffer("UPDATE ");
		strSQL.append(table.getTableName());
		strSQL.append(" SET ");
		for (KeyValue kv : keyValueList) {
			strSQL.append(kv.getKey()).append("=?,");
			sqlInfo.addValue(kv.getValue());
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ").append(strWhere);
		}
		sqlInfo.setSql(strSQL.toString());
		return sqlInfo;
	}

	public static String getCreatTableSQL(Class<?> clazz) {
		TableInfo table = TableInfo.get(clazz);

		Id id = table.getId();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("CREATE TABLE IF NOT EXISTS ");
		strSQL.append(table.getTableName());
		strSQL.append(" ( ");

		Class<?> primaryClazz = id.getDataType();
		if (primaryClazz == int.class || primaryClazz == Integer.class
				|| primaryClazz == long.class || primaryClazz == Long.class) {
			strSQL.append(id.getColumn()).append(
					" INTEGER PRIMARY KEY AUTOINCREMENT,");
		} else {
			strSQL.append(id.getColumn()).append(" TEXT PRIMARY KEY,");
		}

		Collection<Property> propertys = table.propertyMap.values();
		for (Property property : propertys) {
			strSQL.append(property.getColumn());
			Class<?> dataType = property.getDataType();
			if (dataType == int.class || dataType == Integer.class
					|| dataType == long.class || dataType == Long.class) {
				strSQL.append(" INTEGER");
			} else if (dataType == float.class || dataType == Float.class
					|| dataType == double.class || dataType == Double.class) {
				strSQL.append(" REAL");
			} else if (dataType == boolean.class || dataType == Boolean.class) {
				strSQL.append(" NUMERIC");
			}
			strSQL.append(",");
		}

		Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
		for (ManyToOne manyToOne : manyToOnes) {
			strSQL.append(manyToOne.getColumn()).append(" INTEGER").append(",");
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		strSQL.append(" )");
		return strSQL.toString();
	}

	/**
	 * @param key
	 * @param value
	 * @return eg1: name='afinal' eg2: id=100
	 */
	private static String getPropertyStrSql(String key, Object value) {
		StringBuffer sbSQL = new StringBuffer(key).append("=");
		if (value instanceof String || value instanceof java.util.Date
				|| value instanceof java.sql.Date) {
			sbSQL.append("'").append(value).append("'");
		} else {
			sbSQL.append(value);
		}
		return sbSQL.toString();
	}

	private static KeyValue property2KeyValue(Property property, Object entity) {
		KeyValue kv = null;
		String pcolumn = property.getColumn();
		Object value = property.getValue(entity);
		if (value != null) {
			kv = new KeyValue(pcolumn, value);
		} else {
			if (property.getDefaultValue() != null
					&& property.getDefaultValue().trim().length() != 0)
				kv = new KeyValue(pcolumn, property.getDefaultValue());
		}
		return kv;
	}

	private static KeyValue manyToOne2KeyValue(ManyToOne many, Object entity) {
		KeyValue kv = null;
		String manycolumn = many.getColumn();
		Object manyobject = many.getValue(entity);
		if (manyobject != null) {
			Object manyvalue;
			if (manyobject.getClass() == ManyToOneLazyLoader.class) {
				manyvalue = TableInfo.get(many.getManyClass()).getId()
						.getValue(((ManyToOneLazyLoader) manyobject).get());
			} else {
				manyvalue = TableInfo.get(manyobject.getClass()).getId()
						.getValue(manyobject);
			}
			if (manycolumn != null && manyvalue != null) {
				kv = new KeyValue(manycolumn, manyvalue);
			}
		}

		return kv;
	}

}
