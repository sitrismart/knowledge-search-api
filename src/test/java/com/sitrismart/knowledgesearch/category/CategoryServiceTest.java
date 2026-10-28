package com.sitrismart.knowledgesearch.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sitrismart.knowledgesearch.article.ArticleRepository;
import com.sitrismart.knowledgesearch.category.dto.CategoryRequest;
import com.sitrismart.knowledgesearch.category.dto.CategoryResponse;
import com.sitrismart.knowledgesearch.common.ConflictException;
import com.sitrismart.knowledgesearch.common.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@Mock
	private CategoryRepository repository;

	@Mock
	private ArticleRepository articleRepository;

	@InjectMocks
	private CategoryService service;

	@Test
	void createReturnsSavedCategory() {
		when(repository.existsByName("Java")).thenReturn(false);
		when(repository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

		CategoryResponse response = service.create(new CategoryRequest("Java", "Core Java notes"));

		assertThat(response.name()).isEqualTo("Java");
		assertThat(response.description()).isEqualTo("Core Java notes");
	}

	@Test
	void createRejectsDuplicateName() {
		when(repository.existsByName("Java")).thenReturn(true);

		assertThatThrownBy(() -> service.create(new CategoryRequest("Java", null)))
				.isInstanceOf(ConflictException.class);
		verify(repository, never()).save(any());
	}

	@Test
	void findByIdThrowsWhenMissing() {
		when(repository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.findById(99L))
				.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void updateRejectsNameUsedByAnotherCategory() {
		when(repository.findById(1L)).thenReturn(Optional.of(new Category("Java", null)));
		when(repository.existsByNameAndIdNot("SQL", 1L)).thenReturn(true);

		assertThatThrownBy(() -> service.update(1L, new CategoryRequest("SQL", null)))
				.isInstanceOf(ConflictException.class);
	}

	@Test
	void deleteRejectsCategoryWithArticles() {
		Category java = new Category("Java", null);
		when(repository.findById(1L)).thenReturn(Optional.of(java));
		when(articleRepository.existsByCategoryId(1L)).thenReturn(true);

		assertThatThrownBy(() -> service.delete(1L))
				.isInstanceOf(ConflictException.class);
		verify(repository, never()).delete(java);
	}

	@Test
	void deleteThrowsWhenMissing() {
		when(repository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.delete(99L))
				.isInstanceOf(ResourceNotFoundException.class);
	}
}
