package com.example.resourceservice.resource.adapters.api;

import com.example.resourceservice.SpringTestApplication;
import com.example.resourceservice.resource.domain.ports.ResourceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringTestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ResourcesControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ResourceService resourceService;

    @Test
    void uploadResource() throws Exception {
        // given
        byte[] testFile = new byte[] { 1, 2, 3};
        when(resourceService.save(testFile)).thenReturn("test/newfile");

        // when
        mockMvc.perform(post("/api/resources")
                .content(testFile))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("test/newfile"));
    }

    @Test
    void getResource() throws Exception {
        // given
        String testId = "12354";
        byte[] testFile = new byte[] { 1, 2, 3};
        when(resourceService.getResourceData(testId)).thenReturn(Optional.of(testFile));

        // when
        mockMvc.perform(get("/api/resources/" + testId).content(testFile))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes(testFile));
    }

    @Test
    void deleteResources() throws Exception {
        // given
        String testId = "12354";
        byte[] testFile = new byte[] { 1, 2, 3};
        when(resourceService.deleteAll(new String[]{testId})).thenReturn(List.of(testId));

        // when
        mockMvc.perform(delete("/api/resources").param("ids", testId).content(testFile))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").value(testId));
    }
}