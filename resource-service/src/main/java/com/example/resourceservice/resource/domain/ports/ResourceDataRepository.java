package com.example.resourceservice.resource.domain.ports;

public interface ResourceDataRepository {

	String save(String uuid, byte[] fileData, String bucketName);

	byte[] get(String uuid, String bucketName);

}
