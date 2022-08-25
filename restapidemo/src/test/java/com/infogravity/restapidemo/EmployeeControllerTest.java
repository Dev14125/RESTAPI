package com.infogravity.restapidemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infogravity.restapidemo.entity.Employee;
import com.infogravity.restapidemo.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		employeeRepository.deleteAll();
	}

	@Test
	public void createEmployee_thenReturnSavedEmployee() throws Exception {

		
		Employee employee = Employee.builder().firstName("Robert").lastName("BigLowe").email("robertbiglowe@example.com").department("IT")
				.build();

		ResultActions response = mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		response.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())))
				.andExpect(jsonPath("$.department", is(employee.getDepartment())));

	}

	@Test
	public void getAllEmployees_thenReturnEmployeesList() throws Exception {
		
		List<Employee> listOfEmployees = new ArrayList<>();
		listOfEmployees
				.add(Employee.builder().firstName("Robert").lastName("Biglowe").email("robertbiglowe@example.com").department("IT").build());
		listOfEmployees.add(Employee.builder().firstName("Mark").lastName("Zachery").email("markzachery.com").department("HR").build());
		employeeRepository.saveAll(listOfEmployees);
	
		ResultActions response = mockMvc.perform(get("/api/employees"));

		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listOfEmployees.size())));

	}

	@Test
	public void getEmployeeById_thenReturnEmployeeObject() throws Exception {
		
		Employee employee = Employee.builder().firstName("Robert").lastName("Biglowe").email("robertbiglowe@example.com").department("IT")
				.build();
		employeeRepository.save(employee);

		
		ResultActions response = mockMvc.perform(get("/api/employees/emp_id={id}", employee.getId()));

		
		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())))
				.andExpect(jsonPath("$.department", is(employee.getDepartment())));

	}

	@Test
	public void getEmployeeById_thenReturnEmpty() throws Exception {
		
		long employeeId = 1L;
		Employee employee = Employee.builder().firstName("Robert").lastName("Biglowe").email("robertbiglowe@example.com").department("IT")
				.build();
		employeeRepository.save(employee);

		
		ResultActions response = mockMvc.perform(get("/api/employees/emp_id={id}", employeeId));

		
		response.andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void updateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
		
		Employee savedEmployee = Employee.builder().firstName("Robert").lastName("Biglowe").email("robertbiglowe@example.com").department("IT")
				.build();
		employeeRepository.save(savedEmployee);

		Employee updatedEmployee = Employee.builder().firstName("Kevin").lastName("Earl").email("kevinearl@example.com").department("IT")
				.build();

		
		ResultActions response = mockMvc.perform(put("/api/employees/emp_id={id}", savedEmployee.getId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedEmployee)));

		
		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
				.andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())))
				.andExpect(jsonPath("$.department", is(updatedEmployee.getDepartment())));
	}

	
	@Test
	public void updateEmployee_thenReturn404() throws Exception {
		
		long employeeId = 1L;
		
		Employee savedEmployee = Employee.builder().firstName("Robert").lastName("Biglowe").email("robertbiglowe@example.com").department("IT")
				.build();
		employeeRepository.save(savedEmployee);

		Employee updatedEmployee = Employee.builder().firstName("Kevin").lastName("Earl").email("kevinearl@example.com").department("IT")
				.build();

		
		ResultActions response = mockMvc.perform(put("/api/employees/emp_id={id}", employeeId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedEmployee)));

		
		response.andExpect(status().isNotFound()).andDo(print());
	}

	
	@Test
	public void deleteEmployee_thenReturn200() throws Exception {
		
		Employee savedEmployee = Employee.builder().firstName("Robert").lastName("Biglowe").email("robertbiglowe@example.com").department("IT")
				.build();
		employeeRepository.save(savedEmployee);

		
		ResultActions response = mockMvc.perform(delete("/api/employees/emp_id={id}", savedEmployee.getId()));

		
		response.andExpect(status().isOk()).andDo(print());
	}

}
