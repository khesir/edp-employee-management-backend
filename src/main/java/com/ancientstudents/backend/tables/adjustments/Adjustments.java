package com.ancientstudents.backend.tables.adjustments;

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
public class Adjustments {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="employee_id", referencedColumnName="id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "payroll_id", referencedColumnName="id")
    private Payroll payroll;

    @Enumerated(EnumType.STRING)
    private AdjustmentCategory category;
    
    @Enumerated(EnumType.STRING)
    private PayType type;
    private String name;
    private double amount;

    private String remarks;

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


enum PayType{
    EARNING,
    DEDUCTION
}

enum AdjustmentCategory{
    ALLOWANCE,
    BONUS,
    COMMISIONS,
    MISCELLANEOUS,
    SALARY_ADJUSTMENTS
}