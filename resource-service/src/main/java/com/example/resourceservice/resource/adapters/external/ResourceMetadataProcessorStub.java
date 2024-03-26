package com.example.resourceservice.resource.adapters.external;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.example.resourceservice.resource.domain.Resource;
import com.example.resourceservice.resource.domain.ports.ResourceMetadataProcessor;

@Slf4j
@Component
public class ResourceMetadataProcessorStub implements ResourceMetadataProcessor {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final String topicName;

	public ResourceMetadataProcessorStub(@Value("${kafka.producer.topic}") String topicName,
										 KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
		this.topicName = topicName;
	}

	public void sendMessage(String msg) {
		log.info("Sending message='{}' to kafka topic='{}'", msg, topicName);
		CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, msg);
		future.whenComplete((result, ex) -> {
			if (ex != null) {
				log.error("Unable to send message=[{}] due to : {}", msg, ex.getMessage());
			} else {
				log.info("Sent message=[{}] with offset=[{}]", msg, result.getRecordMetadata().offset());
			}
		});
	}

	@Override
	public void process(Resource resource) {
		sendMessage(resource.getUuid());
	}

}
