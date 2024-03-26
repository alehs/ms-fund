import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "should return even when number input is even"
    request{
        method POST()
        url "/api/resources"
        body(new byte[]{1,2,3,4})
    }
    response {
        body $(regex('^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'))
        status 200
    }
}