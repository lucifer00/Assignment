package com.assignment.exception;

public class IncorrectEmployeeWithIdException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public IncorrectEmployeeWithIdException(String message) {
		super(message);
	}
}
