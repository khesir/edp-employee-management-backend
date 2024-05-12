package com.ancientstudents.backend.tables.timeKeeping;


import java.util.Date;

import com.ancientstudents.backend.tables.employee.Employee;
import com.ancientstudents.backend.tables.payroll.Payroll;
import com.ancientstudents.backend.utils.CustomDateDeserializer;
import com.ancientstudents.backend.utils.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class AssignTimeKeeping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="payroll_id", referencedColumnName = "id")
    private Payroll payroll;

    @ManyToOne
    @JoinColumn(name="timekeeping_id", referencedColumnName = "id")
    private TimeKeeping timeKeeping;

    @ManyToOne
    @JoinColumn(name="employee_id", referencedColumnName = "id")
    private Employee employee;

    @Column(name = "hours_worked")
    private double hoursWorked;
    @Enumerated(EnumType.STRING)
    private TimeType type;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date workDate;
}

enum TimeType{
    VACATION,
    ON_BREAK,
    LATE,
    NIGHTSHIFT,
    REGULAR,
    OVERTIME
}