package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "milk_production")
public class MilkProduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cowId;
    private LocalDate productionDate;
    private Double morningQuantity;
    private Double eveningQuantity;
    private Double totalQuantity;
    private String quality;
    private String notes;

    public MilkProduction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCowId() {
        return cowId;
    }

    public void setCowId(Long cowId) {
        this.cowId = cowId;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public Double getMorningQuantity() {
        return morningQuantity;
    }

    public void setMorningQuantity(Double morningQuantity) {
        this.morningQuantity = morningQuantity;
    }

    public Double getEveningQuantity() {
        return eveningQuantity;
    }

    public void setEveningQuantity(Double eveningQuantity) {
        this.eveningQuantity = eveningQuantity;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}