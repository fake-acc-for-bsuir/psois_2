package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.domain.EntryFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntriesRepositoryImpl implements EntriesRepository {
    private CategoriesRepository categoriesRepository;
    private List<Entry> entries;
    private int nextId = 10000;

    public EntriesRepositoryImpl(CategoriesRepository categoriesRepository, List<Entry> entries) {
        this.categoriesRepository = categoriesRepository;
        this.entries = entries;
    }

    @Override
    public Entry loadEntry(int id) {
        for (Entry entry : entries) {
            if (entry.getId() == id)
                return copyEntry(entry);
        }

        return null;
    }

    @Override
    public List<Entry> loadAllEntries() {
        List<Entry> result = new ArrayList<>();

        for (Entry entry : entries)
            result.add(copyEntry(entry));

        return result;
    }

    @Override
    public Entry createEntry(Entry entry) {
        entry.setId(nextId++);

        entries.add(copyEntry(entry));

        return copyEntry(entry);
    }

    @Override
    public void saveEntry(Entry entry) {
        removeEntry(entry);

        entries.add(copyEntry(entry));
    }

    @Override
    public void removeEntry(Entry entry) {
        entries.removeIf(e -> e.getId() == entry.getId());
    }

    @Override
    public List<Entry> loadEntriesByFilter(EntryFilter entryFilter) {
        List<Entry> result = new ArrayList<>();

        for (Entry entry : entries) {
            //i just don't care
            if (checkEntryFilter(entry, entryFilter))
                result.add(copyEntry(entry));
        }

        return result;
    }

    private Entry copyEntry(Entry entry) {
        return new Entry(new Date(entry.getAddDate().getTime()), entry.getComment(), entry.getAuthor(), entry.getId(),
                entry.getCategory(), entry.getTypeId(), entry.getTitle());
    }

    boolean checkEntryFilter(Entry entry, EntryFilter entryFilter) {
        return checkDate(entry.getAddDate(), entryFilter.getAddDateNotAfter(), entryFilter.getAddDateNotBefore(), entryFilter.isDoSearchByAddDate()) &&
                checkTextFilterMatch(entry.getAuthor(), entryFilter.getAuthorValue(), entryFilter.isOnlyFullAuthorMatch(), entryFilter.isDoSearchByAuthor()) &&
                checkTextFilterMatch(entry.getComment(), entryFilter.getCommentValue(), entryFilter.isOnlyFullCommentMatch(), entryFilter.isDoSearchByComment()) &&
                checkTextFilterMatch(entry.getTitle(), entryFilter.getTitleValue(), entryFilter.isOnlyFullTitleMatch(), entryFilter.isDoSearchByTitle()) &&
                (!entryFilter.isDoSearchByCategory()
                        || entry.getCategory() == entryFilter.getCategoryForSearch()
                        ||categoriesRepository.loadChildrenCategories(entryFilter.getCategoryForSearch(),
                        entryFilter.isDoSearchByCategoryRecursively()).stream().anyMatch(c -> entry.getCategory() == c.getId()));
    }

    boolean checkTextFilterMatch(String original, String value, boolean onlyFullMatch, boolean doSearch) {
        return !doSearch || ((onlyFullMatch && original.contains(value.toLowerCase())) || (!onlyFullMatch && original.toLowerCase().equals(value.toLowerCase())));
    }

    boolean checkDate(Date original, Date notAfter, Date notBefore, boolean doSearch) {
        return !doSearch || (original.before(notAfter) && original.after(notBefore));
    }
}
