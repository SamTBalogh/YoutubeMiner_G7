package aiss.YouTubeMiner.service;

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
    @DisplayName("Get a video")
    void findvideo(){
        VideoSnippet res= service.findVideo("VVVfeDVYRzFPVjJQNnVaWjVGU005VHR3LnNxdE9sLXlJSFNB");
    }

    @Test
    @DisplayName("Get videos from a channel")
    void findVideos() {
        String prueba ="Ks-_Mh1QhMc";
        List<VideoSnippet> video= service.findVideos("UC_x5XG1OV2P6uZZ5FSM9Ttw", 4);
        System.out.println("Funciona \n"+ video.toString());

    }
}
