package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ActivityResponse;
import com.example.demo.entity.Cow;
import com.example.demo.entity.Expense;
import com.example.demo.entity.FarmWorker;
import com.example.demo.entity.Feed;
import com.example.demo.entity.HealthRecord;
import com.example.demo.entity.MilkProduction;
import com.example.demo.entity.MilkSale;
import com.example.demo.repository.CowRepository;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.FarmWorkerRepository;
import com.example.demo.repository.FeedRepository;
import com.example.demo.repository.HealthRecordRepository;
import com.example.demo.repository.MilkProductionRepository;
import com.example.demo.repository.MilkSaleRepository;

@RestController
@RequestMapping("/api/dashboard")
public class RecentActivityController {

    private final CowRepository cowRepository;
    private final MilkProductionRepository milkProductionRepository;
    private final MilkSaleRepository milkSaleRepository;
    private final ExpenseRepository expenseRepository;
    private final FarmWorkerRepository farmWorkerRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final FeedRepository feedRepository;

    public RecentActivityController(
            CowRepository cowRepository,
            MilkProductionRepository milkProductionRepository,
            MilkSaleRepository milkSaleRepository,
            ExpenseRepository expenseRepository,
            FarmWorkerRepository farmWorkerRepository,
            HealthRecordRepository healthRecordRepository,
            FeedRepository feedRepository) {

        this.cowRepository = cowRepository;
        this.milkProductionRepository = milkProductionRepository;
        this.milkSaleRepository = milkSaleRepository;
        this.expenseRepository = expenseRepository;
        this.farmWorkerRepository = farmWorkerRepository;
        this.healthRecordRepository = healthRecordRepository;
        this.feedRepository = feedRepository;
    }

    @GetMapping("/recent-activities")
    public List<ActivityResponse> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit) {

        List<ActivityResponse> activities = new ArrayList<>();

        for (Cow cow : cowRepository.findAll()) {
            LocalDate date = cow.getPurchaseDate();

            if (date != null) {
                activities.add(new ActivityResponse(
                        "Cow",
                        "Cow " + getCowName(cow) + " record added",
                        "🐄",
                        date
                ));
            }
        }

        for (MilkProduction production : milkProductionRepository.findAll()) {
            LocalDate date = production.getProductionDate();

            if (date != null) {
                activities.add(new ActivityResponse(
                        "Milk Production",
                        "Milk production recorded - "
                                + formatNumber(production.getTotalQuantity()) + " L",
                        "🥛",
                        date
                ));
            }
        }

        for (MilkSale sale : milkSaleRepository.findAll()) {
            LocalDate date = sale.getSaleDate();

            if (date != null) {
                activities.add(new ActivityResponse(
                        "Milk Sale",
                        "Milk sale recorded - ₹"
                                + formatNumber(sale.getTotalAmount()),
                        "💰",
                        date
                ));
            }
        }

        for (Expense expense : expenseRepository.findAll()) {
            LocalDate date = expense.getExpenseDate();

            if (date != null) {
                activities.add(new ActivityResponse(
                        "Expense",
                        "Expense added - ₹"
                                + formatNumber(expense.getAmount()),
                        "💸",
                        date
                ));
            }
        }

        for (FarmWorker worker : farmWorkerRepository.findAll()) {
            LocalDate date = worker.getJoiningDate();

            if (date != null) {
                activities.add(new ActivityResponse(
                        "Farm Worker",
                        "Farm worker " + safeText(worker.getName()) + " added",
                        "👨‍🌾",
                        date
                ));
            }
        }

        for (HealthRecord healthRecord : healthRecordRepository.findAll()) {
            LocalDate date = healthRecord.getVisitDate();

            if (date != null) {
                activities.add(new ActivityResponse(
                        "Health Record",
                        "Health record added for Cow #"
                                + safeText(healthRecord.getCowId()),
                        "🩺",
                        date
                ));
            }
        }

        for (Feed feed : feedRepository.findAll()) {
            LocalDate date = feed.getFeedDate();

            if (date != null) {
                activities.add(new ActivityResponse(
                        "Feed",
                        "Feed record added - "
                                + safeText(feed.getFeedName()),
                        "🌾",
                        date
                ));
            }
        }

        activities.sort(
                Comparator.comparing(
                        ActivityResponse::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        if (limit < 1) {
            limit = 10;
        }

        if (activities.size() > limit) {
            return new ArrayList<>(activities.subList(0, limit));
        }

        return activities;
    }

    private String getCowName(Cow cow) {
        if (cow.getTagNumber() != null && !cow.getTagNumber().isBlank()) {
            return "#" + cow.getTagNumber();
        }

        if (cow.getName() != null && !cow.getName().isBlank()) {
            return cow.getName();
        }

        if (cow.getId() != null) {
            return "#" + cow.getId();
        }

        return "record";
    }

    private String formatNumber(Double value) {
        if (value == null) {
            return "0.00";
        }

        return String.format("%.2f", value);
    }

    private String safeText(Object value) {
        if (value == null) {
            return "Unknown";
        }

        String text = String.valueOf(value).trim();

        if (text.isEmpty()) {
            return "Unknown";
        }

        return text;
    }
}