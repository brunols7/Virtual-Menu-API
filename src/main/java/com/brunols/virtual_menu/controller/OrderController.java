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

@Tag(name = "Orders")
@RestController
@RequestMapping("/v1/orders")
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

    @Operation(summary = "Create a new order")
    @PostMapping()
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderDTO dto) {
        Orders order = new Orders(dto);
        ordersService.saveOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order created with success!");
    }

    @Operation(summary = "Update order status to 'PAID' by ID")
    @PatchMapping("/{id}/pay")
    public ResponseEntity<String> payOrder(@PathVariable Long id){
        ordersService.payOrder(id);
        return ResponseEntity.ok("Order paid with success!");
    }

    @Operation(summary = "Update order status to 'CANCELLED' by ID")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id){
        ordersService.cancelOrder(id);
        return ResponseEntity.ok("Order cancelled with success!");
    }

    @Operation(summary = "Get order details by ID")
    @GetMapping("/{id}/details")
    public ResponseEntity<Orders> getOrderDetails(@PathVariable Long id) {
        Orders order = ordersService.findOrderById(id);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Update order by ID")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO dto) {
        Orders order = new Orders(dto);
        ordersService.updateOrder(id, order);
        return ResponseEntity.ok("Order updated with success!");
    }

    @Operation(summary = "Get order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<List<OrderItemsResponseDTO>> getOrderItems(@PathVariable Long id){
        List<OrderItems> items = orderItemsService.getAllItems(id);

        List<OrderItemsResponseDTO> dtoList = items.stream()
                .map(OrderItemsResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "Get all orders")
    @GetMapping()
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @Operation(summary = "Get all orders where status is 'OPEN'")
    @GetMapping("open")
    public ResponseEntity<List<Orders>> getOpenOrders(){
        return ResponseEntity.ok(ordersService.getOpenOrders());
    }

    @Operation(summary = "Delete order by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        ordersService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
