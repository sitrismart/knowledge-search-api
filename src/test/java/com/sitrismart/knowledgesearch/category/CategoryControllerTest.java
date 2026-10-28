package com.sitrismart.knowledgesearch.category;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sitrismart.knowledgesearch.category.dto.CategoryRequest;
import com.sitrismart.knowledgesearch.category.dto.CategoryResponse;
import com.sitrismart.knowledgesearch.common.ConflictException;
import com.sitrismart.knowledgesearch.common.ResourceNotFoundException;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

	private static final String JAVA_JSON = """
			{"name":"Java","description":"Core Java notes"}
			""";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoryService service;

	@Test
	void createReturns201() throws Exception {
		when(service.create(new CategoryRequest("Java", "Core Java notes")))
				.thenReturn(new CategoryResponse(1L, "Java", "Core Java notes"));

		mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON).content(JAVA_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Java"));
	}

	@Test
	void createReturns409WhenNameExists() throws Exception {
		when(service.create(new CategoryRequest("Java", "Core Java notes")))
				.thenThrow(new ConflictException("Category name already exists: Java"));

		mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON).content(JAVA_JSON))
				.andExpect(status().isConflict());
	}

	@Test
	void findAllReturns200() throws Exception {
		when(service.findAll()).thenReturn(List.of(new CategoryResponse(1L, "Java", null)));

		mockMvc.perform(get("/api/categories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Java"));
	}

	@Test
	void findByIdReturns404WhenMissing() throws Exception {
		when(service.findById(99L)).thenThrow(new ResourceNotFoundException("Category not found: 99"));

		mockMvc.perform(get("/api/categories/99"))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateReturns200() throws Exception {
		when(service.update(1L, new CategoryRequest("Java", "Core Java notes")))
				.thenReturn(new CategoryResponse(1L, "Java", "Core Java notes"));

		mockMvc.perform(put("/api/categories/1").contentType(MediaType.APPLICATION_JSON).content(JAVA_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.description").value("Core Java notes"));
	}

	@Test
	void deleteReturns204() throws Exception {
		mockMvc.perform(delete("/api/categories/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteReturns404WhenMissing() throws Exception {
		doThrow(new ResourceNotFoundException("Category not found: 99")).when(service).delete(99L);

		mockMvc.perform(delete("/api/categories/99"))
				.andExpect(status().isNotFound());
	}
}
