Feature: service should return uploaded file content
    Scenario: client tries to get the unknown file
        When the client calls /api/resources/unknown
        Then the client receives status code of 404
