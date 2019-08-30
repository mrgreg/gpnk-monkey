package com.gpnk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherReport {

    private double temp;
    private String skyCondition;

    @JsonProperty("currently")
    public void fromDarkSkyJson(final Map<String, String> currently) {
        this.temp = Double.parseDouble(currently.get("temperature"));
        this.skyCondition = currently.get("summary");
    }

    @Override
    public String toString() {
        return temp + " degrees and " + skyCondition;
    }
}
