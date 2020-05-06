package com.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.assignment.exception.IncorrectEmployeeWithIdException;
import com.assignment.exception.IncorrectEmployeeWithNameException;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{
	
	private Logger LOGGER=LoggerFactory.getLogger(this.getClass());
	@ExceptionHandler(IncorrectEmployeeWithNameException.class)
	public final ResponseEntity<ErrorMessage> handleEmployeeNameNotFound(IncorrectEmployeeWithNameException ex){
		LOGGER.error(ex.toString());
		ErrorMessage errorMessage=new ErrorMessage(ex.getMessage(),"No object with the Ename parameter exists");
		return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(IncorrectEmployeeWithIdException.class)
	public final ResponseEntity<ErrorMessage> handleEmployeeIdNotFound(IncorrectEmployeeWithIdException ex){
		LOGGER.error(ex.toString());
		ErrorMessage errorMessage=new ErrorMessage(ex.getMessage(),"No object with the Employee id exists");
		return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
	}
}
