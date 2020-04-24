package com.assignment.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.assignment.dao.AddressDao;
import com.assignment.dao.EmployeeDao;
import com.assignment.entity.Address;
import com.assignment.entity.Employee;
import com.assignment.exception.IncorrectEmployeeWithIdException;
import com.assignment.exception.IncorrectEmployeeWithNameException;
import com.assignment.repository.AddressRepository;
import com.assignment.repository.EmployeeRepository;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Service
public class EmloyeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private CacheManager cacheManager;
	@Override
	public EmployeeDao addNewEmployee(EmployeeDao employeeDao) {
		Employee employee = new DozerBeanMapper().map(employeeDao, Employee.class);
		Employee returnedOnSave = employeeRepository.save(employee);
		if (returnedOnSave != null) {
			cacheManager.getCache("first-level-cache").put(employeeDao.getEmpId(), employeeDao);
			cacheManager.getCache("second-level-cache").put(employeeDao.getEmpId(), employeeDao);
			return employeeDao;
		}
		return null;
	}

	@Override
	public EmployeeDao getEmployeeById(int id)throws IncorrectEmployeeWithIdException{
		EmployeeDao employeeDao=null;
		ValueWrapper valueWrapper=cacheManager.getCache("first-level-cache").get(id);
		if(valueWrapper!=null) {
			return (EmployeeDao)valueWrapper.get();
			}
		else {
			valueWrapper=cacheManager.getCache("second-level-cache").get(id);
			if(valueWrapper==null) {
				Optional<Employee> employee=employeeRepository.findById(id);
				if(employee.isPresent()){
					employeeDao=new DozerBeanMapper().map(employee, EmployeeDao.class);
					cacheManager.getCache("first-level-cache").put(id,employeeDao);
					cacheManager.getCache("second-level-cache").put(id,employeeDao);
				}
				else {
					throw new IncorrectEmployeeWithIdException(""+id);
				}
			}
			else {
				employeeDao=(EmployeeDao)valueWrapper.get();
				cacheManager.getCache("first-level-cache").put(id,employeeDao);
			}
			return employeeDao;
		}
	}
	@Override
	public void addBulkEmployeeData(List<EmployeeDao> employees) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		Runnable runnable = () -> {
			employees.forEach(t->{
				Employee employee=new DozerBeanMapper().map(t,Employee.class);
				employeeRepository.save(employee);
			});
		};
		executorService.submit(runnable);
		executorService.shutdown();
		autoUpdateCache();
	}

	@Override
	public List<EmployeeDao> getAllEmployees() {
		List<Employee> allEmployees = employeeRepository.findAll();
		List<EmployeeDao> toRet = new ArrayList<>();
		allEmployees.forEach(t -> {
			EmployeeDao employeeDao = new DozerBeanMapper().map(t, EmployeeDao.class);
			toRet.add(employeeDao);
		});
		return toRet;
	}
	@Scheduled(fixedRate = 60000*2)
	@Override
	public void autoUpdateCache() {
		List<EmployeeDao> employeeList=getAllEmployees();
		employeeList.forEach(t->{
			cacheManager.getCache("second-level-cache").put(t.getEmpId(), t);
		});
	}
	@Override
	public EmployeeDao getByName(String ename)throws IncorrectEmployeeWithNameException {
		Cache firstLevelCache=(Cache) cacheManager.getCache("first-level-cache").getNativeCache();
		Cache secondLevelCache=(Cache) cacheManager.getCache("second-level-cache").getNativeCache();
		EmployeeDao toRet=null;
		Map<Object, Element> employeeMap=firstLevelCache.getAll(firstLevelCache.getKeys());
		toRet=checkIfNameExists(employeeMap, ename);
		if(toRet==null) {
			employeeMap=secondLevelCache.getAll(secondLevelCache.getKeys());
			toRet=checkIfNameExists(employeeMap, ename);
			if(toRet!=null)cacheManager.getCache("first-level-cache").put(toRet.getEmpId(), toRet);
			else {
				Employee employee=employeeRepository.findByEmpName(ename);
				if(employee==null)throw new IncorrectEmployeeWithNameException(ename);
				toRet=new DozerBeanMapper().map(employee, EmployeeDao.class);
				cacheManager.getCache("first-level-cache").put(toRet.getEmpId(), toRet);
				cacheManager.getCache("second-level-cache").put(toRet.getEmpId(), toRet);
			}
		}
		return toRet;
	}
	public EmployeeDao checkIfNameExists(Map<Object,Element> employeeMap,String ename) {
		for(Map.Entry<Object, Element> entry:employeeMap.entrySet()) {
			EmployeeDao employeeDao=(EmployeeDao)entry.getValue().getObjectValue();
			if(employeeDao.getEmpName().equalsIgnoreCase(ename)) {
				return employeeDao;
			}
		}
		return null;
	}

	@Override
	public Set<EmployeeDao> getByPincode(long pincode) {
		List<Employee> employeeList=employeeRepository.findByPincode(pincode);
		Set<EmployeeDao> toRet=new HashSet<>();
		employeeList.forEach(t->{
			EmployeeDao employeeDao=new DozerBeanMapper().map(t, EmployeeDao.class);
			toRet.add(employeeDao);
		});
		return toRet;
	}
	@Override
	public EmployeeDao addNewAddress(int empId,AddressDao address) {
		Address addressNew=new DozerBeanMapper().map(address, Address.class);
		Employee employee=employeeRepository.getOne(empId);
		employee.getAddresses().add(addressNew);
		employeeRepository.saveAndFlush(employee);
		EmployeeDao employeeDao=new DozerBeanMapper().map(employee, EmployeeDao.class);
		cacheManager.getCache("first-level-cache").put(empId, employeeDao);
		cacheManager.getCache("second-level-cache").put(empId, employeeDao);
		return employeeDao;
	}

	@Override
	public EmployeeDao deleteAddressForEmployee(int empId, int addressId) {
		EmployeeDao employeeDao=null;
		addressRepository.deleteById(addressId);
		Optional<Employee> employee=employeeRepository.findById(empId);
		if(employee.isPresent()) {
			employeeDao=new DozerBeanMapper().map(employee.get(),EmployeeDao.class);
			cacheManager.getCache("first-level-cache").put(empId, employeeDao);
			cacheManager.getCache("second-level-cache").put(empId, employeeDao);
		}
		return employeeDao;
	}
	@Override
	public void deleteEmployeeById(int empId) throws IncorrectEmployeeWithIdException {
		EmployeeDao employeeDao=getEmployeeById(empId);
		if(employeeDao!=null) {
			employeeRepository.deleteById(empId);
			cacheManager.getCache("first-level-cache").evict(empId);
			cacheManager.getCache("second-level-cache").evict(empId);
		}
	}
}