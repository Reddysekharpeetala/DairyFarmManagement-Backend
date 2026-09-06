package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.entity.FarmWorker;
import com.example.demo.repository.FarmWorkerRepository;

@Service
public class WorkerSalaryScheduler {

    private final FarmWorkerRepository farmWorkerRepository;
    private final ExpenseService expenseService;

    public WorkerSalaryScheduler(
            FarmWorkerRepository farmWorkerRepository,
            ExpenseService expenseService) {

        this.farmWorkerRepository = farmWorkerRepository;
        this.expenseService = expenseService;
    }

    @Scheduled(cron = "0 59 23 * * *")
    public void createMonthlySalaryExpenses() {

        LocalDate today = LocalDate.now();
        LocalDate monthEnd = YearMonth.from(today).atEndOfMonth();

        if (!today.equals(monthEnd)) {
            return;
        }

        List<FarmWorker> workers = farmWorkerRepository.findAll();

        for (FarmWorker worker : workers) {
            expenseService.createWorkerSalaryExpense(
                    worker,
                    today
            );
        }
    }
}