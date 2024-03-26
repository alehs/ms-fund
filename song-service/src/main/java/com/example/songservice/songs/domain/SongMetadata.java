package com.example.songservice.songs.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class SongMetadata {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String uuid;
	String resourceId;
	String name;
	String album;
	String artist;
	String length;
	@Column(name = "year_published")
	String year;
	String genre;
}
