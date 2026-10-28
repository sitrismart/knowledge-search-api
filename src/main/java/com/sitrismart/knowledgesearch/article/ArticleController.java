package com.sitrismart.knowledgesearch.article;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sitrismart.knowledgesearch.article.dto.ArticleRequest;
import com.sitrismart.knowledgesearch.article.dto.ArticleResponse;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

	private final ArticleService service;

	public ArticleController(ArticleService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ArticleResponse create(@RequestBody ArticleRequest request) {
		return service.create(request);
	}

	@GetMapping
	public List<ArticleResponse> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public ArticleResponse findById(@PathVariable Long id) {
		return service.findById(id);
	}

	@PutMapping("/{id}")
	public ArticleResponse update(@PathVariable Long id, @RequestBody ArticleRequest request) {
		return service.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
