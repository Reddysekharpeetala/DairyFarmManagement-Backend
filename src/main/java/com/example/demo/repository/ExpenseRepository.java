package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    Double getTotalExpenses();

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate")
    Double getTotalExpensesBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<Expense> findBySourceTypeAndSourceId(
            String sourceType,
            Long sourceId);

    boolean existsBySourceTypeAndSourceIdAndExpenseDateBetween(
            String sourceType,
            Long sourceId,
            LocalDate startDate,
            LocalDate endDate);
}