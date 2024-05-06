package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.ChannelNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Video;
import aiss.YouTubeMiner.model.YoutubeModel.videoSnippet.VideoSnippet;
import aiss.YouTubeMiner.model.YoutubeModel.videoSnippet.VideoSnippetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {
    @Autowired
    RestTemplate restTemplate;

    @Value("${youtube.api.token}")
    private String token;

    @Value("${youtube.api.uri}")
    private String uri;

    public List<Video> findVideos(String channelId, Integer numVideos) throws ChannelNotFoundException {
        String url = uri + "/search?channelId="+channelId+"&part=snippet&type=videomaxResults="+numVideos;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", token);
        HttpEntity<VideoSnippetSearch> request = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<VideoSnippetSearch> response = restTemplate.exchange(url, HttpMethod.GET, request, VideoSnippetSearch.class);
            List<VideoSnippet> videoRequest = response.getBody().getItems();
            return videoRequest.stream().map(Video::new).collect(Collectors.toList());

        } catch (HttpClientErrorException.BadRequest e) {
            throw new ChannelNotFoundException();
        }

    }
}
