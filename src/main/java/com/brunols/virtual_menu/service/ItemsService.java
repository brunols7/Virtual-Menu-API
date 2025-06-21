package com.brunols.virtual_menu.service;

import com.brunols.virtual_menu.entity.Items;
import com.brunols.virtual_menu.repository.ItemsRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ItemsService {

    private final ItemsRepository repository;

    public ItemsService(ItemsRepository repository){
        this.repository = repository;
    }

    public Items saveItem(Items item){
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (!StringUtils.hasText(item.getName())) {
            throw new IllegalArgumentException("Item name must not be empty");
        }
        boolean exists = repository.existsByNameIgnoreCase(item.getName());
        if (exists) {
            throw new IllegalArgumentException("Item with name '" + item.getName() + "' already exists");
        }

        return repository.save(item);
    }

    public Items updateItem(Long id, Items updatedItem) {
        Items existingItem = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + id));

        if (!StringUtils.hasText(updatedItem.getName())) {
            throw new IllegalArgumentException("Item name must not be empty");
        }

        boolean exists = repository.existsByNameIgnoreCase(updatedItem.getName());
        if (exists && !existingItem.getName().equalsIgnoreCase(updatedItem.getName())) {
            throw new IllegalArgumentException("Item with name '" + updatedItem.getName() + "' already exists");
        }

        existingItem.setName(updatedItem.getName());
        return repository.save(existingItem);
    }

    public String deleteItem(Long id){
        Items existingItem = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id : " + id));

        String itemName = existingItem.getName();
        repository.delete(existingItem);

        return itemName;
    }

    public Items getItemById(Long id){

        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + id));
    }

}
