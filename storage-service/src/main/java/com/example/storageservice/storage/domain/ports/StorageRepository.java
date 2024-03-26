package com.example.storageservice.storage.domain.ports;

import com.example.storageservice.storage.domain.StorageObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "storages", path = "storages")
public interface StorageRepository extends CrudRepository<StorageObject, Long> {

}
