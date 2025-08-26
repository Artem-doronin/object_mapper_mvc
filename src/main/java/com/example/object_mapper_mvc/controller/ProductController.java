package com.example.object_mapper_mvc.controller;

import com.example.object_mapper_mvc.model.Product;
import com.example.object_mapper_mvc.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;


    private final ObjectMapper objectMapper;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody String productJson) {
        try {
            Product product = objectMapper.readValue(productJson, Product.class);
            validateProduct(product);
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(objectMapper.writeValueAsString(createdProduct));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product data");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody String productJson) {
        try {
            Product product = objectMapper.readValue(productJson, Product.class);
            validateProduct(product);
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(objectMapper.writeValueAsString(updatedProduct));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product data");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private void validateProduct(Product product) {
        if (product.getName() != null && product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может состоять только из пробелов");
        }

        if (product.getPrice() == null) {
            throw new IllegalArgumentException("Поле не может быть пустым");
        }

        if (product.getQuantityInStock() != null && product.getQuantityInStock() > 1000000) {
            throw new IllegalArgumentException("Количество на складе не может превышать 1,000,000 единиц");
        }

        if (product.getDescription() != null && product.getDescription().length() > 500) {
            throw new IllegalArgumentException("Описание не должно превышать 500 символов");
        }

        if (product.getName() != null) {
            String lowerCaseName = product.getName().toLowerCase();
            if (lowerCaseName.contains("test") || lowerCaseName.contains("temp")) {
                throw new IllegalArgumentException("Название продукта содержит запрещенные слова");
            }
        }
    }
}