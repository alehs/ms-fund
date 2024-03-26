package com.example.resourceservice.contract;

import com.example.resourceservice.SpringTestApplication;
import com.example.resourceservice.resource.adapters.api.ResourcesController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@ActiveProfiles("test")
@AutoConfigureMessageVerifier
public class BaseContractTest {

    @Autowired
    private ResourcesController resourcesController;

    @Before
    public void setup() {
        System.out.println("Running JUnit set up");
        StandaloneMockMvcBuilder standaloneMockMvcBuilder
                = MockMvcBuilders.standaloneSetup(resourcesController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
    }
}