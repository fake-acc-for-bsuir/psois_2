package tech.wcobalt.lab_impl.domain;

import java.util.Date;

public class CDAudio extends Entry {
    private Date releaseYear;
    private String label, coverUrl;
    public final static String TYPE_ID = "cdaudio";

    public CDAudio(Date addDate, String comment, String author, int id, int category, String title, Date releaseYear, String label, String coverUrl) {
        super(addDate, comment, author, id, category, TYPE_ID, title);
        this.releaseYear = releaseYear;
        this.label = label;
        this.coverUrl = coverUrl;
    }

    public Date getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Date releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
