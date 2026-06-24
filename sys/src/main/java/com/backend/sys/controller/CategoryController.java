package com.backend.sys.controller;

import com.backend.sys.dto.request.CategoryRequest;
import com.backend.sys.dto.response.CategoryResponse;
import com.backend.sys.entity.Category;
import com.backend.sys.exception.ResourceNotFoundException;
import com.backend.sys.repository.CategoryRepository;
import com.backend.sys.service.ResponseMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final ResponseMapper mapper;

    public CategoryController(CategoryRepository categoryRepository, ResponseMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @GetMapping
    @Cacheable(value = "categories")
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(mapper::toCategory)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        if (request.active() != null) {
            category.setActive(request.active());
        }
        return mapper.toCategory(categoryRepository.save(category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(request.name());
        category.setDescription(request.description());
        if (request.active() != null) {
            category.setActive(request.active());
        }
        return mapper.toCategory(categoryRepository.save(category));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "categories", allEntries = true)
    public void deactivateCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setActive(false);
        categoryRepository.save(category);
    }
}
