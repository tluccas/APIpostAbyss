package com.alvesdev.MemeAbyss.model.dto.Reddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostData {

    @JsonProperty("title")
    private String titulo;

    @JsonProperty("post_hint")
    private String tipoPost;

    @JsonProperty("url")
    private String url;

    @JsonProperty("preview")
    private Preview preview;

    @JsonProperty("url_overridden_by_dest")
    private String urlOverriddenByDest;

}
