package site.bitlab16.model;

public class Indexes {

    private float timeIndex;
    private float weatherIndex;
    private float seasonIndex;
    private float attractionsIndex;

    public Indexes(float timeIndex, float weatherIndex, float seasonIndex, float attractionsIndex) {
        this.timeIndex = timeIndex;
        this.weatherIndex = weatherIndex;
        this.seasonIndex = seasonIndex;
        this.attractionsIndex = attractionsIndex;
    }

    public float getTimeIndex() {
        return timeIndex;
    }

    public float getWeatherIndex() {
        return weatherIndex;
    }

    public float getSeasonIndex() {
        return seasonIndex;
    }

    public float getAttractionsIndex() {
        return attractionsIndex;
    }
}
