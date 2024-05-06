package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.CommentNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService service;

    @Test
    @DisplayName("Get comments from a video id")
    void findCommentsFromVideoId() throws CommentNotFoundException {
        List<Comment> comment = service.findCommentsByVideoId("_VB39Jo8mAQ");
        assertNotNull(comment);
        System.out.println(comment);
    }

    @Test
    @DisplayName("Get comments from a video with comments disabled")
    void findCommentsFromVideoIdDisabled() throws CommentNotFoundException {
        List<Comment> comment = service.findCommentsByVideoId("KZ613lCnoJ0");
        assertNotNull(comment);
        System.out.println(comment);
    }

    @Test
    @DisplayName("Get comments from an invalid video Id")
    void findCommentsFromInvalidVideoId() throws CommentNotFoundException {
        List<Comment> comment = service.findCommentsByVideoId("Wololo");
        assertNotNull(comment);
        System.out.println(comment);
    }

    @Test
    @DisplayName("Get a certain number of comments from a video")
    void findMaxCommentsFromVideoId() throws CommentNotFoundException {
        List<Comment> comment = service.findCommentsByVideoIdMax("mKIhHNznt4s", 4);
        assertNotNull(comment);
        System.out.println(comment);
        System.out.println(comment.toArray().length);
    }

    @Test
    @DisplayName("Get comments from a video with comments disabled")
    void findMaxCommentsFromVideoIdDisabled() throws CommentNotFoundException {
        List<Comment> comment = service.findCommentsByVideoIdMax("KZ613lCnoJ0", 4);
        assertNotNull(comment);
        System.out.println(comment);
        System.out.println(comment.toArray().length);
    }

    @Test
    @DisplayName("Get comments from an invalid video Id")
    void findMaxCommentsFromInvalidVideoId() throws CommentNotFoundException {
        List<Comment> comment = service.findCommentsByVideoId("Wololo");
        assertNotNull(comment);
        System.out.println(comment);
    }
}