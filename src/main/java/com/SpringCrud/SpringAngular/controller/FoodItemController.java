package com.SpringCrud.SpringAngular.controller;

import com.SpringCrud.SpringAngular.model.FoodItem;
import com.SpringCrud.SpringAngular.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// ✅ Remove the "*" version
@CrossOrigin(origins = "https://strong-praline-d86c79.netlify.app")
@RestController
@RequestMapping("/api/food_item")
public class FoodItemController {

    @Autowired
    private FoodItemRepository repository;

    @GetMapping
    public List<FoodItem> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public FoodItem addItem(@RequestBody FoodItem item) {
        return repository.save(item);
    }

    @PutMapping("/{id}")
    public FoodItem updateItem(@PathVariable Long id, @RequestBody FoodItem updatedItem) {
        Optional<FoodItem> optionalItem = repository.findById(id);
        if (optionalItem.isPresent()) {
            FoodItem item = optionalItem.get();
            item.setName(updatedItem.getName());
            item.setDescription(updatedItem.getDescription());
            item.setPrice(updatedItem.getPrice());
            item.setImageUrl(updatedItem.getImageUrl());
            item.setCategory(updatedItem.getCategory());
            return repository.save(item);
        } else {
            throw new RuntimeException("Item not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
