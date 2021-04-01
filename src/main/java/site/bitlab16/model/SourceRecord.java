package site.bitlab16.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import site.bitlab16.TimeInstant;

import java.sql.Timestamp;
import java.util.Objects;

public class SourceRecord {
    @JsonProperty private Long pointId;
    @JsonProperty private Timestamp time;
    @JsonProperty private int peopleConcentration;
    @JsonProperty private int weather;
    @JsonProperty private int season;
    @JsonProperty private boolean holiday;
    @JsonProperty private float timeIndex;
    @JsonProperty private float weatherIndex;
    @JsonProperty private float seasonIndex;
    @JsonProperty private float attractionsIndex;

    public SourceRecord(
            Long pointId,
            TimeInstant time,
            int peopleConcentration,
            int weather,
            int season,
            boolean holiday,
            float timeIndex,
            float weatherIndex,
            float seasonIndex,
            float attractionsIndex) {
        this.pointId = pointId;
        this.time = new Timestamp(time.getTimeInMillisecond());
        this.peopleConcentration = peopleConcentration;
        this.weather = weather;
        this.season = season;
        this.holiday = holiday;
        this.timeIndex = timeIndex;
        this.weatherIndex = weatherIndex;
        this.seasonIndex = seasonIndex;
        this.attractionsIndex = attractionsIndex;
    }

    public Long getPointId() {
        return pointId;
    }

    public void setPointId(Long pointId) {
        this.pointId = pointId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getPeopleConcentration() {
        return peopleConcentration;
    }

    public void setPeopleConcentration(int peopleConcentration) {
        this.peopleConcentration = peopleConcentration;
    }

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public float getTimeIndex() {
        return timeIndex;
    }

    public void setTimeIndex(float timeIndex) {
        this.timeIndex = timeIndex;
    }

    public float getWeatherIndex() {
        return weatherIndex;
    }

    public void setWeatherIndex(float weatherIndex) {
        this.weatherIndex = weatherIndex;
    }

    public float getSeasonIndex() {
        return seasonIndex;
    }

    public void setSeasonIndex(float seasonIndex) {
        this.seasonIndex = seasonIndex;
    }

    public float getAttractionsIndex() {
        return attractionsIndex;
    }

    public void setAttractionsIndex(float attractionsIndex) {
        this.attractionsIndex = attractionsIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceRecord that = (SourceRecord) o;

        if (peopleConcentration != that.peopleConcentration) return false;
        if (holiday != that.holiday) return false;
        if (!pointId.equals(that.pointId)) return false;
        if (!time.equals(that.time)) return false;
        if (season != that.season) return false;
        if (!Objects.equals(timeIndex, that.timeIndex)) return false;
        if (!Objects.equals(weatherIndex, that.weatherIndex)) return false;
        if (!Objects.equals(seasonIndex, that.seasonIndex)) return false;
        return Objects.equals(attractionsIndex, that.attractionsIndex);
    }

    @Override
    public int hashCode() {
        int result = pointId.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + peopleConcentration;
        result = 31 * result + season;
        result = 31 * result + (holiday ? 1 : 0);
        result = 31 * result + (timeIndex != +0.0f ? Float.floatToIntBits(timeIndex) : 0);
        result = 31 * result + (weatherIndex != +0.0f ? Float.floatToIntBits(weatherIndex) : 0);
        result = 31 * result + (seasonIndex != +0.0f ? Float.floatToIntBits(seasonIndex) : 0);
        result = 31 * result + (attractionsIndex != +0.0f ? Float.floatToIntBits(attractionsIndex) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SourceRecord{" +
                "pointId=" + pointId +
                ", time=" + time +
                ", peopleConcentration=" + peopleConcentration +
                ", season=" + season +
                ", holiday=" + holiday +
                ", timeIndex=" + timeIndex +
                ", weatherIndex=" + weatherIndex +
                ", seasonIndex=" + seasonIndex +
                ", attractionsIndex=" + attractionsIndex +
                '}';
    }
}
