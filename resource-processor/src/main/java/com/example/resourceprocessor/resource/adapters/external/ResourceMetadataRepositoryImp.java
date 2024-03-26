package com.example.resourceprocessor.resource.adapters.external;

import com.example.resourceprocessor.resource.domain.ResourceMetadata;
import com.example.resourceprocessor.resource.domain.ports.ResourceMetadataRepository;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
public class ResourceMetadataRepositoryImp implements ResourceMetadataRepository {

	private final String songsServiceName;
	private final RestTemplate restTemplate;
	private final RetryTemplate retryTemplate;
	private final EurekaClient eurekaClient;

	public ResourceMetadataRepositoryImp(
            @Value("${songs.service.name}") String songsServiceName,
            RestClient.Builder restClientBuilder, RestTemplate restTemplate, RetryTemplate retryTemplate, EurekaClient eurekaClient) {
		this.songsServiceName = songsServiceName;
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
        this.eurekaClient = eurekaClient;
    }

	@Override
	public void save(ResourceMetadata metadata) {
		String requestUri = getBaseUrl();
		log.info("Request to persist metadata in songs service {}", requestUri);
		var songUuid = retryTemplate.execute(arg0 ->
				restTemplate.postForObject(requestUri, metadata, String.class));
		log.info("Persisted song metadata with ID: {}", songUuid);
	}

	@Override
	public ResourceMetadata get(String uuid) {
		String requestUri = getBaseUrl() + uuid;
		log.info("Request to get metadata from songs service {}", requestUri);
		return retryTemplate.execute(arg0 ->
				restTemplate.getForObject(requestUri, ResourceMetadata.class)
		);
	}

	private String getBaseUrl() {
		String baseUrl;
		if (this.songsServiceName.contains("localhost")) {
			baseUrl = "http://" + this.songsServiceName + "/";
		} else {
			baseUrl = eurekaClient.getNextServerFromEureka(songsServiceName, false).getHomePageUrl();
		}
		return baseUrl + "api/songs";
	}
}
