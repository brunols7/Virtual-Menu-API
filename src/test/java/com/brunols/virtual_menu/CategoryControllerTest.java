package com.brunols.virtual_menu;

import com.brunols.virtual_menu.controller.CategoryController;
import com.brunols.virtual_menu.dto.CategoryDTO;
import com.brunols.virtual_menu.entity.Categories;
import com.brunols.virtual_menu.service.CategoriesService;
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

@WebMvcTest(controllers = CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriesService categoriesService;

    @TestConfiguration
    static class TestConfig {
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

    private CategoryDTO categoryDTO;
    private Categories category;

    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO("Drinks");

        category = new Categories(categoryDTO);
        category.setId(1L);
    }

    @Test
    @DisplayName("Should create a new category successfully")
    void createCategory_withValidData_shouldReturnCreated() throws Exception {
        when(categoriesService.saveCategory(any(Categories.class))).thenReturn(category);

        mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Category created with success!"));
    }

    @Test
    @DisplayName("Should return a list of all categories")
    void getAllCategories_shouldReturnListOfCategories() throws Exception {
        when(categoriesService.getAllCategories()).thenReturn(List.of(category));

        mockMvc.perform(get("/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(category.getId()))
                .andExpect(jsonPath("$[0].name").value(category.getName()));
    }

    @Test
    @DisplayName("Should return a category by its ID")
    void getCategoryById_withValidId_shouldReturnCategory() throws Exception {
        when(categoriesService.getCategoryById(anyLong())).thenReturn(category);

        mockMvc.perform(get("/v1/categories/{id}", category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value(category.getName()));
    }

    @Test
    @DisplayName("Should update a category successfully")
    void updateCategory_withValidData_shouldReturnSuccessMessage() throws Exception {
        when(categoriesService.updateCategory(anyLong(), any(Categories.class))).thenReturn(category);

        mockMvc.perform(patch("/v1/categories/{id}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Category updated with success!"));
    }

    @Test
    @DisplayName("Should delete a category successfully")
    void deleteCategory_withValidId_shouldReturnNoContent() throws Exception {
        when(categoriesService.deleteCategory(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/v1/categories/{id}", category.getId()))
                .andExpect(status().isNoContent());

        verify(categoriesService, times(1)).deleteCategory(category.getId());
    }
}