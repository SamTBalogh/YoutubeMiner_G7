package aiss.YouTubeMiner.model.VideoMinerModel;

import aiss.YouTubeMiner.model.YoutubeModel.comment.YoutubeComment;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * @author Juan C. Alonso
 */
@Entity
@Table(name = "Comment")
public class Comment {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("text")
    @Column(columnDefinition="TEXT")
    private String text;

    @JsonProperty("createdOn")
    private String createdOn;

    @JsonProperty("author")
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "Comment author cannot be null")
    private User author;

    public Comment() {}

    public Comment(YoutubeComment youtubeComment) {
        this.id = youtubeComment.getCommentSnippet().getTopLevelComment().getId();
        this.text = youtubeComment.getCommentSnippet().getTopLevelComment().getSnippet().getTextOriginal();
        this.createdOn = youtubeComment.getCommentSnippet().getTopLevelComment().getSnippet().getPublishedAt();
        this.author = new User(youtubeComment.getCommentSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName(), youtubeComment.getCommentSnippet().getTopLevelComment().getSnippet().getAuthorChannelUrl(),youtubeComment.getCommentSnippet().getTopLevelComment().getSnippet().getAuthorProfileImageUrl());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", author=" + author +
                '}';
    }
}
