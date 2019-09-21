package com.cherkashyn.vitalii.smscenter.exception;

public class TransportException extends GeneralException{

	private static final long serialVersionUID = 1L;

	public TransportException() {
		super();
	}

	public TransportException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransportException(String message) {
		super(message);
	}

	public TransportException(Throwable cause) {
		super(cause);
	}
	

}
