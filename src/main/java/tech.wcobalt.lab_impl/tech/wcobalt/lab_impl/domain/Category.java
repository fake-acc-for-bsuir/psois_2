package tech.wcobalt.lab_impl.domain;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id, parentCategory;
    private String name;
    private List<Integer> childrenCategories;

    public Category(int id, int parentCategory, String name) {
        this.id = id;
        this.parentCategory = parentCategory;
        this.name = name;

        childrenCategories = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getParentCategory() {
        return parentCategory;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getChildrenCategories() {
        return childrenCategories;
    }
}
