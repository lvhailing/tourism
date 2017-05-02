package com.tourism.my.tourismmanagement.db.db.afinal.db.table;

import com.tourism.my.tourismmanagement.db.db.afinal.utils.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @title 灞炴��
 * @description 銆愰潪涓婚敭銆戠殑銆愬熀鏈暟鎹被鍨嬨�� 閮芥槸灞炴��?
 * @author michael Young (www.YangFuhai.com)
 * @version 1.0
 * @created 2012-10-10
 */
public class Property {

	private String fieldName;
	private String column;
	private String defaultValue;
	private Class<?> dataType;
	private Field field;

	private Method get;
	private Method set;

	public void setValue(Object receiver, Object value) {
		if (set != null && value != null) {
			try {
				if (dataType == String.class) {
					set.invoke(receiver, value.toString());
				} else if (dataType == int.class || dataType == Integer.class) {
					set.invoke(receiver, value == null ? (Integer) null
							: Integer.parseInt(value.toString()));
				} else if (dataType == float.class || dataType == Float.class) {
					set.invoke(
							receiver,
							value == null ? (Float) null : Float
									.parseFloat(value.toString()));
				} else if (dataType == double.class || dataType == Double.class) {
					set.invoke(
							receiver,
							value == null ? (Double) null : Double
									.parseDouble(value.toString()));
				} else if (dataType == long.class || dataType == Long.class) {
					set.invoke(
							receiver,
							value == null ? (Long) null : Long.parseLong(value
									.toString()));
				} else if (dataType == Date.class
						|| dataType == java.sql.Date.class) {
					set.invoke(receiver, value == null ? (Date) null
							: FieldUtils.stringToDateTime(value.toString()));
				} else if (dataType == boolean.class
						|| dataType == Boolean.class) {
					set.invoke(receiver, value == null ? (Boolean) null
							: "true".equals(value.toString()));
				} else {
					set.invoke(receiver, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (field != null) {
			try {
				field.setAccessible(true);
				field.set(receiver, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 鑾峰彇鏌愪釜瀹炰綋鎵ц鏌愪釜鏂规硶鐨勭粨鏋�
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(Object obj) {
		if (obj != null && get != null) {
			try {
				return (T) get.invoke(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Class<?> getDataType() {
		return dataType;
	}

	public void setDataType(Class<?> dataType) {
		this.dataType = dataType;
	}

	public Method getGet() {
		return get;
	}

	public void setGet(Method get) {
		this.get = get;
	}

	public Method getSet() {
		return set;
	}

	public void setSet(Method set) {
		this.set = set;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

}
