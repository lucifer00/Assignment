package com.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.entity.Address;

public interface AddressRepository extends JpaRepository<Address,Integer>{

}
