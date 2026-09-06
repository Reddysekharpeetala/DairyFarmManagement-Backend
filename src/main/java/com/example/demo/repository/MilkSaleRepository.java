package com.example.demo.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.MilkSale;

public interface MilkSaleRepository extends JpaRepository<MilkSale, Long> {

    @Query("SELECT COALESCE(SUM(m.totalAmount), 0) FROM MilkSale m")
    Double getTotalMilkSales();

    @Query("SELECT COALESCE(SUM(m.totalAmount), 0) FROM MilkSale m WHERE m.saleDate BETWEEN :startDate AND :endDate")
    Double getTotalMilkSalesBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    boolean existsBySaleDate(LocalDate saleDate);
}