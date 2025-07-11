package com.brunols.virtual_menu;

import com.brunols.virtual_menu.controller.ItemController;
import com.brunols.virtual_menu.dto.ItemDTO;
import com.brunols.virtual_menu.entity.Categories;
import com.brunols.virtual_menu.entity.Items;
import com.brunols.virtual_menu.service.CategoriesService;
import com.brunols.virtual_menu.service.ItemsService;
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

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private CategoriesService categoriesService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ItemsService itemsService() {
            return Mockito.mock(ItemsService.class);
        }

        @Bean
        public CategoriesService categoriesService() {
            return Mockito.mock(CategoriesService.class);
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

    private ItemDTO itemDTO;
    private Items item;
    private Categories category;

    @BeforeEach
    void setUp() {
        category = new Categories();
        category.setId(1L);
        category.setName("Beverages");

        itemDTO = new ItemDTO("Coffee", "Hot coffee", 5.0, category.getId());

        item = new Items(itemDTO, category);
        item.setId(1L);
    }

    @Test
    @DisplayName("Should create a new item successfully")
    void createItem_withValidData_shouldReturnCreated() throws Exception {
        // FIX: Use when/thenReturn for methods with a return type.
        when(categoriesService.getCategoryById(anyLong())).thenReturn(category);
        when(itemsService.saveItem(any(Items.class))).thenReturn(item);

        mockMvc.perform(post("/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Item created with success!"));
    }

    @Test
    @DisplayName("Should return a list of all items")
    void getItems_shouldReturnListOfItems() throws Exception {
        when(itemsService.getAllItems()).thenReturn(List.of(item));

        mockMvc.perform(get("/v1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].name").value(item.getName()));
    }

    @Test
    @DisplayName("Should return an item by its ID")
    void getItemById_withValidId_shouldReturnItem() throws Exception {
        when(itemsService.getItemById(anyLong())).thenReturn(item);

        mockMvc.perform(get("/v1/items/{id}", item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()));
    }

    @Test
    @DisplayName("Should return items by category ID")
    void getItemsByCategory_withValidCategoryId_shouldReturnItemList() throws Exception {
        when(itemsService.getItemsByCategoryId(anyLong())).thenReturn(List.of(item));

        mockMvc.perform(get("/v1/items/category/{id}/", category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].category.id").value(category.getId()));
    }

    @Test
    @DisplayName("Should update an item successfully")
    void updateItem_withValidData_shouldReturnSuccessMessage() throws Exception {
        when(categoriesService.getCategoryById(anyLong())).thenReturn(category);
        when(itemsService.updateItem(anyLong(), any(Items.class))).thenReturn(item);

        mockMvc.perform(patch("/v1/items/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Item updated with success!"));
    }

    @Test
    @DisplayName("Should delete an item successfully")
    void deleteItem_withValidId_shouldReturnNoContent() throws Exception {
        when(itemsService.deleteItem(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/v1/items/{id}", item.getId()))
                .andExpect(status().isNoContent());

        verify(itemsService, times(1)).deleteItem(item.getId());
    }
}