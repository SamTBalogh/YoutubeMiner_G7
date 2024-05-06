package aiss.YouTubeMiner.service;
import aiss.YouTubeMiner.model.YoutubeModel.channel.ChannelSearch;
import aiss.YouTubeMiner.model.YoutubeModel.videoSnippet.VideoSnippet;
import aiss.YouTubeMiner.model.YoutubeModel.videoSnippet.VideoSnippetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Service
public class VideoService {
    @Autowired
    RestTemplate restTemplate;
    private final static String token = "AIzaSyBEh_d3t-4l3qq83LeZhDPk3QctLXHH-RI";

    public VideoSnippet findVideo(String videoId) {
        String uri = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet&id=" + videoId + "&key=" + token;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", token);
        HttpEntity<VideoSnippetSearch> request = new HttpEntity<>(null, headers);
        ResponseEntity<VideoSnippetSearch> response = restTemplate.exchange(uri, HttpMethod.GET, request, VideoSnippetSearch.class);
        assert response.getBody() != null;
        System.out.println(response.getBody());
        return response.getBody().getItems().get(0);
    }

    public List<VideoSnippet> findVideos(String id, Integer maxVideo){
        HttpHeaders headers= new HttpHeaders();
        headers.set("X-goog-api-key", token);
        HttpEntity<VideoSnippetSearch> request = new HttpEntity<>(null,headers);
        String uri = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults="+ maxVideo+"&key="+token;
        ResponseEntity<VideoSnippetSearch>response2 = restTemplate.exchange(uri, HttpMethod.GET, request, VideoSnippetSearch.class);

        assert response2.getBody() != null;
        return response2.getBody().getItems();

    }
}
