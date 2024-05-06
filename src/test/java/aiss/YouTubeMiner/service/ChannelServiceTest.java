package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.ChannelNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Channel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ChannelServiceTest {
    @Autowired
    ChannelService service;

    @Test
    @DisplayName("Get a channel")
    void findChannel() throws ChannelNotFoundException {
        Channel channelYouTube = service.findchannelById("UCX6OQ3DkcsbYNE6H8uQQuVA");
        assertNotNull(channelYouTube);
        System.out.println("Funciono + " + channelYouTube + " :)");
    }

}
