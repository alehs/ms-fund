package com.example.resourceprocessor.resource.domain.ports;

import java.io.InputStream;

import com.example.resourceprocessor.resource.domain.ResourceMetadata;

public interface ResourceMetadataParser {
	ResourceMetadata parseMetadata(InputStream steam);
}
