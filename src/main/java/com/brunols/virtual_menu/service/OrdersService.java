package com.brunols.virtual_menu.service;

import com.brunols.virtual_menu.entity.Orders;
import com.brunols.virtual_menu.entity.Status;
import com.brunols.virtual_menu.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    public final OrdersRepository repository;

    public OrdersService(OrdersRepository repository) {
        this.repository = repository;
    }

    public void saveOrder(Orders order){
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null");
        }

        boolean cpfExists = repository.existsByClientCpf(order.getClientCpf());
        if(cpfExists){
            Optional<Orders> openOrder = repository.findByClientCpfAndStatus(order.getClientCpf(), Status.OPEN);

            if (openOrder.isPresent()) {
                throw new IllegalStateException("There is already an open order for this CPF. Please close it before creating a new one.");
            }
        }

        repository.save(order);
    }

    public Orders updateOrder(Long id, Orders updatedOrder){
        Orders existingOrder = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        if(updatedOrder == null){
            throw new IllegalArgumentException("Order cannot be null");
        }

        return repository.save(updatedOrder);
    }

    public void deleteOrder(Long id){
        Orders existingOrder = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        repository.delete(existingOrder);
    }

    public void payOrder(Long id){
        Orders existingOrder = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        existingOrder.setStatus(Status.PAID);

        repository.save(existingOrder);
    }

    public void cancelOrder(Long id){
        Orders existingOrder = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        existingOrder.setStatus(Status.CANCELLED);
        repository.save(existingOrder);
    }

    public Orders findOrderById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    public List<Orders> getAllOrders() {
        return repository.findAll();
}

    public List<Orders> getOpenOrders() {
        return repository.findAllByStatus(Status.OPEN);
    }
}
