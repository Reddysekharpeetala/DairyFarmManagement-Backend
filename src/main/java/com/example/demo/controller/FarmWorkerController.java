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

import com.example.demo.entity.FarmWorker;
import com.example.demo.repository.FarmWorkerRepository;
import com.example.demo.service.ExpenseService;

@RestController
@RequestMapping("/api/farm-workers")
public class FarmWorkerController {

    private final FarmWorkerRepository farmWorkerRepository;
    private final ExpenseService expenseService;

    public FarmWorkerController(
            FarmWorkerRepository farmWorkerRepository,
            ExpenseService expenseService) {

        this.farmWorkerRepository = farmWorkerRepository;
        this.expenseService = expenseService;
    }

    @PostMapping
    public FarmWorker addFarmWorker(@RequestBody FarmWorker farmWorker) {
        return farmWorkerRepository.save(farmWorker);
    }

    @GetMapping
    public List<FarmWorker> getAllFarmWorkers() {
        return farmWorkerRepository.findAll();
    }

    @GetMapping("/count")
    public long getTotalFarmWorkers() {
        return farmWorkerRepository.count();
    }

    @GetMapping("/{id}")
    public FarmWorker getFarmWorkerById(@PathVariable Long id) {
        return farmWorkerRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public FarmWorker updateFarmWorker(
            @PathVariable Long id,
            @RequestBody FarmWorker farmWorker) {

        FarmWorker existingWorker =
                farmWorkerRepository.findById(id).orElse(null);

        if (existingWorker == null) {
            return null;
        }

        existingWorker.setName(farmWorker.getName());
        existingWorker.setPhoneNumber(farmWorker.getPhoneNumber());
        existingWorker.setAddress(farmWorker.getAddress());
        existingWorker.setJoiningDate(farmWorker.getJoiningDate());
        existingWorker.setSalary(farmWorker.getSalary());
        existingWorker.setStatus(farmWorker.getStatus());

        return farmWorkerRepository.save(existingWorker);
    }

    @DeleteMapping("/{id}")
    public String deleteFarmWorker(@PathVariable Long id) {

        if (!farmWorkerRepository.existsById(id)) {
            return "Farm worker not found";
        }

        expenseService.deleteWorkerSalaryExpense(id);

        farmWorkerRepository.deleteById(id);

        return "Farm worker deleted successfully";
    }
}