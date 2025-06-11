package com.SpringCrud.SpringAngular.controller;

import com.SpringCrud.SpringAngular.model.FoodItem;
import com.SpringCrud.SpringAngular.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*; // ✅ Needed for annotations like @RestController, @RequestMapping

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/food_item")
@CrossOrigin(origins = "*")
public class FoodItemController {

    @Autowired
    private FoodItemRepository repository;

    // ✅ GET
    @GetMapping
    public List<FoodItem> getAll() {
        return repository.findAll();
    }

    // ✅ POST
    @PostMapping
    public FoodItem addItem(@RequestBody FoodItem item) {
        return repository.save(item);
    }

    // ✅ PUT
    @PutMapping("/{id}")
    public FoodItem updateItem(@PathVariable Long id, @RequestBody FoodItem updatedItem) {
        Optional<FoodItem> optionalItem = repository.findById(id);
        if (optionalItem.isPresent()) {
            FoodItem item = optionalItem.get();
            item.setName(updatedItem.getName());
            item.setDescription(updatedItem.getDescription());
            item.setPrice(updatedItem.getPrice());
            item.setImageUrl(updatedItem.getImageUrl()); // ✅ Update image if present
            item.setCategory(updatedItem.getCategory());  // ✅ Update category too
            return repository.save(item);
        } else {
            throw new RuntimeException("Item not found with id: " + id);
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        repository.deleteById(id);
    }


}
