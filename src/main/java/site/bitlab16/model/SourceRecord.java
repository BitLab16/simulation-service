package site.bitlab16.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import site.bitlab16.TimeInstant;

public class SourceRecord {
    @JsonProperty
    private Long sourceCode;

    @JsonProperty
    private String date;

    @JsonProperty
    private int stringTime;

    @JsonProperty
    private int peopleConcentration;

    public SourceRecord(Long sourceCode, TimeInstant time, int peopleConcentration) {
        this.sourceCode = sourceCode;
        this.peopleConcentration = peopleConcentration;
        this.date = time.getDay().getCalendarType();
        this.stringTime = time.getMinute().getMinute();
    }

    public Long getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(Long sourceCode) {
        this.sourceCode = sourceCode;
    }

    public int getPeopleConcentration() {
        return peopleConcentration;
    }

    public void setPeopleConcentration(int peopleConcentration) {
        this.peopleConcentration = peopleConcentration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStringTime() {
        return stringTime;
    }

    public void setStringTime(int stringTime) {
        this.stringTime = stringTime;
    }

    @Override
    public String toString() {
        return "SourceRecord{" +
                "sourceCode=" + sourceCode +
                ", peopleConcentration=" + peopleConcentration +
                '}';
    }
}
