package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.ChannelNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Channel;
import aiss.YouTubeMiner.model.YoutubeModel.channel.ChannelSearch;
import aiss.YouTubeMiner.model.YoutubeModel.channel.YoutubeChannels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ChannelService {
    @Value("${youtube.api.token}")
    private String token;

    @Value("${youtube.api.uri}")
    private String uri;

    @Autowired
    RestTemplate restTemplate;
    public Channel findchannelById(String id) throws ChannelNotFoundException {
        try {
            String url = uri + "/channels?part=snippet&id="+ id;
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-goog-api-key", token);
            HttpEntity<YoutubeChannels> request=new HttpEntity<>(null,headers);

            ResponseEntity<ChannelSearch> response = restTemplate.exchange(url, HttpMethod.GET, request, ChannelSearch.class);

            ChannelSearch youtubeChannels = response.getBody();
            if (youtubeChannels == null || youtubeChannels.getItems()==null || youtubeChannels.getItems().get(0).getSnippet() == null) {
                throw new ChannelNotFoundException();
            }
            Channel channel= new Channel(id, youtubeChannels.getItems().get(0).getSnippet().getTitle(), youtubeChannels.getItems().get(0).getSnippet().getDescription(), youtubeChannels.getItems().get(0).getSnippet().getPublishedAt());
            return channel;
        }
        catch (HttpClientErrorException.NotFound e) {
            throw new ChannelNotFoundException();

        }
    }
}
