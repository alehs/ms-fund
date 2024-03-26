package com.example.resourceprocessor.contract;

import com.example.resourceprocessor.resource.domain.ResourceMetadata;
import com.example.resourceprocessor.resource.domain.ports.ResourceMetadataRepository;
import com.example.resourceprocessor.resource.domain.ports.ResourceProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class ResourceProcessorIntegrationTest {

    static {
        System.setProperty("eureka.client.enabled", "false");
    }

    @Autowired
    private ResourceProcessor resourceProcessor;

    @MockBean
    private ResourceMetadataRepository metadataRepository;

    @Test
    public void given_whenReceivedNewResourceProcessorEvent_ThenCallResourceAPIToGetResource()
            throws Exception {
        resourceProcessor.process("123");

        var metadata = new ResourceMetadata();
        metadata.setUuid("123");

        Mockito.verify(metadataRepository, Mockito.times(1)).save(metadata);

    }
}
