package aiss.YouTubeMiner.service;


import aiss.YouTubeMiner.model.YoutubeModel.comment.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${youtube.api.token}")
    private String token;

    @Value("${youtube.api.uri}")
    private String uri;

    public List<Comment> findCommentsByVideoId(String id) {

        String url = uri+"/commentThreads?key=" + token + "&part=snippet&videoId=" + id ;

        ResponseEntity<Comments> response = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, Comments.class);

        comments = response.getBody().getItems();

        return comments;
    }

    public Comment getComment(String commentId) {
        Comment comment = null;
        String uri = "https://www.googleapis.com/youtube/v3/comments?key=" + token +
                "&part=" + snippet +"&id=" + commentId;


        ResponseEntity<Comment> response = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, Comment.class);

        comment = response.getBody();

        return comment;
    }
}

