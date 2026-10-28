package com.sitrismart.knowledgesearch.article;

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

import com.sitrismart.knowledgesearch.article.dto.ArticleRequest;
import com.sitrismart.knowledgesearch.article.dto.ArticleResponse;
import com.sitrismart.knowledgesearch.category.Category;
import com.sitrismart.knowledgesearch.category.CategoryRepository;
import com.sitrismart.knowledgesearch.common.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

	private static final ArticleRequest REQUEST =
			new ArticleRequest("HashMap internals", "A HashMap stores entries in buckets...", "Nam", 1L);

	@Mock
	private ArticleRepository articleRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private ArticleService service;

	@Test
	void createReturnsArticleWithCategoryName() {
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category("Java", null)));
		when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ArticleResponse response = service.create(REQUEST);

		assertThat(response.title()).isEqualTo("HashMap internals");
		assertThat(response.categoryName()).isEqualTo("Java");
	}

	@Test
	void createThrowsWhenCategoryMissing() {
		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.create(REQUEST))
				.isInstanceOf(ResourceNotFoundException.class);
		verify(articleRepository, never()).save(any());
	}

	@Test
	void findByIdThrowsWhenMissing() {
		when(articleRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.findById(99L))
				.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void updateThrowsWhenCategoryMissing() {
		Category java = new Category("Java", null);
		when(articleRepository.findById(7L)).thenReturn(Optional.of(new Article("Old", "Old", "Nam", java)));
		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.update(7L, REQUEST))
				.isInstanceOf(ResourceNotFoundException.class);
		verify(articleRepository, never()).saveAndFlush(any());
	}
}
