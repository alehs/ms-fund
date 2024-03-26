import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "Should return resource content by the provided id"
    request{
        method GET()
        url "/api/resources/123"
    }
    response {
        headers {
            contentType("application/octet-stream")
        }
        body(new byte[] {1,2,4,5})
        status 200
    }
}