package com.example.resourceservice.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageObjectDTO {
    private long id;
    @With
    private String type;
    @With
    private String bucket;
    @With
    private String path;
}
