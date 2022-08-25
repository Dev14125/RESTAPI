package com.infogravity.restapidemo.service;

import java.util.List;
import java.util.Optional;

import com.infogravity.restapidemo.entity.Employee;

public interface EmployeeService {

	Employee saveEmployee(Employee employee);

	List<Employee> getAllEmployees();

	Optional<Employee> getEmployeeById(long id);

	Employee updateEmployee(Employee updatedEmployee);

	public void deleteEmployee(long id);

}
