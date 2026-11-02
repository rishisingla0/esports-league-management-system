package com.esports.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "organization_name", nullable = false, unique = true, length = 150)
    private String organizationName;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "founded_date")
    private LocalDate foundedDate;

    @Column(name = "website", length = 255)
    private String website;

    public Organization() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getCountry() {
        return country;
    }

    public LocalDate getFoundedDate() {
        return foundedDate;
    }

    public String getWebsite() {
        return website;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setFoundedDate(LocalDate foundedDate) {
        this.foundedDate = foundedDate;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}