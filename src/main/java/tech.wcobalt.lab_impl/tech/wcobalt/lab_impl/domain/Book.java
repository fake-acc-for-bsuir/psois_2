package tech.wcobalt.lab_impl.domain;

import java.util.Date;

public class Book extends Entry {
    private Date publishingYear;
    private String publisher, coverUrl;
    public final static String TYPE_ID = "book";

    public Book(Date addDate, String comment, String author, int id, int category, String title, Date publishingYear, String publisher, String coverUrl) {
        super(addDate, comment, author, id, category, TYPE_ID, title);
        this.publishingYear = publishingYear;
        this.publisher = publisher;
        this.coverUrl = coverUrl;
    }

    public Date getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(Date publishingYear) {
        this.publishingYear = publishingYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
