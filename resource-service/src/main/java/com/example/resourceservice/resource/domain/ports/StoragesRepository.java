package com.example.resourceservice.resource.domain.ports;

import com.example.resourceservice.models.StorageObjectDTO;

import java.util.List;

public interface StoragesRepository {

    List<StorageObjectDTO> getStorageList();

}
