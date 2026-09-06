package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Expense;
import com.example.demo.entity.FarmWorker;
import com.example.demo.entity.Feed;
import com.example.demo.entity.HealthRecord;
import com.example.demo.repository.ExpenseRepository;

@Service
public class ExpenseService {

    private static final String FEED = "FEED";
    private static final String HEALTH_RECORD = "HEALTH_RECORD";
    private static final String FARM_WORKER_SALARY = "FARM_WORKER_SALARY";

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense createFeedExpense(Feed feed) {

        if (feed.getId() == null) {
            return null;
        }

        if (feed.getCost() == null || feed.getCost() <= 0) {
            deleteFeedExpense(feed.getId());
            return null;
        }

        Expense expense = expenseRepository
                .findBySourceTypeAndSourceId(FEED, feed.getId())
                .stream()
                .findFirst()
                .orElseGet(Expense::new);

        expense.setExpenseDate(feed.getFeedDate());
        expense.setExpenseType("Feed Cost");
        expense.setDescription("Feed cost for " + feed.getFeedName());
        expense.setAmount(feed.getCost());
        expense.setPaymentMethod("Not Specified");
        expense.setNotes("Automatically generated from feed record.");
        expense.setSourceType(FEED);
        expense.setSourceId(feed.getId());

        return expenseRepository.save(expense);
    }

    public void deleteFeedExpense(Long feedId) {

        expenseRepository
                .findBySourceTypeAndSourceId(FEED, feedId)
                .forEach(expenseRepository::delete);
    }

    public Expense createHealthRecordExpense(HealthRecord record) {

        if (record.getId() == null) {
            return null;
        }

        if (record.getCost() == null || record.getCost() <= 0) {
            deleteHealthRecordExpense(record.getId());
            return null;
        }

        Expense expense = expenseRepository
                .findBySourceTypeAndSourceId(
                        HEALTH_RECORD,
                        record.getId())
                .stream()
                .findFirst()
                .orElseGet(Expense::new);

        String description =
                "Health expense for Cow #" + record.getCowId();

        if (record.getMedicine() != null
                && !record.getMedicine().trim().isEmpty()) {

            description += " - " + record.getMedicine().trim();

        } else if (record.getTreatment() != null
                && !record.getTreatment().trim().isEmpty()) {

            description += " - " + record.getTreatment().trim();
        }

        expense.setExpenseDate(record.getVisitDate());
        expense.setExpenseType("Veterinary/Medicine");
        expense.setDescription(description);
        expense.setAmount(record.getCost());
        expense.setPaymentMethod("Not Specified");
        expense.setNotes("Automatically generated from health record.");
        expense.setSourceType(HEALTH_RECORD);
        expense.setSourceId(record.getId());

        return expenseRepository.save(expense);
    }

    public void deleteHealthRecordExpense(Long recordId) {

        expenseRepository
                .findBySourceTypeAndSourceId(
                        HEALTH_RECORD,
                        recordId)
                .forEach(expenseRepository::delete);
    }

    public Expense createWorkerSalaryExpense(
            FarmWorker worker,
            LocalDate salaryDate) {

        if (worker.getId() == null
                || worker.getSalary() == null
                || worker.getSalary() <= 0
                || !"Active".equalsIgnoreCase(worker.getStatus())) {

            return null;
        }

        LocalDate monthStart =
                salaryDate.withDayOfMonth(1);

        LocalDate monthEnd =
                YearMonth.from(salaryDate).atEndOfMonth();

        boolean alreadyExists =
                expenseRepository
                        .existsBySourceTypeAndSourceIdAndExpenseDateBetween(
                                FARM_WORKER_SALARY,
                                worker.getId(),
                                monthStart,
                                monthEnd);

        if (alreadyExists) {
            return null;
        }

        Expense expense = new Expense();

        expense.setExpenseDate(monthEnd);
        expense.setExpenseType("Worker Salary");
        expense.setDescription(
                "Salary for " + worker.getName());
        expense.setAmount(worker.getSalary());
        expense.setPaymentMethod("Not Specified");
        expense.setNotes(
                "Automatically generated monthly salary expense.");
        expense.setSourceType(FARM_WORKER_SALARY);
        expense.setSourceId(worker.getId());

        return expenseRepository.save(expense);
    }

    public void deleteWorkerSalaryExpense(Long workerId) {

        expenseRepository
                .findBySourceTypeAndSourceId(
                        FARM_WORKER_SALARY,
                        workerId)
                .forEach(expenseRepository::delete);
    }
}