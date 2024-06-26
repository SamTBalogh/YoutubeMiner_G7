package aiss.YouTubeMiner.model.YouTubeModel.extended.channel;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelContentDetails {

    @JsonProperty("relatedPlaylists")
    private RelatedPlaylists relatedPlaylists;

    @JsonProperty("relatedPlaylists")
    public RelatedPlaylists getRelatedPlaylists() {
        return relatedPlaylists;
    }

    @JsonProperty("relatedPlaylists")
    public void setRelatedPlaylists(RelatedPlaylists relatedPlaylists) {
        this.relatedPlaylists = relatedPlaylists;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ChannelContentDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("relatedPlaylists");
        sb.append('=');
        sb.append(((this.relatedPlaylists == null)?"<null>":this.relatedPlaylists));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
