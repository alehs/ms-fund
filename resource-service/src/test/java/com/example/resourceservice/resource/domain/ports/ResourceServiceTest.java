package com.example.resourceservice.resource.domain.ports;

import com.example.resourceservice.models.StorageObjectDTO;
import com.example.resourceservice.resource.adapters.external.StoragesRemoteRepository;
import com.example.resourceservice.resource.domain.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private ResourceDataRepository resourceDataRepository;
    @Mock
    private StoragesRemoteRepository storagesRepository;
    @Mock
    private ResourceMetadataProcessor metadataProcessor;

    @InjectMocks
    private ResourceService resourceService;

    @Test
    public void testValidate() {
        byte[] mockFile = new byte[] {1,2,3,4};
        assertThat(resourceService.validate(mockFile)).isEmpty();
    }

    @Test
    public void testValidate_andContentIsEmpty_thenError() {
        assertThat(resourceService.validate(null)).contains("File content is empty");
    }

    @Test
    public void testValidate_andContentIsNull_thenError() {
        assertThat(resourceService.validate(new byte[] {})).contains("File content is empty");
    }


    @Test
    public void testSave() {
        // given
        byte[] mockFile = new byte[] {1,2,3,4};
        String mockLocation = "test/file";
        String mockId = "123";
        when(resourceDataRepository.save(anyString(), any(byte[].class), anyString())).thenReturn(mockLocation);
        when(storagesRepository.getStorageList()).thenReturn(List.of(new StorageObjectDTO().withType("STAGING").withBucket("test_bucket")));
        when(resourceRepository.save(any(Resource.class))).thenReturn(new Resource(mockId, mockLocation, "STAGING"));

        // when
        String savedId = resourceService.save(mockFile);

        // then
        assertThat(savedId).isEqualTo(mockId);
        verify(resourceDataRepository, Mockito.times(1)).save(anyString(), any(byte[].class), anyString());
        verify(metadataProcessor, times(1)).process(any(Resource.class));
    }

    @Test
    public void getResourceData() {
        // given
        byte[] mockFile = new byte[] {1,2,3,4};
        String testId = "123";
        String testLocation = "test/location";
        Resource mockResource = new Resource(testId, testLocation, "STAGING");

        when(resourceRepository.findById(testId)).thenReturn(Optional.of(mockResource));
        when(storagesRepository.getStorageList()).thenReturn(List.of(new StorageObjectDTO().withType("STAGING").withBucket("test_bucket")));
        when(resourceDataRepository.get(eq(testId), anyString())).thenReturn(mockFile);

        // when
        Optional<byte[]> result = resourceService.getResourceData(testId);

        // then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(mockFile);
    }

    @Test
    public void testGetResourceData_andResourceNotFound_thenNull() {
        // given
        String mockId = "123";
        when(resourceRepository.findById(mockId)).thenReturn(Optional.ofNullable(null));

        // when
        Optional<byte[]> result = resourceService.getResourceData(mockId);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void deleteAll() {
        // TODO: add more tests
    }
}