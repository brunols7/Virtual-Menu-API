package com.brunols.virtual_menu;

import com.brunols.virtual_menu.controller.OrderItemsController;
import com.brunols.virtual_menu.dto.OrderItemsDTO;
import com.brunols.virtual_menu.entity.Items;
import com.brunols.virtual_menu.entity.OrderItems;
import com.brunols.virtual_menu.entity.Orders;
import com.brunols.virtual_menu.service.ItemsService;
import com.brunols.virtual_menu.service.OrderItemsService;
import com.brunols.virtual_menu.service.OrdersService;
import com.brunols.virtual_menu.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderItemsController.class)
public class OrderItemsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderItemsService orderItemsService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ItemsService itemsService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public OrderItemsService orderItemsService() {
            return Mockito.mock(OrderItemsService.class);
        }

        @Bean
        public OrdersService ordersService() {
            return Mockito.mock(OrdersService.class);
        }

        @Bean
        public ItemsService itemsService() {
            return Mockito.mock(ItemsService.class);
        }

        @Bean
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return Mockito.mock(UserDetailsService.class);
        }

        @Bean
        public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    private Orders order;
    private Items item;
    private OrderItems orderItem;
    private OrderItemsDTO orderItemsDTO;

    @BeforeEach
    void setUp() {
        order = new Orders();
        order.setId(1L);

        item = new Items();
        item.setId(10L);
        item.setName("Pizza");
        item.setPrice(35.0);

        orderItemsDTO = new OrderItemsDTO(item.getId(), 2, "Observations");

        orderItem = new OrderItems(orderItemsDTO, order, item);
        orderItem.setId(100L);
    }

    @Test
    @DisplayName("Should get order items by order ID successfully")
    void getOrderItem_withValidOrderId_shouldReturnItemsList() throws Exception {
        when(ordersService.findOrderById(anyLong())).thenReturn(order);
        when(orderItemsService.getAllItems(anyLong())).thenReturn(List.of(orderItem));

        mockMvc.perform(get("/v1/order/items/{id}", order.getId()))
                .andExpect(status().isOk())
                // FIX: The DTO has 'itemId', not 'id'.
                .andExpect(jsonPath("$[0].itemId").value(item.getId()))
                .andExpect(jsonPath("$[0].itemName").value(item.getName()));
    }

    @Test
    @DisplayName("Should add a new item to an order successfully")
    void addItems_withValidData_shouldReturnCreated() throws Exception {
        when(ordersService.findOrderById(anyLong())).thenReturn(order);
        when(itemsService.getItemById(anyLong())).thenReturn(item);
        when(orderItemsService.saveOrderItems(any(OrderItems.class))).thenReturn(orderItem);

        mockMvc.perform(post("/v1/order/items/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemsDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order item created with success!"));
    }

    @Test
    @DisplayName("Should update an order item successfully")
    void updateOrderItem_withValidData_shouldReturnOk() throws Exception {
        when(ordersService.findOrderById(anyLong())).thenReturn(order);
        when(itemsService.getItemById(anyLong())).thenReturn(item);
        when(orderItemsService.updateOrderItems(anyLong(), any(OrderItems.class))).thenReturn(orderItem);

        mockMvc.perform(put("/v1/order/items/{id}", orderItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemsDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Order item updated with success!"));
    }

    @Test
    @DisplayName("Should remove an item from an order successfully")
    void removeItemFromOrder_withValidId_shouldReturnNoContent() throws Exception {
        doNothing().when(orderItemsService).deleteOrderItem(anyLong());

        mockMvc.perform(delete("/v1/order/items/{id}", orderItem.getId()))
                .andExpect(status().isNoContent());

        verify(orderItemsService, times(1)).deleteOrderItem(orderItem.getId());
    }
}
