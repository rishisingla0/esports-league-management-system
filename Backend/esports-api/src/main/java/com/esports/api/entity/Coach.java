package com.esports.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "coaches")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coach_id")
    private Long coachId;

    @Column(name = "coach_name", nullable = false, length = 100)
    private String coachName;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    public Coach() {
    }

    public Long getCoachId() {
        return coachId;
    }

    public String getCoachName() {
        return coachName;
    }

    public String getCountry() {
        return country;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
}