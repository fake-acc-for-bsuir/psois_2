package tech.wcobalt.lab_impl.domain;

import java.util.Date;

public class Entry {
    private Date addDate;
    private String comment;
    private String author;
    private int id;
    private int category;
    private String typeId;
    private String title;

    public Entry(Date addDate, String comment, String author, int id, int category, String typeId, String title) {
        this.addDate = addDate;
        this.comment = comment;
        this.author = author;
        this.id = id;
        this.category = category;
        this.typeId = typeId;
        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getAddDate() {
        return addDate;
    }

    public String getComment() {
        return comment;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public int getCategory() {
        return category;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
