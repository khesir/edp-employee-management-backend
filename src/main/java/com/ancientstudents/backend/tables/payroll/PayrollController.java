package com.ancientstudents.backend.tables.payroll;

import com.ancientstudents.backend.exception.EmployeeNotFoundException;
import com.ancientstudents.backend.exception.PayrollNotFoundException;
import com.ancientstudents.backend.exception.SignatoryNotFoundException;
import com.ancientstudents.backend.tables.employee.Employee;
import com.ancientstudents.backend.tables.employee.EmployeeRepository;
import com.ancientstudents.backend.tables.signatory.Signatory;
import com.ancientstudents.backend.tables.signatory.SignatoryRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api")
public class PayrollController {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SignatoryRepository signatoryRepository;
    @Autowired
    private PayrollRepository payrollRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @PostMapping("/payroll")
    Payroll newPayroll(@RequestBody Payroll newPayroll){
        if(newPayroll == null) return null;
        Payroll x = new Payroll();
        x.setPayrollFrequency(newPayroll.getPayrollFrequency());
        x.setSignatory(getSignatoryById(newPayroll.getSignatory().getId()));
        x.setStart(newPayroll.getStart());
        x.setEnd(newPayroll.getEnd());
        x.setPayrollDate(newPayroll.getPayrollDate());
        x.setEmployee(newPayroll.getEmployee());
        x.setCreatedAt(new Date());
        x.setLastUpdated(new Date());
        return payrollRepository.save(x);
    }

    @GetMapping("/payroll")
    List<Payroll> getAllPayrolls(){
        return payrollRepository.findAll();
    }

    @GetMapping("/payroll/{id}")
    Payroll getPayrollById(@PathVariable Long id){
        if(id == null) return null;
        return payrollRepository.findById(id)
                .orElseThrow(()->new PayrollNotFoundException(id));
    }

    @PutMapping("payroll/{id}")
    Payroll updatePayroll(@RequestBody Payroll newPayroll, @PathVariable Long id){
        if(id == null) return null;
        return payrollRepository.findById(id)
                .map(payroll -> {
                    // ForeignKeys
                    payroll.setSignatory(newPayroll.getSignatory());
                    payroll.setEmployee(newPayroll.getEmployee());

                    // Parameter Settings
                    payroll.setPayrollFrequency(newPayroll.getPayrollFrequency());

                    // Duration
                    payroll.setStart(newPayroll.getStart());
                    payroll.setEnd(newPayroll.getEnd());
                    
                    payroll.setCreatedAt(newPayroll.getCreatedAt());
                    payroll.setLastUpdated(new Date());
                    return payrollRepository.save(payroll);
                }).orElseThrow(()->new PayrollNotFoundException(id));
    }
    

    @DeleteMapping("payroll/{id}")
    String deletePayroll(@PathVariable Long id){
        if(id == null) return null;
        if(!payrollRepository.existsById(id)){
            throw new PayrollNotFoundException(id);
        }
        payrollRepository.deleteById(id);
        return "Payroll with id " + id + " has been deleted successfully.";
    }

    @RequestMapping(value = "payroll/add", method=RequestMethod.GET)
    ResponseEntity<?> addEmployeeInPayroll(@RequestParam(value ="empId") String empId,@RequestParam(value ="payrollID") String payrollID){
        Payroll payroll = getPayrollById( Long.parseLong(payrollID));
        
        List<Employee> employeeINpayroll = payroll.getEmployee();
        
        Employee newEmployee = getEmployeeById(Long.parseLong(empId));
        employeeINpayroll.add(newEmployee);
        
        payroll.setEmployee(employeeINpayroll);
        payrollRepository.save(payroll);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "payroll/remove", method=RequestMethod.GET)
    ResponseEntity<?> removeEmployeeInPayroll(@RequestParam(value ="empId") String empId,@RequestParam(value ="payrollID") String payrollID){
        Payroll payroll = getPayrollById( Long.parseLong(payrollID));
        
        List<Employee> employeeINpayroll = payroll.getEmployee();
        
        Employee newEmployee = getEmployeeById(Long.parseLong(empId));

        employeeINpayroll.remove(newEmployee);
        
        payroll.setEmployee(employeeINpayroll);
        payrollRepository.save(payroll);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "payroll/notInPayroll", method=RequestMethod.GET)
    List<Employee> findAllNotInPayroll(@RequestParam(value ="id") Long id){
        List<Employee> employee = getAllEmployees();
        List<Employee> employeeINpayroll = getPayrollById(id).getEmployee();
        List<Employee> filterEmployees = new ArrayList<>();
        for(Employee emp : employee){
            if(!employeeINpayroll.contains(emp)){
                filterEmployees.add(emp);
            }
        }
       return filterEmployees;
    } 
     
    Signatory getSignatoryById( Long id){
        if(id == null) return null;
        return signatoryRepository.findById(id)
                .orElseThrow(()->new SignatoryNotFoundException(id));
    }

    List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    Employee getEmployeeById(Long id){
        if(id == null) return null;
        return employeeRepository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException(id));
    }

}
