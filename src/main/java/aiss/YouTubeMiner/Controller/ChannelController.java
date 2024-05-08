package aiss.YouTubeMiner.Controller;

import aiss.YouTubeMiner.exception.*;
import aiss.YouTubeMiner.model.VideoMinerModel.Channel;
import aiss.YouTubeMiner.model.VideoMinerModel.Video;
import aiss.YouTubeMiner.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Tag(name="Channel", description="Channel management API")
@RestController
@RequestMapping("/youTubeMiner")
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

    @Autowired
    UploadService uploadService;

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
    @PostMapping("/v1/{id}")
    public Channel PostChannelVideo(@PathVariable("id") String id,
                                    @RequestParam(name = "maxVideos", defaultValue = "10") Integer maxVideos,
                                    @RequestParam(name = "maxComments", defaultValue = "10") Integer maxComments,
                                    @RequestHeader(name = "Authorization", required = false) String token) throws ChannelNotFoundException, ForbiddenException, VideoNotFoundChannelIDException, CaptionNotFoundException, CommentNotFoundException {

        RestTemplate restTemplate = new RestTemplate();

        Channel channel = channelService.findChannelById(id);
        List<Video> videos = videoService.findSearchVideosMaxChannelId(id, maxVideos);
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
    @GetMapping("/v1/{id}")
    public Channel GetChannelVideo(@PathVariable("id") String id,
                                   @RequestParam(name = "maxVideos", defaultValue = "10") Integer maxVideos,
                                   @RequestParam(name = "maxComments", defaultValue = "10") Integer maxComments) throws ChannelNotFoundException, VideoNotFoundChannelIDException, CaptionNotFoundException, CommentNotFoundException {

        Channel channel = channelService.findChannelById(id);

        List<Video> videos = videoService.findSearchVideosMaxChannelId(id, maxVideos);

        for(Video video : videos){
            video.setCaptions(captionService.findCaptionsByVideoId(video.getId()));
            video.setComments(commentService.findCommentsByVideoIdMax(video.getId(), maxComments));
        }

        channel.setVideos(videos);

        return channel;

    }

    // POST http://localhost:8082/youTubeMiner/v1/channels
    @Operation(summary = "Send a List of Channels by searching their name",
            description = "Post a series of Channel objects to VideoMiner from the YouTube's API by searching their name, each Channel data is sent in the body of the request in JSON format",
            tags = {"channels", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v1/channels")
    public List<Channel> PostListChannelsVideo(@RequestParam("name") String name,
                                               @RequestParam(name = "maxChannels", defaultValue = "3") Integer maxChannels,
                                               @RequestParam(name = "maxVideos", defaultValue = "10") Integer maxVideos,
                                               @RequestParam(name = "maxComments", defaultValue = "10") Integer maxComments,
                                               @RequestHeader(name = "Authorization", required = false) String token) throws ForbiddenException, VideoNotFoundChannelIDException, CaptionNotFoundException, ListChannelsNotFoundException, CommentNotFoundException, ChannelNotFoundException {

        RestTemplate restTemplate = new RestTemplate();

        List<Channel> channels = channelService.findSearchListChannelsByNameMax(name, maxChannels);
        for(Channel channel : channels) {
            List<Video> videos = videoService.findSearchVideosMaxChannelId(channel.getId(), maxVideos);
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
        }

        return channels;
    }


    // GET http://localhost:8082/youTubeMiner/v1/{id}
    @Operation( summary = "Retrieve a List of Channels by Id",
            description = "Get a List of Channel objects from the YouTube's API by searching their name.",
            tags = {"channels", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema=@Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema=@Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1/channels")
    public List<Channel> GetListChannelsVideo(@RequestParam("name") String name,
                                              @RequestParam(name = "maxChannels", defaultValue = "3") Integer maxChannels,
                                              @RequestParam(name = "maxVideos", defaultValue = "10") Integer maxVideos,
                                              @RequestParam(name = "maxComments", defaultValue = "10") Integer maxComments) throws VideoNotFoundChannelIDException, CaptionNotFoundException, ListChannelsNotFoundException, CommentNotFoundException, ChannelNotFoundException {

        List<Channel> channels = channelService.findSearchListChannelsByNameMax(name, maxChannels);
        for (Channel channel : channels) {
            List<Video> videos = videoService.findSearchVideosMaxChannelId(channel.getId(), maxVideos);
            channel.setVideos(videos);
            for (Video video : videos) {
                video.setComments(commentService.findCommentsByVideoIdMax(video.getId(), maxComments));
                video.setCaptions(captionService.findCaptionsByVideoId(video.getId()));
            }

            channel.setVideos(videos);

        }
        return channels;
    }

    //
    //EMPLEANDO LOS NUEVOS MODELOS
    //



    // POST http://localhost:8082/youTubeMiner/v2/{id}
    @Operation(summary = "Send a Channel ",
            description = "Post a Channel object to VideoMiner from the YouTube's API by specifying the channel Id, the Channel data is sent in the body of the request in JSON format. This version is using the new models implemented",
            tags = {"channels", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v2/{id}")
    public Channel PostChannelVideoV2(@PathVariable("id") String id,
                                      @RequestParam(name = "maxVideos", defaultValue = "10") Integer maxVideos,
                                      @RequestParam(name = "maxComments", defaultValue = "10") Integer maxComments,
                                      @RequestHeader(name = "Authorization", required = false) String token) throws ChannelNotFoundException, ForbiddenException, CaptionNotFoundException, CommentNotFoundException, UploadsNotFoundException, VideoNotFoundException {

        RestTemplate restTemplate = new RestTemplate();

        Channel channel = channelService.findChannelByIdContentDetails(id);
        List<String> uploads = uploadService.findUploadsIdsMax(channel.getUploads(), maxVideos);
        List<Video> videos = new ArrayList<>();
        for(String videoId : uploads){
            videos.add(videoService.findVideoById(videoId));
        }
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

    // GET http://localhost:8082/youTubeMiner/v2/{id}
    @Operation( summary = "Retrieve a Channel by Id",
            description = "Get a Channel object from the YouTube's API by specifying its id. This version is using the new models implemented",
            tags = {"channels", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema=@Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v2/{id}")
    public Channel GetChannelVideoV2(@PathVariable("id") String id,
                                     @RequestParam(name = "maxVideos", defaultValue = "10") Integer maxVideos,
                                     @RequestParam(name = "maxComments", defaultValue = "10") Integer maxComments) throws ChannelNotFoundException, CaptionNotFoundException, CommentNotFoundException, UploadsNotFoundException, VideoNotFoundException {

        Channel channel = channelService.findChannelByIdContentDetails(id);
        List<String> uploads = uploadService.findUploadsIdsMax(channel.getUploads(), maxVideos);
        List<Video> videos = new ArrayList<>();
        for(String videoId : uploads){
            videos.add(videoService.findVideoById(videoId));
        }
        channel.setVideos(videos);
        for (Video video : videos) {
            video.setComments(commentService.findCommentsByVideoIdMax(video.getId(), maxComments));
            video.setCaptions(captionService.findCaptionsByVideoId(video.getId()));
        }

        return channel;

    }

    // POST http://localhost:8082/youTubeMiner/v2/channels
    @Operation(summary = "Send a List of Channels by searching their name",
            description = "Post a series of Channel objects to VideoMiner from the YouTube's API by searching their name, each Channel data is sent in the body of the request in JSON format. This version is using the new models implemented",
            tags = {"channels", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v2/channels")
    public List<Channel> PostListChannelsVideoV2(@RequestParam("name") String name,
                                                 @RequestParam(name = "maxChannels", defaultValue = "3") Integer maxChannels,
                                                 @RequestParam(name = "maxVideos", defaultValue = "10") Integer maxVideos,
                                                 @RequestParam(name = "maxComments", defaultValue = "10") Integer maxComments,
                                                 @RequestHeader(name = "Authorization", required = false) String token) throws ForbiddenException, CaptionNotFoundException, ListChannelsNotFoundException, CommentNotFoundException, ChannelNotFoundException, VideoNotFoundException, UploadsNotFoundException {

        RestTemplate restTemplate = new RestTemplate();

        List<Channel> channels = channelService.findSearchListChannelsByNameMax(name, maxChannels);
        for(Channel channel : channels) {
            List<String> uploads = uploadService.findUploadsIdsMax(channel.getUploads(), maxVideos);
            List<Video> videos = new ArrayList<>();
            for(String videoId : uploads){
                videos.add(videoService.findVideoById(videoId));
            }
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
        }

        return channels;
    }


    // GET http://localhost:8082/youTubeMiner/v2/{id}
    @Operation( summary = "Retrieve a List of Channels by Id",
            description = "Get a List of Channel objects from the YouTube's API by searching their name. This version is using the new models implemented",
            tags = {"channels", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema=@Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema=@Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v2/channels")
    public List<Channel> GetListChannelsVideoV2(@RequestParam("name") String name,
                                                @RequestParam(name = "maxChannels", defaultValue = "3") Integer maxChannels,
                                              @RequestParam(name = "maxVideos", defaultValue = "10") Integer maxVideos,
                                              @RequestParam(name = "maxComments", defaultValue = "10") Integer maxComments) throws CaptionNotFoundException, ListChannelsNotFoundException, CommentNotFoundException, ChannelNotFoundException, UploadsNotFoundException, VideoNotFoundException {

        List<Channel> channels = channelService.findSearchListChannelsByNameMax(name, maxChannels);
        for(Channel channel : channels) {
            List<String> uploads = uploadService.findUploadsIdsMax(channel.getUploads(), maxVideos);
            List<Video> videos = new ArrayList<>();
            for(String videoId : uploads){
                videos.add(videoService.findVideoById(videoId));
            }
            for (Video video : videos) {
                video.setComments(commentService.findCommentsByVideoIdMax(video.getId(), maxComments));
                video.setCaptions(captionService.findCaptionsByVideoId(video.getId()));
            }

            channel.setVideos(videos);

        }
        return channels;
    }

}
