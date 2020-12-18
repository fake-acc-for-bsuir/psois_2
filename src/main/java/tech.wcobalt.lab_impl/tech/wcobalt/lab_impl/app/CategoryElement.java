package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Category;
import tech.wcobalt.lab_impl.domain.Entry;

public class CategoryElement {
    private boolean isCategory;
    private Category category;
    private Entry entry;

    public CategoryElement(boolean isCategory, Category category, Entry entry) {
        this.isCategory = isCategory;
        this.category = category;
        this.entry = entry;
    }

    public boolean isCategory() {
        return isCategory;
    }

    public Category getCategory() {
        return category;
    }

    public Entry getEntry() {
        return entry;
    }
}
