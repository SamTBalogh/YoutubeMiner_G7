package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.ChannelNotFoundException;
import aiss.YouTubeMiner.exception.ListChannelsNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Channel;
import aiss.YouTubeMiner.model.YouTubeModel.channel.ChannelSearch;
import aiss.YouTubeMiner.model.YouTubeModel.channel.ChannelSnippet;
import aiss.YouTubeMiner.model.YouTubeModel.extended.channel.ChannelsSearch;
import aiss.YouTubeMiner.model.YouTubeModel.channel.YoutubeChannel;
import aiss.YouTubeMiner.model.YouTubeModel.extended.channel.YoutubeChannels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelService {
    @Value("${youtube.api.token}")
    private String token;

    @Value("${youtube.api.uri}")
    private String uri;

    @Autowired
    RestTemplate restTemplate;

    public Channel findChannelById(String id) throws ChannelNotFoundException {
        String url = uri + "/channels?part=snippet&id="+ id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", token);
        HttpEntity<YoutubeChannel> request=new HttpEntity<>(null,headers);

        ResponseEntity<ChannelSearch> response = restTemplate.exchange(url, HttpMethod.GET, request, ChannelSearch.class);

        List<YoutubeChannel> youtubeChannels = response.getBody().getItems();
        if (youtubeChannels == null) {
            throw new ChannelNotFoundException();
        }
        ChannelSnippet channelSnippet = youtubeChannels.get(0).getSnippet();
        return new Channel(id, channelSnippet.getTitle(), channelSnippet.getDescription(), channelSnippet.getPublishedAt());
    }

    public Channel findChannelByIdContentDetails(String id) throws ChannelNotFoundException {
        String url = uri + "/channels?part=snippet,contentDetails&id="+ id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", token);
        HttpEntity<YoutubeChannel> request=new HttpEntity<>(null,headers);

        ResponseEntity<ChannelSearch> response = restTemplate.exchange(url, HttpMethod.GET, request, ChannelSearch.class);

        List<YoutubeChannel> youtubeChannels = response.getBody().getItems();
        if (youtubeChannels == null) {
            throw new ChannelNotFoundException();
        }
        ChannelSnippet channelSnippet = youtubeChannels.get(0).getSnippet();
        return new Channel(id, channelSnippet.getTitle(), channelSnippet.getDescription(), channelSnippet.getPublishedAt(), youtubeChannels.get(0).getContent().getRelatedPlaylists().getUploads());
    }

    public List<Channel> findSearchListChannelsByName(String name) throws ListChannelsNotFoundException, ChannelNotFoundException {
        String url = uri + "/search?part=snippet&q="+name+"&type=channel";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", token);
        HttpEntity<YoutubeChannel> request=new HttpEntity<>(null,headers);

        ResponseEntity<ChannelsSearch> response = restTemplate.exchange(url, HttpMethod.GET, request, ChannelsSearch.class);

        List<YoutubeChannels> youtubeChannels = response.getBody().getItems();
        if (youtubeChannels.isEmpty()) {
            throw new ListChannelsNotFoundException();
        }
        List<Channel> channels = new ArrayList<>();
        for(YoutubeChannels ytChanngel : youtubeChannels){
            Channel channel = findChannelByIdContentDetails(ytChanngel.getId().getChannelId());
            channels.add(channel);
        }
        return channels;
    }

    public List<Channel> findSearchListChannelsByNameMax(String name, Integer maxResults) throws ListChannelsNotFoundException, ChannelNotFoundException {
        String url = uri + "/search?part=snippet&q="+name+"&type=channel&maxResults="+maxResults;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", token);
        HttpEntity<YoutubeChannel> request=new HttpEntity<>(null,headers);

        ResponseEntity<ChannelsSearch> response = restTemplate.exchange(url, HttpMethod.GET, request, ChannelsSearch.class);

        List<YoutubeChannels> youtubeChannels = response.getBody().getItems();
        if (youtubeChannels.isEmpty()) {
            throw new ListChannelsNotFoundException();
        }
        List<Channel> channels = new ArrayList<>();
        for(YoutubeChannels ytChannel : youtubeChannels){
            Channel channel = findChannelByIdContentDetails(ytChannel.getId().getChannelId());
            channels.add(channel);
        }
        return channels;
    }

}
