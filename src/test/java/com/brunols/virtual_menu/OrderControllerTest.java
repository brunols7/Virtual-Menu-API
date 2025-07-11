package com.brunols.virtual_menu;

import com.brunols.virtual_menu.controller.OrderController;
import com.brunols.virtual_menu.dto.OrderDTO;
import com.brunols.virtual_menu.dto.OrderItemsResponseDTO;
import com.brunols.virtual_menu.entity.Items;
import com.brunols.virtual_menu.entity.OrderItems;
import com.brunols.virtual_menu.entity.Orders;
import com.brunols.virtual_menu.entity.Status;
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

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderItemsService orderItemsService;

    @Autowired
    private ItemsService itemsService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public OrdersService ordersService() {
            return Mockito.mock(OrdersService.class);
        }

        @Bean
        public OrderItemsService orderItemsService() {
            return Mockito.mock(OrderItemsService.class);
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

    private OrderDTO orderDTO;
    private Orders order;
    private OrderItems orderItem;

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO(1, "John Doe", "123.456.789-10");

        order = new Orders(orderDTO);
        order.setId(1L);
        order.setStatus(Status.OPEN);

        Items item = new Items();
        item.setId(10L);
        item.setName("Coffee");

        orderItem = new OrderItems();
        orderItem.setId(100L);
        orderItem.setOrder(order);
        orderItem.setItem(item);
        orderItem.setQuantity(2);
    }

    @Test
    @DisplayName("Should create a new order successfully")
    void createOrder_withValidData_shouldReturnCreated() throws Exception {
        doNothing().when(ordersService).saveOrder(any(Orders.class));

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order created with success!"));
    }

    @Test
    @DisplayName("Should pay an order successfully")
    void payOrder_withValidId_shouldReturnOk() throws Exception {
        doNothing().when(ordersService).payOrder(anyLong());

        mockMvc.perform(patch("/v1/orders/{id}/pay", order.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Order paid with success!"));

        verify(ordersService, times(1)).payOrder(order.getId());
    }

    @Test
    @DisplayName("Should cancel an order successfully")
    void cancelOrder_withValidId_shouldReturnOk() throws Exception {
        doNothing().when(ordersService).cancelOrder(anyLong());

        mockMvc.perform(patch("/v1/orders/{id}/cancel", order.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Order cancelled with success!"));

        verify(ordersService, times(1)).cancelOrder(order.getId());
    }

    @Test
    @DisplayName("Should get order details by ID")
    void getOrderDetails_withValidId_shouldReturnOrder() throws Exception {
        when(ordersService.findOrderById(anyLong())).thenReturn(order);

        mockMvc.perform(get("/v1/orders/{id}/details", order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.clientName").value(order.getClientName()));
    }

    @Test
    @DisplayName("Should update an order successfully")
    void updateOrder_withValidData_shouldReturnOk() throws Exception {
        when(ordersService.updateOrder(anyLong(), any(Orders.class))).thenReturn(order);

        mockMvc.perform(put("/v1/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Order updated with success!"));
    }

    @Test
    @DisplayName("Should get all orders")
    void getAllOrders_shouldReturnOrdersList() throws Exception {
        when(ordersService.getAllOrders()).thenReturn(List.of(order));

        mockMvc.perform(get("/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order.getId()));
    }

    @Test
    @DisplayName("Should get all open orders")
    void getOpenOrders_shouldReturnOpenOrdersList() throws Exception {
        when(ordersService.getOpenOrders()).thenReturn(List.of(order));

        mockMvc.perform(get("/v1/orders/open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[0].status").value(Status.OPEN.toString()));
    }

    @Test
    @DisplayName("Should delete an order successfully")
    void deleteOrder_withValidId_shouldReturnNoContent() throws Exception {
        doNothing().when(ordersService).deleteOrder(anyLong());

        mockMvc.perform(delete("/v1/orders/{id}", order.getId()))
                .andExpect(status().isNoContent());

        verify(ordersService, times(1)).deleteOrder(order.getId());
    }
}