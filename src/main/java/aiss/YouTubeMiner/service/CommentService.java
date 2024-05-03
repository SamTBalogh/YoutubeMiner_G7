package aiss.YouTubeMiner.service;


import aiss.YouTubeMiner.exception.CommentsNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Comment;
import aiss.YouTubeMiner.model.YoutubeModel.comment.CommentSearch;
import aiss.YouTubeMiner.model.YoutubeModel.comment.YoutubeComment;
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
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${youtube.api.token}")
    private String token;

    @Value("${youtube.api.uri}")
    private String uri;

    public List<Comment> findCommentsByVideoId(String id) throws  CommentsNotFoundException{
        try {
            String url = uri+"/commentThreads?part=snippet%2Creplies&videoId=" + id + "&key="+token ;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer"+ token);
            HttpEntity<CommentSearch> request = new HttpEntity<>(null,headers);

            ResponseEntity<CommentSearch> response = restTemplate.exchange(url, HttpMethod.GET,request, CommentSearch.class);
            List<Comment> comments =response.getBody().getItems().stream().map(Comment::new).collect(Collectors.toList());
            return comments;
        }
        catch (HttpClientErrorException.NotFound e) {
            throw new CommentsNotFoundException();
        }
    }

    public List<Comment> findCommentsByVideoIdMax(String id, Integer num) throws CommentsNotFoundException{
        try {
            String url = uri+"/commentThreads?part=snippet%2Creplies&maxResults="+num+"&videoId=" + id + "&key="+token ;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer"+ token);
            HttpEntity<CommentSearch> request = new HttpEntity<>(null,headers);

            ResponseEntity<CommentSearch> response = restTemplate.exchange(url, HttpMethod.GET,request, CommentSearch.class);
            List<Comment> comments =response.getBody().getItems().stream().map(Comment::new).collect(Collectors.toList());
            return comments;
        }
        catch (HttpClientErrorException.NotFound e) {
            throw new CommentsNotFoundException();
        }
    }
}

