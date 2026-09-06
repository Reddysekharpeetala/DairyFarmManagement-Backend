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

import com.example.demo.entity.Feed;
import com.example.demo.repository.FeedRepository;
import com.example.demo.service.ExpenseService;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedRepository feedRepository;
    private final ExpenseService expenseService;

    public FeedController(
            FeedRepository feedRepository,
            ExpenseService expenseService) {

        this.feedRepository = feedRepository;
        this.expenseService = expenseService;
    }

    @PostMapping
    public Feed addFeed(@RequestBody Feed feed) {

        Feed savedFeed = feedRepository.save(feed);

        expenseService.createFeedExpense(savedFeed);

        return savedFeed;
    }

    @GetMapping
    public List<Feed> getAllFeeds() {
        return feedRepository.findAll();
    }

    @GetMapping("/count")
    public long getTotalFeeds() {
        return feedRepository.count();
    }

    @GetMapping("/{id}")
    public Feed getFeedById(@PathVariable Long id) {
        return feedRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Feed updateFeed(
            @PathVariable Long id,
            @RequestBody Feed feed) {

        Feed existingFeed =
                feedRepository.findById(id).orElse(null);

        if (existingFeed == null) {
            return null;
        }

        existingFeed.setFeedName(feed.getFeedName());
        existingFeed.setFeedType(feed.getFeedType());
        existingFeed.setQuantity(feed.getQuantity());
        existingFeed.setUnit(feed.getUnit());
        existingFeed.setFeedDate(feed.getFeedDate());
        existingFeed.setCost(feed.getCost());
        existingFeed.setNotes(feed.getNotes());

        Feed savedFeed = feedRepository.save(existingFeed);

        expenseService.createFeedExpense(savedFeed);

        return savedFeed;
    }

    @DeleteMapping("/{id}")
    public String deleteFeed(@PathVariable Long id) {

        if (!feedRepository.existsById(id)) {
            return "Feed record not found";
        }

        expenseService.deleteFeedExpense(id);

        feedRepository.deleteById(id);

        return "Feed record deleted successfully";
    }
}