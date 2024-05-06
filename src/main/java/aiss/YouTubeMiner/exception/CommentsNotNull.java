package aiss.YouTubeMiner.exception;

import aiss.YouTubeMiner.model.YoutubeModel.comment.YoutubeComment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "comments are disable")
public class CommentsNotNull extends Exception {
    public List<YoutubeComment> CommentsNotNull(){
        return new ArrayList<YoutubeComment>();
    }
}
