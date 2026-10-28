package com.sitrismart.knowledgesearch.article;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.sitrismart.knowledgesearch.category.Category;
import com.sitrismart.knowledgesearch.category.CategoryRepository;

@DataJpaTest
class ArticleRepositoryTest {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void savesArticleWithCategoryAndTimestamps() {
		Category java = categoryRepository.save(new Category("Java", null));

		Article saved = articleRepository.saveAndFlush(
				new Article("HashMap internals", "A HashMap stores entries in buckets...", "Nam", java));

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getCategory().getId()).isEqualTo(java.getId());
		assertThat(saved.getCreatedAt()).isNotNull();
		assertThat(saved.getUpdatedAt()).isNotNull();
	}

	@Test
	void existsByCategoryId() {
		Category java = categoryRepository.save(new Category("Java", null));
		Category sql = categoryRepository.save(new Category("SQL", null));
		articleRepository.save(new Article("HashMap internals", "Buckets...", "Nam", java));

		assertThat(articleRepository.existsByCategoryId(java.getId())).isTrue();
		assertThat(articleRepository.existsByCategoryId(sql.getId())).isFalse();
	}
}
