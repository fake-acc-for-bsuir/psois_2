package tech.wcobalt.lab_impl.domain;

import java.util.Date;

public class BookFilter extends EntryFilter {
    private boolean doSearchByPublishingYear, doSearchByPublisher, onlyFullPublisherMatch;
    private String publisherValue;
    private Date publishingYearNotAfter, publishingYearNotBefore;

    public void setPublishingYearFilter(Date notBefore, Date notAfter) {
        doSearchByPublishingYear = true;
        publishingYearNotAfter = notAfter;
        publishingYearNotBefore = notBefore;
    }

    public void setPublisherFilter(String value, boolean onlyFullMatch) {
        doSearchByPublisher = true;
        publisherValue = value;
        onlyFullPublisherMatch = onlyFullMatch;
    }

    public boolean isDoSearchByPublishingYear() {
        return doSearchByPublishingYear;
    }

    public boolean isDoSearchByPublisher() {
        return doSearchByPublisher;
    }

    public boolean isOnlyFullPublisherMatch() {
        return onlyFullPublisherMatch;
    }

    public String getPublisherValue() {
        return publisherValue;
    }

    public Date getPublishingYearNotAfter() {
        return publishingYearNotAfter;
    }

    public Date getPublishingYearNotBefore() {
        return publishingYearNotBefore;
    }
}
