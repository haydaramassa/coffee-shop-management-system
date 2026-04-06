package com.cafe.backend.service;

import com.cafe.backend.dto.ProductRequest;
import com.cafe.backend.dto.ProductResponse;
import com.cafe.backend.entity.Category;
import com.cafe.backend.entity.Product;
import com.cafe.backend.exception.ResourceNotFoundException;
import com.cafe.backend.repository.CategoryRepository;
import com.cafe.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .imageUrl(request.getImageUrl())
                .active(request.getQuantity() > 0)
                .category(category)
                .build();

        Product savedProduct = productRepository.save(product);

        return ProductResponse.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .quantity(savedProduct.getQuantity())
                .imageUrl(savedProduct.getImageUrl())
                .active(savedProduct.getActive())
                .categoryId(savedProduct.getCategory().getId())
                .categoryName(savedProduct.getCategory().getName())
                .createdAt(savedProduct.getCreatedAt())
                .build();
    }
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .imageUrl(product.getImageUrl())
                        .active(product.getActive())
                        .categoryId(product.getCategory().getId())
                        .categoryName(product.getCategory().getName())
                        .createdAt(product.getCreatedAt())
                        .build())
                .toList();
    }
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .imageUrl(product.getImageUrl())
                .active(product.getActive())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .createdAt(product.getCreatedAt())
                .build();
    }
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
        product.setActive(request.getQuantity() > 0);

        Product updatedProduct = productRepository.save(product);

        return ProductResponse.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .price(updatedProduct.getPrice())
                .quantity(updatedProduct.getQuantity())
                .imageUrl(updatedProduct.getImageUrl())
                .active(updatedProduct.getActive())
                .categoryId(updatedProduct.getCategory().getId())
                .categoryName(updatedProduct.getCategory().getName())
                .createdAt(updatedProduct.getCreatedAt())
                .build();
    }
    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(false);
        productRepository.save(product);
    }
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId)
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .imageUrl(product.getImageUrl())
                        .active(product.getActive())
                        .categoryId(product.getCategory().getId())
                        .categoryName(product.getCategory().getName())
                        .createdAt(product.getCreatedAt())
                        .build())
                .toList();
    }
    public List<ProductResponse> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name)
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .imageUrl(product.getImageUrl())
                        .active(product.getActive())
                        .categoryId(product.getCategory().getId())
                        .categoryName(product.getCategory().getName())
                        .createdAt(product.getCreatedAt())
                        .build())
                .toList();
    }
}