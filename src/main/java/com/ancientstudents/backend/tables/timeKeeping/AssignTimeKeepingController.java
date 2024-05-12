package com.ancientstudents.backend.tables.timeKeeping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ancientstudents.backend.exception.PayslipNotFoundException;
import com.ancientstudents.backend.tables.employee.Employee;
import com.ancientstudents.backend.tables.employee.EmployeeRepository;

@RestController
@RequestMapping("/api")
public class AssignTimeKeepingController {
    
    @Autowired
    private  AssignTimeKeepingRepository  assignTimeKeepingRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/assign/time-keeping")
    AssignTimeKeeping newtTimeKeeping(@RequestBody AssignTimeKeeping newAssignTimeKeeping){
        if(newAssignTimeKeeping == null ) return null;

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

    @SuppressWarnings("unlikely-arg-type")
    @RequestMapping("/assign/time-keeping/data")
    public ResponseEntity<?> getAssignTimeKeepByType(
        @RequestParam(value="prID") Long prID,
        @RequestParam(value="empId") Long empId, 
        @RequestParam(value = "type") String type
        ){
        
        List<AssignTimeKeeping> data = assignTimeKeepingRepository.findAll();
        List<AssignTimeKeeping> filteredData = new ArrayList<>();
        for(AssignTimeKeeping as : data){
            
            if(as.getPayroll().getId() == prID && as.getEmployee().getId()== empId){
                
                if(as.getType().equals(type)){
                    filteredData.add(as);
                }
                
            }
        }
        // Filter all Data under Payroll id with empID and varietyy of type such as "OVERTIME", "REGULAR", "VACATION"

        return ResponseEntity.ok(filteredData);
    }
}
