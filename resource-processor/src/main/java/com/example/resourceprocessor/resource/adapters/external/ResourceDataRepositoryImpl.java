package com.example.resourceprocessor.resource.adapters.external;

import com.example.resourceprocessor.resource.domain.ports.ResourceDataRepository;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ResourceDataRepositoryImpl implements ResourceDataRepository {

	private final String resourceServiceName;
	private final RetryTemplate retryTemplate;
	private final RestTemplate restTemplate;
	private final EurekaClient eurekaClient;

	public ResourceDataRepositoryImpl(@Value("${resource.service.name}") String resourceServiceName,
                                      RetryTemplate retryTemplate, RestTemplate restTemplate, EurekaClient eurekaClient) {
		this.resourceServiceName = resourceServiceName;
		this.retryTemplate = retryTemplate;
        this.restTemplate = restTemplate;
        this.eurekaClient = eurekaClient;
    }

	@Override
	public byte[] get(String uuid) {
		return retryTemplate.execute(arg0 -> fetchResourceData(uuid));
	}

	private byte[] fetchResourceData(String uuid) {
		final String serviceUri = getServiceUri() + uuid;
		log.info("Request to fetch resource data from {}", serviceUri);
		return restTemplate.getForObject(serviceUri, byte[].class);
	}

	private String getServiceUri() {
		String baseUrl;
		if (this.resourceServiceName.contains("localhost")) {
			baseUrl = "http://" + this.resourceServiceName + "/";
		} else {
			baseUrl = eurekaClient.getNextServerFromEureka(resourceServiceName, false).getHomePageUrl();
		}
		return baseUrl + "api/resources/";
	}
}
