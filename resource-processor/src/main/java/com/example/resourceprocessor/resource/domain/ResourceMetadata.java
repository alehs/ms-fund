package com.example.resourceprocessor.resource.domain;

import lombok.Data;

@Data
public class ResourceMetadata {
	String uuid;
	String resourceId;
	String name;
	String album;
	String artist;
	String length;
	String year;
	String genre;
}
