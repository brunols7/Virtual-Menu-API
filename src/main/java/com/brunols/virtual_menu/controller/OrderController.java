package com.brunols.virtual_menu.controller;

import com.brunols.virtual_menu.dto.OrderDTO;
import com.brunols.virtual_menu.dto.OrderItemsDTO;
import com.brunols.virtual_menu.dto.OrderItemsResponseDTO;
import com.brunols.virtual_menu.entity.Items;
import com.brunols.virtual_menu.entity.OrderItems;
import com.brunols.virtual_menu.entity.Orders;
import com.brunols.virtual_menu.service.ItemsService;
import com.brunols.virtual_menu.service.OrderItemsService;
import com.brunols.virtual_menu.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderItemsService orderItemsService;
    private final OrdersService ordersService;
    private final ItemsService itemsService;

    @Autowired
    public OrderController(OrderItemsService orderItemsService, OrdersService ordersService, ItemsService itemsService) {
        this.orderItemsService = orderItemsService;
        this.ordersService = ordersService;
        this.itemsService = itemsService;
    }

    @PostMapping()
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderDTO dto) {
        Orders order = new Orders(dto);
        ordersService.saveOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order created with success!");
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<String> addItems(@PathVariable Long id, @Valid @RequestBody OrderItemsDTO dto){
        Orders order = ordersService.findOrderById(id);
        Items item = itemsService.getItemById(dto.itemId());

        OrderItems orderItems = new OrderItems(dto, order, item);
        orderItemsService.saveOrderItems(orderItems);

        return ResponseEntity.status(HttpStatus.CREATED).body("Order item created with success!");
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<String> payOrder(@PathVariable Long id){
        ordersService.payOrder(id);
        return ResponseEntity.ok("Order paid with success!");
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id){
        ordersService.cancelOrder(id);
        return ResponseEntity.ok("Order cancelled with success!");
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<String> updateOrderItem(@PathVariable Long id, @Valid @RequestBody OrderItemsDTO dto){
        Orders order = ordersService.findOrderById(id);
        Items item = itemsService.getItemById(dto.itemId());

        OrderItems orderItems = new OrderItems(dto, order, item);
        orderItemsService.updateOrderItems(id, orderItems);
        return ResponseEntity.ok("Order item updated with success!");
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<Orders> getOrderDetails(@PathVariable Long id) {
        Orders order = ordersService.findOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO dto) {
        Orders order = new Orders(dto);
        ordersService.updateOrder(id, order);
        return ResponseEntity.ok("Order updated with success!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderItemsResponseDTO>> getOrderItems(@PathVariable Long id){
        List<OrderItems> items = orderItemsService.getAllItems(id);

        List<OrderItemsResponseDTO> dtoList = items.stream()
                .map(OrderItemsResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @GetMapping("open")
    public ResponseEntity<List<Orders>> getOpenOrders(){
        return ResponseEntity.ok(ordersService.getOpenOrders());
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<OrderItemsResponseDTO> getOrderItem(@PathVariable Long id){
        OrderItems item = orderItemsService.findItemsById(id);
        OrderItemsResponseDTO response = OrderItemsResponseDTO.fromEntity(item);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItemFromOrder(@PathVariable Long id){
        orderItemsService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        ordersService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
