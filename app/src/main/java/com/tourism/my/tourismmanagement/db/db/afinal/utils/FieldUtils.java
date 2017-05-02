package com.tourism.my.tourismmanagement.db.db.afinal.utils;

import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Id;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.ManyToOne;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.OneToMany;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Property;
import com.tourism.my.tourismmanagement.db.db.afinal.annotation.sqlite.Transient;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @title 瀛楁鎿嶄綔宸ュ叿绫�?
 * @description 鎻忚�?
 * @company 鎺㈢储鑰呯綉缁滃伐浣滃(www.tsz.net)
 * @author michael Young (www.YangFuhai.com)
 * @version 1.0
 * @created 2012-10-10
 */
public class FieldUtils {
	public static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static Method getFieldGetMethod(Class<?> clazz, Field f) {
		String fn = f.getName();
		Method m = null;
		if (f.getType() == boolean.class) {
			m = getBooleanFieldGetMethod(clazz, fn);
		}
		if (m == null) {
			m = getFieldGetMethod(clazz, fn);
		}
		return m;
	}

	public static Method getBooleanFieldGetMethod(Class<?> clazz,
			String fieldName) {
		String mn = "is" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		if (isISStart(fieldName)) {
			mn = fieldName;
		}
		try {
			return clazz.getDeclaredMethod(mn);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Method getBooleanFieldSetMethod(Class<?> clazz, Field f) {
		String fn = f.getName();
		String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
		if (isISStart(f.getName())) {
			mn = "set" + fn.substring(2, 3).toUpperCase() + fn.substring(3);
		}
		try {
			return clazz.getDeclaredMethod(mn, f.getType());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static boolean isISStart(String fieldName) {
		if (fieldName == null || fieldName.trim().length() == 0)
			return false;
		// is寮�澶达紝骞朵笖is涔嬪悗绗竴涓瓧姣嶆槸澶у啓 姣斿�? isAdmin
		return fieldName.startsWith("is")
				&& !Character.isLowerCase(fieldName.charAt(2));
	}

	public static Method getFieldGetMethod(Class<?> clazz, String fieldName) {
		String mn = "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		try {
			return clazz.getDeclaredMethod(mn);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Method getFieldSetMethod(Class<?> clazz, Field f) {
		String fn = f.getName();
		String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
		try {
			return clazz.getDeclaredMethod(mn, f.getType());
		} catch (NoSuchMethodException e) {
			if (f.getType() == boolean.class) {
				return getBooleanFieldSetMethod(clazz, f);
			}
		}
		return null;
	}

	public static Method getFieldSetMethod(Class<?> clazz, String fieldName) {
		try {
			return getFieldSetMethod(clazz, clazz.getDeclaredField(fieldName));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 鑾峰彇鏌愪釜瀛楁鐨勫��?
	 *
	 * @param entity
	 * @return
	 */
	public static Object getFieldValue(Object entity, Field field) {
		Method method = getFieldGetMethod(entity.getClass(), field);
		return invoke(entity, method);
	}

	/**
	 * 鑾峰彇鏌愪釜瀛楁鐨勫��?
	 *
	 * @param entity
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object entity, String fieldName) {
		Method method = getFieldGetMethod(entity.getClass(), fieldName);
		return invoke(entity, method);
	}

	/**
	 * 璁剧疆鏌愪釜瀛楁鐨勫��?
	 *
	 * @param entity
	 * @return
	 */
	public static void setFieldValue(Object entity, Field field, Object value) {
		try {
			Method set = getFieldSetMethod(entity.getClass(), field);
			if (set != null) {
				set.setAccessible(true);
				Class<?> type = field.getType();
				if (type == String.class) {
					set.invoke(entity, value.toString());
				} else if (type == int.class || type == Integer.class) {
					set.invoke(
							entity,
							value == null ? (Integer) null : Integer
									.parseInt(value.toString()));
				} else if (type == float.class || type == Float.class) {
					set.invoke(
							entity,
							value == null ? (Float) null : Float
									.parseFloat(value.toString()));
				} else if (type == long.class || type == Long.class) {
					set.invoke(
							entity,
							value == null ? (Long) null : Long.parseLong(value
									.toString()));
				} else if (type == Date.class) {
					set.invoke(entity, value == null ? (Date) null
							: stringToDateTime(value.toString()));
				} else {
					set.invoke(entity, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鑾峰彇鏌愪釜瀛楁鐨勫��?
	 *
	 * @return
	 */
	public static Field getFieldByColumnName(Class<?> clazz, String columnName) {
		Field field = null;
		if (columnName != null) {
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null && fields.length > 0) {
				if (columnName.equals(ClassUtils.getPrimaryKeyColumn(clazz)))
					field = ClassUtils.getPrimaryKeyField(clazz);

				if (field == null) {
					for (Field f : fields) {
						Property property = f.getAnnotation(Property.class);
						if (property != null
								&& columnName.equals(property.column())) {
							field = f;
							break;
						}

						ManyToOne manyToOne = f.getAnnotation(ManyToOne.class);
						if (manyToOne != null
								&& manyToOne.column().trim().length() != 0) {
							field = f;
							break;
						}
					}
				}

				if (field == null) {
					field = getFieldByName(clazz, columnName);
				}
			}
		}
		return field;
	}

	/**
	 * 鑾峰彇鏌愪釜瀛楁鐨勫��?
	 *
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldByName(Class<?> clazz, String fieldName) {
		Field field = null;
		if (fieldName != null) {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return field;
	}

	/**
	 * 鑾峰彇鏌愪釜灞炴�у搴旂�? 琛ㄧ殑鍒�?
	 *
	 * @return
	 */
	public static String getColumnByField(Field field) {
		Property property = field.getAnnotation(Property.class);
		if (property != null && property.column().trim().length() != 0) {
			return property.column();
		}

		ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
		if (manyToOne != null && manyToOne.column().trim().length() != 0) {
			return manyToOne.column();
		}

		OneToMany oneToMany = field.getAnnotation(OneToMany.class);
		if (oneToMany != null && oneToMany.manyColumn() != null
				&& oneToMany.manyColumn().trim().length() != 0) {
			return oneToMany.manyColumn();
		}

		Id id = field.getAnnotation(Id.class);
		if (id != null && id.column().trim().length() != 0)
			return id.column();

		return field.getName();
	}

	public static String getPropertyDefaultValue(Field field) {
		Property property = field.getAnnotation(Property.class);
		if (property != null && property.defaultValue().trim().length() != 0) {
			return property.defaultValue();
		}
		return null;
	}

	/**
	 * 妫�娴� 瀛楁鏄惁宸茬粡琚爣娉ㄤ�? 闈炴暟鎹簱瀛楁�?
	 *
	 * @param f
	 * @return
	 */
	public static boolean isTransient(Field f) {
		return f.getAnnotation(Transient.class) != null;
	}

	/**
	 * 鑾峰彇鏌愪釜瀹炰綋鎵ц鏌愪釜鏂规硶鐨勭粨鏋�
	 * 
	 * @param obj
	 * @param method
	 * @return
	 */
	private static Object invoke(Object obj, Method method) {
		if (obj == null || method == null)
			return null;
		try {
			return method.invoke(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isManyToOne(Field field) {
		return field.getAnnotation(ManyToOne.class) != null;
	}

	public static boolean isOneToMany(Field field) {
		return field.getAnnotation(OneToMany.class) != null;
	}

	public static boolean isManyToOneOrOneToMany(Field field) {
		return isManyToOne(field) || isOneToMany(field);
	}

	public static boolean isBaseDateType(Field field) {
		Class<?> clazz = field.getType();
		return clazz.equals(String.class) || clazz.equals(Integer.class)
				|| clazz.equals(Byte.class) || clazz.equals(Long.class)
				|| clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class)
				|| clazz.equals(Boolean.class) || clazz.equals(Date.class)
				|| clazz.equals(Date.class)
				|| clazz.equals(java.sql.Date.class) || clazz.isPrimitive();
	}

	public static Date stringToDateTime(String strDate) {
		if (strDate != null) {
			try {
				return SDF.parse(strDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
