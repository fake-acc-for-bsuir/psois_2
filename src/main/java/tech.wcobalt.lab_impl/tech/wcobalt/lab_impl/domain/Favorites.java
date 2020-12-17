package tech.wcobalt.lab_impl.domain;

import java.util.ArrayList;
import java.util.List;

public class Favorites {
    private List<Integer> entries;
    private int familyMember;

    public Favorites(int familyMember) {
        this.entries = new ArrayList<>();
        this.familyMember = familyMember;
    }

    public List<Integer> getEntries() {
        return entries;
    }

    public int getFamilyMember() {
        return familyMember;
    }
}
