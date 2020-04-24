package com.assignment.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class AddressDao {
	private int addressId;
	private String address;
	private String city;
	private String state;
	private long pincode;
}
