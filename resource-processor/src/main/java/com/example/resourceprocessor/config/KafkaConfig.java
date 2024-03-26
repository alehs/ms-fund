package com.example.resourceprocessor.config;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@Slf4j
@EnableKafka
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
public class KafkaConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;
	@Value(value = "${kafka.consumer.topic}")
	private String topicName;
	@Value(value = "${kafka.consumer.group-id}")
	private String groupId;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic processingQueue() {
		return new NewTopic(topicName, 1, (short) 1);
	}

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		log.info("Creating consumer factory for topic {} and group {}. Bootstrap address: {}", topicName, groupId, bootstrapAddress);
		Map<String, Object> props = new HashMap<>();
		props.put(
				ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				bootstrapAddress);
		props.put(
				ConsumerConfig.GROUP_ID_CONFIG,
				groupId);
		props.put(
				ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);
		props.put(
				ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String>	kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.getContainerProperties().setObservationEnabled(true);
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

}
