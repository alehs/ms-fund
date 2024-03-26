package com.example.resourceservice.resource.adapters.external;

import com.example.resourceservice.models.StorageObjectDTO;
import com.example.resourceservice.models.StorageObjectWrapper;
import com.example.resourceservice.resource.domain.ports.StoragesRepository;
import com.netflix.discovery.EurekaClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StoragesRemoteRepository implements StoragesRepository {

    private static final String SERVICE_NAME = "storageService";

    private final String storageService;
    private final EurekaClient eurekaClient;
    private final RestTemplate restTemplate;

    public StoragesRemoteRepository(
            @Value("${storage.service.name}") String storageServiceName,
            EurekaClient eurekaClient, RestTemplate restTemplate) {
        this.eurekaClient = eurekaClient;
        this.restTemplate = restTemplate;
        this.storageService = storageServiceName;
    }

    @Override
    @CircuitBreaker(name = SERVICE_NAME)
    @Bulkhead(name = SERVICE_NAME)
    @Retry(name = SERVICE_NAME)
    public List<StorageObjectDTO> getStorageList() {
//        StorageObjectWrapper items = webClient.get()
//                .uri(URI.create(getStorageURI()))
//                .retrieve()
//                .body(StorageObjectWrapper.class);

        StorageObjectWrapper items = restTemplate.getForObject(URI.create(getStorageURI()), StorageObjectWrapper.class);
        return new ArrayList<>(items.getStorages());
    }

    private String getStorageURI() {
        String baseUrl;
        if (this.storageService.contains("localhost")) {
            baseUrl = "http://" + this.storageService + "/";
        } else {
            baseUrl = eurekaClient.getNextServerFromEureka(storageService, false).getHomePageUrl();
        }

        return baseUrl + "api/storages";
    }
}
