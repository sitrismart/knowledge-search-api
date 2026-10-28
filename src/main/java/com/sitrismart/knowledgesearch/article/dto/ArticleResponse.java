package com.sitrismart.knowledgesearch.article.dto;

import java.time.Instant;

import com.sitrismart.knowledgesearch.article.Article;

public record ArticleResponse(
		Long id,
		String title,
		String content,
		String author,
		Long categoryId,
		String categoryName,
		Instant createdAt,
		Instant updatedAt) {

	public static ArticleResponse from(Article article) {
		return new ArticleResponse(
				article.getId(),
				article.getTitle(),
				article.getContent(),
				article.getAuthor(),
				article.getCategory().getId(),
				article.getCategory().getName(),
				article.getCreatedAt(),
				article.getUpdatedAt());
	}
}
