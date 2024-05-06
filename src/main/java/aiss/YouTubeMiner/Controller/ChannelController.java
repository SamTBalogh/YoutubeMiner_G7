package aiss.YouTubeMiner.Controller;

import aiss.YouTubeMiner.exception.*;
import aiss.YouTubeMiner.model.VideoMinerModel.Channel;
import aiss.YouTubeMiner.model.VideoMinerModel.Video;
import aiss.YouTubeMiner.service.CaptionService;
import aiss.YouTubeMiner.service.CommentService;
import aiss.YouTubeMiner.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import aiss.YouTubeMiner.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Tag(name="Channel", description="Channel management API")
@RestController
@RequestMapping("/youTubeMiner/v1")
public class ChannelController {

    @Value("${videoMiner.url}")
    String videoMinerUrl;

    @Autowired
    ChannelService channelService;

    @Autowired
    VideoService videoService;

    @Autowired
    CaptionService captionService;

    @Autowired
    CommentService commentService;

    // POST http://localhost:8082/youTubeMiner/v1/{id}
    @Operation(summary = "Send a Channel ",
            description = "Post a Channel object to VideoMiner from the YouTube's API by specifying the channel Id, the Channel data is sent in the body of the request in JSON format",
            tags = {"channels", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    public Channel PostChannelVideo(@PathVariable("id") String id,
                                    @RequestParam(name = "maxVideos", defaultValue = "10") Integer maxVideos,
                                    @RequestParam(name = "maxComments", defaultValue = "10") Integer maxComments,
                                    @RequestHeader(name = "Authorization", required = false) String token) throws ChannelNotFoundException, ForbiddenException, VideoNotFoundException {

        RestTemplate restTemplate = new RestTemplate();

        Channel channel = channelService.findChannelById(id);
        List<Video> videos = videoService.findVideosMax(id, maxVideos);
        channel.setVideos(videos);
        for (Video video : videos) {
            video.setComments(commentService.findCommentsByVideoIdMax(video.getId(), maxComments));
            video.setCaptions(captionService.findCaptionsByVideoId(video.getId()));
        }

        channel.setVideos(videos);

        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            headers.add("Authorization", token);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Channel> requestEntity = new HttpEntity<>(channel, headers);
        try {
            restTemplate.exchange(videoMinerUrl + "/channels", HttpMethod.POST, requestEntity, Void.class);
        } catch (HttpClientErrorException e) {
            throw new ForbiddenException(ForbiddenException.parse(e.getMessage()));
        }

        return channel;
    }
    // GET http://localhost:8082/youTubeMiner/v1/{id}
    @Operation( summary = "Retrieve a Channel by Id",
            description = "Get a Channel object from the YouTube's API by specifying its id",
            tags = {"channels", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema=@Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Channel GetChannelVideo(@PathVariable("id") String id,
                                   @RequestParam(name = "maxVideos", defaultValue = "10") Integer maxVideos,
                                   @RequestParam(name = "maxComments", defaultValue = "10") Integer maxComments) throws ChannelNotFoundException, VideoNotFoundException {

        Channel channel = channelService.findChannelById(id);

        List<Video> videos = videoService.findVideosMax(id, maxVideos);

        for(Video video : videos){
            video.setCaptions(captionService.findCaptionsByVideoId(video.getId()));
            video.setComments(commentService.findCommentsByVideoIdMax(video.getId(), maxComments));
        }

        channel.setVideos(videos);

        return channel;

    }

}
