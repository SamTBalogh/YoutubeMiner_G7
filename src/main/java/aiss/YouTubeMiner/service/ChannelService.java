package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.ChannelNotFoundException;
import aiss.YouTubeMiner.model.VideoMinerModel.Channel;
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
            String url = uri + "/channels?part=snippet%2CcontentDetails%2Cstatistics&id="+id+"&key="+token;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer" + token);
            HttpEntity<YoutubeChannels> request=new HttpEntity<>(null,headers);

            ResponseEntity<YoutubeChannels> response = restTemplate.exchange(url, HttpMethod.GET, request, YoutubeChannels.class);

            YoutubeChannels youtubeChannels = response.getBody();
            Channel channel= new Channel(id, youtubeChannels.getSnippet().getTitle(), youtubeChannels.getSnippet().getDescription(), youtubeChannels.getSnippet().getPublishedAt());
            return channel;
        }
        catch (HttpClientErrorException.NotFound e) {
            throw new ChannelNotFoundException();

        }
    }
}
