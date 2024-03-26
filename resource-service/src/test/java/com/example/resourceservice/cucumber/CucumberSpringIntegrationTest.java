package com.example.resourceservice.cucumber;

import com.example.resourceservice.SpringTestApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest(classes = SpringTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CucumberSpringIntegrationTest {

    static ResponseWrap latestResponse = null;

    public final String hostUrl = "http://localhost:8080";

    @Autowired
    protected RestTemplate restTemplate;

    public void executeGet(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(hostUrl + url, HttpMethod.GET, requestCallback, response -> {
            if (errorHandler.hadError) {
                return (errorHandler.getResults());
            } else {
                return new ResponseWrap(response);
            }
        });
    }

    public void executePost(String url, byte[] body) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        requestCallback.setBody(body);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate
                .execute(hostUrl + url, HttpMethod.POST, requestCallback, response -> {
                    if (errorHandler.hadError) {
                        return (errorHandler.getResults());
                    } else {
                        return new ResponseWrap(response);
                    }
                });
    }

    private static class ResponseResultErrorHandler implements ResponseErrorHandler {
        private ResponseWrap results = null;
        private Boolean hadError = false;

        private ResponseWrap getResults() {
            return results;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            hadError = response.getStatusCode().value() >= 400;
            return hadError;
        }

        @Override
        public void handleError(@NotNull ClientHttpResponse response) throws IOException {
            results = new ResponseWrap(response);
        }
    }

        private static class HeaderSettingRequestCallback implements RequestCallback {
            final Map<String, String> requestHeaders;

            private byte[] body;

            public HeaderSettingRequestCallback(final Map<String, String> headers) {
                this.requestHeaders = headers;
            }

            public void setBody(final byte[] postBody) {
                this.body = postBody;
            }

            @Override
            public void doWithRequest(ClientHttpRequest request) throws IOException {
                final HttpHeaders clientHeaders = request.getHeaders();
                for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                    clientHeaders.add(entry.getKey(), entry.getValue());
                }
                if (null != body) {
                    request.getBody().write(body);
                }
            }
        }

    }