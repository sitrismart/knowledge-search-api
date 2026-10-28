package com.sitrismart.knowledgesearch.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repository;

	@Test
	void savesAndFindsCategory() {
		Category saved = repository.save(new Category("Java", "Core Java notes"));

		assertThat(saved.getId()).isNotNull();
		assertThat(repository.findById(saved.getId()))
				.hasValueSatisfying(found -> {
					assertThat(found.getName()).isEqualTo("Java");
					assertThat(found.getDescription()).isEqualTo("Core Java notes");
				});
	}

	@Test
	void existsByName() {
		repository.save(new Category("Java", null));

		assertThat(repository.existsByName("Java")).isTrue();
		assertThat(repository.existsByName("SQL")).isFalse();
	}

	@Test
	void existsByNameAndIdNot() {
		Category java = repository.save(new Category("Java", null));
		Category sql = repository.save(new Category("SQL", null));

		assertThat(repository.existsByNameAndIdNot("Java", java.getId())).isFalse();
		assertThat(repository.existsByNameAndIdNot("Java", sql.getId())).isTrue();
	}
}
