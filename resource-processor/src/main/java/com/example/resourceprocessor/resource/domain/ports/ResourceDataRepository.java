package com.example.resourceprocessor.resource.domain.ports;

import java.io.InputStream;

public interface ResourceDataRepository {
	byte[] get(String uuid);
}
