package com.example.resourceprocessor.resource.domain.ports;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.example.resourceprocessor.resource.domain.ResourceMetadata;

@Slf4j
@Service
public class ResourceProcessor {

	private final ResourceMetadataRepository metadataRepository;
	private final ResourceMetadataParser metadataParser;
	private final ResourceDataRepository dataRepository;

	public ResourceProcessor(ResourceMetadataRepository metadataRepository,
							 ResourceMetadataParser metadataParser, ResourceDataRepository dataRepository) {
		this.metadataRepository = metadataRepository;
		this.metadataParser = metadataParser;
		this.dataRepository = dataRepository;
	}

	public void process(String uuid) {
		log.info("Processing resource with uuid: {}", uuid);
		byte[] resourceData = dataRepository.get(uuid);
		if (resourceData == null) {
			throw new RuntimeException("Cannot find resource data for uuid: " + uuid);
		}
		log.info("Parsing resource metadata for uuid: {}", uuid);
		ResourceMetadata metadata = metadataParser.parseMetadata(new ByteArrayInputStream(resourceData));
		metadata.setUuid(uuid);
		log.info("Persisting resource metadata: {}", metadata);
		metadataRepository.save(metadata);
		log.info("Done processing resource with uuid: {}", uuid);
	}

}
