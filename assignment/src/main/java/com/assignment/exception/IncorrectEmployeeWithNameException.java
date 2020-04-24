package com.assignment.exception;

public class IncorrectEmployeeWithNameException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public IncorrectEmployeeWithNameException(String message) {
		super(message);
	}
}
