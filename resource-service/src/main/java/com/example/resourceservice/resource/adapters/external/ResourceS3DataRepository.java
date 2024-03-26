package com.example.resourceservice.resource.adapters.external;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.resourceservice.resource.domain.ports.ResourceDataRepository;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Slf4j
@Service
public class ResourceS3DataRepository implements ResourceDataRepository {

	private final S3Client s3Client;

	public ResourceS3DataRepository(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	@Override
	public String save(String uuid, byte[] fileData, String bucketName) {
		log.info("Saving resource file {} to s3 bucket {}", uuid, bucketName);

		PutObjectRequest objectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(uuid)
				.build();

		ByteBuffer byteBuffer = ByteBuffer.wrap(fileData);
		PutObjectResponse response = s3Client.putObject(objectRequest, RequestBody.fromByteBuffer(byteBuffer));

		log.info("File {} saved to bucket {}. e-tag={}", uuid, bucketName, response.eTag());
		return bucketName + "/" + uuid;
	}

	@Override
	public byte[] get(String uuid, String bucketName) {
		log.info("Fetching data for resource {}", uuid);

		GetObjectRequest objectRequest = GetObjectRequest
			.builder()
			.key(uuid)
			.bucket(bucketName)
			.build();

		ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
		return objectBytes.asByteArray();
	}

}
