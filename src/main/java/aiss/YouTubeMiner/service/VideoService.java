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
        Video video;
        String url = uri+"/videos?key="+token+"&type=video" + "&part=snippet&id="+videoId;

        video = restTemplate.getForObject(uri, Video.class);

        return video;
    }

    public List<Video> getVideosFromChannel(String channelId) {
        List<Video> videos;
        String url = uri+"/videos?key="+token+"$type=video" + "&part=snippet&channelId="+channelId;

        Video[] response = restTemplate.getForObject(uri, Video[].class);
        videos = Arrays.stream(Objects.requireNonNull(response)).toList();

        return videos;
    }
}
