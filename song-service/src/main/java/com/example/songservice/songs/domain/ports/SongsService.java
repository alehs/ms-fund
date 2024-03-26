package com.example.songservice.songs.domain.ports;

import java.util.Optional;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.example.songservice.songs.domain.SongMetadata;

@Slf4j
@Component
public class SongsService {

	private final SongsRepository songsRepository;
	public SongsService(SongsRepository songsRepository) {
		this.songsRepository = songsRepository;
	}

	@Observed(name = "song.save",
			contextualName = "saving-song-metadata",
			lowCardinalityKeyValues = {"resourceType", "metadata"})
	public SongMetadata save(SongMetadata metadata) {
		metadata.setResourceId(metadata.getUuid());
		log.info("Persisting song metadata for resourceId: {}", metadata.getResourceId());
		return songsRepository.save(metadata);
	}

	@Observed(name = "song.get",
			contextualName = "getting-song-metadata",
			lowCardinalityKeyValues = {"resourceType", "metadata"})
	public Optional<SongMetadata> get(String uuid) {
		log.info("Fetching song metadata for resourceId: {}", uuid);
		return songsRepository.findByResourceId(uuid);
	}
}
