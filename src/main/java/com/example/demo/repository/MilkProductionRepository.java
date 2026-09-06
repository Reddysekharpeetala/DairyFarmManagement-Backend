package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.MilkProduction;

public interface MilkProductionRepository extends JpaRepository<MilkProduction, Long> {

    List<MilkProduction> findByProductionDateGreaterThanEqualOrderByProductionDateAsc(
            LocalDate date);

    List<MilkProduction> findByProductionDateBetweenOrderByProductionDateAsc(
            LocalDate startDate,
            LocalDate endDate);

    boolean existsByCowIdAndProductionDate(
            Long cowId,
            LocalDate productionDate);

    boolean existsByCowIdAndProductionDateAndIdNot(
            Long cowId,
            LocalDate productionDate,
            Long id);

    List<MilkProduction> findByProductionDate(
            LocalDate productionDate);

    @Query("SELECT COALESCE(SUM(m.totalQuantity), 0) FROM MilkProduction m")
    Double getTotalMilkProduction();

    @Query("SELECT COALESCE(SUM(m.totalQuantity), 0) FROM MilkProduction m WHERE m.productionDate BETWEEN :startDate AND :endDate")
    Double getTotalMilkProductionBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}