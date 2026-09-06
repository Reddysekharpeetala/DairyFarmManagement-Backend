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

import com.example.demo.entity.MilkSale;
import com.example.demo.repository.MilkSaleRepository;

@RestController
@RequestMapping("/api/milk-sales")
public class MilkSaleController {

    private final MilkSaleRepository milkSaleRepository;

    public MilkSaleController(MilkSaleRepository milkSaleRepository) {
        this.milkSaleRepository = milkSaleRepository;
    }

    @PostMapping
    public MilkSale addMilkSale(@RequestBody MilkSale milkSale) {
        return milkSaleRepository.save(milkSale);
    }

    @GetMapping
    public List<MilkSale> getAllMilkSales() {
        return milkSaleRepository.findAll();
    }

    @GetMapping("/total")
    public Double getTotalMilkSales() {
        return milkSaleRepository.getTotalMilkSales();
    }

    @GetMapping("/today")
    public Double getTodayMilkSales() {
        LocalDate today = LocalDate.now();

        return milkSaleRepository
                .getTotalMilkSalesBetween(today, today);
    }

    @GetMapping("/total/last-7-days")
    public Double getTotalMilkSalesLast7Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        return milkSaleRepository
                .getTotalMilkSalesBetween(startDate, endDate);
    }

    @GetMapping("/total/last-30-days")
    public Double getTotalMilkSalesLast30Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);

        return milkSaleRepository
                .getTotalMilkSalesBetween(startDate, endDate);
    }

    @GetMapping("/total/this-year")
    public Double getTotalMilkSalesThisYear() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = LocalDate.of(endDate.getYear(), 1, 1);

        return milkSaleRepository
                .getTotalMilkSalesBetween(startDate, endDate);
    }

    @GetMapping("/{id}")
    public MilkSale getMilkSaleById(@PathVariable Long id) {
        return milkSaleRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public MilkSale updateMilkSale(
            @PathVariable Long id,
            @RequestBody MilkSale milkSale) {

        MilkSale existingSale =
                milkSaleRepository.findById(id).orElse(null);

        if (existingSale == null) {
            return null;
        }

        existingSale.setSaleDate(milkSale.getSaleDate());
        existingSale.setCustomerName(milkSale.getCustomerName());
        existingSale.setCustomerPhone(milkSale.getCustomerPhone());
        existingSale.setQuantity(milkSale.getQuantity());
        existingSale.setPricePerLitre(milkSale.getPricePerLitre());
        existingSale.setTotalAmount(milkSale.getTotalAmount());
        existingSale.setPaymentStatus(milkSale.getPaymentStatus());
        existingSale.setNotes(milkSale.getNotes());

        return milkSaleRepository.save(existingSale);
    }

    @DeleteMapping("/{id}")
    public String deleteMilkSale(@PathVariable Long id) {

        if (!milkSaleRepository.existsById(id)) {
            return "Milk sale record not found";
        }

        milkSaleRepository.deleteById(id);

        return "Milk sale record deleted successfully";
    }
}