package com.tourism.my.tourismmanagement.db.db.afinal.exception;

public class AfinalException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AfinalException() {
		super();
	}

	public AfinalException(String msg) {
		super(msg);
	}

	public AfinalException(Throwable ex) {
		super(ex);
	}

	public AfinalException(String msg, Throwable ex) {
		super(msg, ex);
	}

}
