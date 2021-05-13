package site.bitlab16.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class SourceRecord {
    @JsonProperty private Long point;
    @JsonProperty private int flow;
    @JsonProperty private Timestamp detectionTime;
    @JsonProperty private int weather;
    @JsonProperty private int season;
    @JsonProperty private boolean isHoliday;
    @JsonProperty private float timeIndex;
    @JsonProperty private float weatherIndex;
    @JsonProperty private float seasonIndex;
    @JsonProperty private float attractionsIndex;

    public SourceRecord(Long point,
                        int flow,
                        Timestamp detectionTime,
                        int weather,
                        int season,
                        boolean isHoliday,
                        float timeIndex,
                        float weatherIndex,
                        float seasonIndex,
                        float attractionsIndex) {
        this.point = point;
        this.flow = flow;
        this.detectionTime = detectionTime;
        this.weather = weather;
        this.season = season;
        this.isHoliday = isHoliday;
        this.timeIndex = timeIndex;
        this.weatherIndex = weatherIndex;
        this.seasonIndex = seasonIndex;
        this.attractionsIndex = attractionsIndex;
    }

    public Timestamp getDetectionTime() {
        return detectionTime;
    }

    public Long getPoint() {
        return point;
    }
}