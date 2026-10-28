package com.sitrismart.knowledgesearch.article.dto;

public record ArticleRequest(String title, String content, String author, Long categoryId) {
}
