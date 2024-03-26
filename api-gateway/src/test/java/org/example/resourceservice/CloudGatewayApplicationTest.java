package org.example.resourceservice;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CloudGatewayApplication.class)
@ActiveProfiles("test")
public class CloudGatewayApplicationTest {

	@Test
	public void testContextLoads() throws Exception {
	}

}
