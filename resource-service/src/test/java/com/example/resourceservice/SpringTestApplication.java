package com.example.resourceservice;

import com.netflix.discovery.EurekaClient;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class SpringTestApplication {

//    @Bean
//    public RestTemplate getRestTemplate() {
//        return new RestTemplate();
//    }

    @Bean
    public EurekaClient getEurekaClient() {
        return Mockito.mock(EurekaClient.class);
    }

    @Bean
    public S3Client s3Client() {
        return new S3Client() {
            @Override
            public String serviceName() {
                return null;
            }

            @Override
            public void close() {}

            @Override
            public PutObjectResponse putObject(PutObjectRequest putObjectRequest, RequestBody requestBody) throws AwsServiceException, SdkClientException {
                return PutObjectResponse.builder().build();
            }
        };
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate(MockProducer::new) {
            @Override
            public CompletableFuture<SendResult> send(String topic, Object data) {
                return CompletableFuture.completedFuture(new SendResult<>(null, new RecordMetadata(null, 0,0,0,0,0)));
            }
        };
    }

}
