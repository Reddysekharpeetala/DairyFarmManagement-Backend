package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.HealthRecord;
import com.example.demo.repository.HealthRecordRepository;
import com.example.demo.service.ExpenseService;

@RestController
@RequestMapping("/api/health-records")
public class HealthRecordController {

    private final HealthRecordRepository healthRecordRepository;
    private final ExpenseService expenseService;

    public HealthRecordController(
            HealthRecordRepository healthRecordRepository,
            ExpenseService expenseService) {

        this.healthRecordRepository = healthRecordRepository;
        this.expenseService = expenseService;
    }

    @PostMapping
    public HealthRecord addHealthRecord(
            @RequestBody HealthRecord healthRecord) {

        HealthRecord savedRecord =
                healthRecordRepository.save(healthRecord);

        expenseService.createHealthRecordExpense(savedRecord);

        return savedRecord;
    }

    @GetMapping
    public List<HealthRecord> getAllHealthRecords() {
        return healthRecordRepository.findAll();
    }

    @GetMapping("/{id}")
    public HealthRecord getHealthRecordById(@PathVariable Long id) {
        return healthRecordRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public HealthRecord updateHealthRecord(
            @PathVariable Long id,
            @RequestBody HealthRecord healthRecord) {

        HealthRecord existingRecord =
                healthRecordRepository.findById(id).orElse(null);

        if (existingRecord == null) {
            return null;
        }

        existingRecord.setCowId(healthRecord.getCowId());
        existingRecord.setVisitDate(healthRecord.getVisitDate());
        existingRecord.setHealthCondition(healthRecord.getHealthCondition());
        existingRecord.setDisease(healthRecord.getDisease());
        existingRecord.setTreatment(healthRecord.getTreatment());
        existingRecord.setMedicine(healthRecord.getMedicine());
        existingRecord.setVeterinaryDoctor(healthRecord.getVeterinaryDoctor());
        existingRecord.setCost(healthRecord.getCost());
        existingRecord.setNotes(healthRecord.getNotes());

        HealthRecord savedRecord =
                healthRecordRepository.save(existingRecord);

        expenseService.createHealthRecordExpense(savedRecord);

        return savedRecord;
    }

    @GetMapping("/count")
    public long getTotalHealthRecords() {
        return healthRecordRepository.count();
    }

    @DeleteMapping("/{id}")
    public String deleteHealthRecord(@PathVariable Long id) {

        if (!healthRecordRepository.existsById(id)) {
            return "Health record not found";
        }

        expenseService.deleteHealthRecordExpense(id);

        healthRecordRepository.deleteById(id);

        return "Health record deleted successfully";
    }
}