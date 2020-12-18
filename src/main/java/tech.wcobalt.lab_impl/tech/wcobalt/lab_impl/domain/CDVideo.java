package tech.wcobalt.lab_impl.domain;

import java.util.Date;

public class CDVideo extends Entry {
    private Date releaseYear;
    private String coverUrl;
    public static final String TYPE_ID = "cdvideo";

    public CDVideo(Date addDate, String comment, String author, int id, int category, String title, Date releaseYear, String coverUrl) {
        super(addDate, comment, author, id, category, TYPE_ID, title);
        this.releaseYear = releaseYear;
        this.coverUrl = coverUrl;
    }

    public Date getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Date releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
