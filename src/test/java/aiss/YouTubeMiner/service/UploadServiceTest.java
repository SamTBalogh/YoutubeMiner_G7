package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.UploadsNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UploadServiceTest {

    @Autowired
    UploadService service;

    @Test
    @DisplayName("Get a List of Uploads from a valid UploadsId")
    void findUploads() throws UploadsNotFoundException {
        List<String> videoIds = service.findUploadsIds("UUUyeluBRhGPCW4rPe_UvBZQ");
        assertNotNull(videoIds);
        System.out.println(videoIds);
    }

    @Test
    @DisplayName("Get a List of Uploads of a certain size from a valid UploadsId")
    void findUploadsIdsMax() throws UploadsNotFoundException {
        List<String> videoIds = service.findUploadsIdsMax("UUUyeluBRhGPCW4rPe_UvBZQ", 10);
        assertNotNull(videoIds);
        assertEquals(10, videoIds.size());
        System.out.println(videoIds);
    }

    @Test
    @DisplayName("Get a List of Uploads from an invalid UploadsId")
    void findUploadsError(){
        assertThrows(HttpClientErrorException.class, () -> {service.findUploadsIds("Wololo");});
    }

    @Test
    @DisplayName("Get a List of Uploads of a certain size from an invalid UploadsId")
    void findUploadsIdsMaxError(){
        assertThrows(HttpClientErrorException.class, () -> {service.findUploadsIdsMax("Wololo", 10);});
    }

}