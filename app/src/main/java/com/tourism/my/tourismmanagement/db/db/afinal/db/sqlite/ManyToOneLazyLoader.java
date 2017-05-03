package com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite;

import com.tourism.my.tourismmanagement.db.db.afinal.FinalDb;

public class ManyToOneLazyLoader<M, O> {
	M manyEntity;
	Class<M> manyClazz;
	Class<O> oneClazz;
	FinalDb db;
	/**
	 * 鐢ㄤ??
	 */
	private Object fieldValue;

	public ManyToOneLazyLoader(M manyEntity, Class<M> manyClazz,
			Class<O> oneClazz, FinalDb db) {
		this.manyEntity = manyEntity;
		this.manyClazz = manyClazz;
		this.oneClazz = oneClazz;
		this.db = db;
	}

	O oneEntity;
	boolean hasLoaded = false;

	/**
	 * 濡傛灉鏁版嵁鏈姞杞斤紝鍒欒皟鐢╨oadManyToOne濉厖鏁版嵁
	 * 
	 * @return
	 */
	public O get() {
		if (oneEntity == null && !hasLoaded) {
			this.db.loadManyToOne(null, this.manyEntity, this.manyClazz,
					this.oneClazz);
			hasLoaded = true;
		}
		return oneEntity;
	}

	public void set(O value) {
		oneEntity = value;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
}
