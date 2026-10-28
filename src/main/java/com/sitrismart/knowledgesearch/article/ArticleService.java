package com.sitrismart.knowledgesearch.article;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sitrismart.knowledgesearch.article.dto.ArticleRequest;
import com.sitrismart.knowledgesearch.article.dto.ArticleResponse;
import com.sitrismart.knowledgesearch.category.Category;
import com.sitrismart.knowledgesearch.category.CategoryRepository;
import com.sitrismart.knowledgesearch.common.ResourceNotFoundException;

@Service
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final CategoryRepository categoryRepository;

	public ArticleService(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
		this.articleRepository = articleRepository;
		this.categoryRepository = categoryRepository;
	}

	@Transactional(readOnly = true)
	public List<ArticleResponse> findAll() {
		return articleRepository.findAll().stream().map(ArticleResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public ArticleResponse findById(Long id) {
		return ArticleResponse.from(getArticle(id));
	}

	@Transactional
	public ArticleResponse create(ArticleRequest request) {
		Category category = getCategory(request.categoryId());
		Article saved = articleRepository.save(
				new Article(request.title(), request.content(), request.author(), category));
		return ArticleResponse.from(saved);
	}

	@Transactional
	public ArticleResponse update(Long id, ArticleRequest request) {
		Article article = getArticle(id);
		article.setTitle(request.title());
		article.setContent(request.content());
		article.setAuthor(request.author());
		article.setCategory(getCategory(request.categoryId()));
		// flush so @UpdateTimestamp sets updatedAt before the response is built
		return ArticleResponse.from(articleRepository.saveAndFlush(article));
	}

	@Transactional
	public void delete(Long id) {
		articleRepository.delete(getArticle(id));
	}

	private Article getArticle(Long id) {
		return articleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Article not found: " + id));
	}

	private Category getCategory(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
	}
}
