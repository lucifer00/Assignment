package com.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.assignment.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
	public Employee findByEmpName(String empName);
	
	@Query("Select e FROM Employee e JOIN e.addresses a where a.pincode=?1")
	public List<Employee> findByPincode(long pincode);
}
