package com.ancientstudents.backend.tables.timeKeeping;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class TimeKeeping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    
    @Column(name = "daily_work_hour_start")
    private Time dailyWorkHourStart;

    @Column(name = "daily_work_hour_end")
    private Time dailyWorhourEnd;
}
