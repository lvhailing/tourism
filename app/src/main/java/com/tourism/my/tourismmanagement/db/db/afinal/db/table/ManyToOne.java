package com.tourism.my.tourismmanagement.db.db.afinal.db.table;

public class ManyToOne extends Property {

	private Class<?> manyClass;

	public Class<?> getManyClass() {
		return manyClass;
	}

	public void setManyClass(Class<?> manyClass) {
		this.manyClass = manyClass;
	}

}
