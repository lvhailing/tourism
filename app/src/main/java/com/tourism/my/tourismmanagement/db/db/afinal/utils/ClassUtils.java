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
	 * 閺嶈宓佺?圭偘缍嬬猾锟? 閼惧嘲绶? 鐎圭偘缍嬬猾璇差嚠鎼存梻娈戠悰銊ユ倳
	 *
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if (table == null || table.name().trim().length() == 0) {
			// 瑜版挻鐥呴張澶嬫暈鐟欙絿娈戦弮璺猴拷娆撶帛鐠併倗鏁ょ猾鑽ゆ畱閸氬秶袨娴ｆ粈璐熺悰銊ユ倳,楠炶埖濡搁悙鐧哥礄.閿涘娴涢幑顫礋娑撳鍨濈痪锟?(_)
			return clazz.getName().replace('.', '_');
		}
		return table.name();
	}

	public static Object getPrimaryKeyValue(Object entity) {
		return FieldUtils.getFieldValue(entity,
				ClassUtils.getPrimaryKeyField(entity.getClass()));
	}

	/**
	 * 閺嶈宓佺?圭偘缍嬬猾锟? 閼惧嘲绶? 鐎圭偘缍嬬猾璇差嚠鎼存梻娈戠悰銊ユ倳
	 *
	 * @return
	 */
	public static String getPrimaryKeyColumn(Class<?> clazz) {
		String primaryKey = null;
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			Id idAnnotation = null;
			Field idField = null;

			for (Field field : fields) { // 閼惧嘲褰嘔D濞夈劏袙
				idAnnotation = field.getAnnotation(Id.class);
				if (idAnnotation != null) {
					idField = field;
					break;
				}
			}

			if (idAnnotation != null) { // 閺堝D濞夈劏袙
				primaryKey = idAnnotation.column();
				if (primaryKey == null || primaryKey.trim().length() == 0)
					primaryKey = idField.getName();
			} else { // 濞屸剝婀両D濞夈劏袙,姒涙顓婚崢缁樺 _id 閸滐拷 id 娑撹桨瀵岄柨顕嗙礉娴兼ê鍘涚?电粯澹? _id
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
	 * 閺嶈宓佺?圭偘缍嬬猾锟? 閼惧嘲绶? 鐎圭偘缍嬬猾璇差嚠鎼存梻娈戠悰銊ユ倳
	 *
	 * @return
	 */
	public static Field getPrimaryKeyField(Class<?> clazz) {
		Field primaryKeyField = null;
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {

			for (Field field : fields) { // 閼惧嘲褰嘔D濞夈劏袙
				if (field.getAnnotation(Id.class) != null) {
					primaryKeyField = field;
					break;
				}
			}

			if (primaryKeyField == null) { // 濞屸剝婀両D濞夈劏袙
				for (Field field : fields) {
					if ("_id".equals(field.getName())) {
						primaryKeyField = field;
						break;
					}
				}
			}

			if (primaryKeyField == null) { // 婵″倹鐏夊▽鈩冩箒_id閻ㄥ嫬鐡у▓锟?
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
	 * 閺嶈宓佺?圭偘缍嬬猾锟? 閼惧嘲绶? 鐎圭偘缍嬬猾璇差嚠鎼存梻娈戠悰銊ユ倳
	 *
	 * @return
	 */
	public static String getPrimaryKeyFieldName(Class<?> clazz) {
		Field f = getPrimaryKeyField(clazz);
		return f == null ? null : f.getName();
	}

	/**
	 * 鐏忓棗顕挒陇娴嗛幑顫礋ContentValues
	 *
	 * @return
	 */
	public static List<Property> getPropertyList(Class<?> clazz) {

		List<Property> plist = new ArrayList<Property>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
			for (Field f : fs) {
				// 韫囧懘銆忛弰顖氱唨閺堫剚鏆熼幑顔捐閸ㄥ鎷板▽鈩冩箒閺嶅洨鐏涢弮鑸碉拷浣烘畱鐎涙顔?
				if (!FieldUtils.isTransient(f)) {
					if (FieldUtils.isBaseDateType(f)) {

						if (f.getName().equals(primaryKeyFieldName)) // 鏉╁洦鎶ゆ稉濠氭暛
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
	 * 鐏忓棗顕挒陇娴嗛幑顫礋ContentValues
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
					// 婵″倹鐏夌猾璇茬?锋稉绡梐nyToOneLazyLoader閸掓瑥褰囩粭顑跨癌娑擃亜寮弫棰佺稊娑撶皟anyClass閿涘牅绔撮弬鐟扮杽娴ｆ搫绱?
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
	 * 鐏忓棗顕挒陇娴嗛幑顫礋ContentValues
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
						// 婵″倹鐏夌猾璇茬?烽崣鍌涙殶娑擄拷2閸掓瑨顓绘稉鐑樻ЦLazyLoader 2013-7-25
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
					/* 娣囶喗顒滅猾璇茬?风挧瀣拷濂告晩鐠囶垳娈慴ug閿涘畺.getClass鏉╂柨娲栭惃鍕ЦFiled */
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
