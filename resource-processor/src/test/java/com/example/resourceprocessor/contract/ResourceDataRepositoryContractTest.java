package com.example.resourceprocessor.contract;

import com.example.resourceprocessor.resource.domain.ports.ResourceDataRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.example:resource-service:+:stubs:8090")
public class ResourceDataRepositoryContractTest {

    static {
        System.setProperty("eureka.client.enabled", "false");
    }
    @Autowired
    private ResourceDataRepository resourceDataRepository;

    @Test
    public void given_whenReceivedNewResourceProcessorEvent_ThenCallResourceAPIToGetResource() {
        var expectedContent = resourceDataRepository.get("123");
        Assertions.assertThat(expectedContent).isEqualTo(new byte[] {1, 2, 4, 5});
    }
}
