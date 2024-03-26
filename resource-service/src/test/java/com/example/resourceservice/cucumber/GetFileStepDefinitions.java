package com.example.resourceservice.cucumber;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatusCode;

public class GetFileStepDefinitions extends CucumberSpringIntegrationTest {

    @When("the client calls {word}")
    public void the_client_issues_GET_resource(String url) throws Throwable {
        executeGet(url);
    }

    @Then("the client receives status code of {int}")
    public void the_client_receives_status_code_of(int code) throws Throwable {
        final HttpStatusCode statusCode = latestResponse.getTheResponse().getStatusCode();
        Assertions.assertThat(statusCode.value()).isEqualTo(code);
    }

}
