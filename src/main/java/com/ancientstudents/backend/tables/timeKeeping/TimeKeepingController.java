package com.ancientstudents.backend.tables.timeKeeping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ancientstudents.backend.exception.PayslipNotFoundException;

@RestController
@RequestMapping("/api")
public class TimeKeepingController {
    
    @Autowired
    private TimeKeepingRepository timeKeepingRepository;


    @PostMapping("/time-keeping")
    TimeKeeping newtTimeKeeping(@RequestBody TimeKeeping newTimeKeeping){
        if(newTimeKeeping == null ) return null;

        return timeKeepingRepository.save(newTimeKeeping);
    }

    @GetMapping("/time-keeping")
    List<TimeKeeping> getAllTimeKeeping(){
        return timeKeepingRepository.findAll();
    }

    @GetMapping("/time-keeping/{id}")
    TimeKeeping getTimeKeepingById(@PathVariable Long id){
        return timeKeepingRepository.findById(id).orElseThrow(null);
    }

    @PutMapping("/time-keeping/{id}")
    TimeKeeping updateTimeKeeping(@RequestBody TimeKeeping timeKeeping, @PathVariable Long id){
        if(id == null || timeKeeping == null) return null;
        return timeKeepingRepository.findById(id)
                .map(tk -> {
                    tk.setId(timeKeeping.getId());
                    tk.setDescription(timeKeeping.getDescription());
                    tk.setDailyWorkHourStart(timeKeeping.getDailyWorkHourStart());
                    tk.setDailyWorhourEnd(timeKeeping.getDailyWorhourEnd());
                    return timeKeepingRepository.save(tk);
             
                }).orElseThrow(null);
    }
    @DeleteMapping("/time-keeping/{id}")
    String deletePayslip(@PathVariable Long id){
        if(id == null) return null;
        if(!timeKeepingRepository.existsById(id)){
            throw new PayslipNotFoundException(id);
        }
        timeKeepingRepository.deleteById(id);
        return "TimeKeeping with id " + id + " has been deleted successfully.";
    }
}
