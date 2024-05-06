package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.ChannelNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Channel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChannelServiceTest {
    @Autowired
    ChannelService service;

    @Test
    @DisplayName("Get a channel")
    void findChannel() throws ChannelNotFoundException {
        Channel channelYouTube = service.findchannelById("UC_x5XG1OV2P6uZZ5FSM9Ttw");
        System.out.println("Funciono + " + channelYouTube + " :)");
    }

}
