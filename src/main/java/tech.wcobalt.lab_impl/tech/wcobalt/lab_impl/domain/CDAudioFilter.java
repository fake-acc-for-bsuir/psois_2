package tech.wcobalt.lab_impl.domain;

import java.util.Date;

public class CDAudioFilter extends EntryFilter {
    private boolean doSearchByReleaseYear, doSearchByLabel, onlyFullLabelMatch;
    private Date releaseYearNotAfter, releaseYearNotBefore;
    private String labelValue;

    public void setLabelFilter(String value, boolean onlyFullMatch) {
        doSearchByLabel = true;
        labelValue = value;
        onlyFullLabelMatch = onlyFullMatch;
    }

    public void setReleaseYearFilter(Date notBefore, Date notAfter) {
        doSearchByReleaseYear = true;
        releaseYearNotAfter = notAfter;
        releaseYearNotBefore = notBefore;
    }

    public boolean isDoSearchByReleaseYear() {
        return doSearchByReleaseYear;
    }

    public boolean isDoSearchByLabel() {
        return doSearchByLabel;
    }

    public boolean isOnlyFullLabelMatch() {
        return onlyFullLabelMatch;
    }

    public Date getReleaseYearNotAfter() {
        return releaseYearNotAfter;
    }

    public Date getReleaseYearNotBefore() {
        return releaseYearNotBefore;
    }

    public String getLabelValue() {
        return labelValue;
    }
}
