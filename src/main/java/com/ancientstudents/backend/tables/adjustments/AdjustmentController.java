package com.ancientstudents.backend.tables.adjustments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ancientstudents.backend.exception.EmployeeNotFoundException;
import com.ancientstudents.backend.exception.PayrollNotFoundException;
import com.ancientstudents.backend.exception.PayslipNotFoundException;
import com.ancientstudents.backend.tables.employee.Employee;
import com.ancientstudents.backend.tables.employee.EmployeeRepository;
import com.ancientstudents.backend.tables.payroll.Payroll;
import com.ancientstudents.backend.tables.payroll.PayrollRepository;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api")
public class AdjustmentController {
    
    @Autowired
    private AdjustmentRepository adjustmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PayrollRepository payrollRepository;

    @PostMapping("/adjustment")
    ResponseEntity<?> newAdjustment(@RequestBody Adjustments newAdjustments){
        if (newAdjustments == null) {
            return ResponseEntity.badRequest().body("Request body cannot be null");
        }
        newAdjustments.setCreatedAt(new Date());
        newAdjustments.setLastUpdated(new Date());
        newAdjustments.setPayroll(getPayrollById(newAdjustments.getPayroll().getId()));
        newAdjustments.setEmployee(getEmployeeById(newAdjustments.getEmployee().getId()));
        var savedData = adjustmentRepository.save(newAdjustments);
        return ResponseEntity.ok(savedData);
    }

    @GetMapping("/adjustment")
    List<Adjustments> getAllAdjustments(){
        return adjustmentRepository.findAll();
    }

    @GetMapping("/adjustments/{id}")
    Adjustments findAdjustments(@PathVariable Long id){
        return adjustmentRepository.findById(id)
                .orElseThrow(null);
    }

    @PutMapping("/adjustments/{id}")
    Adjustments updateTimeKeeping(@RequestBody Adjustments newAdjustments, @PathVariable Long id){
        if(id == null || newAdjustments == null) return null;
        return adjustmentRepository.findById(id)
                .map(adj -> {
                    adj.setCategory(newAdjustments.getCategory());
                    adj.setType(newAdjustments.getType());
                    adj.setAmount(newAdjustments.getAmount());
                    adj.setRemarks(newAdjustments.getRemarks());
                    adj.setLastUpdated(new Date());
                    return adjustmentRepository.save(adj);
                }).orElseThrow(null);
    }


    @DeleteMapping("/adjustments/{id}")
    String deletePayslip(@PathVariable Long id){
        if(id == null) return null;
        if(!adjustmentRepository.existsById(id)){
            throw new PayslipNotFoundException(id);
        }
        adjustmentRepository.deleteById(id);
        return "AssignedTimeKeeping with id " + id + " has been deleted successfully.";
    }

    @RequestMapping(value= "/adjustment/data", method = RequestMethod.GET)
    List<Adjustments> findAllAdjustmentByEmployeeIDAndPayrollID(
        @RequestParam(value = "empId") Long empId,
        @RequestParam(value = "prId") Long prId
    ) {
        List<Adjustments> allData = adjustmentRepository.findAll();

        List<Adjustments> filteredData = new ArrayList<>();

        for(Adjustments a : allData){
            if(a.getPayroll().getId() == prId && a.getEmployee().getId()== empId){
                filteredData.add(a);
            }
        }
        return filteredData;
    }
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

}
