package com.sitrismart.knowledgesearch.category.dto;

import com.sitrismart.knowledgesearch.category.Category;

public record CategoryResponse(Long id, String name, String description) {

	public static CategoryResponse from(Category category) {
		return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
	}
}
