package com.tourism.my.tourismmanagement.db.db.exception;

/**
 * 
 * @author kaka
 * 
 */
public class BaseException extends Exception {
	private static final long serialVersionUID = 1L;

	public BaseException() {
	}

	public BaseException(String detailMessage) {
		super(detailMessage);
	}

	public BaseException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public BaseException(Throwable throwable) {
		super(throwable);
	}
}
