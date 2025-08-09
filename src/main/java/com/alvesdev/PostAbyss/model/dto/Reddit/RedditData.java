package com.alvesdev.PostAbyss.model.dto.Reddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditData {
    private String after;
    private int dist;
    private String modhash;
    @JsonProperty("geo_filter")
    private Object geoFilter; // Pode ser null ou outro tipo

    private List<Child> children;
}
