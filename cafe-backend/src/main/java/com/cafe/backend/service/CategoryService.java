package com.cafe.backend.service;

import com.cafe.backend.dto.CategoryRequest;
import com.cafe.backend.dto.CategoryResponse;
import com.cafe.backend.entity.Category;
import com.cafe.backend.exception.DuplicateResourceException;
import com.cafe.backend.exception.ResourceNotFoundException;
import com.cafe.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest request) {
        String name = request.getName() != null ? request.getName().trim() : null;
        String description = request.getDescription() != null ? request.getDescription().trim() : "";

        if (name == null || name.isBlank()) {
            throw new RuntimeException("Category name is required");
        }

        if (categoryRepository.existsByName(name)) {
            throw new DuplicateResourceException("Category name already exists");
        }

        Category category = Category.builder()
                .name(name)
                .description(description)
                .active(true)
                .build();

        Category savedCategory = categoryRepository.save(category);

        return mapToResponse(savedCategory);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        String name = request.getName() != null ? request.getName().trim() : null;
        String description = request.getDescription() != null ? request.getDescription().trim() : "";

        if (name == null || name.isBlank()) {
            throw new RuntimeException("Category name is required");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (categoryRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException("Category name already exists");
        }

        category.setName(name);
        category.setDescription(description);

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponse(updatedCategory);
    }

    public void deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setActive(false);
        categoryRepository.save(category);
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .createdAt(category.getCreatedAt())
                .build();
    }
}