package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Expense;
import com.example.demo.repository.ExpenseRepository;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @GetMapping("/total")
    public Double getTotalExpenses() {
        return expenseRepository.getTotalExpenses();
    }

    @GetMapping("/today")
    public Double getTodayExpenses() {
        LocalDate today = LocalDate.now();

        return expenseRepository
                .getTotalExpensesBetween(today, today);
    }

    @GetMapping("/total/last-7-days")
    public Double getTotalExpensesLast7Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        return expenseRepository
                .getTotalExpensesBetween(startDate, endDate);
    }

    @GetMapping("/total/last-30-days")
    public Double getTotalExpensesLast30Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);

        return expenseRepository
                .getTotalExpensesBetween(startDate, endDate);
    }

    @GetMapping("/total/this-year")
    public Double getTotalExpensesThisYear() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = LocalDate.of(endDate.getYear(), 1, 1);

        return expenseRepository
                .getTotalExpensesBetween(startDate, endDate);
    }

    @GetMapping("/{id}")
    public Expense getExpenseById(@PathVariable Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Expense updateExpense(
            @PathVariable Long id,
            @RequestBody Expense expense) {

        Expense existingExpense =
                expenseRepository.findById(id).orElse(null);

        if (existingExpense == null) {
            return null;
        }

        existingExpense.setExpenseDate(expense.getExpenseDate());
        existingExpense.setExpenseType(expense.getExpenseType());
        existingExpense.setDescription(expense.getDescription());
        existingExpense.setAmount(expense.getAmount());
        existingExpense.setPaymentMethod(expense.getPaymentMethod());
        existingExpense.setNotes(expense.getNotes());

        return expenseRepository.save(existingExpense);
    }

    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id) {

        if (!expenseRepository.existsById(id)) {
            return "Expense record not found";
        }

        expenseRepository.deleteById(id);

        return "Expense record deleted successfully";
    }
}