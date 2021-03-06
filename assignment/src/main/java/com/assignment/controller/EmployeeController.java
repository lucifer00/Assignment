package com.assignment.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dao.AddressDao;
import com.assignment.dao.EmployeeDao;
import com.assignment.exception.IncorrectEmployeeWithIdException;
import com.assignment.exception.IncorrectEmployeeWithNameException;
import com.assignment.service.EmployeeService;

@RestController
public class EmployeeController {
	@Autowired
	private Logger logger;
	@Autowired
	private EmployeeService employeeService;
	@PostMapping("/employee/addBulk")
	public String addBulkData(@RequestBody List<EmployeeDao> employees) {
		logger.debug("Request {}",employees);
		employeeService.addBulkEmployeeData(employees);
		logger.debug("Response : Details added successfully");
		return "Details added successfully";
	}
	@GetMapping("/employee/getAll")
	public List<EmployeeDao> getAllEmployeeData(){
		logger.debug("Request:Get all employees");
		return employeeService.getAllEmployees();
	}
	@PostMapping("/employee/addNew")
	public EmployeeDao addNewEmployee(@RequestBody EmployeeDao employee) {
		logger.debug("Request : Add new emploee {}",employee);
		return employeeService.addNewEmployee(employee);
	}
	@GetMapping("/employee/getById/{empId}")
	public EmployeeDao getEmployeeById(@PathVariable int empId)throws IncorrectEmployeeWithIdException {
		logger.debug("Request:Get employee by Id {}",empId);
		return employeeService.getEmployeeById(empId);
	}
	@GetMapping("/employee/getByName/{empName}")
	public EmployeeDao getEmployeeByEmpName(@PathVariable String empName)throws IncorrectEmployeeWithNameException {
		logger.debug("Request:Get employee by name {}",empName);
		return employeeService.getByName(empName);
	}
	@GetMapping("/employee/getByPincode/{pincode}")
	public Set<EmployeeDao> getByPincode(@PathVariable Long pincode){
		logger.debug("Request:Get employees by pincode {}",pincode);
		return employeeService.getByPincode(pincode);
	}
	@PutMapping("/employee/addNewAddress/{empId}")
	public EmployeeDao addNewAddress(@PathVariable int empId,@RequestBody AddressDao address) {
		logger.debug("Request:Add new address for employee {} {}",empId,address);
		return employeeService.addNewAddress(empId, address);
	}
	@DeleteMapping("/employee/deleteAddress")
	public EmployeeDao deleteAddressForEmployee(@RequestParam int empId,@RequestParam int addressId) {
		logger.debug("Request:Delete address for employee {} {}",empId,addressId);
		return employeeService.deleteAddressForEmployee(empId, addressId);
	}
	@DeleteMapping("/employee/deleteEmployee/{empId}")
	public void deleteEmployeeById(@PathVariable int empId)throws IncorrectEmployeeWithIdException {
		logger.debug("Request:Delete employee {}",empId);
		employeeService.deleteEmployeeById(empId);
	}
}
