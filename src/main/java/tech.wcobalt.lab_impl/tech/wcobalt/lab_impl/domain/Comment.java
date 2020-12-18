package tech.wcobalt.lab_impl.domain;

public class Comment {
    private int id, entry, author;
    private String content;

    public Comment(int id, int entry, int author, String content) {
        this.id = id;
        this.entry = entry;
        this.author = author;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntry() {
        return entry;
    }

    public void setEntry(int entry) {
        this.entry = entry;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
