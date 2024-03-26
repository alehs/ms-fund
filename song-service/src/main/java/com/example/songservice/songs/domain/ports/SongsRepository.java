package com.example.songservice.songs.domain.ports;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.songservice.songs.domain.SongMetadata;

@Repository
public interface SongsRepository extends CrudRepository<SongMetadata, String> {
	Optional<SongMetadata> findByResourceId(String resourceId);
}
