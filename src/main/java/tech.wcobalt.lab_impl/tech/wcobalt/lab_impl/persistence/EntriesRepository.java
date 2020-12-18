package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.domain.EntryFilter;

import java.util.List;

public interface EntriesRepository {
    Entry loadEntry(int id);

    List<Entry> loadAllEntries();

    Entry createEntry(Entry entry);

    void saveEntry(Entry entry);

    void removeEntry(Entry entry);

    List<Entry> loadEntriesByFilter(EntryFilter entryFilter);
}
