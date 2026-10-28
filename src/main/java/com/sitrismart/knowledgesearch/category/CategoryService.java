package com.sitrismart.knowledgesearch.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sitrismart.knowledgesearch.category.dto.CategoryRequest;
import com.sitrismart.knowledgesearch.category.dto.CategoryResponse;
import com.sitrismart.knowledgesearch.common.ConflictException;
import com.sitrismart.knowledgesearch.common.ResourceNotFoundException;

@Service
public class CategoryService {

	private final CategoryRepository repository;

	public CategoryService(CategoryRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<CategoryResponse> findAll() {
		return repository.findAll().stream().map(CategoryResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public CategoryResponse findById(Long id) {
		return CategoryResponse.from(getCategory(id));
	}

	@Transactional
	public CategoryResponse create(CategoryRequest request) {
		if (repository.existsByName(request.name())) {
			throw new ConflictException("Category name already exists: " + request.name());
		}
		Category saved = repository.save(new Category(request.name(), request.description()));
		return CategoryResponse.from(saved);
	}

	@Transactional
	public CategoryResponse update(Long id, CategoryRequest request) {
		Category category = getCategory(id);
		if (repository.existsByNameAndIdNot(request.name(), id)) {
			throw new ConflictException("Category name already exists: " + request.name());
		}
		category.setName(request.name());
		category.setDescription(request.description());
		return CategoryResponse.from(category);
	}

	@Transactional
	public void delete(Long id) {
		repository.delete(getCategory(id));
	}

	private Category getCategory(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
	}
}
