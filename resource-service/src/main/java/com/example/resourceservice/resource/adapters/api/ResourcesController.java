package com.example.resourceservice.resource.adapters.api;

import com.example.resourceservice.models.StorageObjectDTO;
import com.example.resourceservice.resource.adapters.external.StoragesRemoteRepository;
import com.example.resourceservice.resource.domain.ports.ResourceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ResourcesController {

	private final ResourceService resourceService;
	private final StoragesRemoteRepository storageRepository;

	public ResourcesController(ResourceService resourceService, StoragesRemoteRepository storageRepository) {
		this.resourceService = resourceService;
        this.storageRepository = storageRepository;
    }

	@GetMapping("/storages")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<StorageObjectDTO>> pingStorage() {
		return ResponseEntity.ok(storageRepository.getStorageList());
	}

	@PostMapping("/resources")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> uploadResource(@RequestBody byte[] data) {
		var validationErrors = resourceService.validate(data);
		if (!CollectionUtils.isEmpty(validationErrors)) {
			HttpHeaders headers = new HttpHeaders();
			validationErrors.stream().forEach(error -> headers.add("app-validation-err", error));
			return ResponseEntity.badRequest().headers(headers).build();
		}
		return ResponseEntity.ok(resourceService.save(data));
	}

	@GetMapping("/resources/{id}")
	public ResponseEntity<byte[]> getResource(@PathVariable("id") String uuid) {
		return resourceService.getResourceData(uuid)
				.map(resource -> ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + uuid + "\"")
						.body(resource))
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/resources")
	public ResponseEntity<List<String>> deleteResources(@RequestParam("ids") String[] uuids) {
		return ResponseEntity.ok(resourceService.deleteAll(uuids));
	}
}
