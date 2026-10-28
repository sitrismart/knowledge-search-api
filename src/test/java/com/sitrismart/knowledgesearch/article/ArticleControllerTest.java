package com.sitrismart.knowledgesearch.article;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sitrismart.knowledgesearch.article.dto.ArticleRequest;
import com.sitrismart.knowledgesearch.article.dto.ArticleResponse;
import com.sitrismart.knowledgesearch.common.ResourceNotFoundException;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

	private static final String ARTICLE_JSON = """
			{"title":"HashMap internals","content":"A HashMap stores entries in buckets...","author":"Nam","categoryId":1}
			""";

	private static final ArticleRequest REQUEST =
			new ArticleRequest("HashMap internals", "A HashMap stores entries in buckets...", "Nam", 1L);

	private static final Instant NOW = Instant.parse("2026-10-28T11:30:00Z");

	private static final ArticleResponse RESPONSE = new ArticleResponse(
			7L, "HashMap internals", "A HashMap stores entries in buckets...", "Nam", 1L, "Java", NOW, NOW);

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ArticleService service;

	@Test
	void createReturns201WithCategoryName() throws Exception {
		when(service.create(REQUEST)).thenReturn(RESPONSE);

		mockMvc.perform(post("/api/articles").contentType(MediaType.APPLICATION_JSON).content(ARTICLE_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(7))
				.andExpect(jsonPath("$.categoryName").value("Java"))
				.andExpect(jsonPath("$.createdAt").value("2026-10-28T11:30:00Z"));
	}

	@Test
	void createReturns404WhenCategoryMissing() throws Exception {
		when(service.create(REQUEST)).thenThrow(new ResourceNotFoundException("Category not found: 1"));

		mockMvc.perform(post("/api/articles").contentType(MediaType.APPLICATION_JSON).content(ARTICLE_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void findAllReturns200() throws Exception {
		when(service.findAll()).thenReturn(List.of(RESPONSE));

		mockMvc.perform(get("/api/articles"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("HashMap internals"));
	}

	@Test
	void findByIdReturns404WhenMissing() throws Exception {
		when(service.findById(99L)).thenThrow(new ResourceNotFoundException("Article not found: 99"));

		mockMvc.perform(get("/api/articles/99"))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateReturns200() throws Exception {
		when(service.update(7L, REQUEST)).thenReturn(RESPONSE);

		mockMvc.perform(put("/api/articles/7").contentType(MediaType.APPLICATION_JSON).content(ARTICLE_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.categoryName").value("Java"));
	}

	@Test
	void deleteReturns204() throws Exception {
		mockMvc.perform(delete("/api/articles/7"))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteReturns404WhenMissing() throws Exception {
		doThrow(new ResourceNotFoundException("Article not found: 99")).when(service).delete(99L);

		mockMvc.perform(delete("/api/articles/99"))
				.andExpect(status().isNotFound());
	}
}
