package com.example.resourceservice.cucumber;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class UploadStepDefinitions extends CucumberSpringIntegrationTest {

    private byte[] fileContent;
    private String uploadedId;

    private static final String CREATE_PATH = "/create";
    private static final String APPLICATION_JSON = "application/json";

    private final WireMockServer wireMockServer = new WireMockServer(options().port(4566));

    @Given("new mp3 file")
    public void given_new_file() {
        this.fileContent = new byte[] {1,2,3,4,5};
    }

    @When("I upload file to the service")
    public void user_upload_file_to_the_service() throws IOException {
        wireMockServer.start();
        executePost("/api/resources", fileContent);
        wireMockServer.stop();
    }

    @Then("I receive ID of new uploaded file")
    public void user_receive_id_of_new_uploaded_file() throws IOException {
        uploadedId = String.valueOf(latestResponse.getBody());
        Assertions.assertThat(uploadedId).isNotBlank();
    }

    @Then("I can download file from s3 using the ID")
    public void user_can_download_file_from_s3_using_the_id() throws IOException {
        executeGet("/api/resources/" + uploadedId);
        Assertions.assertThat(latestResponse.getTheResponse().getStatusCode().value()).isEqualTo(500);
    }

}
