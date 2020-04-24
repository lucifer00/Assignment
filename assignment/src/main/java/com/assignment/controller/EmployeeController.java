package com.assignment.controller;

import java.util.List;
import java.util.Set;

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
	private EmployeeService employeeService;
	@PostMapping("/employee/addBulk")
	public String addBulkData(@RequestBody List<EmployeeDao> employees) {
		employeeService.addBulkEmployeeData(employees);
		return "Details added successfully";
	}
	@GetMapping("/employee/getAll")
	public List<EmployeeDao> getAllEmployeeData(){
		return employeeService.getAllEmployees();
	}
	@PostMapping("/employee/addNew")
	public EmployeeDao addNewEmployee(@RequestBody EmployeeDao employee) {
		return employeeService.addNewEmployee(employee);
	}
	@GetMapping("/employee/getById/{empId}")
	public EmployeeDao getEmployeeById(@PathVariable int empId)throws IncorrectEmployeeWithIdException {
		return employeeService.getEmployeeById(empId);
	}
	@GetMapping("/employee/getByName/{empName}")
	public EmployeeDao getEmployeeByEmpName(@PathVariable String empName)throws IncorrectEmployeeWithNameException {
		return employeeService.getByName(empName);
	}
	@GetMapping("/employee/getByPincode/{pincode}")
	public Set<EmployeeDao> getByPincode(@PathVariable Long pincode){
		return employeeService.getByPincode(pincode);
	}
	@PutMapping("/employee/addNewAddress/{empId}")
	public EmployeeDao addNewAddress(@PathVariable int empId,@RequestBody AddressDao address) {
		return employeeService.addNewAddress(empId, address);
	}
	@DeleteMapping("/employee/deleteAddress")
	public EmployeeDao deleteAddressForEmployee(@RequestParam int empId,@RequestParam int addressId) {
		return employeeService.deleteAddressForEmployee(empId, addressId);
	}
	@DeleteMapping("/employee/deleteEmployee/{empId}")
	public void deleteEmployeeById(@PathVariable int empId)throws IncorrectEmployeeWithIdException {
		employeeService.deleteEmployeeById(empId);
	}
}