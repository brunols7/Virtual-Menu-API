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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Order Items")
@RestController
@RequestMapping("/v1/order/items")
public class OrderItemsController {

    private final OrderItemsService orderItemsService;
    private final OrdersService ordersService;
    private final ItemsService itemsService;

    @Autowired
    public OrderItemsController(OrderItemsService orderItemsService, OrdersService ordersService, ItemsService itemsService) {
        this.orderItemsService = orderItemsService;
        this.ordersService = ordersService;
        this.itemsService = itemsService;
    }

    @Operation(summary = "Get order items by order ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemsResponseDTO> getOrderItem(@PathVariable Long id){
        OrderItems item = orderItemsService.findItemsById(id);
        OrderItemsResponseDTO response = OrderItemsResponseDTO.fromEntity(item);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new order item by order ID")
    @PostMapping("/{id}")
    public ResponseEntity<String> addItems(@PathVariable Long id, @Valid @RequestBody OrderItemsDTO dto){
        Orders order = ordersService.findOrderById(id);
        Items item = itemsService.getItemById(dto.itemId());

        OrderItems orderItems = new OrderItems(dto, order, item);
        orderItemsService.saveOrderItems(orderItems);

        return ResponseEntity.status(HttpStatus.CREATED).body("Order item created with success!");
    }

    @Operation(summary = "Update order item by order ID")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrderItem(@PathVariable Long id, @Valid @RequestBody OrderItemsDTO dto){
        Orders order = ordersService.findOrderById(id);
        Items item = itemsService.getItemById(dto.itemId());

        OrderItems orderItems = new OrderItems(dto, order, item);
        orderItemsService.updateOrderItems(id, orderItems);
        return ResponseEntity.ok("Order item updated with success!");
    }

    @Operation(summary = "Delete order item by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeItemFromOrder(@PathVariable Long id){
        orderItemsService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

}
