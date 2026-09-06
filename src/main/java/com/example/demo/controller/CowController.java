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

import com.example.demo.entity.Cow;
import com.example.demo.repository.CowRepository;

@RestController
@RequestMapping("/api/cows")
public class CowController {

    private final CowRepository cowRepository;

    public CowController(CowRepository cowRepository) {
        this.cowRepository = cowRepository;
    }

    @PostMapping
    public Cow addCow(@RequestBody Cow cow) {
        return cowRepository.save(cow);
    }

    @GetMapping
    public List<Cow> getAllCows() {
        return cowRepository.findAll();
    }

    @GetMapping("/count")
    public long getTotalCows() {
        return cowRepository.count();
    }

    @GetMapping("/{id}")
    public Cow getCowById(@PathVariable Long id) {
        return cowRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Cow updateCow(@PathVariable Long id, @RequestBody Cow cow) {

        Cow existingCow = cowRepository.findById(id).orElse(null);

        if (existingCow == null) {
            return null;
        }

        existingCow.setTagNumber(cow.getTagNumber());
        existingCow.setName(cow.getName());
        existingCow.setBreed(cow.getBreed());
        existingCow.setGender(cow.getGender());
        existingCow.setDateOfBirth(cow.getDateOfBirth());
        existingCow.setWeight(cow.getWeight());
        existingCow.setHealthStatus(cow.getHealthStatus());
        existingCow.setPurchaseDate(cow.getPurchaseDate());
        existingCow.setPurchasePrice(cow.getPurchasePrice());
        existingCow.setStatus(cow.getStatus());

        return cowRepository.save(existingCow);
    }

    @DeleteMapping("/{id}")
    public String deleteCow(@PathVariable Long id) {

        if (!cowRepository.existsById(id)) {
            return "Cow not found";
        }

        cowRepository.deleteById(id);

        return "Cow deleted successfully";
    }
}