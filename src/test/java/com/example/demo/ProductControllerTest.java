package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = {ProductController.class, ProductControllerTest.TestConfig.class})
class ProductControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ProductRepository repository;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                Mockito.reset(repository);
        }

        @Test
        void getAllProducts_ShouldReturnProductList() throws Exception {
                // Arrange
                List<Product> products = Arrays.asList(
                        new Product(1L, "Product1", 100.0),
                        new Product(2L, "Product2", 200.0)
                );
                Mockito.when(repository.findAll()).thenReturn(products);

                // Act & Assert
                mockMvc.perform(get("/products"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(2))
                        .andExpect(jsonPath("$[0].name").value("Product1"))
                        .andExpect(jsonPath("$[1].name").value("Product2"));
        }

        @Test
        void createProduct_ShouldReturnSavedProduct() throws Exception {
                // Arrange
                Product product = new Product(1L, "NewProduct", 150.0);
                Mockito.when(repository.save(any(Product.class))).thenReturn(product);

                // Act & Assert
                mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.name").value("NewProduct"))
                        .andExpect(jsonPath("$.price").value(150.0));
        }

        @Configuration
        static class TestConfig {
                @Bean
                public ProductRepository productRepository() {
                        return Mockito.mock(ProductRepository.class);
                }
        }
}
