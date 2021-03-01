package site.bitlab16.model;

import site.bitlab16.TimeInstant;

public class SourceRecord {
    private Long sourceCode;
    private TimeInstant time;
    private int peopleConcentration;

    public SourceRecord(Long sourceCode, TimeInstant time, int peopleConcentration) {
        this.sourceCode = sourceCode;
        this.time = time;
        this.peopleConcentration = peopleConcentration;
    }

    public Long getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(Long sourceCode) {
        this.sourceCode = sourceCode;
    }

    public TimeInstant getTime() {
        return time;
    }

    public void setTime(TimeInstant time) {
        this.time = time;
    }

    public int getPeopleConcentration() {
        return peopleConcentration;
    }

    public void setPeopleConcentration(int peopleConcentration) {
        this.peopleConcentration = peopleConcentration;
    }

    @Override
    public String toString() {
        return "SourceRecord{" +
                "sourceCode=" + sourceCode +
                ", time=" + time +
                ", peopleConcentration=" + peopleConcentration +
                '}';
    }
}
