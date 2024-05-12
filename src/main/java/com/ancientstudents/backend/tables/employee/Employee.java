package com.ancientstudents.backend.tables.employee;

import java.util.Date;


import com.ancientstudents.backend.tables.department.Department;
import com.ancientstudents.backend.tables.designation.Designation;
import com.ancientstudents.backend.tables.employeeData.EmployeeData;
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

enum EmployeeType {
    FULL_TIME,
    PART_TIME,
    CONTRACT,
    TEMPORARY,
    INTERN
}

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign Keys
    @ManyToOne
    @JoinColumn(name="department_id", referencedColumnName = "id")
    private Department department;
    @ManyToOne
    @JoinColumn(name="designation_id", referencedColumnName = "id")
    private Designation designation;
    @ManyToOne
    @JoinColumn(name="employeedata_id", referencedColumnName = "id")
    private EmployeeData employeeData;
    
    // Employee Metrics
    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;
    private double hourlyRate;
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    // Employee identifications
    private String BankAccountDetails;
    private String TaxIdentification;
    private String SocialSecurity;
    private String Philhealth;

    // Employment Date 
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Column(name = "employment_start_date")
    private Date employmentStartDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Column(name = "employement_end_date")
    private Date employmentEndDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Column(name = "last_updated")
    private Date lastUpdated;
}