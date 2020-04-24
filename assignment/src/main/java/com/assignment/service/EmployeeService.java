package com.assignment.service;

import java.util.List;
import java.util.Set;

import com.assignment.dao.AddressDao;
import com.assignment.dao.EmployeeDao;
import com.assignment.exception.IncorrectEmployeeWithIdException;
import com.assignment.exception.IncorrectEmployeeWithNameException;

public interface EmployeeService {
	public EmployeeDao addNewEmployee(EmployeeDao employeeDao);
	public EmployeeDao getEmployeeById(int id)throws IncorrectEmployeeWithIdException;
	public EmployeeDao addNewAddress(int id,AddressDao address);
	public void addBulkEmployeeData(List<EmployeeDao> employees);
	public List<EmployeeDao> getAllEmployees();
	public void autoUpdateCache();
	public EmployeeDao getByName(String ename)throws IncorrectEmployeeWithNameException;
	public Set<EmployeeDao> getByPincode(long pincode);
	public EmployeeDao deleteAddressForEmployee(int empId,int addressId);
	public void deleteEmployeeById(int empId)throws IncorrectEmployeeWithIdException;
}
