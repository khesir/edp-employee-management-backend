package com.ancientstudents.backend.tables.employee;

import com.ancientstudents.backend.exception.DataEmployeeNotFoundException;
import com.ancientstudents.backend.exception.DepartmentNotFoundException;
import com.ancientstudents.backend.exception.DesignationNotFoundException;
import com.ancientstudents.backend.exception.EmployeeNotFoundException;
import com.ancientstudents.backend.tables.department.Department;
import com.ancientstudents.backend.tables.department.DepartmentRepository;
import com.ancientstudents.backend.tables.designation.Designation;
import com.ancientstudents.backend.tables.designation.DesignationRepository;
import com.ancientstudents.backend.tables.employeeData.EmployeeData;
import com.ancientstudents.backend.tables.employeeData.EmployeeDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeDataRepository dataEmployeeRepository;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    // CRUD for Employee   
    @PostMapping("/employee")
    Employee newEmployee(@RequestBody Employee data){
        if(data == null) return null;

        data.setEmploymentStartDate(data.getEmploymentStartDate());
        data.setStatus(EmployeeStatus.OFFLINE);
        data.setCreatedAt(new Date());
        data.setLastUpdated(new Date());

        return employeeRepository.save(data);
    }

    @GetMapping("/employee")
    List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @GetMapping("/employee/{id}")
    Employee getEmployeeById(@PathVariable Long id){
        if(id == null) return null;
        return employeeRepository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException(id));
    }

    @PutMapping("employee/{id}")
    Employee updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id){
        if(id == null) return null;
        return employeeRepository.findById(id)
                .map(employee -> { 
                    employee.setEmployeeData(newEmployee.getEmployeeData());
                    employee.setDepartment(getDepartmentById(newEmployee.getDepartment().getId()));
                    employee.setDesignation(getDesignationById(newEmployee.getDesignation().getId()));
                    // Employee Metrics
                    employee.setEmployeeType(newEmployee.getEmployeeType());
                    employee.setHourlyRate(newEmployee.getHourlyRate());
                    employee.setStatus(newEmployee.getStatus());

                    // Employee identifications
                    employee.setBankAccountDetails(newEmployee.getBankAccountDetails());
                    employee.setTaxIdentification(newEmployee.getTaxIdentification());
                    employee.setSocialSecurity(newEmployee.getSocialSecurity());
                    employee.setPhilhealth(newEmployee.getPhilhealth());

                    // Employement Date
                    employee.setEmploymentStartDate(newEmployee.getEmploymentStartDate());
                    employee.setEmploymentEndDate(newEmployee.getEmploymentEndDate());

                    employee.setCreatedAt(newEmployee.getCreatedAt());
                    employee.setLastUpdated(new Date());
                    return employeeRepository.save(employee);
                }).orElseThrow(()->new EmployeeNotFoundException(id));
    }

    @DeleteMapping("employee/{id}")
    String deleteUser(@PathVariable Long id){
        if(id == null) return null;
        if(!employeeRepository.existsById(id)){
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
        return "Employee with id " + id + " has been deleted successfully.";
    }
    @RequestMapping(value = "employee/fetch", method=RequestMethod.GET)
    Employee findEmployeeByEmail(@RequestParam(value = "email") String param) {
        List<Employee> allemp = employeeRepository.findAll();
        Employee found = new Employee();
       
        for(Employee e : allemp){
            System.out.println(e.getEmployeeData().getEmail()+ " "+param);
            if(e.getEmployeeData().getEmail().equals(param)){
                found = e;
                break;
            }
        }
        return found;
    }
    
    // Manual operation for Employee

    private Designation getDesignationById( Long id){
        if(id == null) return null;
        return designationRepository.findById(id)
                .orElseThrow(()->new DesignationNotFoundException(id));
    }

    private Department getDepartmentById(Long id){
        if(id == null) return null;
        return departmentRepository.findById(id)
                .orElseThrow(()->new DepartmentNotFoundException(id));
    }

    EmployeeData getEmployeeDataById(Long id){
        if(id == null) return null;
        return dataEmployeeRepository.findById(id)
                .orElseThrow(()->new DataEmployeeNotFoundException(id));
    }
    
}
