package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.exception.CaptionsNotFoundException;
import aiss.YouTubeMiner.model.YoutubeModel.caption.CaptionSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import aiss.YouTubeMiner.model.VideoMinerModel.Caption;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class CaptionService {
    @Value("${youtube.api.token}")
    private String token;

    @Value("${youtube.api.uri}")
    private String uri;

    @Autowired
    RestTemplate restTemplate;

    public List<Caption> findCaptionById(String id) throws CaptionsNotFoundException {

        try {
            String url = uri+ "/captions?part=snippet&videoId="+id+"&key="+token;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer"+ token);
            HttpEntity<CaptionSearch> request = new HttpEntity<>(null, headers);

            ResponseEntity<CaptionSearch> response = restTemplate.exchange(url, HttpMethod.GET, request,CaptionSearch.class);
            List<Caption> captions = response.getBody().getItems().stream().map(Caption::new).collect(Collectors.toList());
            return  captions;
        }
        catch (HttpClientErrorException.NotFound e){
            throw new CaptionsNotFoundException();
        }
    }
}
