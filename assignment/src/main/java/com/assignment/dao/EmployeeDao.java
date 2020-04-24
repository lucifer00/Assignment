package com.assignment.dao;

import java.util.List;

import org.dozer.Mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class EmployeeDao {
	private int empId;
	private String empName;
	private String department;
	private List<AddressDao> addresses;
}
