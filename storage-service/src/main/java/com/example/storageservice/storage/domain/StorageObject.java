package com.example.storageservice.storage.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity (name = "storage_object")
@Data
public class StorageObject {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private StorageType type;
    private String bucket;
    private String path;

}
