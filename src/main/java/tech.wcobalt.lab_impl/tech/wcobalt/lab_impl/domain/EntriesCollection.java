package tech.wcobalt.lab_impl.domain;

import java.util.List;

public class EntriesCollection {
    private int id;
    private String name, coverUrl;
    private List<Integer> entries;

    public EntriesCollection(int id, String name, String coverUrl, List<Integer> entries) {
        this.id = id;
        this.name = name;
        this.coverUrl = coverUrl;
        this.entries = entries;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<Integer> getEntries() {
        return entries;
    }
}
