package com.example.resourceservice.cucumber;

import org.springframework.http.client.ClientHttpResponse;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class ResponseWrap {

    private final ClientHttpResponse theResponse;
    private final String body;

    ResponseWrap(final ClientHttpResponse response) throws IOException {
        this.theResponse = response;
        String parsedBody = null;
        try {
            final InputStream bodyInputStream = response.getBody();
            final StringWriter stringWriter = new StringWriter();
            IOUtils.copy(bodyInputStream, stringWriter);
            parsedBody = stringWriter.toString();
        } catch (Exception ex) {
            parsedBody = null;
        }
        this.body = parsedBody;
    }

    ClientHttpResponse getTheResponse() {
        return theResponse;
    }

    String getBody() {
        return body;
    }
}
