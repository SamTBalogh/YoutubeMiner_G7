package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.CaptionsNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Caption;
import aiss.YouTubeMiner.model.YoutubeModel.caption.Captions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CaptionServiceTest {
    @Autowired
    CaptionService service;
    @Test
    @DisplayName("Get Captions")
    void findCaptionsById() throws CaptionsNotFoundException {
        String prueba =  "_VB39Jo8mAQ";
        List<Caption> captions = service.findCaptionById(prueba);
        System.out.println(captions.toString());
    }
}
