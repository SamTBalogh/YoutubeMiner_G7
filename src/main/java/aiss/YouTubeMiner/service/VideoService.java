package aiss.YouTubeMiner.service;

import org.springframework.beans.factory.annotation.Value;

public class VideoService {
    @Autowired
    RestTemplate restTemplate;

    @Value("${youtube.api.token}")
    private String token;

    @Value("${youtube.api.uri}")
    private String uri;

    public Video getVideo(String videoId) {

    }

    public List<Video> getVideosFromChannel(String channelId) {
    }
}
