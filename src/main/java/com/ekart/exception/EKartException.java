package com.ekart.exception;

import java.io.Serial;

public class EKartException extends Exception{

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	public EKartException(String message) {
		super(message);
	}

}
