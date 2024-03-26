package com.example.resourceservice.resource.domain.ports;

import com.example.resourceservice.models.StorageObjectDTO;
import com.example.resourceservice.resource.adapters.external.StoragesRemoteRepository;
import com.example.resourceservice.resource.domain.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ResourceService {

	public static final String STATUS_STAGING = "STAGING";
	private final ResourceRepository resourceRepository;
	private final ResourceDataRepository resourceDataRepository;
	private final ResourceMetadataProcessor metadataProcessor;
	private final StoragesRemoteRepository storagesRepository;

	public ResourceService(ResourceRepository resourceRepository,
                           ResourceDataRepository resourceDataRepository,
                           ResourceMetadataProcessor resourceMetadataProcessor, StoragesRemoteRepository storagesRepository) {
		this.resourceRepository = resourceRepository;
		this.resourceDataRepository = resourceDataRepository;
		this.metadataProcessor = resourceMetadataProcessor;
        this.storagesRepository = storagesRepository;
    }

	public List<String> validate(byte[] file) {
		List<String> violations = new ArrayList<>();
		if (file == null || file.length == 0) {
			violations.add("File content is empty");
		}
		return violations;
	}

	public String save(byte[] fileData) {
		UUID uuid = UUID.randomUUID();
		log.info("Start processing new save resource request with ID={}", uuid);

		Resource toSave = new Resource(uuid.toString(), null, STATUS_STAGING);
		List<StorageObjectDTO> storageList = storagesRepository.getStorageList();

		String location = resourceDataRepository.save(uuid.toString(), fileData, getBucketByStatus(storageList, toSave.getStatus()));

		toSave.setLocation(location);

		Resource saved = saveResource(toSave);

		metadataProcessor.process(saved);

		return saved.getUuid();
	}

	private Resource saveResource(Resource resource) {
		log.info("Saving to internal DB. Resource: {}", resource);
		Resource saved;
		try {
			saved = resourceRepository.save(resource);
		} catch (Exception e) {
			log.error("Failed to handle file {}", "File", e);
			throw new RuntimeException(e);
		}
		return saved;
	}

	public Optional<Resource> get(String uuid) {
		return resourceRepository.findById(uuid);
	}

	public Optional<byte[]> getResourceData(String uuid) {
		log.info("Getting resource data by uuid {}", uuid);
		Optional<Resource> resource = resourceRepository.findById(uuid);
		return resource.map(this::fetchResourceData);
	}

	public List<String> deleteAll(String[] uuids) {
		List<String> deleted = new ArrayList<>();
		for (String uuid : uuids) {
			resourceRepository.deleteById(uuid);
			deleted.add(uuid);
		}
		return deleted;
	}

	private byte[] fetchResourceData(Resource r) {
		List<StorageObjectDTO> storageList = storagesRepository.getStorageList();
		return resourceDataRepository.get(r.getUuid(), getBucketByStatus(storageList, r.getStatus()));
	}

	private String getBucketByStatus(List<StorageObjectDTO> storageList, String status) {
		return storageList.stream().filter(s -> s.getType().equals(status)).findFirst()
				.map(s -> s.getBucket())
				.orElse(null);
	}
}
