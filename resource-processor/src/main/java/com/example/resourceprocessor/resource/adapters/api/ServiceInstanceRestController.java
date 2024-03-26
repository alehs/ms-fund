package com.example.resourceprocessor.resource.adapters.api;

import java.io.ByteArrayInputStream;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resourceprocessor.resource.adapters.Mp3ResourceMetadataParser;
import com.example.resourceprocessor.resource.domain.ResourceMetadata;

@Slf4j
@RestController
public class ServiceInstanceRestController {

	@Autowired
	private DiscoveryClient discoveryClient;
	@Autowired
	private Mp3ResourceMetadataParser resourceService;

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(
			@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

	@PostMapping("/api/metadata")
	public ResponseEntity<ResourceMetadata> parseMetadata(@RequestBody byte[] data) {
		log.info("Parse metadata");
		return ResponseEntity.ok(resourceService.parseMetadata(new ByteArrayInputStream(data)));
	}

}
