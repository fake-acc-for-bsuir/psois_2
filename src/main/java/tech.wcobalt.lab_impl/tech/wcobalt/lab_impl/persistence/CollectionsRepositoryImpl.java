package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.EntriesCollection;

import java.util.ArrayList;
import java.util.List;

public class CollectionsRepositoryImpl implements CollectionsRepository {
    private List<EntriesCollection> collections;
    private int nextId = 10000;

    public CollectionsRepositoryImpl(List<EntriesCollection> collections) {
        this.collections = collections;
    }

    @Override
    public EntriesCollection loadCollection(int id) {
        for (EntriesCollection collection : collections) {
            if (collection.getId() == id)
                return copyCollection(collection);
        }

        return null;
    }

    @Override
    public List<EntriesCollection> loadAllCollections() {
        List<EntriesCollection> copy = new ArrayList<>();

        for (EntriesCollection collection : collections)
            copy.add(copyCollection(collection));

        return copy;
    }

    @Override
    public EntriesCollection createCollection(EntriesCollection collection) {
        collection.setId(nextId++);

        collections.add(copyCollection(collection));

        return copyCollection(collection);
    }

    @Override
    public void saveCollection(EntriesCollection collection) {
        removeCollection(collection);

        collections.add(copyCollection(collection));
    }

    @Override
    public void removeCollection(EntriesCollection collection) {
        collections.removeIf(c -> c.getId() == collection.getId());
    }

    private EntriesCollection copyCollection(EntriesCollection collection) {
        List<Integer> copy = new ArrayList<>(collection.getEntries());

        return new EntriesCollection(collection.getId(), collection.getName(), collection.getCoverUrl(), copy);
    }
}
