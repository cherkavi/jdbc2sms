package com.cherkashyn.vitalii.smscenter.exception;

public class GateWayException extends GeneralException{

	private static final long serialVersionUID = 1L;

	public GateWayException() {
		super();
	}

	public GateWayException(String message, Throwable cause) {
		super(message, cause);
	}

	public GateWayException(String message) {
		super(message);
	}

	public GateWayException(Throwable cause) {
		super(cause);
	}
	
}
