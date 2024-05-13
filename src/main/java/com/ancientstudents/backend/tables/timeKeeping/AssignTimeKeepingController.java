package com.ancientstudents.backend.tables.timeKeeping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ancientstudents.backend.exception.EmployeeNotFoundException;
import com.ancientstudents.backend.exception.PayrollNotFoundException;
import com.ancientstudents.backend.exception.PayslipNotFoundException;
import com.ancientstudents.backend.tables.employee.Employee;
import com.ancientstudents.backend.tables.employee.EmployeeRepository;
import com.ancientstudents.backend.tables.payroll.Payroll;
import com.ancientstudents.backend.tables.payroll.PayrollRepository;

@RestController
@RequestMapping("/api")
public class AssignTimeKeepingController {
    
    @Autowired
    private  AssignTimeKeepingRepository  assignTimeKeepingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PayrollRepository payrollRepository;
    @Autowired
    private TimeKeepingRepository timeKeepingRepository;


    @PostMapping("/assign/time-keeping")
    AssignTimeKeeping newtTimeKeeping(@RequestBody AssignTimeKeeping newAssignTimeKeeping){
        if(newAssignTimeKeeping == null ) return null;
        
        System.out.println(newAssignTimeKeeping);
        newAssignTimeKeeping.setEmployee(getEmployeeById(newAssignTimeKeeping.getEmployee().getId()));
        newAssignTimeKeeping.setPayroll(getPayrollById(newAssignTimeKeeping.getPayroll().getId()));
        newAssignTimeKeeping.setTimeKeeping(getTimeKeepingById(newAssignTimeKeeping.getTimeKeeping().getId()));
        newAssignTimeKeeping.setWorkDate(new Date());
        return assignTimeKeepingRepository.save(newAssignTimeKeeping);
    }

    @GetMapping("/assign/time-keeping")
    List<AssignTimeKeeping> getAllTimeKeeping(){
        return assignTimeKeepingRepository.findAll();
    }

    @GetMapping("/assign/time-keeping/{id}")
    AssignTimeKeeping getTimeKeeping(@PathVariable Long id){
        return assignTimeKeepingRepository.findById(id).orElseThrow(null);
    }

    @PutMapping("/assign/time-keeping/{id}")
    AssignTimeKeeping updateTimeKeeping(@RequestBody AssignTimeKeeping newAssignTimeKeeping, @PathVariable Long id){
        if(id == null || newAssignTimeKeeping == null) return null;
        return assignTimeKeepingRepository.findById(id)
                .map(atk -> {
                    atk.setId(newAssignTimeKeeping.getId());
                    atk.setEmployee(newAssignTimeKeeping.getEmployee());
                    atk.setHoursWorked(newAssignTimeKeeping.getHoursWorked());
                    atk.setType(newAssignTimeKeeping.getType());
                    atk.setWorkDate(newAssignTimeKeeping.getWorkDate());
                    return assignTimeKeepingRepository.save(atk);
                }).orElseThrow(null);
    }

    @DeleteMapping("/assign/time-keeping/{id}")
    String deletePayslip(@PathVariable Long id){
        if(id == null) return null;
        if(!assignTimeKeepingRepository.existsById(id)){
            throw new PayslipNotFoundException(id);
        }
        assignTimeKeepingRepository.deleteById(id);
        return "AssignedTimeKeeping with id " + id + " has been deleted successfully.";
    }

    @RequestMapping(value = "/assign/time-keeping/data", method = RequestMethod.GET)
    public ResponseEntity<?> getAssignTimeKeepByType(
        @RequestParam(value="prID") Long prID,
        @RequestParam(value="empId") Long empId, 
        @RequestParam(value = "type",required = false) String type
        ){
        
        List<AssignTimeKeeping> data = assignTimeKeepingRepository.findAll();
        List<AssignTimeKeeping> filteredData = new ArrayList<>();
        for(AssignTimeKeeping as : data){
            
            if(as.getPayroll().getId() == prID && as.getEmployee().getId()== empId){
                if(type == null){
                    filteredData.add(as);
                } else if(as.getType().toString().equals(type)){
                    filteredData.add(as);
                }
                
            }
        }
        // Filter all Data under Payroll id with empID and varietyy of type such as "OVERTIME", "REGULAR", "VACATION"
        return ResponseEntity.ok(filteredData);
    }

    // Temporary Solution I have no idea how springboot deals with dependecies and component

    Payroll getPayrollById(Long id){
        if(id == null) return null;
        return payrollRepository.findById(id)
                .orElseThrow(()->new PayrollNotFoundException(id));
    }
    Employee getEmployeeById( Long id){
        if(id == null) return null;
        return employeeRepository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException(id));
    }
    TimeKeeping getTimeKeepingById(Long id){
        return timeKeepingRepository.findById(id).orElseThrow(null);
    }

}
