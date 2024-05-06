package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.ChannelNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Video;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class VideoServiceTest {
    @Autowired
    VideoService service;

    @Test
    @DisplayName("Get videos from a channel")
    void findVideos() throws ChannelNotFoundException {
        List<Video> video= service.findVideos("UCX6OQ3DkcsbYNE6H8uQQuVA", 1);
        assertNotNull(video);
        System.out.println(video);

    }
}
