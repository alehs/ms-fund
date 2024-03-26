package com.example.resourceprocessor.resource.adapters.input;

import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.resourceprocessor.resource.domain.ports.ResourceProcessor;

@Slf4j
@Component
public class ResourceProcessingConsumer {

	private final ResourceProcessor resourceProcessor;

	public ResourceProcessingConsumer(ResourceProcessor resourceProcessor) {
		this.resourceProcessor = resourceProcessor;
	}

	@KafkaListener(containerFactory = "kafkaListenerContainerFactory",
			topics = "${kafka.consumer.topic}", groupId = "${kafka.consumer.group-id}",
			autoStartup = "${kafka.enabled:false}")
	public void listen(String message) {
		log.info("Received Message {}: ", message);
		resourceProcessor.process(message);
	}

}
