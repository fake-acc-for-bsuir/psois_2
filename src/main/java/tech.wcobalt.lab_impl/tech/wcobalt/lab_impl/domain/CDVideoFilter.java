package tech.wcobalt.lab_impl.domain;

import java.util.Date;

public class CDVideoFilter extends EntryFilter {
    private boolean doSearchByReleaseYear;
    private Date releaseYearNotAfter, releaseYearNotBefore;

    public void setReleaseYearFilter(Date notBefore, Date notAfter) {
        doSearchByReleaseYear = true;
        releaseYearNotAfter = notAfter;
        releaseYearNotBefore = notBefore;
    }

    public boolean isDoSearchByReleaseYear() {
        return doSearchByReleaseYear;
    }

    public Date getReleaseYearNotAfter() {
        return releaseYearNotAfter;
    }

    public Date getReleaseYearNotBefore() {
        return releaseYearNotBefore;
    }
}
