package com.sitrismart.knowledgesearch.article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	boolean existsByCategoryId(Long categoryId);
}
