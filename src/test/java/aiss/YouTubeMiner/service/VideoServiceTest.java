package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.ChannelNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Video;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VideoServiceTest {
    @Autowired
    VideoService service;

    @Test
    @DisplayName("Get a certain number of videos from a valid channel Id")
    void findMaxVideos() throws ChannelNotFoundException {
        List<Video> video= service.findVideosMax("UCX6OQ3DkcsbYNE6H8uQQuVA", 3);
        assertNotNull(video);
        assertEquals(3, video.size());
        System.out.println(video);
    }

    @Test
    @DisplayName("Get a certain number videos from an invalid channel Id")
    void findMaxVideosFromInvalidChannelId()  {
        assertThrows(ChannelNotFoundException.class, () -> {service.findVideosMax("Wololo", 10);});
    }

    @Test
    @DisplayName("Get videos from a valid channel Id")
    void findVideos() throws ChannelNotFoundException {
        List<Video> video= service.findVideos("UCX6OQ3DkcsbYNE6H8uQQuVA");
        assertNotNull(video);
        System.out.println(video);
    }

    @Test
    @DisplayName("Get videos from an invalid channel Id")
    void findVideosFromInvalidChannelId()  {
        assertThrows(ChannelNotFoundException.class, () -> {service.findVideos("Wololo");});
    }

}
