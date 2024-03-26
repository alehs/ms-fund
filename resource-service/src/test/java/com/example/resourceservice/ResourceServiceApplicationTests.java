package com.example.resourceservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = SpringTestApplication.class)
@ActiveProfiles("test")
class ResourceServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@MockBean
	private JwtDecoder jwtDecoder;

}
