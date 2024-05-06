package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.CaptionsNotFoundException;
import aiss.YouTubeMiner.exception.CommentNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Video;
import aiss.YouTubeMiner.model.YoutubeModel.videoSnippet.VideoSnippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class VideoServiceTest {
    @Autowired
    VideoService service;

    @Test
    @DisplayName("Get videos from a channel")
    void findVideos() throws CaptionsNotFoundException, CommentNotFoundException {
        String prueba ="UCX6OQ3DkcsbYNE6H8uQQuVA";
        List<Video> video= service.findVideos(prueba, 1, 1);
        System.out.println(video);

    }
}
