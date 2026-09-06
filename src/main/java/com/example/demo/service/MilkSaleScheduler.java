package com.example.demo.service;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.entity.MilkSale;
import com.example.demo.repository.MilkProductionRepository;
import com.example.demo.repository.MilkSaleRepository;

@Service
public class MilkSaleScheduler {

    private static final double DEFAULT_PRICE_PER_LITRE = 36.0;

    private final MilkProductionRepository milkProductionRepository;
    private final MilkSaleRepository milkSaleRepository;

    public MilkSaleScheduler(
            MilkProductionRepository milkProductionRepository,
            MilkSaleRepository milkSaleRepository) {

        this.milkProductionRepository = milkProductionRepository;
        this.milkSaleRepository = milkSaleRepository;
    }

    @Scheduled(cron = "0 59 23 * * *")
    public void createAutomaticDailySale() {

        LocalDate today = LocalDate.now();

        createAutomaticSaleForDate(today);
    }

    private void createAutomaticSaleForDate(LocalDate date) {

        if (milkSaleRepository.existsBySaleDate(date)) {
            return;
        }

        Double totalProduction =
                milkProductionRepository.getTotalMilkProductionBetween(
                        date,
                        date
                );

        if (totalProduction == null || totalProduction <= 0) {
            return;
        }

        double totalAmount =
                totalProduction * DEFAULT_PRICE_PER_LITRE;

        MilkSale automaticSale = new MilkSale();

        automaticSale.setSaleDate(date);
        automaticSale.setCustomerName("Automatic Sale");
        automaticSale.setCustomerPhone("");
        automaticSale.setQuantity(totalProduction);
        automaticSale.setPricePerLitre(DEFAULT_PRICE_PER_LITRE);
        automaticSale.setTotalAmount(totalAmount);
        automaticSale.setPaymentStatus("Pending");
        automaticSale.setNotes(
                "Automatically generated from daily milk production."
        );

        milkSaleRepository.save(automaticSale);
    }
}