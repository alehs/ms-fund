package com.example.resourceprocessor.resource.domain.ports;

import com.example.resourceprocessor.resource.domain.ResourceMetadata;

public interface ResourceMetadataRepository {

	void save(ResourceMetadata metadata);

	ResourceMetadata get(String uuid);
}
