package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.EntriesCollection;

import java.util.List;

public interface CollectionsRepository {
    EntriesCollection loadCollection(int id);

    List<EntriesCollection> loadAllCollections();

    EntriesCollection createCollection(EntriesCollection collection);

    void saveCollection(EntriesCollection collection);

    void removeCollection(EntriesCollection collection);
}
