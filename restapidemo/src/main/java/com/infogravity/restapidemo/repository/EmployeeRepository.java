package com.infogravity.restapidemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infogravity.restapidemo.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
