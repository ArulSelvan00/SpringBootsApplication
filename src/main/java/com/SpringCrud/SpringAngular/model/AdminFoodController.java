package com.SpringCrud.SpringAngular.controller;

import com.SpringCrud.SpringAngular.model.FoodItem;
import com.SpringCrud.SpringAngular.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/a/food_item")
@CrossOrigin(origins = "*")
public class AdminFoodController {

    @Autowired
    private FoodItemRepository repository;

    // ✅ GET all items
    @GetMapping
    public List<FoodItem> getAllItems() {
        return repository.findAll();
    }

    // ✅ POST - Add new food item
    @PostMapping
    public ResponseEntity<FoodItem> addItem(@RequestBody FoodItem item) {
        return ResponseEntity.ok(repository.save(item));
    }

    // ✅ PUT - Update food item
    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> updateItem(@PathVariable Long id, @RequestBody FoodItem updatedItem) {
        Optional<FoodItem> optionalItem = repository.findById(id);

        if (optionalItem.isPresent()) {
            FoodItem existingItem = optionalItem.get();
            existingItem.setName(updatedItem.getName());
            existingItem.setDescription(updatedItem.getDescription());
            existingItem.setPrice(updatedItem.getPrice());
            existingItem.setCategory(updatedItem.getCategory());
            existingItem.setImageUrl(updatedItem.getImageUrl());

            return ResponseEntity.ok(repository.save(existingItem));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ DELETE - Delete food item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
