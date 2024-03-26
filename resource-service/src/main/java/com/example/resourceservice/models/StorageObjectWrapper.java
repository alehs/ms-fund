package com.example.resourceservice.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StorageObjectWrapper {

    @JsonProperty("_embedded")
    List<StorageObjectDTO> storages;

    public List<StorageObjectDTO> getStorages() {
        return storages;
    }

    @JsonCreator
    public StorageObjectWrapper(@JsonProperty("_embedded") StorageObjectWrapper.Embedded embedded) {
        this.storages = embedded == null ? null : embedded.getStorages();
    }

    private static class Embedded {
        private List<StorageObjectDTO> storages;

        @JsonCreator
        public Embedded(@JsonProperty("storages") List<StorageObjectDTO> storages) {
            this.storages = storages;
        }

        public List<StorageObjectDTO> getStorages() {
            return storages;
        }
    }
}
