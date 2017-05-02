package com.tourism.my.tourismmanagement.db.db.afinal.utils;

import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Id;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Table;
import com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite.ManyToOneLazyLoader;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.ManyToOne;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.OneToMany;
import com.tourism.my.tourismmanagement.db.db.afinal.db.table.Property;
import com.tourism.my.tourismmanagement.db.db.afinal.exception.DbException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

	/**
	 * 鏍规嵁�?�炰綋绫�? 鑾峰�? 瀹炰綋绫诲搴旂殑琛ㄥ悕
	 *
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if (table == null || table.name().trim().length() == 0) {
			// 褰撴病鏈夋敞瑙ｇ殑鏃跺�欓粯璁ょ敤绫荤殑鍚嶇О浣滀负琛ㄥ悕,骞舵妸鐐癸紙.锛夋浛鎹负涓嬪垝绾�?(_)
			return clazz.getName().replace('.', '_');
		}
		return table.name();
	}

	public static Object getPrimaryKeyValue(Object entity) {
		return FieldUtils.getFieldValue(entity,
				ClassUtils.getPrimaryKeyField(entity.getClass()));
	}

	/**
	 * 鏍规嵁�?�炰綋绫�? 鑾峰�? 瀹炰綋绫诲搴旂殑琛ㄥ悕
	 *
	 * @return
	 */
	public static String getPrimaryKeyColumn(Class<?> clazz) {
		String primaryKey = null;
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			Id idAnnotation = null;
			Field idField = null;

			for (Field field : fields) { // 鑾峰彇ID娉ㄨВ
				idAnnotation = field.getAnnotation(Id.class);
				if (idAnnotation != null) {
					idField = field;
					break;
				}
			}

			if (idAnnotation != null) { // 鏈塈D娉ㄨВ
				primaryKey = idAnnotation.column();
				if (primaryKey == null || primaryKey.trim().length() == 0)
					primaryKey = idField.getName();
			} else { // 娌℃湁ID娉ㄨВ,榛樿鍘绘壘 _id 鍜� id 涓轰富閿紝浼樺厛�?�绘�? _id
				for (Field field : fields) {
					if ("_id".equals(field.getName()))
						return "_id";
				}

				for (Field field : fields) {
					if ("id".equals(field.getName()))
						return "id";
				}
			}
		} else {
			throw new RuntimeException("this model[" + clazz + "] has no field");
		}
		return primaryKey;
	}

	/**
	 * 鏍规嵁�?�炰綋绫�? 鑾峰�? 瀹炰綋绫诲搴旂殑琛ㄥ悕
	 *
	 * @return
	 */
	public static Field getPrimaryKeyField(Class<?> clazz) {
		Field primaryKeyField = null;
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {

			for (Field field : fields) { // 鑾峰彇ID娉ㄨВ
				if (field.getAnnotation(Id.class) != null) {
					primaryKeyField = field;
					break;
				}
			}

			if (primaryKeyField == null) { // 娌℃湁ID娉ㄨВ
				for (Field field : fields) {
					if ("_id".equals(field.getName())) {
						primaryKeyField = field;
						break;
					}
				}
			}

			if (primaryKeyField == null) { // 濡傛灉娌℃湁_id鐨勫瓧娈�?
				for (Field field : fields) {
					if ("id".equals(field.getName())) {
						primaryKeyField = field;
						break;
					}
				}
			}

		} else {
			throw new RuntimeException("this model[" + clazz + "] has no field");
		}
		return primaryKeyField;
	}

	/**
	 * 鏍规嵁�?�炰綋绫�? 鑾峰�? 瀹炰綋绫诲搴旂殑琛ㄥ悕
	 *
	 * @return
	 */
	public static String getPrimaryKeyFieldName(Class<?> clazz) {
		Field f = getPrimaryKeyField(clazz);
		return f == null ? null : f.getName();
	}

	/**
	 * 灏嗗璞¤浆鎹负ContentValues
	 *
	 * @return
	 */
	public static List<Property> getPropertyList(Class<?> clazz) {

		List<Property> plist = new ArrayList<Property>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
			for (Field f : fs) {
				// 蹇呴』鏄熀鏈暟鎹被鍨嬪拰娌℃湁鏍囩灛鏃舵�佺殑瀛楁�?
				if (!FieldUtils.isTransient(f)) {
					if (FieldUtils.isBaseDateType(f)) {

						if (f.getName().equals(primaryKeyFieldName)) // 杩囨护涓婚敭
							continue;

						Property property = new Property();

						property.setColumn(FieldUtils.getColumnByField(f));
						property.setFieldName(f.getName());
						property.setDataType(f.getType());
						property.setDefaultValue(FieldUtils
								.getPropertyDefaultValue(f));
						property.setSet(FieldUtils.getFieldSetMethod(clazz, f));
						property.setGet(FieldUtils.getFieldGetMethod(clazz, f));
						property.setField(f);

						plist.add(property);
					}
				}
			}
			return plist;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 灏嗗璞¤浆鎹负ContentValues
	 *
	 * @return
	 */
	public static List<ManyToOne> getManyToOneList(Class<?> clazz) {

		List<ManyToOne> mList = new ArrayList<ManyToOne>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				if (!FieldUtils.isTransient(f) && FieldUtils.isManyToOne(f)) {

					ManyToOne mto = new ManyToOne();
					// 濡傛灉绫诲�?�涓篗anyToOneLazyLoader鍒欏彇绗簩涓弬鏁颁綔涓簃anyClass锛堜竴鏂瑰疄浣擄�?
					// 2013-7-26
					if (f.getType() == ManyToOneLazyLoader.class) {
						Class<?> pClazz = (Class<?>) ((ParameterizedType) f
								.getGenericType()).getActualTypeArguments()[1];
						if (pClazz != null)
							mto.setManyClass(pClazz);
					} else {
						mto.setManyClass(f.getType());
					}
					mto.setColumn(FieldUtils.getColumnByField(f));
					mto.setFieldName(f.getName());
					mto.setDataType(f.getType());
					mto.setSet(FieldUtils.getFieldSetMethod(clazz, f));
					mto.setGet(FieldUtils.getFieldGetMethod(clazz, f));

					mList.add(mto);
				}
			}
			return mList;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 灏嗗璞¤浆鎹负ContentValues
	 *
	 * @return
	 */
	public static List<OneToMany> getOneToManyList(Class<?> clazz) {

		List<OneToMany> oList = new ArrayList<OneToMany>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				if (!FieldUtils.isTransient(f) && FieldUtils.isOneToMany(f)) {

					OneToMany otm = new OneToMany();

					otm.setColumn(FieldUtils.getColumnByField(f));
					otm.setFieldName(f.getName());

					Type type = f.getGenericType();

					if (type instanceof ParameterizedType) {
						ParameterizedType pType = (ParameterizedType) f
								.getGenericType();
						// 濡傛灉绫诲�?�鍙傛暟涓�2鍒欒涓烘槸LazyLoader 2013-7-25
						if (pType.getActualTypeArguments().length == 1) {
							Class<?> pClazz = (Class<?>) pType
									.getActualTypeArguments()[0];
							if (pClazz != null)
								otm.setOneClass(pClazz);
						} else {
							Class<?> pClazz = (Class<?>) pType
									.getActualTypeArguments()[1];
							if (pClazz != null)
								otm.setOneClass(pClazz);
						}
					} else {
						throw new DbException("getOneToManyList Exception:"
								+ f.getName() + "'s type is null");
					}
					/* 淇绫诲�?�璧嬪�奸敊璇殑bug锛宖.getClass杩斿洖鐨勬槸Filed */
					otm.setDataType(f.getType());
					otm.setSet(FieldUtils.getFieldSetMethod(clazz, f));
					otm.setGet(FieldUtils.getFieldGetMethod(clazz, f));

					oList.add(otm);
				}
			}
			return oList;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
