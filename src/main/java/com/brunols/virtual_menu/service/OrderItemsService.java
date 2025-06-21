package com.brunols.virtual_menu.service;

import com.brunols.virtual_menu.entity.OrderItems;
import com.brunols.virtual_menu.entity.Orders;
import com.brunols.virtual_menu.repository.OrderItemsRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemsService {
    private final OrderItemsRepository repository;

    public OrderItemsService(OrderItemsRepository repository) {
        this.repository = repository;
    }

    public OrderItems saveOrderItems(OrderItems orderItem){
        if(orderItem == null){
            throw new IllegalArgumentException("Order item cannot be null");
        }

        return repository.save(orderItem);
    }

    public OrderItems updateOrderItems(Long id, OrderItems updatedOrderItem){
        OrderItems existingOrder = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order item not found with id: " + id));

        if(updatedOrderItem == null){
            throw new IllegalArgumentException("Order item cannot be null");
        }

        return repository.save(updatedOrderItem);
    }

    public void deleteOrder(Long id){
        OrderItems existingOrder = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order item not found with id: " + id));

        repository.delete(existingOrder);
    }

    public OrderItems findOrderById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order item not found with id: " + id));
    }
}
