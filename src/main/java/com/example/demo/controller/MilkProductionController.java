package com.example.demo.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.Cow;
import com.example.demo.entity.MilkProduction;
import com.example.demo.repository.CowRepository;
import com.example.demo.repository.MilkProductionRepository;

@RestController
@RequestMapping("/api/milk-production")
public class MilkProductionController {

    private final MilkProductionRepository milkProductionRepository;
    private final CowRepository cowRepository;

    public MilkProductionController(
            MilkProductionRepository milkProductionRepository,
            CowRepository cowRepository) {

        this.milkProductionRepository = milkProductionRepository;
        this.cowRepository = cowRepository;
    }

    @PostMapping
    public MilkProduction addMilkProduction(
            @RequestBody MilkProduction milkProduction) {

        if (milkProduction.getCowId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cow ID is required");
        }

        if (milkProduction.getProductionDate() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Production date is required");
        }

        if (!cowRepository.existsById(milkProduction.getCowId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected Cow ID does not exist");
        }

        if (milkProductionRepository.existsByCowIdAndProductionDate(
                milkProduction.getCowId(),
                milkProduction.getProductionDate())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Milk production already exists for this cow on this date");
        }

        return milkProductionRepository.save(milkProduction);
    }

    @GetMapping
    public List<MilkProduction> getAllMilkProduction() {
        return milkProductionRepository.findAll();
    }

    @GetMapping("/{id}")
    public MilkProduction getMilkProductionById(
            @PathVariable Long id) {

        return milkProductionRepository
                .findById(id)
                .orElse(null);
    }

    @GetMapping("/available-cows")
    public List<Cow> getAvailableCows(
            @RequestParam LocalDate date,
            @RequestParam(required = false) Long excludeId) {

        List<MilkProduction> productions =
                milkProductionRepository.findByProductionDate(date);

        Set<Long> usedCowIds = productions.stream()
                .filter(production ->
                        excludeId == null ||
                        !production.getId().equals(excludeId))
                .map(MilkProduction::getCowId)
                .filter(cowId -> cowId != null)
                .collect(Collectors.toCollection(HashSet::new));

        return cowRepository.findAll()
                .stream()
                .filter(cow -> cow.getId() != null)
                .filter(cow -> !usedCowIds.contains(cow.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/last-7-days")
    public List<MilkProduction> getLast7Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        return milkProductionRepository
                .findByProductionDateBetweenOrderByProductionDateAsc(
                        startDate,
                        endDate);
    }

    @GetMapping("/last-30-days")
    public List<MilkProduction> getLast30Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);

        return milkProductionRepository
                .findByProductionDateBetweenOrderByProductionDateAsc(
                        startDate,
                        endDate);
    }

    @GetMapping("/this-year")
    public List<MilkProduction> getThisYear() {
        LocalDate startDate =
                LocalDate.of(LocalDate.now().getYear(), 1, 1);

        LocalDate endDate = LocalDate.now();

        return milkProductionRepository
                .findByProductionDateBetweenOrderByProductionDateAsc(
                        startDate,
                        endDate);
    }

    @GetMapping("/total")
    public Double getTotalMilkProduction() {
        return milkProductionRepository.getTotalMilkProduction();
    }

    @GetMapping("/today")
    public Double getTodayMilkProduction() {
        LocalDate today = LocalDate.now();

        return milkProductionRepository
                .getTotalMilkProductionBetween(today, today);
    }

    @GetMapping("/total/last-7-days")
    public Double getTotalMilkProductionLast7Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        return milkProductionRepository
                .getTotalMilkProductionBetween(
                        startDate,
                        endDate);
    }

    @GetMapping("/total/last-30-days")
    public Double getTotalMilkProductionLast30Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);

        return milkProductionRepository
                .getTotalMilkProductionBetween(
                        startDate,
                        endDate);
    }

    @GetMapping("/total/this-year")
    public Double getTotalMilkProductionThisYear() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate =
                LocalDate.of(endDate.getYear(), 1, 1);

        return milkProductionRepository
                .getTotalMilkProductionBetween(
                        startDate,
                        endDate);
    }

    @PutMapping("/{id}")
    public MilkProduction updateMilkProduction(
            @PathVariable Long id,
            @RequestBody MilkProduction milkProduction) {

        MilkProduction existingProduction =
                milkProductionRepository
                        .findById(id)
                        .orElse(null);

        if (existingProduction == null) {
            return null;
        }

        if (milkProduction.getCowId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cow ID is required");
        }

        if (milkProduction.getProductionDate() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Production date is required");
        }

        if (!cowRepository.existsById(milkProduction.getCowId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected Cow ID does not exist");
        }

        if (milkProductionRepository
                .existsByCowIdAndProductionDateAndIdNot(
                        milkProduction.getCowId(),
                        milkProduction.getProductionDate(),
                        id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Milk production already exists for this cow on this date");
        }

        existingProduction.setCowId(milkProduction.getCowId());
        existingProduction.setProductionDate(
                milkProduction.getProductionDate());
        existingProduction.setMorningQuantity(
                milkProduction.getMorningQuantity());
        existingProduction.setEveningQuantity(
                milkProduction.getEveningQuantity());
        existingProduction.setTotalQuantity(
                milkProduction.getTotalQuantity());
        existingProduction.setQuality(
                milkProduction.getQuality());
        existingProduction.setNotes(
                milkProduction.getNotes());

        return milkProductionRepository.save(existingProduction);
    }

    @DeleteMapping("/{id}")
    public String deleteMilkProduction(
            @PathVariable Long id) {

        if (!milkProductionRepository.existsById(id)) {
            return "Milk production record not found";
        }

        milkProductionRepository.deleteById(id);

        return "Milk production record deleted successfully";
    }
}