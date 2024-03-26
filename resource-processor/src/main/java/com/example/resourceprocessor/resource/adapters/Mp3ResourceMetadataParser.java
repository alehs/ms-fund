package com.example.resourceprocessor.resource.adapters;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMP;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;

import com.example.resourceprocessor.resource.domain.ResourceMetadata;
import com.example.resourceprocessor.resource.domain.ports.ResourceMetadataParser;


@Component
@Slf4j
public class Mp3ResourceMetadataParser implements ResourceMetadataParser {

	public ResourceMetadata parseMetadata(InputStream steam) {
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		ParseContext pcontext = new ParseContext();

		ResourceMetadata resourceMetadata = new ResourceMetadata();

		//Mp3 parser
		Mp3Parser Mp3Parser = new  Mp3Parser();
		try {
			Mp3Parser.parse(steam, handler, metadata, pcontext);
			Map<String, String> metadataMap = toMap(metadata);
			resourceMetadata.setName(metadataMap.get(TikaCoreProperties.TITLE.getName()));
			resourceMetadata.setAlbum(metadataMap.get(XMPDM.ALBUM.getName()));
			resourceMetadata.setArtist(metadataMap.get(XMPDM.ARTIST.getName()));
			resourceMetadata.setYear(metadataMap.get(XMPDM.RELEASE_DATE.getName()));
			resourceMetadata.setLength(metadataMap.get(XMPDM.DURATION.getName()));
			resourceMetadata.setGenre(metadataMap.get(XMPDM.GENRE.getName()));
		} catch (Exception e) {
			log.error("Failed to parse file", e);
			throw new RuntimeException(e);
		}

		return resourceMetadata;
	}

	private static Map<String, String> toMap(Metadata metadata) {
		Map<String, String> metadataMap = new HashMap<>();

		String[] metadataNames = metadata.names();
		for(String name : metadataNames) {
			metadataMap.put(name, metadata.get(name));
		}
		return metadataMap;
	}

}
