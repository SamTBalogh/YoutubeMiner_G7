package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.CaptionNotFoundException;
import aiss.YouTubeMiner.exception.CommentNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Caption;
import aiss.YouTubeMiner.model.VideoMinerModel.Comment;
import aiss.YouTubeMiner.model.VideoMinerModel.Video;
import aiss.YouTubeMiner.model.YoutubeModel.caption.CaptionSearch;
import aiss.YouTubeMiner.model.YoutubeModel.comment.CommentSearch;
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

import java.util.ArrayList;
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
    public List<Comment> findCommentsByVideoIdMax(String id, Integer num) {
        String url = uri+"/commentThreads?part=snippet&maxResults="+num+"&videoId="+id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", token);
        HttpEntity<CommentSearch> request = new HttpEntity<>(null,headers);
        try{
            ResponseEntity<CommentSearch> response = restTemplate.exchange(url, HttpMethod.GET,request, CommentSearch.class);
            List<Comment> comments =response.getBody().getItems().stream().map(Comment::new).collect(Collectors.toList());
            return comments;
        }
        catch (HttpClientErrorException.Forbidden e){
            List<Comment> comments = new ArrayList<>();
            return comments;
        }

    }
    public List<Caption> findCaptionById(String id) throws CaptionNotFoundException {

        try {
            String url = uri+"/captions?part=snippet&videoId="+id;
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-goog-api-key", token);
            HttpEntity<CaptionSearch> request = new HttpEntity<>(null, headers);

            ResponseEntity<CaptionSearch> response = restTemplate.exchange(url, HttpMethod.GET, request,CaptionSearch.class);
            List<Caption> captions = response.getBody().getItems().stream().map(Caption::new).collect(Collectors.toList());
            return  captions;
        }
        catch (HttpClientErrorException.NotFound e){
            throw new CaptionNotFoundException();
        }
    }
    public List<Video> findVideos(String channelId, Integer numVideos, Integer numMaxComments) throws CommentNotFoundException, CaptionNotFoundException {
        String url = uri + "/search?key="+token+"&channelId="+channelId+"&part=snippet&type=video&maxResults="+numVideos;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", token);
        HttpEntity<VideoSnippetSearch> request = new HttpEntity<>(null, headers);

        ResponseEntity<VideoSnippetSearch> response = restTemplate.exchange(url, HttpMethod.GET, request, VideoSnippetSearch.class);
        List<Video> videos = new ArrayList<>();
        for(int i = 0; i < response.getBody().getItems().size(); i++) {
            List<Comment> comments = findCommentsByVideoIdMax(response.getBody().getItems().get(i).getId().getVideoId(), numMaxComments);
            List<Caption> captions = findCaptionById(response.getBody().getItems().get(i).getId().getVideoId());
            Video video = new Video(response.getBody().getItems().get(i).getId().getVideoId(), response.getBody().getItems().get(i).getSnippet().getTitle(),
                    response.getBody().getItems().get(i).getSnippet().getDescription(),
                    response.getBody().getItems().get(i).getSnippet().getPublishedAt(), comments, captions);
            videos.add(video);
        }
        return videos;
    }
}
