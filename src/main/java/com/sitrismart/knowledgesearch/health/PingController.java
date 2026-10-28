package com.sitrismart.knowledgesearch.health;

import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

	@GetMapping("/api/ping")
	public Map<String, Object> ping() {
		return Map.of("status", "ok", "time", Instant.now().toString());
	}
}
