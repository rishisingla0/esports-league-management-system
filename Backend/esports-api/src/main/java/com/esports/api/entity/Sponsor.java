package com.esports.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sponsors")
public class Sponsor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sponsor_id")
    private Long sponsorId;

    @Column(name = "sponsor_name", nullable = false, unique = true, length = 150)
    private String sponsorName;

    @Column(length = 100)
    private String industry;

    @Column(length = 100)
    private String country;

    @Column(length = 255)
    private String website;

    public Sponsor() {}

    public Long getSponsorId() { return sponsorId; }
    public void setSponsorId(Long sponsorId) { this.sponsorId = sponsorId; }

    public String getSponsorName() { return sponsorName; }
    public void setSponsorName(String sponsorName) { this.sponsorName = sponsorName; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
}