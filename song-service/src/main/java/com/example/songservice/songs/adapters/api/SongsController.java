package com.example.songservice.songs.adapters.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.songservice.songs.domain.SongMetadata;
import com.example.songservice.songs.domain.ports.SongsService;

@Slf4j
@RestController
@RequestMapping("/api/songs")
public class SongsController {

	private final SongsService songsService;
	public SongsController(SongsService songsService) {
		this.songsService = songsService;
	}

	@PostMapping
	public String createSong(@RequestBody SongMetadata metadata) {
		log.info("Request to save song metadata {}", metadata);
		SongMetadata saved = songsService.save(metadata);
		return saved.getUuid();
	}

	@GetMapping("/{uuid}")
	public ResponseEntity<SongMetadata> getSong(@PathVariable("uuid") String uuid) {
		log.info("Request to get song metadata by ID {}", uuid);
		return songsService.get(uuid)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
