package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface FavoritesCRUDUseCase {
    void addEntryToFavorites(FamilyMember favoritesOwner, int entry);

    void removeEntryFromFavorites(FamilyMember favoritesOwner, int entry);

    List<Entry> loadEntriesFromFavorites(FamilyMember favoritesOwner);
}
