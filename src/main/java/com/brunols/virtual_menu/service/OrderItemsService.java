package com.brunols.virtual_menu.service;

import com.brunols.virtual_menu.entity.OrderItems;
import com.brunols.virtual_menu.entity.Orders;
import com.brunols.virtual_menu.exceptions.NoItemsFoundException;
import com.brunols.virtual_menu.repository.OrderItemsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void deleteOrderItem(Long id){
        OrderItems existingOrder = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order item not found with id: " + id));

        repository.delete(existingOrder);
    }

    public OrderItems findItemsById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order item not found with id: " + id));
    }

    public List<OrderItems> getAllItems(Long orderId){
        List<OrderItems> items = repository.findByOrderId(orderId);

        if(items.isEmpty()){
            throw new NoItemsFoundException("No items found for order id: " + orderId);
        }

        return items;
    }

}
